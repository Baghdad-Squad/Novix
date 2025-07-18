package com.baghdad.viewmodel.movieDetails

interface MovieDetailsInteractionListener {

    fun onStarMovieClick()

    fun onSaveCurrentMovieClick()

    fun onSaveMoreLikeThisMedia(id: Long)

    fun onExtendOverviewClick()

    fun onBackClick()
}