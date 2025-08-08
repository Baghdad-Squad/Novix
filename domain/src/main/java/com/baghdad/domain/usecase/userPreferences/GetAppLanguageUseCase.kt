package com.baghdad.domain.usecase.userPreferences

import com.baghdad.domain.repository.UserPreferencesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAppLanguageUseCase @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository
) {
    suspend operator fun invoke(): Flow<String> {
        return userPreferencesRepository.getAppLanguage()
    }
}