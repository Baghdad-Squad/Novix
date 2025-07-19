package com.baghdad.ui.feature.movieDetails

import android.content.Context
import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.baghdad.design_system.R
import com.baghdad.design_system.component.AutoSlidingImageCarousel
import com.baghdad.design_system.component.HomeCard
import com.baghdad.design_system.component.SaveIcon
import com.baghdad.design_system.component.Scaffold
import com.baghdad.design_system.component.SnackBar
import com.baghdad.design_system.component.Text
import com.baghdad.design_system.component.WavyLoadingIndicator
import com.baghdad.design_system.component.appBar.TopAppBar
import com.baghdad.design_system.component.button.IconButton
import com.baghdad.design_system.component.button.PrimaryButton
import com.baghdad.design_system.theme.Theme
import com.baghdad.ui.base.ObserveAsEffect
import com.baghdad.ui.base.toStringResource
import com.baghdad.ui.feature.movieDetails.component.ActorsSection
import com.baghdad.ui.feature.movieDetails.component.MovieDetailsHeader
import com.baghdad.ui.feature.movieDetails.component.OverviewSection
import com.baghdad.ui.feature.util.hideNavigationBar
import com.baghdad.ui.navigation.graph.movieDetails.MovieDetailsNavEvent
import com.baghdad.ui.navigation.graph.movieDetails.MovieDetailsNavEvent.NavigateBack
import com.baghdad.ui.navigation.graph.movieDetails.MovieDetailsNavEvent.NavigateToActorDetails
import com.baghdad.ui.navigation.graph.movieDetails.MovieDetailsNavEvent.NavigateToCategoryMovies
import com.baghdad.ui.navigation.graph.movieDetails.MovieDetailsNavEvent.NavigateToMovieDetails
import com.baghdad.ui.navigation.graph.movieDetails.MovieDetailsNavEvent.NavigateToReviews
import com.baghdad.ui.util.openYouTubeLink
import com.baghdad.viewmodel.base.SnackBarState
import com.baghdad.viewmodel.errorStates.BaseSnackBarMessage
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
    val context = LocalContext.current
    ObserveAsEffect(viewModel.uiEffect) { effect ->
        handleEffect(effect, context, handleNavigation)
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
    state: MovieDetailsState,
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
    val context = LocalContext.current
    val view = LocalView.current
    LaunchedEffect(Unit) {
        hideNavigationBar(context, view)
    }

    LaunchedEffect(lazyState) {
        snapshotFlow { lazyState.layoutInfo.visibleItemsInfo }
            .collect { visibleItems ->
                shouldReduceAspectRatio = visibleItems.any { item ->
                    val aspectRatio = item.size.width.toFloat() / item.size.height.toFloat()
                    aspectRatio in targetAspectRange
                }
            }
    }

    Scaffold(
        modifier = Modifier
            .background(Theme.color.surface)
            .navigationBarsPadding(),
        bottomBar = {
            FloatingIconsButton(
                hasTrailer = state.movieTrailerURL.isNotBlank(),
                onStarClick = { listener.onStarMovieClick() },
                onTrailerClick = { listener.onTrailerClick() },
                isRated = state.isStared
            )
        }, snackbar = {
            SnackBar(
                message = stringResource(snackBarMessage(snackBarState.message)),
                isSuccess = snackBarState.isSuccess,
                isVisible = snackBarState.isVisible
            )
        }
    ) {
        if (state.isLoading) {
            Box(Modifier.fillMaxSize()) {
                WavyLoadingIndicator(modifier = Modifier.align(Alignment.Center))
            }
        }
        Box(Modifier.fillMaxSize()) {
            LazyVerticalGrid(
                state = lazyState,
                columns = GridCells.Fixed(2),
                modifier = Modifier
                    .fillMaxSize()
                    .background(Theme.color.surface),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(bottom = 40.dp)

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
                        onCategoryClick = { listener.onCategoryClick(it) }
                    )
                }
                if (state.overView.isNotBlank()) {
                    item(span = { GridItemSpan(maxLineSpan) }) {
                        OverviewSection(
                            overview = state.overView,
                            isExtended = state.isExtendText,
                        ) { listener.onExtendOverviewClick() }
                    }
                }
                if (state.castes.isNotEmpty()) {
                    item(span = { GridItemSpan(maxLineSpan) }) {
                        ActorsSection(
                            actors = state.castes,
                            modifier = Modifier.offset(y = (-48).dp),
                            onClick = { listener.onActorClick(id = it) }
                        )
                    }
                }
                if (state.moreLikeThisMovie.isNotEmpty()) {
                    item(span = { GridItemSpan(maxLineSpan) }) {
                        Text(
                            text = stringResource(com.baghdad.ui.R.string.more_like_this),
                            fontSize = 18.sp,
                            style = Theme.typography.title.medium,
                            color = Theme.color.title,
                            modifier = Modifier
                                .offset(y = (-48).dp)
                                .padding(start = 16.dp, end = 16.dp, bottom = 8.dp),
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
                                listener.onMovieLikeClick(movieLikeThis.id)
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
            }
            TopAppBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(animatedColor)
                    .zIndex(1f)
                    .padding(top = 56.dp, bottom = 8.dp),
                onGoBackClick = {
                    listener.onBackClick()
                },
                content = {
                    SaveIcon(
                        tint = Theme.color.title,
                        size = 40,
                        backgroundColor = Theme.color.iconBackgroundLow,
                        isSaved = state.isSaved,
                        onClick = {
                            listener.onSaveCurrentMovieClick()
                        }
                    )
                }
            )
        }
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
    modifier: Modifier = Modifier,
    hasTrailer: Boolean,
    isRated: Boolean,
    onStarClick: () -> Unit,
    onTrailerClick: () -> Unit
) {
    Row(
        modifier = modifier
            .zIndex(1f)
            .fillMaxWidth()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(Color.Transparent, Color(0xFF0D0608))
                )
            )
            .padding(horizontal = 24.dp)
            .padding(bottom = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Crossfade(
            targetState = isRated,
        ) { isStared ->
            IconButton(
                icon = if (isStared) painterResource(R.drawable.ic_star_filled) else painterResource(
                    R.drawable.ic_star
                ),
                tintIcon = Theme.color.onPrimary,
                background = Theme.color.primary,
                borderStroke = null,
                size = Pair(52.dp, 48.dp),
                onClick = onStarClick
            )
        }
        PrimaryButton(
            stringResource(com.baghdad.ui.R.string.play_trailer),
            modifier = Modifier.fillMaxWidth(),
            isEnabled = hasTrailer,
            onClick = onTrailerClick
        )
    }
}

private fun handleEffect(
    effect: MovieDetailsEffect,
    context: Context,
    handleNavigation: (MovieDetailsNavEvent) -> Unit,
) {
    when (effect) {
        is MovieDetailsEffect.NavigateToActorDetails -> {
            handleNavigation(
                NavigateToActorDetails(
                    actorId = effect.id
                )
            )
        }

        is MovieDetailsEffect.NavigateToCategory -> {
            handleNavigation(
                NavigateToCategoryMovies(
                    categoryId = effect.id
                )
            )
        }

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

        MovieDetailsEffect.NavigateBack -> handleNavigation(
            NavigateBack
        )
        is MovieDetailsEffect.OpenYoutubeLink -> openYouTubeLink(context, effect.youtubeLink)
    }
}

@Composable
private fun snackBarMessage(type: BaseSnackBarMessage): Int {
    return type.toStringResource()
}
