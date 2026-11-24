package com.sarangsho.plus.data

import android.content.Context
import org.json.JSONArray
import org.json.JSONObject
import java.time.LocalDate

/**
 * Small offline-first store for learner preferences and progress.
 *
 * This is device-local storage. It does not sync across devices or accounts.
 */
object LocalLearningStore {
    private const val PREFS_NAME = "sarangsho_learning"
    private const val KEY_BOOKMARKED_WORDS = "bookmarked_words"
    private const val KEY_COMPLETED_STUDY_TASKS = "completed_study_tasks"
    private const val KEY_COMPLETED_STUDY_TASKS_DATE = "completed_study_tasks_date"
    private const val KEY_WRITING_DRAFT = "writing_draft"
    private const val KEY_SAVED_ARTICLES = "saved_articles"
    private const val KEY_READING_FONT_SIZE = "reading_font_size"
    private const val KEY_READING_LINE_SPACING = "reading_line_spacing"
    private const val KEY_READING_SEPIA = "reading_sepia"
    private const val KEY_APP_LANGUAGE = "app_language"
    private const val KEY_NOTIFY_DAILY_BRIEFING = "notify_daily_briefing"
    private const val KEY_NOTIFY_WORD = "notify_word"
    private const val KEY_NOTIFY_EXAM = "notify_exam"
    private const val KEY_RECENT_ARTICLES = "recent_articles"
    private const val KEY_PRACTICE_HISTORY = "practice_history"
    private const val KEY_FEEDBACK_DRAFT = "feedback_draft"
    private const val KEY_ONBOARDING_GOAL = "onboarding_goal"
    private const val KEY_ONBOARDING_INTERESTS = "onboarding_interests"
    private const val KEY_SELECTED_TOPIC = "selected_topic"

    private fun preferences(context: Context) =
        context.applicationContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun getBookmarkedWords(context: Context): Set<String> =
        preferences(context).getStringSet(KEY_BOOKMARKED_WORDS, emptySet()).orEmpty().toSet()

    fun isWordBookmarked(context: Context, word: String): Boolean =
        word in getBookmarkedWords(context)

    fun setWordBookmarked(context: Context, word: String, bookmarked: Boolean) {
        val prefs = preferences(context)
        val words = prefs.getStringSet(KEY_BOOKMARKED_WORDS, emptySet()).orEmpty().toMutableSet()
        if (bookmarked) words.add(word) else words.remove(word)
        prefs.edit().putStringSet(KEY_BOOKMARKED_WORDS, words).apply()
    }

    /** Study-plan checkmarks belong to the device's current local calendar day. */
    fun getCompletedStudyTasks(context: Context): Set<String> {
        val prefs = preferences(context)
        val today = LocalDate.now().toString()
        if (prefs.getString(KEY_COMPLETED_STUDY_TASKS_DATE, null) != today) {
            prefs.edit()
                .putString(KEY_COMPLETED_STUDY_TASKS_DATE, today)
                .putStringSet(KEY_COMPLETED_STUDY_TASKS, emptySet())
                .apply()
            return emptySet()
        }
        return prefs.getStringSet(KEY_COMPLETED_STUDY_TASKS, emptySet()).orEmpty().toSet()
    }

    fun setStudyTaskCompleted(context: Context, task: String, completed: Boolean) {
        val prefs = preferences(context)
        val today = LocalDate.now().toString()
        val tasks = if (prefs.getString(KEY_COMPLETED_STUDY_TASKS_DATE, null) == today) {
            prefs.getStringSet(KEY_COMPLETED_STUDY_TASKS, emptySet()).orEmpty().toMutableSet()
        } else {
            mutableSetOf()
        }
        if (completed) tasks.add(task) else tasks.remove(task)
        prefs.edit()
            .putString(KEY_COMPLETED_STUDY_TASKS_DATE, today)
            .putStringSet(KEY_COMPLETED_STUDY_TASKS, tasks)
            .apply()
    }

