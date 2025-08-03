package com.baghdad.domain.repository

import com.baghdad.domain.model.PagedResult
import com.baghdad.entity.savedList.SavedList

interface SavedListRepository{
    suspend fun getSavedLists(
        page: Int,
        pageSizes: Int,
    ): PagedResult<SavedList>
}
