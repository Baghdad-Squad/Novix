package com.baghdad.domain.repository

import com.baghdad.domain.model.profile.ContentRestrictionTypes
import kotlinx.coroutines.flow.Flow

interface AppConfigurationsRepository {
    suspend fun isAppInDarkTheme(): Flow<Boolean>
    suspend fun setDarkTheme(enabled: Boolean)
    suspend fun getAppLanguage(): Flow<String>
    suspend fun setAppLanguage(language: String)
    suspend fun setContentRestriction(restriction: ContentRestrictionTypes)
    suspend fun getContentRestriction(): Flow<ContentRestrictionTypes>
    suspend fun isFirstTimeUser(): Boolean
    suspend fun setFirstTimeUser(firstTime: Boolean)
}
