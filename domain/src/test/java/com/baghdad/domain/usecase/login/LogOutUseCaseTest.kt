package com.baghdad.domain.usecase.login

import com.baghdad.domain.repository.AuthenticationRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class LogOutUseCaseTest {
    private lateinit var authenticationRepository: AuthenticationRepository
    private lateinit var logOutUseCase: LogOutUseCase

    @BeforeEach
    fun setUp() {
        authenticationRepository = mockk()
        logOutUseCase = LogOutUseCase(authenticationRepository)
    }

    @Test
    fun `logOutUseCase() should return true when logout is successful`() = runTest {
        // Given
        coEvery { authenticationRepository.logOut() } returns true

        // When
        val result = logOutUseCase.invoke()

        // Then
        assert(result)
    }

    @Test
    fun `logOutUseCase() should return false when logout fails`() = runTest {
        // Given
        coEvery { authenticationRepository.logOut() } returns false

        // When
        val result = logOutUseCase.invoke()

        // Then
        assert(!result)
    }
}