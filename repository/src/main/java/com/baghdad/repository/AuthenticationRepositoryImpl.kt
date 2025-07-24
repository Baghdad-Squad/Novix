package com.baghdad.repository

import android.util.Log
import com.baghdad.domain.repository.AuthenticationRepository
import com.baghdad.entity.User
import com.baghdad.repository.datasource.local.LocalSessionDataStore
import com.baghdad.repository.datasource.local.LocalUserDataStore
import com.baghdad.repository.datasource.remote.RemoteAuthenticationDataSource
import com.baghdad.repository.model.toEntity
import com.baghdad.repository.util.executeLoginSafely

class AuthenticationRepositoryImpl(
    private val remoteAuthenticationDataSource: RemoteAuthenticationDataSource,
    private val localSessionDataStore: LocalSessionDataStore,
    private val localUserDataStore: LocalUserDataStore
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
            val user = remoteAuthenticationDataSource.getUserDetails(sessionId)
            localSessionDataStore.saveSessionId(sessionId)
            localUserDataStore.saveUser(
                id = user.id,
                userName = user.userName,
                imageUrl = user.imageUrl.orEmpty()
            )
            Log.i("login ", "user from local data store ${localUserDataStore.getUser()}")
            Log.i("login ", "session from local data store ${localSessionDataStore.getSessionId()}")
            Log.i("login", "delete user ${localUserDataStore.deleteUser()}")
            Log.i("login", "user after deletion ${localUserDataStore.getUser()}")
            sessionId
        }

    }

    override suspend fun isUserLoggedIn(): Boolean {
        return localSessionDataStore.getSessionId() != null
    }

    override suspend fun getLoggedInUser(): User? {
        return localUserDataStore.getUser()?.toEntity()
    }


    override suspend fun logOut(): Boolean {
        val sessionId = localSessionDataStore.getSessionId()
        return if (sessionId != null) {
            remoteAuthenticationDataSource.deleteSession(sessionId)
            localSessionDataStore.deleteSessionId()
            true
        } else {
            false
        }
    }
}