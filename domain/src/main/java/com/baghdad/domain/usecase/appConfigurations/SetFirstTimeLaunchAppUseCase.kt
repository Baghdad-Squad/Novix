package com.baghdad.domain.usecase.appConfigurations

import com.baghdad.domain.repository.AppConfigurationsRepository
import jakarta.inject.Inject

class SetFirstTimeLaunchAppUseCase @Inject constructor(
    private val appConfigurationsRepository: AppConfigurationsRepository
) {
    suspend operator fun invoke(firstTime: Boolean) {
        appConfigurationsRepository.setFirstTimeUser(firstTime = firstTime)
    }
}