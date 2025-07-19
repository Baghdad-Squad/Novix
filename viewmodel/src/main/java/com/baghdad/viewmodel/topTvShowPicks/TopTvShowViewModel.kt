package com.baghdad.viewmodel.topTvShowPicks

import com.baghdad.domain.usecase.actor.GetActorTvShowUseCase
import com.baghdad.entity.media.TvShow
import com.baghdad.viewmodel.base.BaseViewModel
import com.baghdad.viewmodel.errorStates.BaseSnackBarMessage
import com.baghdad.viewmodel.errorStates.SearchScreenBaseSnackBarMessages

class TopTvShowViewModel(
    val actorId: Long,
    private val getActorTvShowUseCase: GetActorTvShowUseCase,
) : BaseViewModel<TopTvShowPicksState, TopTvShowPicksEffect>
    (TopTvShowPicksState()), TopTvShowPicksInteractionListener {

    init {
        getActorTvShow(actorId)
    }

    private fun getActorTvShow(actorId: Long) {
        tryToExecute(
            callee = { getActorTvShowUseCase(actorId) },
            onSuccess = ::onGetActorTvShowSuccess,
            onStart = ::onLoading,
            onFinally = ::onFinally
        )
    }

    private fun onGetActorTvShowSuccess(tvShows: List<TvShow>) {
        updateState { topTvShowState ->
            topTvShowState.copy(
                tvShows = tvShows.map { it.toUIState() }
            )
        }
    }

    override fun mapThrowableToErrorMessage(throwable: Throwable): BaseSnackBarMessage {
        return BaseSnackBarMessage.UnknownError
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
            message = SearchScreenBaseSnackBarMessages.SavedItemSuccessfully, isSuccess = true
        )
    }

    override fun onBackClick() {
        sendEffect(TopTvShowPicksEffect.NavigateBack)
    }

    private fun onLoading() {
        updateState { it.copy(isLoading = true) }
    }

    private fun onFinally() {
        updateState { it.copy(isLoading = false) }
    }
}
