package com.baghdad.repository.datasource.remote

import com.baghdad.repository.model.PagedResultDto
import com.baghdad.repository.model.SavedListDto

interface RemoteSavedListDataSource {
    suspend fun getSavedLists(
        page: Int,
        sessionId: String,
        ): PagedResultDto<SavedListDto>
}
