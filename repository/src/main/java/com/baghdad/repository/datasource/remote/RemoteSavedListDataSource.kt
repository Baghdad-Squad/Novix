package com.baghdad.repository.datasource.remote

interface RemoteSavedListDataSource {
    suspend fun addMovieToSavedList(listId: Long, movieId: Long, sessionId: String)
    suspend fun addTvShowToSavedList(listId: Long, tvShowId: Long, sessionId: String)
}
