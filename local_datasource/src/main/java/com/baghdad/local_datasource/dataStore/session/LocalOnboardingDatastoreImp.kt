package com.baghdad.local_datasource.dataStore.session

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import com.baghdad.local_datasource.dataStore.safeDataStoreCall
import com.baghdad.repository.datasource.local.LocalOnboardingDatastore
import com.baghdad.repository.logger.Logger
import jakarta.inject.Inject
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Named

class LocalOnboardingDatastoreImp @Inject constructor(
    @Named("preferences") private val dataStore: DataStore<Preferences>,
    private val logger: Logger
) : LocalOnboardingDatastore {

    override suspend fun setFirstTimeStatus() {
        safeDataStoreCall(
            block = {
                dataStore.edit { preferences ->
                    preferences[booleanPreferencesKey(name = IS_FIRST_TIME_LAUNCH_APP)] = false
                }
            },
            logger = logger
        )
    }

    override suspend fun isFirstTime(): Boolean {
        return safeDataStoreCall(
            block = {
                dataStore.data.map { preferences ->
                    preferences[booleanPreferencesKey(name = IS_FIRST_TIME_LAUNCH_APP)] != false
                }.first()
            },
            logger = logger
        ) ?: true
    }

    companion object {
        private const val IS_FIRST_TIME_LAUNCH_APP = "is_first_time_launch_app"
    }
}