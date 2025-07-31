package com.baghdad.ui.feature.continueWatching

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
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.baghdad.design_system.R
import com.baghdad.design_system.component.Chip
import com.baghdad.design_system.component.Scaffold
import com.baghdad.design_system.component.SnackBar
import com.baghdad.design_system.component.Tab
import com.baghdad.design_system.component.appBar.TopAppBar
import com.baghdad.design_system.theme.Theme
import com.baghdad.ui.base.ObserveAsEffect
import com.baghdad.ui.base.toStringResource
import com.baghdad.ui.feature.component.HomeCard
import com.baghdad.ui.feature.component.lazyPaging.LazyPagingVerticalGrid
import com.baghdad.ui.navigation.graph.home.HomeNavEvent
import com.baghdad.ui.navigation.graph.home.HomeNavEvent.NavigateToMovieDetails
import com.baghdad.ui.navigation.graph.home.HomeNavEvent.NavigateToTvShowDetails
import com.baghdad.viewmodel.base.SnackBarState
import com.baghdad.viewmodel.continueWatching.ContinueWatchingInteractionListener
import com.baghdad.viewmodel.continueWatching.ContinueWatchingScreenEffect
import com.baghdad.viewmodel.continueWatching.ContinueWatchingState
import com.baghdad.viewmodel.continueWatching.ContinueWatchingViewModel
import com.baghdad.viewmodel.errorStates.BaseSnackBarMessage


@Composable
fun ContinueWatchingScreen(
    viewModel: ContinueWatchingViewModel = hiltViewModel(),
    handleNavigation: (HomeNavEvent) -> Unit,

    ) {
    val snackBarState by viewModel.snackBarState.collectAsStateWithLifecycle()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val mediaItems = uiState.mediaFlow.collectAsLazyPagingItems()

    ObserveAsEffect(viewModel.uiEffect) { effect ->
        handleEffect(effect, handleNavigation)
    }
    ContinueWatchingContent(
        uiState = uiState,
        listener = viewModel,
        mediaItems = mediaItems,
        snackBarState = snackBarState,
    )
}

private fun handleEffect(
    effect: ContinueWatchingScreenEffect,
    handleNavigation: (HomeNavEvent) -> Unit,
) {
    when (effect) {
        is ContinueWatchingScreenEffect.NavigateBack -> handleNavigation(
            HomeNavEvent.NavigateBack
        )

        is ContinueWatchingScreenEffect.NavigateToLogin -> handleNavigation(
            HomeNavEvent.NavigateToLogin
        )

        is ContinueWatchingScreenEffect.NavigateToMovieDetails -> handleNavigation(
            NavigateToMovieDetails(effect.movieId)
        )

        is ContinueWatchingScreenEffect.NavigateToTvShowDetails -> handleNavigation(
            NavigateToTvShowDetails(effect.tvShowId)
        )
    }
}


@Composable
fun ContinueWatchingContent(
    uiState: ContinueWatchingState,
    mediaItems: LazyPagingItems<ContinueWatchingState.ContinueWatchingMovieUiState>,
    listener: ContinueWatchingInteractionListener,
    snackBarState: SnackBarState,
    modifier: Modifier = Modifier
) {

    val movieGenresScrollState = rememberLazyListState()
    val tvGenresScrollState = rememberLazyListState()

    Scaffold(
        modifier = modifier
            .background(Theme.color.surface)
            .systemBarsPadding()
            .statusBarsPadding(),
        topBar = {
            TopAppBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(top = 22.dp, bottom = 8.dp)
                    .background(Theme.color.surface),
                onGoBackClick = {
                    listener.onBackClick()
                },
                screenTitle = stringResource(com.baghdad.ui.R.string.continue_watching),

                )
        },
        snackbar = {
            SnackBar(
                message = stringResource(snackBarMessage(snackBarState.message)),
                isSuccess = snackBarState.isSuccess,
                isVisible = snackBarState.isVisible
            )
        }
    ) {
        Column(
            modifier = Modifier
                .background(Theme.color.surface)
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
        ) {
            val borderColor = Theme.color.stroke
            Box(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Theme.color.surface)
                        .padding(top = 4.dp)
                        .drawBehind {
                            val strokeWidth = 1.dp.toPx()
                            val y = size.height - strokeWidth / 2
                            drawLine(
                                color = borderColor,
                                start = Offset(0f, y),
                                end = Offset(size.width, y),
                                strokeWidth = strokeWidth
                            )
                        },
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Tab(
                        text = stringResource(com.baghdad.ui.R.string.movies),
                        onClick = { listener.onSelectedTab(true) },
                        isSelected = uiState.selectedMediaTabIsMovie,
                        modifier = Modifier.weight(1f)
                    )
                    Tab(
                        text = stringResource(com.baghdad.ui.R.string.tv_shows),
                        onClick = { listener.onSelectedTab(false) },
                        isSelected = !uiState.selectedMediaTabIsMovie,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
            GenresTabs(
                genres = uiState.genres,
                selectedTab = when (uiState.selectedMediaTabIsMovie) {
                    true -> uiState.selectedMovieGenreId
                    false -> uiState.selectedTvShowGenreId
                },
                genresScrollState = when (uiState.selectedMediaTabIsMovie) {
                    true -> movieGenresScrollState
                    false -> tvGenresScrollState
                },
                onTabClick = { listener.onGenreClick(it) },
                modifier = Modifier.padding(vertical = 12.dp)
            )
            LazyPagingVerticalGrid<ContinueWatchingState.ContinueWatchingMovieUiState>(
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
                items = mediaItems,
            ) { media ->

                HomeCard(
                    url = media.posterPictureURL,
                    contentDescription = null,
                    isSaved = media.isSaved,
                    onSavedClick = { listener.onMovieSaveClick(media.id) },
                    onClick = { listener.onMediaClick(media.id, media.contentType) },
                    modifier = Modifier.aspectRatio(0.8f)
                )
            }
        }
    }

}


@Composable
private fun GenresTabs(
    genres: List<ContinueWatchingState.GenreUiState>,
    selectedTab: Long?,
    onTabClick: (Long?) -> Unit,
    genresScrollState: LazyListState,
    modifier: Modifier = Modifier
) {

    LazyRow(
        modifier = modifier
            .wrapContentSize(),
        state = genresScrollState,
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        contentPadding = PaddingValues(horizontal = 16.dp)
    ) {
        item {
            Chip(
                title = stringResource(R.string.all),
                isSelected = selectedTab == null,
                onClick = { onTabClick(null) },
            )
        }
        items(genres.size) { index ->
            Chip(
                title = genres[index].name,
                isSelected = selectedTab == genres[index].id,
                onClick = { onTabClick(genres[index].id) }
            )
        }
    }
}

@Composable
private fun snackBarMessage(type: BaseSnackBarMessage): Int {
    return type.toStringResource()
}
