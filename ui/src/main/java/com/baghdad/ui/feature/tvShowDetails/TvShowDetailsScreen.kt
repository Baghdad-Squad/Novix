package com.baghdad.ui.feature.tvShowDetails

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.baghdad.design_system.R
import com.baghdad.design_system.component.AutoSlidingImageCarousel
import com.baghdad.design_system.component.SaveIcon
import com.baghdad.design_system.component.Text
import com.baghdad.design_system.component.WavyLoadingIndicator
import com.baghdad.design_system.component.appBar.TopAppBar
import com.baghdad.design_system.component.button.IconButton
import com.baghdad.design_system.component.button.PrimaryButton
import com.baghdad.design_system.theme.Theme
import com.baghdad.ui.base.ObserveAsEffect
import com.baghdad.ui.feature.tvShowDetails.component.CastMembersSection
import com.baghdad.ui.feature.tvShowDetails.component.EpisodesSection
import com.baghdad.ui.feature.tvShowDetails.component.SeasonSection
import com.baghdad.ui.feature.tvShowDetails.component.TvShowDetailsCard
import com.baghdad.ui.feature.tvShowDetails.component.TvShowOverviewSection
import com.baghdad.ui.navigation.graph.tvShowDetails.TvShowDetailsNavEvent
import com.baghdad.viewmodel.tvShowDetails.TvShowDetailsInteractionListener
import com.baghdad.viewmodel.tvShowDetails.TvShowDetailsScreenEffect
import com.baghdad.viewmodel.tvShowDetails.TvShowDetailsScreenState
import com.baghdad.viewmodel.tvShowDetails.TvShowDetailsViewModel
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun TvShowDetailsScreen(
    tvShowId: Long,
    viewModel: TvShowDetailsViewModel = koinViewModel(parameters = { parametersOf(tvShowId) }),
    handleNavigation: (TvShowDetailsNavEvent) -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    ObserveAsEffect(viewModel.uiEffect) { effect ->
        handleEffect(effect, handleNavigation)
    }
    TvShowDetailsContent(
        tvShowId = tvShowId,
        uiState = uiState,
        listener = viewModel
    )
}

private fun handleEffect(
    effect: TvShowDetailsScreenEffect,
    handleNavigation: (TvShowDetailsNavEvent) -> Unit
) {
    when (effect) {
        is TvShowDetailsScreenEffect.NavigateBack -> handleNavigation(
            TvShowDetailsNavEvent.NavigateBack
        )

        is TvShowDetailsScreenEffect.NavigateToLogin -> handleNavigation(
            TvShowDetailsNavEvent.NavigateToLogin
        )

        is TvShowDetailsScreenEffect.NavigateToActorDetails -> handleNavigation(
            TvShowDetailsNavEvent.NavigateToActorDetails(effect.actorId)
        )

        is TvShowDetailsScreenEffect.NavigateToEpisodeDetails -> handleNavigation(
            TvShowDetailsNavEvent.NavigateToEpisodeDetails(
                effect.seasonNumber,
                effect.episodeNumber
            )
        )

        is TvShowDetailsScreenEffect.NavigateToGenreScreen -> handleNavigation(
            TvShowDetailsNavEvent.NavigateToCategoryTvShows(effect.genreId)
        )

        is TvShowDetailsScreenEffect.NavigateToReviews -> handleNavigation(
            TvShowDetailsNavEvent.NavigateToReviews(effect.tvShowId)
        )
    }

}

