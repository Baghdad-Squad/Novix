package com.baghdad.ui.feature.authentication

import android.util.Log
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
import java.util.Locale

@Composable
fun SignUpWebViewScreen(
    handleNavigation: (AuthenticationNavEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    Log.i("language", Locale.getDefault().language)
    val languageTag = remember {
        if (Locale.getDefault().language == "ar") "ar-SA" else "en-US"
    }

    val screenUrl = remember { "https://www.themoviedb.org/signup?language=${languageTag}" }
    var shouldNavigateBack by remember { mutableStateOf(false) }
    LaunchedEffect(shouldNavigateBack) {
        if (shouldNavigateBack) {
            delay(5000)
            handleNavigation(AuthenticationNavEvent.NavigateBack)
        }
    }
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
                    shouldNavigateBack = true
                }
            }
        )
    }
}
