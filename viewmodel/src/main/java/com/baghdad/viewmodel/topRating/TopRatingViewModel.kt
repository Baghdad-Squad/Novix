package com.baghdad.viewmodel.topRating

import com.baghdad.domain.exception.NoInternetException
import com.baghdad.domain.usecase.genre.GetGenresUseCase
import com.baghdad.domain.usecase.topRated.GetMovieTopRatingUseCase
import com.baghdad.domain.usecase.topRated.GetTvShowTopRatingUseCase
import com.baghdad.entity.media.Genre
import com.baghdad.viewmodel.R
import com.baghdad.viewmodel.base.BaseViewModel
import com.baghdad.viewmodel.errorStates.BaseSnackBarMessage

class TopRatingViewModel(
    private val getMovieTopRatingUseCase: GetMovieTopRatingUseCase,
    private val getTvShowTopRatingUseCase: GetTvShowTopRatingUseCase,
    private val getGenresUseCase: GetGenresUseCase,
) : BaseViewModel<TopRatingState, TopRatingEffect>(TopRatingState()),
    TopRatingInteractionListener {
    init {
        loadInitData()
    }

    private fun loadInitData() {
        getMovieGenres()
        fetchMoviesByGenre(null)
    }

    private fun getMovieGenres() {
        tryToExecute(
            callee = { getGenresUseCase.getMovieGenres() },
            onSuccess = ::onGenresFetched,
            onError = ::onError,
            onStart = ::onStart,
            onFinally = ::onFinally,
        )
    }

    private fun getTvShowGenres() {
        tryToExecute(
            callee = { getGenresUseCase.getTvShowGenres() },
            onSuccess = ::onGenresFetched,
            onError = ::onError,
            onStart = ::onStart,
            onFinally = ::onFinally,
        )
    }

    private fun onGenresFetched(genres: List<Genre>) {
        updateState {
            it.copy(
                genres =
                    genres.distinctBy { genre -> genre.id }.map { genre ->
                        genre.toTopRatingGenreUiState()
                    }
            )
        }

    }

    override fun onMovieDetailsClick(movieId: Long) {
        sendEffect(TopRatingEffect.NavigateToMovieDetails(movieId))
    }

    override fun onTvShowDetailsClick(tvShowId: Long) {
        sendEffect(TopRatingEffect.NavigateToTvShowDetails(tvShowId))
    }

    private fun fetchTvShowsByGenre(genreId: Long?) {
        hideSnackBar()
        updateState { it.copy(isLoading = true, selectedTvShowGenreId = genreId) }
        collectPagingFlow(
            loadData = { page ->
                getTvShowTopRatingUseCase.invoke(
                    page = page,
                    genreId = genreId
                )
            },
            onInitialLoadFinished = ::onFinally,
            mapEntityToUiState = { it.toTopRatingTvShowUiState() },
            onFlowCreated = { tvShowsFlow -> updateState { it.copy(tvShowsFlow = tvShowsFlow) } },
            onLoadingChanged = { isLoading ->
                updateState { it.copy(isLoading = isLoading) }
            },
            onInitialLoadError = ::onError
        )
    }

    private fun fetchMoviesByGenre(genreId: Long?) {
        hideSnackBar()
        updateState { it.copy(isLoading = true, selectedMovieGenreId = genreId) }
        collectPagingFlow(
            loadData = { page ->
                getMovieTopRatingUseCase.invoke(
                    genreId = genreId,
                    page = page
                )
            },
            onInitialLoadFinished = ::onFinally,
            mapEntityToUiState = { it.toTopRatingMovieUiState() },
            onFlowCreated = { moviesFlow -> updateState { it.copy(moviesFlow = moviesFlow) } },
            onLoadingChanged = { isLoading ->
                updateState { it.copy(isLoading = isLoading) }
            },
            onInitialLoadError = ::onError
        )
    }

    override fun onGenreClick(genreId: Long?) {
        when (currentState.selectedTab) {
            TopRatingTab.MOVIES -> {
                if (genreId != currentState.selectedMovieGenreId) {
                    updateState { it.copy(selectedMovieGenreId = genreId) }
                    fetchMoviesByGenre(genreId)
                }
            }

            TopRatingTab.TV_SHOWS -> {
                if (genreId != currentState.selectedTvShowGenreId) {
                    updateState { it.copy(selectedTvShowGenreId = genreId) }
                    fetchTvShowsByGenre(genreId)
                }
            }
        }
    }

    override fun onSaveTvShowClick(tvShowId: Long) {
        //         TODO: save logic
    }

    override fun onSaveMovieClick(movieId: Long) {
        //         TODO: save logic
    }

    override fun onBackClick() {
        sendEffect(TopRatingEffect.NavigateBack)
    }

    override fun onSelectedTab(selectedTab: TopRatingTab) {
        if (currentState.selectedTab == selectedTab) return

        updateState {
            it.copy(selectedTab = selectedTab)
        }

        when (selectedTab) {
            TopRatingTab.MOVIES -> {
                getMovieGenres()
                fetchMoviesByGenre(currentState.selectedMovieGenreId)
            }

            TopRatingTab.TV_SHOWS -> {
                getTvShowGenres()
                fetchTvShowsByGenre(currentState.selectedTvShowGenreId)
            }
        }
    }

    private fun onError(throwable: Throwable) {
        when (throwable) {
            is NoInternetException -> showNoInternetSnackBar()
            else -> handleError(throwable)
        }
    }

    private fun showNoInternetSnackBar() {
        showSnackBar(
            message = BaseSnackBarMessage.NetworkError,
            actionLabelRes = R.string.retry,
            isSuccess = false,
            durationMillis = Int.MAX_VALUE.toLong(),
        )
    }

    override fun onSnackBarActionLabelClick() {
        if (currentState.selectedTab == TopRatingTab.MOVIES) {
            getMovieGenres()
            fetchMoviesByGenre(currentState.selectedMovieGenreId)
        } else {
            getTvShowGenres()
            fetchTvShowsByGenre(currentState.selectedTvShowGenreId)
        }
    }

    private fun onStart() {
        updateState { it.copy(isLoading = true) }
    }

    private fun onFinally() {
        updateState { it.copy(isLoading = false) }
    }

    override fun mapThrowableToErrorMessage(throwable: Throwable): BaseSnackBarMessage =
        BaseSnackBarMessage.UnknownError

}

