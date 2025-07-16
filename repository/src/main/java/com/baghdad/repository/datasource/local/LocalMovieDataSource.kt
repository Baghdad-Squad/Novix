package com.baghdad.repository.datasource.local

import com.baghdad.repository.model.MovieDto
import kotlinx.coroutines.flow.Flow

interface LocalMovieDataSource {
    suspend fun addMovie(movie: MovieDto)
    suspend fun addMovies(movies: List<MovieDto>)
    suspend fun getMovieById(id :Long) : MovieDto
    suspend fun getAllMovies(): Flow<List<MovieDto>>
    suspend fun deleteMovieById(id: Long)
    suspend fun deleteAllMovies()
    suspend fun updateMovie(newMovie: MovieDto)
    suspend fun searchMoviesByTitle(title: String): List<MovieDto>
}