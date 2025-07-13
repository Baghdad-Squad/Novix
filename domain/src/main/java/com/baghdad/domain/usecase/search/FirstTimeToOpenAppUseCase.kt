package com.baghdad.domain.usecase.search

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class FirstTimeToOpenAppUseCase(private val context: Context) {

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "app_preferences")

    private companion object {
        val FIRST_TIME_KEY = booleanPreferencesKey("is_first_time")
    }

    fun isFirstTime(): Flow<Boolean> {
        return context.dataStore.data
            .map { preferences ->
                preferences[FIRST_TIME_KEY] ?: true
            }
    }

    suspend fun setFirstTimeCompleted() {
        context.dataStore.edit { preferences ->
            preferences[FIRST_TIME_KEY] = false
        }
    }
}