package com.baghdad.local_datasource

import com.baghdad.local_datasource.dataStore.AppPreferencesDataStore
import com.baghdad.local_datasource.dataStore.safeDataStoreCall
import com.baghdad.repository.datasource.local.LocalSessionDataSource
import com.baghdad.repository.logger.Logger
import com.baghdad.repository.model.UserDto

class LocalSessionDataSourceImpl(
    private val appPreferencesDataStore: AppPreferencesDataStore,
    private val logger: Logger
) : LocalSessionDataSource {
    override suspend fun saveSessionId(sessionId: String) {
        safeDataStoreCall(
            block = { appPreferencesDataStore.saveSessionId(sessionId) },
            logger = logger
        )
    }

    override suspend fun getSessionId(): String? {
        return safeDataStoreCall(
            block = { appPreferencesDataStore.getSessionId() },
            logger = logger
        )
    }

    override suspend fun deleteSessionId() {
        safeDataStoreCall(
            block = { appPreferencesDataStore.deleteSessionId() },
            logger = logger
        )
    }
    override suspend fun getUserDetails(): UserDto? {
        return safeDataStoreCall(
            block = { appPreferencesDataStore.getUserDetails() },
            logger = logger
        )
    }
    override suspend fun clearSession() {
        safeDataStoreCall(
            block = { appPreferencesDataStore.clearSession() },
        )
    }
}