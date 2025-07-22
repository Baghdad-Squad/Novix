package com.baghdad.repository

import com.baghdad.domain.repository.AuthenticationRepository

class AuthenticationRepositoryImpl : AuthenticationRepository {
    override suspend fun login(userName: String, password: String): String {
        return "--"
    }
}