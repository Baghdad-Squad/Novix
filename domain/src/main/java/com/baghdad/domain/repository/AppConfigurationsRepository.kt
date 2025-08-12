package com.baghdad.domain.repository

import kotlinx.coroutines.flow.Flow

interface AppConfigurationsRepository {
    suspend fun isAppInDarkTheme(): Flow<Boolean>
    suspend fun setDarkTheme(enabled: Boolean)
    suspend fun getAppLanguage(): Flow<String>
    suspend fun setAppLanguage(language: String)
    suspend fun isFirstTimeUser(): Boolean
    suspend fun setFirstTimeUser(firstTime: Boolean)
}
