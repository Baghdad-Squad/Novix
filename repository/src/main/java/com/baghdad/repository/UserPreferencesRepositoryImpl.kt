package com.baghdad.repository

import com.baghdad.domain.repository.UserPreferencesRepository
import com.baghdad.repository.datasource.local.LocalUserPreferencesDataSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UserPreferencesRepositoryImpl @Inject constructor(
    private val localUserPreferencesDataSource: LocalUserPreferencesDataSource
) : UserPreferencesRepository {

    override suspend fun isAppInDarkTheme(): Flow<Boolean> {
        return localUserPreferencesDataSource.isAppInDarkTheme()
    }

    override suspend fun setDarkTheme(enabled: Boolean) {
        localUserPreferencesDataSource.setDarkTheme(enabled)
    }

    override suspend fun getAppLanguage(): Flow<String> {
        return localUserPreferencesDataSource.getAppLanguage()
    }

    override suspend fun setAppLanguage(language: String) {
        localUserPreferencesDataSource.setAppLanguage(language)
    }

}