package com.baghdad.remoteDataSource

import com.baghdad.remoteDataSource.apiService.SavedListApiService
import com.baghdad.remoteDataSource.mapper.savedList.toSavedListDetailsDto
import com.baghdad.remoteDataSource.response.savedList.ListDetailsResponse
import com.baghdad.remoteDataSource.util.handleRequest
import com.baghdad.repository.datasource.remote.RemoteSavedListDataSource
import com.baghdad.repository.logger.Logger
import com.baghdad.repository.model.savedList.SavedListDetailsDto

class RemoteSavedListDataSourceImpl(
    private val savedListApiService: SavedListApiService,
    private val logger: Logger,
) : RemoteSavedListDataSource {
    override suspend fun getSavedListDetails(listId: Long): SavedListDetailsDto =
        handleRequest<ListDetailsResponse>(
            apiCall = { savedListApiService.getListDetails(listId) },
            logger = logger,
        ).toSavedListDetailsDto()
}
