package com.baghdad.ui.feature.trendingTvShow

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.baghdad.design_system.component.Scaffold
import com.baghdad.design_system.component.SnackBar
import com.baghdad.design_system.component.WavyLoadingIndicator
import com.baghdad.design_system.component.appBar.TopAppBar
import com.baghdad.design_system.theme.Theme
import com.baghdad.ui.R
import com.baghdad.ui.base.ObserveAsEffect
import com.baghdad.ui.base.toStringResource
import com.baghdad.ui.feature.component.HomeCard
import com.baghdad.ui.feature.component.lazyPaging.LazyPagingVerticalGrid
import com.baghdad.ui.feature.trendingTvShow.component.GenresSection
import com.baghdad.ui.navigation.graph.home.HomeNavEvent
import com.baghdad.viewmodel.base.SnackBarState
import com.baghdad.viewmodel.errorStates.BaseSnackBarMessage
import com.baghdad.viewmodel.trendingTvShow.TrendingTvShowInteractionListener
import com.baghdad.viewmodel.trendingTvShow.TrendingTvShowScreenEffect
import com.baghdad.viewmodel.trendingTvShow.TrendingTvShowScreenState
import com.baghdad.viewmodel.trendingTvShow.TrendingTvShowViewModel

@Composable
fun TrendingTvShowScreen(
    viewModel: TrendingTvShowViewModel = hiltViewModel(),
    handleNavigation: (HomeNavEvent) -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val trendingTvShows = uiState.trendingTvShows.collectAsLazyPagingItems()
    val snackBarState by viewModel.snackBarState.collectAsStateWithLifecycle()

    TrendingTvShowContent(
        uiState = uiState,
        listener = viewModel,
        snackBarState = snackBarState,
        trendingTvShows = trendingTvShows
    )

    ObserveAsEffect(viewModel.uiEffect) { effect ->
        handleEffect(effect, handleNavigation)
    }

}

private fun handleEffect(
    effect: TrendingTvShowScreenEffect,
    handleNavigation: (HomeNavEvent) -> Unit
) {
    when (effect) {
        is TrendingTvShowScreenEffect.NavigateToTvShowDetails -> {
            handleNavigation(HomeNavEvent.NavigateToTvShowDetails(effect.tvShowId))
        }

        TrendingTvShowScreenEffect.NavigateBack -> {
            handleNavigation(HomeNavEvent.NavigateBack)
        }
    }
}

@Composable
fun TrendingTvShowContent(
    uiState: TrendingTvShowScreenState,
    listener: TrendingTvShowInteractionListener,
    snackBarState: SnackBarState,
    trendingTvShows: LazyPagingItems<TrendingTvShowScreenState.TvShowUiState>,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier
            .background(Theme.color.surface)
            .systemBarsPadding()
            .statusBarsPadding(),

        snackbar = {
            SnackBar(
                message = stringResource(snackBarMessage(snackBarState.message)),
                isSuccess = snackBarState.isSuccess,
                isVisible = snackBarState.isVisible,
                actionLabel = snackBarState.actionLabelRes?.let { stringResource(it) },
                onActionClick = {listener.onSnackBarActionLabelClick(uiState.selectedGenreId)},
            )
        },
        isLoading = uiState.isLoading,
        topBar = {
            Column {
                TopAppBar(
                    modifier = Modifier
                        .fillMaxWidth()
                        .statusBarsPadding()
                        .padding(top = 22.dp, bottom = 8.dp)
                        .background(Theme.color.surface),
                    onGoBackClick = {
                        listener.onBackIconClick()
                    },
                    screenTitle = stringResource(R.string.trending_tv_shows),

                    )
                GenresSection(
                    allGenres = uiState.genres,
                    selectedGenre = uiState.selectedGenreId,
                    onGenreSelected = { listener.onGenreClick(it?.id) },
                    modifier = Modifier.padding(vertical = 12.dp)
                )
            }
        }
    ) {
        if (uiState.isLoading) {
            Box(Modifier.fillMaxSize()) {
                WavyLoadingIndicator(modifier = Modifier.align(Alignment.Center))
            }
        }
        Column(
            modifier = Modifier
                .background(Theme.color.surface)
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
        ) {

            LazyPagingVerticalGrid<TrendingTvShowScreenState.TvShowUiState>(
                columns = GridCells.Adaptive(minSize = 150.dp),
                modifier = Modifier
                    .fillMaxSize()
                    .background(Theme.color.surface),
                contentPadding = PaddingValues(
                    start = 16.dp,
                    end = 16.dp,
                    bottom = 12.dp,
                ),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                items = trendingTvShows,
            ) { tvShow ->

                HomeCard(
                    url = tvShow.posterPictureURL,
                    contentDescription = null,
                    isSaved = tvShow.isSaved,
                    onSavedClick = { listener.onSaveTvShowClick(tvShow.id) },
                    onClick = { listener.onTvShowClick(tvShow.id) },
                    modifier = Modifier.aspectRatio(0.8f)
                )

            }
        }
    }
}

@Composable
private fun snackBarMessage(type: BaseSnackBarMessage): Int {
    return type.toStringResource()
}
