package com.baghdad.viewmodel.episodeDetails

interface EpisodeDetailsInteractionListener {
    fun onBackClick()
    fun onReadMoreOverviewClick()
    fun onCategoryClick(categoryId: Long)
    fun onGuestOfHonorClick(guestOfHonorId: Long)
    fun onSaveEpisodeClick()
    fun onDismissAddToListBottomSheetClick()
    fun onLoginClick()
    fun onPlayTrailerClick()
    fun onDismissRatingBottomSheet()
    fun onSnackBarActionLabelClick()
    fun onClickStarButton()
    fun onRatingChanged(rating: Int)
    fun onClickSubmitRating(rating: Int)
    fun onClickLoginButton()
}