package com.baghdad.repository

import com.baghdad.domain.model.PagedResult
import com.baghdad.domain.repository.TrendingMovieRepository
import com.baghdad.entity.media.Movie
import com.baghdad.repository.datasource.remote.RemoteMovieDataSource
import com.baghdad.repository.mapper.toEntity
import com.baghdad.repository.mapper.toPagedResult
import com.baghdad.repository.util.executeSafely

class TrendingMovieRepositoryImpl(
    private val remoteMovieDataSource: RemoteMovieDataSource,
) : TrendingMovieRepository {
    override suspend fun getTrendingMovies(page: Int): PagedResult<Movie> {
        return executeSafely {
            remoteMovieDataSource.getTrendingMovies(page).toPagedResult {
                it.toEntity()
            }
        }
    }
}