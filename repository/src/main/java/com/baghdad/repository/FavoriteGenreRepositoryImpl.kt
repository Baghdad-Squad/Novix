package com.baghdad.repository

import com.baghdad.domain.repository.FavoriteGenreRepository
import com.baghdad.repository.datasource.local.LocalFavoriteGenreDataSource
import com.baghdad.repository.util.executeSafely

class FavoriteGenreRepositoryImpl(
    val favoriteGenreDataSource: LocalFavoriteGenreDataSource
) : FavoriteGenreRepository {

    override suspend fun getFavoriteGenres(): Map<String, Int> {
        return executeSafely {
            favoriteGenreDataSource.getFavoriteGenres().associate { it.name to it.count }
        }
    }
}