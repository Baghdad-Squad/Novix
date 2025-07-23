package com.baghdad.repository.datasource.local

import com.baghdad.repository.model.GenreDto
import com.baghdad.repository.model.MovieDto

interface LocalTopRatingDataSource {
    suspend fun getMovieGenre(language: String): List<GenreDto>
    suspend fun getTopRatedMovies(page: Int, pageSize: Int): List<MovieDto>
    suspend fun saveTopRatedMovies(movies: List<MovieDto>)
}