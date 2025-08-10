package com.baghdad.repository

import com.baghdad.domain.model.ContinueWatching
import com.baghdad.domain.model.PagedResult
import com.baghdad.domain.repository.AuthenticationRepository
import com.baghdad.domain.repository.ContinueWatchingRepository
import com.baghdad.repository.datasource.local.LocalContinueWatchingDataSource
import com.baghdad.repository.datasource.local.LocalSavableMovieDataSource
import com.baghdad.repository.mapper.toDto
import com.baghdad.repository.mapper.toEntity
import com.baghdad.repository.util.executeSafely
import com.baghdad.repository.util.getLocalPagedSafely
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ContinueWatchingRepositoryImpl
    @Inject
    constructor(
        private val localContinueWatchingDataSource: LocalContinueWatchingDataSource,
        private val authenticationRepository: AuthenticationRepository,
        private val savableMovieDataSource: LocalSavableMovieDataSource,
    ) : ContinueWatchingRepository {
        override suspend fun getContinueWatching(
            page: Int,
            pageSize: Int,
        ): PagedResult<ContinueWatching> {
            authenticationRepository.getLoggedInUser()?.let {
                val savedMovies = savableMovieDataSource.getSavedMovies()
                return getLocalPagedSafely(
                    page = page,
                    pageSize = pageSize,
                    getCachedPage = { _, _ ->
                        localContinueWatchingDataSource.getContinueWatching(
                            it.id,
                            pageSize,
                            page,
                        )
                    },
                    mapToEntity = { dto ->
                        dto.toEntity(
                            isSaved = savedMovies.containsKey(dto.contentId),
                            listId = savedMovies[dto.contentId],
                        )
                    },
                )
            }
            return PagedResult(emptyList(), 0, 0)
        }

        override suspend fun observeContinueWatching(): Flow<List<ContinueWatching>> {
            authenticationRepository.getLoggedInUser()?.let {
                val savedMovies = savableMovieDataSource.getSavedMovies()
                return localContinueWatchingDataSource
                    .observeContinueWatching(it.id)
                    .map { continueWatchingItems ->
                        continueWatchingItems.map { dto ->
                            dto.toEntity(
                                isSaved = savedMovies.containsKey(dto.contentId),
                                listId = savedMovies[dto.contentId],
                            )
                        }
                    }
            }
            return flowOf(emptyList())
        }

        override suspend fun addContinueWatching(
            contentId: Long,
            genreIds: List<Long>,
            contentImageUrl: String,
            contentType: ContinueWatching.ContentType,
        ) {
            executeSafely {
                val userId = authenticationRepository.getLoggedInUser()?.id ?: return@executeSafely
                val continueWatching =
                    ContinueWatching(
                        contentId = contentId,
                        genreIds = genreIds,
                        contentImageUrl = contentImageUrl,
                        contentType = contentType,
                        userId = userId,
                        isSaved = false,
                        listId = null,
                    )
                localContinueWatchingDataSource.addContinueWatching(continueWatching.toDto())
            }
        }

        override suspend fun getAllContinueWatchingMovies(): Flow<List<ContinueWatching>> {
            authenticationRepository.getLoggedInUser()?.let {
                val savedMovies = savableMovieDataSource.getSavedMovies()
                return localContinueWatchingDataSource
                    .getAllContinueWatchingMovies(it.id)
                    .map { continueWatchingItems ->
                        continueWatchingItems.map { dto ->
                            dto.toEntity(
                                isSaved = savedMovies.containsKey(dto.contentId),
                                listId = savedMovies[dto.contentId],
                            )
                        }
                    }
            }
            return flowOf(emptyList())
        }

        override suspend fun getAllContinueWatchingTvShows(): Flow<List<ContinueWatching>> {
            authenticationRepository.getLoggedInUser()?.let {
                val savedMovies = savableMovieDataSource.getSavedMovies()
                return localContinueWatchingDataSource
                    .getAllContinueWatchingTvShows(it.id)
                    .map { continueWatchingItems ->
                    continueWatchingItems.map { dto ->
                        dto.toEntity(
                            isSaved = savedMovies.containsKey(dto.contentId),
                            listId = savedMovies[dto.contentId],
                        )
                    }
                }
        }
        return flowOf(emptyList())
    }
}
