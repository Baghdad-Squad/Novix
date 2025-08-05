package com.baghdad.domain.repository

import com.baghdad.domain.model.AppAppearanceMode

interface AppearanceRepository {
    suspend fun setAppearanceMode(appearance: AppAppearanceMode)
    suspend fun getAppearanceMode(): AppAppearanceMode
}