package com.baghdad.ui.feature.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.baghdad.design_system.component.Scaffold
import com.baghdad.design_system.component.SnackBar
import com.baghdad.design_system.component.Text
import com.baghdad.design_system.theme.Theme
import com.baghdad.ui.R
import com.baghdad.ui.base.toStringResource
import com.baghdad.ui.feature.profile.component.ProfileHeaderWithOption
import com.baghdad.ui.feature.profile.component.ProfileScreenItemsList
import com.baghdad.viewmodel.base.SnackBarState
import com.baghdad.viewmodel.errorStates.BaseSnackBarMessage
import com.baghdad.viewmodel.profile.ProfileInteractionListener
import com.baghdad.viewmodel.profile.ProfileScreenUIState
import com.baghdad.viewmodel.profile.ProfileViewModel

@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val snackBarState by viewModel.snackBarState.collectAsStateWithLifecycle()
    ProfileScreenContent(
        state = state,
        listener = viewModel,
        snackBarState = snackBarState,
    )
}

@Composable
private fun ProfileScreenContent(
    state: ProfileScreenUIState,
    listener: ProfileInteractionListener,
    snackBarState: SnackBarState,
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
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .padding(vertical = 25.dp),
            )
        },
        isLoading = state.isLoading,
        snackbar = {
            SnackBar(
                message = stringResource(snackBarMessage(snackBarState.message)),
                isSuccess = snackBarState.isSuccess,
                isVisible = snackBarState.isVisible,
                actionLabel = snackBarState.actionLabelRes?.let { stringResource(it) },
                onActionClick = listener::onSnackBarActionLabelClick,
            )
        }

    ) {
        Column(
            modifier = Modifier.padding(horizontal = 16.dp)
        ) {
            ProfileHeaderWithOption(
                userName = state.userName,
                imageUrl = state.imageUrl,
                onLogoutClick = listener::ontClickLogOut,
            )
            ProfileScreenItemsList(
                appearance = state.appearance,
                language = state.language,
                onclickWatchingHistory = listener::onclickWatchingHistory,
                onclickMyRating = listener::onclickMyRating,
                onclickContentRestriction = listener::onclickContentRestriction,
                onclickChangePassword = listener::onclickChangePassword,
                onclickAppearance = listener::onclickAppearance,
                onclickLanguage = listener::onclickLanguage,
            )
        }
    }
}

@Composable
private fun snackBarMessage(type: BaseSnackBarMessage): Int = type.toStringResource()