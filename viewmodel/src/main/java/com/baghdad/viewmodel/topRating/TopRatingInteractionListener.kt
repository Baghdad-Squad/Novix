package com.baghdad.viewmodel.topRating

interface TopRatingInteractionListener {
    fun onMovieDetailsClick(movieId: Long)

    fun onGenreClick(genreId: Long?)

    fun onSaveMovieClick(movieId: Long)
    fun onBackClick()
    fun onSelectedTab(selectedTab: TopRatingTab)
}