package com.baghdad.domain.usecase.profile.language

import com.baghdad.domain.repository.LanguageProviderRepository
import javax.inject.Inject

class GetLanguageUseCase@Inject constructor(
    private val languageProviderRepository: LanguageProviderRepository
) {
    suspend operator fun invoke(): String {
        return languageProviderRepository.getLanguage()
    }
}
