package com.sarangsho.plus.data

import android.content.Context

/**
 * Small offline-first store for learner preferences and progress.
 *
 * This is device-local storage. It does not sync across devices or accounts.
 */
object LocalLearningStore {
    private const val PREFS_NAME = "sarangsho_learning"
    private const val KEY_BOOKMARKED_WORDS = "bookmarked_words"
    private const val KEY_COMPLETED_STUDY_TASKS = "completed_study_tasks"
    private const val KEY_WRITING_DRAFT = "writing_draft"
    private const val KEY_SAVED_ARTICLES = "saved_articles"
    private const val KEY_READING_FONT_SIZE = "reading_font_size"
    private const val KEY_READING_LINE_SPACING = "reading_line_spacing"
    private const val KEY_READING_SEPIA = "reading_sepia"
    private const val KEY_APP_LANGUAGE = "app_language"
    private const val KEY_NOTIFY_DAILY_BRIEFING = "notify_daily_briefing"
    private const val KEY_NOTIFY_WORD = "notify_word"
    private const val KEY_NOTIFY_EXAM = "notify_exam"

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

    fun getCompletedStudyTasks(context: Context): Set<String> =
        preferences(context).getStringSet(KEY_COMPLETED_STUDY_TASKS, emptySet()).orEmpty().toSet()

    fun setStudyTaskCompleted(context: Context, task: String, completed: Boolean) {
        val prefs = preferences(context)
        val tasks = prefs.getStringSet(KEY_COMPLETED_STUDY_TASKS, emptySet()).orEmpty().toMutableSet()
        if (completed) tasks.add(task) else tasks.remove(task)
        prefs.edit().putStringSet(KEY_COMPLETED_STUDY_TASKS, tasks).apply()
    }

    fun getWritingDraft(context: Context): String =
        preferences(context).getString(KEY_WRITING_DRAFT, "").orEmpty()

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
