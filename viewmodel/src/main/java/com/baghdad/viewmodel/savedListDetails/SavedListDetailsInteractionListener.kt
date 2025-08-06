package com.baghdad.viewmodel.savedListDetails

interface SavedListDetailsInteractionListener {
    fun onBackClick()
    fun onDeleteClick()
    fun onMovieClick(movieId: Long)
    fun onRemoveSavedMovieClick(movieId: Long)
    fun onSnackBarActionLabelClick()
    fun onDeleteListBottomSheetDismiss()
    fun onDeleteListBottomSheetDeleteClick()
}