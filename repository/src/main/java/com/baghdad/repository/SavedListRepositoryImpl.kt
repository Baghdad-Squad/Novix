package com.baghdad.repository

import com.baghdad.domain.model.savedList.SavedListDetails
import com.baghdad.domain.repository.SavedListRepository
import com.baghdad.repository.datasource.remote.RemoteSavedListDataSource
import com.baghdad.repository.mapper.toEntity
import com.baghdad.repository.util.executeSafely

class SavedListRepositoryImpl(
    private val remoteSavedListSource: RemoteSavedListDataSource,
) : SavedListRepository {
    override suspend fun getSavedListDetails(listId: Long): SavedListDetails =
        executeSafely {
            remoteSavedListSource.getSavedListDetails(listId).toEntity()
        }
}
