package com.baghdad.ui.feature.welcome

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.baghdad.design_system.component.Icon
import com.baghdad.design_system.component.Text
import com.baghdad.design_system.component.button.OutlinedButton
import com.baghdad.design_system.component.button.PrimaryButton
import com.baghdad.design_system.theme.Theme
import com.baghdad.ui.R
import com.baghdad.ui.base.ObserveAsEffect
import com.baghdad.ui.navigation.graph.authentication.AuthenticationNavEvent
import com.baghdad.viewmodel.welcome.WelcomeEffect
import com.baghdad.viewmodel.welcome.WelcomeInteractionListener
import com.baghdad.viewmodel.welcome.WelcomeViewModel
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@Composable
fun WelcomeScreen(
    viewModel: WelcomeViewModel = hiltViewModel(),
    handleNavigation: (AuthenticationNavEvent) -> Unit
) {
    ObserveAsEffect(viewModel.uiEffect) { effect ->
        when (effect) {
            WelcomeEffect.NavigateToContinueAsGuest -> handleNavigation(AuthenticationNavEvent.NavigateToHome)
            WelcomeEffect.NavigateToLogin -> handleNavigation(AuthenticationNavEvent.NavigateToLogin)
        }
    }

    WelcomeScreenContent(listener = viewModel)
}

@SuppressLint("ConfigurationScreenWidthHeight")
@Composable
private fun WelcomeScreenContent(
    listener: WelcomeInteractionListener
) {
    val imageHeight = (LocalConfiguration.current.screenHeightDp.dp * 0.8f).coerceIn(300.dp, 650.dp)
    val systemUiController = rememberSystemUiController()

    LaunchedEffect(Unit) {
        systemUiController.setStatusBarColor(
            color = Color.Transparent,
            darkIcons = false
        )
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .navigationBarsPadding(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        item {
            Box(
                modifier = Modifier.height(imageHeight)
            ) {
                val image = if (isSystemInDarkTheme()) {
                    painterResource(id = R.drawable.welcome_screen_dark)
                } else {
                    painterResource(id = R.drawable.welcome_screen_light)
                }

                val appLogo = if (isSystemInDarkTheme()) {
                    ImageVector.vectorResource(id = com.baghdad.design_system.R.drawable.app_logo_dark)
                } else {
                    ImageVector.vectorResource(id = com.baghdad.design_system.R.drawable.app_logo_light)
                }

                Image(
                    painter = image,
                    contentDescription = stringResource(R.string.welcome_screen_background),
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colorStops = arrayOf(
                                    0.00f to Theme.color.surface.copy(alpha = 1f),
                                    0.20f to Theme.color.surface.copy(alpha = 0.45f),
                                    0.45f to Theme.color.surface.copy(alpha = 0.5f),
                                    0.75f to Theme.color.surface.copy(alpha = 0.80f),
                                    0.90f to Theme.color.surface.copy(alpha = 0.9f),
                                    1.00f to Theme.color.surface.copy(alpha = 1f)
                                )
                            )
                        )
                )

                Icon(
                    imageVector = appLogo,
                    contentDescription = stringResource(R.string.login_icon),
                    tint = Theme.color.primary,
                    modifier = Modifier
                        .padding(bottom = 32.dp)
                        .size(88.dp)
                        .align(Alignment.BottomCenter)
                )
            }

            WelcomeDetails(
                modifier = Modifier.padding(
                    start = 16.dp,
                    end = 16.dp,
                    bottom = 32.dp
                )
            )
        }

        item {
            ActionButtons(
                onClickLogin = { listener.onClickLogin() },
                onClickContinueAsGuest = { listener.onClickContinueAsGuest() },
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .navigationBarsPadding()
            )
        }
    }
}

@Composable
private fun WelcomeDetails(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.welcome_to_novix),
            style = Theme.typography.title.large,
            color = Theme.color.title,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Text(
            text = stringResource(R.string.welcome_screen_content_details),
            style = Theme.typography.body.small,
            color = Theme.color.body,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun ActionButtons(
    onClickLogin: () -> Unit,
    onClickContinueAsGuest: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .padding(bottom = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        PrimaryButton(
            label = stringResource(R.string.login),
            onClick = onClickLogin,
            modifier = Modifier
                .padding(bottom = 12.dp)
                .fillMaxWidth()
        )
        OutlinedButton(
            label = stringResource(R.string.continue_as_guest),
            onClick = onClickContinueAsGuest,
            modifier = Modifier.fillMaxWidth()
        )
    }
}