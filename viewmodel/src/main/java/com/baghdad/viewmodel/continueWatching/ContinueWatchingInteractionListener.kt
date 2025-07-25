package com.baghdad.viewmodel.continueWatching

interface ContinueWatchingInteractionListener {
    fun onBackClick()
    fun onMediaClick(mediaId: Long, contentType : ContinueWatchingState.ContinueWatchingMovieUiState.ContentType)

    fun onGenreClick(genreId: Long?)

    fun onMovieSaveClick(movieId: Long) {

    }
}