package com.example.todoapp.data.datastore

import android.content.Context
import androidx.datastore.dataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

/** Manages user preferences using DataStore. */

private val Context.protoDataStore by dataStore("settings.json", PreferencesSerializer)

@Singleton
class PreferencesManager @Inject constructor(@ApplicationContext appContext: Context) {

    private val preferencesDataStore = appContext.protoDataStore
    val userPreferences = preferencesDataStore.data

    suspend fun saveFilterState(isFiltered: Boolean) {
        preferencesDataStore.updateData { userPreferences ->
            userPreferences.copy(isListFilter = isFiltered)
        }
    }
}