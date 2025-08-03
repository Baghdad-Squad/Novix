package com.baghdad.repository

import com.baghdad.domain.repository.SavedListRepository
import com.baghdad.repository.datasource.local.LocalSessionDataStore
import com.baghdad.repository.datasource.remote.RemoteSavedListDataSource
import com.baghdad.repository.util.SavedListCreationException
import com.baghdad.repository.util.executeSafely

class SavedListRepositoryImpl(
    private val remoteSavedListSource: RemoteSavedListDataSource,
    private val localSessionDataStore: LocalSessionDataStore
) : SavedListRepository {
    override suspend fun createSavedList(title: String) {
        val sessionId = localSessionDataStore.getSessionId().toString()
        return executeSafely {
            try {
                remoteSavedListSource.createSavedList(title, sessionId)
            } catch (e: Exception) {
                throw SavedListCreationException("$e Failed to create saved list.")
            }
        }
    }
}
