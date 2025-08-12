package com.baghdad.domain.usecase.login

import jakarta.inject.Inject
import com.baghdad.domain.exception.EmptyFieldException
import com.baghdad.domain.exception.InValidPasswordException

class ValidateCredentialsUseCase @Inject constructor() {
    operator fun invoke(userName: String, password: String) {
        if (password.isEmpty() || userName.isEmpty()) {
            throw EmptyFieldException()
        }
        if (password.length < MIN_PASSWORD_LENGTH) {
            throw InValidPasswordException()
        }
    }

    companion object {
        private const val MIN_PASSWORD_LENGTH = 4
    }
}
