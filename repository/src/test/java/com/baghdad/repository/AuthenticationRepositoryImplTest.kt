package com.baghdad.repository

import com.baghdad.entity.User
import com.baghdad.repository.datasource.local.LocalSessionDataStore
import com.baghdad.repository.datasource.local.LocalUserDataStore
import com.baghdad.repository.datasource.remote.RemoteAuthenticationDataSource
import com.baghdad.repository.model.UserDto
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class AuthenticationRepositoryImplTest {

    private lateinit var remoteAuthenticationDataSource: RemoteAuthenticationDataSource
    private lateinit var localSessionDataStore: LocalSessionDataStore
    private lateinit var localUserDataStore: LocalUserDataStore
    private lateinit var authenticationRepositoryImpl: AuthenticationRepositoryImpl

    @BeforeEach
    fun setUp() {
        remoteAuthenticationDataSource = mockk(relaxed = true)
        localSessionDataStore = mockk(relaxed = true)
        localUserDataStore = mockk(relaxed = true)
        authenticationRepositoryImpl = AuthenticationRepositoryImpl(
            remoteAuthenticationDataSource = remoteAuthenticationDataSource,
            localSessionDataStore = localSessionDataStore,
            localUserDataStore = localUserDataStore
        )
    }
//need to fix
//    @Test
//    fun `login should return session id when authentication succeeds`() = runTest {
//        // Given
//        val userName = "testuser"
//        val password = "testpassword"
//        val requestToken = "request_token_123"
//        val validatedToken = "validated_token_123"
//        val sessionId = "session_id_123"
//        val userDto = createMockUserDto()
//
//        coEvery { remoteAuthenticationDataSource.getRequestToken() } returns requestToken
//        coEvery {
//            remoteAuthenticationDataSource.validateCredentialWithToken(userName, password, requestToken)
//        } returns validatedToken
//        coEvery { remoteAuthenticationDataSource.createSession(validatedToken) } returns sessionId
//        coEvery { remoteAuthenticationDataSource.getUserDetails(sessionId) } returns userDto
//        coEvery { localSessionDataStore.saveSessionId(sessionId) } returns Unit
//        coEvery { localUserDataStore.saveUser(userDto.id, userDto.userName, userDto.imageUrl.orEmpty()) } returns Unit
//
//        // When
//        val result = authenticationRepositoryImpl.login(userName, password)
//
//        // Then
//        assertEquals(sessionId, result)
//        coVerify { remoteAuthenticationDataSource.getRequestToken() }
//        coVerify { remoteAuthenticationDataSource.validateCredentialWithToken(userName, password, requestToken) }
//        coVerify { remoteAuthenticationDataSource.createSession(validatedToken) }
//        coVerify { remoteAuthenticationDataSource.getUserDetails(sessionId) }
//        coVerify { localSessionDataStore.saveSessionId(sessionId) }
//        coVerify { localUserDataStore.saveUser(userDto.id, userDto.userName, userDto.imageUrl.orEmpty()) }
//    }


    @Test
    fun `isUserLoggedIn should return true when session id exists`() = runTest {
        // Given
        val sessionId = "session_id_123"
        coEvery { localSessionDataStore.getSessionId() } returns sessionId

        // When
        val result = authenticationRepositoryImpl.isUserLoggedIn()

        // Then
        assertTrue(result)
        coVerify { localSessionDataStore.getSessionId() }
    }

    @Test
    fun `isUserLoggedIn should return false when session id is null`() = runTest {
        // Given
        coEvery { localSessionDataStore.getSessionId() } returns null

        // When
        val result = authenticationRepositoryImpl.isUserLoggedIn()

        // Then
        assertFalse(result)
        coVerify { localSessionDataStore.getSessionId() }
    }

    @Test
    fun `getLoggedInUser should return user when user exists`() = runTest {
        // Given
        val userDto = createMockUserDto()
        val expectedUser = createMockUser()
        coEvery { localUserDataStore.getUser() } returns userDto

        // When
        val result = authenticationRepositoryImpl.getLoggedInUser()

        // Then
        assertEquals(expectedUser, result)
        coVerify { localUserDataStore.getUser() }
    }

    @Test
    fun `getLoggedInUser should return null when user does not exist`() = runTest {
        // Given
        coEvery { localUserDataStore.getUser() } returns null

        // When
        val result = authenticationRepositoryImpl.getLoggedInUser()

        // Then
        assertNull(result)
        coVerify { localUserDataStore.getUser() }
    }

    @Test
    fun `logOut should return true and delete session when session exists`() = runTest {
        // Given
        val sessionId = "session_id_123"
        coEvery { localSessionDataStore.getSessionId() } returns sessionId
        coEvery { remoteAuthenticationDataSource.deleteSession(sessionId) } returns true
        coEvery { localSessionDataStore.deleteSessionId() } returns Unit

        // When
        val result = authenticationRepositoryImpl.logOut()

        // Then
        assertTrue(result)
        coVerify { localSessionDataStore.getSessionId() }
        coVerify { remoteAuthenticationDataSource.deleteSession(sessionId) }
        coVerify { localSessionDataStore.deleteSessionId() }
    }

    @Test
    fun `logOut should return false when session does not exist`() = runTest {
        // Given
        coEvery { localSessionDataStore.getSessionId() } returns null

        // When
        val result = authenticationRepositoryImpl.logOut()

        // Then
        assertFalse(result)
        coVerify { localSessionDataStore.getSessionId() }
        coVerify(exactly = 0) { remoteAuthenticationDataSource.deleteSession(any()) }
        coVerify(exactly = 0) { localSessionDataStore.deleteSessionId() }
    }

//need to fix
//    @Test
//    fun `login should handle user with null image url`() = runTest {
//        // Given
//        val userName = "testuser"
//        val password = "testpassword"
//        val requestToken = "request_token_123"
//        val validatedToken = "validated_token_123"
//        val sessionId = "session_id_123"
//        val userDto = UserDto(id = 1L, userName = "testuser", imageUrl = null)
//
//        coEvery { remoteAuthenticationDataSource.getRequestToken() } returns requestToken
//        coEvery {
//            remoteAuthenticationDataSource.validateCredentialWithToken(userName, password, requestToken)
//        } returns validatedToken
//        coEvery { remoteAuthenticationDataSource.createSession(validatedToken) } returns sessionId
//        coEvery { remoteAuthenticationDataSource.getUserDetails(sessionId) } returns userDto
//        coEvery { localSessionDataStore.saveSessionId(sessionId) } returns Unit
//        coEvery { localUserDataStore.saveUser(userDto.id, userDto.userName, "") } returns Unit
//
//        // When
//        val result = authenticationRepositoryImpl.login(userName, password)
//
//        // Then
//        assertEquals(sessionId, result)
//        coVerify { localUserDataStore.saveUser(userDto.id, userDto.userName, "") }
//    }

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