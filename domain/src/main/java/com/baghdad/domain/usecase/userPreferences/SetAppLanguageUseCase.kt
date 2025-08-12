package com.baghdad.domain.usecase.userPreferences

import com.baghdad.domain.repository.UserPreferencesRepository
import javax.inject.Inject

class SetAppLanguageUseCase @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository
) {
    suspend operator fun invoke(language: String) {
        userPreferencesRepository.setAppLanguage(language = language)
    }
}