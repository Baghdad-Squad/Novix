package com.baghdad.remoteDataSource

import com.baghdad.remoteDataSource.apiService.SavedListApiService
import com.baghdad.remoteDataSource.request.AddItemRequest
import com.baghdad.remoteDataSource.util.handleRequest
import com.baghdad.repository.datasource.remote.RemoteSavedListDataSource
import com.baghdad.repository.logger.Logger

class RemoteSavedListDataSourceImpl(
    private val savedListApiService: SavedListApiService,
    private val logger: Logger,
) : RemoteSavedListDataSource {

    override suspend fun addMovieToSavedList(listId: Long, movieId: Long, sessionId: String) {
        val body = AddItemRequest(mediaId = movieId)

        handleRequest(
            apiCall = { savedListApiService.addItemToSavedList(listId, body, sessionId) },
            logger = logger
        )
    }

    override suspend fun addTvShowToSavedList(listId: Long, tvShowId: Long, sessionId: String) {
        val body = AddItemRequest(mediaId = tvShowId)

        handleRequest(
            apiCall = { savedListApiService.addItemToSavedList(listId, body, sessionId) },
            logger = logger
        )
    }
}
