package com.baghdad.repository

import com.baghdad.domain.model.pagination.PagedResult
import com.baghdad.domain.model.savedList.SavedListDetails
import com.baghdad.domain.repository.SavedListRepository
import com.baghdad.entity.savedList.SavedList
import com.baghdad.repository.datasource.local.SavableMovieDataSource
import com.baghdad.repository.datasource.local.SessionDataSource
import com.baghdad.repository.datasource.local.UserDataSource
import com.baghdad.repository.datasource.remote.RemoteSavedListDataSource
import com.baghdad.repository.mapper.toEntity
import com.baghdad.repository.mapper.toPagedResult
import com.baghdad.repository.model.PagedResultDto
import com.baghdad.repository.model.SavedListDto
import com.baghdad.repository.model.savedList.SavableMovieDto
import com.baghdad.repository.util.executeSafely
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SavedListRepositoryImpl
    @Inject
    constructor(
        private val remoteSavedListSource: RemoteSavedListDataSource,
        private val sessionDataSource: SessionDataSource,
        private val userDataSource: UserDataSource,
        private val savableMovieDataSource: SavableMovieDataSource,
    ) : SavedListRepository {
        override suspend fun createSavedList(title: String) {
            val sessionId = sessionDataSource.getSessionId()
            return executeSafely {
                remoteSavedListSource.createSavedList(title)
            }
        }

        override suspend fun getSavedLists(
            page: Int,
            pageSize: Int,
        ): PagedResult<SavedList> {
            val accountId = userDataSource.getUser()?.id ?: 0
            return executeSafely{
                remoteSavedListSource
                    .getSavedLists(
                        page = page,
                        pageSize = pageSize,
                        accountId = accountId,
                    ).toPagedResult(SavedListDto::toEntity)
            }
        }

        override suspend fun addMovieToSavedList(
            listId: Long,
            movieId: Long,
        ) {
            val sessionId = sessionDataSource.getSessionId()
            executeSafely{
                remoteSavedListSource.addMovieToSavedList(listId, movieId)
                savableMovieDataSource.addSavedMovie(listId, movieId)
            }
        }

        override suspend fun removeMovieFromSavedList(
            listId: Long,
            movieId: Long,
        ) {
            val sessionId = sessionDataSource.getSessionId()
            executeSafely{
                remoteSavedListSource.removeMovieFromSavedList(listId, movieId)
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
            executeSafely {
                remoteSavedListSource.deleteSavedListById(listId)
                savableMovieDataSource.deleteListMovies(listId)
            }
        }

        override suspend fun syncSavedMoviesCache() {
            executeSafely {
                if (shouldPreformSync().not()) return@executeSafely
                val accountId = getUserAccountId()
                val savedLists = fetchAllSavedLists(accountId =  accountId)

                savedLists.forEach { list ->
                    val movies = fetchAllMoviesForList(list.id)
                    savableMovieDataSource.saveMovies(list.id, movies)
                }
            }
        }

        override suspend fun clearSavedMoviesCache() {
            executeSafely {
                savableMovieDataSource.deleteAllSavedMovies()
            }
        }

        private suspend fun shouldPreformSync(): Boolean = savableMovieDataSource.getSavedMovies().isEmpty()

    private suspend fun getUserAccountId(): Long = userDataSource.getUser()?.id ?: 0

        private suspend fun fetchAllSavedLists(
            accountId: Long,
        ): List<SavedListDto> =
            fetchAllPages { page ->
                remoteSavedListSource.getSavedLists(
                    page = page,
                    pageSize = 20,
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
