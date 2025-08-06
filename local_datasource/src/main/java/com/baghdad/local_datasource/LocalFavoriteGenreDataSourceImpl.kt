package com.baghdad.local_datasource

import com.baghdad.local_datasource.roomDB.dao.FavoriteGenreDao
import com.baghdad.local_datasource.roomDB.entity.toDto
import com.baghdad.local_datasource.roomDB.errorHandler.executeWithErrorHandling
import com.baghdad.repository.datasource.local.LocalFavoriteGenreDataSource
import com.baghdad.repository.logger.Logger
import com.baghdad.repository.model.FavoriteGenreDto
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalFavoriteGenreDataSourceImpl @Inject constructor(
    private val favoriteGenreDao: FavoriteGenreDao,
    private val logger: Logger
) : LocalFavoriteGenreDataSource {
    override suspend fun updateFavoriteGenreCount(id: Long, name: String) {
        executeWithErrorHandling(logger = logger) {
            favoriteGenreDao.updateFavoriteGenreCount(id, name)
        }
    }

    override suspend fun getFavoriteGenres(): List<FavoriteGenreDto> {
        return executeWithErrorHandling(logger = logger) {
            favoriteGenreDao.getFavoriteGenres().map { it.toDto() }
        }
    }
}