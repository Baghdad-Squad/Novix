package com.baghdad.ui.feature.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.baghdad.design_system.component.BackgroundBlur
import com.baghdad.design_system.component.Scaffold
import com.baghdad.design_system.component.SnackBar
import com.baghdad.design_system.component.Text
import com.baghdad.design_system.shared.Selectable
import com.baghdad.design_system.theme.Theme
import com.baghdad.ui.R
import com.baghdad.ui.base.ObserveAsEffect
import com.baghdad.ui.base.toStringResource
import com.baghdad.ui.feature.component.bottomSheet.AppLanguageBottomSheet
import com.baghdad.ui.feature.component.bottomSheet.AppThemeBottomSheet
import com.baghdad.ui.feature.component.bottomSheet.ContentRestrictionBottomSheet
import com.baghdad.ui.feature.profile.component.GuestScreen
import com.baghdad.ui.feature.profile.component.LogOutBottomSheet
import com.baghdad.ui.feature.profile.component.ProfileHeaderWithOption
import com.baghdad.ui.feature.profile.component.ProfileScreenItemsList
import com.baghdad.ui.navigation.graph.myAccount.MyAccountNavEvent
import com.baghdad.viewmodel.base.SnackBarState
import com.baghdad.viewmodel.errorStates.BaseSnackBarMessage
import com.baghdad.viewmodel.profile.ContentRestriction
import com.baghdad.viewmodel.profile.ProfileEffect
import com.baghdad.viewmodel.profile.ProfileInteractionListener
import com.baghdad.viewmodel.profile.ProfileScreenState
import com.baghdad.viewmodel.profile.ProfileViewModel

@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel = hiltViewModel(),
    handleNavigation: (MyAccountNavEvent) -> Unit,
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val snackBarState by viewModel.snackBarState.collectAsStateWithLifecycle()

    ObserveAsEffect(viewModel.uiEffect) { effect ->
        handleEffect(effect, handleNavigation)
    }
    ProfileScreenContent(
        state = state,
        listener = viewModel,
        snackBarState = snackBarState,
    )
}

private fun handleEffect(
    effect: ProfileEffect,
    handleNavigation: (MyAccountNavEvent) -> Unit,
) {
    when (effect) {
        is ProfileEffect.NavigateBack -> handleNavigation(
            MyAccountNavEvent.NavigateBack,
        )

        is ProfileEffect.NavigateToMyRatings -> handleNavigation(
            MyAccountNavEvent.NavigateToMyRatings,
        )

        is ProfileEffect.NavigateToWatchingHistory -> handleNavigation(
            MyAccountNavEvent.NavigateToWatchingHistory,
        )

        is ProfileEffect.NavigateToLogin -> handleNavigation(
            MyAccountNavEvent.NavigateToLogin,
        )

        is ProfileEffect.NavigateToChangePassword -> handleNavigation(
            MyAccountNavEvent.NavigateToChangePassword,
        )
    }
}

@Composable
private fun ProfileScreenContent(
    state: ProfileScreenState,
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
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 25.dp)
            )
        },

        isLoading = state.isLoading,

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
        backgroundBlur = { BackgroundBlur() },

        isSnackBarWithActionLabel = snackBarState.actionLabelRes != null,
    ) {
        if (state.isUserLoggedIn) {
            Column(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                ProfileHeaderWithOption(
                    userName = state.userInfo.userName,
                    imageUrl = state.userInfo.imageUrl,
                    onLogoutClick = listener::onLogOutClick,
                )
                ProfileScreenItemsList(
                    appearance = stringResource(state.userSettings.appearance.title),
                    language = stringResource(state.userSettings.language.title),
                    onclickWatchingHistory = listener::onWatchingHistoryClick,
                    onclickMyRating = listener::onMyRatingClick,
                    onclickContentRestriction = listener::onContentRestrictionClick,
                    onclickChangePassword = listener::onChangePasswordClick,
                    onclickAppearance = { listener.onAppearanceClick() },
                    onclickLanguage = listener::onLanguageClick,
                )
            }

        } else {
            GuestScreen(listener::onLoginClick)
        }

        AppThemeBottomSheet(
            onBottomSheetCloseClick = listener::onAppearanceDialogDismissed,
            isVisible = state.themeBottomSheetState.isVisible,
            themeOptions = listOf(
                Selectable(
                    value = ProfileScreenState.ThemePreferences.LIGHT,
                    isSelected = state.themeBottomSheetState.currentTheme == ProfileScreenState.ThemePreferences.LIGHT
                ),
                Selectable(
                    value = ProfileScreenState.ThemePreferences.DARK,
                    isSelected = state.themeBottomSheetState.currentTheme == ProfileScreenState.ThemePreferences.DARK
                ),
            ),
            onThemeSelected = { listener.onAppearanceChanged(it) },
            onSaveClick = listener::onAppearanceConfirmed,
        )

        AppLanguageBottomSheet(
            onBottomSheetCloseClick = listener::onLanguageDialogDismissed,
            isVisible = state.languageBottomSheetState.isVisible,
            languageOptions = listOf(
                Selectable(
                    value = ProfileScreenState.LanguagePreferences.ENGLISH,
                    isSelected = state.languageBottomSheetState.currentLanguage == ProfileScreenState.LanguagePreferences.ENGLISH
                ),
                Selectable(
                    value = ProfileScreenState.LanguagePreferences.ARABIC,
                    isSelected = state.languageBottomSheetState.currentLanguage == ProfileScreenState.LanguagePreferences.ARABIC
                ),
            ),
            onLanguageSelected = listener::onLanguageChanged ,

            onSaveClick = listener::onLanguageConfirmed,
        )

        ContentRestrictionBottomSheet(
            onBottomSheetCloseClick = listener::onContentRestrictionDialogDismissed,
            isVisible = state.contentRestrictionBottomSheetState.isVisible,
            contentRestrictionOptions = listOf(
                Selectable(
                    value = ContentRestriction.STRICT,
                    isSelected = state.contentRestrictionBottomSheetState.currentRestriction == ContentRestriction.STRICT
                ),
                Selectable(
                    value = ContentRestriction.MODERATE,
                    isSelected = state.contentRestrictionBottomSheetState.currentRestriction == ContentRestriction.MODERATE
                ),
                Selectable(
                    value = ContentRestriction.NONE,
                    isSelected = state.contentRestrictionBottomSheetState.currentRestriction == ContentRestriction.NONE
                ),
            ),
            onContentRestrictionSelected = { listener.onContentRestrictionChanged(it) },
            onSaveClick = listener::onContentRestrictionConfirmed,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        LogOutBottomSheet(
            isVisible = state.logoutBottomSheetState.isVisible,
            onBottomSheetCloseClick = listener::onLogoutDialogDismissed,
            onLogOutClick = listener::onLogOutConfirmed,
        )
    }
}


@Composable
private fun snackBarMessage(type: BaseSnackBarMessage): Int = type.toStringResource()