package com.baghdad.localDatasource

import androidx.datastore.core.DataStore
import com.baghdad.localDatasource.errorHandler.safeDataStoreCall
import com.baghdad.localDatasource.mapper.toDto
import com.baghdad.repository.datasource.local.UserDataSource
import com.baghdad.repository.logger.Logger
import com.baghdad.repository.model.UserDto
import com.example.application.proto.User
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

@Singleton
class UserDataSourceImpl @Inject constructor(
    @Named("user") private val dataStore: DataStore<User>, private val logger: Logger
) : UserDataSource {
    override suspend fun saveUser(
        id: Long, userName: String, imageUrl: String
    ) {
        safeDataStoreCall(
            block = {
                dataStore.updateData {
                    it.toBuilder().setId(id).setUserName(userName).setImageUrl(imageUrl).build()
                }
            }, logger = logger
        )
    }

    override suspend fun getUser(): UserDto? {
        return safeDataStoreCall(
            block = {
                dataStore.data.firstOrNull().takeIf { it != User.getDefaultInstance() }?.toDto()
            }, logger = logger
        )
    }

    override suspend fun deleteUser() {
        safeDataStoreCall(
            block = {
                dataStore.updateData {
                    User.getDefaultInstance()
                }
            }, logger = logger
        )
    }
}