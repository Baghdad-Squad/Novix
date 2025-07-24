package com.baghdad.ui.feature.trendingTvShow

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.baghdad.design_system.component.Scaffold
import com.baghdad.design_system.component.SnackBar
import com.baghdad.design_system.component.Text
import com.baghdad.design_system.component.WavyLoadingIndicator
import com.baghdad.design_system.component.button.IconButton
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
import org.koin.androidx.compose.koinViewModel

@Composable
fun TrendingTvShowScreen(
    viewModel: TrendingTvShowViewModel = koinViewModel(),
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
                isVisible = snackBarState.isVisible
            )
        },
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp, horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    icon = painterResource(com.baghdad.design_system.R.drawable.ic_go_back),
                    onClick = {
                        listener.onBackIconClick()
                    },
                    size = Pair(40.dp, 40.dp),
                    modifier = Modifier
                )
                Text(
                    text = stringResource(R.string.trending_tv_shows),
                    style = Theme.typography.title.large,
                    color = Theme.color.title,
                    modifier = Modifier
                        .padding(start = 12.dp)
                )
            }
        }
    ) {
        Column(
            modifier = Modifier
                .background(Theme.color.surface)
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
        ) {
            GenresSection(
                allGenres = uiState.genres,
                selectedGenre = uiState.selectedGenreId,
                onGenreSelected = { listener.onGenreClick(it.id) },
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
            )

            if (uiState.isLoading) {
                Box(Modifier.fillMaxSize()) {
                    WavyLoadingIndicator(modifier = Modifier.align(Alignment.Center))
                }
            }

            LazyPagingVerticalGrid<TrendingTvShowScreenState.TvShowUiState>(
                columns = GridCells.Adaptive(minSize = 150.dp),
                modifier = Modifier
                    .fillMaxSize()
                    .background(Theme.color.surface),
                contentPadding = PaddingValues(
                    horizontal = 16.dp,
                    vertical = 12.dp
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
