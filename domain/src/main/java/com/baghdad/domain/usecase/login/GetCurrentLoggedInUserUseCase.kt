package com.baghdad.domain.usecase.login

import com.baghdad.domain.repository.AuthenticationRepository
import javax.inject.Inject

class GetCurrentLoggedInUserUseCase @Inject constructor(
    private val authenticationRepository: AuthenticationRepository
) {
    suspend operator fun invoke() = authenticationRepository.getLoggedInUser()
}