package com.baghdad.repository.datasource.local

import com.baghdad.repository.model.UserWatchedMediaDto
import kotlinx.coroutines.flow.Flow

interface UserWatchedMediaDataSource {
    suspend fun addUserWatchedMedia(continueWatching: UserWatchedMediaDto)

    suspend fun getPagedUserWatchedMediaMovies(
        userId: Long,
        pageSize: Int,
        page: Int,
    ): List<UserWatchedMediaDto>

    suspend fun getPagedUserWatchedMediaTvShows(
        userId: Long,
        pageSize: Int,
        page: Int,
    ): List<UserWatchedMediaDto>

    fun observeUserWatchedMedia(userId: Long): Flow<List<UserWatchedMediaDto>>

    fun getUserWatchedMediaMovies(userId: Long): Flow<List<UserWatchedMediaDto>>

    fun getUserWatchedMediaTvShows(userId: Long): Flow<List<UserWatchedMediaDto>>
}
