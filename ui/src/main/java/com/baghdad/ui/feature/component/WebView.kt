package com.baghdad.ui.feature.component

import android.annotation.SuppressLint
import android.view.ViewGroup
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.webkit.WebSettings
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.net.toUri

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun AppWebView(
    url: String,
    modifier: Modifier = Modifier,
    allowedDomains: List<String> = emptyList(),
    onUrlChange: ((String) -> Unit)? = null,
    onPageStarted: ((String) -> Unit)? = null,
    onPageFinished: ((String) -> Unit)? = null,
    onReceivedError: ((String) -> Unit)? = null,
    onAuthenticationResult: ((AuthResult) -> Unit)? = null
) {
    AndroidView(
        factory = { context ->
            WebView(context).apply {
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )

                settings.apply {
                    javaScriptEnabled = true
                    domStorageEnabled = true
                    cacheMode = WebSettings.LOAD_DEFAULT
                    setSupportZoom(true)
                    builtInZoomControls = false
                    displayZoomControls = false
                    allowFileAccess = false
                    allowContentAccess = false
                    setGeolocationEnabled(false)
                }
                webViewClient = CustomWebViewClient(
                    allowedDomains = allowedDomains,
                    onUrlChange = onUrlChange,
                    onPageStarted = onPageStarted,
                    onPageFinished = onPageFinished,
                    onReceivedError = onReceivedError,
                    onAuthenticationResult = onAuthenticationResult
                )
            }
        },
        update = { webView ->
            if (webView.url != url) {
                android.util.Log.i("AppWebView", "Loading new URL: $url")
                webView.loadUrl(url)
            }
        },
        modifier = modifier
    )
}

