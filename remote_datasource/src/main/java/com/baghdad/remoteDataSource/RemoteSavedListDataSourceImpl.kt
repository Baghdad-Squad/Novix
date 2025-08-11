package com.baghdad.remoteDataSource

import com.baghdad.remoteDataSource.apiService.SavedListApiService
import com.baghdad.remoteDataSource.mapper.savedList.toSavedListDetailsDto
import com.baghdad.remoteDataSource.mapper.toPagedSavedListsDtos
import com.baghdad.remoteDataSource.request.AddListItemRequest
import com.baghdad.remoteDataSource.request.CreateListRequest
import com.baghdad.remoteDataSource.request.RemoveListItemRequest
import com.baghdad.remoteDataSource.response.UserListsResponse
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
    override suspend fun createSavedList(title: String) {
        val result = handleRequest<CreateSavedListResponse>(
            apiCall = {
                savedListApiService.createSavedList(
                    body = CreateListRequest(name = title),
                )
            },
            logger = logger,
        )

        if (result.success != true) {
            val message = result.statusMessage ?: "List creation failed"
            throw ItemCreationFailedException(message)
        }
    }

    override suspend fun getSavedLists(
        page: Int,
        pageSize: Int,
        accountId: Long,

    ): PagedResultDto<SavedListDto> {
        val listsResponse = handleRequest<UserListsResponse>(
            apiCall = {
                savedListApiService.getSavedLists(
                    page = page,
                    accountId = accountId,
                    sessionId = "",

                )
            },
            logger = logger,
        )
        return listsResponse.toPagedSavedListsDtos()
    }

    override suspend fun addMovieToSavedList(listId: Long, movieId: Long) {
        val body = AddListItemRequest(mediaId = movieId)

        handleRequest(
            apiCall = { savedListApiService.addMovieToSavedList(listId, body,"") },
            logger = logger
        )
    }

    override suspend fun removeMovieFromSavedList(
        listId: Long,
        movieId: Long,
    ) {
        val body = RemoveListItemRequest(mediaId = movieId)
        handleRequest(
            apiCall = { savedListApiService.removeMovieFromSavedList(listId, body,"") },
            logger = logger
        )
    }

    override suspend fun getSavedListDetails(
        listId: Long,
        page: Int,
        pageSize: Int,
    ): SavedListDetailsDto {
        val response = handleRequest<ListDetailsResponse>(
            apiCall = { savedListApiService.getListDetails(listId, page) },
            logger = logger,
        )
        return response.toSavedListDetailsDto()
    }


    override suspend fun deleteSavedListById(listId: Long) {
        handleRequest(
            apiCall = { savedListApiService.deleteSavedListById(listId)},
            logger = logger,
        )
    }
}
