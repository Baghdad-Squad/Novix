package com.baghdad.remoteDataSource

import com.baghdad.remoteDataSource.apiService.SavedListApiService
import com.baghdad.remoteDataSource.mapper.toPagedSavedListsDtos
import com.baghdad.remoteDataSource.response.UserListsResponse
import com.baghdad.remoteDataSource.util.handleRequest
import com.baghdad.repository.datasource.remote.RemoteSavedListDataSource
import com.baghdad.repository.logger.Logger
import com.baghdad.repository.model.PagedResultDto
import com.baghdad.repository.model.SavedListDto
import javax.inject.Inject

class RemoteSavedListDataSourceImpl @Inject constructor(
    private val savedListApiService: SavedListApiService,
    private val logger: Logger,
) : RemoteSavedListDataSource {
    override suspend fun getSavedLists(
        page: Int,
        pageSize: Int,
        accountId: Long,
        sessionId: String,
    ): PagedResultDto<SavedListDto> {
        val listsResponse = handleRequest<UserListsResponse>(
            apiCall = {
                savedListApiService.getSavedLists(
                    page = page,
                    accountId = accountId,
                    sessionId = sessionId,
                )
            },
            logger = logger,
        )
        return listsResponse.toPagedSavedListsDtos()
    }
}
