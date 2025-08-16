package com.baghdad.viewmodel.episodeDetails

import androidx.lifecycle.SavedStateHandle
import com.baghdad.domain.exception.NoInternetException
import com.baghdad.domain.usecase.episode.AddEpisodeRateUseCase
import com.baghdad.domain.usecase.episode.GetEpisodeAccountStatesUseCase
import com.baghdad.domain.usecase.episode.GetEpisodeCastMembersUseCase
import com.baghdad.domain.usecase.episode.GetEpisodeDetailsUseCase
import com.baghdad.domain.usecase.login.IsUserLoggedInUseCase
import com.baghdad.entity.media.Episode
import com.baghdad.entity.person.CastMember
import com.baghdad.viewmodel.R
import com.baghdad.viewmodel.base.BaseViewModel
import com.baghdad.viewmodel.errorStates.BaseSnackBarMessage
import com.baghdad.viewmodel.shared.BottomSheetType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

@HiltViewModel
class EpisodeDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getEpisodeCastMembersUseCase: GetEpisodeCastMembersUseCase,
    private val getEpisodeDetailsUseCase: GetEpisodeDetailsUseCase,
    private val addEpisodeRateUseCase: AddEpisodeRateUseCase,
    private val getEpisodeAccountStatesUseCase: GetEpisodeAccountStatesUseCase,
    private val isUserLoggedInUseCase: IsUserLoggedInUseCase,
    private val ioDispatcher: CoroutineDispatcher
) : BaseViewModel<EpisodeDetailsScreenState, EpisodeDetailsScreenEffect>(EpisodeDetailsScreenState()),
    EpisodeDetailsInteractionListener {

    private val seasonNumber: Int = checkNotNull(savedStateHandle["seasonNumber"])
    private val episodeNumber: Int = checkNotNull(savedStateHandle["episodeNumber"])
    private val tvShowId: Long = checkNotNull(savedStateHandle["tvShowId"])

    init {
        loadInitData(tvShowId, seasonNumber, episodeNumber)
    }

    private fun loadInitData(tvShowId: Long, seasonNumber: Int, episodeNumber: Int) {
        getEpisodeDetails(tvShowId, seasonNumber, episodeNumber)
        getEpisodeCastMembers(tvShowId, seasonNumber, episodeNumber)
        isUserLoggedIn()
    }

    private fun getEpisodeDetails(tvShowId: Long, seasonNumber: Int, episodeNumber: Int) {
        tryToExecute(
            callee = { getEpisodeDetailsUseCase(tvShowId, seasonNumber, episodeNumber) },
            dispatcher = ioDispatcher,
            onSuccess = ::onGetEpisodeDetailsSuccess,
            onStart = ::onGetEpisodeDetailsStart,
            onFinally = ::onGetEpisodeDetailsFinally,
            onError = ::onError,
        )
    }

    private fun onGetEpisodeDetailsSuccess(episode: Episode) {
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
        hideSnackBar()
        tryToExecute(
            callee = { getEpisodeCastMembersUseCase(tvShowId, seasonNumber, episodeNumber) },
            dispatcher = ioDispatcher,
            onSuccess = ::onGetEpisodeCastMembersSuccess,
            onStart = ::onGetEpisodeCastMembersLoading,
            onFinally = ::onGetEpisodeCastMembersFinally,
            onError = ::onError
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

    override fun onClickStarButton() {
        updateState {
            it.copy(
                ratingStatus = it.ratingStatus.copy(
                    isBottomSheetVisible = true,
                )
            )
        }
    }

    private fun isUserLoggedIn() {
        tryToExecute(
            callee = { isUserLoggedInUseCase() },
            dispatcher = ioDispatcher,
            onSuccess = ::onIsUserLoggedInSuccess,
            onError = ::onError
        )
    }


    private fun onIsUserLoggedInSuccess(isLoggedIn: Boolean) {
        val newBottomSheetType = if (isLoggedIn) {
            BottomSheetType.ShowRating
        } else {
            BottomSheetType.RequireLogin
        }

        if (isLoggedIn) {
            getEpisodeAccountStates()
        }
        updateState {
            it.copy(
                ratingStatus = it.ratingStatus.copy(
                    bottomSheetType = newBottomSheetType,
                ),
                isRated = it.isRated && isLoggedIn,
            )
        }
    }

    override fun onRatingChanged(rating: Int) {
        updateState {
            it.copy(
                episode = it.episode.copy(userRating = rating)
            )
        }
    }

    override fun onDismissRatingBottomSheet() {
        updateState {
            it.copy(
                ratingStatus = it.ratingStatus.copy(
                    isBottomSheetVisible = false,
                ),
                episode = it.episode.copy(userRating = 0)
            )
        }
    }

    override fun onClickSubmitRating(rating: Int) {
        tryToExecute(
            callee = {
                addEpisodeRateUseCase(
                    tvShowId = tvShowId,
                    seasonNumber = seasonNumber,
                    episodeNumber = episodeNumber,
                    rating = rating
                )
            },
            onSuccess = { onSubmitRatingSuccess() },
            dispatcher = ioDispatcher,
            onError = ::onError
        )
    }

    override fun onClickLoginButton() {
        sendEffect(EpisodeDetailsScreenEffect.NavigateToLogin)
    }

    private fun onSubmitRatingSuccess() {
        updateState {
            it.copy(
                ratingStatus = it.ratingStatus.copy(
                    isBottomSheetVisible = false,
                    bottomSheetType = BottomSheetType.Hidden,
                ),
                isRated = true
            )
        }
        showSnackBar(
            message = BaseSnackBarMessage.ItemRateSuccessfully,
            isSuccess = true
        )
    }

    private fun getEpisodeAccountStates() {
        tryToExecute(
            callee = {
                getEpisodeAccountStatesUseCase(
                    tvShowId = tvShowId,
                    seasonNumber = seasonNumber,
                    episodeNumber = episodeNumber
                )
            },
            dispatcher = ioDispatcher,
            onSuccess = ::onGetEpisodeStatesSuccess,
            onError = ::onError
        )
    }

    private fun onGetEpisodeStatesSuccess(isEpisodeRated: Boolean) {
        updateState {
            it.copy(
                isRated = isEpisodeRated,
            )
        }
    }


    private fun onError(throwable: Throwable) {
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

    override fun onSnackBarActionLabelClick() {
        hideSnackBar()
        loadInitData(
            tvShowId, seasonNumber, episodeNumber
        )
    }
}