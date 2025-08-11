package com.baghdad.local_datasource

import android.content.Context
import android.os.Build
import android.os.LocaleList
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.baghdad.local_datasource.roomDB.errorHandler.executeFlowWithErrorHandling
import com.baghdad.local_datasource.roomDB.errorHandler.executeWithErrorHandling
import com.baghdad.repository.datasource.local.LocalUserPreferencesDataSource
import com.baghdad.repository.logger.Logger
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Named

class LocalUserPreferencesDataSourceImpl @Inject constructor(
    @Named("preferences") private val dataStore: DataStore<Preferences>,
    private val logger: Logger,
    @ApplicationContext private val context: Context,
) : LocalUserPreferencesDataSource {
    override  fun isAppInDarkTheme(): Flow<Boolean> {
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

    override fun getAppLanguage(): Flow<String?> {
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
                    changeAppLanguage(context, language)
                }
            }, logger = logger
        )
    }

    private suspend fun saveLanguage(language: String) = dataStore.edit { preferences ->
        preferences[APP_LANGUAGE_KEY] = language
    }

    private fun changeAppLanguage(context: Context, languageCode: String) {
        val localeListCompat = LocaleListCompat.forLanguageTags(languageCode)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val localeManager = context.getSystemService(android.app.LocaleManager::class.java)
            localeManager?.applicationLocales = LocaleList.forLanguageTags(languageCode)
        } else {
            AppCompatDelegate.setApplicationLocales(localeListCompat)
        }
    }

    companion object {
        private val DARK_THEME_KEY = booleanPreferencesKey("dark_theme")
        private val APP_LANGUAGE_KEY = stringPreferencesKey("app_language")
    }
}