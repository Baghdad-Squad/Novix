package com.baghdad.viewmodel.categoryMovies

interface CategoryMoviesInteractionListener {
    fun onBackClicked()
    fun onSavedClick(movieId: Long)
    fun onMovieClicked(movieId: Long)
}