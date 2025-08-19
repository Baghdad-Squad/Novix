package com.baghdad.viewmodel.search

import androidx.paging.PagingData
import com.baghdad.domain.exception.NoInternetException
import com.baghdad.domain.model.search.RecentlyViewed
import com.baghdad.domain.usecase.login.IsUserLoggedInUseCase
import com.baghdad.domain.usecase.recentlyViewed.AddRecentlyViewedUseCase
import com.baghdad.domain.usecase.recentlyViewed.DeleteAllRecentlyViewedUseCase
import com.baghdad.domain.usecase.recentlyViewed.GetRecentlyViewedUseCase
import com.baghdad.domain.usecase.savedList.AddMovieToSavedListUseCase
import com.baghdad.domain.usecase.savedList.CreateSavedListUseCase
import com.baghdad.domain.usecase.savedList.GetSavedListsUseCase
import com.baghdad.domain.usecase.savedList.RemoveMovieFromSavedListUseCase
import com.baghdad.domain.usecase.search.DeleteAllRecentSearchesUseCase
import com.baghdad.domain.usecase.search.DeleteRecentSearchUseCase
import com.baghdad.domain.usecase.search.GetRecentSearchesUseCase
import com.baghdad.domain.usecase.search.SearchActorsUseCase
import com.baghdad.domain.usecase.search.SearchMoviesUseCase
import com.baghdad.domain.usecase.search.SearchTvShowsUseCase
import com.baghdad.entity.savedList.SavedList
import com.baghdad.entity.search.RecentSearch
import com.baghdad.viewmodel.R
import com.baghdad.viewmodel.base.BaseViewModel
import com.baghdad.viewmodel.errorStates.BaseSnackBarMessage
import com.baghdad.viewmodel.errorStates.SearchSnackBarMessage
import com.baghdad.viewmodel.shared.AddToListBottomSheetState
import com.baghdad.viewmodel.shared.SavedListUiState
import com.baghdad.viewmodel.shared.toUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
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
    private val isUserLoggedInUseCase: IsUserLoggedInUseCase,
    private val getSavedListsUseCase: GetSavedListsUseCase,
    private val addMovieToSavedListUseCase: AddMovieToSavedListUseCase,
    private val createSavedListUseCase: CreateSavedListUseCase,
    private val removeMovieFromSavedListUseCase: RemoveMovieFromSavedListUseCase,
    private val defaultDispatcher: CoroutineDispatcher,
) : BaseViewModel<SearchScreenState, SearchScreenEffect>(SearchScreenState()),
    SearchInteractionListener {

    init {
        checkIfUserIsLoggedIn()
        getRecentSearches()
        getRecentViewed()
        observeSearchQueryChanges()
    }

    private fun checkIfUserIsLoggedIn() {
        tryToExecute(
            callee = { isUserLoggedInUseCase() },
            onSuccess = ::onCheckIfUserIsLoggedInSuccess,
            dispatcher = defaultDispatcher,
        )
    }

    private fun onCheckIfUserIsLoggedInSuccess(isLoggedIn: Boolean) {
        updateState {
            it.copy(isUserLoggedIn = isLoggedIn)
        }
        if (isLoggedIn) {
            getUserSavedLists()
        }
    }

    private fun getUserSavedLists() {
        collectPagingFlow(
            loadData = { page ->
                getSavedListsUseCase(
                    page = page,
                    pageSize = DEFAULT_PAGE_SIZE,
                )
            },
            onInitialLoadError = ::onSearchError,
            pageSize = DEFAULT_PAGE_SIZE,
            mapEntityToUiState = SavedList::toUiState,
            onFlowCreated = ::onGetSavedListFlowCreated,
        )
    }

    private fun onGetSavedListFlowCreated(flow: Flow<PagingData<SavedListUiState>>) {
        updateState {
            it.copy(
                addToListBottomSheetState =
                    it.addToListBottomSheetState.copy(
                        savedLists = flow,
                    ),
            )
        }
    }

    override fun mapThrowableToErrorMessage(throwable: Throwable): BaseSnackBarMessage =
        BaseSnackBarMessage.UnknownError

    override fun onSearchTextChanged(text: String) {
        updateState { it.copy(searchText = text, isLoading = true) }
        getRecentSearches()
        getRecentViewed()
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
            flowProvider = { observeSearchQueryFlow() },
            onNewValue = { query -> onSearchQueryChangedCollected(query) },
            dispatcher = defaultDispatcher

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
        if (text.isBlank()) return
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
            flowProvider = { getRecentlyViewedUseCase() },
            onNewValue = ::onGetRecentViewedSuccess,
            dispatcher = defaultDispatcher
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
            flowProvider = { getRecentSearchesUseCase() },
            onNewValue = ::onGetRecentSearchesSuccess,
            dispatcher = defaultDispatcher
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
            callee = { deleteAllRecentlyViewedUseCase() },
            onSuccess = { onClearRecentViewedSuccess() },
            onStart = ::onLoading,
            onFinally = ::onFinally,
            dispatcher = defaultDispatcher
        )
    }

    override fun onSaveMovieClick(movie: SearchScreenState.MovieUiState) {
        onSaveMovieClick(movie.isSaved, movie.savedListId, movie.id)
    }

    private fun onSaveMovieClick(
        isSaved: Boolean,
        listId: Long,
        movieId: Long,
    ) {
        if (isSaved) {
            removeSavedItem(listId, movieId)
        } else {
            updateState {
                it.copy(
                    addToListBottomSheetState =
                        it.addToListBottomSheetState.copy(
                            isVisible = true,
                            selectedItemId = movieId,
                            selectedListId = null,
                        ),
                )
            }
        }
    }

    override fun onSaveRecentlyViewedClick(recentlyViewed: SearchScreenState.RecentlyViewedUiState) {
        if (recentlyViewed.contentType == RecentlyViewed.ContentType.MOVIE) {
            onSaveMovieClick(
                isSaved = recentlyViewed.isSaved,
                listId = recentlyViewed.savedListId,
                movieId = recentlyViewed.id,
            )
        }
    }

    private fun removeSavedItem(
        listId: Long,
        itemId: Long,
    ) {
        tryToExecute(
            callee = { removeMovieFromSavedListUseCase(listId = listId, movieId = itemId) },
            onSuccess = { onRemoveSavedItemSuccess() },
            dispatcher = defaultDispatcher,
            onFinally = ::onRemoveSavedItemFinished,
        )
    }

    private fun onRemoveSavedItemSuccess() {
        refreshSavedItems()
        showItemRemovedSuccessfullySnackBar()
    }

    private fun showItemRemovedSuccessfullySnackBar() {
        showSnackBar(
            message = BaseSnackBarMessage.RemovedItemSuccessfully,
            isSuccess = true,
        )
    }

    private fun onRemoveSavedItemFinished() {
        updateState {
            it.copy(
                addToListBottomSheetState =
                    it.addToListBottomSheetState.copy(
                        isVisible = false,
                    ),
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
            callee = { deleteAllRecentSearchesUseCase() },
            onSuccess = { onClearRecentSearchSuccess() },
            onStart = ::onLoading,
            onFinally = ::onFinally,
            dispatcher = defaultDispatcher
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
            callee = { deleteRecentSearchUseCase(id) },
            onSuccess = { onRemoveRecentSearchItemSuccess() },
            onStart = ::onLoading,
            onFinally = ::onFinally,
            dispatcher = defaultDispatcher
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
            dispatcher = defaultDispatcher
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
            dispatcher = defaultDispatcher
        )
    }

    private fun onAddRecentlyViewedTvShowSuccess(contentId: Long) {
        sendEffect(SearchScreenEffect.NavigateToTvShowDetails(contentId))
    }

    override fun onActorItemClick(id: Long) {
        sendEffect(SearchScreenEffect.NavigateToActorDetails(id))
    }

    override fun onSnackBarActionLabelClick() {
        hideSnackBar()
        performSearchByTab(currentState.searchText)
    }

    override fun onSaveItemToListClicked() {
        tryToExecute(
            callee = {
                addMovieToSavedListUseCase(
                    listId =
                        currentState.addToListBottomSheetState.selectedListId
                            ?: return@tryToExecute,
                    movieId = currentState.addToListBottomSheetState.selectedItemId,
                )
            },
            onError = { onAddItemToListError() },
            onSuccess = { onAddItemToListSuccess() },
            dispatcher = defaultDispatcher,
            onStart = ::onAddItemToListStart,
            onFinally = ::onAddItemToListFinished,
        )
    }

    private fun onAddItemToListError() {
        showNoInternetSnackBarWithoutRetry()
    }

    private fun showNoInternetSnackBarWithoutRetry() {
        showSnackBar(
            message = BaseSnackBarMessage.NetworkError,
            isSuccess = false,
        )
    }

    private fun onAddItemToListSuccess() {
        refreshSavedItems()
        onSaveToListBottomSheetDismiss()
        showItemSavedSuccessfullySnackBar()
    }

    private fun refreshSavedItems() {
        getRecentViewed()
        performSearchByTab(currentState.searchText)
        getUserSavedLists()
    }

    private fun showItemSavedSuccessfullySnackBar() {
        showSnackBar(
            message = BaseSnackBarMessage.SavedItemSuccessfully,
            isSuccess = true,
        )
    }

    private fun onAddItemToListStart() {
        updateState {
            it.copy(
                addToListBottomSheetState =
                    it.addToListBottomSheetState.copy(
                        isLoading = true,
                    ),
            )
        }
    }

    private fun onAddItemToListFinished() {
        updateState {
            it.copy(
                addToListBottomSheetState =
                    it.addToListBottomSheetState.copy(
                        isLoading = false,
                    ),
            )
        }
    }

    override fun onCreateNewListClicked() {
        updateState {
            it.copy(
                addListBottomSheetState =
                    it.addListBottomSheetState.copy(
                        isVisible = true,
                    ),
                addToListBottomSheetState =
                    it.addToListBottomSheetState.copy(
                        isVisible = false,
                    ),
            )
        }
    }

    override fun onLoginClicked() {
        sendEffect(
            SearchScreenEffect.NavigateToLogin,
        )
    }

    override fun onSaveToListBottomSheetDismiss() {
        updateState {
            it.copy(
                addToListBottomSheetState =
                    AddToListBottomSheetState(
                        savedLists = it.addToListBottomSheetState.savedLists,
                    ),
            )
        }
    }

    override fun onListSelected(listId: Long) {
        updateState {
            it.copy(
                addToListBottomSheetState =
                    it.addToListBottomSheetState.copy(
                        selectedListId = listId,
                    ),
            )
        }
    }

    override fun onCreatedListNameChanged(name: String) {
        updateState {
            it.copy(
                addListBottomSheetState =
                    it.addListBottomSheetState.copy(
                        listName = name,
                    ),
            )
        }
    }

    override fun onCreateListBottomSheetDismiss() {
        updateState {
            it.copy(
                addListBottomSheetState =
                    it.addListBottomSheetState.copy(
                        isVisible = false,
                        listName = "",
                        isLoading = false,
                    ),
                addToListBottomSheetState =
                    it.addToListBottomSheetState.copy(
                        isVisible = true,
                    ),
            )
        }
    }

    override fun onCreateListBottomSheetAddClick() {
        tryToExecute(
            callee = {
                createSavedListUseCase(
                    title = currentState.addListBottomSheetState.listName,
                )
            },
            onSuccess = { onCreateListSuccess() },
            onStart = ::onCreateListStart,
            onFinally = ::onCreateListFinished,
            dispatcher = defaultDispatcher,
        )
    }

    private fun onCreateListSuccess() {
        onCreateListBottomSheetDismiss()
        getUserSavedLists()
    }

    private fun onCreateListStart() {
        updateState {
            it.copy(
                addListBottomSheetState =
                    it.addListBottomSheetState.copy(
                        isLoading = true,
                    ),
            )
        }
    }

    private fun onCreateListFinished() {
        updateState {
            it.copy(
                addListBottomSheetState =
                    it.addListBottomSheetState.copy(
                        isLoading = false,
                    ),
            )
        }
    }

    private fun onLoading() {
        updateState { it.copy(isLoading = true) }
    }

    private fun onFinally() {
        updateState { it.copy(isLoading = false) }
    }

    companion object {
        private const val SEARCH_DEBOUNCED_DELAY = 750L
        private const val DEFAULT_PAGE_SIZE = 20
    }
}
