package com.baghdad.localDatasource

import androidx.datastore.core.DataStore
import com.baghdad.repository.logger.Logger
import com.example.application.proto.User
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

@ExperimentalCoroutinesApi
class LocalUserDataSourceImplTest {
    private var dataStore: DataStore<User> = mockk()
    private var logger: Logger = mockk(relaxed = true)
    private var userDataStore: LocalUserDataSourceImpl = LocalUserDataSourceImpl(dataStore, logger)

    @Test
    fun `saveUser should update dataStore with provided values when saveUser is called`() =
        runTest {
            coEvery { dataStore.updateData(any()) } returns user

            userDataStore.saveUser(
                user.id,
                user.userName,
                user.imageUrl
            )

            coVerify(exactly = 1) { dataStore.updateData(any()) }
        }

    @Test
    fun `getUser should return null when default instance is requested in getUser`() = runTest {
        coEvery { dataStore.data } returns flowOf(User.getDefaultInstance())

        val result = userDataStore.getUser()

        assertThat(result).isNull()
    }

    @Test
    fun `deleteUser should reset user to default when deleteUser is called`() = runTest {
        coEvery { dataStore.updateData(any()) } answers {
            User.getDefaultInstance()
        }

        userDataStore.deleteUser()

        coVerify { dataStore.updateData(any()) }
    }

    companion object {
        private val user = User.newBuilder()
            .setId(1L)
            .setUserName("testUser")
            .setImageUrl("http://image.com/avatar.png")
            .build()
    }
}