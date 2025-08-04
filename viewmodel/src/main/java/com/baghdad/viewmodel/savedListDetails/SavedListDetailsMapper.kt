package com.baghdad.viewmodel.savedListDetails

import com.baghdad.domain.model.savedList.SavedListItem

fun SavedListItem.toUIState() = SavedListDetailsScreenState.SavedListDetailsMovieUiState(
    id = this.id,
    name= this.title,
    posterUrl = this.posterUrl,
    contentType = SavedListDetailsScreenState.SavedListDetailsMovieUiState.ContentType.valueOf(this.type.name)
)