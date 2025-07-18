package com.baghdad.ui.feature.movieDetails

import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.baghdad.design_system.R
import com.baghdad.design_system.component.AutoSlidingImageCarousel
import com.baghdad.design_system.component.HomeCard
import com.baghdad.design_system.component.appBar.TopAppBar
import com.baghdad.design_system.component.button.IconButton
import com.baghdad.design_system.component.button.PrimaryButton
import com.baghdad.design_system.theme.Theme
import com.baghdad.ui.base.ObserveAsEffect
import com.baghdad.ui.feature.movieDetails.component.ActorsSection
import com.baghdad.ui.feature.movieDetails.component.MovieDetailsHeader
import com.baghdad.ui.feature.movieDetails.component.OverviewSection
import com.baghdad.ui.feature.movieDetails.component.TextSection
import com.baghdad.ui.navigation.graph.movieDetails.MovieDetailsNavEvent
import com.baghdad.ui.navigation.graph.movieDetails.MovieDetailsNavEvent.*
import com.baghdad.viewmodel.base.SnackBarState
import com.baghdad.viewmodel.movieDetails.MovieDetailsEffect
import com.baghdad.viewmodel.movieDetails.MovieDetailsInteractionListener
import com.baghdad.viewmodel.movieDetails.MovieDetailsState
import com.baghdad.viewmodel.movieDetails.MovieDetailsViewModel
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf


@Composable
fun MovieDetailsScreen(
    movieId: Long,
    viewModel: MovieDetailsViewModel = koinViewModel(parameters = { parametersOf(movieId) }),
    handleNavigation: (MovieDetailsNavEvent) -> Unit,
) {
    val state by viewModel.uiState.collectAsState()
    val snackBarState by viewModel.snackBarState.collectAsStateWithLifecycle()

    ObserveAsEffect(viewModel.uiEffect) { effect ->
        handleEffect(effect, handleNavigation)
    }

    MovieDetailsContent(
        listener = viewModel,
        state = state,
        snackBarState = snackBarState
        )
}

@Composable
private fun MovieDetailsContent(
    listener: MovieDetailsInteractionListener,
    state: MovieDetailsState ,
    snackBarState: SnackBarState
) {


    val lazyState = rememberLazyGridState()
    var shouldReduceAspectRatio by remember { mutableStateOf(false) }
    val targetAspectRange = 1.38f..1.42f

    val animatedColor by animateColorAsState(
        targetValue = if (shouldReduceAspectRatio)
            Color.Transparent
        else
            Theme.color.surface,
        animationSpec = tween(300)
    )

    LaunchedEffect(lazyState) {
        snapshotFlow { lazyState.layoutInfo.visibleItemsInfo }
            .collect { visibleItems ->
                shouldReduceAspectRatio = visibleItems.any { item ->
                    val aspectRatio = item.size.width.toFloat() / item.size.height.toFloat()
                    aspectRatio in targetAspectRange
                }
            }
    }

    Box(Modifier.fillMaxSize()) {
        TopAppBar(
            modifier = Modifier
                .fillMaxWidth()
                .background(animatedColor)
                .zIndex(1f)
                .align(Alignment.TopCenter)
                .padding(top = 56.dp, bottom = 8.dp),
            onGoBackClick = { listener.onNavigateBack()},
            content = {
                Crossfade(
                    targetState = state.isSaved,
                    animationSpec = tween(300)
                ) { isSaved ->
                    IconButton(
                        icon = if (isSaved) painterResource(R.drawable.ic_save_fill) else painterResource(
                            R.drawable.ic_save
                        ),
                        tintIcon = Theme.color.title,
                        onClick = {
                            listener.onSaveCurrentMovieClick()
                        }
                    )
                }

            }
        )

        LazyVerticalGrid(
            state = lazyState,
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .fillMaxSize()
                .background(Theme.color.surface),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),

            ) {

            item(span = { GridItemSpan(maxLineSpan) }) {
                HeaderSliderSection(
                    state.movieImages,
                    indicatorVisibility = state.movieImages.size > 1
                )
            }

            item(span = { GridItemSpan(maxLineSpan) }) {
                MovieDetailsHeader(
                    title = state.movieName,
                    releaseDate = state.date,
                    rating = state.rating,
                    duration = state.duration,
                    categories = state.categories,
                    modifier = Modifier
                        .offset(y = (-48).dp)
                        .padding(horizontal = 16.dp),
                    onViewReviewClicked = {
                        listener.onReviewClick(state.movieId)
                    },
                    onViewCategoryClicked = {listener.onCategoryClick(it)}
                )
            }

            item(span = { GridItemSpan(maxLineSpan) }) {
                OverviewSection(
                    overview = state.overView,
                    isExtended = state.isExtendText,
                ) { listener.onExtendOverviewClick() }
            }

            item(span = { GridItemSpan(maxLineSpan) }) {
                ActorsSection(
                    actors = state.castes,
                    modifier = Modifier.offset(y = (-48).dp),
                    onClick = {listener.onActorClick(id = it)}
                )
            }

            item(span = { GridItemSpan(maxLineSpan) }) {
                TextSection(
                    text = stringResource(com.baghdad.ui.R.string.more_like_this),
                    modifier = Modifier
                        .offset(y = (-48).dp)
                        .padding(end = 16.dp),
                )

            }

            itemsIndexed(state.moreLikeThisMovie) { index, movieLikeThis ->
                HomeCard(
                    url = movieLikeThis.imageUrl,
                    contentDescription = stringResource(com.baghdad.ui.R.string.card_movie_image),
                    isSaved = movieLikeThis.isSaved,
                    onSavedClick = {
                        listener.onSaveMoreLikeThisMedia(movieLikeThis.id)
                    },
                    onClick = {
                        listener.onActorClick(movieLikeThis.id)
                    },
                    modifier = Modifier
                        .offset(y = (-48).dp)
                        .height(210.dp)
                        .then(
                            if (index % 2 == 0) Modifier.padding(start = 16.dp)
                            else Modifier.padding(end = 16.dp)
                        )
                        .clip(RoundedCornerShape(12.dp))
                )
            }
        }
        FloatingIconsButton(
            Modifier.align(Alignment.BottomCenter),
            listener = listener,
            state = state
        )

    }

}


