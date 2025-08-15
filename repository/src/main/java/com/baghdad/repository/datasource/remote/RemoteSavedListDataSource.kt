package com.baghdad.repository.datasource.remote

import com.baghdad.repository.model.PagedResultDto
import com.baghdad.repository.model.SavedListDto
import com.baghdad.repository.model.savedList.SavedListDetailsDto

interface RemoteSavedListDataSource {
    suspend fun createSavedList(title: String)
    suspend fun getSavedLists(
        page: Int,
        pageSize: Int,
        accountId: Long,
    ): PagedResultDto<SavedListDto>

    suspend fun addMovieToSavedList(listId: Long, movieId: Long)
    suspend fun removeMovieFromSavedList(listId: Long, movieId: Long)
    suspend fun getSavedListDetails(
        listId: Long,
        page: Int,
        pageSize: Int
    ): SavedListDetailsDto

    suspend fun deleteSavedListById(listId: Long)
}