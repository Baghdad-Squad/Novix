package com.baghdad.viewmodel.topMoviePicks

interface TopMoviePicksInteractionListener {
    fun onMovieDetailsClicked(movieId: Long)
    fun onSaveMovieClicked(movieId: Long)
    fun onBackClicked()
}