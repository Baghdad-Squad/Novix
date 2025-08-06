package com.baghdad.viewmodel.topTvShowPicks

interface TopTvShowPicksInteractionListener {
    fun onTvShowDetailsClick(tvShowId: Long)
    fun onBackClick()
    fun onSnackBarActionLabelClick()
}