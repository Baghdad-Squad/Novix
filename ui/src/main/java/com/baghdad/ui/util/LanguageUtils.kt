package com.baghdad.ui.util

import android.content.Context
import android.os.Build
import android.os.LocaleList
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import java.util.Locale


fun changeAppLanguage(context: Context, languageCode: String) {
    val localeListCompat = LocaleListCompat.forLanguageTags(languageCode)

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        val localeManager = context.getSystemService(android.app.LocaleManager::class.java)
        localeManager?.applicationLocales = LocaleList.forLanguageTags(languageCode)
    } else {
        AppCompatDelegate.setApplicationLocales(localeListCompat)
    }
}


fun getCurrentLanguage(): String {
    val locales = AppCompatDelegate.getApplicationLocales()
    return if (!locales.isEmpty) {
        locales[0]?.language ?: Locale.getDefault().language
    } else {
        Locale.getDefault().language
    }
}

