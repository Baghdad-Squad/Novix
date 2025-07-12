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
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.baghdad.component.ActorCardList
import com.baghdad.component.MovieCardList
import com.baghdad.component.TvShowCardList
import com.baghdad.design_system.component.Chip
import com.baghdad.design_system.component.WavyLoadingIndicator
import com.baghdad.design_system.theme.Theme
import com.baghdad.ui.R

import com.baghdad.viewmodel.search.SearchScreenState
import com.baghdad.viewmodel.search.SearchTab


@Composable
fun SearchResultContent(
    selectedTab: SearchTab,
    onTabSelected: (SearchTab) -> Unit,
    onSavedClick: (Long) -> Unit,
    movies: List<SearchScreenState.MovieUiState>,
    tvShows: List<SearchScreenState.TvShowUiState>,
    actors: List<SearchScreenState.ActorUiState>,
    onMovieClick: (Long) -> Unit,
    onTvShowClick: (Long) -> Unit,
    onActorClick: (Long) -> Unit,
    isLoading: Boolean ,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Theme.color.surface)
            .padding(bottom = 16.dp),

        ) {
        if (isLoading) {
            Box(Modifier.fillMaxSize()) {
                WavyLoadingIndicator(modifier = Modifier.align(Alignment.Center))
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.Start),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Chip(
                title = stringResource(id = R.string.tab_movies),
                isSelected = selectedTab == SearchTab.MOVIES,
                onClick = { onTabSelected(SearchTab.MOVIES) }
            )
            Chip(
                title = stringResource(id = R.string.tab_tv_shows),
                isSelected = selectedTab == SearchTab.TV_SHOWS,
                onClick = { onTabSelected(SearchTab.TV_SHOWS) }
            )
            Chip(
                title = stringResource(id = R.string.tab_actors),
                isSelected = selectedTab == SearchTab.ACTORS,
                onClick = { onTabSelected(SearchTab.ACTORS) }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))


        when (selectedTab) {
            SearchTab.MOVIES -> {
                MovieCardList(
                    movies = movies,
                    onSavedClick = onSavedClick,
                    onMovieClick = onMovieClick,
                )
            }

            SearchTab.TV_SHOWS -> {
                TvShowCardList(
                    tvShows = tvShows,
                    onSavedClick = onSavedClick,
                    onTVShowClick = onTvShowClick,
                )
            }

            SearchTab.ACTORS -> {
                ActorCardList(
                    actors = actors,
                    onActorClick = onActorClick,
                )
            }
        }
    }
}