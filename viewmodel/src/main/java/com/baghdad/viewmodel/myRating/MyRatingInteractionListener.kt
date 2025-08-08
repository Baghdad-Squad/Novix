package com.baghdad.viewmodel.myRating

sealed interface MyRatingInteractionListener {
    fun onBackClick()

    fun onSnackBarActionLabelClick()
    fun onMediaClick(mediaId: Long, contentType: MyRatingState.ContentType)
    fun onMediaTabClick( mediaTab: MyRatingState.MediaTab?)
    fun onDeleteClick(mediaId: Long , contentType: MyRatingState.ContentType)
}