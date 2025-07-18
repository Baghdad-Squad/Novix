package com.baghdad.viewmodel.episodeDetails

import com.baghdad.viewmodel.base.BaseViewModel
import com.baghdad.viewmodel.errorStates.BaseSnackBarMessage

class EpisodeDetailsViewModel : BaseViewModel<EpisodeDetailsState, EpisodeDetailsEffect>(
    EpisodeDetailsState()
), EpisodeDetailsInteractionListener {


    override fun mapThrowableToErrorMessage(throwable: Throwable): BaseSnackBarMessage {
        TODO("Not yet implemented")
    }

    override fun onSaveCurrentEpisodeClick() {
        updateState {
            it.copy(
                isSaved = !currentState.isSaved
            )
        }
    }

    override fun onClickExtendText() {
        updateState {
            it.copy(
                isExtendText = !currentState.isExtendText
            )
        }
    }
}