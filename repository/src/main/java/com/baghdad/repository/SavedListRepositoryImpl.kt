package com.baghdad.repository

import com.baghdad.domain.repository.SavedListRepository
import com.baghdad.repository.datasource.local.LocalSessionDataStore
import com.baghdad.repository.datasource.remote.RemoteSavedListDataSource
import com.baghdad.repository.util.executeSafely

class SavedListRepositoryImpl(
    private val remoteSavedListSource: RemoteSavedListDataSource,
    private val localSessionDataStore: LocalSessionDataStore
) : SavedListRepository {
    override suspend fun createSavedList(title: String) {
        val sessionId = localSessionDataStore.getSessionId().toString()
        return executeSafely {
            remoteSavedListSource.createSavedList(title, sessionId)
        }
    }
}
