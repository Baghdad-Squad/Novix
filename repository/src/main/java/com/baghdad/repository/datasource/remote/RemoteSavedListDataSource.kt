package com.baghdad.repository.datasource.remote

import com.baghdad.repository.model.PagedResultDto
import com.baghdad.repository.model.SavedListDto
import com.baghdad.repository.model.savedList.SavedListDetailsDto

interface RemoteSavedListDataSource {
    suspend fun createSavedList(title: String, sessionId: String)

    suspend fun deleteSavedListById(listId: Long, sessionId: String)

    suspend fun addMovieToSavedList(
        listId: Long,
        movieId: Long,
        sessionId: String
    )

    suspend fun removeMovieFromSavedList(
        listId: Long,
        movieId: Long,
        sessionId: String
    )

    suspend fun getSavedLists(
        page: Int,
        pageSize: Int,
        accountId: Long,
        sessionId: String,
    ): PagedResultDto<SavedListDto>

    suspend fun getSavedListDetails(
        listId: Long,
        page: Int,
        pageSize: Int
    ): SavedListDetailsDto
}