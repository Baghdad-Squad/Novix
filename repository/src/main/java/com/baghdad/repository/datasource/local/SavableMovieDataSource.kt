package com.baghdad.repository.datasource.local

import com.baghdad.repository.model.savedList.SavableMovieDto
import kotlinx.coroutines.flow.Flow

interface SavableMovieDataSource {
    suspend fun deleteSavedMovie(movieId: Long)

    suspend fun deleteListMovies(listId: Long)

    suspend fun deleteAllSavedMovies()

    suspend fun getSavedMovies(): Map<Long, Long>

    suspend fun saveMovies(
        listId: Long,
        movies: List<SavableMovieDto>
    )

    suspend fun addSavedMovie(
        listId: Long,
        movieId: Long
    )
    fun getSavedListCount(): Flow<Int>
}