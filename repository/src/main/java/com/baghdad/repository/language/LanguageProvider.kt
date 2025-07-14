package com.baghdad.repository.language

interface LanguageProvider {
    fun getCurrentLanguage(): String
}