package com.baghdad.localDatasource.util

import android.content.Context
import android.os.Build
import android.os.LocaleList
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat

class AppLanguageController(private val context: Context) {
    fun changeLanguage(languageCode: String) {
        val localeListCompat = LocaleListCompat.forLanguageTags(languageCode)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val localeManager = context.getSystemService(android.app.LocaleManager::class.java)
            localeManager?.applicationLocales = LocaleList.forLanguageTags(languageCode)
        } else {
            AppCompatDelegate.setApplicationLocales(localeListCompat)
        }
    }
}