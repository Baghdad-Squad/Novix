package com.baghdad.viewmodel.trendingTvShow

interface TrendingTvShowInteractionListener {
    fun onTvShowClick(tvShowId: Long)
    fun onBackIconClick()
    fun onSaveTvShowClick(tvShowId: Long)
    fun onGenreClick(genreId: Long?)
    fun onSnackBarActionLabelClick()
}