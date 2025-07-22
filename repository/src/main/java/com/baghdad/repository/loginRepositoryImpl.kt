package com.baghdad.repository

import com.baghdad.domain.repository.LoginRepository
import com.baghdad.entity.login.Login
import com.baghdad.repository.datasource.remote.RemoteLoginDataSource
import com.baghdad.repository.mapper.toEntity
import com.baghdad.repository.util.executeSafely

class LoginRepositoryImpl(
    private val remoteLoginDataSource: RemoteLoginDataSource,
) : LoginRepository {


    override suspend fun getLoginByUserId(userId: Long): Login {
        TODO("Not yet implemented")

    }

    override suspend fun getLoginBySessionId(sessionId: Int): Login {
        return executeSafely {
            remoteLoginDataSource.getLoginByStatusCode(sessionId).toEntity()

        }

    }


    override suspend fun getLoginByUsernameAndPassword(username: String, password: String): Login {
        TODO("Not yet implemented")

    }

}
