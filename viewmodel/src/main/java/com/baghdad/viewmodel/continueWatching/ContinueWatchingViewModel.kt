package com.baghdad.viewmodel.continueWatching

import com.baghdad.domain.model.ContinueWatching
import com.baghdad.domain.model.PagedResult
import com.baghdad.domain.usecase.continueWatching.GetAllContinueWatchingByGenreUseCase
import com.baghdad.domain.usecase.continueWatching.GetAllContinueWatchingUseCase
import com.baghdad.domain.usecase.genre.GetGenresUseCase
import com.baghdad.entity.media.Genre
import com.baghdad.viewmodel.base.BaseViewModel
import com.baghdad.viewmodel.errorStates.BaseSnackBarMessage
import kotlinx.coroutines.flow.flowOf

class ContinueWatchingViewModel(
    private val getGenresUseCase: GetGenresUseCase,
    private val getAllContinueWatchingUseCase: GetAllContinueWatchingUseCase,
    private val getAllContinueWatchingByGenreUseCase: GetAllContinueWatchingByGenreUseCase
) : BaseViewModel<ContinueWatchingState, ContinueWatchingScreenEffect>(ContinueWatchingState()),
    ContinueWatchingInteractionListener {
    init {
        getGenres()
        getMedia(0)
    }


    private fun getGenres() {
        tryToExecute(
            { if (currentState.selectedMediaTabIsMovie) getGenresUseCase.getMovieGenres() else getGenresUseCase.getTvShowGenres() },
            ::onGenresFetched,
        )
    }

    private fun getMedia(genreId: Long) {
        collectPagingFlow(
            { page -> onGetMedia(genreId, page) },
            onInitialLoadFinished = ::onFinally,
            mapEntityToUiState = { it.toContinueWatchingUiState() },
            onFlowCreated = { mediaFlow -> updateState { it.copy(mediaFlow) } }
        )
    }

    private suspend fun onGetMedia(genreId: Long, page: Int): PagedResult<ContinueWatching> {
        return if (genreId == 0L) {
            val result = getAllContinueWatchingUseCase(page)
            if (currentState.selectedMediaTabIsMovie) {
                result.copy(data = result.data.filter { it.contentType == ContinueWatching.ContentType.MOVIE })
            } else
                result.copy(data = result.data.filter { it.contentType == ContinueWatching.ContentType.TV_SHOW })
        } else {
            val result = getAllContinueWatchingByGenreUseCase(genreId, page)

            if (currentState.selectedMediaTabIsMovie) {
                result.copy(data = result.data.filter { it.contentType == ContinueWatching.ContentType.MOVIE })
            } else
                result.copy(data = result.data.filter { it.contentType == ContinueWatching.ContentType.TV_SHOW })
        }
    }


    private fun onGenresFetched(
        genres: List<Genre>
    ) {
        updateState {
            it.copy(
                genres = listOf(ContinueWatchingState.GenreUiState(name = "All")) + genres
                    .distinctBy { genre -> genre.id }
                    .map { genre -> genre.toContinueWatchingUiState() })
        }
    }

    override fun mapThrowableToErrorMessage(throwable: Throwable): BaseSnackBarMessage {
        return BaseSnackBarMessage.DefaultMessage
    }

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

    override fun onGenreClick(genreId: Long) {
        updateState {
            it.copy(selectedGenreTab = genreId, isLoading = true, mediaFlow = flowOf())
        }
        getMedia(genreId)
    }

    override fun onSelectedTab(isMovieTab: Boolean) {
        updateState {
            it.copy(selectedMediaTabIsMovie = isMovieTab, isLoading = true, mediaFlow = flowOf(),selectedGenreTab = 0)
        }
        getGenres()
        getMedia(0)
    }
}