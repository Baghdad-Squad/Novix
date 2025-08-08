package com.baghdad.repository.datasource.local

import com.baghdad.repository.model.savedList.SavableMovieDto

interface LocalSavableMovieDataSource {
    suspend fun saveMovies(
        listId: Long,
        movies: List<SavableMovieDto>,
    )

    suspend fun addSavedMovie(
        listId: Long,
        movieId: Long,
    )

    suspend fun deleteSavedMovie(movieId: Long)

    suspend fun deleteListMovies(listId: Long)

    suspend fun deleteAllSavedMovies()

    suspend fun getSavedMovies(): Map<Long, Long>
}
