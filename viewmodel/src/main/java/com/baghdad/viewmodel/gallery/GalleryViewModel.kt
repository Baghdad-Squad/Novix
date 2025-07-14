package com.baghdad.viewmodel.gallery

import com.baghdad.domain.usecase.gallery.GetGalleryImagesUseCase
import com.baghdad.viewmodel.base.BaseViewModel
import com.baghdad.viewmodel.errorStates.BaseSnackBarMessage

class GalleryViewModel(
    private val getGalleryImagesUseCase: GetGalleryImagesUseCase

): BaseViewModel<GalleryScreenState, GalleryScreenEffect> (GalleryScreenState()), GalleryInteractionListener {

    init {
        getActorGalleryImages()
    }

    override fun mapThrowableToErrorMessage(throwable: Throwable): BaseSnackBarMessage {
        return BaseSnackBarMessage.UnknownError
    }

    private fun getActorGalleryImages(){

        updateState { it.copy(isLoading = true) }

        tryToExecute(
            callee = { getGalleryImagesUseCase.getActorImages() },
            onStart = ::onStart,
            onSuccess = ::onGalleryActorSuccess,
            onFinally = ::onFinally
        )

    }


    private fun onGalleryActorSuccess(images: List<String>) {

        updateState { it.copy(images = images) }
    }

    private fun onStart() {
        updateState { it.copy(isLoading = false) }
    }

    private fun onFinally() {
        updateState { it.copy(isLoading = false) }
    }

    override fun onBackClick() {
        sendEffect(GalleryScreenEffect.OnBackClick)

    }
}