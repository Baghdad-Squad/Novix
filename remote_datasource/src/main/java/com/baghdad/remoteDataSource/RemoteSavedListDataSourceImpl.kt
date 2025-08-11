package com.baghdad.remoteDataSource

import com.baghdad.remoteDataSource.apiService.SavedListApiService
import com.baghdad.remoteDataSource.mapper.savedList.toSavedListDetailsDto
import com.baghdad.remoteDataSource.mapper.toPagedSavedListsDtos
import com.baghdad.remoteDataSource.request.AddListItemRequest
import com.baghdad.remoteDataSource.request.CreateListRequest
import com.baghdad.remoteDataSource.request.RemoveListItemRequest
import com.baghdad.remoteDataSource.response.savedList.UserListsResponse
import com.baghdad.remoteDataSource.response.savedList.CreateSavedListResponse
import com.baghdad.remoteDataSource.response.savedList.ListDetailsResponse
import com.baghdad.remoteDataSource.util.handleRequest
import com.baghdad.repository.datasource.remote.RemoteSavedListDataSource
import com.baghdad.repository.exception.ItemCreationFailedException
import com.baghdad.repository.logger.Logger
import com.baghdad.repository.model.PagedResultDto
import com.baghdad.repository.model.SavedListDto
import com.baghdad.repository.model.savedList.SavedListDetailsDto
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RemoteSavedListDataSourceImpl @Inject constructor(
    private val savedListApiService: SavedListApiService,
    private val logger: Logger,
) : RemoteSavedListDataSource {

    override suspend fun createSavedList(title: String, sessionId: String) {
        handleRequest<CreateSavedListResponse>(
            apiCall = {
                savedListApiService.createSavedList(
                    body = CreateListRequest(name = title),
                )
            },
            logger = logger,
        ).statusMessage
    }

    override suspend fun getSavedLists(
        page: Int,
        pageSize: Int,
        accountId: Long,
        sessionId: String,
    ): PagedResultDto<SavedListDto> {
      return handleRequest<UserListsResponse>(
            apiCall = {
                savedListApiService.getSavedLists(
                    page = page,
                    accountId = accountId,
                )
            },
            logger = logger,
        ).toPagedSavedListsDtos()
    }

    override suspend fun addMovieToSavedList(listId: Long, movieId: Long, sessionId: String) {
        val body = AddListItemRequest(mediaId = movieId)
        handleRequest(
            apiCall = { savedListApiService.addMovieToSavedList(listId, body) },
            logger = logger
        )
    }

    override suspend fun removeMovieFromSavedList(
        listId: Long,
        movieId: Long,
        sessionId: String
    ) {
        val body = RemoveListItemRequest(mediaId = movieId)
        handleRequest(
            apiCall = { savedListApiService.removeMovieFromSavedList(listId, body) },
            logger = logger
        )
    }

    override suspend fun getSavedListDetails(
        listId: Long,
        page: Int,
        pageSize: Int,
    ): SavedListDetailsDto {
        return handleRequest<ListDetailsResponse>(
            apiCall = { savedListApiService.getListDetails(listId, page) },
            logger = logger,
        ).toSavedListDetailsDto()
    }

    override suspend fun deleteSavedListById(listId: Long, sessionId: String) {
        handleRequest(
            apiCall = { savedListApiService.deleteSavedListById(listId) },
            logger = logger,
        )
    }
}
