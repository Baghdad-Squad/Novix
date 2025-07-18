package com.baghdad.viewmodel.search

import com.baghdad.domain.model.search.RecentlyViewed
import com.baghdad.domain.usecase.genre.GetGenresUseCase
import com.baghdad.domain.usecase.recentlyViewed.AddRecentlyViewedUseCase
import com.baghdad.domain.usecase.recentlyViewed.DeleteAllRecentlyViewedUseCase
import com.baghdad.domain.usecase.recentlyViewed.GetRecentlyViewedUseCase
import com.baghdad.domain.usecase.search.DeleteAllRecentSearchesUseCase
import com.baghdad.domain.usecase.search.DeleteRecentSearchUseCase
import com.baghdad.domain.usecase.search.GetRecentSearchesUseCase
import com.baghdad.domain.usecase.search.SearchActorsUseCase
import com.baghdad.domain.usecase.search.SearchMoviesUseCase
import com.baghdad.domain.usecase.search.SearchTvShowsUseCase
import com.baghdad.entity.media.Genre
import com.baghdad.entity.search.RecentSearch
import com.baghdad.viewmodel.base.BaseViewModel
import com.baghdad.viewmodel.errorStates.BaseSnackBarMessage
import com.baghdad.viewmodel.errorStates.SearchScreenBaseSnackBarMessages
import com.baghdad.viewmodel.search.SearchScreenState.GenreUiState
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flowOf

