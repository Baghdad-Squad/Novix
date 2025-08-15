package com.baghdad.repository

import com.baghdad.domain.model.profile.ContentRestrictionTypes
import com.baghdad.domain.repository.AppConfigurationsRepository
import com.baghdad.repository.datasource.local.AppConfigurationDataSource
import com.baghdad.repository.mapper.toContentRestrictionType
import com.baghdad.repository.mapper.toContentRestrictionTypeDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.Locale
import javax.inject.Inject

class AppConfigurationsRepositoryImpl @Inject constructor(
    private val appConfigurationDataSource: AppConfigurationDataSource
) : AppConfigurationsRepository {

    override suspend fun isAppInDarkTheme(): Flow<Boolean> {
        return appConfigurationDataSource.isAppInDarkTheme()
    }

    override suspend fun setDarkTheme(enabled: Boolean) {
        appConfigurationDataSource.setDarkTheme(enabled)
    }

    override suspend fun getAppLanguage(): Flow<String> {
        return appConfigurationDataSource.getAppLanguage().map {
            if (it == null) {
                appConfigurationDataSource.setAppLanguage(Locale.getDefault().language)
                Locale.getDefault().language
            } else it
        }
    }

    override suspend fun setAppLanguage(language: String) {
        appConfigurationDataSource.setAppLanguage(language)
    }

    override suspend fun setContentRestriction(restriction: ContentRestrictionTypes) {
        appConfigurationDataSource.setContentRestriction(restriction.toContentRestrictionTypeDto())
    }

    override suspend fun getContentRestriction(): Flow<ContentRestrictionTypes> {
        return appConfigurationDataSource.getContentRestriction()
            .map { it.toContentRestrictionType() }
    }

    override suspend fun isFirstTimeUser(): Boolean {
        return appConfigurationDataSource.isFirstTimeLaunchApp()
    }

    override suspend fun setFirstTimeUser(firstTime: Boolean) {
        appConfigurationDataSource.setFirstTimeStatus()
    }

}