package com.baghdad.viewmodel.topRating

interface TopRatingInteractionListener {
    fun onMovieDetailsClick(movieId: Long)
    fun onTvShowDetailsClick(tvShowId: Long)
    fun onGenreClick(genreId: Long?)
    fun onSaveTvShowClick(tvShowId: Long)
    fun onSaveMovieClick(movieId: Long)
    fun onBackClick()
    fun onSelectedTab(selectedTab: TopRatingTab)
}