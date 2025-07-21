package com.baghdad.viewmodel.continueWatching

interface ContinueWatchingInteractionListener {
    fun onBackClick()
    fun onMovieClick(movieId: Long)
    fun onTvShowClick(tvShowId: Long)
    fun onGenreClick(genreId: Long)
    fun onMovieSaveClick(movieId: Long) {

    }
}