package com.baghdad.viewmodel.search

import com.baghdad.domain.exception.NoInternetException
import com.baghdad.domain.model.search.RecentlyViewed
import com.baghdad.domain.usecase.recentlyViewed.AddRecentlyViewedUseCase
import com.baghdad.domain.usecase.recentlyViewed.DeleteAllRecentlyViewedUseCase
import com.baghdad.domain.usecase.recentlyViewed.GetRecentlyViewedUseCase
import com.baghdad.domain.usecase.search.DeleteAllRecentSearchesUseCase
import com.baghdad.domain.usecase.search.DeleteRecentSearchUseCase
import com.baghdad.domain.usecase.search.GetRecentSearchesUseCase
import com.baghdad.domain.usecase.search.SearchActorsUseCase
import com.baghdad.domain.usecase.search.SearchMoviesUseCase
import com.baghdad.domain.usecase.search.SearchTvShowsUseCase
import com.baghdad.entity.search.RecentSearch
import com.baghdad.viewmodel.R
import com.baghdad.viewmodel.base.BaseViewModel
import com.baghdad.viewmodel.errorStates.BaseSnackBarMessage
import com.baghdad.viewmodel.errorStates.SearchSnackBarMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val getRecentSearchesUseCase: GetRecentSearchesUseCase,
    private val getRecentlyViewedUseCase: GetRecentlyViewedUseCase,
    private val addRecentlyViewedUseCase: AddRecentlyViewedUseCase,
    private val deleteAllRecentlyViewedUseCase: DeleteAllRecentlyViewedUseCase,
    private val deleteAllRecentSearchesUseCase: DeleteAllRecentSearchesUseCase,
    private val deleteRecentSearchUseCase: DeleteRecentSearchUseCase,
    private val searchMoviesUseCase: SearchMoviesUseCase,
    private val searchTvShowsUseCase: SearchTvShowsUseCase,
    private val searchActorsUseCase: SearchActorsUseCase,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : BaseViewModel<SearchScreenState, SearchScreenEffect>(SearchScreenState()),
    SearchInteractionListener {
    init {
        getRecentSearches()
        getRecentViewed()
        observeSearchQueryChanges()
    }

    override fun mapThrowableToErrorMessage(throwable: Throwable): BaseSnackBarMessage = BaseSnackBarMessage.UnknownError

    override fun onSearchTextChanged(text: String) {
        updateState { it.copy(searchText = text, isLoading = true) }
        if (text.trim() == currentState.lastProcessedQuery) {
            updateState { it.copy(isLoading = false) }
            return
        }
    }

    @OptIn(FlowPreview::class)
    private fun observeSearchQueryFlow(): Flow<String> =
        uiState.map { it.searchText.trim() }.distinctUntilChanged().debounce(SEARCH_DEBOUNCED_DELAY)

    private fun observeSearchQueryChanges() {
        tryToCollect(
            dispatcher = dispatcher,
            flowProvider = { observeSearchQueryFlow() },
            onNewValue = { query -> onSearchQueryChangedCollected(query) },
        )
    }

    private fun onSearchQueryChangedCollected(query: String) {
        val processedQuery = query.trim()

        if (processedQuery == currentState.lastProcessedQuery) {
            return
        }

        currentState.lastProcessedQuery = processedQuery

        if (processedQuery.isBlank()) {
            clearAllPagingFlows()
        } else {
            performSearchByTab(processedQuery)
        }
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
                    page = page,
                )
            },
            onInitialLoadError = ::onSearchError,
            onInitialLoadFinished = ::onFinally,
            mapEntityToUiState = { it.toMovieUI() },
            onFlowCreated = { moviesFlow ->
                updateState { it.copy(moviesFlow = moviesFlow) }
                hideSnackBar()
            },
            onLoadingChanged = { isLoading ->
                updateState { it.copy(isLoading = isLoading) }
            },
        )
    }

    private fun searchTvShows(text: String) {
        collectPagingFlow(
            loadData = { page ->
                searchTvShowsUseCase(
                    query = text,
                    page = page,
                )
            },
            onInitialLoadError = ::onSearchError,
            onInitialLoadFinished = ::onFinally,
            mapEntityToUiState = { it.toTvShowUI() },
            onFlowCreated = { tvShowsFlow ->
                updateState { it.copy(tvShowsFlow = tvShowsFlow) }
                hideSnackBar()
            },
            onLoadingChanged = { isLoading ->
                updateState { it.copy(isLoading = isLoading) }
            },
        )
    }

    private fun searchActors(text: String) {
        collectPagingFlow(
            loadData = { page ->
                searchActorsUseCase(
                    query = text,
                    page = page,
                )
            },
            onInitialLoadFinished = ::onFinally,
            onInitialLoadError = ::onSearchError,
            mapEntityToUiState = { it.toActorUI() },
            onFlowCreated = { actorsFlow ->
                updateState { it.copy(actorsFlow = actorsFlow) }
                hideSnackBar()
            },
            onLoadingChanged = { isLoading ->
                updateState { it.copy(isLoading = isLoading) }
            },
        )
    }

    private fun onSearchError(throwable: Throwable) {
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

    private fun clearAllPagingFlows() {
        updateState {
            it.copy(
                moviesFlow = flowOf(),
                tvShowsFlow = flowOf(),
                actorsFlow = flowOf(),
            )
        }
    }

    private fun getRecentViewed() {
        tryToCollect(
            dispatcher = dispatcher,
            flowProvider = { getRecentlyViewedUseCase() },
            onNewValue = ::onGetRecentViewedSuccess,
        )
    }

    private fun onGetRecentViewedSuccess(recentlyViewed: List<RecentlyViewed>) {
        val recentViewedUiState = recentlyViewed.take(10).map { it.toRecentlyViewedUI() }
        updateState { searchScreenState ->
            searchScreenState.copy(recentViewed = recentViewedUiState.distinctBy { it.id })
        }
    }

    private fun getRecentSearches() {
        tryToCollect(
            dispatcher = dispatcher,
            flowProvider = { getRecentSearchesUseCase() },
            onNewValue = ::onGetRecentSearchesSuccess,
        )
    }

    private fun onGetRecentSearchesSuccess(recentSearches: List<RecentSearch>) {
        val recentSearchUiState = recentSearches.take(20).map { it.toRecentSearchUI() }
        updateState { searchScreenState ->
            searchScreenState.copy(recentSearch = recentSearchUiState.distinctBy { it.query })
        }
    }

    override fun onClearRecentlyViewedClick() {
        tryToExecute(
            dispatcher = dispatcher,
            callee = { deleteAllRecentlyViewedUseCase() },
            onSuccess = { onClearRecentViewedSuccess() },
            onStart = ::onLoading,
            onFinally = ::onFinally,
        )
    }

    override fun onSaveRecentlyViewedClick(id: Long) {
        updateState {
            it.copy(
                recentViewed =
                    it.recentViewed.map { item ->
                        if (item.id == id) item.copy(isSaved = item.isSaved.not()) else item
                    },
            )
        }
    }

    private fun onClearRecentViewedSuccess() {
        showSnackBar(
            message = SearchSnackBarMessage.RemovedItemSuccessfully,
            isSuccess = true,
        )
        updateState {
            it.copy(
                recentViewed = emptyList(),
            )
        }
    }

    override fun onClearRecentSearchClick() {
        tryToExecute(
            dispatcher = dispatcher,
            callee = { deleteAllRecentSearchesUseCase() },
            onSuccess = { onClearRecentSearchSuccess() },
            onStart = ::onLoading,
            onFinally = ::onFinally,
        )
    }

    private fun onClearRecentSearchSuccess() {
        showSnackBar(
            message = SearchSnackBarMessage.RemovedItemSuccessfully,
            isSuccess = true,
        )
    }

    override fun onRemoveRecentSearchItemClick(id: Long) {
        tryToExecute(
            dispatcher = dispatcher,
            callee = { deleteRecentSearchUseCase(id) },
            onSuccess = { onRemoveRecentSearchItemSuccess() },
            onStart = ::onLoading,
            onFinally = ::onFinally,
        )
    }

    private fun onRemoveRecentSearchItemSuccess() {
        showSnackBar(
            message = SearchSnackBarMessage.RemovedItemSuccessfully,
            isSuccess = true,
        )
    }

    override fun onRecentSearchItemClick(id: Long) {
        val searchText = currentState.recentSearch.find { it.id == id }?.query ?: ""
        updateState { it.copy(searchText = searchText) }
        currentState.lastProcessedQuery = ""
        onSearchTextChanged(searchText)
        onSearchQueryChangedCollected(searchText)
    }


    override fun onSelectedSearchTabChanged(selectedTab: SearchScreenState.SearchTab) {
        if (selectedTab != currentState.selectedSearchTab) {
            updateState { it.copy(selectedSearchTab = selectedTab, isLoading = true) }
            currentState.lastProcessedQuery = ""
            onSearchQueryChangedCollected(currentState.searchText)
        }
    }

    override fun onRecentlyViewedClick(
        id: Long,
        imageUrl: String,
    ) {
        val recentlyViewed = currentState.recentViewed.find { it.id == id }
        recentlyViewed?.let {
            if (it.contentType == RecentlyViewed.ContentType.MOVIE) {
                sendEffect(SearchScreenEffect.NavigateToMovieDetails(id))
            } else {
                sendEffect(SearchScreenEffect.NavigateToTvShowDetails(id))
            }
        }
    }

    override fun onMovieItemClick(
        contentId: Long,
        contentImageUrl: String,
    ) {
        tryToExecute(
            dispatcher = dispatcher,
            callee = {
                addRecentlyViewedUseCase(
                    contentId,
                    contentImageUrl,
                    RecentlyViewed.ContentType.MOVIE,
                )
            },
            onSuccess = { onAddRecentlyViewedMovieSuccess(contentId) },
            onStart = ::onLoading,
            onFinally = ::onFinally,
        )
    }

    private fun onAddRecentlyViewedMovieSuccess(contentId: Long) {
        sendEffect(SearchScreenEffect.NavigateToMovieDetails(contentId))
    }

    override fun onTvShowItemClick(
        contentId: Long,
        contentImageUrl: String,
    ) {
        tryToExecute(
            dispatcher = dispatcher,
            callee = {
                addRecentlyViewedUseCase(
                    contentId,
                    contentImageUrl,
                    RecentlyViewed.ContentType.TV_SHOW,
                )
            },
            onSuccess = { onAddRecentlyViewedTvShowSuccess(contentId) },
            onStart = ::onLoading,
            onFinally = ::onFinally,
        )
    }

    private fun onAddRecentlyViewedTvShowSuccess(contentId: Long) {
        sendEffect(SearchScreenEffect.NavigateToTvShowDetails(contentId))
    }

    override fun onActorItemClick(id: Long) {
        sendEffect(SearchScreenEffect.NavigateToActorDetails(id))
    }

    override fun onSnackBarActionLabelClick() {
        performSearchByTab(currentState.searchText)
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
