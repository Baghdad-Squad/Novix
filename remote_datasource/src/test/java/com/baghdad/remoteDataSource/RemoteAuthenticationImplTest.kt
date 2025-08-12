package com.baghdad.remoteDataSource

import com.baghdad.remoteDataSource.apiService.AuthenticationApiService
import com.baghdad.remoteDataSource.request.CredentialDataBody
import com.baghdad.remoteDataSource.request.RequestTokenBody
import com.baghdad.remoteDataSource.response.session.SessionResponse
import com.baghdad.remoteDataSource.response.token.RequestTokenResponse
import com.baghdad.remoteDataSource.response.user.UserResponse
import com.baghdad.repository.logger.Logger
import com.baghdad.repository.model.UserDto
import com.google.common.truth.Truth.assertThat
import io.mockk.*
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import retrofit2.Response

class RemoteAuthenticationImplTest {

    private val authenticationApiService = mockk<AuthenticationApiService>()
    private val logger = mockk<Logger>(relaxed = true)
    private val dataSource = RemoteAuthenticationImpl(authenticationApiService, logger)

    @Test
    fun `should return request token when getRequestToken is called successfully`() = runTest {
        val successResponse = Response.success(requestTokenResponse)
        coEvery { authenticationApiService.getRequestToken() } returns successResponse

        val result = dataSource.getRequestToken()

        assertThat(result).isEqualTo(REQUEST_TOKEN)
        coVerify(exactly = 1) { authenticationApiService.getRequestToken() }
    }

    @Test
    fun `should return empty token when getRequestToken response has null token`() = runTest {
        val successResponse = Response.success(requestTokenResponseWithNulls)
        coEvery { authenticationApiService.getRequestToken() } returns successResponse

        val result = dataSource.getRequestToken()

        assertThat(result).isEqualTo("")
    }

    @Test
    fun `should return validated token when validateCredentialWithToken is called successfully`() = runTest {
        val successResponse = Response.success(requestTokenResponse)
        coEvery { authenticationApiService.validateCredential(credentialDataBody) } returns successResponse

        val result = dataSource.validateCredentialWithToken(USERNAME, PASSWORD, REQUEST_TOKEN)

        assertThat(result).isEqualTo(REQUEST_TOKEN)
        coVerify(exactly = 1) { authenticationApiService.validateCredential(credentialDataBody) }
    }

    @Test
    fun `should return empty token when validateCredentialWithToken response has null token`() = runTest {
        val successResponse = Response.success(requestTokenResponseWithNulls)
        coEvery { authenticationApiService.validateCredential(credentialDataBody) } returns successResponse

        val result = dataSource.validateCredentialWithToken(USERNAME, PASSWORD, REQUEST_TOKEN)

        assertThat(result).isEqualTo("")
    }

    @Test
    fun `should create correct credential body when validateCredentialWithToken is called`() = runTest {
        val successResponse = Response.success(requestTokenResponse)
        val capturedBody = slot<CredentialDataBody>()
        coEvery { authenticationApiService.validateCredential(capture(capturedBody)) } returns successResponse

        dataSource.validateCredentialWithToken(USERNAME, PASSWORD, REQUEST_TOKEN)

        assertThat(capturedBody.captured.userName).isEqualTo(USERNAME)
        assertThat(capturedBody.captured.password).isEqualTo(PASSWORD)
        assertThat(capturedBody.captured.requestToken).isEqualTo(REQUEST_TOKEN)
    }

    @Test
    fun `should return session id when createSession is called successfully`() = runTest {
        val successResponse = Response.success(sessionResponse)
        coEvery { authenticationApiService.createSession(requestTokenBody) } returns successResponse

        val result = dataSource.createSession(REQUEST_TOKEN)

        assertThat(result).isEqualTo(SESSION_ID)
        coVerify(exactly = 1) { authenticationApiService.createSession(requestTokenBody) }
    }

    @Test
    fun `should return empty session id when createSession response has null session`() = runTest {
        val successResponse = Response.success(sessionResponseWithNulls)
        coEvery { authenticationApiService.createSession(requestTokenBody) } returns successResponse

        val result = dataSource.createSession(REQUEST_TOKEN)

        assertThat(result).isEqualTo("")
    }

