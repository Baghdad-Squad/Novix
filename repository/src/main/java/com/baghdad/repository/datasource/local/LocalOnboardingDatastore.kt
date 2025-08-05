package com.baghdad.repository.datasource.local

interface LocalOnboardingDatastore {
    suspend fun setFirstTimeStatus()
    suspend fun isFirstTime(): Boolean
}