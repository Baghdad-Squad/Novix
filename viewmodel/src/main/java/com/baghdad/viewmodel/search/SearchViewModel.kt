package com.baghdad.viewmodel.search

import com.baghdad.domain.model.search.RecentSearch
import com.baghdad.domain.model.search.SearchResult
import com.baghdad.domain.usecase.genre.GetGenresUseCase
import com.baghdad.domain.usecase.recentlyViewed.DeleteAllRecentlyViewedUseCase
import com.baghdad.domain.usecase.search.DeleteAllRecentSearchesUseCase
import com.baghdad.domain.usecase.search.DeleteRecentSearchUseCase
import com.baghdad.domain.usecase.search.GetRecentSearchesUseCase
import com.baghdad.domain.usecase.search.SearchUseCase
import com.baghdad.entity.media.Genre
import com.baghdad.viewmodel.base.BaseViewModel
import com.baghdad.viewmodel.errorStates.BaseErrorState
import com.baghdad.viewmodel.search.SearchScreenState.GenreUiState
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay

class SearchViewModel(
    private val getGenresUseCase: GetGenresUseCase,
    private val getRecentSearchesUseCase: GetRecentSearchesUseCase,
    private val deleteAllRecentlyViewedUseCase: DeleteAllRecentlyViewedUseCase,
    private val deleteAllRecentSearchesUseCase: DeleteAllRecentSearchesUseCase,
    private val deleteRecentSearchUseCase: DeleteRecentSearchUseCase,
    private val searchUseCase: SearchUseCase
) : BaseViewModel<SearchScreenState, SearchScreenEffect>(SearchScreenState()),
    SearchInteractionListener {

    init {
        getRecentSearches()
        getMovieGenres()
        getTvShowGenres()
    }

    private var searchJob: Job? = null
    override fun handleError(baseErrorState: BaseErrorState) {
        updateState {
            it.copy(baseErrorState = baseErrorState, isLoading = false)
        }
    }

    override fun mapThrowableToErrorState(throwable: Throwable): BaseErrorState {
        return when (throwable) {
            // TODO: Handle specific exceptions
            else -> BaseErrorState.ServerError
        }
    }

    private fun getRecentSearches() {
        tryToCollect(
            flowProvider = { getRecentSearchesUseCase() },
            onNewValue = ::onGetRecentSearchesSuccess,
        )
    }

    private fun onGetRecentSearchesSuccess(recentSearches: List<RecentSearch>) {
        val recentSearchUiState = recentSearches.map { it.toRecentSearchUI() }
        updateState { searchScreenState ->
            searchScreenState.copy(
                recentSearch = recentSearchUiState.distinctBy { it.query }
            )
        }
    }

    private fun getMovieGenres() {
        tryToExecute(
            callee = { getGenresUseCase.getMovieGenres() },
            onSuccess = ::onGetMovieGenresSuccess,
            onFinally = ::onFinally
        )
    }

    private fun onGetMovieGenresSuccess(genres: List<Genre>) {
        updateState { searchScreenState ->
            searchScreenState.copy(
                bottomSheetUiState = searchScreenState.bottomSheetUiState.copy(
                    moviesFilter = searchScreenState.bottomSheetUiState.moviesFilter.copy(
                        allGenres = genres.map { it.toGenreUI() })
                )
            )
        }
    }

    private fun getTvShowGenres() {
        tryToExecute(
            callee = { getGenresUseCase.getTvShowGenres() },
            onSuccess = ::onGetTvShowGenresSuccess,
            onFinally = ::onFinally
        )
    }

    private fun onGetTvShowGenresSuccess(genres: List<Genre>) {
        updateState { searchScreenState ->
            searchScreenState.copy(
                bottomSheetUiState = searchScreenState.bottomSheetUiState.copy(
                    tvShowsFilter = searchScreenState.bottomSheetUiState.tvShowsFilter.copy(
                        allGenres = genres.map { it.toGenreUI() })
                )
            )
        }
    }

    override fun onSearchTextChanged(text: String) {
        updateState { it.copy(searchText = text) }
        searchJob?.cancel()
        if (text.isNotBlank()) {
            searchJob = tryToExecute(
                onStart = {
                    delay(SEARCH_DEBOUNCED_DELAY)
                    onLoading()
                },
                callee = { performSearch(text) },
                onSuccess = ::onSearchSuccess,
                onFinally = ::onFinally
            )
        } else {
            clearSearchResults()
        }
    }

    private suspend fun performSearch(query: String) = searchUseCase(
        query = query,
        moviesFilter = currentState.bottomSheetUiState.moviesFilter.toSearchFilter(),
        tvShowsFilter = currentState.bottomSheetUiState.tvShowsFilter.toSearchFilter()
    )

    private fun clearSearchResults() {
        updateState {
            it.copy(
                movies = emptyList(), tvShows = emptyList(), actors = emptyList()
            )
        }
    }

    private fun onSearchSuccess(searchResult: SearchResult) {
        val movieUiState = searchResult.movies.map { it.toMovieUI() }
        val actorsUiState = searchResult.actors.map { it.toActorUI() }
        val tvShowUiState = searchResult.tvShows.map { it.toTvShowUI() }
        updateState {
            it.copy(
                movies = movieUiState, tvShows = tvShowUiState, actors = actorsUiState
            )
        }
    }

    override fun onClearRecentlyViewedClick() {
        tryToExecute(
            callee = { deleteAllRecentlyViewedUseCase() },
            onSuccess = { onClearRecentWatchSuccess() },
            onStart = ::onLoading,
            onFinally = ::onFinally,
        )
    }

    override fun onSavedRecentlyViewedClick(id: Long) {
//        TODO("Not yet implemented")
    }

    private fun onClearRecentWatchSuccess() {
        showSnackBar(
            message = "SuccessStates.Clear", isSuccess = true
        )
    }

    override fun onClearRecentSearchClick() {
        tryToExecute(
            callee = { deleteAllRecentSearchesUseCase() },
            onSuccess = { onClearRecentSearchSuccess() },
            onStart = ::onLoading,
            onFinally = ::onFinally,
        )
    }

    private fun onClearRecentSearchSuccess() {
        showSnackBar(
            message = "SuccessStates.Clear", isSuccess = true
        )
    }

    override fun onRemoveRecentSearchItemClick(id: Long) {
        tryToExecute(
            callee = { deleteRecentSearchUseCase(id) },
            onSuccess = { onRemoveRecentSearchItemSuccess() },
            onStart = ::onLoading,
            onFinally = ::onFinally,
        )
    }

    private fun onRemoveRecentSearchItemSuccess() {
        showSnackBar(
            message = "SuccessStates.Clear", isSuccess = true
        )
    }


    override fun onRecentSearchItemClick(id: Long) {
        val searchText = currentState.recentSearch.find { it.id == id }?.query ?: ""
        updateState { it.copy(searchText = searchText) }
        onSearchTextChanged(searchText)
    }

    override fun onBottomSheetCloseClick() {
        updateState {
            it.copy(
                bottomSheetUiState = it.bottomSheetUiState.copy(
                    isBottomSheetVisible = false
                )
            )
        }
    }

    override fun onBottomSheetClearClick() {
        if (currentState.selectedSearchTab == SearchTab.MOVIES) {
            resetMoviesFilter()
        } else {
            resetTvShowsFilter()
        }
    }

    private fun resetMoviesFilter() {
        updateState {
            it.copy(
                bottomSheetUiState = it.bottomSheetUiState.copy(
                    moviesFilter = SearchScreenState.SearchFilterUiState()
                )
            )
        }
    }

    private fun resetTvShowsFilter() {
        updateState {
            it.copy(
                bottomSheetUiState = it.bottomSheetUiState.copy(
                    tvShowsFilter = SearchScreenState.SearchFilterUiState()
                )
            )
        }
    }

    override fun onApplyClick() {
        updateState {
            it.copy(
                bottomSheetUiState = it.bottomSheetUiState.copy(
                    isBottomSheetVisible = false
                )
            )
        }
        onSearchTextChanged(currentState.searchText)
    }

    override fun onFilterIconClick() {
        updateState {
            it.copy(
                bottomSheetUiState = it.bottomSheetUiState.copy(
                    isBottomSheetVisible = true
                )
            )
        }
    }

    override fun onRatingChanged(rating: Int) {
        if (currentState.selectedSearchTab == SearchTab.MOVIES) {
            updateMoviesFilterRating(rating)
        } else {
            updateTvShowsFilterRating(rating)
        }
    }

    private fun updateMoviesFilterRating(rating: Int) {
        updateState {
            it.copy(
                bottomSheetUiState = it.bottomSheetUiState.copy(
                    moviesFilter = it.bottomSheetUiState.moviesFilter.copy(minimumRating = rating)
                )
            )
        }
    }

    private fun updateTvShowsFilterRating(rating: Int) {
        updateState {
            it.copy(
                bottomSheetUiState = it.bottomSheetUiState.copy(
                    tvShowsFilter = it.bottomSheetUiState.tvShowsFilter.copy(minimumRating = rating)
                )
            )
        }
    }

    override fun onYearRangeSelected(range: ClosedFloatingPointRange<Float>) {
        if (currentState.selectedSearchTab == SearchTab.MOVIES) {
            updateMoviesFilterYearRange(range)
        } else {
            updateTvShowsFilterYearRange(range)
        }
    }

    private fun updateMoviesFilterYearRange(range: ClosedFloatingPointRange<Float>) {
        updateState {
            it.copy(
                bottomSheetUiState = it.bottomSheetUiState.copy(
                    moviesFilter = it.bottomSheetUiState.moviesFilter.copy(
                        minimumYear = range.start.toInt(),
                        maximumYear = range.endInclusive.toInt()
                    )
                )
            )
        }
    }

    private fun updateTvShowsFilterYearRange(range: ClosedFloatingPointRange<Float>) {
        updateState {
            it.copy(
                bottomSheetUiState = it.bottomSheetUiState.copy(
                    tvShowsFilter = it.bottomSheetUiState.tvShowsFilter.copy(
                        minimumYear = range.start.toInt(),
                        maximumYear = range.endInclusive.toInt()
                    )
                )
            )
        }
    }

    override fun onGenreSelected(genre: GenreUiState) {
        if (currentState.selectedSearchTab == SearchTab.MOVIES) {
            updateMoviesFilterGenres(genre)
        } else {
            updateTvShowsFilterGenres(genre)
        }
    }

    private fun updateMoviesFilterGenres(selectedGenre: GenreUiState) {
        val currentGenres = currentState.bottomSheetUiState.moviesFilter.selectedGenres
        val updatedGenres = if (currentGenres.contains(selectedGenre)) {
            currentGenres - selectedGenre
        } else {
            currentGenres + selectedGenre
        }
        updateState { state ->
            state.copy(
                bottomSheetUiState = state.bottomSheetUiState.copy(
                    moviesFilter = state.bottomSheetUiState.moviesFilter.copy(
                        selectedGenres = updatedGenres
                    )
                )
            )
        }
    }

    private fun updateTvShowsFilterGenres(selectedGenre: GenreUiState) {
        val currentGenres = currentState.bottomSheetUiState.tvShowsFilter.selectedGenres
        val updatedGenres = if (currentGenres.contains(selectedGenre)) {
            currentGenres - selectedGenre
        } else {
            currentGenres + selectedGenre
        }
        updateState { state ->
            state.copy(
                bottomSheetUiState = state.bottomSheetUiState.copy(
                    tvShowsFilter = state.bottomSheetUiState.tvShowsFilter.copy(
                        selectedGenres = updatedGenres
                    )
                )
            )
        }
    }

    override fun onSelectedSearchTabChanged(selectedTab: SearchTab) {
        updateState { it.copy(selectedSearchTab = selectedTab) }
    }

    override fun onRecentlyViewedClick(id: Long) {
//        TODO("Not yet implemented")
    }

    private fun onLoading() {
        updateState { it.copy(isLoading = true) }
    }

    private fun onFinally() {
        updateState { it.copy(isLoading = false) }
    }

    companion object {
        private const val SEARCH_DEBOUNCED_DELAY = 750L
    }
}
