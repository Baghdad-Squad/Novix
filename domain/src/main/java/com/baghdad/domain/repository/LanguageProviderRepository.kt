package com.baghdad.domain.repository

interface LanguageProviderRepository {
    suspend fun getLanguage(): String
    suspend fun setLanguage(languageCode: String)
}