    fun getOnboardingGoal(context: Context): String =
        preferences(context).getString(KEY_ONBOARDING_GOAL, "BCS প্রস্তুতি").orEmpty()

    fun setOnboardingGoal(context: Context, goal: String) {
        require(goal.isNotBlank())
        preferences(context).edit().putString(KEY_ONBOARDING_GOAL, goal.trim()).apply()
    }

    fun getOnboardingInterests(context: Context): Set<String> =
        preferences(context).getStringSet(
            KEY_ONBOARDING_INTERESTS,
            setOf("বাংলাদেশ", "অর্থনীতি ও ব্যাংকিং")
        ).orEmpty().toSet()

    fun setOnboardingInterests(context: Context, interests: Set<String>) {
        val cleaned = interests.map(String::trim).filter(String::isNotBlank).toSet()
        preferences(context).edit().putStringSet(KEY_ONBOARDING_INTERESTS, cleaned).apply()
    }

    fun getSelectedTopic(context: Context): String =
        preferences(context).getString(KEY_SELECTED_TOPIC, "").orEmpty()

    fun setSelectedTopic(context: Context, topic: String) {
        require(topic.isNotBlank())
        preferences(context).edit().putString(KEY_SELECTED_TOPIC, topic.trim()).apply()
    }

    fun getWritingDraft(context: Context): String =
        preferences(context).getString(KEY_WRITING_DRAFT, "").orEmpty()

    fun getFeedbackDraft(context: Context): String =
        preferences(context).getString(KEY_FEEDBACK_DRAFT, "").orEmpty()

    fun setFeedbackDraft(context: Context, draft: String) {
        preferences(context).edit().putString(KEY_FEEDBACK_DRAFT, draft).apply()
    }

    fun setWritingDraft(context: Context, draft: String) {
        preferences(context).edit().putString(KEY_WRITING_DRAFT, draft).apply()
    }

    fun getSavedArticles(context: Context): Set<String> =
        preferences(context).getStringSet(KEY_SAVED_ARTICLES, emptySet()).orEmpty().toSet()

    fun isArticleSaved(context: Context, articleId: String): Boolean =
        articleId in getSavedArticles(context)

    fun setArticleSaved(context: Context, articleId: String, saved: Boolean) {
        val articles = preferences(context).getStringSet(KEY_SAVED_ARTICLES, emptySet()).orEmpty().toMutableSet()
        if (saved) articles.add(articleId) else articles.remove(articleId)
        preferences(context).edit().putStringSet(KEY_SAVED_ARTICLES, articles).apply()
    }

    /** Most recently opened article first. JSON preserves order, unlike SharedPreferences StringSet. */
    fun getRecentArticles(context: Context): List<String> {
        val prefs = preferences(context)
        return when (val stored = prefs.all[KEY_RECENT_ARTICLES]) {
            is String -> try {
                val json = JSONArray(stored)
                (0 until json.length()).mapNotNull { json.optString(it).takeIf(String::isNotBlank) }.distinct()
            } catch (_: Exception) { emptyList() }
            // Older builds stored this as a StringSet. Its original order was not retained,
            // but migrate its contents rather than discarding a user's saved history.
            is Set<*> -> stored.filterIsInstance<String>().filter(String::isNotBlank).sorted()
            else -> emptyList()
        }
    }

    fun recordArticleRead(context: Context, articleId: String) {
        if (articleId.isBlank()) return
        val articles = (listOf(articleId) + getRecentArticles(context).filterNot { it == articleId }).take(50)
        val json = JSONArray()
        articles.forEach(json::put)
        preferences(context).edit().putString(KEY_RECENT_ARTICLES, json.toString()).apply()
    }

    fun clearReadingHistory(context: Context) {
        preferences(context).edit().remove(KEY_RECENT_ARTICLES).apply()
    }

    data class PracticeAttempt(val correct: Int, val total: Int, val timestamp: Long)

