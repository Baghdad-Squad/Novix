package com.baghdad.domain.usecase.login

import com.baghdad.domain.repository.AuthenticationRepository
import com.baghdad.domain.repository.SavedListRepository
import javax.inject.Inject

class LogOutUseCase @Inject constructor(
    private val authenticationRepository: AuthenticationRepository,
    private val savedListRepository: SavedListRepository,
) {
    suspend operator fun invoke(): Boolean {
        val isLogoutSuccessful = authenticationRepository.logOut()
        if (isLogoutSuccessful) clearUserData()
        return isLogoutSuccessful
    }

    private suspend fun clearUserData() {
        savedListRepository.clearSavedMoviesCache()
    }
}
