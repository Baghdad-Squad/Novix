package com.baghdad.viewmodel.util

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.util.Locale

object LanguageManager {
    private val _currentLanguage = MutableStateFlow(Locale.getDefault().language)
    val currentLanguage: StateFlow<String> = _currentLanguage

    fun updateLanguage(language: String) {
        _currentLanguage.value = language
    }
}