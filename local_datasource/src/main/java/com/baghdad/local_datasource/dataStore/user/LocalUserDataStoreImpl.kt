package com.baghdad.local_datasource.dataStore.user

import androidx.datastore.core.DataStore
import com.baghdad.local_datasource.dataStore.safeDataStoreCall
import com.baghdad.repository.datasource.local.LocalUserDataStore
import com.baghdad.repository.logger.Logger
import com.baghdad.repository.model.UserDto
import com.example.application.proto.User
import kotlinx.coroutines.flow.firstOrNull

class LocalUserDataStoreImpl(
    private val dataStore: DataStore<User>, private val logger: Logger
) : LocalUserDataStore {
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