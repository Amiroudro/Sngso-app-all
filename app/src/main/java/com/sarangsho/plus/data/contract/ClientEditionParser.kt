package com.sarangsho.plus.data.contract

import com.google.gson.GsonBuilder
import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import java.net.URI
import java.nio.charset.StandardCharsets
import java.security.MessageDigest
import java.time.OffsetDateTime

/**
 * Read-only parser for the proposed client-edition projection v1.
 *
 * This parser is deliberately not connected to networking or UI. The projection
 * remains a proposal until the editorial engine adopts an agreed delivery contract.
 */
object ClientEditionParser {
    private val gson = GsonBuilder().disableHtmlEscaping().serializeNulls().create()
    private val languagePattern = Regex("^[a-z]{2,3}(-[A-Za-z0-9]{2,8})*$")
    private val hashPattern = Regex("^sha256:[a-f0-9]{64}$")

    fun parse(json: String): ClientEdition {
        val root = try {
            JsonParser.parseString(json).asJsonObject
        } catch (error: Exception) {
            throw IllegalArgumentException("Edition payload must be a JSON object", error)
        }
        requireKeys(root, setOf("contract_version", "content_hash", "edition", "stories"), path = "root")
        require(root.string("contract_version") == "1.0.0") {
            "Unsupported client-edition contract version"
        }

        val declaredHash = root.string("content_hash")
        require(hashPattern.matches(declaredHash)) { "Invalid content_hash format" }
        val actualHash = calculateContentHash(root)
        require(declaredHash == actualHash) { "Client-edition content_hash mismatch" }

        val editionJson = root.objectValue("edition")
        requireKeys(
            editionJson,
            setOf("id", "edition_key", "status", "published_at", "languages"),
            setOf("supersedes_edition_id", "correction_note"),
            "edition"
        )
        val editionId = editionJson.string("id").also { requireId(it, "edition.id") }
        val editionKey = editionJson.string("edition_key")
        require(Regex("^[0-9]{4}-[0-9]{2}-[0-9]{2}([_-][a-z0-9-]+)?$").matches(editionKey)) {
            "Invalid edition.edition_key"
        }
        require(editionJson.string("status") == "published") { "Only published editions are accepted" }
        val editionPublishedAt = editionJson.string("published_at").also { requireTimestamp(it, "edition.published_at") }
        val supersedesEditionId = editionJson.get("supersedes_edition_id")?.let { element ->
            require(element.isJsonPrimitive && element.asJsonPrimitive.isString) {
                "edition.supersedes_edition_id must be a string"
            }
            element.asString.also { requireId(it, "edition.supersedes_edition_id") }
        }
        val correctionNote = editionJson.get("correction_note")?.let { element ->
            require(element.isJsonPrimitive && element.asJsonPrimitive.isString) {
                "edition.correction_note must be a string"
            }
            element.asString.also { require(it.length <= 4000) { "edition.correction_note is too long" } }
        }
        val languages = editionJson.arrayValue("languages").mapIndexed { index, value ->
            require(value.isJsonPrimitive && value.asJsonPrimitive.isString) {
                "edition.languages[$index] must be a string"
            }
            value.asString.also { requireLanguage(it, "edition.languages[$index]") }
        }
        require(languages.isNotEmpty() && languages.distinct().size == languages.size) {
            "edition.languages must be non-empty and unique"
        }

        val stories = root.arrayValue("stories").mapIndexed { index, value ->
            parseStory(value.asJsonObject, index, languages.toSet())
        }
        require(stories.map { it.id }.distinct().size == stories.size) {
            "stories must have unique IDs"
        }
        return ClientEdition(
            id = editionId,
            editionKey = editionKey,
            publishedAt = editionPublishedAt,
            languages = languages,
            supersedesEditionId = supersedesEditionId,
            correctionNote = correctionNote,
            contentHash = declaredHash,
            stories = stories
        )
    }

    fun calculateContentHash(root: JsonObject): String {
        val payload = root.deepCopy().apply { remove("content_hash") }
        val canonical = gson.toJson(sortRecursively(payload))
        val digest = MessageDigest.getInstance("SHA-256")
            .digest(canonical.toByteArray(StandardCharsets.UTF_8))
            .joinToString("") { "%02x".format(it) }
        return "sha256:$digest"
    }

