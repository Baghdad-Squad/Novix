package com.baghdad.ui.feature.categories

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.grid.GridCells.Adaptive
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.baghdad.design_system.component.BackgroundBlur
import com.baghdad.design_system.component.CategoryCard
import com.baghdad.design_system.component.Chip
import com.baghdad.design_system.component.Text
import com.baghdad.design_system.component.scaffold.Scaffold
import com.baghdad.design_system.theme.Theme
import com.baghdad.ui.R
import com.baghdad.ui.base.ObserveAsEffect
import com.baghdad.ui.base.toStringResource
import com.baghdad.ui.feature.categories.component.MovieCategoryDrawableMap
import com.baghdad.ui.feature.categories.component.TvShowCategoryDrawableMap
import com.baghdad.ui.navigation.graph.categories.CategoriesNavEvent
import com.baghdad.ui.util.toScaffoldSnackBarState
import com.baghdad.viewmodel.base.SnackBarState
import com.baghdad.viewmodel.categories.CategoriesEffect
import com.baghdad.viewmodel.categories.CategoriesInteractionListener
import com.baghdad.viewmodel.categories.CategoriesState
import com.baghdad.viewmodel.categories.CategoriesViewModel
import com.baghdad.viewmodel.errorStates.BaseSnackBarMessage

@Composable
fun CategoriesScreen(
    viewModel: CategoriesViewModel = hiltViewModel(),
    handleNavigation: (CategoriesNavEvent) -> Unit,
) {
    val state by viewModel.uiState.collectAsState()
    val snackBarState = viewModel.snackBarState.collectAsStateWithLifecycle()

    ObserveAsEffect(viewModel.uiEffect) { effect ->
        handleEffect(effect, handleNavigation)
    }

    CategoriesScreenContent(
        state = state,
        listener = viewModel,
        snackBarState = snackBarState.value,
    )
}

private fun handleEffect(
    effect: CategoriesEffect,
    handleNavigation: (CategoriesNavEvent) -> Unit,
) {
    when (effect) {
        is CategoriesEffect.NavigateToCategoryMovies -> {
            handleNavigation(CategoriesNavEvent.NavigateToCategoryMovies(effect.id))
        }
        is CategoriesEffect.NavigateToCategoryTVShows -> {
            handleNavigation(CategoriesNavEvent.NavigateToCategoryTvShows(effect.id))
        }
    }
}

@Composable
private fun CategoriesScreenContent(
    state: CategoriesState,
    listener: CategoriesInteractionListener,
    snackBarState: SnackBarState
) {
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(Theme.color.surface)
            .statusBarsPadding(),
        topBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 25.dp, end = 16.dp, start = 16.dp)
            ) {
                Text(
                    text = stringResource(R.string.categories),
                    style = Theme.typography.title.large,
                    color = Theme.color.title,
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp, bottom = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Chip(
                        title = stringResource(R.string.movies),
                        isSelected = state.selectedCategoriesTab == CategoriesState.CategoriesTab.MOVIES,
                        onClick = { listener.onTabSelected(CategoriesState.CategoriesTab.MOVIES) }
                    )
                    Chip(
                        title = stringResource(R.string.tv_shows),
                        isSelected = state.selectedCategoriesTab == CategoriesState.CategoriesTab.TV_SHOWS,
                        onClick = { listener.onTabSelected(CategoriesState.CategoriesTab.TV_SHOWS) }
                    )
                }
            }
        },
        snackBarState = snackBarState.toScaffoldSnackBarState(::mapSnackBarMessage),
        onSnackBarActionClick = listener::onSnackBarActionLabelClicked,
        backgroundContent = { BackgroundBlur() },
        isLoading = state.isLoading
    ) {
        AnimatedContent(
            targetState = state.selectedCategoriesTab,
            transitionSpec = { fadeIn(tween(600)) togetherWith fadeOut(tween(600)) },
        ) { tab ->
            when (tab) {
                CategoriesState.CategoriesTab.MOVIES -> {
                    CategoryGrid(
                        genres = state.movieGenres,
                        onCardClick = { listener.onCategoryMovieClick(it) }
                    )
                }
                CategoriesState.CategoriesTab.TV_SHOWS -> {
                    CategoryGrid(
                        genres = state.tvShowGenres,
                        onCardClick = { listener.onCategoryTvShowClick(it) }
                    )
                }
            }
        }
    }
}

@Composable
fun CategoryGrid(
    genres: List<CategoriesState.GenreUiState>,
    onCardClick: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    val gridState = rememberLazyGridState()

    LazyVerticalGrid(
        columns = Adaptive(minSize = 150.dp),
        state = gridState,
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(bottom = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(genres) { genre ->
            val imageRes = when {
                genre.movieCategory != null -> MovieCategoryDrawableMap.map[genre.movieCategory]
                genre.tvShowCategory != null -> TvShowCategoryDrawableMap.map[genre.tvShowCategory]
                else -> null
            }
            imageRes?.let {
                CategoryCard(
                    title = genre.name,
                    image = painterResource(id = imageRes),
                    onClick = { onCardClick(genre.id) }
                )
            }
        }
    }
}

private fun mapSnackBarMessage(type: BaseSnackBarMessage): Int = type.toStringResource()