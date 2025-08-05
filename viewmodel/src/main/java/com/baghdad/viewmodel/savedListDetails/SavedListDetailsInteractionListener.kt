package com.baghdad.viewmodel.savedListDetails

interface SavedListDetailsInteractionListener {
    fun onBackClick()
    fun onDeleteClick(listId: Long)
    fun onCategoryClick(categoryId: Long)
    fun onTvShowClick(tvShowId: Long)
    fun onMovieClick(movieId: Long)
    fun onRemoveSavedMovieClick(movieId: Long)
    fun onRemoveSavedTvShowClick(tvShowId: Long)
    fun onSnackBarActionLabelClick()
}