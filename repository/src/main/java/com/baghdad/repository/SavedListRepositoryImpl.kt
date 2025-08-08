package com.baghdad.repository

import com.baghdad.domain.model.PagedResult
import com.baghdad.domain.model.savedList.SavedListDetails
import com.baghdad.domain.repository.SavedListRepository
import com.baghdad.entity.savedList.SavedList
import com.baghdad.repository.datasource.local.LocalSavableMovieDataSource
import com.baghdad.repository.datasource.local.LocalSessionDataStore
import com.baghdad.repository.datasource.local.LocalUserDataStore
import com.baghdad.repository.datasource.remote.RemoteSavedListDataSource
import com.baghdad.repository.mapper.toEntity
import com.baghdad.repository.mapper.toPagedResult
import com.baghdad.repository.model.SavedListDto
import com.baghdad.repository.model.savedList.SavableMovieDto
import com.baghdad.repository.util.executeAuthorizedSafely
import com.baghdad.repository.util.executeSafely
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SavedListRepositoryImpl
    @Inject
    constructor(
        private val remoteSavedListSource: RemoteSavedListDataSource,
        private val localSessionDataStore: LocalSessionDataStore,
        private val localUserDataStore: LocalUserDataStore,
        private val savableMovieDataSource: LocalSavableMovieDataSource,
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
                remoteSavedListSource
                    .getSavedLists(
                        page = page,
                        pageSize = pageSize,
                        accountId = accountId,
                        sessionId = sessionId,
                    ).toPagedResult(SavedListDto::toEntity)
            }
        }

        override suspend fun addMovieToSavedList(
            listId: Long,
            movieId: Long,
        ) {
            val sessionId = localSessionDataStore.getSessionId()
            executeAuthorizedSafely(sessionId) { sessionId ->
                remoteSavedListSource.addMovieToSavedList(listId, movieId, sessionId)
                savableMovieDataSource.addSavedMovie(listId, movieId)
            }
        }

        override suspend fun removeMovieFromSavedList(
            listId: Long,
            movieId: Long,
        ) {
            val sessionId = localSessionDataStore.getSessionId()
            executeAuthorizedSafely(sessionId) { sessionId ->
                remoteSavedListSource.removeMovieFromSavedList(listId, movieId, sessionId)
                savableMovieDataSource.deleteSavedMovie(movieId)
            }
        }

        override suspend fun getSavedListDetails(
            listId: Long,
            page: Int,
            pageSize: Int,
        ): SavedListDetails =
            executeSafely {
                remoteSavedListSource.getSavedListDetails(listId, page, pageSize).toEntity()
            }

        override suspend fun deleteSavedListById(listId: Long) {
            val sessionId = localSessionDataStore.getSessionId()
            executeAuthorizedSafely(sessionId) { sessionId ->
                remoteSavedListSource.deleteSavedListById(listId, sessionId)
                savableMovieDataSource.deleteListMovies(listId)
            }
        }

        override suspend fun syncSavedMovies() {
            executeAuthorizedSafely(localSessionDataStore.getSessionId()) { sessionId ->
                val allSavedLists = mutableListOf<SavedListDto>()
                var page = 1
                var nextKey: Int? = null
                val accountId = localUserDataStore.getUser()?.id ?: 0
                do {
                    val result =
                        remoteSavedListSource.getSavedLists(
                            page = page,
                            pageSize = 20,
                            sessionId = sessionId,
                            accountId = accountId,
                        )
                    allSavedLists.addAll(result.data)
                    nextKey = result.nextKey
                    page = nextKey ?: -1
                } while (nextKey != null)
                allSavedLists.forEach { list ->
                    val savedMovies = mutableListOf<SavableMovieDto>()
                    var page = 1
                    do {
                        val result =
                            remoteSavedListSource.getSavedListDetails(
                                page = page,
                                pageSize = 20,
                                listId = list.id,
                            )
                        savedMovies.addAll(result.pagedItems.data)
                        nextKey = result.pagedItems.nextKey
                    page = nextKey ?: -1
                } while (nextKey != null)
                savableMovieDataSource.saveMovies(list.id, savedMovies)
            }
        }
    }
}
