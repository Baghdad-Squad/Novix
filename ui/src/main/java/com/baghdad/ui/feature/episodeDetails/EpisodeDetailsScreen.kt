package com.baghdad.ui.feature.episodeDetails

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.baghdad.design_system.component.BackgroundBlur
import com.baghdad.design_system.component.SaveIcon
import com.baghdad.design_system.component.Scaffold
import com.baghdad.design_system.component.SnackBar
import com.baghdad.design_system.component.appBar.TopAppBar
import com.baghdad.design_system.theme.Theme
import com.baghdad.ui.R
import com.baghdad.ui.base.ObserveAsEffect
import com.baghdad.ui.base.toStringResource
import com.baghdad.ui.feature.component.DetailsScreenBottomBar
import com.baghdad.ui.feature.component.bottomSheet.LoginRequiredSheet
import com.baghdad.ui.feature.component.bottomSheet.RatingBottomSheet
import com.baghdad.ui.feature.episodeDetails.component.EpisodeHeaderWithDetailsCard
import com.baghdad.ui.feature.episodeDetails.component.guestsOfHonorItems
import com.baghdad.ui.feature.movieDetails.component.OverviewSection
import com.baghdad.ui.navigation.graph.tvShowDetails.TvShowDetailsNavEvent
import com.baghdad.viewmodel.base.SnackBarState
import com.baghdad.viewmodel.episodeDetails.EpisodeDetailsInteractionListener
import com.baghdad.viewmodel.episodeDetails.EpisodeDetailsScreenEffect
import com.baghdad.viewmodel.episodeDetails.EpisodeDetailsScreenState
import com.baghdad.viewmodel.episodeDetails.EpisodeDetailsViewModel
import com.baghdad.viewmodel.errorStates.BaseSnackBarMessage
import com.baghdad.viewmodel.shared.BottomSheetType

@Composable
fun EpisodeDetailsScreen(
    viewModel: EpisodeDetailsViewModel = hiltViewModel(),
    handleNavigation: (TvShowDetailsNavEvent) -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackBarState by viewModel.snackBarState.collectAsStateWithLifecycle()
    ObserveAsEffect(viewModel.uiEffect) { effect ->
        handleEffect(effect, handleNavigation)
    }
    EpisodeDetailsContent(
        state = uiState,
        listener = viewModel,
        snackBarState = snackBarState,
    )
}

@Composable
fun EpisodeDetailsContent(
    state: EpisodeDetailsScreenState,
    listener: EpisodeDetailsInteractionListener,
    snackBarState: SnackBarState,
) {
    val listState = rememberLazyListState()
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
        modifier =
            Modifier
                .background(Theme.color.surface)
                .fillMaxSize(),
        isLoading = state.isLoading,
        bottomBar = {
            DetailsScreenBottomBar(
                isRated = state.isRated,
                onRateClicked = listener::onClickStarButton,
                hasTrailer = state.episode.trailerUrl.isNotBlank(),
                isLoading = state.isEpisodeDetailsLoading,
                onPlayTrailerClicked = listener::onPlayTrailerClick,
            )
        },
        snackbar = { position ->
            SnackBar(
                message = stringResource(snackBarMessage(snackBarState.message)),
                isSuccess = snackBarState.isSuccess,
                isVisible = snackBarState.isVisible,
                actionLabel = snackBarState.actionLabelRes?.let { stringResource(it) },
                onActionClick = listener::onSnackBarActionLabelClick,
                position = position,
            )
        },
        backgroundBlur = {
            BackgroundBlur()
        },
        isSnackBarWithActionLabel = snackBarState.actionLabelRes != null,
    ) {

        RatingBottomSheet(
            isVisible = state.ratingStatus.isBottomSheetVisible && state.ratingStatus.bottomSheetType == BottomSheetType.ShowRating,
            onBottomSheetCloseClick = { listener.onDismissRatingBottomSheet() },
            rate = state.episode.userRating,
            isButtonEnabled = state.episode.userRating != 0,
            onRateChanged = { listener.onRatingChanged(it) },
            onSubmitClick = { listener.onClickSubmitRating(state.episode.userRating) }
        )


        LoginRequiredSheet(
            isVisible = state.ratingStatus.isBottomSheetVisible && state.ratingStatus.bottomSheetType == BottomSheetType.RequireLogin,
            onBottomSheetCloseClick = { listener.onDismissRatingBottomSheet() },
            onLoginClick = { listener.onClickLoginButton() },
            title = stringResource(R.string.rate_it),
            description = stringResource(R.string.please_login_to_rate)
        )

        LazyColumn(
            state = listState,
            contentPadding = PaddingValues(bottom = 72.dp),
            modifier =
                Modifier
                    .fillMaxSize(),
        ) {
            item {
                EpisodeHeaderWithDetailsCard(
                    state = state,
                    listener = listener,
                )
            }

            item {
                Spacer(Modifier.height(128.dp))
            }

            item {
                AnimatedVisibility(state.episode.overview.isNotBlank()) {
                    OverviewSection(
                        overview = state.episode.overview,
                        isExtended = state.isOverviewExpanded,
                        onExtendClicked = listener::onReadMoreOverviewClick,
                        modifier = Modifier.padding(bottom = 16.dp),
                    )
                }
            }
            guestsOfHonorItems(
                guestsOfHonor = state.guestsOfHonor,
                onClick = listener::onGuestOfHonorClick,
            )
        }

        TopAppBar(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .background(animatedColor)
                    .zIndex(1f)
                    .align(Alignment.TopCenter)
                    .padding(top = 56.dp, bottom = 8.dp),
            onGoBackClick = listener::onBackClick,
            content = {
                SaveIcon(
                    size = 40,
                    backgroundColor = Theme.color.iconBackgroundLow,
                    isSaved = state.isSavedToList,
                    tint = Theme.color.title,
                    onClick = listener::onSaveEpisodeClick,
                )
            },
        )
    }
}

fun handleEffect(
    effect: EpisodeDetailsScreenEffect,
    handleNavigation: (TvShowDetailsNavEvent) -> Unit,
) {
    when (effect) {
        EpisodeDetailsScreenEffect.NavigateBack -> handleNavigation(TvShowDetailsNavEvent.NavigateBack)
        EpisodeDetailsScreenEffect.NavigateToLogin -> handleNavigation(TvShowDetailsNavEvent.NavigateToLogin)
        is EpisodeDetailsScreenEffect.NavigateToActorDetails ->
            handleNavigation(
                TvShowDetailsNavEvent.NavigateToActorDetails(effect.actorId),
            )

        is EpisodeDetailsScreenEffect.NavigateToCategoryTvShows ->
            handleNavigation(
                TvShowDetailsNavEvent.NavigateToCategoryTvShows(effect.categoryId),
            )
    }
}

@Composable
private fun snackBarMessage(type: BaseSnackBarMessage): Int = type.toStringResource()
