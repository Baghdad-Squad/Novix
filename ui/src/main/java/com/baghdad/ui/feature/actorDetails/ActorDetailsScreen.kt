package com.baghdad.ui.feature.actorDetails

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.baghdad.viewmodel.actorDetails.ActorDetailsViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun ActorDetailsScreen(
    viewModel: ActorDetailsViewModel = koinViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    ActorDetailsContent(
        uiState = uiState,
        listener = viewModel
    )
}

