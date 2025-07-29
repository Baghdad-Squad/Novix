package com.baghdad.viewmodel.actorDetails

import androidx.lifecycle.SavedStateHandle
import com.baghdad.domain.usecase.actor.GetActorGalleryUseCase
import com.baghdad.domain.usecase.actor.GetActorInfoUseCase
import com.baghdad.domain.usecase.actor.GetActorMoviesUseCase
import com.baghdad.domain.usecase.actor.GetActorTvShowUseCase
import com.baghdad.entity.media.Movie
import com.baghdad.entity.media.TvShow
import com.baghdad.entity.person.Actor
import com.baghdad.viewmodel.base.BaseViewModel
import com.baghdad.viewmodel.errorStates.BaseSnackBarMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ActorDetailsViewModel @Inject constructor(
//    actorId: Long,
    savedStateHandle: SavedStateHandle,

    private val getActorInfoUseCase: GetActorInfoUseCase,
    private val getActorMoviesUseCase: GetActorMoviesUseCase,
    private val getActorTvShowUseCase: GetActorTvShowUseCase,
    private val getActorGalleryUseCase: GetActorGalleryUseCase
) :
    BaseViewModel<ActorDetailsScreenState, ActorDetailsScreenEffect>(ActorDetailsScreenState()),
    ActorDetailsInteractionListener {

    private val actorId: Long = checkNotNull(savedStateHandle["actorId"])
    init {
        getActorInfo(actorId = actorId)
        getActorMovies(actorId = actorId)
        getActorTvShows(actorId = actorId)
        getActorGallery(actorId = actorId)
    }

    override fun mapThrowableToErrorMessage(throwable: Throwable): BaseSnackBarMessage {
        return BaseSnackBarMessage.UnknownError
    }

    private fun getActorInfo(
        actorId: Long
    ) {
        tryToExecute(
            callee = {
                getActorInfoUseCase(actorId)
            },
            onSuccess = ::onGetActorInfoSuccess,
            onStart = ::onLoading,
            onFinally = ::onFinally
        )
    }

    private fun onGetActorInfoSuccess(actor: Actor) {
        updateState { actorDetailsScreenState ->
            actorDetailsScreenState.copy(
                actorInfo = actor.toActorInfoUI()
            )
        }
    }

    private fun getActorMovies(actorId: Long) {
        tryToExecute(
            callee = { getActorMoviesUseCase(actorId) },
            onSuccess = ::onGetActorMoviesSuccess,
            onStart = ::onLoading,
            onFinally = ::onFinally
        )
    }

    private fun onGetActorMoviesSuccess(movies: List<Movie>) {
        updateState { actorDetailsScreenState ->
            actorDetailsScreenState.copy(
                isMoviesMoreThanTen = movies.size > 10,
                topMoviesPicks = movies.take(10).map { it.toMovieUI() }
            )
        }
    }

    private fun getActorTvShows(actorId: Long) {
        tryToExecute(
            callee = { getActorTvShowUseCase(actorId) },
            onSuccess = ::onGetActorTvShowsSuccess,
            onStart = ::onLoading,
            onFinally = ::onFinally
        )
    }

    private fun onGetActorTvShowsSuccess(tvShows: List<TvShow>) {
        updateState { actorDetailsScreenState ->
            actorDetailsScreenState.copy(
                isTvShowsMoreThanTen = tvShows.size > 10,
                topTvShowsPicks = tvShows.take(10).map { it.toTvShowUI() }
            )
        }
    }

    private fun getActorGallery(actorId: Long) {
        tryToExecute(
            callee = { getActorGalleryUseCase(actorId) },
            onSuccess = ::onGetActorGallerySuccess,
            onStart = ::onLoading,
            onFinally = ::onFinally
        )
    }

    private fun onGetActorGallerySuccess(gallery: List<String>) {
        updateState { actorDetailsScreenState ->
            actorDetailsScreenState.copy(
                isGalleryMoreThanTen = gallery.size > 10,
                gallery = gallery.take(10)
            )
        }
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

    private fun onLoading() {
        updateState { it.copy(isLoading = true) }
    }

    private fun onFinally() {
        updateState { it.copy(isLoading = false) }
    }

}