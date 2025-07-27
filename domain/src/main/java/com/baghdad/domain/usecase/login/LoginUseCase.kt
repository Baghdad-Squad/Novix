package com.baghdad.domain.usecase.login

import com.baghdad.domain.repository.AuthenticationRepository
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val authenticationRepository: AuthenticationRepository
) {
    suspend operator fun invoke(userName: String, password: String) {
        authenticationRepository.login(
            userName = userName,
            password = password
        )
    }
}