package com.baghdad.repository

import com.baghdad.domain.repository.TvShowRepository
import com.baghdad.entity.media.Genre

class TvShowRepositoryImpl: TvShowRepository {
    override suspend fun getGenres(): List<Genre> {
        // TODO("Not yet implemented")
        return emptyList()
    }
}