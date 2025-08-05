package com.baghdad.viewmodel.savedListDetails

import com.baghdad.viewmodel.savedListDetails.SavedListDetailsScreenState.SavedListDetailsMovieUiState

interface SavedListDetailsInteractionListener {
    fun onBackClick()
    fun onDeleteClick(listId: Long)
    fun onCategoryClick(category: SavedListTab)
    fun onMediaClick(mediaId: Long, contentType: SavedListDetailsMovieUiState.ContentType)
    fun onRemoveSavedMediaClick(mediaId: Long)
    fun onSnackBarActionLabelClick()
}