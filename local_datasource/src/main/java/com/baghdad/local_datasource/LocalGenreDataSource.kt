package com.baghdad.local_datasource

import com.baghdad.local_datasource.database.dao.GenreDao
import com.baghdad.local_datasource.database.dto.LocalGenreDto
import com.baghdad.local_datasource.database.dto.toDto
import com.baghdad.local_datasource.database.errorHandler.executeWithErrorHandling
import com.baghdad.repository.datasource.local.LocalGenreDataSource
import com.baghdad.repository.model.GenreDto

class LocalGenreDataSource(
    val genreDao: GenreDao
) : LocalGenreDataSource {
    override suspend fun getMovieGenre(language: String): List<GenreDto> {
        return executeWithErrorHandling {
            genreDao.getAllGenres().filter {
                it.type == GenreDto.GenreType.MOVIE.name
            }.map { it.toDto() }
        }
    }

    override suspend fun getTvShowGenre(language: String): List<GenreDto> {
        return executeWithErrorHandling {
            genreDao.getAllGenres().filter {
                it.type == GenreDto.GenreType.TV_SHOW.name
            }.map { it.toDto() }
        }
    }

    override suspend fun addGenre(
        genre: GenreDto,
        type: GenreDto.GenreType
    ) {
        executeWithErrorHandling {
            genreDao.addGenre(
                LocalGenreDto(
                    type = type.name,
                    name = genre.name
                )
            )
        }

    }

    override suspend fun getAllGenres(): List<GenreDto> {
        return executeWithErrorHandling {
            genreDao.getAllGenres().map { it.toDto() }
        }
    }

    override suspend fun getGenreById(id: Long): GenreDto {
        return executeWithErrorHandling {
            genreDao.getGenreById(id).toDto()
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