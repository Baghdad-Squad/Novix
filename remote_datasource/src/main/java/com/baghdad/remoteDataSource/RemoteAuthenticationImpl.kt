package com.baghdad.remoteDataSource

import com.baghdad.remoteDataSource.apiService.AuthenticationApiService
import com.baghdad.remoteDataSource.mapper.user.toDto
import com.baghdad.remoteDataSource.request.CredentialDataBody
import com.baghdad.remoteDataSource.request.RequestTokenBody
import com.baghdad.remoteDataSource.util.handleRequest
import com.baghdad.repository.datasource.remote.RemoteAuthenticationDataSource
import com.baghdad.repository.logger.Logger
import com.baghdad.repository.model.UserDto
import javax.inject.Inject

class RemoteAuthenticationImpl @Inject constructor(
    private val authenticationApiService: AuthenticationApiService,
    private val logger: Logger
) : RemoteAuthenticationDataSource {
    override suspend fun getRequestToken(): String {
        return handleRequest(
            apiCall = { authenticationApiService.getRequestToken() },
            logger = logger
        ).requestToken
    }

    override suspend fun validateCredentialWithToken(
        userName: String,
        password: String,
        requestToken: String
    ): String {
        return handleRequest(
            apiCall = {
                authenticationApiService.validateCredential(
                    body = CredentialDataBody(
                        password = password,
                        requestToken = requestToken,
                        userName = userName
                    )
                )
            },
            logger = logger
        ).requestToken
    }

    override suspend fun createSession(requestToken: String): String {
        return handleRequest(
            apiCall = {
                authenticationApiService.createSession(
                    body = RequestTokenBody(
                        requestToken = requestToken
                    )
                )
            },
            logger = logger
        ).sessionId
    }

    override suspend fun getUserDetails(sessionId: String): UserDto {
        return handleRequest(
            apiCall = {
                authenticationApiService.getUserDetails(
                    sessionId = sessionId
                )
            },
            logger = logger
        ).toDto()
    }

    override suspend fun deleteSession(sessionId: String): Boolean {
        return handleRequest(
            apiCall = {
                authenticationApiService.deleteSession(
                    sessionId = sessionId
                )
            },
            logger = logger
        ).success
    }

}