    @Test
    fun `should create correct request token body when createSession is called`() = runTest {
        val successResponse = Response.success(sessionResponse)
        val capturedBody = slot<RequestTokenBody>()
        coEvery { authenticationApiService.createSession(capture(capturedBody)) } returns successResponse

        dataSource.createSession(REQUEST_TOKEN)

        assertThat(capturedBody.captured.requestToken).isEqualTo(REQUEST_TOKEN)
    }

    @Test
    fun `should return user dto when getUserDetails is called successfully`() = runTest {
        val successResponse = Response.success(userResponse)
        coEvery { authenticationApiService.getUserDetails() } returns successResponse

        val result = dataSource.getUserDetails(SESSION_ID)

        assertThat(result).isEqualTo(expectedUserDto)
        coVerify(exactly = 1) { authenticationApiService.getUserDetails() }
    }

    @Test
    fun `should return true when deleteSession is called successfully`() = runTest {
        val successResponse = Response.success(deleteSessionSuccessResponse)
        coEvery { authenticationApiService.deleteSession() } returns successResponse

        val result = dataSource.deleteSession(SESSION_ID)

        assertThat(result).isTrue()
        coVerify(exactly = 1) { authenticationApiService.deleteSession() }
    }

    @Test
    fun `should return false when deleteSession fails`() = runTest {
        val successResponse = Response.success(deleteSessionFailureResponse)
        coEvery { authenticationApiService.deleteSession() } returns successResponse

        val result = dataSource.deleteSession(SESSION_ID)

        assertThat(result).isFalse()
    }

    companion object {
        const val REQUEST_TOKEN = "abc123token"
        const val USERNAME = "testuser"
        const val PASSWORD = "testpassword"
        const val SESSION_ID = "session123"
        const val USER_ID = 456L
        const val AVATAR_PATH = "/avatar.jpg"

        val requestTokenResponse = RequestTokenResponse(
            requestToken = REQUEST_TOKEN,
            expireAt = "",
            success = true
        )

        val requestTokenResponseWithNulls = RequestTokenResponse(
            requestToken = "",
            expireAt = "",
            success = false
        )

        val sessionResponse = SessionResponse(
            sessionId = SESSION_ID,
            success = true
        )

        val sessionResponseWithNulls = SessionResponse(
            sessionId = "",
            success = false
        )

        val deleteSessionSuccessResponse = SessionResponse(
            sessionId = "",
            success = true
        )

        val deleteSessionFailureResponse = SessionResponse(
            sessionId = "",
            success = false
        )

        val userResponse = UserResponse(
            id = USER_ID,
            userName = USERNAME,
            imageUrl = UserResponse.AvatarResponse(
                tmdb = UserResponse.TmdbResponse(
                    avatarPath = AVATAR_PATH
                )
            )
        )

        val userResponseWithNulls = UserResponse(
            id = null,
            userName = null,
            imageUrl = null
        )

        val userResponseWithNullAvatar = UserResponse(
            id = USER_ID,
            userName = USERNAME,
            imageUrl = UserResponse.AvatarResponse(
                tmdb = UserResponse.TmdbResponse(
                    avatarPath = null
                )
            )
        )

        val userResponseWithNullTmdb = UserResponse(
            id = USER_ID,
            userName = USERNAME,
            imageUrl = UserResponse.AvatarResponse(
                tmdb = null
            )
        )

        val credentialDataBody = CredentialDataBody(
            password = PASSWORD,
            requestToken = REQUEST_TOKEN,
            userName = USERNAME
        )

        val requestTokenBody = RequestTokenBody(
            requestToken = REQUEST_TOKEN
        )

        val expectedUserDto = UserDto(
            id = USER_ID,
            userName = USERNAME,
            imageUrl = "https://image.tmdb.org/t/p/w500$AVATAR_PATH"
        )

        val expectedUserDtoWithDefaults = UserDto(
            id = 0L,
            userName = "",
            imageUrl = "https://image.tmdb.org/t/p/w500"
        )
    }
}