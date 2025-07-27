package com.baghdad.ui.feature.movieDetails

import android.content.Context
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.baghdad.design_system.component.SaveIcon
import com.baghdad.design_system.component.Scaffold
import com.baghdad.design_system.component.SnackBar
import com.baghdad.design_system.component.Text
import com.baghdad.design_system.component.appBar.TopAppBar
import com.baghdad.design_system.theme.Theme
import com.baghdad.ui.R
import com.baghdad.ui.base.ObserveAsEffect
import com.baghdad.ui.base.toStringResource
import com.baghdad.ui.feature.component.DetailsScreenBottomBar
import com.baghdad.ui.feature.component.HomeCard
import com.baghdad.ui.feature.movieDetails.component.ActorsSection
import com.baghdad.ui.feature.movieDetails.component.MovieHeaderWithDetailsCard
import com.baghdad.ui.feature.movieDetails.component.OverviewSection
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


@Composable
fun MovieDetailsScreen(
    movieId: Long,
    viewModel: MovieDetailsViewModel = hiltViewModel(),
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
private fun MovieDetailsContent(
    listener: MovieDetailsInteractionListener,
    state: MovieDetailsState,
    snackBarState: SnackBarState,
    modifier: Modifier = Modifier
) {

    val lazyState = rememberLazyGridState()
    var shouldShowBackground by remember { mutableStateOf(false) }

    val animatedColor by animateColorAsState(
        targetValue = if (shouldShowBackground)
            Theme.color.surface
        else
            Color.Transparent,
        animationSpec = tween(
            durationMillis = 500,
            easing = FastOutSlowInEasing
        ),
    )

    LaunchedEffect(lazyState) {
        snapshotFlow {
            val firstVisibleItemIndex = lazyState.firstVisibleItemIndex
            val firstVisibleItemScrollOffset = lazyState.firstVisibleItemScrollOffset

            if (firstVisibleItemIndex > 0) {
                Int.MAX_VALUE
            } else {
                firstVisibleItemScrollOffset
            }
        }.collect { totalScrollPosition ->
            shouldShowBackground = totalScrollPosition > 450
        }
    }

    Scaffold(
        modifier = Modifier
            .background(Theme.color.surface)
            .navigationBarsPadding(),
        bottomBar = {
            DetailsScreenBottomBar(
                hasTrailer = state.movieTrailerURL.isNotBlank(),
                onRateClicked = { listener.onStarMovieClick() },
                onPlayTrailerClicked = { listener.onTrailerClick() },
                isRated = state.isStared,
                isLoading = false /*TODO*/
            )
        }, snackbar = {
            SnackBar(
                message = stringResource(snackBarMessage(snackBarState.message)),
                isSuccess = snackBarState.isSuccess,
                isVisible = snackBarState.isVisible
            )
        }
    ) {
        Box(
            modifier = modifier
                .background(Theme.color.surface)
                .fillMaxSize()
                .navigationBarsPadding()
        ) {
            LazyVerticalGrid(
                state = lazyState,
                columns = GridCells.Fixed(2),
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(bottom = 80.dp)

            ) {
                item(span = { GridItemSpan(maxLineSpan) }) {
                    MovieHeaderWithDetailsCard(
                        state = state,
                        listener = listener
                    )
                }

                item {
                    Spacer(Modifier.height(116.dp))
                }

                if (state.overView.isNotBlank()) {
                    item(span = { GridItemSpan(maxLineSpan) }) {
                        OverviewSection(
                            overview = state.overView,
                            isExtended = state.isExtendText,
                        ) { listener.onExtendOverviewClick() }
                    }
                }

                if (state.castMembers.isNotEmpty()) {
                    item(span = { GridItemSpan(maxLineSpan) }) {
                        ActorsSection(
                            actors = state.castMembers,
                            onClick = { listener.onActorClick(id = it) }
                        )
                    }
                }
                if (state.moreLikeThisMovie.isNotEmpty()) {
                    item(span = { GridItemSpan(maxLineSpan) }) {
                        Text(
                            text = stringResource(R.string.more_like_this),
                            fontSize = 18.sp,
                            style = Theme.typography.title.medium,
                            color = Theme.color.title,
                            modifier = Modifier
                                .padding(start = 16.dp, end = 16.dp, bottom = 8.dp),
                        )
                    }

                    itemsIndexed(state.moreLikeThisMovie) { index, movie ->
                        HomeCard(
                            url = movie.imageUrl,
                            contentDescription = stringResource(R.string.card_movie_image),
                            isSaved = movie.isSaved,
                            onSavedClick = {
                                listener.onSaveMoreLikeThisMedia(movie.id)
                            },
                            onClick = {
                                listener.onMovieClick(movie.id)
                            },
                            modifier = Modifier
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
private fun snackBarMessage(type: BaseSnackBarMessage): Int {
    return type.toStringResource()
}
