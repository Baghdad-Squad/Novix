package com.baghdad.local_datasource

import androidx.datastore.core.DataStore
import com.baghdad.local_datasource.dataStore.user.LocalUserDataStoreImpl
import com.baghdad.repository.logger.Logger
import com.example.application.proto.User
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
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
    fun `should update dataStore with provided values when saveUser is called`() = runTest {
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
    fun `should return null when default instance is requested in getUser`() = runTest {
        // When
        coEvery { dataStore.data } returns flowOf(User.getDefaultInstance())

        val result = userDataStore.getUser()

        // Then
        assertThat(result).isNull()
    }

    @Test
    fun `should reset user to default when deleteUser is called`() = runTest {
        // When
        coEvery { dataStore.updateData(any()) } answers {
            User.getDefaultInstance()
        }

        userDataStore.deleteUser()

        // Then
        coVerify { dataStore.updateData(any()) }
    }

    @Test
    fun `should store default user instance when deleteUser is called`() = runTest {
        // When
        coEvery { dataStore.updateData(any()) } coAnswers {
            val transform: suspend (User) -> User = it.invocation.args[0] as suspend (User) -> User
            val result = transform(User.newBuilder().setId(999).setUserName("Ghost").build())

            assertThat(result.id).isEqualTo(User.getDefaultInstance().id)
            result
        }

        // Then
        userDataStore.deleteUser()
    }


    @Test
    fun `should store correct user data when saveUser is called`() = runTest {
        // Given
        val id = 123L
        val userName = "Alice"
        val imageUrl = "http://image.com/avatar.png"

        // When
        coEvery { dataStore.updateData(any()) } coAnswers {
            val transform: suspend (User) -> User = it.invocation.args[0] as suspend (User) -> User
            val result = transform(User.getDefaultInstance())

            assertThat(id).isEqualTo(result.id)
            assertThat(userName).isEqualTo(result.userName)
            assertThat(imageUrl).isEqualTo(result.imageUrl)
            result
        }

        // Then
        userDataStore.saveUser(id, userName, imageUrl)
    }
}