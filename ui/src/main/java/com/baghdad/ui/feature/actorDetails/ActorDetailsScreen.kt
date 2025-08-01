package com.baghdad.ui.feature.actorDetails

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.baghdad.design_system.component.Scaffold
import com.baghdad.design_system.component.SnackBar
import com.baghdad.design_system.component.appBar.TopAppBar
import com.baghdad.design_system.theme.Theme
import com.baghdad.ui.base.ObserveAsEffect
import com.baghdad.ui.base.toStringResource
import com.baghdad.ui.feature.actorDetails.component.ActorBiographySection
import com.baghdad.ui.feature.actorDetails.component.ActorHeaderWithDetailsCard
import com.baghdad.ui.feature.actorDetails.component.GallerySection
import com.baghdad.ui.feature.actorDetails.component.TopMediaPicksSection
import com.baghdad.ui.navigation.graph.actorDetails.ActorDetailsNavEvent
import com.baghdad.ui.navigation.graph.actorDetails.ActorDetailsNavEvent.NavigateToMovieDetails
import com.baghdad.ui.navigation.graph.actorDetails.ActorDetailsNavEvent.NavigateToTvShowDetails
import com.baghdad.viewmodel.actorDetails.ActorDetailsInteractionListener
import com.baghdad.viewmodel.actorDetails.ActorDetailsScreenEffect
import com.baghdad.viewmodel.actorDetails.ActorDetailsScreenState
import com.baghdad.viewmodel.actorDetails.ActorDetailsViewModel
import com.baghdad.viewmodel.base.SnackBarState
import com.baghdad.viewmodel.errorStates.BaseSnackBarMessage


@Composable
fun ActorDetailsScreen(
    viewModel: ActorDetailsViewModel = hiltViewModel(),
    handleNavigation: (ActorDetailsNavEvent) -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackBarState by viewModel.snackBarState.collectAsStateWithLifecycle()
    ObserveAsEffect(viewModel.uiEffect) { effect ->
        handleEffect(effect, handleNavigation)
    }
    ActorDetailsContent(
        uiState = uiState,
        listener = viewModel,
        snackBarState = snackBarState,
    )
}

private fun handleEffect(
    effect: ActorDetailsScreenEffect,
    handleNavigation: (ActorDetailsNavEvent) -> Unit,
) {
    when (effect) {
        is ActorDetailsScreenEffect.NavigateBack ->
            handleNavigation(
                ActorDetailsNavEvent.NavigateBack,
            )

        is ActorDetailsScreenEffect.NavigateToMovieDetails ->
            handleNavigation(
                NavigateToMovieDetails(effect.movieId),
            )

        is ActorDetailsScreenEffect.NavigateToTvShowDetails ->
            handleNavigation(
                NavigateToTvShowDetails(effect.tvShowId),
            )

        ActorDetailsScreenEffect.NavigateToActorGallery ->
            handleNavigation(
                ActorDetailsNavEvent.NavigateToActorGallery,
            )

        ActorDetailsScreenEffect.NavigateToActorTopMoviePicks ->
            handleNavigation(
                ActorDetailsNavEvent.NavigateToActorTopMoviePicks,
            )

        ActorDetailsScreenEffect.NavigateToActorTopTvShowPicks ->
            handleNavigation(
                ActorDetailsNavEvent.NavigateToActorTopTvShowPicks,
            )

        ActorDetailsScreenEffect.NavigateToLogin ->
            handleNavigation(
                ActorDetailsNavEvent.NavigateToLogin,
            )
    }
}

@Composable
fun ActorDetailsContent(
    uiState: ActorDetailsScreenState,
    listener: ActorDetailsInteractionListener,
    modifier: Modifier = Modifier,
    snackBarState: SnackBarState,
) {
    val scrollState = rememberScrollState()
    var shouldShowBackground by remember { mutableStateOf(false) }
    val animatedColor by animateColorAsState(
        targetValue =
            if (shouldShowBackground) {
                Theme.color.surface
            } else {
                Theme.color.surface.copy(alpha = 0f)
            },
        animationSpec =
            tween(
                durationMillis = 500,
                easing = FastOutSlowInEasing,
            ),
    )
    Scaffold(
        modifier =
            Modifier
                .background(Theme.color.surface)
                .navigationBarsPadding(),
        isLoading = uiState.isLoading,
        snackbar = {
            SnackBar(
                message = stringResource(snackBarMessage(snackBarState.message)),
                isSuccess = snackBarState.isSuccess,
                isVisible = snackBarState.isVisible,
                actionLabel = snackBarState.actionLabelRes?.let { stringResource(it) },
                onActionClick = listener::onSnackBarActionLabelClick,
            )
        },
    ) {
        LaunchedEffect(scrollState) {
            snapshotFlow { scrollState.value }.collect { scrollValue ->
                shouldShowBackground = scrollValue > 450
            }
        }

        Box(
            modifier = modifier
                .background(Theme.color.surface)
                .fillMaxSize()
                .navigationBarsPadding(),
        ) {
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .padding(bottom = 24.dp),
            ) {
                ActorHeaderWithDetailsCard(uiState = uiState)

                if (uiState.actorInfo.biography.isNotBlank()) {
                    ActorBiographySection(
                        biography = uiState.actorInfo.biography,
                        onExpandedChange = { listener.onReadMoreBiographyClick() },
                        isExpanded = uiState.isTextExpanded,
                        modifier = Modifier.padding(bottom = 16.dp),
                    )
                }

                if (uiState.gallery.isNotEmpty()) {
                    GallerySection(
                        imageUrls = uiState.gallery,
                        isShowAllVisible = uiState.gallery.size >= 10,
                        onClickShowAll = { listener.onViewAllGalleryClick() },
                        modifier = Modifier.padding(bottom = 16.dp),
                    )
                }

                if (uiState.topMoviesPicks.isNotEmpty()) {
                    TopMediaPicksSection(
                        title = stringResource(com.baghdad.ui.R.string.top_movies_picks),
                        items = uiState.topMoviesPicks,
                        imageUrl = { it.posterPictureURL },
                        onSavedClick = { listener.onSaveMovieClick(it.id) },
                        onCardClick = { listener.onMovieCardClick(it.id) },
                        isSaved = { it.isSaved },
                        isShowAllVisible = uiState.topMoviesPicks.size >= 10,
                        onClickShowAll = { listener.onViewAllTopMoviesPicksClick() },
                        modifier = Modifier.padding(bottom = 16.dp),
                    )
                }
                if (uiState.topTvShowsPicks.isNotEmpty()) {
                    TopMediaPicksSection(
                        title = stringResource(com.baghdad.ui.R.string.top_tv_shows_picks),
                        items = uiState.topTvShowsPicks,
                        imageUrl = { it.posterPictureURL },
                        onSavedClick = { listener.onSaveTvShowClick(it.id) },
                        onCardClick = { listener.onTvShowCardClick(it.id) },
                        isSaved = { it.isSaved },
                        isShowAllVisible = uiState.topTvShowsPicks.size >= 10,
                        onClickShowAll = { listener.onViewAllTopTvShowsClick() },
                    )
                }
            }
            TopAppBar(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .background(animatedColor)
                        .zIndex(1f)
                        .align(Alignment.TopCenter)
                        .padding(top = 56.dp, bottom = 8.dp),
                onGoBackClick = {
                    listener.onBackIconClick()
                },
            )
        }
    }
}

@Composable
private fun snackBarMessage(type: BaseSnackBarMessage): Int = type.toStringResource()
