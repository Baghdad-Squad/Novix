package com.baghdad.ui.util

import android.content.Context
import android.content.Intent
import androidx.core.net.toUri


fun openYouTubeLink(context: Context, youtubeUrl: String) {
    try {
        val youtubeIntent = Intent(Intent.ACTION_VIEW, youtubeUrl.toUri())
        youtubeIntent.setPackage("com.google.android.youtube")
        context.startActivity(youtubeIntent)
    } catch (e: Exception) {
        val browserIntent = Intent(Intent.ACTION_VIEW, youtubeUrl.toUri())
        context.startActivity(browserIntent)
    }
}