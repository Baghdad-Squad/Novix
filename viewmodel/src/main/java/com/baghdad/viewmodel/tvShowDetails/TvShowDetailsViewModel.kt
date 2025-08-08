package com.baghdad.viewmodel.tvShowDetails

import androidx.lifecycle.SavedStateHandle
import com.baghdad.domain.exception.NoInternetException
import com.baghdad.domain.model.ContinueWatching
import com.baghdad.domain.model.MediaAccountStates
import com.baghdad.domain.usecase.continueWatching.AddContinueWatchingUseCase
import com.baghdad.domain.usecase.login.IsUserLoggedInUseCase
import com.baghdad.domain.usecase.tvShow.AddTvShowRateUseCase
import com.baghdad.domain.usecase.tvShow.GetTvShowAccountStatesUseCase
import com.baghdad.domain.usecase.tvShow.GetTvShowCastMembersUseCase
import com.baghdad.domain.usecase.tvShow.GetTvShowDetailsUseCase
import com.baghdad.domain.usecase.tvShow.GetTvShowSeasonEpisodesUseCase
import com.baghdad.entity.media.Episode
import com.baghdad.entity.media.TvShow
import com.baghdad.entity.person.CastMember
import com.baghdad.viewmodel.R
import com.baghdad.viewmodel.base.BaseViewModel
import com.baghdad.viewmodel.errorStates.BaseSnackBarMessage
import com.baghdad.viewmodel.shared.BottomSheetType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

