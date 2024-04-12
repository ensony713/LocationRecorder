package com.sample.locationrecorder

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

class PathRepository(
    private val dataStore: DataStore<Preferences>
) {

    private companion object {
        val FILE_PATH = stringPreferencesKey("path")
        const val TAG = "PathRepo"
    }

    val filePath: Flow<String> = dataStore.data
        .catch {
            if (it is IOException) {
                Log.d(TAG, "Error reading preference.", it)
                emit(emptyPreferences())
            }
            else {
                throw it
            }
        }
        .map { preference ->
            preference[FILE_PATH] ?: "/storage/"
        }

    suspend fun saveFilePath(path: String) {
        dataStore.edit { preference ->
            preference[FILE_PATH] = path
        }
    }
}