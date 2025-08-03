package com.baghdad.repository

import com.baghdad.domain.repository.SavedListRepository
import com.baghdad.repository.datasource.local.LocalSessionDataStore
import com.baghdad.repository.datasource.remote.RemoteSavedListDataSource
import com.baghdad.repository.util.executeAuthorizedSafely

class SavedListRepositoryImpl(
    private val remoteSavedListSource: RemoteSavedListDataSource,
    private val localSessionDataSource: LocalSessionDataStore
) : SavedListRepository {


    override suspend fun addMovieToSavedList(listId: Long, movieId: Long) {
        val sessionId = localSessionDataSource.getSessionId().toString()
        executeAuthorizedSafely(sessionId) {

            remoteSavedListSource.addMovieToSavedList(listId, movieId, sessionId)
        }
    }

    override suspend fun addTvShowToSavedList(listId: Long, tvShowId: Long) {
        val sessionId = localSessionDataSource.getSessionId().toString()
        executeAuthorizedSafely(sessionId) {
            remoteSavedListSource.addTvShowToSavedList(listId, tvShowId, sessionId)
        }
    }
}