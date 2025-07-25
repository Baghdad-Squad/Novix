package com.baghdad.ui.feature.search.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
    movies: LazyPagingItems<SearchScreenState.MovieUiState>,
    tvShows: LazyPagingItems<SearchScreenState.TvShowUiState>,
    actors: LazyPagingItems<SearchScreenState.ActorUiState>,
    onMovieClick: (Long, String) -> Unit,
    onTvShowClick: (Long, String) -> Unit,
    onActorClick: (Long) -> Unit,
    isLoading: Boolean,
    modifier: Modifier = Modifier,
) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(top = 12.dp),
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

            Spacer(modifier = Modifier.height(16.dp))

            when (selectedTab) {

                SearchScreenState.SearchTab.MOVIES -> {
                    if (movies.itemCount != 0) {
                        AnimatedVisibility(
                            visible = movies.itemCount != 0,
                            enter = scaleIn(),
                            exit = scaleOut(),
                        ) {
                            MovieCardList(
                                movies = movies,
                                onSavedClick = onSavedClick,
                                onMovieClick = onMovieClick,
                            )
                        }
                    } else {
                        AnimatedVisibility(
                            visible = movies.itemCount == 0,
                            enter = scaleIn(),
                            exit = scaleOut(),
                        ) {
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


                SearchScreenState.SearchTab.TV_SHOWS -> {
                    if (tvShows.itemCount != 0) {
                        AnimatedVisibility(
                            visible = tvShows.itemCount != 0,
                            enter = scaleIn(),
                            exit = scaleOut(),
                        ) {
                            TvShowCardList(
                                tvShows = tvShows,
                                onSavedClick = onSavedClick,
                                onTVShowClick = onTvShowClick,
                            )
                        }
                    } else {
                        AnimatedVisibility(
                            visible = tvShows.itemCount != 0,
                            enter = scaleIn(animationSpec = tween(durationMillis = 1)),
                            exit = scaleOut(animationSpec = tween(durationMillis = 30)),
                        ) {
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

                SearchScreenState.SearchTab.ACTORS -> {
                    if (actors.itemCount != 0) {
                        AnimatedVisibility(
                            visible = actors.itemCount != 0,
                            enter = scaleIn(),
                            exit = scaleOut(),
                        ) {
                        ActorCardList(
                            actors = actors,
                            onActorClick = onActorClick,
                        )
                        }
                    } else {
                        AnimatedVisibility(
                            visible = actors.itemCount != 0,
                            enter = scaleIn(animationSpec = tween(durationMillis = 1)),
                            exit = scaleOut(animationSpec = tween(durationMillis = 30)),
                        ) {
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
    }
}