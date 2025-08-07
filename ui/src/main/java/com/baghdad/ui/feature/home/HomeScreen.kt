package com.baghdad.ui.feature.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.collectAsLazyPagingItems
import com.baghdad.design_system.component.Scaffold
import com.baghdad.design_system.component.SnackBar
import com.baghdad.design_system.component.appBar.HomeAppBar
import com.baghdad.design_system.theme.Theme
import com.baghdad.ui.base.ObserveAsEffect
import com.baghdad.ui.base.toStringResource
import com.baghdad.ui.feature.component.bottomSheet.AddListBottomSheet
import com.baghdad.ui.feature.component.bottomSheet.SavedListBottomSheet
import com.baghdad.ui.feature.home.component.ContinueWatchingSection
import com.baghdad.ui.feature.home.component.PopularSection
import com.baghdad.ui.feature.home.component.TopRatingSection
import com.baghdad.ui.feature.home.component.WhatToWatchSection
import com.baghdad.ui.feature.home.component.upcomingSection
import com.baghdad.ui.navigation.graph.home.HomeNavEvent
import com.baghdad.ui.navigation.graph.home.HomeNavEvent.NavigateToActors
import com.baghdad.ui.navigation.graph.home.HomeNavEvent.NavigateToContinueWatching
import com.baghdad.ui.navigation.graph.home.HomeNavEvent.NavigateToLogin
import com.baghdad.ui.navigation.graph.home.HomeNavEvent.NavigateToMovieDetails
import com.baghdad.ui.navigation.graph.home.HomeNavEvent.NavigateToMovies
import com.baghdad.ui.navigation.graph.home.HomeNavEvent.NavigateToTopRatingMovies
import com.baghdad.ui.navigation.graph.home.HomeNavEvent.NavigateToTvShowDetails
import com.baghdad.ui.navigation.graph.home.HomeNavEvent.NavigateToTvShows
import com.baghdad.viewmodel.base.SnackBarState
import com.baghdad.viewmodel.errorStates.BaseSnackBarMessage
import com.baghdad.viewmodel.home.HomeInteractionListener
import com.baghdad.viewmodel.home.HomeScreenEffect
import com.baghdad.viewmodel.home.HomeScreenState
import com.baghdad.viewmodel.home.HomeViewModel

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    handleNavigation: (HomeNavEvent) -> Unit,
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val snackBarState = viewModel.snackBarState.collectAsStateWithLifecycle()

    ObserveAsEffect(viewModel.uiEffect) { effect ->
        handleEffect(effect, handleNavigation)
    }

    HomeContent(
        state = state,
        interactionListener = viewModel,
        snackBarState = snackBarState.value,
    )
}

