package com.baghdad.local_datasource

import com.baghdad.local_datasource.roomDB.dao.MovieDao
import com.baghdad.local_datasource.roomDB.entity.toDto
import com.baghdad.local_datasource.roomDB.entity.toEntity
import com.baghdad.local_datasource.roomDB.errorHandler.executeWithErrorHandling
import com.baghdad.repository.datasource.local.LocalMovieDataSource
import com.baghdad.repository.model.MovieDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class LocalMovieDataSourceImpl(
    private val movieDao: MovieDao
) : LocalMovieDataSource {
    override suspend fun addMovie(movie: MovieDto) =
        executeWithErrorHandling {
            val movieEntity = movie.toEntity()
            movieDao.upsertMovie(movieEntity)
        }

    override suspend fun getMovieById(id: Long): MovieDto =
        executeWithErrorHandling {
            movieDao.getMovieById(id).toDto()
        }

    override suspend fun getAllMovies(): Flow<List<MovieDto>> =
        executeWithErrorHandling {
            movieDao.getAllMovies().map {
                it.map { it.toDto() }
            }
        }

    override suspend fun deleteMovieById(id: Long) =
        executeWithErrorHandling {
            movieDao.deleteMovieById(id)
        }

    override suspend fun deleteAllMovies() =
        executeWithErrorHandling {
            movieDao.deleteAll()
        }

    override suspend fun updateMovie(newMovie: MovieDto) =
        executeWithErrorHandling {
            val movieEntity = newMovie.toEntity()
            movieDao.upsertMovie(movieEntity)
        }
}