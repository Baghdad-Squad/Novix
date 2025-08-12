package com.baghdad.domain.repository

import com.baghdad.domain.model.ContinueWatching
import com.baghdad.domain.model.PagedResult
import com.baghdad.entity.media.Genre
import kotlinx.coroutines.flow.Flow

interface ContinueWatchingRepository {
    suspend fun getPagedMovies(
        page: Int,
        pageSize: Int,
    ): PagedResult<ContinueWatching>

    suspend fun getPagedTvShows(
        page: Int,
        pageSize: Int,
    ): PagedResult<ContinueWatching>

    suspend fun observeContinueWatching(): Flow<List<ContinueWatching>>

    suspend fun addContinueWatching(
        contentId: Long,
        genreIds: List<Long>,
        contentImageUrl: String,
        contentType: ContinueWatching.ContentType,
    )

    suspend fun getUsedMovieGenres(): Flow<List<Genre>>
    suspend fun getUsedTvShowGenres(): Flow<List<Genre>>
}
