package com.sarangsho.plus

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.sarangsho.plus.data.LocalLearningStore
import com.sarangsho.plus.features.learning.searchVocabulary
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
    fun selectedTopicPersistsAndCanBeChanged() {
        LocalLearningStore.setSelectedTopic(context, "অর্থনীতি ও ব্যাংকিং")
        assertEquals("অর্থনীতি ও ব্যাংকিং", LocalLearningStore.getSelectedTopic(context))

        LocalLearningStore.setSelectedTopic(context, "আন্তর্জাতিক আইন ও চুক্তি")
        assertEquals("আন্তর্জাতিক আইন ও চুক্তি", LocalLearningStore.getSelectedTopic(context))
    }

    @Test
    fun localVocabularySearchMatchesTermsAndBanglaMeanings() {
        assertTrue(searchVocabulary("resilience").any { it.first == "Resilience" })
        assertTrue(searchVocabulary("জবাবদিহিতা").any { it.first == "Accountability" })
        assertTrue(searchVocabulary("no matching term").isEmpty())
        assertTrue(searchVocabulary(" ").isEmpty())
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
    fun feedbackDraftPersistsAndCanBeReplaced() {
        LocalLearningStore.setFeedbackDraft(context, "[সমস্যা] নিবন্ধ খুলছে না")
        assertEquals("[সমস্যা] নিবন্ধ খুলছে না", LocalLearningStore.getFeedbackDraft(context))

        LocalLearningStore.setFeedbackDraft(context, "[ভুল তথ্য] তারিখটি ভুল")
        assertEquals("[ভুল তথ্য] তারিখটি ভুল", LocalLearningStore.getFeedbackDraft(context))
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
    fun onboardingGoalAndInterestsPersistLocally() {
        LocalLearningStore.setOnboardingGoal(context, "Bank recruitment")
        LocalLearningStore.setOnboardingInterests(
            context,
            setOf("বাংলাদেশ", "অর্থনীতি ও ব্যাংকিং", "  ")
        )

        assertEquals("Bank recruitment", LocalLearningStore.getOnboardingGoal(context))
        assertEquals(
            setOf("বাংলাদেশ", "অর্থনীতি ও ব্যাংকিং"),
            LocalLearningStore.getOnboardingInterests(context)
        )
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
    fun legacyReadingHistoryStringSetMigratesToOrderedJson() {
        val prefs = context.getSharedPreferences("sarangsho_learning", 0)
        prefs.edit()
            .putStringSet("recent_articles", setOf("story-z", "story-a", "story-m"))
            .commit()

        assertEquals(
            listOf("story-a", "story-m", "story-z"),
            LocalLearningStore.getRecentArticles(context)
        )
        assertEquals(
            """["story-a","story-m","story-z"]""",
            prefs.getString("recent_articles", null)
        )
        assertTrue(prefs.all["recent_articles"] is String)
    }

    @Test
    fun readingHistoryPreservesNewestFirstOrderAndMovesReopenedArticleToTop() {
        LocalLearningStore.recordArticleRead(context, "story-one")
        LocalLearningStore.recordArticleRead(context, "story-two")
        LocalLearningStore.recordArticleRead(context, "story-one")

        assertEquals(listOf("story-one", "story-two"), LocalLearningStore.getRecentArticles(context))
    }

    @Test
    fun practiceAttemptsPersistNewestFirstAndCanBeCleared() {
        LocalLearningStore.recordPracticeAttempt(context, 1, 2)
        LocalLearningStore.recordPracticeAttempt(context, 2, 2)

        val attempts = LocalLearningStore.getPracticeHistory(context)
        assertEquals(2, attempts.size)
        assertEquals(2, attempts[0].correct)
        assertEquals(2, attempts[0].total)
        assertEquals(1, attempts[1].correct)

        LocalLearningStore.clearPracticeHistory(context)
        assertTrue(LocalLearningStore.getPracticeHistory(context).isEmpty())
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
    @Test
    fun completedStudyTasksResetWhenTheLocalCalendarDateChanges() {
        val prefs = context.getSharedPreferences("sarangsho_learning", 0)
        prefs.edit()
            .putString("completed_study_tasks_date", "2000-01-01")
            .putStringSet("completed_study_tasks", setOf("আজকের ব্রিফিং পড়া"))
            .commit()

        assertTrue(LocalLearningStore.getCompletedStudyTasks(context).isEmpty())
        assertEquals(
            java.time.LocalDate.now().toString(),
            prefs.getString("completed_study_tasks_date", null)
        )

        LocalLearningStore.setStudyTaskCompleted(context, "আজকের ব্রিফিং পড়া", true)
        assertEquals(
            setOf("আজকের ব্রিফিং পড়া"),
            LocalLearningStore.getCompletedStudyTasks(context)
        )
    }

}
