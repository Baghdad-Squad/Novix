package com.baghdad.viewmodel.movieDetails

import com.baghdad.viewmodel.shared.AddListBottomSheetState

interface MovieDetailsInteractionListener {
    fun onSaveCurrentMovieClick()
    fun onSaveMoreLikeThisMedia(movie: MovieDetailsState.MoreLikeThisMovie)
    fun onExtendOverviewClick()
    fun onCategoryClick(categoryId: Long)
    fun onBackClicked()
    fun onActorClick(id: Long)
    fun onReviewClick()
    fun onMovieClick(id: Long)
    fun onBackClick()
    fun onClickPlayTrailer()
    fun onSnackBarActionLabelClick()
    fun onClickStarButton()
    fun onRatingChanged(rating: Int)
    fun onClickSubmitRating(rating: Int)
    fun onDismissRatingBottomSheet()
    fun onLoginClick()
    fun onCreateNewListClicked()
    fun onSaveItemToListClicked()
    fun onSaveToListBottomSheetDismiss()
    fun onListSelected(listId: Long)
    fun onCreatedListNameChanged(name: String)
    fun onCreateListBottomSheetDismiss()
    fun onCreateListBottomSheetAddClick()
}