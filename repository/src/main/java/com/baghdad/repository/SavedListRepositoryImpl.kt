package com.baghdad.repository

import com.baghdad.domain.repository.SavedListRepository
import com.baghdad.repository.datasource.remote.RemoteSavedListDataSource
import com.baghdad.repository.util.executeSafely

class SavedListRepositoryImpl(
    private val remoteSavedListSource: RemoteSavedListDataSource,
) : SavedListRepository {
    override suspend fun createSavedList(title: String) {
        return executeSafely {
            remoteSavedListSource.createSavedList(title)
        }
    }
}
