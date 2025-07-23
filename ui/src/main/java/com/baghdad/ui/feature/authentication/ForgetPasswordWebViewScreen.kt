package com.baghdad.ui.feature.authentication

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.baghdad.design_system.component.Scaffold
import com.baghdad.design_system.theme.Theme
import com.baghdad.ui.feature.component.AppWebView
import com.baghdad.ui.navigation.graph.authentication.AuthenticationNavEvent
import kotlinx.coroutines.delay


@Composable
fun ForgotPasswordWebViewScreen(
    modifier: Modifier = Modifier,
    handleNavigation: (AuthenticationNavEvent) -> Unit,
) {
    var shouldNavigate by remember { mutableStateOf(false) }
    LaunchedEffect(shouldNavigate) {
        if (shouldNavigate) {
            delay(5000)
            handleNavigation(AuthenticationNavEvent.NavigateBack)
        }
    }
    val screenUrl = remember { "https://www.themoviedb.org/reset-password" }

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .background(color = Theme.color.surface)
            .statusBarsPadding()
            .navigationBarsPadding(),
    ) {

        AppWebView(
            url = screenUrl,
            modifier = Modifier.fillMaxSize(),
            allowedDomains = listOf("themoviedb.org"),
            onUrlChange = {
                if (it.startsWith("https://www.themoviedb.org/login")) {
                    shouldNavigate = true
                }
            }
        )
    }
}
