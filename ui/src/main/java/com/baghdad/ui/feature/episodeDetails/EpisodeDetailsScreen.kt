package com.baghdad.ui.feature.episodeDetails

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.baghdad.design_system.R
import com.baghdad.design_system.component.AutoSlidingImageCarousel
import com.baghdad.design_system.component.Scaffold
import com.baghdad.design_system.component.appBar.TopAppBar
import com.baghdad.design_system.component.button.IconButton
import com.baghdad.design_system.theme.Theme
import com.baghdad.ui.base.ObserveAsEffect
import com.baghdad.ui.feature.episodeDetails.component.EpisodeDetailsBottomBar
import com.baghdad.ui.feature.episodeDetails.component.EpisodeDetailsHeader
import com.baghdad.ui.feature.episodeDetails.component.guestsOfHonorItems
import com.baghdad.ui.feature.movieDetails.component.OverviewSection
import com.baghdad.ui.navigation.graph.tvShowDetails.TvShowDetailsNavEvent
import com.baghdad.viewmodel.episodeDetails.EpisodeDetailsInteractionListener
import com.baghdad.viewmodel.episodeDetails.EpisodeDetailsScreenEffect
import com.baghdad.viewmodel.episodeDetails.EpisodeDetailsScreenState
import com.baghdad.viewmodel.episodeDetails.EpisodeDetailsViewModel
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun EpisodeDetailsScreen(
    tvShowId: Long,
    seasonNumber: Int,
    episodeNumber: Int,
    viewModel: EpisodeDetailsViewModel = koinViewModel(
        key = tvShowId.toString() + seasonNumber.toString() + episodeNumber.toString(),
        parameters = { parametersOf(tvShowId, seasonNumber, episodeNumber) }
    ),
    handleNavigation: (TvShowDetailsNavEvent) -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    ObserveAsEffect(viewModel.uiEffect) { effect ->
        handleEffect(effect, handleNavigation)
    }
    EpisodeDetailsContent(
        state = uiState,
        listener = viewModel
    )
}

@Composable
fun EpisodeDetailsContent(
    state: EpisodeDetailsScreenState,
    listener: EpisodeDetailsInteractionListener,
) {
    val lazyState = rememberLazyListState()
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                modifier = Modifier
                    .fillMaxWidth(),
//                .background(animatedColor),
                onGoBackClick = listener::onBackClick,
                content = {
                    Crossfade(
                        targetState = state.isSavedToList,
                        animationSpec = tween(300)
                    ) { isSaved ->
                        IconButton(
                            icon = painterResource(if (isSaved) R.drawable.ic_save_fill else R.drawable.ic_save),
                            tintIcon = Theme.color.title,
                            onClick = listener::onSaveEpisodeClick
                        )
                    }
                }
            )
        },
        bottomBar = {
            EpisodeDetailsBottomBar(
                isRated = state.isRated,
                onRateClicked = listener::onRateEpisodeClick,
                hasTrailer = state.episode.trailerUrl.isNotBlank(),
                isLoading = state.isEpisodeDetailsLoading,
                onPlayTrailerClicked = listener::onPlayTrailerClick
            )
        }
    ) {
        LazyColumn(
            state = lazyState,
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .fillMaxSize()
                .background(Theme.color.surface),
        ) {

            item {
                AutoSlidingImageCarousel(
                    imageUrls = state.episode.headerPictures,
                    modifier = Modifier
                        .zIndex(-1f)
                        .fillMaxWidth(),
                    autoSlideDuration = 7000,
                    indicatorVisibility = state.episode.headerPictures.size > 1
                )
            }

            item {
                EpisodeDetailsHeader(
                    title = state.episode.title,
                    releaseDate = state.episode.releasedDate,
                    rating = state.episode.rating,
                    //TODO
                    categories = emptyList(),
                    onCategoryClicked = { listener.onCategoryClick(it) },
                    seasonNumber = state.episode.currentSeason,
                    modifier = Modifier
                        .offset(y = (-48).dp)
                        .padding(horizontal = 16.dp),
                )
            }

            item {
                OverviewSection(
                    overview = state.episode.overview,
                    isExtended = state.isOverviewExpanded,
                    onExtendClicked = listener::onReadMoreOverviewClick
                )
            }
            guestsOfHonorItems(
                guestsOfHonor = state.guestsOfHonor,
                onClick = listener::onGuestOfHonorClick
            )
        }
    }
}

fun handleEffect(
    effect: EpisodeDetailsScreenEffect,
    handleNavigation: (TvShowDetailsNavEvent) -> Unit
) {
    when (effect) {
        EpisodeDetailsScreenEffect.NavigateBack -> handleNavigation(TvShowDetailsNavEvent.NavigateBack)
        EpisodeDetailsScreenEffect.NavigateToLogin -> handleNavigation(TvShowDetailsNavEvent.NavigateToLogin)
        is EpisodeDetailsScreenEffect.NavigateToActorDetails -> handleNavigation(
            TvShowDetailsNavEvent.NavigateToActorDetails(effect.actorId)
        )

        is EpisodeDetailsScreenEffect.NavigateToCategoryTvShows -> handleNavigation(
            TvShowDetailsNavEvent.NavigateToCategoryTvShows(effect.categoryId)
        )
    }
}