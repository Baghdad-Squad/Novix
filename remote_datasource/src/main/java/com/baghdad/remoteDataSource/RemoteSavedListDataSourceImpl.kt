package com.baghdad.remoteDataSource

import com.baghdad.remoteDataSource.apiService.SavedListApiService
import com.baghdad.remoteDataSource.response.savedList.CreateSavedListResponse
import com.baghdad.remoteDataSource.util.handleRequest
import com.baghdad.repository.datasource.remote.RemoteSavedListDataSource
import com.baghdad.repository.logger.Logger

class RemoteSavedListDataSourceImpl(
    private val savedListApiService: SavedListApiService,
    private val logger: Logger,
) : RemoteSavedListDataSource {
    override suspend fun createSavedList(title: String) {
        val result = handleRequest<CreateSavedListResponse>(
            apiCall = { savedListApiService.createSavedList(title) },
            logger = logger,
        )
        if (result.success == true) {
            logger.logException(
                Exception("List created successfully with ID: ${result.listId}")
            )
        } else {
            val message = result.statusMessage ?: "List creation failed"
            logger.logException(Exception("Failed to create list: $message"))
        }
    }
}

