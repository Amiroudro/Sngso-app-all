package com.sarangsho.plus

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.sarangsho.plus.data.LocalLearningStore
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LocalLearningStoreTest {
    private val context
        get() = InstrumentationRegistry.getInstrumentation().targetContext

    @Before
    fun clearLearningPreferences() {
        context.getSharedPreferences("sarangsho_learning", 0).edit().clear().commit()
    }

    @After
    fun cleanUpLearningPreferences() {
        context.getSharedPreferences("sarangsho_learning", 0).edit().clear().commit()
    }

    @Test
    fun bookmarkedWordsCanBeAddedReadAndRemoved() {
        LocalLearningStore.setWordBookmarked(context, "Resilience", true)
        LocalLearningStore.setWordBookmarked(context, "Mitigate", true)

        assertTrue(LocalLearningStore.isWordBookmarked(context, "Resilience"))
        assertEquals(setOf("Resilience", "Mitigate"), LocalLearningStore.getBookmarkedWords(context))

        LocalLearningStore.setWordBookmarked(context, "Resilience", false)

        assertFalse(LocalLearningStore.isWordBookmarked(context, "Resilience"))
        assertEquals(setOf("Mitigate"), LocalLearningStore.getBookmarkedWords(context))
    }

    @Test
    fun writingDraftCanBeSavedReadAndCleared() {
        LocalLearningStore.setWritingDraft(context, "বাংলাদেশের জলবায়ু ঝুঁকি")

        assertEquals("বাংলাদেশের জলবায়ু ঝুঁকি", LocalLearningStore.getWritingDraft(context))

        LocalLearningStore.setWritingDraft(context, "")

        assertEquals("", LocalLearningStore.getWritingDraft(context))
    }

    @Test
    fun savedArticlesCanBeAddedReadAndRemoved() {
        LocalLearningStore.setArticleSaved(context, "sample-story", true)

        assertTrue(LocalLearningStore.isArticleSaved(context, "sample-story"))
        assertEquals(setOf("sample-story"), LocalLearningStore.getSavedArticles(context))

        LocalLearningStore.setArticleSaved(context, "sample-story", false)

        assertFalse(LocalLearningStore.isArticleSaved(context, "sample-story"))
        assertEquals(emptySet<String>(), LocalLearningStore.getSavedArticles(context))
    }

    @Test
    fun readerPreferencesPersistAndClampToSupportedRanges() {
        LocalLearningStore.setReadingFontSize(context, 99f)
        LocalLearningStore.setReadingLineSpacing(context, 0.5f)
        LocalLearningStore.setReadingSepia(context, true)
        LocalLearningStore.setAppLanguage(context, "English")

        assertEquals(26f, LocalLearningStore.getReadingFontSize(context))
        assertEquals(1.2f, LocalLearningStore.getReadingLineSpacing(context))
        assertTrue(LocalLearningStore.isReadingSepia(context))
        assertEquals("English", LocalLearningStore.getAppLanguage(context))
    }

    @Test
    fun notificationPreferencesPersistIndependently() {
        LocalLearningStore.setDailyBriefingNotifications(context, false)
        LocalLearningStore.setWordNotifications(context, true)
        LocalLearningStore.setExamNotifications(context, false)

        assertFalse(LocalLearningStore.getDailyBriefingNotifications(context))
        assertTrue(LocalLearningStore.getWordNotifications(context))
        assertFalse(LocalLearningStore.getExamNotifications(context))
    }

    @Test
    fun recentReadingHistoryCanBeRecordedAndCleared() {
        LocalLearningStore.recordArticleRead(context, "story-one")
        LocalLearningStore.recordArticleRead(context, "story-two")
        LocalLearningStore.recordArticleRead(context, "story-one")

        assertEquals(2, LocalLearningStore.getRecentArticles(context).size)
        assertTrue(LocalLearningStore.getRecentArticles(context).containsAll(listOf("story-one", "story-two")))

        LocalLearningStore.clearReadingHistory(context)

        assertTrue(LocalLearningStore.getRecentArticles(context).isEmpty())
    }

    @Test
    fun completedStudyTasksCanBeAddedAndRemoved() {
        LocalLearningStore.setStudyTaskCompleted(context, "Read today's briefing", true)
        LocalLearningStore.setStudyTaskCompleted(context, "Revise vocabulary", true)

        assertEquals(
            setOf("Read today's briefing", "Revise vocabulary"),
            LocalLearningStore.getCompletedStudyTasks(context)
        )

        LocalLearningStore.setStudyTaskCompleted(context, "Read today's briefing", false)

        assertEquals(
            setOf("Revise vocabulary"),
            LocalLearningStore.getCompletedStudyTasks(context)
        )
    }
}
