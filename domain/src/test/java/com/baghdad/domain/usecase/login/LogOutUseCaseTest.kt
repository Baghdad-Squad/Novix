package com.baghdad.domain.usecase.login

import com.baghdad.domain.repository.AuthenticationRepository
import com.baghdad.domain.repository.SavedListRepository
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class LogOutUseCaseTest {
    private lateinit var authenticationRepository: AuthenticationRepository
    private lateinit var savedListRepository: SavedListRepository
    private lateinit var logOutUseCase: LogOutUseCase

    @BeforeEach
    fun setUp() {
        authenticationRepository = mockk(relaxed = true)
        savedListRepository = mockk(relaxed = true)
        logOutUseCase = LogOutUseCase(authenticationRepository, savedListRepository)
    }

    @Test
    fun `logOutUseCase() should return true when logout is successful`() = runTest {
        coEvery { authenticationRepository.logOut() } returns true
        coEvery { savedListRepository.clearSavedMoviesCache() } returns Unit

        val result = logOutUseCase.invoke()

        assertThat(result).isTrue()
    }

    @Test
    fun `logOutUseCase() should clear user data when logout is successful`() = runTest {
        coEvery { authenticationRepository.logOut() } returns true

        logOutUseCase.invoke()

        coVerify(exactly = 1) { savedListRepository.clearSavedMoviesCache() }
    }

    @Test
    fun `logOutUseCase() should NOT clear user data when logout fails`() = runTest {
        coEvery { authenticationRepository.logOut() } returns false

        logOutUseCase.invoke()

        coVerify(exactly = 0) { savedListRepository.clearSavedMoviesCache() }
    }

    @Test
    fun `logOutUseCase() should return false when logout fails`() = runTest {
        coEvery { authenticationRepository.logOut() } returns false

        val result = logOutUseCase.invoke()

        assertThat(result).isFalse()
    }
}