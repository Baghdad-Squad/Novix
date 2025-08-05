package com.baghdad.repository

import com.baghdad.domain.repository.OnBoardingRepository
import com.baghdad.repository.datasource.local.LocalOnboardingDatastore
import jakarta.inject.Inject

class OnBoardingRepositoryImpl @Inject constructor(
    private val localOnboardingDatastore: LocalOnboardingDatastore,
) : OnBoardingRepository {
    override suspend fun isFirstTimeUser(): Boolean {
        return localOnboardingDatastore.isFirstTime()
    }

    override suspend fun setFirstTimeUser(firstTime: Boolean) {
        localOnboardingDatastore.setFirstTimeStatus()
    }
}