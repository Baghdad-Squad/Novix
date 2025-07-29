package com.baghdad.ui.feature.authentication

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.baghdad.ui.R
import com.baghdad.ui.feature.component.AppWebView
import com.baghdad.ui.navigation.graph.authentication.AuthenticationNavEvent
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Locale

@Composable
fun ForgotPasswordWebViewScreen(
    handleNavigation: (AuthenticationNavEvent) -> Unit
) {
    val shouldNavigateBack = remember { mutableStateOf(false) }
    val languageTag = remember { if (Locale.getDefault().language == "ar") "ar-SA" else "en-US" }

    ForgotPasswordWebViewContent(
        handleNavigation = handleNavigation,
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
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val screenUrl = remember { "https://www.themoviedb.org/reset-password?language=$languageTag" }
    val messages = remember {
        mapOf(
            "pageNotFound" to context.getString(R.string.oops_we_can_t_find_the_page),
            "error" to context.getString(R.string.there_was_a_problem),
            "success" to context.getString(R.string.password_reset_success_message),
            "mainHeader" to context.getString(R.string.password_reset_main_page_header),
        )
    }

    LaunchedEffect(shouldNavigateBack.value) {
        if (shouldNavigateBack.value) {
            scope.launch {
                Toast
                    .makeText(
                        context,
                        context.getString(R.string.automatically_redirecting_to_login),
                        Toast.LENGTH_LONG,
                    ).show()
            }
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
            onDetected = {
                when (it.trim('"')) {
                    messages["pageNotFound"],
                    messages["error"] -> handleNavigation(AuthenticationNavEvent.NavigateBack)

                    messages["success"] -> shouldNavigateBack.value = true
                    messages["mainHeader"] -> Unit
                    else -> handleNavigation(AuthenticationNavEvent.NavigateBack)
                }
            }
        )
    }
}
