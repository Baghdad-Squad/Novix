package com.baghdad.ui.feature.search

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.baghdad.design_system.component.SnackBar
import com.baghdad.design_system.theme.NovixTheme
import com.baghdad.design_system.theme.Theme
import com.baghdad.ui.R
import com.baghdad.ui.base.ObserveAsEffect
import com.baghdad.ui.base.toStringResource
import com.baghdad.ui.feature.search.component.EmptySearchState
import com.baghdad.ui.feature.search.component.RecentlyViewedSection
import com.baghdad.ui.feature.search.component.SearchResultContent
import com.baghdad.ui.feature.search.component.SearchTextField
import com.baghdad.ui.feature.search.component.filter.FilterBottomSheet
import com.baghdad.ui.feature.search.component.recentSearchSection
import com.baghdad.viewmodel.base.SnackBarState
import com.baghdad.viewmodel.errorStates.BaseSnackBarMessage
import com.baghdad.viewmodel.errorStates.SearchScreenBaseSnackBarMessages
import com.baghdad.viewmodel.search.SearchInteractionListener
import com.baghdad.viewmodel.search.SearchScreenEffect
import com.baghdad.viewmodel.search.SearchScreenState
import com.baghdad.viewmodel.search.SearchViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun SearchScreen(
    viewModel: SearchViewModel = koinViewModel(),
    navigateToActorDetails: (id: Long) -> Unit,
    navigateToMovieDetails: (id: Long) -> Unit,
    navigateToTvShowDetails: (id: Long) -> Unit,
    navigateToRecentlyViewedDetails: (id: Long) -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackBarState by viewModel.snackBarState.collectAsStateWithLifecycle()
    SearchContent(
        uiState = uiState, listener = viewModel, snackBarState
    )

    ObserveAsEffect(viewModel.uiEffect) { effect ->
        when (effect) {
            is SearchScreenEffect.NavigateToActorDetails -> navigateToActorDetails(effect.actorId)
            is SearchScreenEffect.NavigateToMovieDetails -> navigateToMovieDetails(effect.movieId)
            is SearchScreenEffect.NavigateToRecentlyViewedDetails -> navigateToRecentlyViewedDetails(
                effect.mediaId
            )

            is SearchScreenEffect.NavigateToTvShowDetails -> navigateToTvShowDetails(effect.tvShowId)
        }
    }
}

@Composable
fun SearchContent(
    uiState: SearchScreenState, listener: SearchInteractionListener, snackBarState: SnackBarState
) {
    Column(
        modifier = Modifier
            .background(Theme.color.surface)
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(vertical = 12.dp, horizontal = 16.dp)
    ) {
        SearchTextField(
            query = uiState.searchText,
            onQueryChange = { listener.onSearchTextChanged(it) },
            onFilterIconClick = { listener.onFilterIconClick() },
            searchTab = uiState.selectedSearchTab
        )
        AnimatedContent(uiState.searchText.isNotBlank()) { it ->
            if (it) {
                SearchResultContent(
                    selectedTab = uiState.selectedSearchTab,
                    onTabSelected = { listener.onSelectedSearchTabChanged(it) },
                    onSavedClick = { listener.onSaveRecentlyViewedClick(it) },
                    movies = uiState.movies,
                    tvShows = uiState.tvShows,
                    actors = uiState.actors,
                    onMovieClick = { listener.onMovieItemClick(it) },
                    onTvShowClick = { listener.onTvShowItemClick(it) },
                    onActorClick = { listener.onActorItemClick(it) },
                    isLoading = uiState.isLoading
                )
            } else RecentlyViewsWithSearch(uiState, listener)
        }

        FilterBottomSheet(
            isBottomSheetVisible = uiState.bottomSheetUiState.isBottomSheetVisible,
            searchFilter = uiState.searchFilter,
            onBottomSheetCloseClick = { listener.onFilterCloseIconClick() },
            onClearClick = { listener.onFilterClearClick() },
            onApplyClick = { listener.onApplyFilterClick() },
            onRatingChanged = { listener.onRatingChanged(it) },
            onYearRangeSelected = { listener.onYearRangeSelected(it) },
            onGenreSelected = { listener.onGenreSelected(it) })

        SnackBar(
            message = stringResource(snackBarMessage(snackBarState.message)),
            isSuccess = snackBarState.isSuccess,
            isVisible = snackBarState.isVisible
        )
    }
}


@Composable
private fun RecentlyViewsWithSearch(
    uiState: SearchScreenState, listener: SearchInteractionListener
) {

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Theme.color.surface)
    ) {
        if (uiState.recentSearch.isEmpty() && uiState.recentViewed.isEmpty()) {
            item {
                Box(
                    modifier = Modifier.height(600.dp), contentAlignment = Alignment.Center
                ) {
                    EmptySearchState(
                        imagePath = com.baghdad.design_system.R.drawable.start_explore,
                        contentDescription = stringResource(R.string.start_exploring),
                        message = stringResource(R.string.start_exploring),
                    )
                }
            }
        } else {

            if (uiState.recentViewed.isNotEmpty()) {
                item {
                    RecentlyViewedSection(
                        recentViewed = uiState.recentViewed,
                        onClearRecentlyViewedClick = { listener.onClearRecentSearchClick() },
                        onSavedClick = { listener.onSaveRecentlyViewedClick(it) },
                        onRecentlyViewedClick = { listener.onRecentlyViewedClick(it) },
                        modifier = Modifier.padding(top = 12.dp)
                    )
                }
            } else {
                item {
                    Spacer(modifier = Modifier.padding(top = 12.dp))
                }
            }
            if (uiState.recentSearch.isNotEmpty()) recentSearchSection(
                recentSearch = uiState.recentSearch,
                onClearRecentSearchClick = { listener.onClearRecentSearchClick() },
                onRemoveRecentSearchItemClick = { listener.onRemoveRecentSearchItemClick(it) },
                onRecentSearchClicked = { listener.onRecentSearchItemClick(it) })
        }
    }
}

@Composable
private fun snackBarMessage(type: BaseSnackBarMessage): Int {
    return when (type) {
        SearchScreenBaseSnackBarMessages.RemovedItemSuccessfully -> R.string.snackbar_removed_success
        SearchScreenBaseSnackBarMessages.SavedItemSuccessfully -> R.string.snackbar_saved_success
        else -> type.toStringResource()
    }
}

@Preview
@Composable
private fun SearchScreenPreview() {
    NovixTheme {
        SearchContent(
            uiState = SearchScreenState(
                searchText = "", recentSearch = emptyList(), recentViewed = emptyList()
            ), listener = object : SearchInteractionListener {
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
                override fun onRecentlyViewedClick(item: Long) {}
                override fun onMovieItemClick(contentId: Long) {}
                override fun onTvShowItemClick(contentId: Long) {}
                override fun onActorItemClick(id: Long) {}
                override fun onSaveRecentlyViewedClick(item: Long) {}
                override fun onSelectedSearchTabChanged(selectedTab: SearchScreenState.SearchTab) {}
            },
            snackBarState = SnackBarState()
        )
    }
}