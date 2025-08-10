package com.baghdad.ui.feature.search.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.baghdad.design_system.component.Chip
import com.baghdad.design_system.component.WavyLoadingIndicator
import com.baghdad.design_system.theme.Theme
import com.baghdad.ui.R
import com.baghdad.viewmodel.search.SearchScreenState
import kotlinx.coroutines.flow.flowOf


@Composable
fun SearchResultContent(
    selectedTab: SearchScreenState.SearchTab,
    onTabSelected: (SearchScreenState.SearchTab) -> Unit,
    onSaveMovieClick: (SearchScreenState.MovieUiState) -> Unit,
    moviesState: LazyGridState,
    tvShowsState: LazyGridState,
    actorsState: LazyListState,
    movies: LazyPagingItems<SearchScreenState.MovieUiState>,
    tvShows: LazyPagingItems<SearchScreenState.TvShowUiState>,
    actors: LazyPagingItems<SearchScreenState.ActorUiState>,
    onMovieClick: (Long, String) -> Unit,
    onTvShowClick: (Long, String) -> Unit,
    onActorClick: (Long) -> Unit,
    isLoading: Boolean,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
    ) {
        SearchTabHeader(selectedTab, onTabSelected)

        if (isLoading) {
            LoadingContent()
        } else {
            when (selectedTab) {
                SearchScreenState.SearchTab.MOVIES -> {
                    RenderMovieResults(movies, moviesState, onSaveMovieClick, onMovieClick)
                }

                SearchScreenState.SearchTab.TV_SHOWS -> {
                    RenderTvShowResults(tvShows, tvShowsState, onTvShowClick)
                }

                SearchScreenState.SearchTab.ACTORS -> {
                    RenderActorResults(actors, actorsState, onActorClick)
                }
            }
        }
    }
}

@Composable
private fun SearchTabHeader(
    selectedTab: SearchScreenState.SearchTab,
    onTabSelected: (SearchScreenState.SearchTab) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 12.dp, bottom = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.Start),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Chip(
            title = stringResource(id = R.string.tab_movies),
            isSelected = selectedTab == SearchScreenState.SearchTab.MOVIES,
            onClick = { onTabSelected(SearchScreenState.SearchTab.MOVIES) }
        )
        Chip(
            title = stringResource(id = R.string.tab_tv_shows),
            isSelected = selectedTab == SearchScreenState.SearchTab.TV_SHOWS,
            onClick = { onTabSelected(SearchScreenState.SearchTab.TV_SHOWS) }
        )
        Chip(
            title = stringResource(id = R.string.tab_actors),
            isSelected = selectedTab == SearchScreenState.SearchTab.ACTORS,
            onClick = { onTabSelected(SearchScreenState.SearchTab.ACTORS) }
        )
    }
}

@Composable
private fun LoadingContent() {
    Box(Modifier.fillMaxSize()) {
        WavyLoadingIndicator(modifier = Modifier.align(Alignment.Center))
    }
}

@Composable
private fun RenderMovieResults(
    movies: LazyPagingItems<SearchScreenState.MovieUiState>,
    state: LazyGridState,
    onSavedClick: (SearchScreenState.MovieUiState) -> Unit,
    onMovieClick: (Long, String) -> Unit
) {
    if (movies.itemCount != 0) {
        MovieCardList(
            state = state,
            movies = movies,
            onSavedClick = onSavedClick,
            onMovieClick = onMovieClick,
        )
    } else {
        EmptyStateContent()
    }
}

@Composable
private fun RenderTvShowResults(
    tvShows: LazyPagingItems<SearchScreenState.TvShowUiState>,
    state: LazyGridState,
    onTvShowClick: (Long, String) -> Unit
) {
    if (tvShows.itemCount != 0) {
        TvShowCardList(
            state = state,
            tvShows = tvShows,
            onTVShowClick = onTvShowClick,
        )
    } else {
        EmptyStateContent()
    }
}

@Composable
private fun RenderActorResults(
    actors: LazyPagingItems<SearchScreenState.ActorUiState>,
    state: LazyListState,
    onActorClick: (Long) -> Unit
) {
    if (actors.itemCount != 0) {
        ActorCardList(
            state = state,
            actors = actors,
            onActorClick = onActorClick,
        )
    } else {
        EmptyStateContent()
    }
}

@Composable
private fun EmptyStateContent() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        EmptySearchState(
            imagePath = if (Theme.isDarkTheme) {
                com.baghdad.design_system.R.drawable.no_search_results_night
            } else {
                com.baghdad.design_system.R.drawable.no_search_results
            },
            contentDescription = stringResource(R.string.no_search_result_picture),
            message = stringResource(R.string.no_search_result_please_try_with_another_keyword),
            modifier = Modifier.padding(bottom = 60.dp)
        )
    }
}

@Preview
@Composable
private fun SearchResultContentPreview() {
    SearchResultContent(
        selectedTab = SearchScreenState.SearchTab.MOVIES,
        onTabSelected = {},
        onSaveMovieClick = {},
        moviesState = LazyGridState(),
        tvShowsState = LazyGridState(),
        actorsState = LazyListState(),
        movies = flowOf(PagingData.empty<SearchScreenState.MovieUiState>()).collectAsLazyPagingItems(),
        actors = flowOf(PagingData.empty<SearchScreenState.ActorUiState>()).collectAsLazyPagingItems(),
        tvShows = flowOf(PagingData.empty<SearchScreenState.TvShowUiState>()).collectAsLazyPagingItems(),

        onMovieClick = { _, _ -> },
        onTvShowClick = { _, _ -> },
        onActorClick = {},
        isLoading = false
    )
}
