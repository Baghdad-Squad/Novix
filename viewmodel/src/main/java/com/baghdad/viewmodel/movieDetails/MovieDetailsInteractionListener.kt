package com.baghdad.viewmodel.movieDetails

interface MovieDetailsInteractionListener {
    fun onSaveCurrentMovieClick()
    fun onSaveMoreLikeThisMedia(id: Long)
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

    fun onSaveMovieClick(listId: Long, itemId: Long, isSaved: Boolean)
    fun onSaveItemToListClicked()

    //    fun onSnackBarActionLabelClick()
    fun onCreateNewListClicked()
    fun onLoginClicked()
    fun onSaveToListBottomSheetDismiss()
    fun onListSelected(listId: Long)
    fun onCreatedListNameChanged(name: String)
    fun onCreateListBottomSheetDismiss()
    fun onCreateListBottomSheetAddClick()
}