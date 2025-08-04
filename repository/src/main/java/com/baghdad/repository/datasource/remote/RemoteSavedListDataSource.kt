package com.baghdad.repository.datasource.remote

import com.baghdad.repository.model.PagedResultDto
import com.baghdad.repository.model.SavedListDto

interface RemoteSavedListDataSource {
    suspend fun createSavedList(title: String, sessionId: String)
    suspend fun getSavedLists(
        page: Int,
        pageSize: Int,
        accountId: Long,
        sessionId: String,
    ): PagedResultDto<SavedListDto>

    suspend fun addMovieToSavedList(listId: Long, movieId: Long, sessionId: String)
    suspend fun addTvShowToSavedList(listId: Long, tvShowId: Long, sessionId: String)
}
