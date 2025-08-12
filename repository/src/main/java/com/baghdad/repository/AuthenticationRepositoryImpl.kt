package com.baghdad.repository

import android.util.Log
import com.baghdad.domain.repository.AuthenticationRepository
import com.baghdad.entity.User
import com.baghdad.repository.datasource.local.LocalSessionDataSource
import com.baghdad.repository.datasource.local.LocalUserDataSource
import com.baghdad.repository.datasource.remote.RemoteAuthenticationDataSource
import com.baghdad.repository.model.toEntity
import com.baghdad.repository.util.executeLoginSafely
import com.baghdad.repository.util.executeSafely
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthenticationRepositoryImpl @Inject constructor(
    private val remoteAuthenticationDataSource: RemoteAuthenticationDataSource,
    private val localSessionDataSource: LocalSessionDataSource,
    private val localUserDataSource: LocalUserDataSource
) : AuthenticationRepository {
    override suspend fun login(userName: String, password: String) {
        return executeLoginSafely {
            val requestToken = remoteAuthenticationDataSource.getRequestToken()
            Log.d("AuthenticationRepository", "Request Token: $requestToken")
            val validatedRequestToken = remoteAuthenticationDataSource.validateCredentialWithToken(
                userName = userName,
                password = password,
                requestToken = requestToken
            )
            Log.d("AuthenticationRepository", "Validated Request Token: $validatedRequestToken")
            val sessionId = remoteAuthenticationDataSource.createSession(validatedRequestToken)
            localSessionDataSource.saveSessionId(sessionId)
            val user = remoteAuthenticationDataSource.getUserDetails(sessionId)
            localUserDataSource.saveUser(
                id = user.id,
                userName = user.userName,
                imageUrl = user.imageUrl.orEmpty()
            )
        }
    }


    override suspend fun isUserLoggedIn(): Boolean {
        return localSessionDataSource.getSessionId() != null
    }

    override suspend fun getLoggedInUser(): User? {
        return localUserDataSource.getUser()?.toEntity()
    }


    override suspend fun logOut(): Boolean {
        return executeSafely {
            val sessionId = localSessionDataSource.getSessionId()
            if (sessionId != null) {
                remoteAuthenticationDataSource.deleteSession(sessionId)
                localSessionDataSource.deleteSessionId()
                true
            } else {
                false
            }
        }


    }
}