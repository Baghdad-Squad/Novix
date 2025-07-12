package com.baghdad.viewmodel.search

import com.baghdad.domain.model.search.SearchFilter
import com.baghdad.domain.model.search.SearchResult
import com.baghdad.domain.usecase.genre.GetGenresUseCase
import com.baghdad.domain.usecase.recentlyViewed.DeleteAllRecentlyViewedUseCase
import com.baghdad.domain.usecase.search.DeleteAllRecentSearchesUseCase
import com.baghdad.domain.usecase.search.DeleteRecentSearchUseCase
import com.baghdad.domain.usecase.search.GetRecentSearchesUseCase
import com.baghdad.domain.usecase.search.SearchUseCase
import com.baghdad.entity.media.Genre
import com.baghdad.entity.search.RecentSearch
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
                recentSearch = recentSearchUiState
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
                    moviesGenres = genres.map { it.toGenreUI() })
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
                    tvShowsGenres = genres.map { it.toGenreUI() })
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
        query = query, filter = createSearchFilter(),
    )

    private fun createSearchFilter() = SearchFilter(
        minimumYear = currentState.bottomSheetUiState.minimumYear,
        maximumYear = currentState.bottomSheetUiState.maximumYear,
        minimumRating = currentState.bottomSheetUiState.rate,
        selectedGenres = currentState.bottomSheetUiState.selectedGenres.map { it.toGenre() })

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
        TODO("Not yet implemented")
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

    override fun onBottomSheetClearClick(
    ) {
        updateState {
            it.copy(
                bottomSheetUiState = it.bottomSheetUiState.copy(
                    minimumYear = 1990, maximumYear = 2025, rate = 0, selectedGenres = emptyList()
                )
            )
        }
    }

    override fun onApplyClick(
    ) {
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
        updateState { it.copy(bottomSheetUiState = it.bottomSheetUiState.copy(rate = rating)) }
    }

    override fun onYearRangeSelected(range: ClosedFloatingPointRange<Float>) {
        updateState {
            it.copy(
                bottomSheetUiState = it.bottomSheetUiState.copy(
                    minimumYear = range.start.toInt(), maximumYear = range.endInclusive.toInt()
                )
            )
        }
    }

    override fun onGenreSelected(genre: GenreUiState) {
        val currentGenres = currentState.bottomSheetUiState.selectedGenres
        val updatedGenres = if (currentGenres.contains(genre)) {
            currentGenres - genre
        } else {
            currentGenres + genre
        }
        updateState { state ->
            state.copy(
                bottomSheetUiState = state.bottomSheetUiState.copy(
                    selectedGenres = updatedGenres
                )
            )
        }
    }

    override fun onSelectedSearchTabChanged(selectedTab: SearchTab) {
        updateState { it.copy(selectedSearchTab = selectedTab) }
    }

    override fun onRecentlyViewedClick(id: Long) {
        TODO("Not yet implemented")
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
