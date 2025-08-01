package com.baghdad.viewmodel.topMoviePicks

interface TopMoviePicksInteractionListener {
    fun onMovieDetailsClick(movieId: Long)
    fun onSaveMovieClick(movieId: Long)
    fun onBackClick()

    fun onSnackBarActionLabelClick()
}