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
            legacy.edit()
                .remove("reading_font_size")
                .remove("reading_line_spacing")
                .remove("reading_sepia")
                .remove("unrelated_learning_value")
                .commit()
        }
    }
}
