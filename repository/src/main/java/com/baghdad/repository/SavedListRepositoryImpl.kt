package com.baghdad.repository

import com.baghdad.domain.model.PagedResult
import com.baghdad.domain.repository.SavedListRepository
import com.baghdad.entity.savedList.SavedList
import com.baghdad.repository.datasource.local.LocalSessionDataStore
import com.baghdad.repository.datasource.remote.RemoteSavedListDataSource
import com.baghdad.repository.mapper.toEntity
import com.baghdad.repository.mapper.toPagedResult
import com.baghdad.repository.model.SavedListDto
import com.baghdad.repository.util.executeAuthorizedSafely

class SavedListRepositoryImpl(
    private val remoteSavedListSource: RemoteSavedListDataSource,
    private val localSessionDataStore: LocalSessionDataStore
) : SavedListRepository {
    override suspend fun getSavedLists(
        page: Int,
        pageSizes: Int,
    ): PagedResult<SavedList> {
        val sessionId = localSessionDataStore.getSessionId().toString()
        return executeAuthorizedSafely(sessionId) {
            remoteSavedListSource.getSavedLists(
                page = page,
                sessionId = localSessionDataStore.getSessionId().toString()
            ).toPagedResult(SavedListDto::toEntity)
        }
    }
}

