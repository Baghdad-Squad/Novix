package com.baghdad.local_datasource

import com.baghdad.local_datasource.roomDB.dao.GenreDao
import com.baghdad.local_datasource.roomDB.entity.Genre
import com.baghdad.local_datasource.roomDB.entity.toDto
import com.baghdad.local_datasource.roomDB.errorHandler.executeWithErrorHandling
import com.baghdad.repository.datasource.local.LocalGenreDataSource
import com.baghdad.repository.logger.Logger
import com.baghdad.repository.model.GenreDto

class LocalGenreDataSourceImpl(
    private val genreDao: GenreDao,
    private val logger: Logger
) : LocalGenreDataSource {
    override suspend fun getMovieGenre(language: String): List<GenreDto> {
        return executeWithErrorHandling(logger = logger) {
            genreDao.getAllGenres().filter {
                it.type == GenreDto.GenreType.MOVIE.name
            }.map { it.toDto() }
        }
    }

    override suspend fun getTvShowGenre(language: String): List<GenreDto> {
        return executeWithErrorHandling(logger = logger) {
            genreDao.getAllGenres().filter {
                it.type == GenreDto.GenreType.TV_SHOW.name
            }.map { it.toDto() }
        }
    }

    override suspend fun addGenre(
        genre: GenreDto,
    ) {
        executeWithErrorHandling(logger = logger) {
            genreDao.addGenre(
                Genre(
                    type = genre.type.name,
                    name = genre.name,
                    id = genre.id
                )
            )
        }

    }

    override suspend fun getAllGenres(): List<GenreDto> {
        return executeWithErrorHandling(logger = logger) {
            genreDao.getAllGenres().map { it.toDto() }
        }
    }

    override suspend fun getGenreById(id: Long): GenreDto {
        return executeWithErrorHandling(logger = logger) {
            genreDao.getGenreById(id).toDto()
        }
    }

    override suspend fun deleteGenreById(id: Long) {
        return executeWithErrorHandling(logger = logger) {
            genreDao.deleteGenreById(id)
        }
    }

    override suspend fun deleteAllGenres() {
        return executeWithErrorHandling(logger = logger) {
            genreDao.deleteAllGenres()
        }
    }
}