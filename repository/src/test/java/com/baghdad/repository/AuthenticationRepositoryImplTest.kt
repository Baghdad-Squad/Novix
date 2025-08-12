package com.baghdad.repository

import com.baghdad.entity.User
import com.baghdad.repository.datasource.local.LocalSessionDataStore
import com.baghdad.repository.datasource.local.LocalUserDataStore
import com.baghdad.repository.datasource.remote.RemoteAuthenticationDataSource
import com.baghdad.repository.model.UserDto
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class AuthenticationRepositoryImplTest {

    private lateinit var mockRemoteAuthDataSource: RemoteAuthenticationDataSource
    private lateinit var mockLocalSessionDataStore: LocalSessionDataStore
    private lateinit var mockLocalUserDataStore: LocalUserDataStore
    private lateinit var authRepositoryUnderTest: AuthenticationRepositoryImpl

    @BeforeEach
    fun setUp() {
        mockRemoteAuthDataSource = mockk(relaxed = true)
        mockLocalSessionDataStore = mockk(relaxed = true)
        mockLocalUserDataStore = mockk(relaxed = true)
        authRepositoryUnderTest = AuthenticationRepositoryImpl(
            remoteAuthenticationDataSource = mockRemoteAuthDataSource,
            localSessionDataStore = mockLocalSessionDataStore,
            localUserDataStore = mockLocalUserDataStore
        )
    }

    @Test
    fun `login should complete full authentication flow when all remote calls succeed`() = runTest {
        // Given
        val remoteUserDto = createSampleUserDto()

        coEvery { mockRemoteAuthDataSource.getRequestToken() } returns REQUEST_TOKEN
        coEvery {
            mockRemoteAuthDataSource.validateCredentialWithToken(
                USERNAME,
                PASSWORD,
                REQUEST_TOKEN
            )
        } returns VALIDATED_TOKEN
        coEvery { mockRemoteAuthDataSource.createSession(VALIDATED_TOKEN) } returns SESSION_ID
        coEvery { mockRemoteAuthDataSource.getUserDetails(SESSION_ID) } returns remoteUserDto
        coEvery { mockLocalSessionDataStore.saveSessionId(SESSION_ID) } returns Unit
        coEvery {
            mockLocalUserDataStore.saveUser(
                remoteUserDto.id,
                remoteUserDto.userName,
                remoteUserDto.imageUrl.orEmpty()
            )
        } returns Unit

        // When
        authRepositoryUnderTest.login(USERNAME, PASSWORD)

        // Then
        coVerify { mockRemoteAuthDataSource.getRequestToken() }
        coVerify {
            mockRemoteAuthDataSource.validateCredentialWithToken(
                USERNAME,
                PASSWORD,
                REQUEST_TOKEN
            )
        }
        coVerify { mockRemoteAuthDataSource.createSession(VALIDATED_TOKEN) }
        coVerify { mockRemoteAuthDataSource.getUserDetails(SESSION_ID) }
        coVerify { mockLocalSessionDataStore.saveSessionId(SESSION_ID) }
        coVerify {
            mockLocalUserDataStore.saveUser(
                remoteUserDto.id,
                remoteUserDto.userName,
                remoteUserDto.imageUrl.orEmpty()
            )
        }
    }

    @Test
    fun `isUserLoggedIn should return true when valid session ID exists in local storage`() =
        runTest {
            // Given
            coEvery { mockLocalSessionDataStore.getSessionId() } returns SESSION_ID

            // When
            val isLoggedInResult = authRepositoryUnderTest.isUserLoggedIn()

            // Then
            assertThat(isLoggedInResult).isTrue()
            coVerify { mockLocalSessionDataStore.getSessionId() }
        }

    @Test
    fun `isUserLoggedIn should return false when no session ID exists in local storage`() =
        runTest {
            // Given
            coEvery { mockLocalSessionDataStore.getSessionId() } returns null

            // When
            val isLoggedInResult = authRepositoryUnderTest.isUserLoggedIn()

            // Then
            assertThat(isLoggedInResult).isFalse()
            coVerify { mockLocalSessionDataStore.getSessionId() }
        }

    @Test
    fun `getLoggedInUser should return mapped user entity when user data exists locally`() =
        runTest {
            // Given
            val localUserDto = createSampleUserDto()
            val expectedUserEntity = createSampleUserEntity()
            coEvery { mockLocalUserDataStore.getUser() } returns localUserDto

            // When
            val actualUserResult = authRepositoryUnderTest.getLoggedInUser()

            // Then
            assertThat(actualUserResult).isEqualTo(expectedUserEntity)
            coVerify { mockLocalUserDataStore.getUser() }
        }

    @Test
    fun `getLoggedInUser should return null when no user data exists locally`() = runTest {
        // Given
        coEvery { mockLocalUserDataStore.getUser() } returns null

        // When
        val actualUserResult = authRepositoryUnderTest.getLoggedInUser()

        // Then
        assertThat(actualUserResult).isNull()
        coVerify { mockLocalUserDataStore.getUser() }
    }

    @Test
    fun `logOut should successfully delete session when session exists and remote deletion succeeds`() =
        runTest {
            // Given
            coEvery { mockLocalSessionDataStore.getSessionId() } returns SESSION_ID
            coEvery { mockRemoteAuthDataSource.deleteSession(SESSION_ID) } returns true
            coEvery { mockLocalSessionDataStore.deleteSessionId() } returns Unit

            // When
            val logOutResult = authRepositoryUnderTest.logOut()

            // Then
            assertThat(logOutResult).isTrue()
            coVerify { mockLocalSessionDataStore.getSessionId() }
            coVerify { mockRemoteAuthDataSource.deleteSession(SESSION_ID) }
            coVerify { mockLocalSessionDataStore.deleteSessionId() }
        }

    @Test
    fun `logOut should return false and skip remote calls when no session exists locally`() =
        runTest {
            // Given
            coEvery { mockLocalSessionDataStore.getSessionId() } returns null

            // When
            val logOutResult = authRepositoryUnderTest.logOut()

            // Then
            assertThat(logOutResult).isFalse()
            coVerify { mockLocalSessionDataStore.getSessionId() }
            coVerify(exactly = 0) { mockRemoteAuthDataSource.deleteSession(any()) }
            coVerify(exactly = 0) { mockLocalSessionDataStore.deleteSessionId() }
        }

    private fun createSampleUserDto() = UserDto(
        id = USER_ID,
        userName = USERNAME,
        imageUrl = AVATAR_URL
    )

    private fun createSampleUserEntity() = User(
        id = USER_ID,
        userName = USERNAME,
        imageUrl = AVATAR_URL
    )

    private companion object {
        const val USERNAME = "fara7_Baghdad"
        const val PASSWORD = "secure_password_123"
        const val REQUEST_TOKEN = "tmdb_request_token_abc123"
        const val VALIDATED_TOKEN = "tmdb_validated_token_xyz789"
        const val SESSION_ID = "tmdb_session_id_def456"
        const val USER_ID = 12345L
        const val AVATAR_URL = "https://secure.gravatar.com/avatar/fara7.jpg"
    }
}