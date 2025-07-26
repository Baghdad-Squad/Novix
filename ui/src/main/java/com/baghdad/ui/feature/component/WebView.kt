package com.baghdad.ui.feature.component

import android.annotation.SuppressLint
import android.view.ViewGroup
import android.webkit.CookieManager
import android.webkit.WebResourceError
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
    onDetected: (text: String) -> Unit
) {
    AndroidView(
        modifier = modifier,
        factory = { context ->
            WebView(context).apply {
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
                settings.configureWebSettings()
                CookieManager.getInstance().removeAllCookies(null)
                CookieManager.getInstance().flush()
                WebStorage.getInstance().deleteAllData()
                clearCache(true)

                webViewClient = SimpleWebViewClient(
                    allowedDomains = allowedDomains,
                    onUrlChange = onUrlChange,
                    onReceivedError = onReceivedError,
                    onDetected = { onDetected(it) }
                )
            }
        },
        update = { webView ->
            if (webView.url != url) {
                webView.loadUrl(url)
            }
        }
    )
}

@SuppressLint("SetJavaScriptEnabled")
private fun WebSettings.configureWebSettings() {
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

class SimpleWebViewClient(
    private val onDetected: ((text: String) -> Unit),
    private val allowedDomains: List<String> = emptyList(),
    private val onUrlChange: ((String) -> Unit)? = null,
    private val onReceivedError: ((String) -> Unit)? = null
) : WebViewClient() {

    override fun shouldOverrideUrlLoading(
        view: WebView?,
        request: WebResourceRequest?
    ): Boolean {
        val url = request?.url?.toString() ?: return super.shouldOverrideUrlLoading(view, request)

        onUrlChange?.invoke(url)

        return if (allowedDomains.isEmpty()) {
            false
        } else {
            !allowedDomains.any { domain -> url.contains(domain, ignoreCase = true) }
        }
    }

    override fun onReceivedError(
        view: WebView?,
        request: WebResourceRequest?,
        error: WebResourceError?
    ) {
        super.onReceivedError(view, request, error)
        error?.toString()?.let { onReceivedError?.invoke(it) }
    }

    override fun onPageFinished(view: WebView?, url: String?) {
        super.onPageFinished(view, url)

        detect(view, { onDetected(it) })
    }


}

private fun detect(webView: WebView?, onProblemDetected: (str: String) -> Unit) {
    webView?.evaluateJavascript(
        "(function() { " +
                "var h2 = document.querySelector('h2'); " +
                "return h2 ? h2.textContent : ''; " +
                "})();"
    ) { value ->
        onProblemDetected(value)
    }
}
