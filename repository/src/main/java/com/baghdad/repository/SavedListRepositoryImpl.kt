package com.baghdad.repository

import com.baghdad.domain.model.PagedResult
import com.baghdad.domain.repository.SavedListRepository
import com.baghdad.entity.savedList.SavedList
import com.baghdad.repository.datasource.local.LocalSessionDataStore
import com.baghdad.repository.datasource.local.LocalUserDataStore
import com.baghdad.repository.datasource.remote.RemoteSavedListDataSource
import com.baghdad.repository.mapper.toEntity
import com.baghdad.repository.mapper.toPagedResult
import com.baghdad.repository.model.SavedListDto
import com.baghdad.repository.util.executeAuthorizedSafely
import javax.inject.Inject

class SavedListRepositoryImpl @Inject constructor(
    private val remoteSavedListSource: RemoteSavedListDataSource,
    private val localSessionDataStore: LocalSessionDataStore,
    private val localUserDataStore: LocalUserDataStore,
) : SavedListRepository {
    override suspend fun getSavedLists(
        page: Int,
        pageSize: Int,
    ): PagedResult<SavedList> {
        val sessionId = localSessionDataStore.getSessionId()
        val accountId = localUserDataStore.getUser()?.id ?: 0
        return executeAuthorizedSafely(sessionId) { sessionId ->
            remoteSavedListSource.getSavedLists(
                page = page,
                pageSize = pageSize,
                accountId = accountId,
                sessionId = sessionId,
            ).toPagedResult(SavedListDto::toEntity)
        }
    }
}

