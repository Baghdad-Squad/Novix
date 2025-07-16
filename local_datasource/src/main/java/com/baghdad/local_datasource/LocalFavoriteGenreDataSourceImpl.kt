package com.baghdad.local_datasource

import com.baghdad.local_datasource.roomDB.dao.FavoriteGenreDao
import com.baghdad.local_datasource.roomDB.entity.toDto
import com.baghdad.local_datasource.roomDB.errorHandler.executeWithErrorHandling
import com.baghdad.repository.datasource.local.LocalFavoriteGenreDataSource
import com.baghdad.repository.model.FavoriteGenreDto

class LocalFavoriteGenreDataSourceImpl(
    val favoriteGenreDao: FavoriteGenreDao
) : LocalFavoriteGenreDataSource {
    override suspend fun updateFavoriteGenreCount(id: Long, name: String) {
        executeWithErrorHandling {
            favoriteGenreDao.updateFavoriteGenreCount(id, name)
        }
    }

    override suspend fun getFavoriteGenres(): List<FavoriteGenreDto> {
        return executeWithErrorHandling {
            favoriteGenreDao.getFavoriteGenres().map { it.toDto() }
        }
    }
}