class SearchViewModel(
    private val getGenresUseCase: GetGenresUseCase,
    private val getRecentSearchesUseCase: GetRecentSearchesUseCase,
    private val getRecentlyViewedUseCase: GetRecentlyViewedUseCase,
    private val addRecentlyViewedUseCase: AddRecentlyViewedUseCase,
    private val deleteAllRecentlyViewedUseCase: DeleteAllRecentlyViewedUseCase,
    private val deleteAllRecentSearchesUseCase: DeleteAllRecentSearchesUseCase,
    private val deleteRecentSearchUseCase: DeleteRecentSearchUseCase,
    private val searchMoviesUseCase: SearchMoviesUseCase,
    private val searchTvShowsUseCase: SearchTvShowsUseCase,
    private val searchActorsUseCase: SearchActorsUseCase
) : BaseViewModel<SearchScreenState, SearchScreenEffect>(SearchScreenState()),
    SearchInteractionListener {
    private var searchJob: Job? = null

    init {
        getRecentSearches()
        getRecentViewed()
        getMovieGenres()
        getTvShowGenres()
    }

    override fun mapThrowableToErrorMessage(throwable: Throwable): BaseSnackBarMessage {
        return BaseSnackBarMessage.UnknownError
    }

    override fun onSearchTextChanged(text: String) {
        updateState { it.copy(searchText = text) }
        searchJob?.cancel()

        if (text.isBlank()) {
            clearAllPagingFlows()
            return
        }

        searchJob = tryToExecute(
            onStart = { handleSearchStart() },
            callee = { performSearchByTab(text) },
        )
    }

    private fun performSearchByTab(text: String) {
        when (currentState.selectedSearchTab) {
            SearchScreenState.SearchTab.MOVIES -> searchMovies(text)
            SearchScreenState.SearchTab.TV_SHOWS -> searchTvShows(text)
            SearchScreenState.SearchTab.ACTORS -> searchActors(text)
        }
    }

    private fun searchMovies(text: String) {
        collectPagingFlow(
            loadData = { page ->
                searchMoviesUseCase(
                    query = text,
                    filter = currentState.bottomSheetUiState.moviesFilter.toSearchFilter(),
                    page = page
                )
            },
            onInitialLoadFinished = ::onFinally,
            mapEntityToUiState = { it.toMovieUI() },
            onFlowCreated = { moviesFlow -> updateState { it.copy(moviesFlow = moviesFlow) } }
        )
    }

    private fun searchTvShows(text: String) {
        collectPagingFlow(
            loadData = { page ->
                searchTvShowsUseCase(
                    query = text,
                    filter = currentState.bottomSheetUiState.tvShowsFilter.toSearchFilter(),
                    page = page
                )
            },
            onInitialLoadFinished = ::onFinally,
            mapEntityToUiState = { it.toTvShowUI() },
            onFlowCreated = { tvShowsFlow -> updateState { it.copy(tvShowsFlow = tvShowsFlow) } }
        )
    }

    private fun searchActors(text: String) {
        collectPagingFlow(
            loadData = { page ->
                searchActorsUseCase(
                    query = text,
                    page = page
                )
            },
            onInitialLoadFinished = ::onFinally,
            mapEntityToUiState = { it.toActorUI() },
            onFlowCreated = { actorsFlow -> updateState { it.copy(actorsFlow = actorsFlow) } }
        )
    }

    private fun clearAllPagingFlows() {
        updateState {
            it.copy(
                moviesFlow = flowOf(),
                tvShowsFlow = flowOf(),
                actorsFlow = flowOf()
            )
        }
    }

    private suspend fun handleSearchStart() {
        delay(SEARCH_DEBOUNCED_DELAY)
        onLoading()
    }

    private fun getRecentViewed() {
        tryToCollect(
            flowProvider = { getRecentlyViewedUseCase() },
            onNewValue = ::onGetRecentViewedSuccess,
        )
    }

    private fun onGetRecentViewedSuccess(recentlyViewed: List<RecentlyViewed>) {
        val recentViewedUiState = recentlyViewed.map { it.toRecentlyViewedUI() }
        updateState { searchScreenState ->
            searchScreenState.copy(
                recentViewed = recentViewedUiState.distinctBy { it.id })
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
                recentSearch = recentSearchUiState.distinctBy { it.query })
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

    private fun getRecentlyViewed() {
        tryToCollect(
            flowProvider = { getRecentlyViewedUseCase.invoke() },
            onNewValue = { newValues ->
                updateState {
                    it.copy(recentViewed = newValues.map { it.toRecentlyViewedUI() }.distinctBy {
                        it.id
                    })
                }
            },
            onError = {},
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

    override fun onClearRecentlyViewedClick() {
        tryToExecute(
            callee = { deleteAllRecentlyViewedUseCase() },
            onSuccess = { onClearRecentViewedSuccess() },
            onStart = ::onLoading,
            onFinally = ::onFinally,
        )
    }

    override fun onSaveRecentlyViewedClick(id: Long) {
        updateState {
            it.copy(
                recentViewed = it.recentViewed.map { item ->
                    if (item.id == id) item.copy(isSaved = item.isSaved.not()) else item
                })
        }
        showSnackBar(
            message = SearchScreenBaseSnackBarMessages.SavedItemSuccessfully, isSuccess = true
        )
    }

    private fun onClearRecentViewedSuccess() {
        showSnackBar(
            message = SearchScreenBaseSnackBarMessages.RemovedItemSuccessfully, isSuccess = true
        )
        updateState {
            it.copy(
                recentViewed = emptyList()
            )
        }
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
            message = SearchScreenBaseSnackBarMessages.RemovedItemSuccessfully, isSuccess = true
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
            message = SearchScreenBaseSnackBarMessages.RemovedItemSuccessfully, isSuccess = true
        )
    }


    override fun onRecentSearchItemClick(id: Long) {
        val searchText = currentState.recentSearch.find { it.id == id }?.query ?: ""
        updateState { it.copy(searchText = searchText) }
        onSearchTextChanged(searchText)
    }

    override fun onFilterCloseIconClick() {
        updateState {
            it.copy(
                bottomSheetUiState = it.bottomSheetUiState.copy(
                    isBottomSheetVisible = false
                )
            )
        }
    }

    override fun onFilterClearClick() {
        if (currentState.selectedSearchTab == SearchScreenState.SearchTab.MOVIES) {
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

    override fun onApplyFilterClick() {
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
        if (currentState.selectedSearchTab == SearchScreenState.SearchTab.MOVIES) {
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
        if (currentState.selectedSearchTab == SearchScreenState.SearchTab.MOVIES) {
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
                        minimumYear = range.start.toInt(), maximumYear = range.endInclusive.toInt()
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
                        minimumYear = range.start.toInt(), maximumYear = range.endInclusive.toInt()
                    )
                )
            )
        }
    }

    override fun onGenreSelected(genre: GenreUiState) {
        if (currentState.selectedSearchTab == SearchScreenState.SearchTab.MOVIES) {
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

    override fun onSelectedSearchTabChanged(selectedTab: SearchScreenState.SearchTab) {
        updateState { it.copy(selectedSearchTab = selectedTab) }
        performSearchByTab(currentState.searchText)
    }

    override fun onRecentlyViewedClick(id: Long, imageUrl: String) {
        val recentlyViewed = currentState.recentViewed.find { it.id == id }
        recentlyViewed?.let {
            if (it.contentType == RecentlyViewed.ContentType.MOVIE) {
                onMovieItemClick(id, imageUrl)
            } else {
                onTvShowItemClick(id, imageUrl)
            }
        }
    }

    override fun onMovieItemClick(
        contentId: Long,
        contentImageUrl: String
    ) {
        tryToExecute(
            callee = {
                addRecentlyViewedUseCase(
                    contentId, contentImageUrl, RecentlyViewed.ContentType.MOVIE
                )
            },
            onSuccess = { onAddRecentlyViewedMovieSuccess(contentId) },
            onStart = ::onLoading,
            onFinally = ::onFinally
        )
    }

    private fun onAddRecentlyViewedMovieSuccess(contentId: Long) {
        sendEffect(SearchScreenEffect.NavigateToMovieDetails(contentId))
    }

    override fun onTvShowItemClick(
        contentId: Long,
        contentImageUrl: String
    ) {
        tryToExecute(
            callee = {
                addRecentlyViewedUseCase(
                    contentId, contentImageUrl, RecentlyViewed.ContentType.TV_SHOW
                )
            },
            onSuccess = { onAddRecentlyViewedTvShowSuccess(contentId) },
            onStart = ::onLoading,
            onFinally = ::onFinally
        )
    }

    private fun onAddRecentlyViewedTvShowSuccess(contentId: Long) {
        sendEffect(SearchScreenEffect.NavigateToTvShowDetails(contentId))
    }

    override fun onActorItemClick(id: Long) {
        sendEffect(SearchScreenEffect.NavigateToActorDetails(id))
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
