package com.baghdad.domain.usecase.userPreferences

import com.baghdad.domain.repository.UserPreferencesRepository
import javax.inject.Inject

class SetAppThemeUseCase @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository
) {
    suspend operator fun invoke(isDarkTheme: Boolean) {
        userPreferencesRepository.setDarkTheme(enabled = isDarkTheme)
    }
}
