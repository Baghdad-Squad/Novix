package com.baghdad.repository.datasource.local

import com.baghdad.repository.model.UserDto

interface LocalUserDataStore {
    suspend fun saveUser(id: Long, userName: String, imageUrl: String)

    suspend fun getUser(): UserDto?

    suspend fun deleteUser()
}