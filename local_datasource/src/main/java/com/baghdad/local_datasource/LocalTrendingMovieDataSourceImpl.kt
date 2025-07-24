package com.baghdad.local_datasource

import com.baghdad.local_datasource.roomDB.dao.TrendingMovieDoa
import com.baghdad.local_datasource.roomDB.entity.toDto
import com.baghdad.local_datasource.roomDB.entity.toTrendingMovie
import com.baghdad.local_datasource.util.calculatePageOffset
import com.baghdad.repository.datasource.local.LocalTrendingMovieDataSource
import com.baghdad.repository.model.MovieDto

class LocalTrendingMovieDataSourceImpl(val trendingMovieDoa: TrendingMovieDoa) :
    LocalTrendingMovieDataSource {
    override suspend fun addTrendingMovies(movies: List<MovieDto>) {
        trendingMovieDoa.upsertMovies(movies.map { it.toTrendingMovie() })
    }

    override suspend fun getTrendingMovies(
        page: Int,
        pageSize: Int
    ): List<MovieDto> {
        val pageOffset = calculatePageOffset(pageSize, page)
        return trendingMovieDoa.getTrendingMovie(pageSize, pageOffset).map { it.toDto() }
    }
}