package com.baghdad.viewmodel.trendingMovie

interface TrendingMoviesInteractionListener {
    fun onBackClick()
    fun onMovieClick(movieId: Long)
    fun onToggleSaveMovie(movieId: Long)
    fun onCategoryClick(categoryId: Long?)
}