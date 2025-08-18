package com.baghdad.localDatasource

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import com.baghdad.localDatasource.errorHandler.executeFlowWithErrorHandling
import com.baghdad.localDatasource.errorHandler.executeWithErrorHandling
import com.baghdad.localDatasource.roomDB.dao.SavedListMovieDao
import com.baghdad.localDatasource.roomDB.entity.SavedListMovie
import com.baghdad.repository.datasource.local.SavableMovieDataSource
import com.baghdad.repository.logger.Logger
import com.baghdad.repository.model.savedList.SavableMovieDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

private typealias ListId = Long
private typealias MovieId = Long

@Singleton
class SavableMovieDataSourceImpl
@Inject
constructor(
    private val savedListMovieDao: SavedListMovieDao,
    @Named("preferences") private val dataStore: DataStore<Preferences>,
    private val logger: Logger,
) : SavableMovieDataSource {
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

    override fun getSavedMoviesCount(): Flow<Int> {
        return executeFlowWithErrorHandling(logger = logger) {
            savedListMovieDao.getSavedMoviesCount()
        }
    }

    override fun getSavedListCount(): Flow<Int> {
        return executeFlowWithErrorHandling(logger = logger) {
            dataStore.data.map { preferences ->
                preferences[SAVED_LIST_COUNT] ?: 0
            }
        }
    }

    override suspend fun addSavedListCount(count: Int) {
        return executeWithErrorHandling(logger = logger) {
            dataStore.edit { preferences ->
                preferences[SAVED_LIST_COUNT] = count
            }
        }
    }

    override suspend fun deleteSavedMovie(movieId: Long) {
        executeWithErrorHandling(logger = logger) {
            savedListMovieDao.deleteByMovieId(movieId = movieId)
            savedMovies.remove(movieId)
        }
    }

    override suspend fun deleteListMovies(listId: Long) {
        executeWithErrorHandling(logger = logger) {
            savedListMovieDao.deleteAllByListId(listId = listId)
            savedMovies.entries.removeIf { it.value == listId }
        }
    }

    override suspend fun deleteAllSavedMovies() {
        executeWithErrorHandling(logger = logger) {
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
    }

    private companion object {
        val SAVED_LIST_COUNT = intPreferencesKey("saved_list_count")
    }

}
