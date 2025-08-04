package com.baghdad.domain.repository

interface SavedListRepository {
    suspend fun addMovieToSavedList(listId: Long, movieId: Long)
    suspend fun addTvShowToSavedList(listId: Long, tvShowId: Long)
}