class CustomWebViewClient(
    private val allowedDomains: List<String> = emptyList(),
    private val onUrlChange: ((String) -> Unit)? = null,
    private val onPageStarted: ((String) -> Unit)? = null,
    private val onPageFinished: ((String) -> Unit)? = null,
    private val onReceivedError: ((String) -> Unit)? = null,
    private val onAuthenticationResult: ((AuthResult) -> Unit)? = null
) : WebViewClient() {

    companion object {
        private const val TAG = "CustomWebViewClient"
    }

    override fun shouldOverrideUrlLoading(
        view: WebView?,
        request: WebResourceRequest?
    ): Boolean {
        val requestUrl = request?.url?.toString()

        requestUrl?.let { url ->
            android.util.Log.i(TAG, "URL Loading: $url")

            checkAuthenticationFlow(url)

            onUrlChange?.invoke(url)

            if (allowedDomains.isEmpty()) {
                android.util.Log.i(TAG, "No domain restrictions - allowing URL")
                return false
            }

            val isAllowed = allowedDomains.any { domain ->
                url.contains(domain, ignoreCase = true)
            }

            if (isAllowed) {
                android.util.Log.i(TAG, "URL allowed by domain filter")
            } else {
                android.util.Log.w(TAG, "URL blocked by domain filter")
            }
            return !isAllowed
        }
        return super.shouldOverrideUrlLoading(view, request)
    }

    private fun checkAuthenticationFlow(url: String) {
        android.util.Log.i(TAG, "Checking authentication flow for: $url")

        when {

            url.contains("success", ignoreCase = true) -> {
                android.util.Log.i(TAG, "Authentication SUCCESS detected in URL")
                onAuthenticationResult?.invoke(AuthResult.Success(url))
            }
            url.contains("dashboard", ignoreCase = true) -> {
                android.util.Log.i(TAG, "Redirected to DASHBOARD - likely successful login")
                onAuthenticationResult?.invoke(AuthResult.Success(url))
            }
            url.contains("home", ignoreCase = true) && !url.contains("login") -> {
                android.util.Log.i(TAG, "Redirected to HOME - likely successful login")
                onAuthenticationResult?.invoke(AuthResult.Success(url))
            }
            url.contains("profile", ignoreCase = true) -> {
                android.util.Log.i(TAG, "Redirected to PROFILE - likely successful login")
                onAuthenticationResult?.invoke(AuthResult.Success(url))
            }
            url.contains("error", ignoreCase = true) -> {
                android.util.Log.w(TAG, "Authentication ERROR detected in URL")
                onAuthenticationResult?.invoke(AuthResult.Error(url, "Error in URL"))
            }
            url.contains("invalid", ignoreCase = true) -> {
                android.util.Log.w(TAG, "INVALID credentials detected in URL")
                onAuthenticationResult?.invoke(AuthResult.Error(url, "Invalid credentials"))
            }
            url.contains("failed", ignoreCase = true) -> {
                android.util.Log.w(TAG, "Authentication FAILED detected in URL")
                onAuthenticationResult?.invoke(AuthResult.Error(url, "Authentication failed"))
            }
            url.contains("denied", ignoreCase = true) -> {
                android.util.Log.w(TAG, "Access DENIED detected in URL")
                onAuthenticationResult?.invoke(AuthResult.Error(url, "Access denied"))
            }
            url.contains("login", ignoreCase = true) -> {
                android.util.Log.i(TAG, "LOGIN page detected")
                onAuthenticationResult?.invoke(AuthResult.LoginPage(url))
            }
            url.contains("signin", ignoreCase = true) -> {
                android.util.Log.i(TAG, "SIGNIN page detected")
                onAuthenticationResult?.invoke(AuthResult.LoginPage(url))
            }
            url.contains("auth", ignoreCase = true) -> {
                android.util.Log.i(TAG, "AUTH page detected")
                onAuthenticationResult?.invoke(AuthResult.LoginPage(url))
            }
            else -> {
                android.util.Log.d(TAG, "Regular navigation - no auth indicators detected")
            }
        }

        if (url.contains("?") || url.contains("&")) {
            checkUrlParameters(url)
        }
    }

    private fun checkUrlParameters(url: String) {
        android.util.Log.d(TAG, "Checking URL parameters for auth indicators")

        val uri = url.toUri()

        uri.getQueryParameter("status")?.let { status ->
            android.util.Log.i(TAG, "Status parameter found: $status")
            when (status.lowercase()) {
                "success", "ok", "authenticated" -> {
                    onAuthenticationResult?.invoke(AuthResult.Success(url))
                }
                "error", "failed", "invalid" -> {
                    onAuthenticationResult?.invoke(AuthResult.Error(url, "Status: $status"))
                }
            }
        }

        uri.getQueryParameter("error")?.let { error ->
            android.util.Log.w(TAG, "Error parameter found: $error")
            onAuthenticationResult?.invoke(AuthResult.Error(url, error))
        }

        uri.getQueryParameter("message")?.let { message ->
            android.util.Log.i(TAG, "Message parameter found: $message")
        }

        // OAuth/Token indicators
        uri.getQueryParameter("access_token")?.let { token ->
            android.util.Log.i(TAG, "Access token found (length: ${token.length})")
            onAuthenticationResult?.invoke(AuthResult.Success(url))
        }

        uri.getQueryParameter("code")?.let { code ->
            android.util.Log.i(TAG, "Authorization code found (length: ${code.length})")
            onAuthenticationResult?.invoke(AuthResult.Success(url))
        }
    }

    override fun onPageStarted(view: WebView?, url: String?, favicon: android.graphics.Bitmap?) {
        super.onPageStarted(view, url, favicon)
        android.util.Log.i(TAG, "Page started loading: $url")
        url?.let { onPageStarted?.invoke(it) }
    }

    override fun onPageFinished(view: WebView?, url: String?) {
        super.onPageFinished(view, url)
        android.util.Log.i(TAG, "Page finished loading: $url")

        url?.let {
            onPageFinished?.invoke(it)
            checkAuthenticationFlow(it)

            view?.title?.let { title ->
                android.util.Log.i(TAG, "Page title: $title")
                checkPageTitle(title, it)
            }
        }
    }

    private fun checkPageTitle(title: String, url: String) {
        when {
            title.contains("Dashboard", ignoreCase = true) -> {
                android.util.Log.i(TAG, "Dashboard title detected - likely successful login")
                onAuthenticationResult?.invoke(AuthResult.Success(url))
            }
            title.contains("Welcome", ignoreCase = true) -> {
                android.util.Log.i(TAG, "Welcome title detected - likely successful login")
                onAuthenticationResult?.invoke(AuthResult.Success(url))
            }
            title.contains("Error", ignoreCase = true) -> {
                android.util.Log.w(TAG, "Error in page title")
                onAuthenticationResult?.invoke(AuthResult.Error(url, "Error in page title"))
            }
            title.contains("Invalid", ignoreCase = true) -> {
                android.util.Log.w(TAG, "Invalid in page title")
                onAuthenticationResult?.invoke(AuthResult.Error(url, "Invalid credentials"))
            }
            title.contains("Login", ignoreCase = true) -> {
                android.util.Log.i(TAG, "Login page title detected")
                onAuthenticationResult?.invoke(AuthResult.LoginPage(url))
            }
        }
    }

    override fun onReceivedError(
        view: WebView?,
        request: WebResourceRequest?,
        error: android.webkit.WebResourceError?
    ) {
        super.onReceivedError(view, request, error)
        val errorDescription = error?.description?.toString() ?: "Unknown error"
        val errorUrl = request?.url?.toString() ?: "Unknown URL"

        android.util.Log.e(TAG, "WebView error: $errorDescription for URL: $errorUrl")
        onReceivedError?.invoke(errorDescription)
        onAuthenticationResult?.invoke(AuthResult.Error(errorUrl, errorDescription))
    }
    @Deprecated("Deprecated in Java")
    @Suppress("DEPRECATION")
    override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
        url?.let {
            android.util.Log.i(TAG, "URL Loading (legacy): $it")
            checkAuthenticationFlow(it)
            onUrlChange?.invoke(it)

            if (allowedDomains.isEmpty()) {
                return false
            }

            val isAllowed = allowedDomains.any { domain ->
                it.contains(domain, ignoreCase = true)
            }

            return !isAllowed
        }

        return super.shouldOverrideUrlLoading(view, url)
    }
}
sealed class AuthResult {
    data class Success(val url: String) : AuthResult()
    data class Error(val url: String, val message: String) : AuthResult()
    data class LoginPage(val url: String) : AuthResult()

}
