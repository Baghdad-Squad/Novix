package com.baghdad.ui.feature.search

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.baghdad.design_system.component.HorizontalDivider
import com.baghdad.design_system.component.Scaffold
import com.baghdad.design_system.component.SnackBar
import com.baghdad.design_system.theme.NovixTheme
import com.baghdad.design_system.theme.Theme
import com.baghdad.ui.R
import com.baghdad.ui.base.ObserveAsEffect
import com.baghdad.ui.base.toStringResource
import com.baghdad.ui.feature.search.component.EmptySearchState
import com.baghdad.ui.feature.search.component.RecentSearchItem
import com.baghdad.ui.feature.search.component.RecentlyViewedSection
import com.baghdad.ui.feature.search.component.SearchResultContent
import com.baghdad.ui.feature.search.component.SearchTextField
import com.baghdad.ui.feature.search.component.SectionHeaderWithAction
import com.baghdad.ui.feature.search.component.filter.FilterBottomSheet
import com.baghdad.ui.feature.util.remeberSaveableLazyListState
import com.baghdad.ui.feature.util.rememberSaveableLazyGridState
import com.baghdad.ui.navigation.graph.search.SearchNavEvent
import com.baghdad.viewmodel.base.SnackBarState
import com.baghdad.viewmodel.errorStates.BaseSnackBarMessage
import com.baghdad.viewmodel.errorStates.SearchSnackBarMessage
import com.baghdad.viewmodel.search.SearchInteractionListener
import com.baghdad.viewmodel.search.SearchScreenEffect
import com.baghdad.viewmodel.search.SearchScreenState
import com.baghdad.viewmodel.search.SearchViewModel
import kotlinx.coroutines.flow.flowOf
import org.koin.androidx.compose.koinViewModel

@Composable
fun SearchScreen(
    viewModel: SearchViewModel = koinViewModel(),
    handleNavigation: (SearchNavEvent) -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackBarState by viewModel.snackBarState.collectAsStateWithLifecycle()

    val movieItems = uiState.moviesFlow.collectAsLazyPagingItems()
    val actorItems = uiState.actorsFlow.collectAsLazyPagingItems()
    val tvShowItems = uiState.tvShowsFlow.collectAsLazyPagingItems()

    SearchContent(
        uiState = uiState,
        listener = viewModel,
        snackBarState = snackBarState,
        movieItems = movieItems,
        actorItems = actorItems,
        tvShowItems = tvShowItems
    )

    HandleNavigationEffects(
        viewModel = viewModel,
        handleNavigation = handleNavigation
    )
}

@Composable
private fun HandleNavigationEffects(
    viewModel: SearchViewModel,
    handleNavigation: (SearchNavEvent) -> Unit
) {
    ObserveAsEffect(viewModel.uiEffect) { effect ->
        when (effect) {
            is SearchScreenEffect.NavigateToActorDetails -> {
                handleNavigation(SearchNavEvent.NavigateToActorDetails(effect.actorId))
            }

            is SearchScreenEffect.NavigateToMovieDetails -> {
                handleNavigation(SearchNavEvent.NavigateToMovieDetails(effect.movieId))
            }

            is SearchScreenEffect.NavigateToTvShowDetails -> {
                handleNavigation(SearchNavEvent.NavigateToTvShowDetails(effect.tvShowId))
            }
        }
    }
}