@Composable
private fun HeaderSliderSection(movieImages: List<String>, indicatorVisibility: Boolean = true) {

    Box(modifier = Modifier.fillMaxWidth()) {
        AutoSlidingImageCarousel(
            imageUrls = movieImages,
            modifier = Modifier
                .zIndex(-1f)
                .fillMaxWidth(),
            autoSlideDuration = 7000,
            indicatorVisibility = indicatorVisibility
        )
    }

}

@Composable
private fun FloatingIconsButton(
    modifier: Modifier,
    listener: MovieDetailsInteractionListener,
    state: MovieDetailsState,
) {
    Box(modifier) {
        Box(
            modifier = Modifier
                .zIndex(1f)
                .fillMaxWidth()
                .height(112.dp)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color(0x000D0608),
                            Color(0xFF000000),
                        ),
                    )
                )
        )
        Row(
            modifier = modifier
                .zIndex(1f)
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 24.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Crossfade(
                targetState = state.isStared,
            ) { isStared ->
                IconButton(
                    icon = if (isStared) painterResource(R.drawable.ic_star_filled) else painterResource(
                        R.drawable.ic_star
                    ),
                    tintIcon = Theme.color.onPrimary,
                    background = Theme.color.primary,
                    borderStroke = null,
                    size = Pair(52.dp, 48.dp),
                    onClick = {
                        listener.onStarMovieClick()
                    }
                )
            }
            PrimaryButton(
                stringResource(com.baghdad.ui.R.string.play_trailer),
                modifier = Modifier.fillMaxWidth(),
                isEnabled = state.isHasTrailer,
                isLoading = state.isLoading,
            ) {
            }
        }
    }
}

private fun handleEffect(
    effect: MovieDetailsEffect,
    handleNavigation: (MovieDetailsNavEvent) -> Unit,
) {
    when (effect) {
        is MovieDetailsEffect.NavigateToActorDetails -> handleNavigation(
            NavigateToActorDetails(
                actorId = effect.id
            )
            )


        is MovieDetailsEffect.NavigateToCategory -> handleNavigation(
            NavigateToCategoryMovies(
                categoryId = effect.id
            )
            )


        is MovieDetailsEffect.NavigateToMovie -> handleNavigation(
            NavigateToMovieDetails(
                movieId = effect.id,
            )
        )

        is MovieDetailsEffect.NavigateToReviewDetails -> handleNavigation(
            NavigateToReviews(
                movieId = effect.id
            )
        )

        MovieDetailsEffect.NavigateBack -> handleNavigation(NavigateBack)
    }
}

