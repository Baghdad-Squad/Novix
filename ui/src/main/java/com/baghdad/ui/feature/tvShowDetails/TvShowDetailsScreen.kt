package com.baghdad.ui.feature.tvShowDetails

import android.content.Context
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.baghdad.design_system.component.SaveIcon
import com.baghdad.design_system.component.Scaffold
import com.baghdad.design_system.component.SnackBar
import com.baghdad.design_system.component.Text
import com.baghdad.design_system.component.appBar.TopAppBar
import com.baghdad.design_system.modifier.noRippleClickable
import com.baghdad.design_system.theme.Theme
import com.baghdad.ui.base.ObserveAsEffect
import com.baghdad.ui.base.toStringResource
import com.baghdad.ui.feature.component.DetailsScreenBottomBar
import com.baghdad.ui.feature.tvShowDetails.component.CastMembersSection
import com.baghdad.ui.feature.tvShowDetails.component.EpisodeCard
import com.baghdad.ui.feature.tvShowDetails.component.SeasonSection
import com.baghdad.ui.feature.tvShowDetails.component.TvShowHeaderWithDetailsCard
import com.baghdad.ui.feature.tvShowDetails.component.TvShowOverviewSection
import com.baghdad.ui.navigation.graph.tvShowDetails.TvShowDetailsNavEvent
import com.baghdad.ui.navigation.graph.tvShowDetails.TvShowDetailsNavEvent.NavigateToActorDetails
import com.baghdad.ui.navigation.graph.tvShowDetails.TvShowDetailsNavEvent.NavigateToCategoryTvShows
import com.baghdad.ui.navigation.graph.tvShowDetails.TvShowDetailsNavEvent.NavigateToEpisodeDetails
import com.baghdad.ui.navigation.graph.tvShowDetails.TvShowDetailsNavEvent.NavigateToReviews
import com.baghdad.ui.util.openYouTubeLink
import com.baghdad.viewmodel.base.SnackBarState
import com.baghdad.viewmodel.errorStates.BaseSnackBarMessage
import com.baghdad.viewmodel.tvShowDetails.TvShowDetailsInteractionListener
import com.baghdad.viewmodel.tvShowDetails.TvShowDetailsScreenEffect
import com.baghdad.viewmodel.tvShowDetails.TvShowDetailsScreenState
import com.baghdad.viewmodel.tvShowDetails.TvShowDetailsViewModel

@Composable
fun TvShowDetailsScreen(
    tvShowId: Long,
    viewModel: TvShowDetailsViewModel = hiltViewModel(),
    handleNavigation: (TvShowDetailsNavEvent) -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackBarState by viewModel.snackBarState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    ObserveAsEffect(viewModel.uiEffect) { effect ->
        handleEffect(effect, context, handleNavigation)
    }
    TvShowDetailsContent(
        tvShowId = tvShowId,
        uiState = uiState,
        listener = viewModel,
        snackBarState = snackBarState
    )
}

private fun handleEffect(
    effect: TvShowDetailsScreenEffect,
    context: Context,
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
            NavigateToActorDetails(effect.actorId)
        )

        is TvShowDetailsScreenEffect.NavigateToEpisodeDetails -> handleNavigation(
            NavigateToEpisodeDetails(
                effect.seasonNumber,
                effect.episodeNumber
            )
        )

        is TvShowDetailsScreenEffect.NavigateToGenreScreen -> handleNavigation(
            NavigateToCategoryTvShows(effect.genreId)
        )

        is TvShowDetailsScreenEffect.NavigateToReviews -> handleNavigation(
            NavigateToReviews(effect.tvShowId)
        )

        is TvShowDetailsScreenEffect.OpenYoutubeLink -> openYouTubeLink(context, effect.youtubeLink)
    }

}

@Composable
fun TvShowDetailsContent(
    tvShowId: Long,
    uiState: TvShowDetailsScreenState,
    listener: TvShowDetailsInteractionListener,
    snackBarState: SnackBarState,
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
        snapshotFlow {
            val firstVisibleItemIndex = listState.firstVisibleItemIndex
            val firstVisibleItemScrollOffset = listState.firstVisibleItemScrollOffset

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
                hasTrailer = uiState.tvShowInfo.trailerURL.isNotBlank(),
                onRateClicked = { listener.onClickAddRating() },
                onPlayTrailerClicked = { listener.onClickPlayTrailer() },
                isRated = uiState.isTvShowRated,
                isLoading = false /*TODO*/
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
        Box(
            modifier = modifier
                .background(Theme.color.surface)
                .fillMaxSize()
                .navigationBarsPadding()
        ) {
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .fillMaxSize(),
                contentPadding = PaddingValues(bottom = 72.dp)
            ) {

                item {
                    TvShowHeaderWithDetailsCard(
                        tvShowId = tvShowId,
                        uiState = uiState,
                        listener = listener
                    )
                }

                item {
                    Spacer(Modifier.height(136.dp))
                }

                if (uiState.tvShowInfo.overView.isNotBlank()) {
                    item {
                        TvShowOverviewSection(
                            overview = uiState.tvShowInfo.overView,
                            onExpandedChange = { listener.onClickReadMoreOverview() },
                            isExpanded = uiState.isTextExpanded,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )
                    }
                }

                if (uiState.castMembers.isNotEmpty()) {
                    item {
                        CastMembersSection(
                            actors = uiState.castMembers,
                            onClickCastMember = { actorId ->
                                actorId?.let { listener.onClickCastMember(it) }
                            },
                            modifier = Modifier.padding(bottom = 16.dp)
                        )
                    }
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

                if (uiState.episodes.isNotEmpty()) {
                    items(uiState.episodes) { episode ->
                        EpisodeCard(
                            episodeNumber = episode.episodeNumber,
                            imageUrl = uiState.tvShowInfo.posterPictureURL,
                            episodeName = episode.name,
                            releaseDate = episode.releaseDate,
                            duration = episode.duration,
                            rating = episode.rating,
                            modifier = Modifier
                                .padding(horizontal = 16.dp)
                                .padding(bottom = 8.dp)
                                .noRippleClickable {
                                    listener.onClickEpisode(
                                        episode.currentSeason,
                                        episode.episodeNumber
                                    )
                                }
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
                    listener.onClickBackIcon()
                },
                content = {
                    SaveIcon(
                        tint = Theme.color.title,
                        size = 40,
                        backgroundColor = Theme.color.iconBackgroundLow,
                        isSaved = uiState.isTvShowSaved,
                        onClick = {
                            listener.onClickSaveTvShow(tvShowId)
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
