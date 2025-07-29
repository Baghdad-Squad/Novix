package com.baghdad.viewmodel.tvShowDetails

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import com.baghdad.domain.model.ContinueWatching
import com.baghdad.domain.usecase.continueWatching.AddContinueWatchingUseCase
import com.baghdad.domain.usecase.tvShow.GetTvShowCastMembersUseCase
import com.baghdad.domain.usecase.tvShow.GetTvShowDetailsUseCase
import com.baghdad.domain.usecase.tvShow.GetTvShowSeasonEpisodesUseCase
import com.baghdad.entity.media.Episode
import com.baghdad.entity.media.TvShow
import com.baghdad.entity.person.CastMember
import com.baghdad.viewmodel.base.BaseViewModel
import com.baghdad.viewmodel.errorStates.BaseSnackBarMessage
import com.baghdad.viewmodel.review.ContentType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import javax.inject.Inject

@HiltViewModel
class TvShowDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getTvShowDetailsUseCase: GetTvShowDetailsUseCase,
    private val getTvShowCastMembersUseCase: GetTvShowCastMembersUseCase,
    private val getTvShowSeasonEpisodesUseCase: GetTvShowSeasonEpisodesUseCase,
    private val addContinueWatchingUseCase: AddContinueWatchingUseCase,
) :
    BaseViewModel<TvShowDetailsScreenState, TvShowDetailsScreenEffect>(TvShowDetailsScreenState()),
    TvShowDetailsInteractionListener {

    private val tvShowId: Long = checkNotNull(savedStateHandle["tvShowId"])


    init {
        getTvShowDetails(tvShowId)
        getTvShowCast(tvShowId)
        onClickSeasonTab(0)

    }

    private fun getTvShowDetails(tvShowId: Long) {
        tryToExecute(
            callee = { getTvShowDetailsUseCase(tvShowId) },
            onSuccess = ::onGetTvShowDetailsSuccess,
            onStart = ::onLoading,
            onFinally = ::onFinallyAndAddToContinueWatching
        )
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
            onStart = ::onLoading,
            onFinally = ::onFinally
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
            onStart = ::onLoading,
            onFinally = ::onFinally
        )
    }

    private fun onGetTvShowEpisodesSuccess(episodes: List<Episode>) {
        updateState { tvShowDetailsScreenState ->
            tvShowDetailsScreenState.copy(
                episodes = episodes.map { it.toUiState() }
            )
        }
    }

    override fun onClickPlayTrailer() {
        sendEffect(TvShowDetailsScreenEffect.OpenYoutubeLink(currentState.tvShowInfo.trailerURL))
    }

    private fun onLoading() {
        updateState { it.copy(isLoading = true) }
    }

    private fun onFinally() {
        updateState { it.copy(isLoading = false) }
    }
    private fun onFinallyAndAddToContinueWatching() {
        onFinally()
        addToContinueWatching()
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
