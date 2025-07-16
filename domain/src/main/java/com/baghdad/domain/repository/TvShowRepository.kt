package com.baghdad.domain.repository

import com.baghdad.entity.media.Genre
import com.baghdad.entity.media.TvShow

interface TvShowRepository {
    suspend fun getGenres() : List<Genre>
    suspend fun searchTvShowsByName(query: String): List<TvShow>
}