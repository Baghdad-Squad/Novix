package com.baghdad.remoteDataSource

import com.baghdad.remoteDataSource.apiService.SavedListApiService
import com.baghdad.remoteDataSource.request.CreateListRequest
import com.baghdad.remoteDataSource.response.savedList.CreateSavedListResponse
import com.baghdad.remoteDataSource.util.handleRequest
import com.baghdad.repository.datasource.remote.RemoteSavedListDataSource
import com.baghdad.repository.exception.ItemCreationFailedException
import com.baghdad.repository.logger.Logger

class RemoteSavedListDataSourceImpl(
    private val savedListApiService: SavedListApiService,
    private val logger: Logger,
) : RemoteSavedListDataSource {
    override suspend fun createSavedList(title: String, sessionId: String) {
        val result = handleRequest<CreateSavedListResponse>(
            apiCall = {
                savedListApiService.createSavedList(
                    body = CreateListRequest(name = title)
                )
            },
            logger = logger,
        )

        if (result.success != true) {
            val message = result.statusMessage ?: "List creation failed"
            throw ItemCreationFailedException(message)
        }
    }
}

