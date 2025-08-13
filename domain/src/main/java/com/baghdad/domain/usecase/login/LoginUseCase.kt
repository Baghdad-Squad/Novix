package com.baghdad.domain.usecase.login

import com.baghdad.domain.repository.AuthenticationRepository
import javax.inject.Inject


class LoginUseCase @Inject constructor(
    private val authenticationRepository: AuthenticationRepository,
    private val validateCredentialsUseCase: ValidateCredentialsUseCase
) {
    suspend operator fun invoke(userName: String, password: String) {
        validateCredentialsUseCase(userName, password)
        authenticationRepository.login(
            userName = userName,
            password = password
        )
    }
}