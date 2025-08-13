package com.baghdad.repository

import com.baghdad.entity.user.User
import com.baghdad.repository.datasource.local.LocalSessionDataSource
import com.baghdad.repository.datasource.local.LocalUserDataSource
import com.baghdad.repository.datasource.remote.RemoteAuthenticationDataSource
import com.baghdad.repository.model.UserDto
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class AuthenticationRepositoryImplTest {
    private lateinit var remoteAuthenticationDataSource: RemoteAuthenticationDataSource
    private lateinit var localSessionDataSource: LocalSessionDataSource
    private lateinit var localUserDataSource: LocalUserDataSource
    private lateinit var authenticationRepositoryImpl: AuthenticationRepositoryImpl

    @BeforeEach
    fun setUp() {
        remoteAuthenticationDataSource = mockk(relaxed = true)
        localSessionDataSource = mockk(relaxed = true)
        localUserDataSource = mockk(relaxed = true)
        authenticationRepositoryImpl = AuthenticationRepositoryImpl(
            remoteAuthenticationDataSource = remoteAuthenticationDataSource,
            localSessionDataSource = localSessionDataSource,
            localUserDataSource = localUserDataSource
        )
    }

    @Test
    fun `login should complete successfully when authentication succeeds`() = runTest {
        // Given
        val userName = "testuser"
        val password = "testpassword"
        val requestToken = "request_token_123"
        val validatedToken = "validated_token_123"
        val sessionId = "session_id_123"
        val userDto = createMockUserDto()

        coEvery { remoteAuthenticationDataSource.getRequestToken() } returns requestToken
        coEvery {
            remoteAuthenticationDataSource.validateCredentialWithToken(
                userName,
                password,
                requestToken
            )
        } returns validatedToken
        coEvery { remoteAuthenticationDataSource.createSession(validatedToken) } returns sessionId
        coEvery { remoteAuthenticationDataSource.getUserDetails(sessionId) } returns userDto
        coEvery { localSessionDataSource.saveSessionId(sessionId) } returns Unit
        coEvery {
            localUserDataSource.saveUser(
                userDto.id,
                userDto.userName,
                userDto.imageUrl.orEmpty()
            )
        } returns Unit

        // When
        authenticationRepositoryImpl.login(userName, password)

        // Then
        coVerify { remoteAuthenticationDataSource.getRequestToken() }
        coVerify {
            remoteAuthenticationDataSource.validateCredentialWithToken(
                userName,
                password,
                requestToken
            )
        }
        coVerify { remoteAuthenticationDataSource.createSession(validatedToken) }
        coVerify { remoteAuthenticationDataSource.getUserDetails(sessionId) }
        coVerify { localSessionDataSource.saveSessionId(sessionId) }
        coVerify {
            localUserDataSource.saveUser(
                userDto.id,
                userDto.userName,
                userDto.imageUrl.orEmpty()
            )
        }
    }


    @Test
    fun `isUserLoggedIn should return true when session id exists`() = runTest {
        // Given
        val sessionId = "session_id_123"
        coEvery { localSessionDataSource.getSessionId() } returns sessionId
        // When
        val result = authenticationRepositoryImpl.isUserLoggedIn()
        // Then
        assertThat(result).isTrue()
        coVerify { localSessionDataSource.getSessionId() }
    }

    @Test
    fun `isUserLoggedIn should return false when session id is null`() = runTest {
        // Given
        coEvery { localSessionDataSource.getSessionId() } returns null
        // When
        val result = authenticationRepositoryImpl.isUserLoggedIn()
        // Then
        assertFalse(result)
        coVerify { localSessionDataSource.getSessionId() }
    }

    @Test
    fun `getLoggedInUser should return user when user exists`() = runTest {
        // Given
        val userDto = createMockUserDto()
        val expectedUser = createMockUser()
        coEvery { localUserDataSource.getUser() } returns userDto
        // When
        val result = authenticationRepositoryImpl.getUserInfo()
        // Then
        assertThat(expectedUser == result).isTrue()
        coVerify { localUserDataSource.getUser() }
    }

    @Test
    fun `getLoggedInUser should return null when user does not exist`() = runTest {
        // Given
        coEvery { localUserDataSource.getUser() } returns null
        // When
        val result = authenticationRepositoryImpl.getUserInfo()
        // Then
        assertThat(result).isNull()
        coVerify { localUserDataSource.getUser() }
    }

    @Test
    fun `logOut should return true and delete session when session exists`() = runTest {
        // Given
        val sessionId = "session_id_123"
        coEvery { localSessionDataSource.getSessionId() } returns sessionId
        coEvery { remoteAuthenticationDataSource.deleteSession(sessionId) } returns true
        coEvery { localSessionDataSource.deleteSessionId() } returns Unit
        // When
        val result = authenticationRepositoryImpl.logOut()
        // Then
        assertThat(result).isTrue()
        coVerify { localSessionDataSource.getSessionId() }
        coVerify { remoteAuthenticationDataSource.deleteSession(sessionId) }
        coVerify { localSessionDataSource.deleteSessionId() }
    }

    @Test
    fun `logOut should return false when session does not exist`() = runTest {
        // Given
        coEvery { localSessionDataSource.getSessionId() } returns null
        // When
        val result = authenticationRepositoryImpl.logOut()
        // Then
        assertThat(result).isFalse()
        coVerify { localSessionDataSource.getSessionId() }
        coVerify(exactly = 0) { remoteAuthenticationDataSource.deleteSession(any()) }
        coVerify(exactly = 0) { localSessionDataSource.deleteSessionId() }
    }

    companion object {
        private fun createMockUserDto() = UserDto(
            id = 1L,
            userName = "testuser",
            imageUrl = "https://example.com/avatar.jpg"
        )

        private fun createMockUser() = User(
            id = 1L,
            userName = "testuser",
            imageUrl = "https://example.com/avatar.jpg"
        )
    }
}