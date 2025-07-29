package com.baghdad.ui.feature.topRating

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.baghdad.design_system.component.Scaffold
import com.baghdad.design_system.component.SnackBar
import com.baghdad.design_system.component.Tab
import com.baghdad.design_system.component.WavyLoadingIndicator
import com.baghdad.design_system.component.appBar.TopAppBar
import com.baghdad.design_system.theme.Theme
import com.baghdad.ui.base.ObserveAsEffect
import com.baghdad.ui.base.toStringResource
import com.baghdad.ui.feature.component.HomeCard
import com.baghdad.ui.feature.component.lazyPaging.LazyPagingVerticalGrid
import com.baghdad.ui.feature.topRating.component.GenresSection
import com.baghdad.ui.navigation.graph.home.HomeNavEvent
import com.baghdad.ui.navigation.graph.home.HomeNavEvent.NavigateBack
import com.baghdad.ui.navigation.graph.home.HomeNavEvent.NavigateToMovieDetails
import com.baghdad.viewmodel.base.SnackBarState
import com.baghdad.viewmodel.errorStates.BaseSnackBarMessage
import com.baghdad.viewmodel.errorStates.SearchSnackBarMessage
import com.baghdad.viewmodel.topRating.TopRatingEffect
import com.baghdad.viewmodel.topRating.TopRatingInteractionListener
import com.baghdad.viewmodel.topRating.TopRatingState
import com.baghdad.viewmodel.topRating.TopRatingTab
import com.baghdad.viewmodel.topRating.TopRatingViewModel
import org.koin.androidx.compose.koinViewModel


@Composable
fun TopRatingScreen(
    viewModel: TopRatingViewModel = koinViewModel(),
    handleNavigation: (HomeNavEvent) -> Unit
) {
    ObserveAsEffect(viewModel.uiEffect) { effect ->
        handleEffect(effect, handleNavigation)
    }
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackBarState by viewModel.snackBarState.collectAsStateWithLifecycle()
    val movieItems = uiState.moviesFlow.collectAsLazyPagingItems()
    val tvShowItems = uiState.tvShowsFlow.collectAsLazyPagingItems()

    TopRatingContent(
        uiState = uiState,
        listener = viewModel,
        snackBarState = snackBarState,
        movieItems = movieItems,
        tvShowItems = tvShowItems
    )
}

private fun handleEffect(
    effect: TopRatingEffect,
    handleNavigation: (HomeNavEvent) -> Unit
) {
    when (effect) {

        TopRatingEffect.NavigateBack -> handleNavigation(
            NavigateBack
        )

        is TopRatingEffect.NavigateToMovieDetails -> handleNavigation(
            NavigateToMovieDetails(effect.movieId)
        )

        is TopRatingEffect.NavigateToTvShowDetails -> handleNavigation(
            HomeNavEvent.NavigateToTvShowDetails(effect.tvShowId)
        )
    }
}

@Composable
private fun TopRatingContent(
    uiState: TopRatingState,
    listener: TopRatingInteractionListener,
    snackBarState: SnackBarState,
    movieItems: LazyPagingItems<TopRatingState.MovieUiState>,
    tvShowItems: LazyPagingItems<TopRatingState.TvShowUiState>
) {
    Scaffold(
        modifier = Modifier
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
                screenTitle = stringResource(com.baghdad.ui.R.string.top_rating),
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Theme.color.surface)
                    .padding(top = 4.dp, bottom = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            )
            {
                Tab(
                    text = stringResource(com.baghdad.ui.R.string.movies),
                    onClick = { listener.onSelectedTab(TopRatingTab.MOVIES) },
                    isSelected = uiState.selectedTab == TopRatingTab.MOVIES,
                    modifier = Modifier.weight(1f)
                )
                Tab(
                    text = stringResource(com.baghdad.ui.R.string.tv_shows),
                    onClick = { listener.onSelectedTab(TopRatingTab.TV_SHOWS) },
                    isSelected = uiState.selectedTab == TopRatingTab.TV_SHOWS,
                    modifier = Modifier.weight(1f)
                )
            }

            key(uiState.selectedTab) {
                GenresSection(
                    allGenres = uiState.genres,
                    selectedGenres = uiState.selectedGenreId,
                    onGenreSelected = { listener.onGenreClick(it?.id) },
                    modifier = Modifier
                        .background(Theme.color.surface)
                        .padding(bottom = 12.dp)
                )
            }
        },
        snackbar = {
            SnackBar(
                message = stringResource(snackBarMessage(snackBarState.message)),
                isSuccess = snackBarState.isSuccess,
                isVisible = snackBarState.isVisible
            )
        }
    ) {

        if (uiState.isLoading) {
            Box(Modifier.fillMaxSize()) {
                WavyLoadingIndicator(modifier = Modifier.align(Alignment.Center))
            }
        }

        when (uiState.selectedTab) {
            TopRatingTab.MOVIES -> {
                LazyPagingVerticalGrid<TopRatingState.MovieUiState>(
                    columns = GridCells.Adaptive(minSize = 150.dp),
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Theme.color.surface),
                    contentPadding = PaddingValues(
                        bottom = 12.dp,
                        end = 16.dp,
                        start = 16.dp,
                    ),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    items = movieItems,
                ) { movie ->
                    Box(contentAlignment = Alignment.TopCenter) {

                    }

                    HomeCard(
                        url = movie.posterPictureURL,
                        contentDescription = null,
                        isSaved = movie.isSaved,
                        onSavedClick = { listener.onSaveMovieClick(movie.id) },
                        onClick = { listener.onMovieDetailsClick(movie.id) },
                        modifier = Modifier.aspectRatio(0.8f)
                    )
                }
            }

            TopRatingTab.TV_SHOWS -> {
                LazyPagingVerticalGrid<TopRatingState.TvShowUiState>(
                    columns = GridCells.Adaptive(minSize = 150.dp),
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Theme.color.surface),
                    contentPadding = PaddingValues(
                        start = 16.dp,
                        end = 16.dp,
                        top = 8.dp,
                        bottom = 8.dp
                    ),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    items = tvShowItems,
                ) { tvShow ->
                    HomeCard(
                        url = tvShow.posterPictureURL,
                        contentDescription = null,
                        isSaved = tvShow.isSaved,
                        onSavedClick = { listener.onSaveTvShowClick(tvShow.id) },
                        onClick = { listener.onTvShowDetailsClick(tvShow.id) },
                        modifier = Modifier.aspectRatio(0.8f)
                    )
                }
            }
        }
    }
}

@Composable
private fun snackBarMessage(type: BaseSnackBarMessage): Int {
    return when (type) {
        SearchSnackBarMessage.SavedItemSuccessfully -> com.baghdad.ui.R.string.snackbar_saved_success
        else -> type.toStringResource()
    }
}
