package com.baghdad.ui.feature.actorDetails

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.baghdad.ui.base.ObserveAsEffect
import com.baghdad.ui.navigation.graph.actorDetails.ActorDetailsNavEvent
import com.baghdad.ui.navigation.graph.actorDetails.ActorDetailsNavEvent.NavigateToMovieDetails
import com.baghdad.ui.navigation.graph.actorDetails.ActorDetailsNavEvent.NavigateToTvShowDetails
import com.baghdad.viewmodel.actorDetails.ActorDetailsScreenEffect
import com.baghdad.viewmodel.actorDetails.ActorDetailsViewModel
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun ActorDetailsScreen(
    actorId: Long,
    viewModel: ActorDetailsViewModel = koinViewModel(
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
    ActorDetailsContent(
        uiState = uiState,
        listener = viewModel,
        snackBarState = snackBarState,
    )
}

private fun handleEffect(
    effect: ActorDetailsScreenEffect,
    handleNavigation: (ActorDetailsNavEvent) -> Unit
) {
    when (effect) {
        is ActorDetailsScreenEffect.NavigateBack -> handleNavigation(
            ActorDetailsNavEvent.NavigateBack
        )

        is ActorDetailsScreenEffect.NavigateToMovieDetails -> handleNavigation(
            NavigateToMovieDetails(effect.movieId)
        )

        is ActorDetailsScreenEffect.NavigateToTvShowDetails -> handleNavigation(
            NavigateToTvShowDetails(effect.tvShowId)
        )

        ActorDetailsScreenEffect.NavigateToActorGallery -> handleNavigation(
            ActorDetailsNavEvent.NavigateToActorGallery
        )

        ActorDetailsScreenEffect.NavigateToActorTopMoviePicks -> handleNavigation(
            ActorDetailsNavEvent.NavigateToActorTopMoviePicks
        )

        ActorDetailsScreenEffect.NavigateToActorTopTvShowPicks -> handleNavigation(
            ActorDetailsNavEvent.NavigateToActorTopTvShowPicks
        )

        ActorDetailsScreenEffect.NavigateToLogin -> handleNavigation(
            ActorDetailsNavEvent.NavigateToLogin
        )
    }
}
