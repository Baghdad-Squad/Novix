package com.baghdad.ui.feature.topRating

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.baghdad.design_system.component.Scaffold
import com.baghdad.design_system.component.SnackBar
import com.baghdad.design_system.component.Text
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
import com.baghdad.viewmodel.topRating.TopRatingEffect
import com.baghdad.viewmodel.topRating.TopRatingInteractionListener
import com.baghdad.viewmodel.topRating.TopRatingMovieState
import com.baghdad.viewmodel.topRating.TopRatingViewModel
import org.koin.androidx.compose.koinViewModel


@Composable
fun TopRatingMoviesScreen(
    viewModel: TopRatingViewModel = koinViewModel(),
    handleNavigation: (HomeNavEvent) -> Unit
) {
    ObserveAsEffect(viewModel.uiEffect) { effect ->
        handleEffect(effect, handleNavigation)
    }
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackBarState by viewModel.snackBarState.collectAsStateWithLifecycle()
    val movieItems = uiState.moviesFlow.collectAsLazyPagingItems()

    TopRatingMoviesContent(
        uiState = uiState,
        listener = viewModel,
        movieItems = movieItems,
        snackBarState = snackBarState
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
    }
}

@Composable
fun TopRatingMoviesContent(
    uiState: TopRatingMovieState,
    listener: TopRatingInteractionListener,
    snackBarState: SnackBarState,
    movieItems: LazyPagingItems<TopRatingMovieState.MovieUiState>,
    modifier: Modifier = Modifier
) {
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
                    .padding(top = 22.dp, bottom = 12.dp)
                    .background(Theme.color.surface),
                onGoBackClick = {
                    listener.onBackClick()
                },
                content = {
                    Text(
                        text = stringResource(com.baghdad.ui.R.string.top_rating),
                        style = Theme.typography.title.large,
                        color = Theme.color.title,
                        modifier = Modifier
                            .padding(start = 8.dp)
                    )
                }
            )
            GenresSection(
                allGenres = uiState.genres,
                selectedGenres = uiState.selectedGenreId,
                onGenreSelected = { listener.onGenreClick(it?.id) },
                modifier = Modifier
                    .background(Theme.color.surface)
                    .padding(start = 16.dp, top = 12.dp, bottom = 12.dp)
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

        if (uiState.isLoading) {
            Box(Modifier.fillMaxSize()) {
                WavyLoadingIndicator(modifier = Modifier.align(Alignment.Center))
            }
        }
        LazyPagingVerticalGrid<TopRatingMovieState.MovieUiState>(
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
            items = movieItems,
        ) { movie ->

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
}

@Composable
private fun snackBarMessage(type: BaseSnackBarMessage): Int {
    return type.toStringResource()
}
