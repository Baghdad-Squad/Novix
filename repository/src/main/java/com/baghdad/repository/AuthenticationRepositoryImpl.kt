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
        return sessionDataSource.getSessionId() != null
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
            val requestToken = getRequestToken()
            val validatedToken = validateCredentials(
                userName = userName,
                password = password,
                requestToken = requestToken
            )
            val sessionId = createSession(validatedToken = validatedToken)
            saveSession(sessionId = sessionId)
            saveUserDetails(sessionId = sessionId)
        }
    }

    private suspend fun getRequestToken(): String =
        remoteAuthenticationDataSource.getRequestToken()

    private suspend fun validateCredentials(
        userName: String,
        password: String,
        requestToken: String
    ): String = remoteAuthenticationDataSource.validateCredentialWithToken(
        userName = userName,
        password = password,
        requestToken = requestToken
    )

    private suspend fun createSession(validatedToken: String): String =
        remoteAuthenticationDataSource.createSession(requestToken = validatedToken)

    private suspend fun saveSession(sessionId: String) =
        sessionDataSource.saveSessionId(sessionId = sessionId)

    private suspend fun saveUserDetails(sessionId: String) {
        val user = remoteAuthenticationDataSource.getUserDetails(sessionId = sessionId)
        userDataSource.saveUser(
            id = user.id,
            userName = user.userName,
            imageUrl = user.imageUrl.orEmpty()
        )
    }

}