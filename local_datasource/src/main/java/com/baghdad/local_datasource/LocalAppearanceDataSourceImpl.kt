package com.baghdad.local_datasource

import android.content.Context
import android.content.SharedPreferences
import com.baghdad.repository.datasource.local.LocalAppearanceDataSource
import com.baghdad.repository.model.AppAppearanceMode
import androidx.core.content.edit
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class LocalAppearanceDataSourceImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : LocalAppearanceDataSource {

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    override suspend fun saveAppearanceMode(appearance: AppAppearanceMode) {
        sharedPreferences.edit {
            putString(KEY_APPEARANCE_MODE, appearance.name)
        }
    }

    override suspend fun getAppearanceMode(): AppAppearanceMode {
        val saved = sharedPreferences.getString(KEY_APPEARANCE_MODE, null)
        return saved?.let { runCatching { AppAppearanceMode.valueOf(it) }.getOrNull() }
            ?: AppAppearanceMode.DARK
    }

    companion object {
        private const val PREF_NAME = "settings"
        private const val KEY_APPEARANCE_MODE = "appearance_mode"
    }
}