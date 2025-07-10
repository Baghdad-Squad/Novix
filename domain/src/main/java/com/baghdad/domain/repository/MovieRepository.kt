package com.baghdad.domain.repository

import com.baghdad.entity.media.Genre

interface MovieRepository {
    suspend fun getGenres() : List<Genre>
}