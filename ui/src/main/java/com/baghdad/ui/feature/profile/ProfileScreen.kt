package com.baghdad.ui.feature.profile

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.baghdad.viewmodel.profile.ProfileInteractionListener
import com.baghdad.viewmodel.profile.ProfileScreenUIState
import com.baghdad.viewmodel.profile.ProfileViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel = koinViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    ProfileScreenContent(
        state = state,
        interactionListener = viewModel,
    )
}

@Composable
private fun ProfileScreenContent(
    state: ProfileScreenUIState,
    interactionListener: ProfileInteractionListener,
) {

}