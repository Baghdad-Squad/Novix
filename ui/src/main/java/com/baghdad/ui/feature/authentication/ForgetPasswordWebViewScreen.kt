package com.baghdad.ui.feature.authentication

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
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
import com.baghdad.ui.feature.component.AppWebView
import com.baghdad.ui.navigation.graph.authentication.AuthenticationNavEvent
import kotlinx.coroutines.delay
import java.util.Locale

@Composable
fun ForgotPasswordWebViewScreen(
    modifier: Modifier = Modifier,
    handleNavigation: (AuthenticationNavEvent) -> Unit,
    onUrlChange: ((String) -> Unit)? = null,
    onPasswordResetComplete: (() -> Unit)? = null,
    onNavigateBack: (() -> Unit)? = null
) {
    var shouldNavigate by remember { mutableStateOf(false) }
    var hasError by remember { mutableStateOf(false) }

    val languageTag = remember {
        if (Locale.getDefault().language == "ar") "ar-SA" else "en-US"
    }

    LaunchedEffect(shouldNavigate) {
        if (shouldNavigate) {
            delay(5000)
            handleNavigation(AuthenticationNavEvent.NavigateBack)
        }
    }
        val screenUrl = remember { "https://www.themoviedb.org/reset-password?language=${languageTag}" }

        Box(
            modifier = modifier
                .fillMaxSize()
                .navigationBarsPadding()
                .statusBarsPadding()
        ) {
            AppWebView(
                url = screenUrl,
                modifier = Modifier.fillMaxSize(),
                allowedDomains = listOf("themoviedb.org"),
                onUrlChange = { url ->
                    onUrlChange?.invoke(url)
                },
                onPageFinished = { url ->
                    hasError = false
                },
                onReceivedError = { error ->
                    hasError = true
                },
                onNavigationComplete = {
                    onPasswordResetComplete?.invoke()
                }
            )
        }
    }

