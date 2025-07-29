package com.baghdad.viewmodel.episodeDetails

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import com.baghdad.domain.usecase.episode.GetEpisodeCastMembersUseCase
import com.baghdad.domain.usecase.episode.GetEpisodeDetailsUseCase
import com.baghdad.entity.media.Episode
import com.baghdad.entity.person.CastMember
import com.baghdad.viewmodel.base.BaseViewModel
import com.baghdad.viewmodel.errorStates.BaseSnackBarMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class EpisodeDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getEpisodeCastMembersUseCase: GetEpisodeCastMembersUseCase,
    private val getEpisodeDetailsUseCase: GetEpisodeDetailsUseCase
) : BaseViewModel<EpisodeDetailsScreenState, EpisodeDetailsScreenEffect>(EpisodeDetailsScreenState()),
    EpisodeDetailsInteractionListener {

    private val seasonNumber: Int = checkNotNull(savedStateHandle["seasonNumber"])
    private val episodeNumber: Int = checkNotNull(savedStateHandle["episodeNumber"])
//    private val tvShowId: Long = checkNotNull(savedStateHandle["tvShowId"])

    init {
        getEpisodeDetails(14L, seasonNumber, episodeNumber)
        getEpisodeCastMembers(14L, seasonNumber, episodeNumber)
    }

    private fun getEpisodeDetails(tvShowId: Long, seasonNumber: Int, episodeNumber: Int) {
        tryToExecute(
            callee = { getEpisodeDetailsUseCase(tvShowId, seasonNumber, episodeNumber) },
            onSuccess = ::onGetEpisodeDetailsSuccess,
            onStart = ::onGetEpisodeDetailsStart,
            onFinally = ::onGetEpisodeDetailsFinally
        )
    }

    private fun onGetEpisodeDetailsSuccess(episode: Episode) {
        Log.d("EpisodeDetailsViewModel", "Episode details: $episode")
        updateState {
            it.copy(episode = episode.toUiState())
        }
    }

    private fun onGetEpisodeDetailsStart() {
        updateState { it.copy(isEpisodeDetailsLoading = true) }
    }

    private fun onGetEpisodeDetailsFinally() {
        updateState { it.copy(isEpisodeDetailsLoading = false) }
    }

    private fun getEpisodeCastMembers(tvShowId: Long, seasonNumber: Int, episodeNumber: Int) {
        tryToExecute(
            callee = { getEpisodeCastMembersUseCase(tvShowId, seasonNumber, episodeNumber) },
            onSuccess = ::onGetEpisodeCastMembersSuccess,
            onStart = ::onGetEpisodeCastMembersLoading,
            onFinally = ::onGetEpisodeCastMembersFinally
        )
    }

    private fun onGetEpisodeCastMembersLoading() {
        updateState { it.copy(isEpisodeCastMembersLoading = true) }
    }

    private fun onGetEpisodeCastMembersFinally() {
        updateState { it.copy(isEpisodeCastMembersLoading = false) }
    }

    private fun onGetEpisodeCastMembersSuccess(castMembers: List<CastMember>) {
        updateState {
            it.copy(guestsOfHonor = castMembers.map { it.toUiState() })
        }
    }

    override fun mapThrowableToErrorMessage(throwable: Throwable): BaseSnackBarMessage {
//        TODO("Not yet implemented")
        return BaseSnackBarMessage.DefaultMessage
    }

    override fun onBackClick() {
        sendEffect(EpisodeDetailsScreenEffect.NavigateBack)
    }

    override fun onReadMoreOverviewClick() {
        updateState {
            it.copy(isOverviewExpanded = !it.isOverviewExpanded)
        }
    }

    override fun onCategoryClick(categoryId: Long) {
        sendEffect(EpisodeDetailsScreenEffect.NavigateToCategoryTvShows(categoryId))
    }

    override fun onGuestOfHonorClick(guestOfHonorId: Long) {
        sendEffect(EpisodeDetailsScreenEffect.NavigateToActorDetails(guestOfHonorId))
    }

    override fun onSaveEpisodeClick() {
        updateState {
            it.copy(addToListBottomSheetState = it.addToListBottomSheetState.copy(isVisible = true))
        }
    }

    override fun onDismissAddToListBottomSheetClick() {
        updateState {
            it.copy(addToListBottomSheetState = it.addToListBottomSheetState.copy(isVisible = false))
        }
    }

    override fun onLoginClick() {
        sendEffect(EpisodeDetailsScreenEffect.NavigateToLogin)
    }

    override fun onPlayTrailerClick() {
//        TODO("Not yet implemented")
    }

    override fun onRateEpisodeClick() {
        updateState {
            it.copy(rateEpisodeBottomSheetState = it.rateEpisodeBottomSheetState.copy(isVisible = true))
        }
    }

    override fun onDismissRatingBottomSheet() {
        updateState {
            it.copy(rateEpisodeBottomSheetState = it.rateEpisodeBottomSheetState.copy(isVisible = false))
        }
    }
}