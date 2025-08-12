package com.baghdad.domain.repository

import com.baghdad.domain.model.continueWatching.UserWatchedMedia
import com.baghdad.domain.model.pagination.PagedResult
import kotlinx.coroutines.flow.Flow

interface ContinueWatchingRepository {
    suspend fun getContinueWatching(
        page: Int,
        pageSize: Int,
    ): PagedResult<UserWatchedMedia>

    suspend fun observeContinueWatching(): Flow<List<UserWatchedMedia>>

    suspend fun addContinueWatching(
        contentId: Long,
        genreIds: List<Long>,
        contentImageUrl: String,
        contentType: UserWatchedMedia.ContentType,
    )

    suspend fun getAllContinueWatchingMovies(): Flow<List<UserWatchedMedia>>
    suspend fun getAllContinueWatchingTvShows(): Flow<List<UserWatchedMedia>>
}
