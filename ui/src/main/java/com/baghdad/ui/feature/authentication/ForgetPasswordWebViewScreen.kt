package com.baghdad.ui.feature.authentication

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.baghdad.ui.feature.component.AppWebView
import com.baghdad.ui.navigation.graph.authentication.AuthenticationNavEvent
import kotlinx.coroutines.delay
import java.util.Locale

@Composable
fun ForgotPasswordWebViewScreen(
    handleNavigation: (AuthenticationNavEvent) -> Unit
) {
    val shouldNavigateBack = remember { mutableStateOf(false) }
    val languageTag = remember {
        if (Locale.getDefault().language == "ar") "ar-SA" else "en-US"
    }
    ForgotPasswordWebViewContent(
        handleNavigation = { handleNavigation(it) },
        shouldNavigateBack = shouldNavigateBack,
        languageTag = languageTag
    )
}

@Composable
fun ForgotPasswordWebViewContent(
    handleNavigation: (AuthenticationNavEvent) -> Unit,
    shouldNavigateBack: MutableState<Boolean>,
    languageTag: String,
) {

    LaunchedEffect(shouldNavigateBack) {
        if (shouldNavigateBack.value) {
            delay(5000)
            handleNavigation(AuthenticationNavEvent.NavigateBack)
        }
    }
    val screenUrl = remember { "https://www.themoviedb.org/reset-password?language=${languageTag}" }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .navigationBarsPadding()
            .statusBarsPadding()
    ) {
        AppWebView(
            url = screenUrl,
            modifier = Modifier.fillMaxSize(),
            allowedDomains = listOf("themoviedb.org"),
            onUrlChange = { url ->
                shouldNavigateBack.value = screenUrl != url
            },
        )
    }
}