    private fun parseStory(value: JsonObject, index: Int, editionLanguages: Set<String>): ClientStory {
        val path = "stories[$index]"
        requireKeys(
            value,
            setOf("id", "title", "published_at", "source", "summaries"),
            setOf("topic_tags", "correction", "additional_sources"),
            path
        )
        val id = value.string("id").also { requireId(it, "$path.id") }
        val titleObject = value.objectValue("title")
        require(titleObject.size() > 0) { "$path.title must contain at least one language" }
        val titles = titleObject.entrySet().associate { (language, text) ->
            requireLanguage(language, "$path.title key")
            require(editionLanguages.contains(language)) { "$path.title.$language is not declared by the edition" }
            require(text.isJsonPrimitive && text.asJsonPrimitive.isString && text.asString.isNotBlank() && text.asString.length <= 1000) {
                "$path.title.$language must be a non-empty string of at most 1000 characters"
            }
            language to text.asString
        }
        val tags = value.get("topic_tags")?.let { element ->
            require(element.isJsonArray) { "$path.topic_tags must be an array" }
            element.asJsonArray.mapIndexed { tagIndex, tag ->
                require(tag.isJsonPrimitive && tag.asJsonPrimitive.isString) { "$path.topic_tags[$tagIndex] must be a string" }
                tag.asString.also { require(it.isNotEmpty() && it.length <= 80) { "$path.topic_tags[$tagIndex] is invalid" } }
            }
        } ?: emptyList()
        require(tags.distinct().size == tags.size) { "$path.topic_tags must be unique" }
        val publishedAt = value.string("published_at").also { requireTimestamp(it, "$path.published_at") }

        val sourceJson = value.objectValue("source")
        requireKeys(sourceJson, setOf("id", "name", "url", "published_at"), emptySet(), "$path.source")
        val sourceId = sourceJson.string("id").also { requireId(it, "$path.source.id") }
        val sourceName = sourceJson.string("name").also {
            require(it.isNotBlank() && it.length <= 300) { "$path.source.name is invalid" }
        }
        val sourceUrl = sourceJson.string("url").also { raw ->
            val uri = try { URI(raw) } catch (error: Exception) {
                throw IllegalArgumentException("$path.source.url is invalid", error)
            }
            require(uri.isAbsolute && uri.scheme in setOf("http", "https") && !uri.host.isNullOrBlank()) {
                "$path.source.url must be an absolute HTTP(S) URL"
            }
        }
        val sourcePublishedAt = sourceJson.string("published_at")
            .also { requireTimestamp(it, "$path.source.published_at") }

        val additionalSources = value.get("additional_sources")?.let { element ->
            require(element.isJsonArray) { "$path.additional_sources must be an array" }
            element.asJsonArray.mapIndexed { sourceIndex, sourceElement ->
                require(sourceElement.isJsonObject) {
                    "$path.additional_sources[$sourceIndex] must be an object"
                }
                val additionalPath = "$path.additional_sources[$sourceIndex]"
                val additional = sourceElement.asJsonObject
                requireKeys(additional, setOf("id", "name", "url", "published_at"), path = additionalPath)
                val additionalId = additional.string("id").also { requireId(it, "$additionalPath.id") }
                require(additionalId != sourceId) { "$additionalPath duplicates the primary source" }
                val additionalName = additional.string("name").also {
                    require(it.isNotBlank() && it.length <= 300) { "$additionalPath.name is invalid" }
                }
                val additionalUrl = additional.string("url").also { raw ->
                    val uri = try { URI(raw) } catch (error: Exception) {
                        throw IllegalArgumentException("$additionalPath.url is invalid", error)
                    }
                    require(uri.isAbsolute && uri.scheme in setOf("http", "https") && !uri.host.isNullOrBlank()) {
                        "$additionalPath.url must be an absolute HTTP(S) URL"
                    }
                }
                val additionalPublishedAt = additional.string("published_at")
                    .also { requireTimestamp(it, "$additionalPath.published_at") }
                ClientSource(additionalId, additionalName, additionalUrl, additionalPublishedAt)
            }
        } ?: emptyList()
        require(additionalSources.map { it.id }.distinct().size == additionalSources.size) {
            "$path.additional_sources must have unique source IDs"
        }

        val summaries = value.arrayValue("summaries").mapIndexed { summaryIndex, summaryElement ->
            val summaryPath = "$path.summaries[$summaryIndex]"
            val summary = summaryElement.asJsonObject
            requireKeys(summary, setOf("language", "status", "brief", "detailed"), emptySet(), summaryPath)
            val language = summary.string("language").also { requireLanguage(it, "$summaryPath.language") }
            require(summary.string("status") == "approved") { "$summaryPath is not approved" }
            val brief = summary.string("brief").also {
                require(it.isNotBlank() && it.length <= 2000) { "$summaryPath.brief is invalid" }
            }
            val detailed = summary.string("detailed").also {
                require(it.isNotBlank() && it.length <= 12000) { "$summaryPath.detailed is invalid" }
            }
            ClientSummary(language, brief, detailed)
        }
        require(summaries.isNotEmpty()) { "$path.summaries must not be empty" }
        require(summaries.map { it.language }.distinct().size == summaries.size) {
            "$path.summaries languages must be unique"
        }
        require(summaries.all { it.language in editionLanguages }) {
            "$path.summaries contains a language not declared by the edition"
        }
        val correction = value.get("correction")?.let { element ->
            if (element.isJsonNull) null else {
                require(element.isJsonObject) { "$path.correction must be an object or null" }
                val correctionJson = element.asJsonObject
                requireKeys(correctionJson, setOf("note", "corrected_at"), path = "$path.correction")
                val note = correctionJson.string("note").also {
                    require(it.isNotBlank() && it.length <= 4000) { "$path.correction.note is invalid" }
                }
                val correctedAt = correctionJson.string("corrected_at")
                    .also { requireTimestamp(it, "$path.correction.corrected_at") }
                ClientCorrection(note, correctedAt)
            }
        }

        return ClientStory(
            id = id,
            titles = titles,
            topicTags = tags,
            publishedAt = publishedAt,
            source = ClientSource(sourceId, sourceName, sourceUrl, sourcePublishedAt),
            additionalSources = additionalSources,
            summaries = summaries,
            correction = correction
        )
    }

