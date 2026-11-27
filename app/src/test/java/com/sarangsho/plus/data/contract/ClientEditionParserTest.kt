package com.sarangsho.plus.data.contract

import com.google.gson.JsonParser
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Assert.assertTrue
import org.junit.Test

class ClientEditionParserTest {
    private fun fixture(): String =
        requireNotNull(javaClass.classLoader?.getResourceAsStream("client-edition-v1.json"))
            .bufferedReader(Charsets.UTF_8)
            .use { it.readText() }

    private fun rehash(root: com.google.gson.JsonObject): String {
        root.addProperty("content_hash", ClientEditionParser.calculateContentHash(root))
        return root.toString()
    }

    @Test
    fun parsesPublishedBilingualFixtureAndVerifiesHash() {
        val edition = ClientEditionParser.parse(fixture())

        assertEquals("edition:2026-11-27-demo", edition.id)
        assertEquals(listOf("bn", "en"), edition.languages)
        assertEquals(1, edition.stories.size)
        assertEquals("story:demo-water-supply", edition.stories.single().id)
        assertEquals("Synthetic Public Bulletin", edition.stories.single().source.name)
        assertEquals(setOf("bn", "en"), edition.stories.single().summaries.map { it.language }.toSet())
    }

    @Test
    fun rejectsUnknownMajorContractVersionEvenWhenHashMatches() {
        val root = JsonParser.parseString(fixture()).asJsonObject
        root.addProperty("contract_version", "2.0.0")

        assertThrows(IllegalArgumentException::class.java) {
            ClientEditionParser.parse(rehash(root))
        }
    }

    @Test
    fun rejectsUnpublishedEditionEvenWhenHashMatches() {
        val root = JsonParser.parseString(fixture()).asJsonObject
        root.getAsJsonObject("edition").addProperty("status", "draft")

        assertThrows(IllegalArgumentException::class.java) {
            ClientEditionParser.parse(rehash(root))
        }
    }

    @Test
    fun rejectsUnapprovedSummaryEvenWhenHashMatches() {
        val root = JsonParser.parseString(fixture()).asJsonObject
        root.getAsJsonArray("stories").get(0).asJsonObject
            .getAsJsonArray("summaries").get(0).asJsonObject
            .addProperty("status", "draft")

        assertThrows(IllegalArgumentException::class.java) {
            ClientEditionParser.parse(rehash(root))
        }
    }

    @Test
    fun rejectsMissingSourceAttributionEvenWhenHashMatches() {
        val root = JsonParser.parseString(fixture()).asJsonObject
        root.getAsJsonArray("stories").get(0).asJsonObject.remove("source")

        assertThrows(IllegalArgumentException::class.java) {
            ClientEditionParser.parse(rehash(root))
        }
    }

    @Test
    fun rejectsContentHashMismatch() {
        val root = JsonParser.parseString(fixture()).asJsonObject
        root.addProperty("content_hash", "sha256:" + "0".repeat(64))

        assertThrows(IllegalArgumentException::class.java) {
            ClientEditionParser.parse(root.toString())
        }
    }

    @Test
    fun rejectsNonHttpSourceUrls() {
        val root = JsonParser.parseString(fixture()).asJsonObject
        root.getAsJsonArray("stories").get(0).asJsonObject
            .getAsJsonObject("source").addProperty("url", "javascript:alert(1)")

        assertThrows(IllegalArgumentException::class.java) {
            ClientEditionParser.parse(rehash(root))
        }
    }

    @Test
    fun rejectsMissingStoryIdEvenWhenHashMatches() {
        val root = JsonParser.parseString(fixture()).asJsonObject
        root.getAsJsonArray("stories").get(0).asJsonObject.remove("id")

        assertThrows(IllegalArgumentException::class.java) {
            ClientEditionParser.parse(rehash(root))
        }
    }

