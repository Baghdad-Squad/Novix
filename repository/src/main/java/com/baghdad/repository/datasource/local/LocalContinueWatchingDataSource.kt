package com.baghdad.repository.datasource.local

import com.baghdad.domain.model.ContinueWatching
import com.baghdad.repository.model.ContinueWatchingDto

interface LocalContinueWatchingDataSource{
    suspend fun addContinueWatching(continueWatching: ContinueWatchingDto)
    suspend fun getContinueWatching(userId: Long): List<ContinueWatchingDto>
    suspend fun getMoviesByGenreId(userId: Long, genreId: Long): List<ContinueWatchingDto>
}