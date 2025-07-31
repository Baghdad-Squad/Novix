package com.baghdad.viewmodel.actorGallery

import androidx.lifecycle.SavedStateHandle
import com.baghdad.domain.usecase.actor.GetActorGalleryUseCase
import com.baghdad.viewmodel.base.BaseViewModel
import com.baghdad.viewmodel.errorStates.BaseSnackBarMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ActorGalleryViewModel @Inject constructor(
    private val getGalleryImagesUseCase: GetActorGalleryUseCase,
    savedStateHandle: SavedStateHandle,
) : BaseViewModel<ActorGalleryScreenState, ActorGalleryScreenEffect>(ActorGalleryScreenState()),
    ActorGalleryInteractionListener {

    private val actorId: Long = checkNotNull(savedStateHandle["actorId"])

    init {
        getActorGalleryImages(actorId)
    }

    override fun mapThrowableToErrorMessage(throwable: Throwable): BaseSnackBarMessage {
        return BaseSnackBarMessage.UnknownError
    }

    fun getActorGalleryImages(actorId: Long) {

        tryToExecute(
            callee = { getGalleryImagesUseCase.invoke(actorId) },
            onStart = ::onStart,
            onSuccess = ::onGalleryActorSuccess,
            onFinally = ::onFinally
        )

    }

    private fun onGalleryActorSuccess(images: List<String>) {

        updateState { it.copy(images = images) }
    }

    private fun onStart() {
        updateState { it.copy(isLoading = true) }
    }

    private fun onFinally() {
        updateState { it.copy(isLoading = false) }
    }

    override fun onBackClick() {
        sendEffect(ActorGalleryScreenEffect.OnBackClick)

    }
}