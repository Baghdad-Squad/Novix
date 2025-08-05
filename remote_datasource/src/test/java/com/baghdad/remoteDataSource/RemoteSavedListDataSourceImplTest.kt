package com.baghdad.remoteDataSource

import com.baghdad.remoteDataSource.apiService.SavedListApiService
import com.baghdad.remoteDataSource.request.CreateListRequest
import com.baghdad.remoteDataSource.response.UserListDto
import com.baghdad.remoteDataSource.response.UserListsResponse
import com.baghdad.remoteDataSource.response.savedList.AddListItemResponse
import com.baghdad.remoteDataSource.response.savedList.CreateSavedListResponse
import com.baghdad.remoteDataSource.response.savedList.DeleteSavedListResponse
import com.baghdad.remoteDataSource.response.savedList.ListDetailsResponse
import com.baghdad.remoteDataSource.response.savedList.RemoveListItemResponse
import com.baghdad.repository.datasource.remote.RemoteSavedListDataSource
import com.baghdad.repository.exception.ItemCreationFailedException
import com.baghdad.repository.exception.UnknownNetworkException
import com.baghdad.repository.logger.Logger
import com.baghdad.repository.model.savedList.SavedListItemDto
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
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

    @BeforeEach
    fun setUp() {
        savedListApiService = mockk(relaxed = true)
        logger = mockk(relaxed = true)
        remoteSource = RemoteSavedListDataSourceImpl(savedListApiService, logger)
    }

    val title = "Favorites"
    val sessionId = "1"
    val listId = 22L
    val movieId = 2L
    val tvShowId = 2002L

    @Test
    fun `should createSavedList succeed when response is successful`() = runTest {
        // Given
        val response = CreateSavedListResponse(success = true)

        coEvery { savedListApiService.createSavedList(any(), any()) } returns mockk {
            every { body() } returns response
            every { isSuccessful } returns true
        }

        // When
        remoteSource.createSavedList(title, sessionId)

        // Then
        coVerify {
            savedListApiService.createSavedList(
                CreateListRequest(name = title),
                sessionId = sessionId
            )
        }
    }

    @Test
    fun `should createSavedList throw ItemCreationFailedException when response success is false`() =
        runTest {
            // Given
            val errorMessage = "Unauthorized access"
            val response = CreateSavedListResponse(success = false, statusMessage = errorMessage)

            coEvery { savedListApiService.createSavedList(any(), any()) } returns mockk {
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

            coVerify {
                savedListApiService.createSavedList(
                    CreateListRequest(name = title),
                    sessionId
                )
            }
        }


    @Test
    fun `should createSavedList throw default message when statusMessage is null`() = runTest {
        // Given
        val response = CreateSavedListResponse(success = false, statusMessage = null)

        coEvery { savedListApiService.createSavedList(any(), any()) } returns mockk {
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
    fun `should createSavedList  propagate ItemCreationFailedException when API returns failure`() =
        runTest {
            // Given
            val expectedMessage = "Access denied"
            val response = CreateSavedListResponse(success = false, statusMessage = expectedMessage)

            coEvery { savedListApiService.createSavedList(any(), any()) } returns mockk {
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

    @Test
    fun `should getSavedLists return paged results when API returns successful response`() =
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
    fun `should getSavedLists return empty list when API returns empty results`() = runTest {
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

    @Test
    fun `should return success response when adding a movie to list`() = runTest {
        // Given
        val successResponse = Response.success(AddListItemResponse(1, "Success"))

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
        val successResponse = Response.success(AddListItemResponse(1, "Success"))
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
        val errorResponse = Response.error<AddListItemResponse>(
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
        val errorResponse = Response.error<AddListItemResponse>(
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

    @Test
    fun `should remove movie form saved list when the movie removed successfully`() = runTest {
        // Given
        coEvery { savedListApiService.removeItemFromSavedList(listId, any(), sessionId) } returns
                Response.success(RemoveListItemResponse(1, "Success"))

        // When
        remoteSource.removeMovieFromSavedList(listId, movieId, sessionId)

        // Then
        coVerify { savedListApiService.removeItemFromSavedList(listId, any(), sessionId) }
    }

    @Test
    fun `should remove tv show form saved list when the tv show removed successfully`() = runTest {
        // Given
        coEvery { savedListApiService.removeItemFromSavedList(listId, any(), sessionId) } returns
                Response.success(RemoveListItemResponse(1, "Success"))

        // When
        remoteSource.removeTvShowFromSavedList(listId, tvShowId, sessionId)

        // Then
        coVerify { savedListApiService.removeItemFromSavedList(listId, any(), sessionId) }
    }

    @Test
    fun `getSavedListDetails should return valid DTO when API returns valid response`() =
        runTest {
            // Given
            coEvery {
                savedListApiService.getListDetails(LIST_ID, page)
            } returns Response.success(VALID_LIST_DETAILS_RESPONSE)

            // When
            val result = remoteSource.getSavedListDetails(LIST_ID, page, pageSize)

            // Then
            assertThat(result.savedList.id).isEqualTo(123L)
            assertThat(result.savedList.name).isEqualTo("My List")
            assertThat(result.savedList.itemCount).isEqualTo(2)

            assertThat(result.pagedItems.data).hasSize(2)

            with(result.pagedItems.data[0]) {
                assertThat(id).isEqualTo(1L)
                assertThat(type).isEqualTo(SavedListItemDto.Type.MOVIE)
                assertThat(title).isEqualTo("Oppenheimer")
                assertThat(posterUrl).isEqualTo("/path1.jpg")
            }

            with(result.pagedItems.data[1]) {
                assertThat(id).isEqualTo(2L)
                assertThat(type).isEqualTo(SavedListItemDto.Type.TV_SHOW)
                assertThat(title).isEqualTo("Breaking Bad")
                assertThat(posterUrl).isEqualTo("/path2.jpg")
            }

            coVerify(exactly = 1) { savedListApiService.getListDetails(LIST_ID, page) }
        }

    @Test
    fun `should getSavedListDetails return default values and filter out invalid items`() =
        runTest {
            // Given
            coEvery {
                savedListApiService.getListDetails(LIST_ID, page)
            } returns Response.success(INVALID_LIST_DETAILS_RESPONSE)

            // When
            val result = remoteSource.getSavedListDetails(LIST_ID, page, pageSize)

            // Then
            assertThat(result.savedList.id).isEqualTo(-1L)
            assertThat(result.savedList.name).isEqualTo("")
            assertThat(result.savedList.itemCount).isEqualTo(0)

            assertThat(result.pagedItems.data).isEmpty()

            coVerify(exactly = 1) { savedListApiService.getListDetails(LIST_ID, page) }
        }

    @Test
    fun `should getSavedListDetails throw when API returns error`() =
        runTest {
            // Given
            val errorBody = "Unknown Server Error".toResponseBody(null)
            coEvery {
                savedListApiService.getListDetails(LIST_ID, page)
            } returns Response.error(999, errorBody)

            // When
            val thrown =
                runCatching {
                    remoteSource.getSavedListDetails(
                        LIST_ID,
                        page,
                        pageSize,
                    )
                }.exceptionOrNull()

            // Then
            assertThat(thrown).isInstanceOf(UnknownNetworkException::class.java)
            coVerify(exactly = 1) { savedListApiService.getListDetails(LIST_ID, page) }
        }

    @Test
    fun `should call deleteSavedListById API with its parameters`() =
        runTest {
            // Given
            val mockResponse: Response<DeleteSavedListResponse> =
                Response.success(DeleteSavedListResponse(1))

            coEvery {
                savedListApiService.deleteSavedListById(
                    listId,
                    sessionId
                )
            } returns mockResponse

            // When
            val result = savedListApiService.deleteSavedListById(listId, sessionId)

            // Then
            coVerify(exactly = 1) { savedListApiService.deleteSavedListById(listId, sessionId) }
            assertThat(result.isSuccessful).isTrue()
        }

    @Test
    fun `should delegate deleteSavedListById to Retrofit service`() =
        runTest {
            // Given
            val mockResponse: Response<DeleteSavedListResponse> =
                Response.success(DeleteSavedListResponse(1))
            coEvery {
                savedListApiService.deleteSavedListById(
                    listId,
                    sessionId
                )
            } returns mockResponse

            // When
            remoteSource.deleteSavedListById(listId, sessionId)

            // Then
            coVerify { savedListApiService.deleteSavedListById(listId, sessionId) }
        }

    companion object {
        private const val page = 1
        private const val pageSize = 20
        private const val accountId = 12345L

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

        private const val LIST_ID = 123L

        private val VALID_MOVIE_ITEM =
            ListDetailsResponse.Item(
                id = 1L,
                mediaType = "movie",
                title = "Oppenheimer",
                originalTitle = null,
                posterPath = "/path1.jpg",
            )

        private val VALID_TV_ITEM =
            ListDetailsResponse.Item(
                id = 2L,
                mediaType = "tv",
                title = null,
                originalTitle = "Breaking Bad",
                posterPath = "/path2.jpg",
            )

        private val VALID_LIST_DETAILS_RESPONSE =
            ListDetailsResponse(
                id = 123L,
                name = "My List",
                itemCount = 2,
                items =
                    listOf(
                        VALID_MOVIE_ITEM,
                        VALID_TV_ITEM,
                    ),
            )

        private val INVALID_LIST_DETAILS_RESPONSE =
            ListDetailsResponse(
                id = null,
                name = null,
                itemCount = null,
                items =
                    listOf(
                        null,
                        ListDetailsResponse.Item(
                            id = null,
                            mediaType = "movie",
                            title = "Title",
                            originalTitle = null,
                            posterPath = null,
                        ),
                        ListDetailsResponse.Item(
                            id = 3L,
                            mediaType = "invalid",
                            title = "Title",
                            originalTitle = null,
                            posterPath = null,
                        ),
                        ListDetailsResponse.Item(
                            id = 4L,
                            mediaType = "tv",
                            title = null,
                            originalTitle = null,
                            posterPath = null
                        )
                    )
            )
    }
}
