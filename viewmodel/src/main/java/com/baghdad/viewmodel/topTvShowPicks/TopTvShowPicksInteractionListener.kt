package com.baghdad.viewmodel.topTvShowPicks

interface TopTvShowPicksInteractionListener {
    fun onMovieDetailsClick(tvShowId: Long)
    fun onSaveTvShowClick(tvShowId: Long)
    fun onBackClick()
}