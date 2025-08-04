package com.baghdad.local_datasource.dataStore.session

import android.util.Log
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
                val test = dataStore.data.map { preferences ->
                    preferences[booleanPreferencesKey(name = IS_FIRST_TIME_LAUNCH_APP)] != false
                }.first()
                Log.i("Test Logging ", test.toString())
                test

            },
            logger = logger
        ) ?: true
    }

    override suspend fun clearFirstTimeStatus() {
        safeDataStoreCall(
            block = {
                dataStore.edit { preferences ->
                    preferences.remove(booleanPreferencesKey(name = IS_FIRST_TIME_LAUNCH_APP))
                }
            },
            logger = logger
        )
    }

    companion object {
        private const val IS_FIRST_TIME_LAUNCH_APP = "is_first_time_launch_app"
    }
}