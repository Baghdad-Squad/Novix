package com.baghdad.local_datasource.dataStore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map

class AppPreferencesDataStore(
    private val dataStore: DataStore<Preferences>
) {
    suspend fun saveSessionId(sessionId: String) {
        dataStore.edit {
            it[SESSION_ID] = sessionId
        }
    }

    suspend fun getSessionId(): String? {
        return dataStore.data.map {
            it[SESSION_ID]
        }.firstOrNull()
    }

    suspend fun deleteSessionId() {
        dataStore.edit {
            it.remove(SESSION_ID)
        }
    }

    private companion object {
        val SESSION_ID = stringPreferencesKey("sessionId")
    }
}