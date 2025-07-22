package com.baghdad.local_datasource

import com.baghdad.local_datasource.roomDB.dao.LoginDao
import com.baghdad.local_datasource.roomDB.entity.toDto
import com.baghdad.local_datasource.roomDB.entity.toEntity
import com.baghdad.local_datasource.roomDB.errorHandler.executeWithErrorHandling
import com.baghdad.repository.datasource.local.LocalLoginDataSource
import com.baghdad.repository.logger.Logger
import com.baghdad.repository.model.LoginDto

class LocalLoginDataSourceImpl(
    private val loginDao: LoginDao,
    private val logger: Logger
) : LocalLoginDataSource {
    override suspend fun addLogin(login: LoginDto) =
        executeWithErrorHandling(logger = logger) {
            loginDao.upsertLogin(login.toEntity())
        }

    override suspend fun getLoginByUserId(userId: Long): LoginDto =
        executeWithErrorHandling(logger = logger) {
            loginDao.getLoginByUserId(userId).toDto()
        }

    override suspend fun getLoginBySessionId(sessionId: String): LoginDto =
        executeWithErrorHandling(logger = logger) {
            loginDao.getLoginBySessionId(sessionId).toDto()
        }

    override suspend fun getLoginByUsernameAndPassword(
        username: String,
        password: String
    ): LoginDto =
        executeWithErrorHandling(logger = logger) {
            loginDao.getLoginByUsernameAndPassword(username, password).toDto()
        }

    override suspend fun saveLogin(login: LoginDto) =
        executeWithErrorHandling(logger = logger) {
            loginDao.upsertLogin(login.toEntity())
        }

    override suspend fun deleteLoginByUserId(userId: Long) =
        executeWithErrorHandling(logger = logger) {
            loginDao.deleteLoginByUserId(userId)
        }

}