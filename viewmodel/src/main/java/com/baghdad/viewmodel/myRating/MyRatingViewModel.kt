package com.baghdad.viewmodel.myRating

import androidx.paging.PagingData
import com.baghdad.domain.exception.NoInternetException
import com.baghdad.domain.usecase.mediaRated.GetUserMediaRatedUseCase
import com.baghdad.domain.usecase.mediaRated.GetUserRatedMoviesUseCase
import com.baghdad.domain.usecase.mediaRated.GetUserRatedTvShowsUseCase
import com.baghdad.domain.usecase.movie.DeleteMovieRateUseCase
import com.baghdad.domain.usecase.tvShow.DeleteTvShowRateUseCase
import com.baghdad.viewmodel.R
import com.baghdad.viewmodel.base.BaseViewModel
import com.baghdad.viewmodel.errorStates.BaseSnackBarMessage
import com.baghdad.viewmodel.myRating.MyRatingState.MediaItemUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class MyRatingViewModel @Inject constructor(
    private val getUserRatedMoviesUseCase: GetUserRatedMoviesUseCase,
    private val getUserRatedTvShowsUseCase: GetUserRatedTvShowsUseCase,
    private val deleteMovieRateUseCase: DeleteMovieRateUseCase,
    private val deleteTvShowRateUseCase: DeleteTvShowRateUseCase,
    private val getUserMediaRatedUseCase: GetUserMediaRatedUseCase
) : BaseViewModel<MyRatingState, MyRatingEffect>(MyRatingState()),
    MyRatingInteractionListener {

    init {
        fetchUserRatedMedia()
    }

    private fun fetchUserRatedMedia() {
        hideSnackBar()
        when (currentState.selectedMediaTab) {
            MyRatingState.MediaTab.MOVIE -> fetchMovies()
            MyRatingState.MediaTab.TV_SHOW -> fetchTvShows()
            else -> {
                collectPagingFlow(
                    loadData = { page -> getUserMediaRatedUseCase(page, PAGE_SIZE) },
                    onInitialLoadFinished = ::onFinally,
                    onInitialLoadError = ::onError,
                    mapEntityToUiState = { it.toUiState() },
                    onFlowCreated = ::onFlowCreated,
                    onLoadingChanged = ::onGetMediaLoadingChanged
                )
            }
        }
    }

    private fun fetchMovies() {
        hideSnackBar()
        collectPagingFlow(
            loadData = { page -> getUserRatedMoviesUseCase(page, PAGE_SIZE) },
            onInitialLoadFinished = ::onFinally,
            onInitialLoadError = ::onError,
            mapEntityToUiState = { it.toUiState() },
            onFlowCreated = ::onFlowCreated,
            onLoadingChanged = ::onGetMediaLoadingChanged
        )
    }

    private fun fetchTvShows() {
        hideSnackBar()
        collectPagingFlow(
            loadData = { page -> getUserRatedTvShowsUseCase(page, PAGE_SIZE) },
            onInitialLoadFinished = ::onFinally,
            onInitialLoadError = ::onError,
            mapEntityToUiState = { it.toUiState() },
            onFlowCreated = ::onFlowCreated,
            onLoadingChanged = ::onGetMediaLoadingChanged
        )
    }

    private fun onGetMediaLoadingChanged(isLoading: Boolean) {
        updateState { it.copy(isLoading = isLoading) }
    }

    override fun mapThrowableToErrorMessage(throwable: Throwable): BaseSnackBarMessage {
        return BaseSnackBarMessage.UnknownError
    }

    override fun onBackClick() {
        sendEffect(MyRatingEffect.NavigateBack)
    }

    private fun onFlowCreated(mediaFlow: Flow<PagingData<MediaItemUiState>>) {
        updateState { it.copy(mediaFlow = mediaFlow) }
    }

    override fun onSnackBarActionLabelClick() {
        refreshMediaList()
    }

    private fun refreshMediaList() {
        hideSnackBar()
        fetchUserRatedMedia()
    }


    override fun onMediaClick(
        mediaId: Long,
        contentType: MyRatingState.ContentType
    ) {
        when (contentType) {
            MyRatingState.ContentType.MOVIE -> sendEffect(
                MyRatingEffect.NavigateToMovieDetails(movieId = mediaId)
            )

            MyRatingState.ContentType.TV_SHOW -> sendEffect(
                MyRatingEffect.NavigateToTvShowDetails(tvShowId = mediaId)
            )
        }
    }

    override fun onMediaTabClick(mediaTab: MyRatingState.MediaTab?) {
        updateState {
            currentState.copy(selectedMediaTab = mediaTab, isLoading = true)
        }
        fetchUserRatedMedia()
    }

    override fun onDeleteClick(mediaId: Long, contentType: MyRatingState.ContentType) {
        when (contentType) {
            MyRatingState.ContentType.MOVIE -> onDelete { deleteMovieRateUseCase(mediaId) }
            MyRatingState.ContentType.TV_SHOW -> onDelete { deleteTvShowRateUseCase(mediaId) }
        }
    }

    private fun onDelete(action: suspend () -> Unit) {
        tryToExecute(
            callee = { action() },
            onSuccess = { onDeletedSuccess() },
            onStart = ::onStart,
            onFinally = ::onFinally,
            onError = ::onError
        )
    }

    private fun onDeletedSuccess() {
        showSnackBar(
            message = BaseSnackBarMessage.RatedRemoveSuccessfully,
            isSuccess = true
        )
        fetchUserRatedMedia()
    }

    private fun showNoInternetSnackBar() {
        showSnackBar(
            message = BaseSnackBarMessage.NetworkError,
            actionLabelRes = R.string.retry,
            isSuccess = false,
            durationMillis = Int.MAX_VALUE.toLong(),
        )
    }


    private fun onError(throwable: Throwable) {
        when (throwable) {
            is NoInternetException -> showNoInternetSnackBar()
            else -> handleError(throwable)
        }
    }


    private fun onStart() {
        updateState {
            it.copy(isLoading = true)
        }
    }

    private fun onFinally() {
        updateState {
            it.copy(isLoading = false)
        }
    }

    private companion object {
        const val PAGE_SIZE = 20
    }
}