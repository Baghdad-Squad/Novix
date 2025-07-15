package com.baghdad.domain.repository

import com.baghdad.entity.media.Genre


interface FavoriteGenreRepository {
    suspend fun getFavoriteGenres(): List<Genre>
}