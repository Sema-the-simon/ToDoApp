package com.example.todoapp.data.datastore

import android.content.Context
import androidx.datastore.dataStore
import com.example.todoapp.di.IoDispatcher
import com.example.todoapp.domain.model.ThemeMode
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

/** Manages user preferences using DataStore. */

private val Context.protoDataStore by dataStore("settings.json", PreferencesSerializer)

@Singleton
class PreferencesManager @Inject constructor(
    @ApplicationContext appContext: Context,
    @IoDispatcher
    private val ioDispatcher: CoroutineDispatcher
) {

    private val preferencesDataStore = appContext.protoDataStore
    val userPreferences = preferencesDataStore.data

    suspend fun saveFilterState(isFiltered: Boolean) = withContext(ioDispatcher) {
        preferencesDataStore.updateData { userPreferences ->
            userPreferences.copy(isListFilter = isFiltered)
        }
    }

    suspend fun saveThemeMode(themeMode: ThemeMode) = withContext(ioDispatcher) {
        preferencesDataStore.updateData { userPreferences ->
            userPreferences.copy(themeMode = themeMode)
        }
    }
}