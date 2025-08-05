package com.baghdad.viewmodel.savedListDetails

import com.baghdad.domain.model.savedList.SavedListItem
import com.baghdad.entity.savedList.SavedList

fun SavedListItem.toUIState() = SavedListDetailsScreenState.SavedListDetailsMovieUiState(
    id = this.id,
    name= this.title,
    posterUrl = this.posterUrl,
    contentType = SavedListDetailsScreenState.SavedListDetailsMovieUiState.ContentType.valueOf(this.type.name)
)
fun SavedList.toUIState() = SavedListDetailsScreenState.SavedListUiState(
    id = this.id,
    name = this.name,
    itemCount = this.itemCount
)