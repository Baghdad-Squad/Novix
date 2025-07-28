package com.baghdad.ui.feature.trendingMovies

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.baghdad.design_system.component.Chip
import com.baghdad.design_system.component.Scaffold
import com.baghdad.design_system.component.SnackBar
import com.baghdad.design_system.component.appBar.TopAppBar
import com.baghdad.design_system.theme.Theme
import com.baghdad.ui.R
import com.baghdad.ui.base.ObserveAsEffect
import com.baghdad.ui.base.toStringResource
import com.baghdad.ui.feature.component.HomeCard
import com.baghdad.ui.feature.component.lazyPaging.LazyPagingVerticalGrid
import com.baghdad.ui.navigation.graph.home.HomeNavEvent
import com.baghdad.ui.navigation.graph.home.HomeNavEvent.NavigateToMovieDetails
import com.baghdad.viewmodel.base.SnackBarState
import com.baghdad.viewmodel.errorStates.BaseSnackBarMessage
import com.baghdad.viewmodel.trendingMovie.TrendingMoviesEffect
import com.baghdad.viewmodel.trendingMovie.TrendingMoviesInteractionListener
import com.baghdad.viewmodel.trendingMovie.TrendingMoviesScreenState
import com.baghdad.viewmodel.trendingMovie.TrendingMoviesViewModel
import org.koin.androidx.compose.koinViewModel


@Composable
fun TrendingMoviesScreen(
    viewModel: TrendingMoviesViewModel = koinViewModel(),
    handleNavigation: (HomeNavEvent) -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackBarState by viewModel.snackBarState.collectAsStateWithLifecycle()
    val movieItems =
        uiState.movies.collectAsLazyPagingItems()



    ObserveAsEffect(viewModel.uiEffect) { effect ->
        handleEffect(effect, handleNavigation)
    }

    TrendingMoviesContent(
        movieItems = movieItems,
        uiState = uiState,
        listener = viewModel,
        snackBarState = snackBarState
    )
}

private fun handleEffect(
    effect: TrendingMoviesEffect,
    handleNavigation: (HomeNavEvent) -> Unit,
) {
    when (effect) {
        is TrendingMoviesEffect.NavigateBack -> handleNavigation(
            HomeNavEvent.NavigateBack
        )

        is TrendingMoviesEffect.NavigateToMovieDetails -> handleNavigation(
            NavigateToMovieDetails(effect.movieId)
        )
    }
}
@Composable
private fun TrendingMoviesContent(
    movieItems: LazyPagingItems<TrendingMoviesScreenState.TrendingMovieUiState>,
    uiState: TrendingMoviesScreenState,
    listener: TrendingMoviesInteractionListener,
    snackBarState: SnackBarState
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
                onGoBackClick = listener::onBackClick,
                screenTitle = stringResource(R.string.trending_movies),
            ) {}
        }, snackbar = {
            SnackBar(
                message = stringResource(snackBarMessage(snackBarState.message)),
                isSuccess = snackBarState.isSuccess,
                isVisible = snackBarState.isVisible
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Theme.color.surface)
        ) {
            LazyRow(
                contentPadding = PaddingValues(
                    start = 16.dp,
                    end = 16.dp,
                    bottom = 12.dp,
                    top = 12.dp
                ),
            ) {
                item {
                    Chip(
                        title = stringResource(R.string.all),
                        isSelected = uiState.selectedGenreId == null,
                        onClick = { listener.onCategoryClick(null) }
                    )
                }
                items(uiState.categories) { category ->
                    Chip(
                        title = category.name,
                        isSelected = category.id == uiState.selectedGenreId,
                        onClick = { listener.onCategoryClick(category.id) }
                    )
                }
            }
            LazyPagingVerticalGrid(
                items = movieItems,
                key = { it.id },
                columns = GridCells.Adaptive(minSize = 158.dp),
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(
                    start = 16.dp,
                    end = 16.dp,
                    bottom = 12.dp
                ),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) { movie ->
                HomeCard(
                    url = movie.posterPictureURL,
                    isSaved = movie.isSaved,
                    contentDescription = stringResource(R.string.movie_card),
                    onSavedClick = { listener.onToggleSaveMovie(movie.id) },
                    onClick = { listener.onMovieClick(movie.id) },
                )
            }
        }
    }
}

@Composable
private fun snackBarMessage(type: BaseSnackBarMessage): Int {
    return type.toStringResource()

}