@Composable
fun SearchContent(
    uiState: SearchScreenState,
    listener: SearchInteractionListener,
    snackBarState: SnackBarState,
    movieItems: LazyPagingItems<SearchScreenState.MovieUiState>,
    actorItems: LazyPagingItems<SearchScreenState.ActorUiState>,
    tvShowItems: LazyPagingItems<SearchScreenState.TvShowUiState>
) {
    val moviesState = rememberSaveableLazyGridState(key = "movies_grid")
    val actorsState = remeberSaveableLazyListState(key = "actors_list")
    val tvShowsState = rememberSaveableLazyGridState(key = "tv_shows_grid")

    Scaffold(
        modifier = Modifier
            .background(Theme.color.surface)
            .systemBarsPadding()
            .statusBarsPadding(),
        snackbar = {
            SearchSnackBar(
                snackBarState = snackBarState,
                onActionClick = listener::onSnackBarActionLabelClick,
            )
        },
    ) {
        Column(
            modifier = Modifier
                .background(Theme.color.surface)
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
                .padding(top = 24.dp)
        ) {
            SearchTextField(
                query = uiState.searchText,
                onQueryChange = listener::onSearchTextChanged,
                onFilterIconClick = listener::onFilterIconClick,
                searchTab = uiState.selectedSearchTab,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            SearchContentSection(
                uiState = uiState,
                listener = listener,
                movieItems = movieItems,
                actorItems = actorItems,
                tvShowItems = tvShowItems,
                moviesState = moviesState,
                actorsState = actorsState,
                tvShowsState = tvShowsState
            )

            FilterBottomSheet(
                isBottomSheetVisible = uiState.bottomSheetUiState.isBottomSheetVisible,
                searchFilter = uiState.searchFilter,
                isGenresError = uiState.searchFilter.isGenresError,
                onReloadGenres = listener::onReloadFilterGenres,
                onBottomSheetCloseClick = listener::onFilterCloseIconClick,
                onClearClick = listener::onFilterClearClick,
                onApplyClick = listener::onApplyFilterClick,
                onRatingChanged = listener::onRatingChanged,
                onYearRangeSelected = listener::onYearRangeSelected,
                onGenreSelected = listener::onGenreSelected
            )
        }
    }
}

@Composable
private fun SearchSnackBar(
    snackBarState: SnackBarState,
    onActionClick: () -> Unit,
) {
    SnackBar(
        message = stringResource(getSnackBarMessage(snackBarState.message)),
        isSuccess = snackBarState.isSuccess,
        isVisible = snackBarState.isVisible,
        actionLabel = snackBarState.actionLabelRes?.let { stringResource(it) },
        onActionClick = onActionClick,
    )
}

@Composable
private fun SearchContentSection(
    uiState: SearchScreenState,
    listener: SearchInteractionListener,
    movieItems: LazyPagingItems<SearchScreenState.MovieUiState>,
    actorItems: LazyPagingItems<SearchScreenState.ActorUiState>,
    tvShowItems: LazyPagingItems<SearchScreenState.TvShowUiState>,
    moviesState: androidx.compose.foundation.lazy.grid.LazyGridState,
    actorsState: androidx.compose.foundation.lazy.LazyListState,
    tvShowsState: androidx.compose.foundation.lazy.grid.LazyGridState
) {
    val shouldShowSearchResults = uiState.searchText.trim().isNotBlank()
    AnimatedContent(targetState = shouldShowSearchResults) { showResults ->
        if (showResults) {
            SearchResultContent(
                selectedTab = uiState.selectedSearchTab,
                onTabSelected = listener::onSelectedSearchTabChanged,
                onSavedClick = listener::onSaveRecentlyViewedClick,
                movies = movieItems,
                tvShows = tvShowItems,
                actors = actorItems,
                onMovieClick = { id, imageUrl ->
                    listener.onMovieItemClick(id, imageUrl)
                },
                onTvShowClick = { id, imageUrl ->
                    listener.onTvShowItemClick(id, imageUrl)
                },
                onActorClick = listener::onActorItemClick,
                isLoading = uiState.isLoading,
                moviesState = moviesState,
                actorsState = actorsState,
                tvShowsState = tvShowsState,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        } else {
            RecentViewsAndSearchSection(
                uiState = uiState,
                listener = listener
            )
        }
    }
}

@Composable
private fun RecentViewsAndSearchSection(
    uiState: SearchScreenState,
    listener: SearchInteractionListener
) {
    val hasNoContent = uiState.recentSearch.isEmpty() && uiState.recentViewed.isEmpty()

    if (hasNoContent) {
        EmptySearchStateSection()
    } else {
        RecentContentList(
            uiState = uiState,
            listener = listener
        )
    }
}

@Composable
private fun EmptySearchStateSection() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        EmptySearchState(
            imagePath = com.baghdad.design_system.R.drawable.start_explore,
            contentDescription = stringResource(R.string.start_exploring),
            message = stringResource(R.string.start_exploring),
            modifier = Modifier.padding(bottom = 60.dp)
        )
    }
}

@Composable
private fun RecentContentList(
    uiState: SearchScreenState,
    listener: SearchInteractionListener
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Theme.color.surface),
        contentPadding = PaddingValues(bottom = 8.dp)
    ) {
        addRecentlyViewedSection(
            uiState = uiState,
            listener = listener
        )

        addRecentSearchSection(
            uiState = uiState,
            listener = listener
        )
    }
}

