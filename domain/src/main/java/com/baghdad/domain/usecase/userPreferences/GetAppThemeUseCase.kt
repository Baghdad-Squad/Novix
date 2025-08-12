package com.baghdad.domain.usecase.userPreferences

import com.baghdad.domain.repository.UserPreferencesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAppThemeUseCase @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository
) {
    suspend operator fun invoke(): Flow<Boolean> {
        return userPreferencesRepository.isAppInDarkTheme()
    }
}