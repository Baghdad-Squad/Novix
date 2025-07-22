package com.baghdad.remoteDataSource

import com.baghdad.remoteDataSource.api.AuthenticationApi
import com.baghdad.remoteDataSource.request.CredentialDataBody
import com.baghdad.remoteDataSource.request.RequestTokenBody
import com.baghdad.remoteDataSource.util.safeRetrofitCall
import com.baghdad.repository.datasource.remote.RemoteAuthenticationDataSource
import com.baghdad.repository.logger.Logger

class RemoteAuthenticationImpl(
    private val api: AuthenticationApi,
    private val logger: Logger
) : RemoteAuthenticationDataSource {
    override suspend fun getRequestToken(): String {
        return safeRetrofitCall(
            apiCall = { api.getRequestToken().requestToken },
            logger = logger
        )
    }

    override suspend fun validateCredentialWithToken(
        userName: String,
        password: String,
        requestToken: String
    ): String {
        return safeRetrofitCall(
            apiCall = {
                api.validateCredential(
                    body = CredentialDataBody(
                        password = password,
                        requestToken = requestToken,
                        userName = userName
                    )
                ).requestToken
            },
            logger = logger
        )
    }

    override suspend fun createSession(requestToken: String): String {
        return safeRetrofitCall(
            apiCall = {
                api.createSession(
                    body = RequestTokenBody(
                        requestToken = requestToken
                    )
                ).sessionId
            },
            logger = logger
        )
    }
}