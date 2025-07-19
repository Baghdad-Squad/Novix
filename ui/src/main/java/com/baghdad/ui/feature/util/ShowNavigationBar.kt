package com.baghdad.ui.feature.util

import android.app.Activity
import android.content.Context
import android.view.View
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat

fun showNavigationBar(context: Context, view: View) {
    val window = (context as? Activity)?.window ?: return
    WindowCompat.setDecorFitsSystemWindows(window, true)
    val controller = WindowInsetsControllerCompat(window, view)
    controller.show(WindowInsetsCompat.Type.navigationBars())
}