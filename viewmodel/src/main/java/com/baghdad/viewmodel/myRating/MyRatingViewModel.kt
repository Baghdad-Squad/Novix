package com.baghdad.viewmodel.myRating

import com.baghdad.domain.usecase.mediaRated.GetUserMediaRatedUseCase
import com.baghdad.domain.usecase.movie.DeleteMovieRateUseCase
import com.baghdad.domain.usecase.mediaRated.GetUserRatedMoviesUseCase
import com.baghdad.domain.usecase.tvShow.DeleteTvShowRateUseCase
import com.baghdad.domain.usecase.mediaRated.GetUserRatedTvShowsUseCase
import com.baghdad.viewmodel.base.BaseViewModel
import com.baghdad.viewmodel.errorStates.BaseSnackBarMessage
import dagger.hilt.android.lifecycle.HiltViewModel
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
        when (currentState.selectedMediaTab) {
            MyRatingState.MediaTab.MOVIE -> fetchMovies()
            MyRatingState.MediaTab.TV_SHOW -> fetchTvShows()
            else -> {
                collectPagingFlow(
                    loadData = { page -> getUserMediaRatedUseCase(page, PAGE_SIZE) },
                    onInitialLoadFinished = ::onFinally,
                    mapEntityToUiState = { it.toMediaItemUiState() },
                    onFlowCreated = { mediaFlow -> updateState { it.copy(mediaFlow = mediaFlow) } },
                    onLoadingChanged = ::onGetMediaLoadingChanged,
                )
            }
        }
    }

    private fun fetchMovies() {
        collectPagingFlow(
            loadData = { page -> getUserRatedMoviesUseCase(page, PAGE_SIZE) },
            onInitialLoadFinished = ::onFinally,
            mapEntityToUiState = { it.toMediaItemUiState() },
            onFlowCreated = { mediaFlow -> updateState { it.copy(mediaFlow = mediaFlow) } },
            onLoadingChanged = ::onGetMediaLoadingChanged,
        )
    }

    private fun fetchTvShows() {
        collectPagingFlow(
            loadData = { page -> getUserRatedTvShowsUseCase(page, PAGE_SIZE) },
            onInitialLoadFinished = ::onFinally,
            mapEntityToUiState = { it.toMediaItemUiState() },
            onFlowCreated = { mediaFlow -> updateState { it.copy(mediaFlow = mediaFlow) } },
            onLoadingChanged = ::onGetMediaLoadingChanged,
        )
    }

    private fun onGetMediaLoadingChanged(isLoading: Boolean) {
        updateState { it.copy(isLoading = isLoading) }
    }

    override fun mapThrowableToErrorMessage(throwable: Throwable): BaseSnackBarMessage {
        return BaseSnackBarMessage.DefaultMessage
    }

    override fun onBackClick() {
        sendEffect(MyRatingEffect.NavigateBack)
    }

    override fun onSnackBarActionLabelClick() {
        fetchUserRatedMedia()
    }

    override fun onMediaClick(
        mediaId: Long,
        contentType: MyRatingState.ContentType
    ) {
        when (contentType) {
            MyRatingState.ContentType.MOVIE -> sendEffect(
                MyRatingEffect.NavigateToMovieDetails(
                    mediaId
                )
            )

            MyRatingState.ContentType.TV_SHOW -> sendEffect(
                MyRatingEffect.NavigateToTvShowDetails(
                    mediaId
                )
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

    private fun onDelete(cally: suspend () -> Unit) {
        tryToExecute(
            { cally() },
            { onDeletedSuccess() },
        )
    }

    private fun onDeletedSuccess() {
        showSnackBar(
            message = BaseSnackBarMessage.RatedRemoveSuccessfully,
            isSuccess = true

        )
        updateState {
            currentState.copy(isLoading = true)
        }
        fetchUserRatedMedia()
    }

    private fun onFinally() {
        updateState {
            currentState.copy(isLoading = false)
        }
    }

    private companion object {
        const val PAGE_SIZE = 20
    }

}