    fun getPracticeHistory(context: Context): List<PracticeAttempt> {
        val raw = preferences(context).getString(KEY_PRACTICE_HISTORY, null) ?: return emptyList()
        return try {
            val json = JSONArray(raw)
            (0 until json.length()).mapNotNull { index ->
                val item = json.optJSONObject(index) ?: return@mapNotNull null
                val total = item.optInt("total")
                val correct = item.optInt("correct")
                val timestamp = item.optLong("timestamp")
                if (total > 0 && correct in 0..total && timestamp > 0) PracticeAttempt(correct, total, timestamp) else null
            }
        } catch (_: Exception) { emptyList() }
    }

    fun recordPracticeAttempt(context: Context, correct: Int, total: Int) {
        require(total > 0 && correct in 0..total)
        val attempts = (listOf(PracticeAttempt(correct, total, System.currentTimeMillis())) + getPracticeHistory(context)).take(100)
        val json = JSONArray()
        attempts.forEach { attempt ->
            json.put(JSONObject().put("correct", attempt.correct).put("total", attempt.total).put("timestamp", attempt.timestamp))
        }
        preferences(context).edit().putString(KEY_PRACTICE_HISTORY, json.toString()).apply()
    }

    fun clearPracticeHistory(context: Context) {
        preferences(context).edit().remove(KEY_PRACTICE_HISTORY).apply()
    }

    fun getReadingFontSize(context: Context): Float =
        preferences(context).getFloat(KEY_READING_FONT_SIZE, 18f)

    fun setReadingFontSize(context: Context, value: Float) {
        preferences(context).edit().putFloat(KEY_READING_FONT_SIZE, value.coerceIn(16f, 26f)).apply()
    }

    fun getReadingLineSpacing(context: Context): Float =
        preferences(context).getFloat(KEY_READING_LINE_SPACING, 1.5f)

    fun setReadingLineSpacing(context: Context, value: Float) {
        preferences(context).edit().putFloat(KEY_READING_LINE_SPACING, value.coerceIn(1.2f, 2f)).apply()
    }

    fun isReadingSepia(context: Context): Boolean =
        preferences(context).getBoolean(KEY_READING_SEPIA, false)

    fun setReadingSepia(context: Context, enabled: Boolean) {
        preferences(context).edit().putBoolean(KEY_READING_SEPIA, enabled).apply()
    }

    fun getAppLanguage(context: Context): String =
        preferences(context).getString(KEY_APP_LANGUAGE, "বাংলা").orEmpty()

    fun setAppLanguage(context: Context, language: String) {
        preferences(context).edit().putString(KEY_APP_LANGUAGE, language).apply()
    }

    fun getNotificationPreference(context: Context, key: String): Boolean =
        preferences(context).getBoolean(key, true)

    fun setNotificationPreference(context: Context, key: String, enabled: Boolean) {
        require(key in setOf(KEY_NOTIFY_DAILY_BRIEFING, KEY_NOTIFY_WORD, KEY_NOTIFY_EXAM))
        preferences(context).edit().putBoolean(key, enabled).apply()
    }

    fun getDailyBriefingNotifications(context: Context) = getNotificationPreference(context, KEY_NOTIFY_DAILY_BRIEFING)
    fun setDailyBriefingNotifications(context: Context, enabled: Boolean) = setNotificationPreference(context, KEY_NOTIFY_DAILY_BRIEFING, enabled)
    fun getWordNotifications(context: Context) = getNotificationPreference(context, KEY_NOTIFY_WORD)
    fun setWordNotifications(context: Context, enabled: Boolean) = setNotificationPreference(context, KEY_NOTIFY_WORD, enabled)
    fun getExamNotifications(context: Context) = getNotificationPreference(context, KEY_NOTIFY_EXAM)
    fun setExamNotifications(context: Context, enabled: Boolean) = setNotificationPreference(context, KEY_NOTIFY_EXAM, enabled)
}
