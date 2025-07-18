package com.baghdad.viewmodel.movieDetails

interface MovieDetailsInteractionListener {

    fun onStarMovieClick()

    fun onSaveCurrentMovieClick()

    fun onSaveMoreLikeThisMedia(id: Long)

    fun onExtendOverviewClick()

    fun onCategoryClick(id: Long)

    fun onActorClick(id: Long)

    fun onReviewClick(id: Long)

    fun onNavigateBack()



}