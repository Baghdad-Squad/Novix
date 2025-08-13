package com.baghdad.repository

import com.baghdad.domain.model.continueWatching.UserWatchedMedia
import com.baghdad.domain.model.pagination.PagedResult
import com.baghdad.domain.repository.AuthenticationRepository
import com.baghdad.domain.repository.ContinueWatchingRepository
import com.baghdad.repository.datasource.local.ContinueWatchingDataSource
import com.baghdad.repository.datasource.local.SavableMovieDataSource
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
        private val continueWatchingDataSource: ContinueWatchingDataSource,
        private val authenticationRepository: AuthenticationRepository,
        private val savableMovieDataSource: SavableMovieDataSource,
    ) : ContinueWatchingRepository {
        override suspend fun getContinueWatching(
            page: Int,
            pageSize: Int,
        ): PagedResult<UserWatchedMedia> {
            authenticationRepository.getLoggedInUser()?.let {
                val savedMovies = savableMovieDataSource.getSavedMovies()
                return getLocalPagedSafely(
                    page = page,
                    pageSize = pageSize,
                    getCachedPage = { _, _ ->
                        continueWatchingDataSource.getContinueWatching(
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

        override suspend fun observeContinueWatching(): Flow<List<UserWatchedMedia>> {
            authenticationRepository.getLoggedInUser()?.let {
                val savedMovies = savableMovieDataSource.getSavedMovies()
                return continueWatchingDataSource
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
            contentType: UserWatchedMedia.ContentType,
        ) {
            executeSafely {
                val userId = authenticationRepository.getLoggedInUser()?.id ?: return@executeSafely
                val userWatchedMedia =
                    UserWatchedMedia(
                        contentId = contentId,
                        genreIds = genreIds,
                        contentImageUrl = contentImageUrl,
                        contentType = contentType,
                        userId = userId,
                        isSaved = false,
                        listId = null,
                    )
                continueWatchingDataSource.addContinueWatching(userWatchedMedia.toDto())
            }
        }

        override suspend fun getAllContinueWatchingMovies(): Flow<List<UserWatchedMedia>> {
            authenticationRepository.getLoggedInUser()?.let {
                val savedMovies = savableMovieDataSource.getSavedMovies()
                return continueWatchingDataSource
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

        override suspend fun getAllContinueWatchingTvShows(): Flow<List<UserWatchedMedia>> {
            authenticationRepository.getLoggedInUser()?.let {
                val savedMovies = savableMovieDataSource.getSavedMovies()
                return continueWatchingDataSource
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
