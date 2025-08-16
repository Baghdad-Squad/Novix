package com.baghdad.ui.feature.trendingTvShow

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.baghdad.design_system.component.BackgroundBlur
import com.baghdad.design_system.component.Scaffold
import com.baghdad.design_system.component.SnackBar
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
import com.baghdad.viewmodel.trendingTvShow.TrendingTvShowEffect
import com.baghdad.viewmodel.trendingTvShow.TrendingTvShowInteractionListener
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
    effect: TrendingTvShowEffect,
    handleNavigation: (HomeNavEvent) -> Unit
) {
    when (effect) {
        is TrendingTvShowEffect.NavigateToTvShowDetails -> {
            handleNavigation(HomeNavEvent.NavigateToTvShowDetails(effect.tvShowId))
        }

        TrendingTvShowEffect.NavigateBack -> {
            handleNavigation(HomeNavEvent.NavigateBack)
        }
    }
}

@Composable
private fun TrendingTvShowContent(
    uiState: TrendingTvShowScreenState,
    listener: TrendingTvShowInteractionListener,
    snackBarState: SnackBarState,
    trendingTvShows: LazyPagingItems<TrendingTvShowScreenState.TvShowUiState>,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier
            .background(Theme.color.surface)
            .statusBarsPadding()
            .padding(top = 12.dp),

        snackbar = { position ->
            SnackBar(
                message = stringResource(snackBarMessage(snackBarState.message)),
                isSuccess = snackBarState.isSuccess,
                isVisible = snackBarState.isVisible,
                actionLabel = snackBarState.actionLabelRes?.let { stringResource(it) },
                onActionClick = { listener.onSnackBarActionLabelClicked(uiState.selectedGenreId) },
                position = position,
            )
        },

        topBar = {
            Column {
                TopAppBar(
                    modifier = Modifier
                        .fillMaxWidth()
                        .statusBarsPadding()
                        .padding(vertical = 8.dp),
                    onGoBackClick = { listener.onBackIconClicked() },
                    screenTitle = stringResource(R.string.trending_tv_shows),
                )

                GenresSection(
                    allGenres = uiState.genres,
                    selectedGenre = uiState.selectedGenreId,
                    onGenreSelected = { listener.onGenreClicked(it?.id) },
                    modifier = Modifier.padding(vertical = 12.dp)
                )
            }
        },
        isSnackBarWithActionLabel = snackBarState.actionLabelRes != null,
        isLoading = uiState.isLoading,
        backgroundBlur = { BackgroundBlur() }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
        ) {
            LazyPagingVerticalGrid<TrendingTvShowScreenState.TvShowUiState>(
                columns = GridCells.Adaptive(minSize = 150.dp),
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(
                    start = 16.dp,
                    end = 16.dp,
                    bottom = 12.dp,
                ),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                items = trendingTvShows
            ) { tvShow ->
                HomeCard(
                    url = tvShow.posterPictureURL,
                    contentDescription = null,
                    isSaveToListVisible = false,
                    onClick = { listener.onTvShowClicked(tvShow.id) },
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