package com.baghdad.viewmodel.movieDetails

interface MovieDetailsInteractionListener {

    fun onStarMovieClick()

    fun onSaveCurrentMovieClick()

    fun onSaveMoreLikeThisMedia(id: Long)

    fun onExtendOverviewClick()

    fun onCategoryClick(categoryId: Long)

    fun onBackClicked()

    fun onActorClick(id: Long)

    fun onReviewClick(id: Long)

    fun onMovieLikeClick(id : Long)

    fun onBackClick()
}