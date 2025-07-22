package com.baghdad.repository.datasource.remote

import com.baghdad.repository.model.LoginDto


interface RemoteLoginDataSource{
    suspend fun getLoginByStatusCode(
        statusCode:Int
    ): LoginDto
}
//here i wanna ask about the remote just return StatusCode to we can use as key to login