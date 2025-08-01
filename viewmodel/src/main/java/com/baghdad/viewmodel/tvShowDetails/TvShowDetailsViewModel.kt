package com.baghdad.viewmodel.tvShowDetails

import com.baghdad.domain.exception.NoInternetException
import com.baghdad.domain.model.ContinueWatching
import com.baghdad.domain.usecase.continueWatching.AddContinueWatchingUseCase
import com.baghdad.domain.usecase.tvShow.GetTvShowCastMembersUseCase
import com.baghdad.domain.usecase.tvShow.GetTvShowDetailsUseCase
import com.baghdad.domain.usecase.tvShow.GetTvShowSeasonEpisodesUseCase
import com.baghdad.entity.media.Episode
import com.baghdad.entity.media.TvShow
import com.baghdad.entity.person.CastMember
import com.baghdad.viewmodel.R
import com.baghdad.viewmodel.base.BaseViewModel
import com.baghdad.viewmodel.errorStates.BaseSnackBarMessage

class TvShowDetailsViewModel(
    private val tvShowId: Long,
    private val getTvShowDetailsUseCase: GetTvShowDetailsUseCase,
    private val getTvShowCastMembersUseCase: GetTvShowCastMembersUseCase,
    private val getTvShowSeasonEpisodesUseCase: GetTvShowSeasonEpisodesUseCase,
    private val addContinueWatchingUseCase: AddContinueWatchingUseCase,
) :
    BaseViewModel<TvShowDetailsScreenState, TvShowDetailsScreenEffect>(TvShowDetailsScreenState()),
    TvShowDetailsInteractionListener {

    init {
        getTvShowDetails(tvShowId)
        getTvShowCast(tvShowId)
        onClickSeasonTab(0)
    }

    private fun getTvShowDetails(tvShowId: Long) {
        tryToExecute(
            callee = { getTvShowDetailsUseCase(tvShowId) },
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

    override fun mapThrowableToErrorMessage(throwable: Throwable): BaseSnackBarMessage {
        return BaseSnackBarMessage.UnknownError
    }

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
        sendEffect(TvShowDetailsScreenEffect.NavigateToEpisodeDetails(seasonNumber, episodeNumber))
    }

    override fun onClickReviews(tvShowId: Long) {
        sendEffect(TvShowDetailsScreenEffect.NavigateToReviews(tvShowId))
    }

    override fun onClickSaveTvShow(tvShowId: Long) {
//        TODO("Not yet implemented")
    }

    override fun onClickAddRating() {
//        TODO("Not yet implemented")
    }

    override fun onClickSeasonTab(seasonIndex: Int) {
        updateState { it.copy(selectedSeasonIndex = seasonIndex) }

        tryToExecute(
            callee = { getTvShowSeasonEpisodesUseCase(tvShowId, seasonIndex + 1) },
            onSuccess = ::onGetTvShowEpisodesSuccess,
            onStart = {updateState { it.copy(isEpisodesLoading = true) }},
            onFinally = {updateState { it.copy(isEpisodesLoading = false) }},
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
        )
    }

}
