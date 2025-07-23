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

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun AppWebView(
    url: String,
    modifier: Modifier = Modifier,
    allowedDomains: List<String> = emptyList(),
    onUrlChange: ((String) -> Unit)? = null,
    onPageFinished: ((String) -> Unit)? = null,
    onReceivedError: ((String) -> Unit)? = null,
    onNavigationComplete: (() -> Unit)? = null
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

                webViewClient = SimpleWebViewClient(
                    allowedDomains = allowedDomains,
                    onUrlChange = onUrlChange,
                    onPageFinished = onPageFinished,
                    onReceivedError = onReceivedError,
                    onNavigationComplete = onNavigationComplete
                )
            }
        },
        update = { webView ->
            if (webView.url != url) {
                webView.loadUrl(url)
            }
        },
        modifier = modifier
    )
}

class SimpleWebViewClient(
    private val allowedDomains: List<String> = emptyList(),
    private val onUrlChange: ((String) -> Unit)? = null,
    private val onPageFinished: ((String) -> Unit)? = null,
    private val onReceivedError: ((String) -> Unit)? = null,
    private val onNavigationComplete: (() -> Unit)? = null
) : WebViewClient() {

    override fun shouldOverrideUrlLoading(
        view: WebView?,
        request: WebResourceRequest?
    ): Boolean {
        val requestUrl = request?.url?.toString()

        requestUrl?.let { url ->
            onUrlChange?.invoke(url)

            if (isCompletionUrl(url)) {
                onNavigationComplete?.invoke()
            }

            if (allowedDomains.isEmpty()) {
                return false
            }

            val isAllowed = allowedDomains.any { domain ->
                url.contains(domain, ignoreCase = true)
            }

            return !isAllowed
        }
        return super.shouldOverrideUrlLoading(view, request)
    }

    private fun isCompletionUrl(url: String): Boolean {
        return url.contains("success", ignoreCase = true) ||
                url.contains("dashboard", ignoreCase = true) ||
                (url.contains("home", ignoreCase = true) && !url.contains("login")) ||
                url.contains("profile", ignoreCase = true) ||
                url.contains("complete", ignoreCase = true)
    }

    override fun onPageFinished(view: WebView?, url: String?) {
        super.onPageFinished(view, url)
        url?.let {
            onPageFinished?.invoke(it)

            if (isCompletionUrl(it)) {
                onNavigationComplete?.invoke()
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
        onReceivedError?.invoke(errorDescription)
    }

    @Deprecated("Deprecated in Java")
    @Suppress("DEPRECATION")
    override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
        url?.let {
            onUrlChange?.invoke(it)

            if (isCompletionUrl(it)) {
                onNavigationComplete?.invoke()
            }

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