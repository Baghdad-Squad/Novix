package com.baghdad.viewmodel.profile

import androidx.annotation.StringRes
import com.baghdad.viewmodel.R

enum class ThemePreferences(@StringRes val title: Int, val isDark: Boolean = false) {
    DARK(R.string.dark, true),
    LIGHT(R.string.light, false)
}