package com.baghdad.local_datasource.dataStore.session

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.baghdad.local_datasource.dataStore.safeDataStoreCall
import com.baghdad.repository.datasource.local.LocalSessionDataStore
import com.baghdad.repository.logger.Logger
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

@Singleton
class LocalSessionDataStoreImpl @Inject constructor(
    @Named("preferences") private val dataStore: DataStore<Preferences>,
    private val logger: Logger
) : LocalSessionDataStore {
    override suspend fun saveSessionId(sessionId: String) {
        safeDataStoreCall(
            block = {
                dataStore.edit {
                    it[SESSION_ID] = sessionId
                }
            },
            logger = logger
        )
    }

    override suspend fun getSessionId(): String? {
        return safeDataStoreCall(
            block = {
                dataStore.data.map {
                    it[SESSION_ID]
                }.firstOrNull()
            },
            logger = logger
        )
    }

    override suspend fun deleteSessionId() {
        safeDataStoreCall(
            block = {
                dataStore.edit {
                    it.remove(SESSION_ID)
                }
            },
            logger = logger
        )
    }

    private companion object {
        val SESSION_ID = stringPreferencesKey("sessionId")
    }
}