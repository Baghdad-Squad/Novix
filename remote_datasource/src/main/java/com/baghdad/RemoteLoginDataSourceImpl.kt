package com.baghdad

import com.baghdad.repository.datasource.remote.RemoteLoginDataSource
import com.baghdad.repository.model.LoginDto

class RemoteLoginDataSourceImpl(

) : RemoteLoginDataSource {
    override suspend fun getLoginByStatusCode(statusCode: Int): LoginDto {
        TODO("Not yet implemented")
    }

    companion object {
        private const val LOGIN_STATUS_CODE = "login/{status_code}"

    }

}
// same thing to RemoteLoginDataSource