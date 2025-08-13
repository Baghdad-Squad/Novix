package com.baghdad.repository.datasource.local

import com.baghdad.repository.model.UserDto

interface UserDataSource {
    suspend fun deleteUser()
    suspend fun getUser(): UserDto?
    suspend fun saveUser(
        id: Long,
        userName: String,
        imageUrl: String
    )
}