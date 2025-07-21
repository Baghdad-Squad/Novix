package com.baghdad.ui.auth

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import android.webkit.WebView
import android.webkit.WebViewClient
import android.webkit.WebSettings
import android.view.ViewGroup

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun SignUpWebViewScreen() {
    val context = LocalContext.current

    AndroidView(
        factory = { context ->
            WebView(context).apply {
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )

                // WebView settings
                settings.apply {
                    javaScriptEnabled = true
                    domStorageEnabled = true
                    cacheMode = WebSettings.LOAD_DEFAULT
                    setSupportZoom(true)
                    builtInZoomControls = false
                    displayZoomControls = false
                }

                // Set WebViewClient to handle navigation within the WebView
                webViewClient = object : WebViewClient() {
                    override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                        // Keep navigation within the WebView
                        return false
                    }

                    override fun onPageStarted(
                        view: WebView?,
                        url: String?,
                        favicon: android.graphics.Bitmap?
                    ) {
                        super.onPageStarted(view, url, favicon)
                        // Optional: Add loading indicator logic here
                    }

                    override fun onPageFinished(view: WebView?, url: String?) {
                        super.onPageFinished(view, url)
                        // Optional: Hide loading indicator here
                    }
                }

                // Load the sign-up URL
                loadUrl("https://www.themoviedb.org/signup")
            }
        },
        modifier = Modifier.fillMaxSize()
    )
}
