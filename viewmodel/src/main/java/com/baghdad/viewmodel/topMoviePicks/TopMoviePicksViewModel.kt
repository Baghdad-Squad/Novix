package com.baghdad.viewmodel.topMoviePicks

import androidx.lifecycle.SavedStateHandle
import com.baghdad.domain.exception.NoInternetException
import com.baghdad.domain.usecase.actor.GetActorMoviesUseCase
import com.baghdad.entity.media.Movie
import com.baghdad.viewmodel.R
import com.baghdad.viewmodel.base.BaseViewModel
import com.baghdad.viewmodel.errorStates.BaseSnackBarMessage
import com.baghdad.viewmodel.errorStates.SearchSnackBarMessage
import kotlinx.coroutines.CoroutineDispatcher
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TopMoviePicksViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getActorMoviesUseCase: GetActorMoviesUseCase,
    private val ioDispatcher: CoroutineDispatcher,
): BaseViewModel<TopMoviePicksState, TopMoviePicksEffect>
    (TopMoviePicksState()), TopMoviePicksInteractionListener {

    private val actorId: Long = checkNotNull(savedStateHandle["actorId"])

    init {
        loadData()
    }

    private fun loadData() {
        getActorMovies(actorId)
    }

    private fun getActorMovies(actorId: Long) {
        tryToExecute(
            callee = { getActorMoviesUseCase(actorId) },
            dispatcher = ioDispatcher,
            onSuccess = ::onGetActorMoviesSuccess,
            onError = ::onGetActorMoviesError,
            onStart = ::onLoading,
            onFinally = ::onFinally,
        )
    }

    private fun onGetActorMoviesSuccess(movies: List<Movie>) {
        hideSnackBar()
        updateState { topMoviePicksState ->
            topMoviePicksState.copy(movies = movies.map { it.toUIState() })
        }
    }

    private fun onGetActorMoviesError(throwable: Throwable) {
        when (throwable) {
            is NoInternetException -> showNoInternetSnackBar()
            else -> handleError(throwable)
        }
    }

    override fun mapThrowableToErrorMessage(throwable: Throwable): BaseSnackBarMessage =
        BaseSnackBarMessage.UnknownError
    private fun showNoInternetSnackBar() {
        showSnackBar(
            message = BaseSnackBarMessage.NetworkError,
            actionLabelRes = R.string.retry,
            isSuccess = false,
            durationMillis = Int.MAX_VALUE.toLong(),
        )
    }


    override fun onMovieDetailsClick(movieId: Long) {
        sendEffect(TopMoviePicksEffect.NavigateToMovieDetails(movieId))
    }

    override fun onSaveMovieClick(movieId: Long) {
        updateState {
            it.copy(
                movies =
                    it.movies.map { item ->
                        if (item.id == movieId) item.copy(isSaved = item.isSaved.not()) else item
                    },
            )
        }
        showSnackBar(
            message = SearchSnackBarMessage.SavedItemSuccessfully,
            isSuccess = true,
        )
    }

    override fun onBackClick() {
        sendEffect(TopMoviePicksEffect.NavigateBack)
    }

    override fun onSnackBarActionLabelClick() {
        loadData()
    }

    private fun onLoading() {
        updateState { it.copy(isLoading = true) }
    }

    private fun onFinally() {
        updateState { it.copy(isLoading = false) }
    }
}
