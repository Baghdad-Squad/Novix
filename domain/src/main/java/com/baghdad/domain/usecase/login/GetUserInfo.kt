package com.baghdad.domain.usecase.login

import com.baghdad.domain.repository.AuthenticationRepository
import javax.inject.Inject

class GetUserInfo @Inject constructor(
    private val authenticationRepository: AuthenticationRepository
) {
    suspend operator fun invoke() = authenticationRepository.getUserInfo()
}