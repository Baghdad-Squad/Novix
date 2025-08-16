package com.baghdad.ui.feature.trendingActors

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.collectAsLazyPagingItems
import com.baghdad.design_system.component.BackgroundBlur
import com.baghdad.design_system.component.Scaffold
import com.baghdad.design_system.component.SnackBar
import com.baghdad.design_system.component.appBar.TopAppBar
import com.baghdad.design_system.theme.Theme
import com.baghdad.ui.R
import com.baghdad.ui.base.ObserveAsEffect
import com.baghdad.ui.base.toStringResource
import com.baghdad.ui.feature.component.ActorCard
import com.baghdad.ui.navigation.graph.home.HomeNavEvent
import com.baghdad.viewmodel.base.SnackBarState
import com.baghdad.viewmodel.errorStates.BaseSnackBarMessage
import com.baghdad.viewmodel.trendingActors.TrendingActorViewModel
import com.baghdad.viewmodel.trendingActors.TrendingActorsInteractionListener
import com.baghdad.viewmodel.trendingActors.TrendingActorsUiEffect
import com.baghdad.viewmodel.trendingActors.TrendingActorsUiState

@Composable
fun TrendingActorsScreen(
    viewModel: TrendingActorViewModel = hiltViewModel(),
    handleNavigation: (HomeNavEvent) -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackBarState by viewModel.snackBarState.collectAsStateWithLifecycle()

    TrendingActorsContent(
        uiState = uiState,
        listener = viewModel,
        snackBarState = snackBarState
    )

    ObserveAsEffect(viewModel.uiEffect) { effect ->
        when (effect) {
            is TrendingActorsUiEffect.OnBackClick -> {
                handleNavigation(HomeNavEvent.NavigateBack)
            }

            is TrendingActorsUiEffect.NavigateToActorsDetails -> {
                handleNavigation(HomeNavEvent.NavigateToActorDetails(effect.actorId))
            }
        }
    }
}


@Composable
private fun TrendingActorsContent(
    uiState: TrendingActorsUiState,
    listener: TrendingActorsInteractionListener,
    snackBarState: SnackBarState
) {
    val trendingActors = uiState.trendingActor.collectAsLazyPagingItems()

    Scaffold(
        modifier = Modifier
            .background(Theme.color.surface)
            .systemBarsPadding()
            .statusBarsPadding()
            .padding(vertical = 12.dp),

        isLoading = uiState.isLoading,

        topBar = {
            TopAppBar(
                modifier = Modifier.padding(vertical = 8.dp),
                onGoBackClick = { listener.onBackClick() },
                screenTitle = stringResource(R.string.trending_people),
            )
        },
        snackbar = { position ->
            SnackBar(
                message = stringResource(snackBarMessage(snackBarState.message)),
                isSuccess = snackBarState.isSuccess,
                isVisible = snackBarState.isVisible,
                actionLabel = snackBarState.actionLabelRes?.let { stringResource(it) },
                onActionClick = listener::onSnackBarActionLabelClick,
                position = position
            )
        },
        backgroundBlur = { BackgroundBlur() },

        isSnackBarWithActionLabel = snackBarState.actionLabelRes != null,

    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
            ) {
                items(trendingActors.itemCount) { index ->
                    trendingActors[index]?.let { actor ->
                        ActorCard(
                            actorName = actor.name,
                            actorImage = actor.profilePictureURL,
                            onClick = { listener::onTrendingActorClick },
                            modifier = Modifier.padding(vertical = 12.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun snackBarMessage(type: BaseSnackBarMessage): Int {
    return type.toStringResource()
}