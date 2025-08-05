package com.baghdad.repository.datasource.local

import com.baghdad.repository.model.AppAppearanceMode

interface LocalAppearanceDataSource {
    suspend fun saveAppearanceMode(appearance: AppAppearanceMode)
    suspend fun getAppearanceMode(): AppAppearanceMode
}