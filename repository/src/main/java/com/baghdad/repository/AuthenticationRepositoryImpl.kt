package com.baghdad.repository

import com.baghdad.domain.repository.AuthenticationRepository
import com.baghdad.entity.user.User
import com.baghdad.repository.datasource.local.SessionDataSource
import com.baghdad.repository.datasource.local.UserDataSource
import com.baghdad.repository.datasource.remote.RemoteAuthenticationDataSource
import com.baghdad.repository.mapper.toEntity
import com.baghdad.repository.util.executeLoginSafely
import com.baghdad.repository.util.executeSafely
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthenticationRepositoryImpl @Inject constructor(
    private val remoteAuthenticationDataSource: RemoteAuthenticationDataSource,
    private val sessionDataSource: SessionDataSource,
    private val userDataSource: UserDataSource
) : AuthenticationRepository {

    override suspend fun isUserLoggedIn(): Boolean {
        return executeSafely { sessionDataSource.getSessionId() != null }
    }

    override suspend fun getUserInfo(): User? {
        return userDataSource.getUser()?.toEntity()
    }

    override suspend fun logOut(): Boolean {
        return executeSafely {
            sessionDataSource.getSessionId()?.let { sessionId ->
                remoteAuthenticationDataSource.deleteSession(sessionId = sessionId)
                sessionDataSource.deleteSessionId()
                true
            } ?: false
        }
    }

    override suspend fun login(userName: String, password: String) {
        executeLoginSafely {
            val requestToken = remoteAuthenticationDataSource.getRequestToken()
            val validatedToken = remoteAuthenticationDataSource.validateCredentialWithToken(
                userName = userName,
                password = password,
                requestToken = requestToken
            )
            val sessionId =
                remoteAuthenticationDataSource.createSession(requestToken = validatedToken)
            sessionDataSource.saveSessionId(sessionId = sessionId)
            val user = remoteAuthenticationDataSource.getUserDetails(sessionId = sessionId)
            userDataSource.saveUser(
                id = user.id,
                userName = user.userName,
                imageUrl = user.imageUrl.orEmpty()
            )
        }
    }
}