package com.baghdad.remoteDataSource

import com.baghdad.remoteDataSource.apiService.SavedListApiService
import com.baghdad.remoteDataSource.response.AddItemToSavedResponse
import com.baghdad.remoteDataSource.response.UserListDto
import com.baghdad.remoteDataSource.response.UserListsResponse
import com.baghdad.repository.datasource.remote.RemoteSavedListDataSource
import com.baghdad.repository.logger.Logger
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import retrofit2.Response

class RemoteSavedListDataSourceImplTest {
    private lateinit var savedListApiService: SavedListApiService
    private lateinit var logger: Logger
    private lateinit var remoteSource: RemoteSavedListDataSource

    val listId = 22L
    val movieId = 2L
    val tvShowId = 2002L
    val sessionId = "id_session"

    @BeforeEach
    fun setUp() {
        savedListApiService = mockk(relaxed = true)
        logger = mockk(relaxed = true)
        remoteSource = RemoteSavedListDataSourceImpl(savedListApiService, logger)
    }

    @Test
    fun `getSavedLists should return paged results when API returns successful response`() =
        runTest {
            // Given
            coEvery {
                savedListApiService.getSavedLists(accountId, sessionId, page)
            } returns Response.success(successResponse)

            // When
            val result = remoteSource.getSavedLists(page, pageSize, accountId, sessionId)

            // Then
            assertThat(result.data[0].id).isEqualTo(1L)
            assertThat(result.data[0].name).isEqualTo("My Favorites")
            assertThat(result.data[0].itemCount).isEqualTo(10)
            coVerify(exactly = 1) { savedListApiService.getSavedLists(accountId, sessionId, page) }
        }

    @Test
    fun `getSavedLists should return empty list when API returns empty results`() = runTest {
        // Given
        val response = UserListsResponse(
            page = page,
            results = emptyList(),
            totalPages = 0,
            totalResults = 0
        )

        coEvery {
            savedListApiService.getSavedLists(accountId, sessionId, page)
        } returns Response.success(response)

        // When
        val result = remoteSource.getSavedLists(page, pageSize, accountId, sessionId)

        // Then
        assertThat(result.data).isEmpty()
        assertThat(result.nextKey).isNull()
        assertThat(result.prevKey).isNull()
        coVerify(exactly = 1) { savedListApiService.getSavedLists(accountId, sessionId, page) }
    }


    companion object {
        private const val page = 1
        private const val pageSize = 20
        private const val accountId = 12345L
        private const val sessionId = "test_session_id"

        private val successResponse = UserListsResponse(
            page = page,
            results = listOf(
                UserListDto(
                    id = 1L,
                    name = "My Favorites",
                    itemCount = 10,
                    description = "My favorite movies",
                    favoriteCount = 5
                )
            ),
            totalPages = 5,
            totalResults = 100
        )
    }

    @Test
    fun `should return success response when adding a movie to list`() = runTest {
        // Given
        val successResponse = Response.success(AddItemToSavedResponse(1, "Success"))

        coEvery {
            savedListApiService.addItemToSavedList(listId, any(), sessionId)
        } returns successResponse

        // When
        remoteSource.addMovieToSavedList(listId, movieId, sessionId)

        // Then
        coVerify { savedListApiService.addItemToSavedList(listId, any(), sessionId) }
    }

    @Test
    fun `should return success response when adding a tv show to list`() = runTest {
        // Given
        val successResponse = Response.success(AddItemToSavedResponse(1, "Success"))
        coEvery {
            savedListApiService.addItemToSavedList(listId, any(), sessionId)
        } returns successResponse

        // When
        remoteSource.addTvShowToSavedList(listId, tvShowId, sessionId)

        // Then
        coVerify { savedListApiService.addItemToSavedList(listId, any(), sessionId) }
    }

    @Test
    fun `should throw exception when api returns error response`() = runTest {
        // Given
        val errorResponse = Response.error<AddItemToSavedResponse>(
            401, "Unauthorized".toResponseBody("application/json".toMediaTypeOrNull())
        )

        coEvery {
            savedListApiService.addItemToSavedList(listId, any(), sessionId)
        } returns errorResponse

        // When & Then
        assertThrows<Exception> {
            remoteSource.addMovieToSavedList(listId, movieId, sessionId)
        }

        coVerify { savedListApiService.addItemToSavedList(listId, any(), sessionId) }
    }

    @Test
    fun `should throw exception when network call fails`() = runTest {
        // Given
        val errorResponse = Response.error<AddItemToSavedResponse>(
            401, "Unauthorized".toResponseBody("application/json".toMediaTypeOrNull())
        )

        coEvery {
            savedListApiService.addItemToSavedList(listId, any(), sessionId)
        } returns errorResponse

        // When & Then
        assertThrows<Exception> {
            remoteSource.addMovieToSavedList(listId, tvShowId, sessionId)
        }

        coVerify { savedListApiService.addItemToSavedList(listId, any(), sessionId) }
    }
}
