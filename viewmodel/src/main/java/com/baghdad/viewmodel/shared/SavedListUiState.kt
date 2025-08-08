package com.baghdad.viewmodel.shared

import com.baghdad.entity.savedList.SavedList

data class SavedListUiState(
    val id: Long,
    val name: String,
    val itemsCount: Int = 0,
)

fun SavedList.toUiState() =
    SavedListUiState(
        id = id,
        name = name,
        itemsCount = itemCount,
    )
