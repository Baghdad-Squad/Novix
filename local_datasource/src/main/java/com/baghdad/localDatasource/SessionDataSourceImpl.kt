package com.baghdad.localDatasource

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.baghdad.localDatasource.errorHandler.safeDataStoreCall
import com.baghdad.repository.datasource.local.SessionDataSource
import com.baghdad.repository.logger.Logger
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

@Singleton
class SessionDataSourceImpl @Inject constructor(
    @Named("preferences") private val dataStore: DataStore<Preferences>,
    private val logger: Logger
) : SessionDataSource {
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