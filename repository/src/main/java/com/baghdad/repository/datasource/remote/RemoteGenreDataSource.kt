package com.baghdad.repository.datasource.remote

import com.baghdad.repository.model.GenreDto

interface RemoteGenreDataSource {
    suspend fun getMovieGenre(language: String): List<GenreDto>

    suspend fun getTvShowGenre(language: String): List<GenreDto>
}