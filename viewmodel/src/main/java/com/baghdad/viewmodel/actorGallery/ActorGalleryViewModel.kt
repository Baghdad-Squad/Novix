package com.baghdad.viewmodel.actorGallery

import com.baghdad.domain.exception.NoInternetException
import com.baghdad.domain.usecase.actor.GetActorGalleryUseCase
import com.baghdad.viewmodel.R
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
        loadData()
    }

    fun loadData() {
        getActorGalleryImages(actorId)
    }

    override fun mapThrowableToErrorMessage(throwable: Throwable): BaseSnackBarMessage =
        BaseSnackBarMessage.UnknownError

    fun getActorGalleryImages(actorId: Long) {
        tryToExecute(
            callee = { getGalleryImagesUseCase.invoke(actorId) },
            dispatcher = defaultDispatcher,
            onStart = ::onStart,
            onSuccess = ::onGalleryActorSuccess,
            onError = ::onGetActorGalleryError,
            onFinally = ::onFinally,
        )
    }

    private fun onGetActorGalleryError(throwable: Throwable) {
        when (throwable) {
            is NoInternetException -> showNoInternetSnackBar()
            else -> handleError(throwable)
        }
    }


    private fun showNoInternetSnackBar() {
        showSnackBar(
            message = BaseSnackBarMessage.NetworkError,
            actionLabelRes = R.string.retry,
            isSuccess = false,
            durationMillis = Int.MAX_VALUE.toLong(),
        )
    }

    private fun onGalleryActorSuccess(images: List<String>) {
        hideSnackBar()
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

    override fun onSnackBarActionLabelClick() {
        loadData()
    }
}
