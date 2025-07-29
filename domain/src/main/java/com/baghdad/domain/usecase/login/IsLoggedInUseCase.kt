package com.baghdad.domain.usecase.login

import com.baghdad.domain.repository.AuthenticationRepository

class IsLoggedInUseCase(private val authenticationRepository: AuthenticationRepository) {
    suspend operator fun invoke(): Boolean {
        return authenticationRepository.isUserLoggedIn()
    }
}