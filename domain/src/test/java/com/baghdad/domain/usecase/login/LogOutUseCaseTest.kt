package com.baghdad.domain.usecase.login

import com.baghdad.domain.repository.AuthenticationRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class LogOutUseCaseTest {
    lateinit var authenticationRepository: AuthenticationRepository
    lateinit var logOutUseCase: LogOutUseCase

    @BeforeEach
    fun setUp() {
        authenticationRepository = mockk()
        logOutUseCase = LogOutUseCase(authenticationRepository)
    }

    @Test
    fun `should return true when successful logout`() = runTest{
        // Given
        coEvery { authenticationRepository.logOut() } returns true

        // When
        val result = logOutUseCase.invoke()

        // Then
        assert(result)
    }

    @Test
    fun `should return false when invalid logout`() = runTest {
        // Given
        coEvery { authenticationRepository.logOut() } returns false

        // When
        val result = logOutUseCase.invoke()

        // Then
        assert(!result)
    }

}
