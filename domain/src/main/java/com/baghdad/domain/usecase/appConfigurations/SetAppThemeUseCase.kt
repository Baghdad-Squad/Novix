package com.baghdad.domain.usecase.appConfigurations

import com.baghdad.domain.repository.AppConfigurationsRepository
import javax.inject.Inject

class SetAppThemeUseCase @Inject constructor(
    private val appConfigurationsRepository: AppConfigurationsRepository
) {
    suspend operator fun invoke(isDarkTheme: Boolean) {
        appConfigurationsRepository.setDarkTheme(isDarkTheme)
    }
}
