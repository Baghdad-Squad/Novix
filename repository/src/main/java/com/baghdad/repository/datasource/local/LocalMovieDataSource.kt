package com.baghdad.repository.datasource.local

import com.baghdad.repository.model.MovieDto

interface LocalMovieDataSource {
    suspend fun addMovie(movie: MovieDto)
    suspend fun getMovieById(id :Long) : MovieDto
    suspend fun getAllMovies() : List<MovieDto>
    suspend fun deleteMovieById(id: Long)
    suspend fun deleteAllMovies()
    suspend fun updateMovie(newMovie: MovieDto)
}