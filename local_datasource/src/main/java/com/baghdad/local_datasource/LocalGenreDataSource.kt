package com.baghdad.local_datasource

import com.baghdad.local_datasource.roomDB.dao.GenreDao
import com.baghdad.local_datasource.roomDB.entity.Genre
import com.baghdad.local_datasource.roomDB.entity.toEntity
import com.baghdad.local_datasource.roomDB.errorHandler.executeWithErrorHandling
import com.baghdad.repository.datasource.local.LocalGenreDataSource
import com.baghdad.repository.model.GenreDto
import com.baghdad.repository.model.MediaType

class LocalGenreDataSource(
    val genreDao: GenreDao
) : LocalGenreDataSource {
    override suspend fun getMovieGenre(language: String): List<GenreDto> {
        return executeWithErrorHandling {
            genreDao.getAllGenres().filter {
                it.type == MediaType.MOVIE.name
            }.map { it.toEntity() }
        }
    }

    override suspend fun getTvShowGenre(language: String): List<GenreDto> {
        return executeWithErrorHandling {
            genreDao.getAllGenres().filter {
                it.type == MediaType.TV_SHOW.name
            }.map { it.toEntity() }
        }
    }

    override suspend fun addGenre(
        genre: GenreDto,
        type: MediaType
    ) {
        executeWithErrorHandling {
            genreDao.addGenre(
                Genre(
                    type = type.name,
                    name = genre.name
                )
            )
        }

    }

    override suspend fun getAllGenres(): List<GenreDto> {
        return executeWithErrorHandling {
            genreDao.getAllGenres().map { it.toEntity() }
        }
    }

    override suspend fun getGenreById(id: Long): GenreDto {
        return executeWithErrorHandling {
            genreDao.getGenreById(id).toEntity()
        }
    }

    override suspend fun deleteGenreById(id: Long) {
        return executeWithErrorHandling {
            genreDao.deleteGenreById(id)
        }
    }

    override suspend fun deleteAllGenres() {
        return executeWithErrorHandling {
            genreDao.deleteAllGenres()
        }
    }
}