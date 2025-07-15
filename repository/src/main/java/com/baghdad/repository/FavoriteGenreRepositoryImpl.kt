package com.baghdad.repository

import com.baghdad.domain.repository.FavoriteGenreRepository
import com.baghdad.entity.media.Genre
import com.baghdad.repository.datasource.local.LocalFavoriteGenreDataSource
import com.baghdad.repository.model.toEntity
import com.baghdad.repository.util.executeSafely

class FavoriteGenreRepositoryImpl(
    val favoriteGenreDataSource: LocalFavoriteGenreDataSource
) : FavoriteGenreRepository {

    override suspend fun getFavoriteGenres(): List<Genre> {
        return executeSafely {
            favoriteGenreDataSource.getFavoriteGenres().map { it.toEntity() }
        }
    }
}