package com.baghdad.domain.repository

import com.baghdad.domain.model.PagedResult
import com.baghdad.domain.model.savedList.SavedListDetails
import com.baghdad.entity.savedList.SavedList

interface SavedListRepository {
    suspend fun createSavedList(title: String)
    suspend fun getSavedLists(
        page: Int,
        pageSize: Int,
    ): PagedResult<SavedList>
    suspend fun addMovieToSavedList(listId: Long, movieId: Long)
    suspend fun getSavedListDetails(
        listId: Long,
        page: Int,
        pageSize: Int): SavedListDetails
    suspend fun removeMovieFromSavedList(listId: Long, movieId: Long)

    suspend fun deleteSavedListById(listId: Long)
}
