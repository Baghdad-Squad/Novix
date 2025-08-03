package com.baghdad.ui.feature.welcome

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.baghdad.design_system.component.Icon
import com.baghdad.design_system.component.Scaffold
import com.baghdad.design_system.component.Text
import com.baghdad.design_system.component.button.OutlinedButton
import com.baghdad.design_system.component.button.PrimaryButton
import com.baghdad.design_system.theme.Theme
import com.baghdad.ui.R
import com.baghdad.ui.base.ObserveAsEffect
import com.baghdad.ui.navigation.graph.onBoarding.OnBoardingNavEvent
import com.baghdad.viewmodel.welcome.WelcomeEffect
import com.baghdad.viewmodel.welcome.WelcomeInteractionListener
import com.baghdad.viewmodel.welcome.WelcomeViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun WelcomeScreen(
    viewModel: WelcomeViewModel = koinViewModel(),
    handleNavigation: (OnBoardingNavEvent) -> Unit
) {

    ObserveAsEffect(
        viewModel.uiEffect
    ) { effect ->
        when (effect) {
            WelcomeEffect.NavigateToContinueAsGuest -> handleNavigation(OnBoardingNavEvent.NavigateToHome)
            WelcomeEffect.NavigateToLogin -> handleNavigation(OnBoardingNavEvent.NavigateToLogin)
        }
    }

    WelcomeScreenContent(
        listener = viewModel
    )
}


@Composable
fun WelcomeScreenContent(
    listener: WelcomeInteractionListener
) {
    Scaffold(
        modifier =
            Modifier
                .background(Theme.color.surface)
                .fillMaxSize()
                .navigationBarsPadding(),
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(modifier = Modifier.weight(1f)) {
                Image(
                    painter = painterResource(R.drawable.img_welcome_background),
                    contentDescription = stringResource(R.string.welcome_screen_background),
                    modifier = Modifier.fillMaxWidth().offset(y = 32.dp),
                    contentScale = ContentScale.Crop,
                )
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .offset(y = 32.dp)
                        .background(
                            Brush.verticalGradient(
                                colorStops = arrayOf(
                                    0.00f to Theme.color.surface.copy(alpha = 1f),
                                    0.20f to Theme.color.surface.copy(alpha = 0.35f),
                                    0.45f to Theme.color.surface.copy(alpha = 0.35f),
                                    0.75f to Theme.color.surface.copy(alpha = 0.70f),
                                    0.90f to Theme.color.surface.copy(alpha = 0.85f),
                                    1.00f to Theme.color.surface.copy(alpha = 1f),
                                )
                            )
                        )
,
                )
                Icon(
                    imageVector = ImageVector.vectorResource(com.baghdad.design_system.R.drawable.logo_design),
                    contentDescription = stringResource(R.string.login_icon),
                    tint = Theme.color.primary,
                    modifier = Modifier
                        .padding(bottom = 32.dp)
                        .size(88.dp)
                        .align(Alignment.BottomCenter)
                )
            }

            WelcomeDetails(Modifier.padding(horizontal = 16.dp))
            ActionButtons(
                onClickLogin = {
                    listener.onClickLogin()
                },
                onClickContinueAsGuest = {
                    listener.onClickContinueAsGuest()
                },
                Modifier
                    .padding(horizontal = 16.dp)
                    .navigationBarsPadding()
            )
        }
    }
}

@Composable
fun WelcomeDetails(modifier: Modifier = Modifier) {
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
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
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 32.dp)
        )
    }
}

@Composable
fun ActionButtons(
    onClickLogin: () -> Unit,
    onClickContinueAsGuest: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        PrimaryButton(
            label = stringResource(R.string.login),
            onClick = { onClickLogin() },
            modifier = Modifier
                .padding(bottom = 12.dp)
                .fillMaxWidth()
        )
        OutlinedButton(
            label = stringResource(R.string.continue_as_guest),
            onClick = { onClickContinueAsGuest() },
            modifier = Modifier
                .fillMaxWidth()
        )
    }
}


