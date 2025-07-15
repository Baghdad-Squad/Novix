package com.baghdad.domain.repository


interface FavoriteGenreRepository {
    suspend fun getFavoriteGenres(): Map<String, Int>
}