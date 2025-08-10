package com.baghdad.domain.model.savedList

import com.baghdad.domain.model.PagedResult
import com.baghdad.entity.savedList.SavedList

data class SavedListDetails(
    val savedList: SavedList,
    val pagedItems: PagedResult<SavableMovie>,
)
