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
import com.baghdad.design_system.component.BackgroundBlur
import com.baghdad.design_system.component.CategoryCard
import com.baghdad.design_system.component.Chip
import com.baghdad.design_system.component.Scaffold
import com.baghdad.design_system.component.Text
import com.baghdad.design_system.theme.Theme
import com.baghdad.ui.R
import com.baghdad.ui.base.ObserveAsEffect
import com.baghdad.ui.feature.categories.component.MovieCategoryDrawableMap
import com.baghdad.ui.feature.categories.component.TvShowCategoryDrawableMap
import com.baghdad.ui.navigation.graph.categories.CategoriesNavEvent
import com.baghdad.viewmodel.categories.CategoriesEffect
import com.baghdad.viewmodel.categories.CategoriesInteractionListener
import com.baghdad.viewmodel.categories.CategoriesState
import com.baghdad.viewmodel.categories.CategoriesViewModel

@Composable
fun CategoriesScreen(
    viewModel: CategoriesViewModel = hiltViewModel(),
    handleNavigation: (CategoriesNavEvent) -> Unit,
) {
    val state by viewModel.uiState.collectAsState()

    ObserveAsEffect(viewModel.uiEffect) { effect ->
        handleEffect(effect, handleNavigation)
    }

    CategoriesScreenContent(
        state = state,
        selectedTab = state.selectedCategoriesTab,
        listener = viewModel
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
fun CategoriesScreenContent(
    state: CategoriesState,
    selectedTab: CategoriesState.CategoriesTab,
    listener: CategoriesInteractionListener
) {
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(Theme.color.surface)
            .statusBarsPadding(),
        backgroundBlur = { BackgroundBlur() }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 12.dp, end = 16.dp, start = 16.dp)
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
                    .padding(top = 16.dp, bottom = 20.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Chip(
                    title = stringResource(R.string.movies),
                    isSelected = selectedTab == CategoriesState.CategoriesTab.MOVIES,
                    onClick = { listener.onTabSelected(CategoriesState.CategoriesTab.MOVIES) }
                )
                Chip(
                    title = stringResource(R.string.tv_shows),
                    isSelected = selectedTab == CategoriesState.CategoriesTab.TV_SHOWS,
                    onClick = { listener.onTabSelected(CategoriesState.CategoriesTab.TV_SHOWS) }
                )
            }

            AnimatedContent(
                targetState = selectedTab,
                transitionSpec = { fadeIn(tween(600)) togetherWith fadeOut(tween(600)) }
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
}
@Composable
fun CategoryGrid(
    genres: List<CategoriesState.GenreUiState>,
    onCardClick: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    val gridState = rememberLazyGridState()

    LazyVerticalGrid(
        columns = Adaptive(minSize = 160.dp),
        state = gridState,
        modifier = modifier.fillMaxSize(),
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
