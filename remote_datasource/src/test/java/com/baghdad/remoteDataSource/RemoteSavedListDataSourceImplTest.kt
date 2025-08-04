package com.baghdad.remoteDataSource

import com.baghdad.remoteDataSource.apiService.SavedListApiService
import com.baghdad.remoteDataSource.response.savedList.ListDetailsResponse
import com.baghdad.repository.datasource.remote.RemoteSavedListDataSource
import com.baghdad.repository.exception.UnknownNetworkException
import com.baghdad.repository.logger.Logger
import com.baghdad.repository.model.savedList.SavedListItemDto
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
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

    @Test
    fun `getSavedListDetails should return valid DTO when API returns valid response`() =
        runTest {
            // Given
            coEvery {
                savedListApiService.getListDetails(LIST_ID)
            } returns Response.success(VALID_LIST_DETAILS_RESPONSE)

            // When
            val result = remoteSource.getSavedListDetails(LIST_ID)

            // Then
            assertThat(result.savedList.id).isEqualTo(123L)
            assertThat(result.savedList.name).isEqualTo("My List")
            assertThat(result.savedList.itemCount).isEqualTo(2)

            assertThat(result.listItems).hasSize(2)

            with(result.listItems[0]) {
                assertThat(id).isEqualTo(1L)
                assertThat(type).isEqualTo(SavedListItemDto.Type.MOVIE)
                assertThat(title).isEqualTo("Oppenheimer")
                assertThat(posterUrl).isEqualTo("/path1.jpg")
            }

            with(result.listItems[1]) {
                assertThat(id).isEqualTo(2L)
                assertThat(type).isEqualTo(SavedListItemDto.Type.TV_SHOW)
                assertThat(title).isEqualTo("Breaking Bad")
                assertThat(posterUrl).isEqualTo("/path2.jpg")
            }

            coVerify(exactly = 1) { savedListApiService.getListDetails(LIST_ID) }
        }

    @Test
    fun `getSavedListDetails should return default values and filter out invalid items`() =
        runTest {
            // Given
            coEvery {
                savedListApiService.getListDetails(LIST_ID)
            } returns Response.success(INVALID_LIST_DETAILS_RESPONSE)

            // When
            val result = remoteSource.getSavedListDetails(LIST_ID)

            // Then
            assertThat(result.savedList.id).isEqualTo(-1L)
            assertThat(result.savedList.name).isEqualTo("")
            assertThat(result.savedList.itemCount).isEqualTo(0)

            assertThat(result.listItems).isEmpty()

            coVerify(exactly = 1) { savedListApiService.getListDetails(LIST_ID) }
        }

    @Test
    fun `getSavedListDetails should throw when API returns error`() =
        runTest {
            // Given
            val errorBody = "Unknown Server Error".toResponseBody(null)
            coEvery {
                savedListApiService.getListDetails(LIST_ID)
            } returns Response.error(999, errorBody)

            // When
            val thrown = runCatching { remoteSource.getSavedListDetails(LIST_ID) }.exceptionOrNull()

            // Then
            assertThat(thrown).isInstanceOf(UnknownNetworkException::class.java)
            coVerify(exactly = 1) { savedListApiService.getListDetails(LIST_ID) }
        }

    companion object {
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
