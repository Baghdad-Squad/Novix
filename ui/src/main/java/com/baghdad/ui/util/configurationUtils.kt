package com.baghdad.ui.util

import android.content.res.Resources

fun isArabicSystemLocale(): Boolean {
    val locale = Resources.getSystem().configuration.locales[0]
    return locale.language == "ar"
}