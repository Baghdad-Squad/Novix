package com.baghdad.domain.repository

import com.baghdad.entity.media.Genre

interface TvShowRepository {
    suspend fun getGenres() : List<Genre>
}