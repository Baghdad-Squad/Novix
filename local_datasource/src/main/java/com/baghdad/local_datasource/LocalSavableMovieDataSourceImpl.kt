package com.baghdad.local_datasource

import com.baghdad.local_datasource.roomDB.dao.SavedListMovieDao
import com.baghdad.local_datasource.roomDB.entity.SavedListMovie
import com.baghdad.local_datasource.roomDB.errorHandler.executeWithErrorHandling
import com.baghdad.repository.datasource.local.LocalSavableMovieDataSource
import com.baghdad.repository.logger.Logger
import com.baghdad.repository.model.savedList.SavableMovieDto
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalSavableMovieDataSourceImpl
    @Inject
    constructor(
        private val savedListMovieDao: SavedListMovieDao,
        private val logger: Logger,
    ) : LocalSavableMovieDataSource {
        override suspend fun saveMovies(
            listId: Long,
            movies: List<SavableMovieDto>,
        ) {
            executeWithErrorHandling(logger = logger) {
                val savedMovies =
                    movies.mapNotNull {
                        SavedListMovie(
                            movieId = it.movie.id,
                            listId = listId,
                        )
                    }
                savedListMovieDao.upsertAll(savedMovies)
            }
        }

        override suspend fun addSavedMovie(
            listId: Long,
            movieId: Long,
        ) {
            executeWithErrorHandling(logger = logger) {
                savedListMovieDao.upsert(
                    SavedListMovie(
                        movieId = movieId,
                        listId = listId,
                    ),
                )
            }
        }

        override suspend fun deleteSavedMovie(movieId: Long) {
            executeWithErrorHandling(logger) {
                savedListMovieDao.deleteByMovieId(movieId = movieId)
            }
        }

        override suspend fun deleteListMovies(listId: Long) {
            executeWithErrorHandling(logger) {
                savedListMovieDao.deleteAllByListId(listId = listId)
            }
        }

        override suspend fun getSavedMovies(): Map<Long, Long> =
            executeWithErrorHandling(logger) {
                savedListMovieDao
                    .getSavedMovies()
                    .associate { it.movieId to it.listId }
        }
    }
