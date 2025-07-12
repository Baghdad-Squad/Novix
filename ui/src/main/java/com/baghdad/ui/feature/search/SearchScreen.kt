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
import com.baghdad.design_system.theme.NovixTheme
import com.baghdad.design_system.theme.Theme
import com.baghdad.ui.R
import com.baghdad.ui.feature.search.component.EmptySearchState
import com.baghdad.ui.feature.search.component.RecentlyViewedSection
import com.baghdad.ui.feature.search.component.SearchResultContent
import com.baghdad.ui.feature.search.component.SearchTextField
import com.baghdad.ui.feature.search.component.filter.FilterBottomSheet
import com.baghdad.ui.feature.search.component.recentSearchSection
import com.baghdad.viewmodel.search.SearchInteractionListener
import com.baghdad.viewmodel.search.SearchScreenState
import com.baghdad.viewmodel.search.SearchTab
import com.baghdad.viewmodel.search.SearchViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun SearchScreen(
    viewModel: SearchViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    SearchContent(
        uiState = uiState, listener = viewModel
    )
}

@Composable
fun SearchContent(
    uiState: SearchScreenState, listener: SearchInteractionListener
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
        )
        AnimatedContent(uiState.searchText.isNotBlank()) { it ->
            if (it) {
                SearchResultContent(
                    selectedTab = uiState.selectedSearchTab,
                    onTabSelected = { listener.onSelectedSearchTabChanged(it) },
                    onSavedClick = { listener.onSavedRecentlyViewedClick(it) },
                    movies = uiState.movies,
                    tvShows = uiState.tvShows,
                    actors = uiState.actors,
                    onMovieClick = { listener.onRecentlyViewedClick(it) },
                    onTvShowClick = { listener.onRecentlyViewedClick(it) },
                    onActorClick = { listener.onRecentlyViewedClick(it) },
                    isLoading = uiState.isLoading
                )
            } else TestComp(uiState, listener)
        }

        FilterBottomSheet(
            isBottomSheetVisible = uiState.bottomSheetUiState.isBottomSheetVisible,
            minimumYear = uiState.bottomSheetUiState.minimumYear,
            maximumYear = uiState.bottomSheetUiState.maximumYear,
            rate = uiState.bottomSheetUiState.rate,
            selectedSearchTab = uiState.selectedSearchTab,
            selectedGenres = uiState.bottomSheetUiState.selectedGenres,
            moviesGenres = uiState.bottomSheetUiState.moviesGenres,
            tvShowsGenres = uiState.bottomSheetUiState.tvShowsGenres,
            onBottomSheetCloseClick = { listener.onBottomSheetCloseClick() },
            onClearClick = { listener.onBottomSheetClearClick() },
            onApplyClick = { listener.onApplyClick() },
            onRatingChanged = { listener.onRatingChanged(it) },
            onYearRangeSelected = { listener.onYearRangeSelected(it) },
            onGenreSelected = { listener.onGenreSelected(it) }
        )
    }

}

@Composable
private fun TestComp(
    uiState: SearchScreenState,
    listener: SearchInteractionListener
) {

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Theme.color.surface)
    ) {
        if (uiState.recentSearch.isEmpty() && uiState.recentViewed.isEmpty()) {
            item {
                Box(
                    modifier = Modifier.height(600.dp),
                    contentAlignment = Alignment.Center
                ) {
                    EmptySearchState(
                        imagePath = com.baghdad.design_system.R.drawable.no_search_results,
                        contentDescription = stringResource(R.string.no_search_result_picture),
                        message = stringResource(R.string.no_search_result_please_try_with_another_keyword),
                    )
                }
            }
        } else {

            if (uiState.recentViewed.isNotEmpty()) {
                item {
                    RecentlyViewedSection(
                        recentViewed = uiState.recentViewed,
                        onClearRecentlyViewedClick = { listener.onClearRecentSearchClick() },
                        onSavedClick = { listener.onSavedRecentlyViewedClick(it) },
                        onRecentlyViewedClick = { listener.onRecentlyViewedClick(it) },
                        modifier = Modifier.padding(top = 12.dp)
                    )
                }
            } else {
                item {
                    Spacer(modifier = Modifier.padding(top = 12.dp))
                }
            }
            if (uiState.recentSearch.isNotEmpty())
                recentSearchSection(
                    recentSearch = uiState.recentSearch,
                    onClearRecentSearchClick = { listener.onClearRecentSearchClick() },
                    onRemoveRecentSearchItemClick = { listener.onRemoveRecentSearchItemClick(it) },
                    onRecentSearchClicked = { listener.onRecentSearchItemClick(it) }
                )
        }
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
                override fun onBottomSheetCloseClick() {}
                override fun onBottomSheetClearClick() {}
                override fun onApplyClick() {}
                override fun onRecentlyViewedClick(item: Long) {}
                override fun onSavedRecentlyViewedClick(item: Long) {}
                override fun onSelectedSearchTabChanged(selectedTab: SearchTab) {}
            })
    }
}