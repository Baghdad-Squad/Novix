package com.baghdad.repository

import com.baghdad.domain.model.PagedResult
import com.baghdad.domain.model.savedList.SavedListDetails
import com.baghdad.domain.repository.SavedListRepository
import com.baghdad.repository.datasource.local.LocalSessionDataStore
import com.baghdad.entity.savedList.SavedList
import com.baghdad.repository.datasource.local.LocalUserDataStore
import com.baghdad.repository.datasource.remote.RemoteSavedListDataSource
import com.baghdad.repository.mapper.toEntity
import com.baghdad.repository.mapper.toPagedResult
import com.baghdad.repository.model.SavedListDto
import com.baghdad.repository.util.executeAuthorizedSafely
import javax.inject.Inject
import com.baghdad.repository.mapper.toEntity
import com.baghdad.repository.util.executeSafely

class SavedListRepositoryImpl @Inject constructor(
    private val remoteSavedListSource: RemoteSavedListDataSource,
    private val localSessionDataStore: LocalSessionDataStore,
    private val localUserDataStore: LocalUserDataStore,
) : SavedListRepository {
    override suspend fun createSavedList(title: String) {
        val sessionId = localSessionDataStore.getSessionId()
        return executeAuthorizedSafely(sessionId) { sessionId ->
            remoteSavedListSource.createSavedList(title, sessionId)
        }
    }

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

    override suspend fun addMovieToSavedList(listId: Long, movieId: Long) {
        val sessionId = localSessionDataStore.getSessionId()
        executeAuthorizedSafely(sessionId) { sessionId ->
            remoteSavedListSource.addMovieToSavedList(listId, movieId, sessionId)
        }
    }

    override suspend fun addTvShowToSavedList(listId: Long, tvShowId: Long) {
        val sessionId = localSessionDataStore.getSessionId()
        executeAuthorizedSafely(sessionId) { sessionId ->
            remoteSavedListSource.addTvShowToSavedList(listId, tvShowId, sessionId)
        }
    }

    override suspend fun getSavedListDetails(listId: Long): SavedListDetails =
        executeSafely {
            remoteSavedListSource.getSavedListDetails(listId).toEntity()
        }
}