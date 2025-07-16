package com.baghdad.ui.feature.tvShowDetails

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.baghdad.ui.base.ObserveAsEffect
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
        uiState = uiState,
        listener = viewModel
    )
}

private fun handleEffect(
    effect: TvShowDetailsScreenEffect,
    handleNavigation: (TvShowDetailsNavEvent) -> Unit
) {

}

@Composable
fun TvShowDetailsContent(
    uiState: TvShowDetailsScreenState,
    listener: TvShowDetailsInteractionListener,
    modifier: Modifier = Modifier
) {

}