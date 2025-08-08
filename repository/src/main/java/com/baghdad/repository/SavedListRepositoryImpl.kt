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
import com.baghdad.repository.model.PagedResultDto
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
                val accountId = getUserAccountId()
                val savedLists = fetchAllSavedLists(sessionId, accountId)

                savedLists.forEach { list ->
                    val movies = fetchAllMoviesForList(list.id)
                    savableMovieDataSource.saveMovies(list.id, movies)
                }
            }
        }

        private suspend fun getUserAccountId(): Long = localUserDataStore.getUser()?.id ?: 0

        private suspend fun fetchAllSavedLists(
            sessionId: String,
            accountId: Long,
        ): List<SavedListDto> =
            fetchAllPages { page ->
                remoteSavedListSource.getSavedLists(
                    page = page,
                    pageSize = 20,
                    sessionId = sessionId,
                    accountId = accountId,
                )
            }

        private suspend fun fetchAllMoviesForList(listId: Long): List<SavableMovieDto> =
            fetchAllPages { page ->
                val result =
                    remoteSavedListSource.getSavedListDetails(
                        page = page,
                        pageSize = 20,
                        listId = listId,
                    )
                PagedResultDto(
                    result.pagedItems.data,
                    result.pagedItems.nextKey,
                    result.pagedItems.prevKey,
                )
            }

        private suspend inline fun <T> fetchAllPages(crossinline fetchPage: suspend (page: Int) -> PagedResultDto<T>): List<T> {
            val allItems = mutableListOf<T>()
            var page = 1

            do {
                val result = fetchPage(page)
                allItems.addAll(result.data)
            page = result.nextKey ?: break
        } while (true)

        return allItems
    }
}
