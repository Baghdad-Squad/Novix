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
        val remoteUserDto = createSampleUserDto()

        mockGetRequestToken()
        mockValidateCredentialWithToken(USERNAME, PASSWORD)
        mockCreateSession()
        mockGetUserDetails(remoteUserDto)
        mockSaveSessionId()
        mockSaveUser(remoteUserDto)

        authRepositoryUnderTest.login(USERNAME, PASSWORD)

        verifyGetRequestToken()
        verifyValidateCredentialWithToken(USERNAME, PASSWORD)
        verifyCreateSession()
        verifyGetUserDetails()
        verifySaveSessionId()
        verifySaveUser(remoteUserDto)
    }

    @Test
    fun `isUserLoggedIn should return true when valid session ID exists in local storage`() =
        runTest {
            mockGetSessionId(SESSION_ID)

            val result = authRepositoryUnderTest.isUserLoggedIn()

            assertThat(result).isTrue()
            verifyGetSessionId()
        }

    @Test
    fun `isUserLoggedIn should return false when no session ID exists in local storage`() =
        runTest {
            mockGetSessionId(null)

            val result = authRepositoryUnderTest.isUserLoggedIn()

            assertThat(result).isFalse()
            verifyGetSessionId()
        }

    @Test
    fun `getLoggedInUser should return mapped user entity when user data exists locally`() =
        runTest {
            val localUserDto = createSampleUserDto()
            val expectedUser = createSampleUserEntity()

            mockGetUser(localUserDto)

            val actual = authRepositoryUnderTest.getLoggedInUser()

            assertThat(actual).isEqualTo(expectedUser)
            verifyGetUser()
        }

    @Test
    fun `getLoggedInUser should return null when no user data exists locally`() = runTest {
        mockGetUser(null)

        val actual = authRepositoryUnderTest.getLoggedInUser()

        assertThat(actual).isNull()
        verifyGetUser()
    }

    @Test
    fun `logOut should successfully delete session when session exists and remote deletion succeeds`() =
        runTest {
            mockGetSessionId(SESSION_ID)
            mockDeleteSessionReturns(true)
            mockDeleteSessionId()

            val result = authRepositoryUnderTest.logOut()

            assertThat(result).isTrue()
            verifyGetSessionId()
            verifyDeleteSession()
            verifyDeleteSessionId()
        }

    @Test
    fun `logOut should return false and skip remote calls when no session exists locally`() =
        runTest {
            mockGetSessionId(null)

            val result = authRepositoryUnderTest.logOut()

            assertThat(result).isFalse()
            verifyGetSessionId()
            coVerify(exactly = 0) { mockRemoteAuthDataSource.deleteSession(any()) }
            coVerify(exactly = 0) { mockLocalSessionDataStore.deleteSessionId() }
        }

    private fun mockGetRequestToken() {
        coEvery { mockRemoteAuthDataSource.getRequestToken() } returns REQUEST_TOKEN
    }

    private fun mockValidateCredentialWithToken(username: String, password: String) {
        coEvery {
            mockRemoteAuthDataSource.validateCredentialWithToken(username, password, REQUEST_TOKEN)
        } returns VALIDATED_TOKEN
    }

    private fun mockCreateSession() {
        coEvery { mockRemoteAuthDataSource.createSession(VALIDATED_TOKEN) } returns SESSION_ID
    }

    private fun mockGetUserDetails(userDto: UserDto) {
        coEvery { mockRemoteAuthDataSource.getUserDetails(SESSION_ID) } returns userDto
    }

    private fun mockSaveSessionId() {
        coEvery { mockLocalSessionDataStore.saveSessionId(SESSION_ID) } returns Unit
    }

    private fun mockSaveUser(userDto: UserDto) {
        coEvery {
            mockLocalUserDataStore.saveUser(
                userDto.id,
                userDto.userName,
                userDto.imageUrl.orEmpty()
            )
        } returns Unit
    }

    private fun mockGetSessionId(sessionId: String?) {
        coEvery { mockLocalSessionDataStore.getSessionId() } returns sessionId
    }

    private fun mockGetUser(userDto: UserDto?) {
        coEvery { mockLocalUserDataStore.getUser() } returns userDto
    }

    private fun mockDeleteSessionReturns(value: Boolean) {
        coEvery { mockRemoteAuthDataSource.deleteSession(SESSION_ID) } returns value
    }

    private fun mockDeleteSessionId() {
        coEvery { mockLocalSessionDataStore.deleteSessionId() } returns Unit
    }

    private fun verifyGetRequestToken() = coVerify { mockRemoteAuthDataSource.getRequestToken() }

    private fun verifyValidateCredentialWithToken(username: String, password: String) =
        coVerify {
            mockRemoteAuthDataSource.validateCredentialWithToken(username, password, REQUEST_TOKEN)
        }

    private fun verifyCreateSession() =
        coVerify { mockRemoteAuthDataSource.createSession(VALIDATED_TOKEN) }

    private fun verifyGetUserDetails() =
        coVerify { mockRemoteAuthDataSource.getUserDetails(SESSION_ID) }

    private fun verifySaveSessionId() =
        coVerify { mockLocalSessionDataStore.saveSessionId(SESSION_ID) }

    private fun verifySaveUser(userDto: UserDto) = coVerify {
        mockLocalUserDataStore.saveUser(userDto.id, userDto.userName, userDto.imageUrl.orEmpty())
    }

    private fun verifyGetSessionId() = coVerify { mockLocalSessionDataStore.getSessionId() }

    private fun verifyGetUser() = coVerify { mockLocalUserDataStore.getUser() }

    private fun verifyDeleteSession() =
        coVerify { mockRemoteAuthDataSource.deleteSession(SESSION_ID) }

    private fun verifyDeleteSessionId() = coVerify { mockLocalSessionDataStore.deleteSessionId() }

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