@Composable
private fun HomeContent(
    state: HomeScreenState,
    interactionListener: HomeInteractionListener,
    snackBarState: SnackBarState,
) {
    val lazyGridState = rememberLazyGridState()
    val savedLists = state.addToListBottomSheetState.savedLists.collectAsLazyPagingItems()
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(Theme.color.surface)
            .statusBarsPadding(),
        topBar = { HomeAppBar(modifier = Modifier.padding(top = 12.dp, bottom = 8.dp)) },
        snackbar = { position ->
            SnackBar(
                message = stringResource(snackBarMessage(snackBarState.message)),
                isSuccess = snackBarState.isSuccess,
                isVisible = snackBarState.isVisible,
                actionLabel = snackBarState.actionLabelRes?.let { stringResource(it) },
                onActionClick = interactionListener::onSnackBarActionLabelClicked,
                position = position,
            )
        },
        isSnackBarWithActionLabel = snackBarState.actionLabelRes != null,
    ) {
        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 150.dp),
            contentPadding = PaddingValues(bottom = 16.dp, top = 8.dp),
            state = lazyGridState,
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            item(span = { GridItemSpan(maxLineSpan) }) {
                PopularSection(
                    isLoading = state.isPopularLoading,
                    popularItems = state.popularItems,
                    onClick = interactionListener::onPopularItemClicked,
                    onSaveClick = interactionListener::onPopularItemSaveClicked,
                )
            }

            item(span = { GridItemSpan(maxLineSpan) }) {
                WhatToWatchSection(
                    modifier = Modifier.padding(top = 8.dp),
                    onMoviesClick = interactionListener::onMoviesClicked,
                    onTvShowsClick = interactionListener::onTvShowsClicked,
                    onActorsClick = interactionListener::onActorsClicked,
                )
            }

            item(span = { GridItemSpan(maxLineSpan) }) {
                TopRatingSection(
                    modifier = Modifier.padding(top = 24.dp),
                    isLoading = state.isTopRatingLoading,
                    items = state.topRatingItems,
                    onClick = interactionListener::onTopRatingItemClicked,
                    onSaveClick = interactionListener::onTopRatingItemSaveClicked,
                    onViewAllClick = interactionListener::onViewAllTopRatingClicked,
                )
            }

            item(span = { GridItemSpan(maxLineSpan) }) {
                ContinueWatchingSection(
                    modifier = Modifier.padding(top = 24.dp),
                    isLoading = state.isContinueWatchingLoading,
                    items = state.continueWatchingItems,
                    onClick = interactionListener::onContinueWatchingItemClicked,
                    onSaveClick = interactionListener::onContinueWatchingItemSaveClicked,
                    onViewAllClick = interactionListener::onViewAllContinueWatchingClicked,
                )
            }

            upcomingSection(
                modifier = Modifier.padding(top = 24.dp),
                selectedGenreId = state.selectedUpcomingGenreId,
                genres = state.upcomingGenres,
                isGenresLoading = state.isUpcomingGenresLoading,
                onGenreSelected = interactionListener::onUpcomingGenreSelected,
                upcomingItems = state.upcomingItems,
                isUpcomingItemsLoading = state.isUpcomingMoviesLoading,
                onUpcomingItemClicked = interactionListener::onUpcomingItemClicked,
                onUpcomingItemSaveClicked = interactionListener::onUpcomingItemSaveClicked,
            )
        }
        SavedListBottomSheet(
            isVisible = state.addToListBottomSheetState.isVisible,
            isUserLoggedIn = state.isUserLoggedIn,
            onAddClick = interactionListener::onSaveItemToListClicked,
            onCreateNewListClick = interactionListener::onCreateNewListClicked,
            onLoginClick = interactionListener::onLoginClicked,
            onBottomSheetCloseClick = interactionListener::onSaveToListBottomSheetDismiss,
            lists = savedLists,
            selectedListId = state.addToListBottomSheetState.selectedListId,
            onListSelected = interactionListener::onListSelected,
        )
        AddListBottomSheet(
            isVisible = state.addListBottomSheetState.isVisible,
            isLoading = state.addListBottomSheetState.isLoading,
            listName = state.addListBottomSheetState.listName,
            onDismiss = interactionListener::onCreateListBottomSheetDismiss,
            onAddClick = interactionListener::onCreateListBottomSheetAddClick,
            onListNameChange = interactionListener::onCreatedListNameChanged,
        )
    }
}

@Composable
private fun snackBarMessage(type: BaseSnackBarMessage): Int =
    when (type) {
        else -> type.toStringResource()
    }

private fun handleEffect(
    effect: HomeScreenEffect,
    handleNavigation: (HomeNavEvent) -> Unit,
) {
    when (effect) {
        is HomeScreenEffect.NavigateToMovieDetails -> {
            handleNavigation(NavigateToMovieDetails(effect.movieId))
        }

        is HomeScreenEffect.NavigateToTvShowDetails -> {
            handleNavigation(NavigateToTvShowDetails(effect.tvShowId))
        }

        HomeScreenEffect.NavigateToActors -> {
            handleNavigation(NavigateToActors)
        }

        HomeScreenEffect.NavigateToContinueWatching -> {
            handleNavigation(NavigateToContinueWatching)
        }

        HomeScreenEffect.NavigateToLogin -> {
            handleNavigation(NavigateToLogin)
        }

        HomeScreenEffect.NavigateToMovies -> {
            handleNavigation(NavigateToMovies)
        }

        HomeScreenEffect.NavigateToTopRating -> {
            handleNavigation(NavigateToTopRatingMovies)
        }

        HomeScreenEffect.NavigateToTvShows -> {
            handleNavigation(NavigateToTvShows)
        }
    }
}
