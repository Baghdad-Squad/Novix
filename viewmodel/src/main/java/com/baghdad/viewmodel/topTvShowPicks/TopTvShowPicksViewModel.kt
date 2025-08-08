package com.baghdad.viewmodel.topTvShowPicks

import androidx.lifecycle.SavedStateHandle
import com.baghdad.domain.exception.NoInternetException
import com.baghdad.domain.usecase.actor.GetActorTvShowUseCase
import com.baghdad.entity.media.TvShow
import com.baghdad.viewmodel.R
import com.baghdad.viewmodel.base.BaseViewModel
import com.baghdad.viewmodel.errorStates.BaseSnackBarMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

@HiltViewModel
class TopTvShowViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getActorTvShowUseCase: GetActorTvShowUseCase,
    private val ioDispatcher: CoroutineDispatcher,
) : BaseViewModel<TopTvShowPicksState, TopTvShowPicksEffect>
    (TopTvShowPicksState()), TopTvShowPicksInteractionListener {

    private val actorId: Long = checkNotNull(savedStateHandle["actorId"])

    init {
        getActorTvShow(actorId)
    }

    private fun getActorTvShow(actorId: Long) {
        tryToExecute(
            callee = { getActorTvShowUseCase(actorId) },
            dispatcher = ioDispatcher,
            onSuccess = ::onGetActorTvShowSuccess,
            onStart = ::onLoading,
            onError = ::onGetActorTvShowError,
            onFinally = ::onFinally
        )
    }

    private fun onGetActorTvShowSuccess(tvShows: List<TvShow>) {
        hideSnackBar()
        updateState { topTvShowState ->
            topTvShowState.copy(
                tvShows = tvShows.map { it.toUIState() }
            )
        }
    }

    override fun mapThrowableToErrorMessage(throwable: Throwable): BaseSnackBarMessage =
        BaseSnackBarMessage.UnknownError

    private fun onGetActorTvShowError(throwable: Throwable) {
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

    override fun onTvShowDetailsClick(tvShowId: Long) {
        sendEffect(TopTvShowPicksEffect.NavigateToTvShowDetails(tvShowId))
    }

    override fun onBackClick() {
        sendEffect(TopTvShowPicksEffect.NavigateBack)
    }

    override fun onSnackBarActionLabelClick() {
        hideSnackBar()
        getActorTvShow(actorId)
    }

    private fun onLoading() {
        updateState { it.copy(isLoading = true) }
    }

    private fun onFinally() {
        updateState { it.copy(isLoading = false) }
    }
}