private fun LazyListScope.addRecentlyViewedSection(
    uiState: SearchScreenState,
    listener: SearchInteractionListener
) {
    if (uiState.recentViewed.isNotEmpty()) {
        item {
            RecentlyViewedSection(
                recentViewed = uiState.recentViewed,
                onClearRecentlyViewedClick = listener::onClearRecentlyViewedClick,
                onSavedClick = listener::onSaveRecentlyViewedClick,
                onRecentlyViewedClick = { id, imageUrl ->
                    listener.onRecentlyViewedClick(id, imageUrl)
                },
                modifier = Modifier.padding(top = 12.dp)
            )
        }
    } else {
        item {
            Spacer(modifier = Modifier.padding(top = 12.dp))
        }
    }
}

private fun LazyListScope.addRecentSearchSection(
    uiState: SearchScreenState,
    listener: SearchInteractionListener
) {
    if (uiState.recentSearch.isNotEmpty()) {
        item {
            SectionHeaderWithAction(
                title = stringResource(R.string.recent_search),
                onClearAllClick = listener::onClearRecentSearchClick,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }

        itemsIndexed(
            items = uiState.recentSearch,
        ) { index, keyWord ->
            RecentSearchItem(
                title = keyWord.query,
                onCancelClick = { listener.onRemoveRecentSearchItemClick(keyWord.id) },
                onRecentSearchClicked = { listener.onRecentSearchItemClick(keyWord.id) },
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .animateItem(
                        fadeInSpec = tween(durationMillis = 300),
                        fadeOutSpec = tween(durationMillis = 300),
                        placementSpec = tween(durationMillis = 300)
                    )
            )

            if (index < uiState.recentSearch.lastIndex) {
                HorizontalDivider(
                    modifier = Modifier
                        .padding(horizontal = 18.dp)
                        .animateItem(
                            fadeInSpec = tween(durationMillis = 300),
                            fadeOutSpec = tween(durationMillis = 300),
                            placementSpec = tween(durationMillis = 300)
                        ),
                    thickness = 1.dp,
                    color = Theme.color.stroke
                )
            }
        }
    }
}

@Composable
private fun getSnackBarMessage(type: BaseSnackBarMessage): Int {
    return when (type) {
        SearchSnackBarMessage.RemovedItemSuccessfully -> R.string.snackbar_removed_success
        SearchSnackBarMessage.SavedItemSuccessfully -> R.string.snackbar_saved_success
        else -> type.toStringResource()
    }
}

@Preview(device = "spec:width=411dp,height=891dp")
@Composable
private fun SearchScreenPreview() {
    NovixTheme {
        SearchContent(
            uiState = SearchScreenState(
                searchText = "",
                recentSearch = emptyList(),
                recentViewed = emptyList()
            ),
            listener = createPreviewListener(),
            snackBarState = SnackBarState(),
            movieItems = flowOf(PagingData.empty<SearchScreenState.MovieUiState>()).collectAsLazyPagingItems(),
            actorItems = flowOf(PagingData.empty<SearchScreenState.ActorUiState>()).collectAsLazyPagingItems(),
            tvShowItems = flowOf(PagingData.empty<SearchScreenState.TvShowUiState>()).collectAsLazyPagingItems()
        )
    }
}

private fun createPreviewListener() = object : SearchInteractionListener {
    override fun onSearchTextChanged(query: String) {}
    override fun onFilterIconClick() {}
    override fun onRatingChanged(rating: Int) {}
    override fun onYearRangeSelected(range: ClosedFloatingPointRange<Float>) {}
    override fun onGenreSelected(genre: SearchScreenState.GenreUiState) {}
    override fun onClearRecentlyViewedClick() {}
    override fun onClearRecentSearchClick() {}
    override fun onRemoveRecentSearchItemClick(id: Long) {}
    override fun onRecentSearchItemClick(id: Long) {}
    override fun onFilterCloseIconClick() {}
    override fun onFilterClearClick() {}
    override fun onApplyFilterClick() {}
    override fun onActorItemClick(id: Long) {}
    override fun onSaveRecentlyViewedClick(item: Long) {}
    override fun onSelectedSearchTabChanged(selectedTab: SearchScreenState.SearchTab) {}
    override fun onRecentlyViewedClick(id: Long, imageUrl: String) {}
    override fun onMovieItemClick(contentId: Long, contentImageUrl: String) {}
    override fun onTvShowItemClick(contentId: Long, contentImageUrl: String,
    ) {}

    override fun onSnackBarActionLabelClick() {}

    override fun onReloadFilterGenres() {}
}