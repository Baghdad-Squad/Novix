package com.baghdad.repository

import com.baghdad.domain.repository.AuthenticationRepository
import com.baghdad.repository.datasource.local.LocalSessionDataSource
import com.baghdad.repository.datasource.remote.RemoteAuthenticationDataSource
import com.baghdad.repository.util.executeLoginSafely

class AuthenticationRepositoryImpl(
    private val remoteAuthenticationDataSource: RemoteAuthenticationDataSource,
    private val localSessionDataSource: LocalSessionDataSource
) : AuthenticationRepository {
    override suspend fun login(userName: String, password: String): String {
        return executeLoginSafely {
            val requestToken = remoteAuthenticationDataSource.getRequestToken()
            val validatedRequestToken = remoteAuthenticationDataSource.validateCredentialWithToken(
                userName = userName,
                password = password,
                requestToken = requestToken
            )
            val sessionId = remoteAuthenticationDataSource.createSession(validatedRequestToken)
            remoteAuthenticationDataSource.getUserDetails(sessionId)
            localSessionDataSource.saveSessionId(sessionId)
            sessionId
        }

    }

    override suspend fun isUserLoggedIn(): Boolean {
        // TODO: need to handle error
        return localSessionDataSource.getSessionId() != null
    }

    override suspend fun logOut(): Boolean {
        val sessionId = localSessionDataSource.getSessionId()
        return if (sessionId != null) {
            remoteAuthenticationDataSource.deleteSession(sessionId)
            localSessionDataSource.clearSession()
            true
        } else {
            false
        }
    }
}