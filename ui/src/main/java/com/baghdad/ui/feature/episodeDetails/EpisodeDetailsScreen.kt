package com.baghdad.ui.feature.episodeDetails

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
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.baghdad.design_system.R
import com.baghdad.design_system.component.AutoSlidingImageCarousel
import com.baghdad.design_system.component.appBar.TopAppBar
import com.baghdad.design_system.component.button.IconButton
import com.baghdad.design_system.component.button.PrimaryButton
import com.baghdad.design_system.theme.Theme
import com.baghdad.ui.feature.episodeDetails.component.GuestsOfHonorSection
import com.baghdad.ui.feature.movieDetails.component.MovieDetailsHeader
import com.baghdad.ui.feature.movieDetails.component.OverviewSection
import com.baghdad.viewmodel.episodeDetails.EpisodeDetailsInteractionListener
import com.baghdad.viewmodel.episodeDetails.EpisodeDetailsState
import com.baghdad.viewmodel.episodeDetails.EpisodeDetailsViewModel
import org.koin.androidx.compose.koinViewModel


@Composable
fun EpisodeDetailsScreen(
    viewModel: EpisodeDetailsViewModel = koinViewModel(),
    listener: EpisodeDetailsInteractionListener = viewModel,
) {
    EpisodeDetailsContent(viewModel, listener)
}

@Composable
fun EpisodeDetailsContent(
    viewModel: EpisodeDetailsViewModel,
    listener: EpisodeDetailsInteractionListener,
) {
    val state by viewModel.uiState.collectAsState()

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
            onGoBackClick = { },
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
                            listener.onSaveCurrentEpisodeClick()
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
                    state.episodeImages,
                    indicatorVisibility = state.episodeImages.size > 1
                )
            }

            item(span = { GridItemSpan(maxLineSpan) }) {
                MovieDetailsHeader(
                    title = state.episodeName,
                    releaseDate = state.duration,
                    rating = state.rating,
                    duration = state.duration,
                    categories = state.categories,
                    modifier = Modifier
                        .offset(y = (-48).dp)
                        .padding(horizontal = 16.dp),
                    onViewReviewClicked = {
                    }
                )
            }

            item(span = { GridItemSpan(maxLineSpan) }) {
                OverviewSection(
                    overview = state.overView,
                    isExtended = state.isExtendText,
                ) { listener.onClickExtendText() }
            }

            item(span = { GridItemSpan(maxLineSpan) }) {
                GuestsOfHonorSection(
                    guestsOfHonor = state.guestsOfHonor,
                    modifier = Modifier.offset(y = (-48).dp)
                ){}
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
    listener: EpisodeDetailsInteractionListener,
    state: EpisodeDetailsState,
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
                        listener.onClickExtendText()
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
