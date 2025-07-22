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
    onUrlChange: ((String) -> Unit)? = null
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
                }

                webViewClient = AppWebViewClient(
                    allowedDomains = allowedDomains,
                    onUrlChange = onUrlChange
                )
            }
        },
        update = { webView ->
            webView.loadUrl(url)
        },
        modifier = modifier
    )
}

class AppWebViewClient(
    private val allowedDomains: List<String>,
    private val onUrlChange: ((String) -> Unit)?
) : WebViewClient() {

    override fun shouldOverrideUrlLoading(
        view: WebView?,
        request: WebResourceRequest?
    ): Boolean {
        val currentUrl = request?.url?.toString()

        currentUrl?.let {
            onUrlChange?.invoke(it)
            if (allowedDomains.isEmpty()) return false
            return !allowedDomains.any { domain ->
                it.contains(domain, ignoreCase = true)
            }
        }

        return super.shouldOverrideUrlLoading(view, request)
    }
}