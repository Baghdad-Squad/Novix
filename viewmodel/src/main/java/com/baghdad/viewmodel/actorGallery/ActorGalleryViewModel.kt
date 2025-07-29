package com.baghdad.viewmodel.actorGallery

import com.baghdad.domain.usecase.actor.GetActorGalleryUseCase
import com.baghdad.viewmodel.base.BaseViewModel
import com.baghdad.viewmodel.errorStates.BaseSnackBarMessage
import kotlinx.coroutines.CoroutineDispatcher

class ActorGalleryViewModel(
    private val getGalleryImagesUseCase: GetActorGalleryUseCase,
    private val actorId: Long,
    private val defaultDispatcher: CoroutineDispatcher,

) : BaseViewModel<ActorGalleryScreenState, ActorGalleryScreenEffect>(ActorGalleryScreenState()),
    ActorGalleryInteractionListener {

    init {
        getActorGalleryImages(actorId)
    }

    override fun mapThrowableToErrorMessage(throwable: Throwable): BaseSnackBarMessage {
        return BaseSnackBarMessage.UnknownError
    }

    fun getActorGalleryImages(actorId: Long) {

        tryToExecute(
            callee = { getGalleryImagesUseCase.invoke(actorId) },
            dispatcher = defaultDispatcher,
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