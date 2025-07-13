package com.baghdad.repository.datasource.local

import com.baghdad.repository.model.GenreDto

interface LocalGenreDataSource {
    suspend fun getMovieGenre(language: String): List<GenreDto>
    suspend fun getTvShowGenre(language: String): List<GenreDto>
    suspend fun addGenre(genre: GenreDto, type: GenreDto.GenreType)
    suspend fun getAllGenres(): List<GenreDto>
    suspend fun getGenreById(id: Long): GenreDto
    suspend fun deleteGenreById(id: Long)
    suspend fun deleteAllGenres()

}