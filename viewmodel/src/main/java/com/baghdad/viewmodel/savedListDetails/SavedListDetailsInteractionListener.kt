package com.baghdad.viewmodel.savedListDetails

interface SavedListDetailsInteractionListener {
    fun onBackClick()
    fun onDeleteClick(listId: Long)
    fun onMovieClick(movieId: Long)
    fun onRemoveSavedMovieClick(movieId: Long)
    fun onSnackBarActionLabelClick()
}