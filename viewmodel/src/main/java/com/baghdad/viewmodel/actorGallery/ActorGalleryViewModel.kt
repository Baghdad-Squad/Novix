package com.baghdad.viewmodel.actorGallery

import androidx.lifecycle.SavedStateHandle
import com.baghdad.domain.exception.NoInternetException
import com.baghdad.domain.usecase.actor.GetActorGalleryUseCase
import com.baghdad.viewmodel.R
import com.baghdad.viewmodel.base.BaseViewModel
import com.baghdad.viewmodel.errorStates.BaseSnackBarMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

@HiltViewModel
class ActorGalleryViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getGalleryImagesUseCase: GetActorGalleryUseCase,
    private val ioDispatcher: CoroutineDispatcher,
    ) : BaseViewModel<ActorGalleryScreenState, ActorGalleryScreenEffect>(ActorGalleryScreenState()),
    ActorGalleryInteractionListener {

    private val actorId: Long = checkNotNull(savedStateHandle["actorId"])


    init {
        loadData()
    }

    private fun loadData() {
        getActorGalleryImages(actorId)
    }

    override fun mapThrowableToErrorMessage(throwable: Throwable): BaseSnackBarMessage =
        BaseSnackBarMessage.UnknownError

    fun getActorGalleryImages(actorId: Long) {
        tryToExecute(
            callee = { getGalleryImagesUseCase.invoke(actorId) },
            dispatcher = ioDispatcher,
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

    override fun onGalleryImageClick(imageUrl: String) {
        updateState { it.copy(selectedImageUrl = imageUrl) }
    }

    override fun onImageDialogDismiss() {
        updateState { it.copy(selectedImageUrl = "") }
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
        hideSnackBar()
        loadData()
    }
}
