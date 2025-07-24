package com.baghdad.domain.repository

import com.baghdad.domain.model.PagedResult
import com.baghdad.entity.media.Movie

interface TrendingMovieRepository {
    suspend fun getTrendingMovies(page: Int): PagedResult<Movie>
    suspend fun addTrendingMovie(movies: List<Movie>)
}