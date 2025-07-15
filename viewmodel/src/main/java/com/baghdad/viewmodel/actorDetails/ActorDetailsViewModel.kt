package com.baghdad.viewmodel.actorDetails

import com.baghdad.domain.usecase.actorDetails.GetActorGalleryUseCase
import com.baghdad.domain.usecase.actorDetails.GetActorInfoUseCase
import com.baghdad.domain.usecase.actorDetails.GetActorMoviesUseCase
import com.baghdad.domain.usecase.actorDetails.GetActorTvShowUseCase
import com.baghdad.entity.media.Movie
import com.baghdad.entity.media.TvShow
import com.baghdad.entity.person.Actor
import com.baghdad.viewmodel.base.BaseViewModel
import com.baghdad.viewmodel.errorStates.BaseSnackBarMessage

class ActorDetailsViewModel(
    private val getActorInfoUseCase: GetActorInfoUseCase,
    private val getActorMoviesUseCase: GetActorMoviesUseCase,
    private val getActorTvShowUseCase: GetActorTvShowUseCase,
    private val getActorGalleryUseCase: GetActorGalleryUseCase
) :
    BaseViewModel<ActorDetailsScreenState, ActorDetailsScreenEffect>(ActorDetailsScreenState()),
    ActorDetailsInteractionListener {

    private val actorId: Long = 1 /*TODO*/

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
                topMoviesPicks = movies.map { it.toMovieUI() }
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
                topTvShowsPicks = tvShows.map { it.toTvShowUI() }
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
                gallery = gallery
            )
        }
    }


    override fun onBackIconClick() {
//        TODO("Not yet implemented")
    }

    override fun onReadMoreBiographyClick() {
        updateState { it.copy(isTextExpanded = !it.isTextExpanded) }
    }

    override fun onViewAllGalleryClick() {
//        TODO("Not yet implemented")
    }

    override fun onViewAllTopMoviesPicksClick() {
//        TODO("Not yet implemented")
    }

    override fun onViewAllTopTvShowsClick() {
//        TODO("Not yet implemented")
    }

    override fun onMovieCardClick(movieId: Long) {
//        TODO("Not yet implemented")
    }

    override fun onTvShowCardClick(tvShowId: Long) {
//        TODO("Not yet implemented")
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