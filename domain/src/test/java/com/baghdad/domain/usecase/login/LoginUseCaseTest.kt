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
    private lateinit var validateCredentialsUseCase: ValidateCredentialsUseCase

    @BeforeEach
    fun setUp() {
        authenticationRepository = mockk()
        validateCredentialsUseCase = ValidateCredentialsUseCase()
        loginUseCase = LoginUseCase(
            authenticationRepository = authenticationRepository,
            validateCredentialsUseCase = validateCredentialsUseCase
        )
    }


    @Test
    fun `loginUseCase should throw InValidPasswordException when password less than 4 chars`() =
        runTest {
            coEvery { authenticationRepository.login(VALID_USERNAME, SHORT_PASSWORD) } just runs

            assertThrows<InValidPasswordException> {
                loginUseCase.invoke(VALID_USERNAME, SHORT_PASSWORD)
            }
        }

    @Test
    fun `loginUseCase should throw EmptyFieldException when username is empty`() = runTest {
        coEvery { authenticationRepository.login(EMPTY_STRING, VALID_PASSWORD) } just runs

        assertThrows<EmptyFieldException> {
            loginUseCase.invoke(EMPTY_STRING, VALID_PASSWORD)
        }
    }

    @Test
    fun `loginUseCase should throw EmptyFieldException when password is empty`() = runTest {
        coEvery { authenticationRepository.login(VALID_USERNAME, EMPTY_STRING) } just runs

        assertThrows<EmptyFieldException> {
            loginUseCase.invoke(VALID_USERNAME, EMPTY_STRING)
        }
    }

    @Test
    fun `loginUseCase should throw InValidUserCredentialException when the info is wrong`() =
        runTest {
            coEvery {
                authenticationRepository.login(WRONG_USERNAME, WRONG_PASSWORD)
            } throws InValidUserCredentialException()

            assertThrows<InValidUserCredentialException> {
                loginUseCase.invoke(WRONG_USERNAME, WRONG_PASSWORD)
            }
        }

    @Test
    fun `loginUseCase should succeed when username and password are valid`() = runTest {
        coEvery { authenticationRepository.login(VALID_USERNAME, VALID_PASSWORD) } just runs

        loginUseCase(VALID_USERNAME, VALID_PASSWORD)

        coVerify(exactly = 1) { authenticationRepository.login(VALID_USERNAME, VALID_PASSWORD) }
    }


    companion object {
        private const val VALID_USERNAME = "user"
        private const val VALID_PASSWORD = "password"
        private const val SHORT_PASSWORD = "pas"
        private const val EMPTY_STRING = ""
        private const val WRONG_USERNAME = "wrong"
        private const val WRONG_PASSWORD = "wrong pass"
    }
}