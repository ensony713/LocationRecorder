package com.sample.locationrecorder

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore

const val PATH_PREFERENCE_NAME = "path_preferences"
val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
    name = PATH_PREFERENCE_NAME
)