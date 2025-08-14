package com.baghdad.viewmodel.actorGallery

interface ActorGalleryInteractionListener {
    fun onBackClick()

    fun onSnackBarActionLabelClick()

    fun onGalleryImageClick(imageUrl: String)

    fun onImageDialogDismiss()
}