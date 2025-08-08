package com.baghdad.domain.usecase.login

import com.baghdad.domain.exception.EmptyFieldException
import com.baghdad.domain.exception.InValidPasswordException
import com.baghdad.domain.exception.InValidUserCredentialException
import com.baghdad.domain.repository.AuthenticationRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class LoginUseCaseTest {

    private lateinit var authenticationRepository: AuthenticationRepository
    private lateinit var loginUseCase: LoginUseCase

    @BeforeEach
    fun setUp() {
        authenticationRepository = mockk()
        loginUseCase = LoginUseCase(authenticationRepository)
    }

    @Test
    fun `loginUseCase() should complete successfully when login is successful`() = runTest {
        // Given
        val username = "user"
        val password = "password"

        // When
        coEvery { authenticationRepository.login(username, password) } just runs

        // Then
        loginUseCase.invoke(username, password)

        coVerify(exactly = 1) { authenticationRepository.login(username, password) }
    }

    @Test
    fun `logiUseCase should throw InValidPasswordException when password less than 4 chars`() =
        runTest {
            // Given
            val username = "user"
            val password = "pas"

            coEvery { authenticationRepository.login(username, password) } just runs

            assertThrows<InValidPasswordException> {
                loginUseCase.invoke(username, password)
            }
        }

    @Test
    fun `loginUseCase should throw EmptyFieldException when username is empty`() = runTest {
        // Given
        val username = ""
        val password = "password"

        coEvery { authenticationRepository.login(username, password) } just runs

        assertThrows<EmptyFieldException> {
            loginUseCase.invoke(username, password)
        }
    }

    @Test
    fun `loginUseCase should throw EmptyFieldException when password is empty`() = runTest {
        // Given
        val username = "user_name"
        val password = ""

        coEvery { authenticationRepository.login(username, password) } just runs

        assertThrows<EmptyFieldException> {
            loginUseCase.invoke(username, password)
        }
    }

    @Test
    fun `loginUseCase should throw InValidUserCredentialException when the info is wrong`() =
        runTest {
            // Given
            val username = "wrong"
            val password = "wrong pass"

            coEvery {
                authenticationRepository.login(
                    username, password
                )
            } throws InValidUserCredentialException()

            assertThrows<InValidUserCredentialException> {
                loginUseCase.invoke(username, password)
            }
        }
}