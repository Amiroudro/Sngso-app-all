package com.sarangsho.plus.data.edition

import android.content.Context
import org.json.JSONArray
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL
import java.time.Instant

/**
 * Read-only client for the published daily-edition feed.
 *
 * The publisher remains the editorial authority. This client only downloads
 * published JSON, validates its edition identity, and caches the exact payload.
 */
object EditionFeedRepository {
    private const val PREFS = "sarangsho_edition_feed"
    private const val KEY_DATA = "edition_data"
    private const val KEY_BN = "edition_bn"
    private const val KEY_FETCHED_AT = "fetched_at"
    private const val KEY_SELECTED_STORY = "selected_story_id"
    private const val BASE = "https://amiroudro.github.io/sarangsho-plus-editions/"
    private const val INDEX_PATH = "editions/index.json"

    data class Snapshot(
        val date: String,
        val day: String,
        val coverage: String,
        val fetchedAt: String,
        val stale: Boolean,
        val stories: List<Story>
    )

    data class Story(
        val id: String,
        val category: String,
        val headline: String,
        val summary: String,
        val oneLiner: String,
        val whyItMatters: String,
        val examFacts: String,
        val detail: String,
        val sources: List<Source>,
        val keyFacts: List<KeyFact>
    )

    data class Source(
        val publication: String,
        val headline: String,
        val date: String,
        val url: String
    )

    data class KeyFact(val label: String, val value: String, val original: String)

    fun cached(context: Context): Snapshot? {
        val prefs = context.applicationContext.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
        val data = prefs.getString(KEY_DATA, null) ?: return null
        val bn = prefs.getString(KEY_BN, null)
        val fetchedAt = prefs.getString(KEY_FETCHED_AT, null) ?: return null
        return try {
            parseEdition(data, bn, fetchedAt, stale = true)
        } catch (_: Exception) {
            null
        }
    }

    suspend fun refresh(context: Context): Snapshot {
        val prefs = context.applicationContext.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
        val index = JSONObject(fetchText(INDEX_PATH))
        val editions = index.optJSONArray("editions") ?: error("Edition index has no editions array")
        val latest = (0 until editions.length())
            .map { editions.getJSONObject(it) }
            .filter { it.optString("status") == "published" }
            .maxByOrNull { it.optString("edition_date") }
            ?: error("No published edition is available")

        val date = latest.optString("edition_date")
        require(Regex("^\\d{4}-\\d{2}-\\d{2}$").matches(date)) { "Invalid edition date" }
        val file = latest.optString("file")
        require(file == "data-$date.json") { "Edition index points to an unexpected file" }

        val data = fetchText("editions/$file")
        val overlay = try {
            fetchText("editions/bn-$date.json")
        } catch (_: Exception) {
            null
        }
        val fetchedAt = Instant.now().toString()
        val snapshot = parseEdition(data, overlay, fetchedAt, stale = false)
        require(snapshot.date == date) { "Edition file date does not match the published index" }

        prefs.edit()
            .putString(KEY_DATA, data)
            .putString(KEY_BN, overlay)
            .putString(KEY_FETCHED_AT, fetchedAt)
            .apply()
        return snapshot
    }

    fun selectedStoryId(context: Context): String =
        context.applicationContext.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
            .getString(KEY_SELECTED_STORY, "").orEmpty()

    fun selectStory(context: Context, storyId: String) {
        context.applicationContext.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
            .edit().putString(KEY_SELECTED_STORY, storyId).apply()
    }

    fun story(context: Context, storyId: String): Story? =
        cached(context)?.stories?.firstOrNull { it.id == storyId }

    private suspend fun fetchText(path: String): String =
        kotlinx.coroutines.withContext(kotlinx.coroutines.Dispatchers.IO) {
            val url = URL(BASE + path)
            require(url.protocol == "https" && url.host == "amiroudro.github.io") {
                "Edition feed URL is outside the configured HTTPS host"
            }
            val connection = (url.openConnection() as HttpURLConnection).apply {
                requestMethod = "GET"
                connectTimeout = 10_000
                readTimeout = 15_000
                useCaches = false
                setRequestProperty("Accept", "application/json")
                setRequestProperty("User-Agent", "SarangshoPlus-Android")
            }
            try {
                val status = connection.responseCode
                require(status in 200..299) { "Edition feed returned HTTP $status" }
                connection.inputStream.bufferedReader(Charsets.UTF_8).use { it.readText() }
            } finally {
                connection.disconnect()
            }
        }

    private fun parseEdition(
        dataText: String,
        overlayText: String?,
        fetchedAt: String,
        stale: Boolean
    ): Snapshot {
        val data = JSONObject(dataText)
        require(data.optString("status") == "published") { "Only published editions can be displayed" }
        val date = data.optString("edition_date")
        require(Regex("^\\d{4}-\\d{2}-\\d{2}$").matches(date)) { "Invalid edition date" }

        val overlay = overlayText?.let {
            try {
                JSONObject(it).takeIf { json ->
                    json.optString("status") == "published" &&
                        json.optString("language") == "bn" &&
                        json.optString("edition_date") == date
                }
            } catch (_: Exception) {
                null
            }
        }
        val overlayStories = overlay?.optJSONObject("stories")
        val storyArray = data.optJSONArray("stories") ?: error("Published edition has no stories")
        require(storyArray.length() > 0) { "Published edition is empty" }

        val stories = (0 until storyArray.length()).map { index ->
            val item = storyArray.getJSONObject(index)
            val id = item.optString("id")
            require(id.isNotBlank()) { "Story is missing its ID" }
            val bn = overlayStories?.optJSONObject(id)
            val sourcesJson = item.optJSONArray("sources") ?: JSONArray()
            val sources = (0 until sourcesJson.length()).mapNotNull { sourceIndex ->
                val source = sourcesJson.optJSONObject(sourceIndex) ?: return@mapNotNull null
                val url = source.optString("url")
                if (!url.startsWith("https://")) return@mapNotNull null
                Source(
                    publication = source.optString("publication"),
                    headline = source.optString("headline"),
                    date = source.optString("date"),
                    url = url
                )
            }
            val factsJson = bn?.optJSONArray("key_facts") ?: item.optJSONArray("key_facts") ?: JSONArray()
            val facts = (0 until factsJson.length()).mapNotNull { factIndex ->
                val fact = factsJson.optJSONObject(factIndex) ?: return@mapNotNull null
                KeyFact(
                    label = fact.optString("label"),
                    value = fact.optString("value"),
                    original = fact.optString("original")
                )
            }
            Story(
                id = id,
                category = bn?.text("category") ?: item.optString("category", "সংবাদ"),
                headline = bn?.text("headline") ?: item.optString("headline"),
                summary = bn?.text("summary") ?: item.optString("summary"),
                oneLiner = bn?.text("one_liner") ?: item.optString("one_liner"),
                whyItMatters = bn?.text("why_it_matters") ?: item.optString("why_it_matters"),
                examFacts = bn?.text("exam_facts") ?: item.optString("exam_facts"),
                detail = bn?.text("detail") ?: item.optString("detail"),
                sources = sources,
                keyFacts = facts
            )
        }
        return Snapshot(
            date = date,
            day = data.optString("edition_day"),
            coverage = data.optString("coverage"),
            fetchedAt = fetchedAt,
            stale = stale,
            stories = stories
        )
    }

    private fun JSONObject.text(key: String): String? =
        optString(key).takeIf { it.isNotBlank() && it != "null" }
}
