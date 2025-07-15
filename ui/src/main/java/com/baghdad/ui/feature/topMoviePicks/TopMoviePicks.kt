package com.baghdad.ui.feature.topMoviePicks

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.baghdad.design_system.component.HomeCard
import com.baghdad.ui.base.ObserveAsEffect
import com.baghdad.ui.navigation.graph.actorDetails.ActorDetailsNavEvent
import com.baghdad.ui.navigation.graph.actorDetails.ActorDetailsNavEvent.NavigateToMovieDetails
import com.baghdad.viewmodel.topMoviePicks.TopMoviePicksEffect
import com.baghdad.viewmodel.topMoviePicks.TopMoviePicksInteractionListener
import com.baghdad.viewmodel.topMoviePicks.TopMoviePicksState
import com.baghdad.viewmodel.topMoviePicks.TopMoviePicksViewModel
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun TopMoviePicksScreen(
    actorId: Long,
    viewModel: TopMoviePicksViewModel = koinViewModel(parameters = { parametersOf(actorId) }),
    handleNavigation: (ActorDetailsNavEvent) -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    ObserveAsEffect(viewModel.uiEffect) { effect ->
        handleEffect(effect, handleNavigation)
    }
    TopMoviePicksContent(
        uiState = uiState,
        listener = viewModel
    )
}
private fun handleEffect(
    effect: TopMoviePicksEffect,
    handleNavigation: (ActorDetailsNavEvent) -> Unit
) {
    when (effect) {
        is TopMoviePicksEffect.NavigateBack -> handleNavigation(
            ActorDetailsNavEvent.NavigateBack
        )
        is TopMoviePicksEffect.NavigateToMovieDetails -> handleNavigation(
            NavigateToMovieDetails(effect.movieId)
        )
    }
}

@Composable
private fun TopMoviePicksContent(
    uiState: TopMoviePicksState,
    listener: TopMoviePicksInteractionListener,
) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 150.dp),
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),

        ) {
        items(uiState.movies) { movie ->
            HomeCard(
                url = movie.posterPictureURL,
                contentDescription = null,
                isSaved = movie.isSaved,
                onSavedClick = { listener.onSaveMovieClicked(movie.id) },
                onClick = { listener.onMovieDetailsClicked(movie.id) },
                modifier = Modifier.aspectRatio(0.8f)
            )
        }
    }
}
