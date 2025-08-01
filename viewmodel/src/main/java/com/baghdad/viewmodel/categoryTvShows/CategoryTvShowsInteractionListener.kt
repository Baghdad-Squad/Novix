package com.baghdad.viewmodel.categoryTvShows

interface CategoryTvShowsInteractionListener {
    fun onBackClicked()
    fun onSavedClick(tvShowId: Long)
    fun onTvShowClicked(tvShowId: Long)
    fun onSnackBarActionLabelClick()
}