package com.baghdad.ui.feature.topTvShowPicks

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.baghdad.design_system.R
import com.baghdad.design_system.component.HomeCard
import com.baghdad.design_system.component.Scaffold
import com.baghdad.design_system.component.SnackBar
import com.baghdad.design_system.component.Text
import com.baghdad.design_system.component.button.IconButton
import com.baghdad.design_system.theme.Theme
import com.baghdad.ui.base.ObserveAsEffect
import com.baghdad.ui.base.toStringResource
import com.baghdad.ui.navigation.graph.actorDetails.ActorDetailsNavEvent
import com.baghdad.ui.navigation.graph.actorDetails.ActorDetailsNavEvent.NavigateToMovieDetails
import com.baghdad.viewmodel.base.SnackBarState
import com.baghdad.viewmodel.errorStates.BaseSnackBarMessage
import com.baghdad.viewmodel.topTvShowPicks.TopTvShowPicksEffect
import com.baghdad.viewmodel.topTvShowPicks.TopTvShowPicksInteractionListener
import com.baghdad.viewmodel.topTvShowPicks.TopTvShowPicksState
import com.baghdad.viewmodel.topTvShowPicks.TopTvShowViewModel
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun TopTvShowPicksScreen(
    actorId: Long,
    viewModel: TopTvShowViewModel = koinViewModel(
        key = actorId.toString(),
        parameters = { parametersOf(actorId) }
    ),
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
            NavigateToMovieDetails(effect.tvShowId)
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
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Theme.color.surface)
                    .statusBarsPadding()
                    .padding(top = 12.dp, bottom = 17.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    icon = painterResource(R.drawable.ic_go_back),
                    onClick = { listener.onBackClick() },
                    modifier = Modifier
                        .padding(start = 16.dp, bottom = 8.dp)
                )
                Text(
                    text = stringResource(com.baghdad.ui.R.string.top_tv_shows_picks),
                    style = Theme.typography.title.large,
                    color = Theme.color.title,
                    modifier = Modifier
                        .padding(start = 8.dp, bottom = 8.dp)
                )
            }
        }, snackbar = {
            SnackBar(
                message = stringResource(snackBarMessage(snackBarState.message)),
                isSuccess = snackBarState.isSuccess,
                isVisible = snackBarState.isVisible
            )
        }
    ) {
        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 150.dp),
            modifier = Modifier
                .fillMaxSize()
                .background(Theme.color.surface),
            contentPadding = PaddingValues(
                start = 16.dp,
                end = 16.dp,
                top = 8.dp,
                bottom = 8.dp
            ),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            items(uiState.tvShows) { tvShow ->
                HomeCard(
                    url = tvShow.posterPictureURL,
                    contentDescription = null,
                    isSaved = tvShow.isSaved,
                    onSavedClick = { listener.onSaveTvShowClick(tvShow.id) },
                    onClick = { listener.onMovieDetailsClick(tvShow.id) },
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

