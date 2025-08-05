package com.baghdad.domain.repository

import com.baghdad.domain.model.ContinueWatching
import com.baghdad.domain.model.PagedResult
import kotlinx.coroutines.flow.Flow

interface ContinueWatchingRepository {
    suspend fun getContinueWatching(
        page: Int,
        pageSize: Int,
    ): PagedResult<ContinueWatching>

    fun observeContinueWatching(): Flow<List<ContinueWatching>>

    suspend fun addContinueWatching(
        contentId: Long,
        genreIds: List<Long>,
        contentImageUrl: String,
        contentType: ContinueWatching.ContentType,
    )

    suspend fun getAllContinueWatchingMovies(): Flow<List<ContinueWatching>>
    suspend fun getAllContinueWatchingTvShows(): Flow<List<ContinueWatching>>
}
