package com.baghdad.viewmodel.continueWatching

import com.baghdad.domain.model.ContinueWatching
import com.baghdad.domain.model.PagedResult
import com.baghdad.domain.usecase.continueWatching.GetAllContinueWatchingByGenreUseCase
import com.baghdad.domain.usecase.continueWatching.GetAllContinueWatchingUseCase
import com.baghdad.domain.usecase.genre.GetGenresUseCase
import com.baghdad.entity.media.Genre
import com.baghdad.viewmodel.base.BaseViewModel
import com.baghdad.viewmodel.errorStates.BaseSnackBarMessage
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOf

class ContinueWatchingViewModel(
    private val getGenresUseCase: GetGenresUseCase,
    private val getAllContinueWatchingUseCase: GetAllContinueWatchingUseCase,
    private val getAllContinueWatchingByGenreUseCase: GetAllContinueWatchingByGenreUseCase,
    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.IO
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
            dispatcher = defaultDispatcher
        )
    }

    private fun getMedia(genreId: Long?) {
        collectPagingFlow(
            { page -> onGetMedia(genreId, page) },
            onInitialLoadFinished = ::onFinally,
            mapEntityToUiState = { it.toContinueWatchingUiState() },
            onFlowCreated = { mediaFlow -> updateState { it.copy(mediaFlow) } },
        )
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

        val filteredData = result.data.filter { item ->
            (item.contentType == ContinueWatching.ContentType.MOVIE && currentState.selectedMediaTabIsMovie) ||
                    (item.contentType == ContinueWatching.ContentType.TV_SHOW && !currentState.selectedMediaTabIsMovie)
        }

        return result.copy(data = filteredData)
    }

    private fun onGenresFetched(genres: List<Genre>) {
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
        contentType: ContinueWatchingState.ContinueWatchingMovieUiState.ContentType
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
                            mediaFlow = flowOf()
                        )
                    }
                }
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
                            mediaFlow = flowOf()
                        )
                    }
                }
            )
        }
    }

    private fun handleGenreSelection(
        currentSelectedId: Long?,
        newGenreId: Long?,
        update: (Long?) -> Unit
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
}
