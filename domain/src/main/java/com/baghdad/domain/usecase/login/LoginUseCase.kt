package com.baghdad.domain.usecase.login

import com.baghdad.domain.exception.EmptyFieldException
import com.baghdad.domain.exception.InValidPasswordException
import com.baghdad.domain.repository.AuthenticationRepository
import javax.inject.Inject


class LoginUseCase @Inject constructor(
    private val authenticationRepository: AuthenticationRepository
) {
    suspend operator fun invoke(userName: String, password: String) {
        validateCredential(userName, password)
        authenticationRepository.login(
            userName = userName,
            password = password
        )
    }

    fun validateCredential(userName: String, password: String) {
        if (password.isEmpty() || userName.isEmpty()) throw EmptyFieldException()
        if (password.length < 4) throw InValidPasswordException()
    }
}