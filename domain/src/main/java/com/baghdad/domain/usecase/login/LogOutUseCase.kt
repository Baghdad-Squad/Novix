package com.baghdad.domain.usecase.login

import com.baghdad.domain.repository.AuthenticationRepository

class LogOutUseCase(
    private val authenticationRepository: AuthenticationRepository
) {
    suspend operator fun invoke() {
        authenticationRepository.logOut()
    }
}