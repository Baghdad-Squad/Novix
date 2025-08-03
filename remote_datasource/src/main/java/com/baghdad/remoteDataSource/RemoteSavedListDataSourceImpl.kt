package com.baghdad.remoteDataSource

import com.baghdad.remoteDataSource.apiService.SavedListApiService
import com.baghdad.remoteDataSource.request.AddItemsRequest
import com.baghdad.remoteDataSource.request.MediaItem
import com.baghdad.remoteDataSource.util.handleRequest
import com.baghdad.repository.datasource.remote.RemoteSavedListDataSource
import com.baghdad.repository.logger.Logger

class RemoteSavedListDataSourceImpl(
    private val savedListApiService: SavedListApiService,
    private val logger: Logger,
) : RemoteSavedListDataSource {

    override suspend fun addMovieToSavedList(listId: Long, movieId: Long, sessionId: String) {
        val body = AddItemsRequest(
            items = listOf(
                MediaItem(mediaType = "movie", mediaId = movieId)
            )
        )

        return handleRequest(
            apiCall = { savedListApiService.addItemToSavedList(listId, body, sessionId) },
            logger = logger
        )
    }

    override suspend fun addTvShowToSavedList(listId: Long, tvShowId: Long, sessionId: String) {
        val body = AddItemsRequest(
            items = listOf(
                MediaItem(
                    mediaType = "tv",
                    mediaId = tvShowId
                )
            )
        )

        return handleRequest(
            apiCall = { savedListApiService.addItemToSavedList(listId, body, sessionId) },
            logger = logger
        )
    }
}
