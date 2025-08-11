package com.baghdad.repository.datasource.local

import com.baghdad.repository.model.ContinueWatchingDto
import kotlinx.coroutines.flow.Flow

/**
 * ContinueWatching for aziz
 */
interface LocalContinueWatchingDataSource {
    suspend fun addContinueWatching(continueWatching: ContinueWatchingDto)

    suspend fun getContinueWatching(
        userId: Long,
        pageSize: Int,
        page: Int,
    ): List<ContinueWatchingDto>

    fun observeContinueWatching(userId: Long): Flow<List<ContinueWatchingDto>>

    fun getAllContinueWatchingMovies(userId: Long): Flow<List<ContinueWatchingDto>>
    fun getAllContinueWatchingTvShows(userId: Long): Flow<List<ContinueWatchingDto>>
}