@HiltViewModel
class TvShowDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getTvShowDetailsUseCase: GetTvShowDetailsUseCase,
    private val getTvShowCastMembersUseCase: GetTvShowCastMembersUseCase,
    private val getTvShowSeasonEpisodesUseCase: GetTvShowSeasonEpisodesUseCase,
    private val addContinueWatchingUseCase: AddContinueWatchingUseCase,
    private val addTvShowRateUseCase: AddTvShowRateUseCase,
    private val isUserLoggedInUseCase: IsUserLoggedInUseCase,
    private val getTvShowAccountStatesUseCase: GetTvShowAccountStatesUseCase,
    private val ioDispatcher: CoroutineDispatcher,
) :
    BaseViewModel<TvShowDetailsScreenState, TvShowDetailsScreenEffect>(TvShowDetailsScreenState()),
    TvShowDetailsInteractionListener {

    private val tvShowId: Long = checkNotNull(savedStateHandle["tvShowId"])


    init {
        getTvShowDetails(tvShowId)
        getTvShowCast(tvShowId)
        onClickSeasonTab(0)
        getTvShowAccountStates()
        isUserLoggedIn()
    }

    private fun getTvShowDetails(tvShowId: Long) {
        tryToExecute(
            callee = { getTvShowDetailsUseCase(tvShowId) },
            dispatcher = ioDispatcher,
            onSuccess = ::onGetTvShowDetailsSuccess,
            onStart = { updateState { it.copy(isTvShowDetailsLoading = true) } },
            onFinally = ::onFinallyAndAddToContinueWatching,
            onError = ::onLoadDataError
        )
    }

    private fun onLoadDataError(throwable: Throwable) {
        when (throwable) {
            is NoInternetException -> showNoInternetSnackBar()
            else -> handleError(throwable)
        }
    }

    private fun onGetTvShowDetailsSuccess(tvShow: TvShow) {
        updateState { tvShowDetailsScreenState ->
            tvShowDetailsScreenState.copy(
                tvShowInfo = tvShow.toUiState()
            )
        }
    }

    private fun getTvShowCast(tvShowId: Long) {
        tryToExecute(
            callee = { getTvShowCastMembersUseCase(tvShowId) },
            dispatcher = ioDispatcher,
            onSuccess = ::onGetTvShowCastSuccess,
            onStart = { updateState { it.copy(isCastMembersLoading = true) } },
            onFinally = { updateState { it.copy(isCastMembersLoading = false) } },
            onError = ::onLoadDataError
        )
    }

    private fun onGetTvShowCastSuccess(cast: List<CastMember>) {
        updateState { tvShowDetailsScreenState ->
            tvShowDetailsScreenState.copy(
                castMembers = cast.map { it.toUiState() }
            )
        }
    }

    override fun mapThrowableToErrorMessage(throwable: Throwable): BaseSnackBarMessage =
        BaseSnackBarMessage.UnknownError

    override fun onClickBackIcon() {
        sendEffect(TvShowDetailsScreenEffect.NavigateBack)
    }

    override fun onClickReadMoreOverview() {
        updateState { it.copy(isTextExpanded = !it.isTextExpanded) }
    }

    override fun onClickGenre(genreId: Long) {
        sendEffect(TvShowDetailsScreenEffect.NavigateToGenreScreen(genreId))
    }

    override fun onClickCastMember(actorId: Long) {
        sendEffect(TvShowDetailsScreenEffect.NavigateToActorDetails(actorId))
    }

    override fun onClickEpisode(seasonNumber: Int, episodeNumber: Int) {
        sendEffect(
            TvShowDetailsScreenEffect.NavigateToEpisodeDetails(
                tvShowId = tvShowId,
                seasonNumber,
                episodeNumber
            )
        )
    }

    override fun onClickReviews() {
        sendEffect(TvShowDetailsScreenEffect.NavigateToReviews(tvShowId))
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
            onError = ::onLoadDataError
        )
    }


    private fun onIsUserLoggedInSuccess(isLoggedIn: Boolean) {
        val newBottomSheetType = if (isLoggedIn) {
            BottomSheetType.ShowRating
        } else {
            BottomSheetType.RequireLogin
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
                tvShowInfo = it.tvShowInfo.copy(userRating = rating)
            )
        }
    }

    override fun onDismissRatingBottomSheet() {
        updateState {
            it.copy(
                ratingStatus = it.ratingStatus.copy(
                    isBottomSheetVisible = false,
                ),
                tvShowInfo = it.tvShowInfo.copy(userRating = 0)
            )
        }
    }


    override fun onClickSubmitRating(rating: Int) {
        tryToExecute(
            callee = { addTvShowRateUseCase(tvShowId, rating) },
            onSuccess = { onSubmitRatingSuccess() },
            dispatcher = ioDispatcher,
            onError = ::onLoadDataError
        )
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

    private fun getTvShowAccountStates() {
        tryToExecute(
            callee = { getTvShowAccountStatesUseCase(tvShowId) },
            dispatcher = ioDispatcher,
            onSuccess = ::onGetTvShowAccountStatesSuccess,
            onError = ::onLoadDataError
        )
    }

    private fun onGetTvShowAccountStatesSuccess(accountStates: MediaAccountStates) {
        updateState {
            it.copy(
                isRated = accountStates.isMediaRated,
            )
        }
    }

    override fun onClickSeasonTab(seasonIndex: Int) {
        updateState { it.copy(selectedSeasonIndex = seasonIndex) }
        tryToExecute(
            callee = { getTvShowSeasonEpisodesUseCase(tvShowId, seasonIndex + 1) },
            dispatcher = ioDispatcher,
            onSuccess = ::onGetTvShowEpisodesSuccess,
            onStart = { updateState { it.copy(isEpisodesLoading = true) } },
            onFinally = { updateState { it.copy(isEpisodesLoading = false) } },
            onError = ::onLoadDataError
        )
    }

    private fun onGetTvShowEpisodesSuccess(episodes: List<Episode>) {
        hideSnackBar()
        updateState { tvShowDetailsScreenState ->
            tvShowDetailsScreenState.copy(
                episodes = episodes.map { it.toUiState() }
            )
        }
    }

    override fun onClickPlayTrailer() {
        sendEffect(TvShowDetailsScreenEffect.OpenYoutubeLink(currentState.tvShowInfo.trailerURL))
    }

    override fun onSnackBarActionLabelClick() {
        getTvShowDetails(tvShowId)
        getTvShowCast(tvShowId)
        onClickSeasonTab(0)
    }

    override fun onClickLoginButton() {
        sendEffect(TvShowDetailsScreenEffect.NavigateToLogin)
    }


    private fun onFinallyAndAddToContinueWatching() {
        updateState { it.copy(isTvShowDetailsLoading = false) }
        addToContinueWatching()
    }

    private fun showNoInternetSnackBar() {
        showSnackBar(
            message = BaseSnackBarMessage.NetworkError,
            actionLabelRes = R.string.retry,
            isSuccess = false,
            durationMillis = Int.MAX_VALUE.toLong(),
        )
    }

    private fun addToContinueWatching() {
        tryToExecute(
            callee = {
                addContinueWatchingUseCase(
                    tvShowId, currentState.tvShowInfo.genres.map { it.id ?: 0 },
                    contentImageUrl = currentState.tvShowInfo.posterPictureURL,
                    contentType = ContinueWatching.ContentType.TV_SHOW,
                )
            },
            dispatcher = ioDispatcher,
        )
    }

}
