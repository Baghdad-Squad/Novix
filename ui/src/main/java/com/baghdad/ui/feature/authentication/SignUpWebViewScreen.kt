package com.baghdad.ui.feature.authentication

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.baghdad.ui.feature.component.AppWebView

@Composable
fun SignUpWebViewScreen(
    onUrlChange: ((String) -> Unit)? = null
) {
    AppWebView(
        url = "https://www.themoviedb.org/signup",
        modifier = Modifier
            .fillMaxSize()
            .navigationBarsPadding()
            .statusBarsPadding(),
        allowedDomains = listOf("themoviedb.org"),
        onUrlChange = onUrlChange
    )
}