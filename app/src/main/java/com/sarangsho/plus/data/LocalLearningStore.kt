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
}
