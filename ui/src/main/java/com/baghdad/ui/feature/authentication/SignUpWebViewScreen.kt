package com.baghdad.ui.feature.authentication

import android.util.Log
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

private const val OOPS_MESSAGE = "Oops! We can't find the page you're looking for"
private const val ERROR_MESSAGE = "There was a problem"
private const val LOGIN_MESSAGE = "Login to your account"

@Composable
fun SignUpWebViewScreen(handleNavigation: (AuthenticationNavEvent) -> Unit) {
    val languageTag = remember {
        if (Locale.getDefault().language == "ar") "ar-SA" else "en-US"
    }
    val screenUrl = remember { "https://www.themoviedb.org/signup?language=${languageTag}" }
    val shouldNavigateBack = remember { mutableStateOf(false) }
    SignUpWebViewContent(
        handleNavigation = { handleNavigation(it) },
        screenUrl = screenUrl,
        shouldNavigateBack = shouldNavigateBack
    )

}


@Composable
fun SignUpWebViewContent(
    handleNavigation: (AuthenticationNavEvent) -> Unit,
    screenUrl: String,
    shouldNavigateBack: MutableState<Boolean>
) {
    LaunchedEffect(shouldNavigateBack.value) {
        if (shouldNavigateBack.value) {
            delay(5000)
            handleNavigation(AuthenticationNavEvent.NavigateBack)
        }
    }
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
                Log.d("Aboud", "onUrlChange: $url")
                shouldNavigateBack.value = url != screenUrl
            },
            onReceivedError = { if (it.isNotBlank()) handleNavigation(AuthenticationNavEvent.NavigateBack) },
            onDetected = {
                when (it.trim('"')) {
                    OOPS_MESSAGE -> shouldNavigateBack.value = true
                    ERROR_MESSAGE -> shouldNavigateBack.value = true
                    LOGIN_MESSAGE -> shouldNavigateBack.value = true
                }
            }
        )
    }

}