    private fun sortRecursively(element: JsonElement): JsonElement = when {
        element.isJsonObject -> JsonObject().apply {
            element.asJsonObject.entrySet().sortedBy { it.key }.forEach { (key, value) ->
                add(key, sortRecursively(value))
            }
        }
        element.isJsonArray -> JsonArray().apply {
            element.asJsonArray.forEach { add(sortRecursively(it)) }
        }
        else -> element.deepCopy()
    }

    private fun requireKeys(
        value: JsonObject,
        required: Set<String>,
        optional: Set<String> = emptySet(),
        path: String
    ) {
        val keys = value.keySet()
        require(keys.containsAll(required)) { "$path is missing required fields: ${required - keys}" }
        require((keys - required - optional).isEmpty()) { "$path contains unknown fields: ${keys - required - optional}" }
    }

    private fun JsonObject.string(key: String): String {
        val value = get(key)
        require(value != null && value.isJsonPrimitive && value.asJsonPrimitive.isString) {
            "Expected '$key' to be a string"
        }
        return value.asString
    }

    private fun JsonObject.objectValue(key: String): JsonObject {
        val value = get(key)
        require(value != null && value.isJsonObject) { "Expected '$key' to be an object" }
        return value.asJsonObject
    }

    private fun JsonObject.arrayValue(key: String): JsonArray {
        val value = get(key)
        require(value != null && value.isJsonArray) { "Expected '$key' to be an array" }
        return value.asJsonArray
    }

    private fun requireId(value: String, path: String) {
        require(value.length in 3..160 && Regex("^[a-z][a-z0-9_-]*:[A-Za-z0-9._~-]+$").matches(value)) {
            "$path has an invalid ID"
        }
    }

    private fun requireLanguage(value: String, path: String) {
        require(languagePattern.matches(value)) { "$path is not a valid language tag" }
    }

    private fun requireTimestamp(value: String, path: String) {
        try {
            OffsetDateTime.parse(value)
        } catch (error: Exception) {
            throw IllegalArgumentException("$path must be an RFC 3339 timestamp with timezone", error)
        }
    }
}

data class ClientEdition(
    val id: String,
    val editionKey: String,
    val publishedAt: String,
    val languages: List<String>,
    val supersedesEditionId: String?,
    val correctionNote: String?,
    val contentHash: String,
    val stories: List<ClientStory>
)

data class ClientStory(
    val id: String,
    val titles: Map<String, String>,
    val topicTags: List<String>,
    val publishedAt: String,
    val source: ClientSource,
    val additionalSources: List<ClientSource>,
    val summaries: List<ClientSummary>,
    val correction: ClientCorrection?
)

data class ClientSource(
    val id: String,
    val name: String,
    val url: String,
    val publishedAt: String
)

data class ClientSummary(
    val language: String,
    val brief: String,
    val detailed: String
)

data class ClientCorrection(
    val note: String,
    val correctedAt: String
)
