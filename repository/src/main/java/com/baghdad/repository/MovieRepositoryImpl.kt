package com.baghdad.repository

import com.baghdad.domain.repository.MovieRepository
import com.baghdad.entity.media.Genre

class MovieRepositoryImpl: MovieRepository  {
    override suspend fun getGenres(): List<Genre> {
        // TODO("Not yet implemented")
        return emptyList()
    }
}