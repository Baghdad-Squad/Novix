package com.baghdad.repository

import com.baghdad.domain.repository.FavoriteGenreRepository
import com.baghdad.repository.datasource.local.LocalFavoriteGenreDataSource
import com.baghdad.repository.util.executeSafely
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FavoriteGenreRepositoryImpl @Inject constructor(
    val favoriteGenreDataSource: LocalFavoriteGenreDataSource
) : FavoriteGenreRepository {

    override suspend fun getFavoriteGenres(): Map<String, Int> {
        return executeSafely {
            favoriteGenreDataSource.getFavoriteGenres().associate { it.name to it.count }
        }
    }
}