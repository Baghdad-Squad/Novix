package com.baghdad.repository.datasource.local

import com.baghdad.repository.model.TvShowDto
import kotlinx.coroutines.flow.Flow

interface LocalTvShowDataSource {
    suspend fun addTvShow(movie: TvShowDto)
    suspend fun getTvShowById(id: Long): TvShowDto
    suspend fun getAllTvShows(): Flow<List<TvShowDto>>
    suspend fun deleteTvShowById(id: Long)
    suspend fun deleteAllTvShows()
    suspend fun updateTvShow(newMovie: TvShowDto)
}