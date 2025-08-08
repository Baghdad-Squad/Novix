package com.baghdad.viewmodel.profile

import androidx.annotation.StringRes
import com.baghdad.viewmodel.R

enum class LanguagePreferences(
    @StringRes val title: Int,
    val languageCode: String = "",
    val flag: String = ""
) {
    ENGLISH(R.string.english, "en", "ENG"),
    ARABIC(R.string.arabic, "ar", "AR");

    companion object {
        fun fromLanguageCode(code: String): LanguagePreferences {
            return entries.firstOrNull { it.languageCode == code } ?: ARABIC
        }
    }
}