package com.baghdad.repository.datasource.local

import kotlinx.coroutines.flow.Flow

interface LocalUserPreferencesDataSource {
    fun isAppInDarkTheme(): Flow<Boolean>

    suspend fun setDarkTheme(enabled: Boolean)

    fun getAppLanguage(): Flow<String?>

    suspend fun setAppLanguage(language: String)
}