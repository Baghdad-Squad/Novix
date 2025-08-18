package com.baghdad.viewmodel.trendingTvShow

interface TrendingTvShowInteractionListener {
    fun onTvShowClicked(tvShowId: Long)
    fun onBackIconClicked()
    fun onGenreClicked(genreId: Long?)

    fun onSnackBarActionLabelClicked()
}