@Composable
fun TvShowDetailsContent(
    tvShowId: Long,
    uiState: TvShowDetailsScreenState,
    listener: TvShowDetailsInteractionListener,
    modifier: Modifier = Modifier
) {
    val listState = rememberLazyListState()
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

    LaunchedEffect(listState) {
        snapshotFlow { listState.firstVisibleItemScrollOffset }
            .collect { scrollOffset ->
                shouldShowBackground = scrollOffset > 450
            }
    }

    Box(
        modifier = modifier
            .background(Theme.color.surface)
            .fillMaxSize()
            .navigationBarsPadding()
    ) {
        AnimatedContent(uiState.isLoading) { isLoading ->
            if (isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    WavyLoadingIndicator()
                }
            } else {
                LazyColumn(
                    state = listState,
                    modifier = Modifier
                        .fillMaxSize(),
                    contentPadding = PaddingValues(bottom = 72.dp)
                ) {
                    item {
                        Box {
                            AutoSlidingImageCarousel(
                                imageUrls = uiState.tvShowInfo.headerImagesURLs,
                                imageAspectRatio = 1.778f, //
                                modifier = Modifier.padding(bottom = 128.dp)
                            )
                            TvShowDetailsCard(
                                tvShowId = tvShowId,
                                title = uiState.tvShowInfo.title,
                                genres = uiState.tvShowInfo.genres,
                                rating = uiState.tvShowInfo.rating,
                                date = uiState.tvShowInfo.releaseDate,
                                seasonsCount = uiState.tvShowInfo.seasonCount,
                                onReviewClick = { listener.onClickReviews(tvShowId) },
                                onGenreClick = { genreId ->
                                    genreId?.let { listener.onClickGenre(it) }
                                },
                                modifier = Modifier
                                    .padding(bottom = 16.dp)
                                    .padding(horizontal = 16.dp)
                                    .align(Alignment.BottomCenter)
                            )
                        }
                    }

                    item {
                        TvShowOverviewSection(
                            overview = uiState.tvShowInfo.overView,
                            onExpandedChange = { listener.onClickReadMoreOverview() },
                            isExpanded = uiState.isTextExpanded,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )
                    }

                    item {
                        CastMembersSection(
                            actors = uiState.castMembers,
                            onClickCastMember = { actorId ->
                                actorId?.let { listener.onClickCastMember(it) }
                            },
                            modifier = Modifier.padding(bottom = 16.dp)
                        )
                    }

                    item {
                        SeasonSection(
                            seasonCount = uiState.tvShowInfo.seasonCount,
                            selectedSeasonIndex = uiState.selectedSeasonIndex,
                            onSeasonSelected = { listener.onClickSeasonTab(it) },
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                    }

                    item {
                        Text(
                            text = "${uiState.episodes.size} Episodes",
                            style = Theme.typography.label.small,
                            color = Theme.color.hint,
                            modifier = Modifier
                                .padding(horizontal = 16.dp)
                                .padding(bottom = 12.dp)
                        )
                    }

                    item {
                        EpisodesSection(
                            episodes = uiState.episodes,
                            posterPictureUrl = uiState.tvShowInfo.posterImageURL,
                            onClickEpisode = { seasonNumber, episodeNumber ->
                                listener.onClickEpisode(seasonNumber, episodeNumber)
                            },
                            modifier = Modifier.padding(bottom = 16.dp)
                        )
                    }
                }
            }
        }

        TopAppBar(
            modifier = Modifier
                .fillMaxWidth()
                .background(animatedColor)
                .zIndex(1f)
                .align(Alignment.TopCenter)
                .padding(top = 56.dp, bottom = 8.dp),
            onGoBackClick = {
                listener.onClickBackIcon()
            },
            content = {
                SaveIcon(
                    size = 40,
                    backgroundColor = Theme.color.iconBackgroundLow,
                    isSaved = uiState.isTvShowSaved,
                    onClick = {
                        listener.onClickSaveTvShow(tvShowId)
                    }
                )
            }
        )

        FloatingIconsButton(
            modifier = Modifier.align(Alignment.BottomCenter),
            isRated = uiState.isTvShowRated,
            hasTrailer = uiState.hasTrailer,
            onStarClick = { listener.onClickAddRating() },
            onTrailerClick = { listener.onClickPlayTrailer() }
        )
    }
}


@Composable
private fun FloatingIconsButton(
    modifier: Modifier,
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
            .padding(start = 24.dp, end = 24.dp, top = 40.dp, bottom = 24.dp),
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