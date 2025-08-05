package com.baghdad.viewmodel.movieDetails

interface MovieDetailsInteractionListener {
    fun onSaveCurrentMovieClick()
    fun onSaveMoreLikeThisMedia(id: Long)
    fun onExtendOverviewClick()
    fun onCategoryClick(categoryId: Long)
    fun onBackClicked()
    fun onActorClick(id: Long)
    fun onReviewClick(id: Long)
    fun onMovieClick(id: Long)
    fun onBackClick()
    fun onClickPlayTrailer()
    fun onSnackBarActionLabelClick()
    fun onClickStarButton()
    fun onRatingChanged(rating: Int)
    fun onClickSubmitRating(rating: Int)
    fun onDismissRatingBottomSheet()
    fun onLoginClick()
}