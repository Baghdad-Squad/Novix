package com.baghdad.local_datasource

import com.baghdad.local_datasource.database.dao.MovieDao
import com.baghdad.local_datasource.database.dto.toDto
import com.baghdad.local_datasource.database.dto.toEntity
import com.baghdad.local_datasource.database.errorHandler.executeWithErrorHandling
import com.baghdad.repository.datasource.local.LocalMovieDataSource
import com.baghdad.repository.model.MovieDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class LocalMovieDataSourceImpl(
    private val movieDao: MovieDao
) : LocalMovieDataSource {
    override suspend fun addMovie(movie: MovieDto) {
        executeWithErrorHandling {
            val movieEntity = movie.toEntity()
            movieDao.upsertMovie(movieEntity)
        }
    }

    override suspend fun getMovieById(id: Long): MovieDto {
        return executeWithErrorHandling {
            movieDao.getMovieById(id).toDto()
        }
    }

    override suspend fun getAllMovies(): Flow<List<MovieDto>> {
        return executeWithErrorHandling {
            movieDao.getAllMovies().map {
                it.map { it.toDto() }
            }
        }
    }

    override suspend fun deleteMovieById(id: Long) {
        executeWithErrorHandling {
            movieDao.deleteMovieById(id)
        }
    }

    override suspend fun deleteAllMovies() {
        return executeWithErrorHandling {
            movieDao.deleteAll()
        }
    }

    override suspend fun updateMovie(newMovie: MovieDto) {
        executeWithErrorHandling {
            val movieEntity = newMovie.toEntity()
            movieDao.upsertMovie(movieEntity)
        }
    }

    override suspend fun searchMoviesByTitle(title: String): List<MovieDto> {
        return executeWithErrorHandling {
            movieDao.searchMoviesByTitle(title).map {
                it.toDto()
            }
        }
    }
}
