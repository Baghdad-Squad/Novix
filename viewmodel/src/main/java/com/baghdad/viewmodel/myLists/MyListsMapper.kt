package com.baghdad.viewmodel.myLists

import com.baghdad.entity.savedList.SavedList

fun SavedList.toUiState() = MyListsScreenState.SavedListUiState(
    id = this.id,
    name = this.name,
    itemCount = this.itemCount
)