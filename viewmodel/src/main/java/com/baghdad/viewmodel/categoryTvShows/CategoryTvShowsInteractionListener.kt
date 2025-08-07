package com.baghdad.viewmodel.categoryTvShows

interface CategoryTvShowsInteractionListener {
    fun onBackClicked()
    fun onTvShowClicked(tvShowId: Long)
    fun onSnackBarActionLabelClick()
}