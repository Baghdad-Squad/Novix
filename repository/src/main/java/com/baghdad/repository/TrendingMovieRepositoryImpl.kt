package com.baghdad.repository

import com.baghdad.domain.model.PagedResult
import com.baghdad.domain.repository.TrendingMovieRepository
import com.baghdad.entity.media.Movie
import com.baghdad.repository.datasource.local.LocalTrendingMovieDataSource
import com.baghdad.repository.datasource.remote.RemoteMovieDataSource
import com.baghdad.repository.mapper.toDto
import com.baghdad.repository.mapper.toEntity
import com.baghdad.repository.model.MovieDto
import com.baghdad.repository.util.getPagedSafely

class TrendingMovieRepositoryImpl(
    val remoteMovieDataSource: RemoteMovieDataSource,
    val localTrendingMovie: LocalTrendingMovieDataSource,
) : TrendingMovieRepository {
    override suspend fun getTrendingMovies(page: Int): PagedResult<Movie> {
        return getPagedSafely(
            page = page,
            mapToEntity = MovieDto::toEntity,

            getCachedPage = { page, pageSize ->
                localTrendingMovie.getTrendingMovies(page)
            },
            getRemoteData = { page, _ -> remoteMovieDataSource.getTrendingMovies(page) },
            cacheData = {
                localTrendingMovie.addTrendingMovies(it)
            }
        )

    }

    override suspend fun addTrendingMovie(movies: List<Movie>) {
        localTrendingMovie.addTrendingMovies(movies.map { it.toDto() })
    }
}