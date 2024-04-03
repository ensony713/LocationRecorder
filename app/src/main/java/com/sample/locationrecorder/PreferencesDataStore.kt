package com.sample.locationrecorder

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.preferencesDataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "setting")

val FILE_PATH = stringPreferencesKey("file_path")