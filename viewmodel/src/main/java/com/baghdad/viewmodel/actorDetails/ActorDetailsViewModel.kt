package com.baghdad.viewmodel.actorDetails

import com.baghdad.domain.exception.NoInternetException
import com.baghdad.domain.usecase.actor.GetActorGalleryUseCase
import com.baghdad.domain.usecase.actor.GetActorInfoUseCase
import com.baghdad.domain.usecase.actor.GetActorMoviesUseCase
import com.baghdad.domain.usecase.actor.GetActorTvShowUseCase
import com.baghdad.entity.media.Movie
import com.baghdad.entity.media.TvShow
import com.baghdad.entity.person.Actor
import com.baghdad.viewmodel.R
import com.baghdad.viewmodel.base.BaseViewModel
import com.baghdad.viewmodel.errorStates.BaseSnackBarMessage

class ActorDetailsViewModel(
    private val actorId: Long,
    private val getActorInfoUseCase: GetActorInfoUseCase,
    private val getActorMoviesUseCase: GetActorMoviesUseCase,
    private val getActorTvShowUseCase: GetActorTvShowUseCase,
    private val getActorGalleryUseCase: GetActorGalleryUseCase,
) : BaseViewModel<ActorDetailsScreenState, ActorDetailsScreenEffect>(ActorDetailsScreenState()),
    ActorDetailsInteractionListener {
    init {
        loadData()
    }

    private fun loadData() {
        getActorInfo(actorId)
        getActorGallery(actorId)
        getActorMovies(actorId)
        getActorTvShows(actorId)
    }

    override fun mapThrowableToErrorMessage(throwable: Throwable): BaseSnackBarMessage = BaseSnackBarMessage.UnknownError

    private fun getActorInfo(actorId: Long) {
        tryToExecute(
            callee = {
                getActorInfoUseCase(actorId)
            },
            onSuccess = ::onGetActorInfoSuccess,
            onStart = ::onGetActorInfoStart,
            onError = ::onError,
            onFinally = ::onGetActorInfoFinally,
        )
    }

    private fun onGetActorInfoSuccess(actor: Actor) {
        updateState { actorDetailsScreenState ->
            actorDetailsScreenState.copy(
                actorInfo = actor.toActorInfoUI(),
            )
        }
    }

    private fun onGetActorInfoStart() {
        updateState { it.copy(isActorInfoLoading = true) }
    }

    private fun onGetActorInfoFinally() {
        updateState { it.copy(isActorInfoLoading = false) }
    }

    private fun getActorGallery(actorId: Long) {
        tryToExecute(
            callee = { getActorGalleryUseCase(actorId) },
            onSuccess = ::onGetActorGallerySuccess,
            onStart = ::onGetActorGalleryStart,
            onError = ::onError,
            onFinally = ::onGetActorGalleryFinally,
        )
    }

    private fun onGetActorGallerySuccess(gallery: List<String>) {
        updateState { actorDetailsScreenState ->
            actorDetailsScreenState.copy(
                gallery = gallery.take(MAX_GALLERY_IMAGES),
            )
        }
    }

    private fun onGetActorGalleryStart() {
        updateState { it.copy(isGalleryLoading = true) }
    }

    private fun onGetActorGalleryFinally() {
        updateState { it.copy(isGalleryLoading = false) }
    }

    private fun getActorMovies(actorId: Long) {
        tryToExecute(
            callee = { getActorMoviesUseCase(actorId) },
            onSuccess = ::onGetActorMoviesSuccess,
            onStart = ::onGetActorMoviesStart,
            onError = ::onError,
            onFinally = ::onGetActorMoviesFinally,
        )
    }

    private fun onGetActorMoviesSuccess(movies: List<Movie>) {
        updateState { actorDetailsScreenState ->
            actorDetailsScreenState.copy(
                topMoviesPicks = movies.take(MAX_TOP_MOVIE_PICKS).map { it.toMovieUI() },
            )
        }
    }

    private fun onGetActorMoviesStart() {
        updateState { it.copy(isTopMoviePicksLoading = true) }
    }

    private fun onGetActorMoviesFinally() {
        updateState { it.copy(isTopMoviePicksLoading = false) }
    }

    private fun getActorTvShows(actorId: Long) {
        tryToExecute(
            callee = { getActorTvShowUseCase(actorId) },
            onSuccess = ::onGetActorTvShowsSuccess,
            onStart = ::onGetActorTvShowsStart,
            onError = ::onError,
            onFinally = ::onGetActorTvShowsFinally,
        )
    }

    private fun onGetActorTvShowsSuccess(tvShows: List<TvShow>) {
        hideSnackBar()
        updateState { actorDetailsScreenState ->
            actorDetailsScreenState.copy(
                topTvShowsPicks = tvShows.take(MAX_TOP_TV_SHOW_PICKS).map { it.toTvShowUI() },
            )
        }
    }

    private fun onGetActorTvShowsStart() {
        updateState { it.copy(isTopTvShowPicksLoading = true) }
    }

    private fun onGetActorTvShowsFinally() {
        updateState { it.copy(isTopTvShowPicksLoading = false) }
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

    override fun onBackIconClick() {
        sendEffect(ActorDetailsScreenEffect.NavigateBack)
    }

    override fun onReadMoreBiographyClick() {
        updateState { it.copy(isTextExpanded = !it.isTextExpanded) }
    }

    override fun onViewAllGalleryClick() {
        sendEffect(ActorDetailsScreenEffect.NavigateToActorGallery)
    }

    override fun onViewAllTopMoviesPicksClick() {
        sendEffect(ActorDetailsScreenEffect.NavigateToActorTopMoviePicks)
    }

    override fun onViewAllTopTvShowsClick() {
        sendEffect(ActorDetailsScreenEffect.NavigateToActorTopTvShowPicks)
    }

    override fun onMovieCardClick(movieId: Long) {
        sendEffect(ActorDetailsScreenEffect.NavigateToMovieDetails(movieId))
    }

    override fun onTvShowCardClick(tvShowId: Long) {
        sendEffect(ActorDetailsScreenEffect.NavigateToTvShowDetails(tvShowId))
    }

    override fun onSaveMovieClick(movieId: Long) {
//        TODO("Not yet implemented")
    }

    override fun onSaveTvShowClick(tvShowId: Long) {
//        TODO("Not yet implemented")
    }

    override fun onSnackBarActionLabelClick() {
        loadData()
    }

    companion object {
        private const val MAX_GALLERY_IMAGES = 10
        private const val MAX_TOP_MOVIE_PICKS = 10
        private const val MAX_TOP_TV_SHOW_PICKS = 10
    }
}
