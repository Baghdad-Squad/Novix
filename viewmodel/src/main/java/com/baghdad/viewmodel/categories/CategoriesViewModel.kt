package com.baghdad.viewmodel.categories

import com.baghdad.domain.exception.NoInternetException
import com.baghdad.domain.usecase.movie.GetMovieGenresUseCase
import com.baghdad.domain.usecase.tvShow.GetTvShowGenresUseCase
import com.baghdad.entity.media.Genre
import com.baghdad.viewmodel.R
import com.baghdad.viewmodel.base.BaseViewModel
import com.baghdad.viewmodel.errorStates.BaseSnackBarMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

@HiltViewModel
class CategoriesViewModel @Inject constructor(
    private val getGenreMoviesUseCase: GetMovieGenresUseCase,
    private val getGenreTvShowsUseCase: GetTvShowGenresUseCase,
    private val ioDispatcher: CoroutineDispatcher,
) : BaseViewModel<CategoriesState, CategoriesEffect>(CategoriesState()),
    CategoriesInteractionListener {

    init {
        getGenreMovies()
        getGenreTvShows()
    }

    override fun mapThrowableToErrorMessage(throwable: Throwable): BaseSnackBarMessage {
        return BaseSnackBarMessage.UnknownError
    }

    private fun getGenreMovies() {
        tryToExecute(
            callee = { getGenreMoviesUseCase.getMovieGenres() },
            onStart = ::onStart,
            onSuccess = ::onGetMovieGenresSuccess,
            onError = ::onGetGenreMoviesError,
            dispatcher = ioDispatcher,
            onFinally = ::onFinally
        )
    }

    private fun getGenreTvShows() {
        tryToExecute(
            callee = { getGenreTvShowsUseCase.getTvShowGenres() },
            onStart = ::onStart,
            onSuccess = ::onGetTvShowGenresSuccess,
            onError = ::onGetGenreMoviesError,
            dispatcher = ioDispatcher,
            onFinally = ::onFinally
        )
    }

    private fun onGetMovieGenresSuccess(movieGenre: List<Genre>) {
        val movieIds = MovieCategory.entries.map { it.id }.toSet()
        val filteredAndMappedGenres = mapSupportedGenres(movieGenre, movieIds)
        updateState { genre ->
            genre.copy(movieGenres = filteredAndMappedGenres)
        }
    }

    private fun onGetTvShowGenresSuccess(tvShowGenre: List<Genre>) {
        val tvShowIds = TvShowCategory.entries.map { it.id }.toSet()
        val filteredAndMappedGenres = mapSupportedGenres(tvShowGenre, tvShowIds)
        updateState { genre ->
            genre.copy(tvShowGenres = filteredAndMappedGenres)
        }
    }

    private fun mapSupportedGenres(
        genres: List<Genre>,
        availableCategoryIds: Set<Long>
    ): List<CategoriesState.GenreUiState> {
        return genres.filter { it.id in availableCategoryIds }.map { it.toGenreUiState() }
    }


    private fun onGetGenreMoviesError(throwable: Throwable) {
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

    override fun onTabSelected(tab: CategoriesState.CategoriesTab) {
        if (currentState.selectedCategoriesTab == tab) return
        updateState { it.copy(selectedCategoriesTab = tab) }
        selectedCategoryByTab()
    }

    private fun selectedCategoryByTab() {
        when (currentState.selectedCategoriesTab) {
            CategoriesState.CategoriesTab.MOVIES -> getGenreMovies()
            CategoriesState.CategoriesTab.TV_SHOWS -> getGenreTvShows()
        }
    }

    override fun onCategoryMovieClick(categoryId: Long) {
        sendEffect(CategoriesEffect.NavigateToCategoryMovies(categoryId))
    }

    override fun onCategoryTvShowClick(categoryId: Long) {
        sendEffect(CategoriesEffect.NavigateToCategoryTVShows(categoryId))
    }

    private fun onFinally() {
        updateState { it.copy(isLoading = false) }

    }

    private fun onStart() {
        updateState { it.copy(isLoading = true) }

    }
}
