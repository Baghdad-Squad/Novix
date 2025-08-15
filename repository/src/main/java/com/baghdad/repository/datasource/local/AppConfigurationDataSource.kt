package com.baghdad.repository.datasource.local

import com.baghdad.repository.model.ContentRestrictionTypesDto
import kotlinx.coroutines.flow.Flow

interface AppConfigurationDataSource {
    suspend fun isAppInDarkTheme(): Flow<Boolean>
    suspend fun setDarkTheme(enabled: Boolean)
    suspend fun getAppLanguage(): Flow<String?>
    suspend fun setAppLanguage(language: String)
    suspend fun setFirstTimeStatus()
    suspend fun isFirstTimeLaunchApp(): Boolean
    suspend fun setContentRestriction(restriction: ContentRestrictionTypesDto)
    suspend fun getContentRestriction(): Flow<ContentRestrictionTypesDto>
}