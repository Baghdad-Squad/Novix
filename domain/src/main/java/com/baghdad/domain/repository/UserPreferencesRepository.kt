package com.baghdad.domain.repository

import kotlinx.coroutines.flow.Flow

interface UserPreferencesRepository {
    suspend fun isAppInDarkTheme(): Flow<Boolean>
    suspend fun setDarkTheme(enabled: Boolean)
    suspend fun getAppLanguage(): Flow<String>
    suspend fun setAppLanguage(language: String)
}
