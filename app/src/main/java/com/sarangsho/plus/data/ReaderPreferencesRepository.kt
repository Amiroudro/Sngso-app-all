package com.sarangsho.plus.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.SharedPreferencesMigration
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private const val LEGACY_PREFERENCES = "sarangsho_learning"

private val Context.readerPreferencesDataStore: DataStore<Preferences> by preferencesDataStore(
    name = "reader_preferences",
    produceMigrations = { context ->
        listOf(
            SharedPreferencesMigration(
                context,
                LEGACY_PREFERENCES,
                keysToMigrate = setOf("reading_font_size", "reading_line_spacing", "reading_sepia")
            )
        )
    }
)

data class ReaderSettings(
    val fontSize: Float = 18f,
    val lineSpacing: Float = 1.5f,
    val sepia: Boolean = false
)

/**
 * Durable reader preferences.
 *
 * Existing values are copied from the legacy SharedPreferences file on first access.
 * The legacy file is deliberately retained during migration so unrelated learning data
 * and older app versions are not erased.
 */
object ReaderPreferencesRepository {
    private val fontSizeKey = floatPreferencesKey("reading_font_size")
    private val lineSpacingKey = floatPreferencesKey("reading_line_spacing")
    private val sepiaKey = booleanPreferencesKey("reading_sepia")

    fun observe(context: Context): Flow<ReaderSettings> =
        context.applicationContext.readerPreferencesDataStore.data.map { preferences ->
            ReaderSettings(
                fontSize = (preferences[fontSizeKey] ?: 18f).coerceIn(16f, 26f),
                lineSpacing = (preferences[lineSpacingKey] ?: 1.5f).coerceIn(1.2f, 2f),
                sepia = preferences[sepiaKey] ?: false
            )
        }

    suspend fun setFontSize(context: Context, value: Float) {
        context.applicationContext.readerPreferencesDataStore.edit {
            it[fontSizeKey] = value.coerceIn(16f, 26f)
        }
    }

    suspend fun setLineSpacing(context: Context, value: Float) {
        context.applicationContext.readerPreferencesDataStore.edit {
            it[lineSpacingKey] = value.coerceIn(1.2f, 2f)
        }
    }

    suspend fun setSepia(context: Context, enabled: Boolean) {
        context.applicationContext.readerPreferencesDataStore.edit {
            it[sepiaKey] = enabled
        }
    }
}
