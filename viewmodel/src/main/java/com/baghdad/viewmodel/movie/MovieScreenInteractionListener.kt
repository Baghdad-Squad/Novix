package com.baghdad.viewmodel.movie

interface MovieScreenInteractionListener {
    fun onBackClick()
    fun onMovieClick(movieId: Long)
    fun onToggleSaveMovie(movieId: Long)
    fun onCategoryClick(categoryId: Long)
}