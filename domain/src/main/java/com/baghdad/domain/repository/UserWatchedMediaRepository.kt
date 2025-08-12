package com.baghdad.domain.repository

import com.baghdad.domain.model.continueWatching.UserWatchedMedia
import com.baghdad.domain.model.pagination.PagedResult
import com.baghdad.entity.media.Genre
import kotlinx.coroutines.flow.Flow

interface UserWatchedMediaRepository {
    suspend fun getPagedMovies(
        page: Int,
        pageSize: Int,
    ): PagedResult<UserWatchedMedia>

    suspend fun getPagedTvShows(
        page: Int,
        pageSize: Int,
    ): PagedResult<UserWatchedMedia>

    suspend fun observeUserWatchedMedia(): Flow<List<UserWatchedMedia>>

    suspend fun addUserWatchedMedia(
        contentId: Long,
        genreIds: List<Long>,
        contentImageUrl: String,
        contentType: UserWatchedMedia.ContentType,
    )

    suspend fun getUsedMovieGenres(): Flow<List<Genre>>
    suspend fun getUsedTvShowGenres(): Flow<List<Genre>>
}
