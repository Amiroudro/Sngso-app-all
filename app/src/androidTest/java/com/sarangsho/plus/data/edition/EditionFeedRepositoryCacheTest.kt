package com.sarangsho.plus.data.edition

import android.content.Context
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test
import java.time.Instant

class EditionFeedRepositoryCacheTest {
    private fun edition(date: String, headline: String): String = """
        {
          "edition_date":"$date",
          "edition_day":"Friday",
          "coverage":"$date coverage",
          "status":"published",
          "stories":[{
            "id":"CA01",
            "category":"Bangladesh",
            "headline":"$headline",
            "summary":"Summary for $date",
            "one_liner":"One line",
            "why_it_matters":"Context",
            "exam_facts":"Exam facts",
            "detail":"Detailed story",
            "sources":[{"publication":"Test Publisher","headline":"Original","date":"$date","url":"https://example.org/$date"}],
            "key_facts":[]
          }]
        }
    """.trimIndent()

    @Test
    fun archiveCacheDoesNotReplaceLatestEditionAndStorySelectionIsDateScoped() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val prefs = context.getSharedPreferences("sarangsho_edition_feed", Context.MODE_PRIVATE)
        prefs.edit().clear()
            .putString("edition_data", edition("2026-11-27", "Latest edition headline"))
            .putString("fetched_at", Instant.now().toString())
            .putString("archive_data_2026-11-26", edition("2026-11-26", "Archive edition headline"))
            .putString("archive_fetched_at_2026-11-26", Instant.now().toString())
            .commit()

        val latest = EditionFeedRepository.cached(context)
        val archive = EditionFeedRepository.cachedEdition(context, "2026-11-26")

        assertNotNull(latest)
        assertNotNull(archive)
        assertEquals("2026-11-27", latest?.date)
        assertEquals("Latest edition headline", latest?.stories?.single()?.headline)
        assertEquals("2026-11-26", archive?.date)
        assertEquals("Latest edition headline", EditionFeedRepository.cached(context)?.stories?.single()?.headline)

        EditionFeedRepository.selectStory(context, "CA01", "2026-11-26")
        assertEquals("Archive edition headline", EditionFeedRepository.story(context, "CA01")?.headline)

        EditionFeedRepository.selectStory(context, "CA01")
        assertEquals("Latest edition headline", EditionFeedRepository.story(context, "CA01")?.headline)
    }
}
