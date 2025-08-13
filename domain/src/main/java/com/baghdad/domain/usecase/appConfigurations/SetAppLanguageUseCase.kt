package com.baghdad.domain.usecase.appConfigurations

import com.baghdad.domain.repository.AppConfigurationsRepository
import javax.inject.Inject

class SetAppLanguageUseCase @Inject constructor(
    private val appConfigurationsRepository: AppConfigurationsRepository
) {
    suspend operator fun invoke(language: String) {
        appConfigurationsRepository.setAppLanguage(language)
    }
}