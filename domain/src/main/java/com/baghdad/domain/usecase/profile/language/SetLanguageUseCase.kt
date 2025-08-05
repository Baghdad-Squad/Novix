package com.baghdad.domain.usecase.profile.language

import com.baghdad.domain.repository.LanguageProviderRepository
import javax.inject.Inject

class SetLanguageUseCase@Inject constructor(
    private val languageProviderRepository: LanguageProviderRepository
) {
    suspend operator fun invoke(languageCode: String) {
        languageProviderRepository.setLanguage(languageCode)
    }
}