package com.baghdad.repository.datasource.local

import com.baghdad.repository.model.MovieDto

interface LocalTrendingMovieDataSource {
    suspend fun addTrendingMovies(movies: List<MovieDto>)
    suspend fun getTrendingMovies(page: Int, pageSize: Int = 20): List<MovieDto>
}