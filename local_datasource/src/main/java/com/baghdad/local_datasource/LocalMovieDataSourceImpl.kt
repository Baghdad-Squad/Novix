package com.baghdad.local_datasource

import com.baghdad.local_datasource.roomDB.dao.MovieDao
import com.baghdad.local_datasource.roomDB.entity.toDto
import com.baghdad.local_datasource.roomDB.entity.toEntity
import com.baghdad.repository.datasource.local.LocalMovieDataSource
import com.baghdad.repository.model.MovieDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class LocalMovieDataSourceImpl(
    private val movieDao: MovieDao
) : LocalMovieDataSource {
    override suspend fun addMovie(movie: MovieDto) {
        val movieEntity = movie.toEntity()
        return movieDao.upsertMovie(movieEntity) // used again in the last fun
    }

    override suspend fun getMovieById(id: Long): MovieDto { // Flow and list deleted
        return movieDao.getMovieById(id).toDto()
    }

    override suspend fun getAllMovies(): Flow<List<MovieDto>> {
        return movieDao.getAllMovies().map {
            it.map { it.toDto() }
        }
    }

    override suspend fun deleteMovieById(id: Long) {
        return movieDao.deleteMovieById(id)
    }

    override suspend fun deleteAllMovies() {
        return movieDao.deleteAll()
    }

    override suspend fun updateMovie(newMovie: MovieDto) {
        val movieEntity = newMovie.toEntity()
        return movieDao.upsertMovie(movieEntity)
    }
}