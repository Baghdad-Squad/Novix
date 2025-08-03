package com.baghdad.remoteDataSource

import com.baghdad.remoteDataSource.apiService.SavedListApiService
import com.baghdad.remoteDataSource.request.CreateListRequest
import com.baghdad.remoteDataSource.response.savedList.CreateSavedListResponse
import com.baghdad.repository.datasource.remote.RemoteSavedListDataSource
import com.baghdad.repository.exception.ItemCreationFailedException
import com.baghdad.repository.logger.Logger
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class RemoteSavedListDataSourceImplTest {
    private lateinit var savedListApiService: SavedListApiService
    private lateinit var logger: Logger
    private lateinit var remoteSource: RemoteSavedListDataSource

    @BeforeEach
    fun setUp() {
        savedListApiService = mockk(relaxed = true)
        logger = mockk(relaxed = true)
        remoteSource = RemoteSavedListDataSourceImpl(savedListApiService, logger)
    }

    val title = "Favorites"
    val sessionId = "1"

    @Test
    fun `should createSavedList succeed when response is successful`() = runTest {
        // Given
        val response = CreateSavedListResponse(success = true)

        coEvery { savedListApiService.createSavedList(any()) } returns mockk {
            every { body() } returns response
            every { isSuccessful } returns true
        }

        // When
        remoteSource.createSavedList(title, sessionId)

        // Then
        coVerify { savedListApiService.createSavedList(CreateListRequest(name = title)) }
    }

    @Test
    fun `should createSavedList throw ItemCreationFailedException when response success is false`() =
        runTest {
            // Given
            val errorMessage = "Unauthorized access"
            val response = CreateSavedListResponse(success = false, statusMessage = errorMessage)

            coEvery { savedListApiService.createSavedList(any()) } returns mockk {
                every { body() } returns response
                every { isSuccessful } returns true
            }

            // When
            val resultException = runCatching {
                remoteSource.createSavedList(title, sessionId)
            }.exceptionOrNull()

            // Then
            assertThat(resultException).isNotNull()
            assertThat(resultException).isInstanceOf(ItemCreationFailedException::class.java)
            assertThat(resultException?.message).isEqualTo(errorMessage)

            coVerify { savedListApiService.createSavedList(CreateListRequest(name = title)) }
        }


    @Test
    fun `createSavedList should throw default message when statusMessage is null`() = runTest {
        // Given
        val response = CreateSavedListResponse(success = false, statusMessage = null)

        coEvery { savedListApiService.createSavedList(any()) } returns mockk {
            every { body() } returns response
            every { isSuccessful } returns true
        }

        // When
        val resultException = runCatching {
            remoteSource.createSavedList(title, sessionId)
        }.exceptionOrNull()

        // Then
        assertThat(resultException).isNotNull()
        assertThat(resultException).isInstanceOf(ItemCreationFailedException::class.java)
        assertThat(resultException?.message).isEqualTo("List creation failed")
    }

    @Test
    fun `createSavedList should propagate ItemCreationFailedException when API returns failure`() =
        runTest {
            // Given
            val expectedMessage = "Access denied"
            val response = CreateSavedListResponse(success = false, statusMessage = expectedMessage)

            coEvery { savedListApiService.createSavedList(any()) } returns mockk {
                every { body() } returns response
                every { isSuccessful } returns true
            }

            // When
            val resultException = runCatching {
                remoteSource.createSavedList(title, sessionId)
            }.exceptionOrNull()

            // Then
            assertThat(resultException).isNotNull()
            assertThat(resultException).isInstanceOf(ItemCreationFailedException::class.java)
            assertThat(resultException?.message).isEqualTo(expectedMessage)
        }

}
