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
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.baghdad.ui.R
import com.baghdad.ui.feature.component.AppWebView
import com.baghdad.ui.navigation.graph.myAccount.MyAccountNavEvent
import kotlinx.coroutines.delay
import java.util.Locale

@Composable
fun ResetPasswordWebViewScreen(
    handleNavigation: (MyAccountNavEvent) -> Unit
) {
    val shouldNavigateToLogin = remember { mutableStateOf(false) }
    val shouldShowError = remember { mutableStateOf(false) }
    val languageTag = remember { if (Locale.getDefault().language == "ar") "ar-SA" else "en-US" }

    ResetPasswordWebViewContent(
        handleNavigation = handleNavigation,
        shouldNavigateToLogin = shouldNavigateToLogin,
        shouldShowError = shouldShowError,
        languageTag = languageTag
    )
}

@Composable
private fun ResetPasswordWebViewContent(
    handleNavigation: (MyAccountNavEvent) -> Unit,
    shouldNavigateToLogin: MutableState<Boolean>,
    shouldShowError: MutableState<Boolean>,
    languageTag: String
) {
    val context = LocalContext.current

    val screenUrl = remember {
        "https://www.themoviedb.org/reset-password?language=$languageTag"
    }

    val messages = remember {
        PasswordMessages(
            pageNotFound = context.getString(R.string.oops_we_can_t_find_the_page),
            error = context.getString(R.string.there_was_a_problem),
            success = context.getString(R.string.password_reset_completed_successfully),
            mainHeader = context.getString(R.string.reset_your_password),
            successToast = context.getString(R.string.password_reset_success_redirecting_to_login),
            errorToast = context.getString(R.string.reset_password_failed)
        )
    }

    HandleNavigationEvents(
        shouldNavigateToLogin = shouldNavigateToLogin,
        shouldShowError = shouldShowError,
        messages = messages,
        context = context,
        handleNavigation = handleNavigation
    )

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
            onDetected = { detectedText ->
                when (detectedText.trim('"')) {
                    messages.success -> shouldNavigateToLogin.value = true
                    messages.pageNotFound,
                    messages.error,
                    messages.successToast -> Unit
                }
            }
        )
    }
}

@Composable
private fun HandleNavigationEvents(
    shouldNavigateToLogin: MutableState<Boolean>,
    shouldShowError: MutableState<Boolean>,
    messages: PasswordMessages,
    context: android.content.Context,
    handleNavigation: (MyAccountNavEvent) -> Unit
) {
    LaunchedEffect(shouldNavigateToLogin.value) {
        if (shouldNavigateToLogin.value) {
            Toast.makeText(context, messages.successToast, Toast.LENGTH_LONG).show()
            delay(3000)
            handleNavigation(MyAccountNavEvent.NavigateToLogin)
        }
    }

    LaunchedEffect(shouldShowError.value) {
        if (shouldShowError.value) {
            Toast.makeText(context, messages.errorToast, Toast.LENGTH_LONG).show()
            delay(2000)
            handleNavigation(MyAccountNavEvent.NavigateBack)
        }
    }
}