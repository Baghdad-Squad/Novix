package com.baghdad.viewmodel.topMoviePicks

import com.baghdad.domain.usecase.actorDetails.GetActorMoviesUseCase
import com.baghdad.entity.media.Movie
import com.baghdad.viewmodel.base.BaseViewModel
import com.baghdad.viewmodel.errorStates.BaseSnackBarMessage
import com.baghdad.viewmodel.errorStates.SearchScreenBaseSnackBarMessages

class TopMoviePicksViewModel(
    val  actorId: Long,
    private val getActorMoviesUseCase: GetActorMoviesUseCase,
): BaseViewModel<TopMoviePicksState, TopMoviePicksEffect>
    (TopMoviePicksState()), TopMoviePicksInteractionListener {

     init {
         getActorMovies(actorId)
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
        updateState { topMoviePicksState ->
            topMoviePicksState.copy(
                movies = movies.map { it.toUIState() }
            )
        }
    }

    override fun mapThrowableToErrorMessage(throwable: Throwable): BaseSnackBarMessage {
        return BaseSnackBarMessage.UnknownError
    }

    override fun onMovieDetailsClick(movieId: Long) {
        sendEffect(TopMoviePicksEffect.NavigateToMovieDetails(movieId))
    }

    override fun onSaveMovieClick(movieId: Long) {
        updateState {
            it.copy(
                movies = it.movies.map { item ->
                    if (item.id == movieId) item.copy(isSaved = item.isSaved.not()) else item
                }
            )
        }
        showSnackBar(
            message = SearchScreenBaseSnackBarMessages.SavedItemSuccessfully, isSuccess = true
        )
    }

    override fun onBackClick() {
        sendEffect(TopMoviePicksEffect.NavigateBack)
    }
    private fun onLoading() {
        updateState { it.copy(isLoading = true) }
    }
    private fun onFinally() {
        updateState { it.copy(isLoading = false) }
    }
}
