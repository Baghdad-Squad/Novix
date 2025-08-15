package com.baghdad.localDatasource

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.baghdad.localDatasource.errorHandler.executeFlowWithErrorHandling
import com.baghdad.localDatasource.errorHandler.executeWithErrorHandling
import com.baghdad.localDatasource.errorHandler.safeDataStoreCall
import com.baghdad.localDatasource.util.AppLanguageController
import com.baghdad.repository.datasource.local.AppConfigurationDataSource
import com.baghdad.repository.logger.Logger
import com.baghdad.repository.model.ContentRestrictionTypesDto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

@Singleton
class AppConfigurationDataSourceImpl @Inject constructor(
    @Named("preferences") private val dataStore: DataStore<Preferences>,
    private val logger: Logger,
    private val appLanguageController: AppLanguageController
) : AppConfigurationDataSource {
    override suspend fun isAppInDarkTheme(): Flow<Boolean> {
        return executeFlowWithErrorHandling(
            block = {
                dataStore.data
                    .map { preferences ->
                        preferences[DARK_THEME_KEY] ?: true
                    }
            }, logger = logger
        )
    }

    override suspend fun setDarkTheme(enabled: Boolean) {
        executeWithErrorHandling(
            block = {
                dataStore.edit { preferences ->
                    preferences[DARK_THEME_KEY] = enabled
                }
            }, logger = logger
        )
    }

    override suspend fun getAppLanguage(): Flow<String?> {
        return executeFlowWithErrorHandling(
            block = {
                dataStore.data.map { preferences ->
                    return@map preferences[APP_LANGUAGE_KEY]
                }
            }, logger = logger
        )
    }

    override suspend fun setAppLanguage(language: String) {
        executeWithErrorHandling(
            block = {
                saveLanguage(language)
                withContext(Dispatchers.Main) {
                    changeAppLanguage(language)
                }
            }, logger = logger
        )
    }

    private suspend fun saveLanguage(language: String) = dataStore.edit { preferences ->
        preferences[APP_LANGUAGE_KEY] = language
    }

    private fun changeAppLanguage(languageCode: String) {
        appLanguageController.changeLanguage(languageCode)
    }

    override suspend fun setFirstTimeStatus() {
        safeDataStoreCall(
            block = {
                dataStore.edit { preferences ->
                    preferences[IS_FIRST_TIME_LAUNCH_APP] = false
                }
            },
            logger = logger
        )
    }

    override suspend fun isFirstTimeLaunchApp(): Boolean {
        return safeDataStoreCall(
            block = {
                dataStore.data.map { preferences ->
                    preferences[IS_FIRST_TIME_LAUNCH_APP] != false
                }.first()
            },
            logger = logger
        ) != false
    }

    override suspend fun setContentRestriction(restriction: ContentRestrictionTypesDto) {
        executeWithErrorHandling(
            block = {
                dataStore.edit { preferences ->
                    preferences[CONTENT_RESTRICTION_KEY] = restriction.name
                }
            }, logger = logger
        )
    }

    override suspend fun getContentRestriction(): Flow<ContentRestrictionTypesDto> {
        return executeFlowWithErrorHandling(
            block = {
                dataStore.data.map { preferences ->
                    val restrictionName = preferences[CONTENT_RESTRICTION_KEY]
                    ContentRestrictionTypesDto.valueOf(
                        restrictionName ?: ContentRestrictionTypesDto.STRICT.name
                    )
                }
            }, logger = logger
        )
    }

    private companion object {
        val DARK_THEME_KEY = booleanPreferencesKey("dark_theme")
        val APP_LANGUAGE_KEY = stringPreferencesKey("app_language")
        val CONTENT_RESTRICTION_KEY = stringPreferencesKey("content_restriction")
        val IS_FIRST_TIME_LAUNCH_APP = booleanPreferencesKey("is_first_time_launch_app")
    }
}