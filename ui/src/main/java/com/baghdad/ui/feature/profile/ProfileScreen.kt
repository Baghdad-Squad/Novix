package com.baghdad.ui.feature.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.baghdad.design_system.component.Scaffold
import com.baghdad.design_system.component.Text
import com.baghdad.design_system.theme.Theme
import com.baghdad.ui.R
import com.baghdad.ui.feature.profile.component.ProfileHeaderWithOption
import com.baghdad.ui.feature.profile.component.ProfileItemWithDefaults
import com.baghdad.ui.feature.profile.component.ProfileScreenDivider
import com.baghdad.ui.feature.profile.component.ProfileScreenItem
import com.baghdad.viewmodel.profile.ProfileInteractionListener
import com.baghdad.viewmodel.profile.ProfileScreenUIState
import com.baghdad.viewmodel.profile.ProfileViewModel

@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel = hiltViewModel(),
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
    Scaffold(
        modifier = Modifier
            .background(Theme.color.surface)
            .systemBarsPadding()
            .statusBarsPadding(),
        topBar = {
            Text(
                text = stringResource(R.string.my_account),
                style = Theme.typography.title.large,
                color = Theme.color.title,
                modifier =
                    Modifier
                        .padding(horizontal = 16.dp)
                        .padding(vertical = 25.dp),
            )
        },
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 16.dp)
        ) {
            ProfileHeaderWithOption(
                userName = state.userName,
                imageUrl = state.imageUrl,
                onLogoutClick = {},
            )
            ProfileScreenItem(
                title = stringResource(R.string.watching_history),
                icon = painterResource(com.baghdad.design_system.R.drawable.ic_time_schedule),
                onClick = {}
            )
            ProfileScreenDivider()
            ProfileScreenItem(
                title = stringResource(R.string.my_rating),
                icon = painterResource(com.baghdad.design_system.R.drawable.ic_star_square),
                onClick = {}
            )
            ProfileScreenDivider()
            ProfileScreenItem(
                title = stringResource(R.string.content_restriction),
                icon = painterResource(R.drawable.shield_energy),
                onClick = {}
            )
            ProfileScreenDivider()
            ProfileScreenItem(
                title = stringResource(R.string.change_password),
                icon = painterResource(com.baghdad.design_system.R.drawable.ic_lock_key),
                onClick = {}
            )
            ProfileScreenDivider()
            ProfileItemWithDefaults(
                title = stringResource(R.string.appearance),
                icon = painterResource(com.baghdad.design_system.R.drawable.ic_moon),
                defaultValue = state.appearance,
                onClick = {}
            )
            ProfileScreenDivider()
            ProfileItemWithDefaults(
                title = stringResource(R.string.language),
                icon = painterResource(com.baghdad.design_system.R.drawable.ic_language_circle),
                defaultValue = state.appearance,
                onClick = {}
            )

        }
    }
}