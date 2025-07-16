package com.baghdad.viewmodel.tvShowDetails

import com.baghdad.domain.usecase.genre.GetGenresUseCase
import com.baghdad.domain.usecase.tvShowDetails.GetTvShowCastUseCase
import com.baghdad.domain.usecase.tvShowDetails.GetTvShowEpisodesUseCase
import com.baghdad.domain.usecase.tvShowDetails.GetTvShowInfoUseCase
import com.baghdad.viewmodel.base.BaseViewModel
import com.baghdad.viewmodel.errorStates.BaseSnackBarMessage

class TvShowDetailsViewModel(
    private val tvShowId: Long,
    private val getTvShowInfoUseCase: GetTvShowInfoUseCase,
    private val getTvShowCastUseCase: GetTvShowCastUseCase,
    private val getTvShowEpisodesUseCase: GetTvShowEpisodesUseCase,
    private val getTvShowGenresUseCase: GetGenresUseCase
) :
    BaseViewModel<TvShowDetailsScreenState, TvShowDetailsScreenEffect>(TvShowDetailsScreenState()),
    TvShowDetailsInteractionListener {

    init {
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

    override fun onClickEpisode(episodeId: Long) {
        sendEffect(TvShowDetailsScreenEffect.NavigateToEpisodeDetails(episodeId))
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

    override fun onClickSeasonTab() {
//        TODO("Not yet implemented")
    }

    override fun onClickPlayTrailer() {
//        TODO("Not yet implemented")
    }
}