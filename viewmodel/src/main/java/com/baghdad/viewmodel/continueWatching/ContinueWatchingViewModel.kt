package com.baghdad.viewmodel.continueWatching

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
        getMovies(0)
    }


    private fun getGenres() {
        tryToExecute(
            { getGenresUseCase.getMovieGenres() + getGenresUseCase.getTvShowGenres() },
            ::onGenresFetched,
        )
    }

    private fun getMovies(genreId: Long) {
        collectPagingFlow(
            { page ->
                if (genreId == 0L) getAllContinueWatchingUseCase(
                    1,
                    page
                ) else getAllContinueWatchingByGenreUseCase(genreId, page)
            },
            onInitialLoadFinished = ::onFinally,
            mapEntityToUiState = { it.toContinueWatchingUiState() },
            onFlowCreated = { mediaFlow -> updateState { it.copy(mediaFlow) } }
        )
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
        TODO("Not yet implemented")
    }

    override fun onBackClick() {
        sendEffect(ContinueWatchingScreenEffect.NavigateBack)
    }

    override fun onMediaClick(mediaId: Long, contentType: ContinueWatchingState.ContinueWatchingMovieUiState.ContentType) {
        if (contentType == ContinueWatchingState.ContinueWatchingMovieUiState.ContentType.MOVIE){
            sendEffect(ContinueWatchingScreenEffect.NavigateToMovieDetails(mediaId))
        }else{
            sendEffect(ContinueWatchingScreenEffect.NavigateToTvShowDetails(mediaId))
        }
    }

    private fun onFinally() {
        updateState { it.copy(isLoading = false) }
    }

    override fun onGenreClick(genreId: Long) {
        updateState {
            it.copy(selectedTab = genreId, isLoading = true, mediaFlow = flowOf())
        }
        getMovies(genreId)
    }
}