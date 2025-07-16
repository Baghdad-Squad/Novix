package com.baghdad.repository.datasource.local

import com.baghdad.repository.model.FavoriteGenreDto

interface LocalFavoriteGenreDataSource {
    suspend fun updateFavoriteGenreCount(id: Long, name: String)
    suspend fun getFavoriteGenres(): List<FavoriteGenreDto>
}