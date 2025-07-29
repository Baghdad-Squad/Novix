package com.baghdad.remoteDataSource

import com.baghdad.remoteDataSource.apiService.AuthenticationApiService
import com.baghdad.remoteDataSource.request.RequestTokenBody
import com.baghdad.remoteDataSource.response.RequestTokenResponse
import com.baghdad.remoteDataSource.response.SessionResponse
import com.baghdad.remoteDataSource.response.user.UserResponse
import com.baghdad.repository.logger.Logger
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import retrofit2.Response

class RemoteAuthenticationImplTest {

    private lateinit var apiService: AuthenticationApiService
    private lateinit var logger: Logger
    private lateinit var dataSource: RemoteAuthenticationImpl

    @BeforeEach
    fun setUp() {
        apiService = mockk(relaxed = true)
        logger = mockk(relaxed = true)
        dataSource = RemoteAuthenticationImpl(apiService, logger)
    }

    @Test
    fun `getRequestToken should return token from API`() = runTest {
        // Given
        val expectedToken = "reqToken123"
        coEvery { apiService.getRequestToken() } returns Response.success(
            RequestTokenResponse(
                requestToken = expectedToken,
                expireAt = "2025-07-27",
                success = true
            )

        )

        // When
        val result = dataSource.getRequestToken()

        // Then
        assertThat(result).isEqualTo(expectedToken)
    }

    @Test
    fun `validateCredentialWithToken should return validated request token`() = runTest {
        // Given
        val expectedToken = "validatedToken"
        val username = "user"
        val password = "pass"
        val requestToken = "reqToken"
        coEvery {
            apiService.validateCredential(any())
        } returns Response.success(
            RequestTokenResponse(
                requestToken = expectedToken,
                expireAt = "2025-07-27",
                success = true
            )
        )

        // When
        val result = dataSource.validateCredentialWithToken(username, password, requestToken)

        // Then
        assertThat(result).isEqualTo(expectedToken)
    }

    @Test
    fun `createSession should return session id`() = runTest {
        // Given
        val expectedSessionId = "session123"
        coEvery {
            apiService.createSession(RequestTokenBody(requestToken = "reqToken"))
        } returns Response.success(
            SessionResponse(
                sessionId = expectedSessionId,
                success = true
            )
        )

        // When
        val result = dataSource.createSession("reqToken")

        // Then
        assertThat(result).isEqualTo(expectedSessionId)
    }

    @Test
    fun `getUserDetails should return user DTO`() = runTest {
        // Given
        val sessionId = "session123"
        val userId = 99L
        coEvery {
            apiService.getUserDetails(sessionId)
        } returns Response.success(
            UserResponse(id = userId, userName = "fara7", imageUrl = null)
        )

        // When
        val result = dataSource.getUserDetails(sessionId)

        // Then
        assertThat(result.id).isEqualTo(userId)
        assertThat(result.userName).isEqualTo("fara7")
    }

    @Test
    fun `deleteSession should return true if success is true`() = runTest {
        // Given
        val sessionId = "session123"
        coEvery {
            apiService.deleteSession(sessionId)
        } returns Response.success(
            SessionResponse(
                sessionId = "7",
                success = true
            )
        )

        // When
        val result = dataSource.deleteSession(sessionId)

        // Then
        assertThat(result).isTrue()
    }
}
