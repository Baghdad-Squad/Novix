package com.baghdad.ui.feature.authentication

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.baghdad.design_system.R
import com.baghdad.design_system.component.Icon
import com.baghdad.design_system.component.NovixTextField
import com.baghdad.design_system.component.Scaffold
import com.baghdad.design_system.component.SnackBar
import com.baghdad.design_system.component.Text
import com.baghdad.design_system.component.button.IconButton
import com.baghdad.design_system.component.button.PrimaryButton
import com.baghdad.design_system.component.button.TextButton
import com.baghdad.design_system.theme.NovixTheme
import com.baghdad.design_system.theme.Theme
import com.baghdad.ui.base.ObserveAsEffect
import com.baghdad.ui.base.toStringResource
import com.baghdad.ui.navigation.graph.authentication.AuthenticationNavEvent
import com.baghdad.viewmodel.base.SnackBarState
import com.baghdad.viewmodel.login.LoginInteractionListener
import com.baghdad.viewmodel.login.LoginUiEffect
import com.baghdad.viewmodel.login.LoginUiState
import com.baghdad.viewmodel.login.LoginViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    loginViewModel: LoginViewModel = koinViewModel(),
    handleNavigation: (AuthenticationNavEvent) -> Unit,
) {

    val state = loginViewModel.uiState.collectAsStateWithLifecycle().value
    val snackBarState = loginViewModel.snackBarState.collectAsStateWithLifecycle().value

    ObserveAsEffect(loginViewModel.uiEffect) {
        handleLoginEffect(it, handleNavigation)
    }


    LoginScreenContent(
        modifier = modifier,
        state = state,
        snackBarState = snackBarState,
        listener = loginViewModel
    )

}

@Composable
fun LoginScreenContent(
    state: LoginUiState,
    snackBarState: SnackBarState,
    listener: LoginInteractionListener,
    modifier: Modifier = Modifier
) {
    val screenHeight = LocalWindowInfo.current.containerSize.height.dp
    Scaffold(
        modifier = modifier
            .background(Theme.color.surface)
            .height(screenHeight)
            .fillMaxWidth()
            .statusBarsPadding()
            .navigationBarsPadding(),
        snackbar = {
            SnackBar(
                message = stringResource(snackBarState.message.toStringResource()),
                isSuccess = snackBarState.isSuccess,
                isVisible = snackBarState.isVisible
            )
        },
        topBar = {
            TopBar(listener)
        },
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 16.dp)
                .verticalScroll(state = rememberScrollState())
        ) {
            LoginForm(state, listener)
            Spacer(modifier = Modifier.weight(1f))
            BottomCreateAccount(listener)
        }

    }
}


@Composable
private fun TopBar(listener: LoginInteractionListener) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Theme.color.surface)
            .statusBarsPadding()
            .padding(vertical = 12.dp, horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            icon = painterResource(R.drawable.ic_go_back),
            onClick = listener::onNavigateBackClicked,
            modifier = Modifier.padding(end = 12.dp)
        )
        Text(
            text = stringResource(com.baghdad.ui.R.string.login),
            style = Theme.typography.title.large,
            color = Theme.color.title,
        )
    }
}

@Composable
private fun LoginForm(
    state: LoginUiState, listener: LoginInteractionListener
) {
    Icon(
        imageVector = ImageVector.vectorResource(R.drawable.logo_design),
        contentDescription = stringResource(com.baghdad.ui.R.string.login_icon),
        modifier = Modifier
            .size(64.dp)
            .padding(top = 12.dp)
    )

    Text(
        stringResource(com.baghdad.ui.R.string.login_to_your_account),
        modifier = Modifier.padding(bottom = 40.dp, top = 16.dp),
        style = Theme.typography.title.medium,
        color = Theme.color.title
    )

    NovixTextField(
        label = stringResource(com.baghdad.ui.R.string.user_name),
        value = state.userName,
        onValueChange = listener::onUserNameValueChange,
        leadingIcon = painterResource(R.drawable.ic_user_guest),
        singleLine = true,
        modifier = Modifier.padding(bottom = 16.dp)
    )

    NovixTextField(
        label = stringResource(com.baghdad.ui.R.string.password),
        value = state.password,
        onValueChange = listener::onPasswordValueChange,
        leadingIcon = painterResource(R.drawable.ic_lock_key),
        singleLine = true,
        isTextMasked = !state.isPasswordVisible,
        trailingVisibility = true,
        trailingIcon = if (!state.isPasswordVisible) painterResource(R.drawable.ic_closed_eye)
        else painterResource(R.drawable.ic_opened_eye),
        onClickTrailingIcon = listener::togglePasswordVisibility
    )

    PrimaryButton(
        isLoading = state.isLoading,
        label = stringResource(com.baghdad.ui.R.string.login),
        isEnabled = !state.isAnyFieldEmpty,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 32.dp, bottom = 12.dp)
            .clip(RoundedCornerShape(12.dp))
    ) {
        listener.onLoginClicked()
    }

    TextButton(
        textModifier = Modifier.fillMaxWidth(),
        textAlign = TextAlign.Center,
        label = stringResource(com.baghdad.ui.R.string.forgot_password),
        modifier = Modifier.fillMaxWidth(),
        onClick = listener::onForgotPasswordClicked
    )
}

@Composable
private fun BottomCreateAccount(listener: LoginInteractionListener) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 20.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            stringResource(com.baghdad.ui.R.string.don_t_have_account),
            style = Theme.typography.body.small,
            color = Theme.color.body,
            modifier = Modifier.padding(end = 4.dp),
        )
        TextButton(
            label = stringResource(com.baghdad.ui.R.string.create_account),
            onClick = listener::onRegisterClicked
        )
    }
}

private fun handleLoginEffect(
    effect: LoginUiEffect, handleNavigation: (AuthenticationNavEvent) -> Unit
) {
    handleNavigation(effect.toNavEvent())
}

private fun LoginUiEffect.toNavEvent(): AuthenticationNavEvent = when (this) {
    LoginUiEffect.NavigateBack -> AuthenticationNavEvent.NavigateBack
    LoginUiEffect.NavigateToForgotPassword -> AuthenticationNavEvent.NavigateToForgotPassword
    LoginUiEffect.NavigateToHome -> AuthenticationNavEvent.NavigateToHome
    LoginUiEffect.NavigateToRegister -> AuthenticationNavEvent.NavigateToRegister
}

@Preview
@Composable
private fun PreviewLoginScreen() {
    NovixTheme(isDarkTheme = true) {
        LoginScreen(
            handleNavigation = {}
        )
    }
}