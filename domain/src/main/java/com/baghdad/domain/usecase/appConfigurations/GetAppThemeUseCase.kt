package com.baghdad.domain.usecase.appConfigurations

import com.baghdad.domain.repository.AppConfigurationsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAppThemeUseCase @Inject constructor(private val appConfigurationsRepository: AppConfigurationsRepository) {
    suspend operator fun invoke(): Flow<Boolean> {
        return appConfigurationsRepository.isAppInDarkTheme()
    }
}