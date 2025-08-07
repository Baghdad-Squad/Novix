package com.baghdad.ui.feature.topTvShowPicks

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.baghdad.design_system.component.Scaffold
import com.baghdad.design_system.component.SnackBar
import com.baghdad.design_system.component.appBar.TopAppBar
import com.baghdad.design_system.theme.Theme
import com.baghdad.ui.base.ObserveAsEffect
import com.baghdad.ui.base.toStringResource
import com.baghdad.ui.feature.component.HomeCard
import com.baghdad.ui.navigation.graph.actorDetails.ActorDetailsNavEvent
import com.baghdad.ui.navigation.graph.actorDetails.ActorDetailsNavEvent.NavigateToTvShowDetails
import com.baghdad.viewmodel.base.SnackBarState
import com.baghdad.viewmodel.errorStates.BaseSnackBarMessage
import com.baghdad.viewmodel.topTvShowPicks.TopTvShowPicksEffect
import com.baghdad.viewmodel.topTvShowPicks.TopTvShowPicksInteractionListener
import com.baghdad.viewmodel.topTvShowPicks.TopTvShowPicksState
import com.baghdad.viewmodel.topTvShowPicks.TopTvShowViewModel

@Composable
fun TopTvShowPicksScreen(
    viewModel: TopTvShowViewModel = hiltViewModel(),
    handleNavigation: (ActorDetailsNavEvent) -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackBarState by viewModel.snackBarState.collectAsStateWithLifecycle()
    ObserveAsEffect(viewModel.uiEffect) { effect ->
        handleEffect(effect, handleNavigation)
    }
    TopTvShowPicksContent(
        uiState = uiState,
        listener = viewModel,
        snackBarState = snackBarState
    )
}

private fun handleEffect(
    effect: TopTvShowPicksEffect,
    handleNavigation: (ActorDetailsNavEvent) -> Unit
) {
    when (effect) {
        is TopTvShowPicksEffect.NavigateBack -> handleNavigation(
            ActorDetailsNavEvent.NavigateBack
        )

        is TopTvShowPicksEffect.NavigateToTvShowDetails -> handleNavigation(
            NavigateToTvShowDetails(effect.tvShowId)
        )
    }
}

@Composable
private fun TopTvShowPicksContent(
    uiState: TopTvShowPicksState,
    listener: TopTvShowPicksInteractionListener,
    snackBarState: SnackBarState
) {
    Scaffold(
        modifier = Modifier
            .background(Theme.color.surface)
            .systemBarsPadding()
            .statusBarsPadding(),
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
        isSnackBarWithActionLabel = snackBarState.actionLabelRes != null,
        topBar = {
            TopAppBar(
                onGoBackClick = listener::onBackClick,
                screenTitle = stringResource(com.baghdad.ui.R.string.top_tv_shows_picks),
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .padding(top = 12.dp)
            )
        },
        isLoading = uiState.isLoading
    ) {
        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 150.dp),
            modifier = Modifier
                .fillMaxSize()
                .background(Theme.color.surface),
            contentPadding = PaddingValues(
                start = 16.dp,
                end = 16.dp,
                top = 16.dp,
                bottom = 8.dp
            ),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            items(uiState.tvShows) { tvShow ->
                HomeCard(
                    url = tvShow.posterPictureURL,
                    contentDescription = null,
                    isSaveToListVisible = false,
                    onClick = { listener.onTvShowDetailsClick(tvShow.id) },
                    modifier = Modifier.aspectRatio(0.8f)
                )
            }
        }
    }
}

@Composable
private fun snackBarMessage(type: BaseSnackBarMessage): Int {
    return type.toStringResource()
}

