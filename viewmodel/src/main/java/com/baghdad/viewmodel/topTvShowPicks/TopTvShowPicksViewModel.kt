package com.baghdad.viewmodel.topTvShowPicks

import com.baghdad.domain.exception.NoInternetException
import androidx.lifecycle.SavedStateHandle
import com.baghdad.domain.usecase.actor.GetActorTvShowUseCase
import com.baghdad.entity.media.TvShow
import com.baghdad.viewmodel.R
import com.baghdad.viewmodel.base.BaseViewModel
import com.baghdad.viewmodel.errorStates.BaseSnackBarMessage
import com.baghdad.viewmodel.errorStates.SearchSnackBarMessage
import kotlinx.coroutines.CoroutineDispatcher
import dagger.hilt.android.lifecycle.HiltViewModel
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

    override fun onSaveTvShowClick(tvShowId: Long) {
        updateState {
            it.copy(
                tvShows = it.tvShows.map { item ->
                    if (item.id == tvShowId) item.copy(isSaved = item.isSaved.not()) else item
                }
            )
        }
        showSnackBar(
            message = SearchSnackBarMessage.SavedItemSuccessfully, isSuccess = true
        )
    }

    override fun onBackClick() {
        sendEffect(TopTvShowPicksEffect.NavigateBack)
    }

    override fun onSnackBarActionLabelClick() {
        getActorTvShow(actorId)
    }

    private fun onLoading() {
        updateState { it.copy(isLoading = true) }
    }

    private fun onFinally() {
        updateState { it.copy(isLoading = false) }
    }
}
