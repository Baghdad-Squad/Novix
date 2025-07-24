package com.baghdad.ui.feature.component

import android.annotation.SuppressLint
import android.view.ViewGroup
import android.webkit.CookieManager
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebStorage
import android.webkit.WebView
import android.webkit.WebViewClient
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
    onReceivedError: ((String) -> Unit)? = null,
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
                    cacheMode = WebSettings.LOAD_NO_CACHE
                    setSupportZoom(true)
                    builtInZoomControls = false
                    displayZoomControls = false
                }

                CookieManager.getInstance().removeAllCookies(null)
                CookieManager.getInstance().flush()
                WebStorage.getInstance().deleteAllData()
                clearCache(true)

                webViewClient = SimpleWebViewClient(
                    allowedDomains = allowedDomains,
                    onUrlChange = { onUrlChange?.invoke(it) },
                    onReceivedError = { onReceivedError?.invoke(it) },
                )
            }
        },
        update = { webView ->
            webView.loadUrl(url)
        },
        modifier = modifier
    )
}

class SimpleWebViewClient(
    private val allowedDomains: List<String> = emptyList(),
    private val onUrlChange: ((String) -> Unit)? = null,
    private val onReceivedError: ((String) -> Unit)? = null,
) : WebViewClient() {

    override fun shouldOverrideUrlLoading(
        view: WebView?,
        request: WebResourceRequest?
    ): Boolean {
        val requestUrl = request?.url?.toString()

        requestUrl?.let { url ->
            onUrlChange?.invoke(url)
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

    override fun onReceivedError(
        view: WebView?,
        request: WebResourceRequest?,
        error: android.webkit.WebResourceError?
    ) {
        super.onReceivedError(view, request, error)
        onReceivedError?.invoke(error.toString())
    }
}