package com.sarangsho.plus

import com.sarangsho.plus.data.LegacyReaderPreferencesMigration

import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import java.io.File

@RunWith(AndroidJUnit4::class)
class ReaderPreferencesMigrationTest {
    @Test
    fun legacyReaderPreferencesAreCopiedWithoutDeletingTheLegacyFile() = runBlocking {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val legacyName = "sarangsho_learning"
        val legacy = context.getSharedPreferences(legacyName, 0)
        val hadFontSize = legacy.contains("reading_font_size")
        val previousFontSize = if (hadFontSize) legacy.getFloat("reading_font_size", 18f) else 18f
        val hadLineSpacing = legacy.contains("reading_line_spacing")
        val previousLineSpacing = if (hadLineSpacing) legacy.getFloat("reading_line_spacing", 1.5f) else 1.5f
        val hadSepia = legacy.contains("reading_sepia")
        val previousSepia = if (hadSepia) legacy.getBoolean("reading_sepia", false) else false
        val hadUnrelated = legacy.contains("unrelated_learning_value")
        val previousUnrelated = legacy.getString("unrelated_learning_value", null)
        legacy.edit()
            .putFloat("reading_font_size", 23f)
            .putFloat("reading_line_spacing", 1.8f)
            .putBoolean("reading_sepia", true)
            .putString("unrelated_learning_value", "keep-me")
            .commit()

        val file = File(context.cacheDir, "reader-preferences-migration-test.preferences_pb")
        file.delete()
        val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
        try {
            val store = PreferenceDataStoreFactory.create(
                migrations = listOf(LegacyReaderPreferencesMigration(context)),
                scope = scope,
                produceFile = { file }
            )

            val migrated = store.data.first()
            assertEquals(23f, migrated[floatPreferencesKey("reading_font_size")] ?: 0f, 0f)
            assertEquals(1.8f, migrated[floatPreferencesKey("reading_line_spacing")] ?: 0f, 0f)
            assertTrue(migrated[booleanPreferencesKey("reading_sepia")] == true)
            assertTrue(legacy.contains("reading_font_size"))
            assertEquals("keep-me", legacy.getString("unrelated_learning_value", null))
        } finally {
            scope.cancel()
            file.delete()
            legacy.edit().apply {
                if (hadFontSize) putFloat("reading_font_size", previousFontSize) else remove("reading_font_size")
                if (hadLineSpacing) putFloat("reading_line_spacing", previousLineSpacing) else remove("reading_line_spacing")
                if (hadSepia) putBoolean("reading_sepia", previousSepia) else remove("reading_sepia")
                if (hadUnrelated) putString("unrelated_learning_value", previousUnrelated) else remove("unrelated_learning_value")
            }.commit()
        }
    }
}
