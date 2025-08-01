package com.baghdad.viewmodel.topTvShowPicks

interface TopTvShowPicksInteractionListener {
    fun onTvShowDetailsClick(tvShowId: Long)
    fun onSaveTvShowClick(tvShowId: Long)
    fun onBackClick()
    fun onSnackBarActionLabelClick()
}