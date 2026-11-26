package com.sarangsho.plus.data

import android.content.Context
import androidx.datastore.core.DataMigration
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.toMutablePreferences
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private const val LEGACY_PREFERENCES = "sarangsho_learning"

private val legacyFontSizeKey = floatPreferencesKey("reading_font_size")
private val legacyLineSpacingKey = floatPreferencesKey("reading_line_spacing")
private val legacySepiaKey = booleanPreferencesKey("reading_sepia")

/**
 * Copies only reader settings and intentionally leaves the legacy SharedPreferences
 * untouched. Other learning data still belongs to LocalLearningStore during this
 * staged migration and must not be deleted as a side effect.
 */
internal class LegacyReaderPreferencesMigration(context: Context) : DataMigration<Preferences> {
    private val legacy = context.applicationContext.getSharedPreferences(LEGACY_PREFERENCES, Context.MODE_PRIVATE)

    override suspend fun shouldMigrate(currentData: Preferences): Boolean =
        (legacy.contains("reading_font_size") && currentData[legacyFontSizeKey] == null) ||
            (legacy.contains("reading_line_spacing") && currentData[legacyLineSpacingKey] == null) ||
            (legacy.contains("reading_sepia") && currentData[legacySepiaKey] == null)

    override suspend fun migrate(currentData: Preferences): Preferences {
        val migrated = currentData.toMutablePreferences()
        if (migrated[legacyFontSizeKey] == null && legacy.contains("reading_font_size")) {
            migrated[legacyFontSizeKey] = legacy.getFloat("reading_font_size", 18f)
        }
        if (migrated[legacyLineSpacingKey] == null && legacy.contains("reading_line_spacing")) {
            migrated[legacyLineSpacingKey] = legacy.getFloat("reading_line_spacing", 1.5f)
        }
        if (migrated[legacySepiaKey] == null && legacy.contains("reading_sepia")) {
            migrated[legacySepiaKey] = legacy.getBoolean("reading_sepia", false)
        }
        return migrated
    }

    override suspend fun cleanUp() {
        // Deliberately retain the source preferences until every legacy data domain migrates.
    }
}

private val Context.readerPreferencesDataStore: DataStore<Preferences> by preferencesDataStore(
    name = "reader_preferences",
    produceMigrations = { context -> listOf(LegacyReaderPreferencesMigration(context)) }
)

data class ReaderSettings(
    val fontSize: Float = 18f,
    val lineSpacing: Float = 1.5f,
    val sepia: Boolean = false
)

/** Durable reader preferences with a non-destructive one-time legacy migration. */
object ReaderPreferencesRepository {
    private val fontSizeKey = legacyFontSizeKey
    private val lineSpacingKey = legacyLineSpacingKey
    private val sepiaKey = legacySepiaKey

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
