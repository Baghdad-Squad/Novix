package com.baghdad.domain.model.savedList

import com.baghdad.entity.savedList.SavedList

data class SavedListDetails(
    val savedList: SavedList,
    val listItems: List<SavedListItem>,
)