    @Test
    fun rejectsDuplicateEditionLanguagesEvenWhenHashMatches() {
        val root = JsonParser.parseString(fixture()).asJsonObject
        root.getAsJsonObject("edition").getAsJsonArray("languages").add("bn")

        assertThrows(IllegalArgumentException::class.java) {
            ClientEditionParser.parse(rehash(root))
        }
    }

    @Test
    fun rejectsStoryTitleLanguageNotDeclaredByEdition() {
        val root = JsonParser.parseString(fixture()).asJsonObject
        root.getAsJsonArray("stories").get(0).asJsonObject
            .getAsJsonObject("title").addProperty("fr", "Titre")

        assertThrows(IllegalArgumentException::class.java) {
            ClientEditionParser.parse(rehash(root))
        }
    }

    @Test
    fun parsesAndPreservesValidCorrectionMetadata() {
        val root = JsonParser.parseString(fixture()).asJsonObject
        root.getAsJsonArray("stories").get(0).asJsonObject.add(
            "correction",
            JsonParser.parseString("""{"note":"Corrected publication time","corrected_at":"2026-11-27T10:30:00Z"}""")
        )
        val edition = ClientEditionParser.parse(rehash(root))

        assertEquals("Corrected publication time", edition.stories.single().correction?.note)
        assertEquals("2026-11-27T10:30:00Z", edition.stories.single().correction?.correctedAt)
    }


    @Test
    fun parsesAndPreservesAdditionalSourceCitationsInOrder() {
        val root = JsonParser.parseString(fixture()).asJsonObject
        root.getAsJsonArray("stories").get(0).asJsonObject.add(
            "additional_sources",
            JsonParser.parseString(
                """[{"id":"source:second","name":"Second Publisher","url":"https://example.org/second","published_at":"2026-11-27T08:10:00Z"},{"id":"source:third","name":"Third Publisher","url":"https://example.org/third","published_at":"2026-11-27T08:11:00Z"}]"""
            )
        )
        val story = ClientEditionParser.parse(rehash(root)).stories.single()

        assertEquals(
            listOf("source:second", "source:third"),
            story.additionalSources.map { it.id }
        )
        assertEquals("Second Publisher", story.additionalSources.first().name)
    }

    @Test
    fun rejectsAdditionalSourceThatDuplicatesPrimarySource() {
        val root = JsonParser.parseString(fixture()).asJsonObject
        val primary = root.getAsJsonArray("stories").get(0).asJsonObject
            .getAsJsonObject("source").deepCopy()
        root.getAsJsonArray("stories").get(0).asJsonObject.add(
            "additional_sources",
            JsonParser.parseString("[" + primary.toString() + "]")
        )

        assertThrows(IllegalArgumentException::class.java) {
            ClientEditionParser.parse(rehash(root))
        }
    }

    @Test
    fun rejectsDuplicateAdditionalSourceIds() {
        val root = JsonParser.parseString(fixture()).asJsonObject
        val sources = JsonParser.parseString(
            """[{"id":"source:second","name":"Second","url":"https://example.org/second","published_at":"2026-11-27T08:10:00Z"},{"id":"source:second","name":"Duplicate","url":"https://example.org/duplicate","published_at":"2026-11-27T08:11:00Z"}]"""
        )
        root.getAsJsonArray("stories").get(0).asJsonObject.add("additional_sources", sources)

        assertThrows(IllegalArgumentException::class.java) {
            ClientEditionParser.parse(rehash(root))
        }
    }

    @Test
    fun rejectsMalformedCorrectionMetadata() {
        val root = JsonParser.parseString(fixture()).asJsonObject
        root.getAsJsonArray("stories").get(0).asJsonObject.add(
            "correction",
            JsonParser.parseString("""{"note":"","corrected_at":"not-a-date"}""")
        )

        assertThrows(IllegalArgumentException::class.java) {
            ClientEditionParser.parse(rehash(root))
        }
    }

}
