package com.baghdad.domain.model.savedList

import com.baghdad.domain.model.pagination.PagedResult
import com.baghdad.entity.savedList.SavedList

data class SavedListDetails(
    val savedList: SavedList,
    val pagedItems: PagedResult<SavedMovie>
)
