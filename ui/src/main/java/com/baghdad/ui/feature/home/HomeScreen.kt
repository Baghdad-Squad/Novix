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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.baghdad.design_system.component.Scaffold
import com.baghdad.design_system.component.SnackBar
import com.baghdad.design_system.component.appBar.HomeAppBar
import com.baghdad.design_system.theme.Theme
import com.baghdad.ui.base.ObserveAsEffect
import com.baghdad.ui.base.toStringResource
import com.baghdad.ui.feature.home.component.ContinueWatchingSection
import com.baghdad.ui.feature.home.component.PopularSection
import com.baghdad.ui.feature.home.component.TopRatingSection
import com.baghdad.ui.feature.home.component.upcomingSection
import com.baghdad.ui.navigation.graph.home.HomeNavEvent
import com.baghdad.viewmodel.base.SnackBarState
import com.baghdad.viewmodel.errorStates.BaseSnackBarMessage
import com.baghdad.viewmodel.home.HomeInteractionListener
import com.baghdad.viewmodel.home.HomeScreenEffect
import com.baghdad.viewmodel.home.HomeScreenState
import com.baghdad.viewmodel.home.HomeViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = koinViewModel(),
    handleNavigation: (HomeNavEvent) -> Unit
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val upcomingItems = state.upcomingItems.collectAsLazyPagingItems()
    val snackBarState = viewModel.snackBarState.collectAsStateWithLifecycle()

    ObserveAsEffect(viewModel.uiEffect) { effect ->
        handleEffect(effect, handleNavigation)
    }

    HomeContent(
        state = state,
        upcomingItems = upcomingItems,
        interactionListener = viewModel,
        snackBarState = snackBarState.value
    )
}

@Composable
private fun HomeContent(
    state: HomeScreenState,
    upcomingItems: LazyPagingItems<HomeScreenState.UpcomingItemUiState>,
    interactionListener: HomeInteractionListener,
    snackBarState: SnackBarState
) {
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(Theme.color.surface)
            .statusBarsPadding(),
        topBar = { HomeAppBar(modifier = Modifier.padding(top = 12.dp)) },
        snackbar = {
            SnackBar(
                message = stringResource(snackBarMessage(snackBarState.message)),
                isSuccess = snackBarState.isSuccess,
                isVisible = snackBarState.isVisible
            )
        }
    ) {

        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 150.dp),
            contentPadding = PaddingValues(vertical = 16.dp),
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            item(span = { GridItemSpan(maxLineSpan) }) {
                PopularSection(
                    isLoading = state.isPopularLoading,
                    popularItems = state.popularItems,
                    onClick = interactionListener::onPopularItemClicked,
                    onSaveClick = interactionListener::onPopularItemSaveClicked,
                )
            }
            // What you want to watch section

            item(span = { GridItemSpan(maxLineSpan) }) {
                TopRatingSection(
                    isLoading = state.isTopRatingLoading,
                    items = state.topRatingItems,
                    onClick = interactionListener::onTopRatingItemClicked,
                    onSaveClick = interactionListener::onTopRatingItemSaveClicked,
                    onViewAllClick = interactionListener::onViewAllTopRatingClicked,
                )
            }

            item(span = { GridItemSpan(maxLineSpan) }) {
                ContinueWatchingSection(
                    isLoading = state.isContinueWatchingLoading,
                    items = state.continueWatchingItems,
                    onClick = interactionListener::onContinueWatchingItemClicked,
                    onSaveClick = interactionListener::onContinueWatchingItemSaveClicked,
                    onViewAllClick = interactionListener::onViewAllContinueWatchingClicked
                )
            }

            upcomingSection(
                selectedGenreId = state.selectedUpcomingGenreId,
                genres = state.upcomingGenres,
                isGenresLoading = state.isUpcomingGenresLoading,
                onGenreSelected = interactionListener::onUpcomingGenreSelected,
                upcomingItems = upcomingItems,
                onUpcomingItemClicked = interactionListener::onUpcomingItemClicked,
                onUpcomingItemSaveClicked = interactionListener::onUpcomingItemSaveClicked
            )
        }
    }
}

@Composable
private fun snackBarMessage(type: BaseSnackBarMessage): Int {
    return when (type) {
        else -> type.toStringResource()
    }
}

private fun handleEffect(
    effect: HomeScreenEffect,
    handleNavigation: (HomeNavEvent) -> Unit
) {
    when (effect) {
        is HomeScreenEffect.NavigateToMovieDetails -> {
            handleNavigation(HomeNavEvent.NavigateToMovieDetails(effect.movieId))
        }

        is HomeScreenEffect.NavigateToTvShowDetails -> {
            handleNavigation(HomeNavEvent.NavigateToTvShowDetails(effect.tvShowId))
        }
    }
}