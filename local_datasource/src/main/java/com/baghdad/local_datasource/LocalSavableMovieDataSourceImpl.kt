package com.baghdad.local_datasource

import com.baghdad.local_datasource.roomDB.dao.SavedListMovieDao
import com.baghdad.local_datasource.roomDB.entity.SavedListMovie
import com.baghdad.local_datasource.roomDB.errorHandler.executeWithErrorHandling
import com.baghdad.repository.datasource.local.LocalSavableMovieDataSource
import com.baghdad.repository.logger.Logger
import com.baghdad.repository.model.savedList.SavableMovieDto
import javax.inject.Inject
import javax.inject.Singleton

private typealias ListId = Long
private typealias MovieId = Long

@Singleton
class LocalSavableMovieDataSourceImpl
    @Inject
    constructor(
        private val savedListMovieDao: SavedListMovieDao,
        private val logger: Logger,
    ) : LocalSavableMovieDataSource {
        private val savedMovies = mutableMapOf<MovieId, ListId>()

        override suspend fun saveMovies(
            listId: Long,
            movies: List<SavableMovieDto>,
        ) {
            executeWithErrorHandling(logger = logger) {
                val savedMovies =
                    movies.map {
                        SavedListMovie(
                            movieId = it.movie.id,
                            listId = listId,
                        )
                    }
                savedListMovieDao.upsertAll(savedMovies)
                this.savedMovies.putAll(savedMovies.associate { it.movieId to it.listId })
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
                savedMovies[movieId] = listId
            }
        }

        override suspend fun deleteSavedMovie(movieId: Long) {
            executeWithErrorHandling(logger) {
                savedListMovieDao.deleteByMovieId(movieId = movieId)
                savedMovies.remove(movieId)
            }
        }

        override suspend fun deleteListMovies(listId: Long) {
            executeWithErrorHandling(logger) {
                savedListMovieDao.deleteAllByListId(listId = listId)
                savedMovies.entries.removeIf { it.value == listId }
            }
        }

        override suspend fun deleteAllSavedMovies() {
            executeWithErrorHandling(logger) {
                savedListMovieDao.deleteAllSavedMovies()
                savedMovies.clear()
            }
        }

        override suspend fun getSavedMovies(): Map<Long, Long> {
            return executeWithErrorHandling(logger) {
                if (savedMovies.isEmpty()) {
                    val movies = savedListMovieDao.getSavedMovies()
                    savedMovies.clear()
                    savedMovies.putAll(movies.associate { it.movieId to it.listId })
                }
                savedMovies
        }
}}
