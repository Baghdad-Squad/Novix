package com.baghdad.domain.usecase.login

import com.baghdad.domain.repository.AuthenticationRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class LoginUseCaseTest {

    private lateinit var authenticationRepository: AuthenticationRepository
    private lateinit var loginUseCase: LoginUseCase

    @BeforeEach
    fun setUp() {
        authenticationRepository = mockk()
        loginUseCase = LoginUseCase(authenticationRepository)
    }

    @Test
    fun `should not throw any exception when Successful login`() = runTest {
        // Given
        val username = "user"
        val password = "password"

        // When
        coEvery { authenticationRepository.login(username, password) } just runs

        // Then
        loginUseCase.invoke(username, password)

        coVerify { authenticationRepository.login(username, password) }
    }
}