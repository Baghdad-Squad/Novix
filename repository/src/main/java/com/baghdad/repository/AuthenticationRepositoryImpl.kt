package com.baghdad.repository

import com.baghdad.domain.repository.AuthenticationRepository
import com.baghdad.entity.user.User
import com.baghdad.repository.datasource.local.LocalSessionDataSource
import com.baghdad.repository.datasource.local.LocalUserDataSource
import com.baghdad.repository.datasource.remote.RemoteAuthenticationDataSource
import com.baghdad.repository.mapper.toEntity
import com.baghdad.repository.util.executeLoginSafely
import com.baghdad.repository.util.executeSafely
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthenticationRepositoryImpl @Inject constructor(
    private val remoteAuthenticationDataSource: RemoteAuthenticationDataSource,
    private val localSessionDataStore: LocalSessionDataSource,
    private val localUserDataSource: LocalUserDataSource
) : AuthenticationRepository {

    override suspend fun isUserLoggedIn(): Boolean {
        return localSessionDataStore.getSessionId() != null
    }

    override suspend fun getLoggedInUser(): User? {
        return localUserDataSource.getUser()?.toEntity()
    }

    override suspend fun logOut(): Boolean {
        return executeSafely {
            localSessionDataStore.getSessionId()?.let { sessionId ->
                remoteAuthenticationDataSource.deleteSession(sessionId = sessionId)
                localSessionDataStore.deleteSessionId()
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
        localSessionDataStore.saveSessionId(sessionId = sessionId)

    private suspend fun saveUserDetails(sessionId: String) {
        val user = remoteAuthenticationDataSource.getUserDetails(sessionId = sessionId)
        localUserDataSource.saveUser(
            id = user.id,
            userName = user.userName,
            imageUrl = user.imageUrl.orEmpty()
        )
    }
}