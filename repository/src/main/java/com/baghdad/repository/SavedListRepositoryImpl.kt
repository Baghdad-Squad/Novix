package com.baghdad.repository

import com.baghdad.domain.repository.SavedListRepository
import com.baghdad.repository.datasource.remote.RemoteSavedListDataSource
import javax.inject.Inject

class SavedListRepositoryImpl @Inject constructor(
    private val remoteSavedListSource: RemoteSavedListDataSource,
) : SavedListRepository {
    override suspend fun deleteSavedListByTitle(title: String) {
        remoteSavedListSource.deleteSavedListByTitle(title)
    }
}
