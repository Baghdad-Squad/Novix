package com.baghdad.local_datasource

import org.junit.Assert.*
import androidx.datastore.core.DataStore
import com.baghdad.local_datasource.dataStore.user.LocalUserDataStoreImpl
import com.baghdad.repository.logger.Logger
import com.example.application.proto.User
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@ExperimentalCoroutinesApi
class LocalUserDataStoreImplTest {

    private lateinit var dataStore: DataStore<User>
    private lateinit var logger: Logger
    private lateinit var userDataStore: LocalUserDataStoreImpl

    @BeforeEach
    fun setUp() {
        dataStore = mockk(relaxed = true)
        logger = mockk(relaxed = true)
        userDataStore = LocalUserDataStoreImpl(dataStore, logger)
    }

    @Test
    fun `saveUser should update dataStore with provided values`() = runTest {
        val id = 1L
        val userName = "testUser"
        val imageUrl = "http://image.com/avatar.png"

        coEvery { dataStore.updateData(any()) } answers {
            val userBuilder = User.newBuilder()
                .setId(id)
                .setUserName(userName)
                .setImageUrl(imageUrl)
            userBuilder.build()
        }

        userDataStore.saveUser(id, userName, imageUrl)

        coVerify { dataStore.updateData(any()) }
    }

    @Test
    fun `getUser should return null if default instance`() = runTest {
        coEvery { dataStore.data } returns flowOf(User.getDefaultInstance())

        val result = userDataStore.getUser()

        assertNull(result)
    }

    @Test
    fun `deleteUser should reset user to default`() = runTest {
        coEvery { dataStore.updateData(any()) } answers {
            User.getDefaultInstance()
        }

        userDataStore.deleteUser()

        coVerify { dataStore.updateData(any()) }
    }

    @Test
    fun `deleteUser should store default user instance`() = runTest {
        coEvery { dataStore.updateData(any()) } coAnswers {
            val transform: suspend (User) -> User = it.invocation.args[0] as suspend (User) -> User
            val result = transform(User.newBuilder().setId(999).setUserName("Ghost").build())

            assertEquals(User.getDefaultInstance(), result)
            result
        }

        userDataStore.deleteUser()
    }


    @Test
    fun `saveUser should store correct user data`() = runTest {
        val id = 123L
        val userName = "Alice"
        val imageUrl = "http://image.com/avatar.png"

        coEvery { dataStore.updateData(any()) } coAnswers {
            val transform: suspend (User) -> User = it.invocation.args[0] as suspend (User) -> User
            val result = transform(User.getDefaultInstance())

            assertEquals(id, result.id)
            assertEquals(userName, result.userName)
            assertEquals(imageUrl, result.imageUrl)
            result
        }

        userDataStore.saveUser(id, userName, imageUrl)
    }


}