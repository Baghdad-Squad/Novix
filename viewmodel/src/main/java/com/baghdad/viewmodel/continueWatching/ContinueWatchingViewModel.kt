package com.baghdad.viewmodel.continueWatching

import com.baghdad.domain.exception.NoInternetException
import com.baghdad.domain.model.ContinueWatching
import com.baghdad.domain.model.PagedResult
import com.baghdad.domain.usecase.continueWatching.GetAllContinueWatchingByGenreUseCase
import com.baghdad.domain.usecase.continueWatching.GetAllContinueWatchingUseCase
import com.baghdad.domain.usecase.genre.GetGenresUseCase
import com.baghdad.entity.media.Genre
import com.baghdad.viewmodel.R
import com.baghdad.viewmodel.base.BaseViewModel
import com.baghdad.viewmodel.errorStates.BaseSnackBarMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

@HiltViewModel
class ContinueWatchingViewModel @Inject constructor(
    private val getGenresUseCase: GetGenresUseCase,
    private val getAllContinueWatchingUseCase: GetAllContinueWatchingUseCase,
    private val getAllContinueWatchingByGenreUseCase: GetAllContinueWatchingByGenreUseCase,
) : BaseViewModel<ContinueWatchingState, ContinueWatchingScreenEffect>(ContinueWatchingState()),
    ContinueWatchingInteractionListener {
    init {
        getGenres()
        getMedia(null)
    }

    private fun getGenres() {
        tryToExecute(
            { if (currentState.selectedMediaTabIsMovie) getGenresUseCase.getMovieGenres() else getGenresUseCase.getTvShowGenres() },
            ::onGenresFetched,
            onError = ::onGetGenresError,
        )
    }

    private fun onGetGenresError(throwable: Throwable) {
        when (throwable) {
            is NoInternetException -> showNoInternetSnackBar()
            else -> handleError(throwable)
        }
    }

    private fun showNoInternetSnackBar() {
        updateState {
            it.copy(
                isLoading = true
            )
        }
        showSnackBar(
            message = BaseSnackBarMessage.NetworkError,
            actionLabelRes = R.string.retry,
            isSuccess = false,
            durationMillis = Int.MAX_VALUE.toLong(),
        )
    }

    private fun getMedia(genreId: Long?) {
        collectPagingFlow(
            { page -> onGetMedia(genreId, page) },
            onInitialLoadFinished = ::onFinally,
            mapEntityToUiState = { it.toContinueWatchingUiState() },
            onFlowCreated = { mediaFlow -> updateState { it.copy(mediaFlow) } },
            onLoadingChanged = ::onGetMediaLoadingChanged,
        )
    }

    private fun onGetMediaLoadingChanged(isLoading: Boolean) {
        updateState { it.copy(isLoading = isLoading) }
    }

    private suspend fun onGetMedia(
        genreId: Long?,
        page: Int,
    ): PagedResult<ContinueWatching> {
        val result =
            if (genreId == null) {
                getAllContinueWatchingUseCase(page)
            } else {
                getAllContinueWatchingByGenreUseCase(genreId, page)
            }

        val filteredData =
            result.data.filter { item ->
                (item.contentType == ContinueWatching.ContentType.MOVIE && currentState.selectedMediaTabIsMovie) ||
                    (item.contentType == ContinueWatching.ContentType.TV_SHOW && !currentState.selectedMediaTabIsMovie)
            }

        return result.copy(data = filteredData)
    }

    private fun onGenresFetched(genres: List<Genre>) {
        hideSnackBar()
        updateState {
            it.copy(
                genres =
                    genres
                        .distinctBy { genre -> genre.id }
                        .map { genre -> genre.toContinueWatchingUiState() },
            )
        }
    }

    override fun mapThrowableToErrorMessage(throwable: Throwable): BaseSnackBarMessage = BaseSnackBarMessage.DefaultMessage

    override fun onBackClick() {
        sendEffect(ContinueWatchingScreenEffect.NavigateBack)
    }

    override fun onMediaClick(
        mediaId: Long,
        contentType: ContinueWatchingState.ContinueWatchingMovieUiState.ContentType,
    ) {
        if (contentType == ContinueWatchingState.ContinueWatchingMovieUiState.ContentType.MOVIE) {
            sendEffect(ContinueWatchingScreenEffect.NavigateToMovieDetails(mediaId))
        } else {
            sendEffect(ContinueWatchingScreenEffect.NavigateToTvShowDetails(mediaId))
        }
    }

    private fun onFinally() {
        updateState { it.copy(isLoading = false) }
    }

    override fun onGenreClick(genreId: Long?) {
        if (currentState.selectedMediaTabIsMovie) {
            handleGenreSelection(
                currentSelectedId = currentState.selectedMovieGenreId,
                newGenreId = genreId,
                update = { id ->
                    updateState {
                        it.copy(
                            selectedMovieGenreId = id,
                            isLoading = true,
                            mediaFlow = flowOf(),
                        )
                    }
                },
            )
        } else {
            handleGenreSelection(
                currentSelectedId = currentState.selectedTvShowGenreId,
                newGenreId = genreId,
                update = { id ->
                    updateState {
                        it.copy(
                            selectedTvShowGenreId = id,
                            isLoading = true,
                            mediaFlow = flowOf(),
                        )
                    }
                },
            )
        }
    }

    private fun handleGenreSelection(
        currentSelectedId: Long?,
        newGenreId: Long?,
        update: (Long?) -> Unit,
    ) {
        if (newGenreId != currentSelectedId) {
            update(newGenreId)
            getMedia(newGenreId)
        }
    }

    override fun onSelectedTab(isMovieTab: Boolean) {
        val genreId =
            if (isMovieTab) currentState.selectedMovieGenreId else currentState.selectedTvShowGenreId

        updateState {
            it.copy(
                selectedMediaTabIsMovie = isMovieTab,
                isLoading = true,
                mediaFlow = flowOf(),
            )
        }
        getGenres()
        getMedia(genreId)
    }

    override fun onMovieSaveClick(movieId: Long) {
        // TODO("Implement when save functionality is implemented")
    }

    override fun onSnackBarActionClick() {
        getGenres()
    }
}
