package com.baghdad.ui.feature.search.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import com.baghdad.design_system.component.Chip
import com.baghdad.design_system.component.WavyLoadingIndicator
import com.baghdad.design_system.theme.Theme
import com.baghdad.ui.R
import com.baghdad.viewmodel.search.SearchScreenState


@Composable
fun SearchResultContent(
    selectedTab: SearchScreenState.SearchTab,
    onTabSelected: (SearchScreenState.SearchTab) -> Unit,
    onSavedClick: (Long) -> Unit,
    moviesState: LazyGridState,
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
            .background(Theme.color.surface)
        ) {
        if (isLoading) {
            Box(Modifier.fillMaxSize()) {
                WavyLoadingIndicator(modifier = Modifier.align(Alignment.Center))
            }
        }

        Spacer(modifier = Modifier.height(12.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth(),
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

        Spacer(modifier = Modifier.height(16.dp))


        when (selectedTab) {
            SearchScreenState.SearchTab.MOVIES -> {
                if (movies.itemCount != 0) {
                    MovieCardList(
                        state = moviesState,
                        movies = movies,
                        onSavedClick = onSavedClick,
                        onMovieClick = onMovieClick,
                    )
                } else {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        EmptySearchState(
                            imagePath = com.baghdad.design_system.R.drawable.no_search_results,
                            contentDescription = stringResource(R.string.no_search_result_picture),
                            message = stringResource(R.string.no_search_result_please_try_with_another_keyword),
                        )
                    }
                }

            }

            SearchScreenState.SearchTab.TV_SHOWS -> {
                if (tvShows.itemCount != 0) {
                    TvShowCardList(
                        tvShows = tvShows,
                        onSavedClick = onSavedClick,
                        onTVShowClick = onTvShowClick,
                    )
                } else {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        EmptySearchState(
                            imagePath = com.baghdad.design_system.R.drawable.no_search_results,
                            contentDescription = stringResource(R.string.no_search_result_picture),
                            message = stringResource(R.string.no_search_result_please_try_with_another_keyword),
                        )
                    }
                }
            }

            SearchScreenState.SearchTab.ACTORS -> {
                if (actors.itemCount != 0) {
                    ActorCardList(
                        actors = actors,
                        onActorClick = onActorClick,
                    )
                } else {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        EmptySearchState(
                            imagePath = com.baghdad.design_system.R.drawable.no_search_results,
                            contentDescription = stringResource(R.string.no_search_result_picture),
                            message = stringResource(R.string.no_search_result_please_try_with_another_keyword),
                        )
                    }
                }
            }
        }
    }

}
