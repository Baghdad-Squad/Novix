package com.baghdad.repository

import com.baghdad.domain.model.continueWatching.UserWatchedMedia
import com.baghdad.domain.model.pagination.PagedResult
import com.baghdad.domain.repository.AuthenticationRepository
import com.baghdad.domain.repository.ContinueWatchingRepository
import com.baghdad.repository.datasource.local.LocalContinueWatchingDataSource
import com.baghdad.repository.datasource.local.LocalSavableMovieDataSource
import com.baghdad.repository.mapper.toDto
import com.baghdad.repository.mapper.toEntity
import com.baghdad.repository.model.ContinueWatchingDto
import com.baghdad.repository.util.executeSafely
import com.baghdad.repository.util.getLocalPagedSafely
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ContinueWatchingRepositoryImpl @Inject constructor(
    private val localContinueWatchingDataSource: LocalContinueWatchingDataSource,
    private val authenticationRepository: AuthenticationRepository,
    private val savableMovieDataSource: LocalSavableMovieDataSource,
) : ContinueWatchingRepository {

    override suspend fun getAllContinueWatchingMovies(): Flow<List<UserWatchedMedia>> {
        val userId = getUserId() ?: return flowOf(emptyList())
        val savedMovies = getSavedMovies()

        return localContinueWatchingDataSource.getAllContinueWatchingMovies(userId)
            .map { list -> mapDtosToEntities(dtos = list, savedMovies = savedMovies) }
    }

    override suspend fun observeContinueWatching(): Flow<List<UserWatchedMedia>> {
        val userId = getUserId() ?: return flowOf(emptyList())
        val savedMovies = getSavedMovies()

        return localContinueWatchingDataSource.observeContinueWatching(userId)
            .map { list -> mapDtosToEntities(dtos = list, savedMovies = savedMovies) }
    }

    override suspend fun getContinueWatching(
        page: Int,
        pageSize: Int,
    ): PagedResult<UserWatchedMedia> {
        val userId = getUserId() ?: return emptyPagedResult()
        val savedMovies = getSavedMovies()

        return getLocalPagedSafely(
            page = page,
            pageSize = pageSize,
            getCachedPage = { _, _ ->
                localContinueWatchingDataSource.getContinueWatching(userId, pageSize, page)
            },
            mapToEntity = { dto -> mapDtoToEntity(dto = dto, savedMovies = savedMovies) }
        )
    }

    override suspend fun addContinueWatching(
        contentId: Long,
        genreIds: List<Long>,
        contentImageUrl: String,
        contentType: UserWatchedMedia.ContentType,
    ) {
        return executeSafely {
            val userId = getUserId() ?: return@executeSafely
            val continueWatching = continueWatching(
                contentId = contentId,
                genreIds = genreIds,
                contentImageUrl = contentImageUrl,
                contentType = contentType,
                userId = userId
            )
            localContinueWatchingDataSource.addContinueWatching(continueWatching.toDto())
        }
    }

    override suspend fun getAllContinueWatchingTvShows(): Flow<List<UserWatchedMedia>> {
        val userId = getUserId() ?: return flowOf(emptyList())
        val savedMovies = getSavedMovies()

        return localContinueWatchingDataSource.getAllContinueWatchingTvShows(userId)
            .map { list -> mapDtosToEntities(dtos = list, savedMovies = savedMovies) }
    }

    private suspend fun getUserId(): Long? =
        authenticationRepository.getLoggedInUser()?.id

    private suspend fun getSavedMovies(): Map<Long, Long> =
        savableMovieDataSource.getSavedMovies()

    private fun emptyPagedResult() =
        PagedResult<UserWatchedMedia>(data = emptyList(), nextKey = 0, prevKey = 0)

    private fun mapDtoToEntity(
        dto: ContinueWatchingDto,
        savedMovies: Map<Long, Long>
    ): UserWatchedMedia = dto.toEntity(
        isSaved = savedMovies.containsKey(dto.contentId),
        listId = savedMovies[dto.contentId]
    )

    private fun mapDtosToEntities(
        dtos: List<ContinueWatchingDto>,
        savedMovies: Map<Long, Long>
    ): List<UserWatchedMedia> = dtos.map { mapDtoToEntity(it, savedMovies) }

    private fun continueWatching(
        contentId: Long,
        genreIds: List<Long>,
        contentImageUrl: String,
        contentType: UserWatchedMedia.ContentType,
        userId: Long,
    ): UserWatchedMedia = UserWatchedMedia(
        contentId = contentId,
        genreIds = genreIds,
        contentImageUrl = contentImageUrl,
        contentType = contentType,
        userId = userId,
        isSaved = false,
        listId = null
    )
}