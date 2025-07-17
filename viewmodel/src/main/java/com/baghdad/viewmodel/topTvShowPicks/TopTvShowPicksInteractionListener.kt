package com.baghdad.viewmodel.topTvShowPicks

interface TopTvShowPicksInteractionListener {
    fun onMovieDetailsClicked(tvShowId: Long)
    fun onSaveTvShowClicked(tvShowId: Long)
    fun onBackClicked()
}