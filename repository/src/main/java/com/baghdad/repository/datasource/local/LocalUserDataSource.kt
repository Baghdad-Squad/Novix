package com.baghdad.repository.datasource.local

import com.baghdad.repository.model.UserDto

interface LocalUserDataSource {
    suspend fun saveUser(id: Long, userName: String, imageUrl: String)
    suspend fun getUser(): UserDto?
    suspend fun deleteUser()
}