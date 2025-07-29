package com.baghdad.local_datasource

import androidx.datastore.core.DataStore
import com.baghdad.local_datasource.dataStore.user.LocalUserDataStoreImpl
import com.baghdad.repository.logger.Logger
import com.example.application.proto.User
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions
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
        // Given
        val id = 1L
        val userName = "testUser"
        val imageUrl = "http://image.com/avatar.png"

        // When
        coEvery { dataStore.updateData(any()) } answers {
            val userBuilder = User.newBuilder()
                .setId(id)
                .setUserName(userName)
                .setImageUrl(imageUrl)
            userBuilder.build()
        }
        userDataStore.saveUser(id, userName, imageUrl)

        // Then
        coVerify { dataStore.updateData(any()) }
    }

    @Test
    fun `getUser should return null if default instance`() = runTest {
        // When
        coEvery { dataStore.data } returns flowOf(User.getDefaultInstance())

        val result = userDataStore.getUser()

        // Then
        Assertions.assertNull(result)
    }

    @Test
    fun `deleteUser should reset user to default`() = runTest {
        // When
        coEvery { dataStore.updateData(any()) } answers {
            User.getDefaultInstance()
        }

        userDataStore.deleteUser()

        // Then
        coVerify { dataStore.updateData(any()) }
    }

    @Test
    fun `deleteUser should store default user instance`() = runTest {
        // When
        coEvery { dataStore.updateData(any()) } coAnswers {
            val transform: suspend (User) -> User = it.invocation.args[0] as suspend (User) -> User
            val result = transform(User.newBuilder().setId(999).setUserName("Ghost").build())

            Assertions.assertEquals(User.getDefaultInstance(), result)
            result
        }

        // Then
        userDataStore.deleteUser()
    }


    @Test
    fun `saveUser should store correct user data`() = runTest {
        // Given
        val id = 123L
        val userName = "Alice"
        val imageUrl = "http://image.com/avatar.png"

        // When
        coEvery { dataStore.updateData(any()) } coAnswers {
            val transform: suspend (User) -> User = it.invocation.args[0] as suspend (User) -> User
            val result = transform(User.getDefaultInstance())

            Assertions.assertEquals(id, result.id)
            Assertions.assertEquals(userName, result.userName)
            Assertions.assertEquals(imageUrl, result.imageUrl)
            result
        }

        // Then
        userDataStore.saveUser(id, userName, imageUrl)
    }
}