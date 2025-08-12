package com.baghdad.remoteDataSource

import com.baghdad.remoteDataSource.apiService.SavedListApiService
import com.baghdad.remoteDataSource.request.AddListItemRequest
import com.baghdad.remoteDataSource.request.CreateListRequest
import com.baghdad.remoteDataSource.request.RemoveListItemRequest
import com.baghdad.remoteDataSource.response.savedList.AddListItemResponse
import com.baghdad.remoteDataSource.response.savedList.CreateSavedListResponse
import com.baghdad.remoteDataSource.response.savedList.DeleteSavedListResponse
import com.baghdad.remoteDataSource.response.savedList.ListDetailsResponse
import com.baghdad.remoteDataSource.response.savedList.RemoveListItemResponse
import com.baghdad.remoteDataSource.response.savedList.UserListsResponse
import com.baghdad.repository.logger.Logger
import com.baghdad.repository.model.MovieDto
import com.baghdad.repository.model.PagedResultDto
import com.baghdad.repository.model.SavedListDto
import com.baghdad.repository.model.savedList.SavableMovieDto
import com.baghdad.repository.model.savedList.SavedListDetailsDto
import com.google.common.truth.Truth.assertThat
import io.mockk.*
import kotlinx.coroutines.test.runTest
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.jupiter.api.Test
import retrofit2.Response

class RemoteSavedListDataSourceImplTest {
    private val savedListApiService = mockk<SavedListApiService>()
    private val logger = mockk<Logger>(relaxed = true)
    private val dataSource = RemoteSavedListDataSourceImpl(savedListApiService, logger)

    @Test
    fun `should create saved list when createSavedList is called with valid parameters`() = runTest {
        val successResponse = Response.success(createSavedListResponse)
        coEvery { savedListApiService.createSavedList(createListRequest) } returns successResponse

        dataSource.createSavedList(LIST_TITLE, SESSION_ID)

        coVerify(exactly = 1) { savedListApiService.createSavedList(createListRequest) }
    }

    @Test
    fun `should handle null response when createSavedList is called`() = runTest {
        val successResponse = Response.success(createSavedListResponseWithNulls)
        coEvery { savedListApiService.createSavedList(createListRequest) } returns successResponse

        dataSource.createSavedList(LIST_TITLE, SESSION_ID)

        coVerify(exactly = 1) { savedListApiService.createSavedList(createListRequest) }
    }

    @Test
    fun `should return paged saved lists when getSavedLists is called`() = runTest {
        val successResponse = Response.success(userListsResponse)
        coEvery { savedListApiService.getSavedLists(ACCOUNT_ID, PAGE) } returns successResponse

        val result = dataSource.getSavedLists(PAGE, PAGE_SIZE, ACCOUNT_ID, SESSION_ID)

        assertThat(result.data).hasSize(1)
        assertThat(result.data[0]).isEqualTo(expectedSavedListDto)
        assertThat(result.nextKey).isEqualTo(2)
        assertThat(result.prevKey).isNull()
        coVerify(exactly = 1) { savedListApiService.getSavedLists(ACCOUNT_ID, PAGE) }
    }

    @Test
    fun `should filter out lists with null data when getSavedLists is called`() = runTest {
        val successResponse = Response.success(userListsResponseWithNulls)
        coEvery { savedListApiService.getSavedLists(ACCOUNT_ID, PAGE) } returns successResponse

        val result = dataSource.getSavedLists(PAGE, PAGE_SIZE, ACCOUNT_ID, SESSION_ID)

        assertThat(result.data).hasSize(2)
        assertThat(result.data[0]).isEqualTo(expectedSavedListDto)
        assertThat(result.data[1]).isEqualTo(expectedSavedListDtoWithDefaults)
    }

    @Test
    fun `should return empty list when getSavedLists receives empty results`() = runTest {
        val successResponse = Response.success(userListsResponseEmpty)
        coEvery { savedListApiService.getSavedLists(ACCOUNT_ID, PAGE) } returns successResponse

        val result = dataSource.getSavedLists(PAGE, PAGE_SIZE, ACCOUNT_ID, SESSION_ID)

        assertThat(result.data).isEmpty()
        assertThat(result.nextKey).isNull()
        assertThat(result.prevKey).isNull()
    }

    @Test
    fun `should return empty list when getSavedLists receives null results`() = runTest {
        val successResponse = Response.success(userListsResponseNullResults)
        coEvery { savedListApiService.getSavedLists(ACCOUNT_ID, PAGE) } returns successResponse

        val result = dataSource.getSavedLists(PAGE, PAGE_SIZE, ACCOUNT_ID, SESSION_ID)

        assertThat(result.data).isEmpty()
    }

    @Test
    fun `should return saved list details when getSavedListDetails is called`() = runTest {
        val successResponse = Response.success(listDetailsResponse)
        coEvery { savedListApiService.getListDetails(LIST_ID, PAGE) } returns successResponse

        val result = dataSource.getSavedListDetails(LIST_ID, PAGE, PAGE_SIZE)

        assertThat(result.savedList).isEqualTo(expectedSavedListDto)
        assertThat(result.pagedItems.data).hasSize(1)
        assertThat(result.pagedItems.data[0]).isEqualTo(expectedSavableMovieDto)
        coVerify(exactly = 1) { savedListApiService.getListDetails(LIST_ID, PAGE) }
    }

    @Test
    fun `should handle null fields in getSavedListDetails`() = runTest {
        val successResponse = Response.success(listDetailsResponseWithNulls)
        coEvery { savedListApiService.getListDetails(LIST_ID, PAGE) } returns successResponse

        val result = dataSource.getSavedListDetails(LIST_ID, PAGE, PAGE_SIZE)

        assertThat(result.savedList.id).isEqualTo(-1L)
        assertThat(result.savedList.name).isEmpty()
        assertThat(result.savedList.itemCount).isEqualTo(0)
        assertThat(result.pagedItems.data).isEmpty()
    }

    @Test
    fun `should delete saved list when deleteSavedListById is called`() = runTest {
        val successResponse = Response.success(deleteSavedListResponse)
        coEvery { savedListApiService.deleteSavedListById(LIST_ID) } returns successResponse

        dataSource.deleteSavedListById(LIST_ID, SESSION_ID)

        coVerify(exactly = 1) { savedListApiService.deleteSavedListById(LIST_ID) }
    }

    @Test
    fun `should handle movie with empty release date in getSavedListDetails`() = runTest {
        val itemWithEmptyReleaseDate = listDetailsItem.copy(releaseDate = "")
        val responseWithEmptyDate = listDetailsResponse.copy(items = listOf(itemWithEmptyReleaseDate))
        val successResponse = Response.success(responseWithEmptyDate)
        coEvery { savedListApiService.getListDetails(LIST_ID, PAGE) } returns successResponse

        val result = dataSource.getSavedListDetails(LIST_ID, PAGE, PAGE_SIZE)

        assertThat(result.pagedItems.data).hasSize(1)
        assertThat(result.pagedItems.data[0].movie.releaseDate).isEqualTo("0001-01-01")
    }

    @Test
    fun `should handle movie with null release date in getSavedListDetails`() = runTest {
        val itemWithNullReleaseDate = listDetailsItem.copy(releaseDate = null)
        val responseWithNullDate = listDetailsResponse.copy(items = listOf(itemWithNullReleaseDate))
        val successResponse = Response.success(responseWithNullDate)
        coEvery { savedListApiService.getListDetails(LIST_ID, PAGE) } returns successResponse

        val result = dataSource.getSavedListDetails(LIST_ID, PAGE, PAGE_SIZE)

        assertThat(result.pagedItems.data).hasSize(1)
        assertThat(result.pagedItems.data[0].movie.releaseDate).isEqualTo("0001-01-01")
    }

    @Test
    fun `should use originalTitle when title is null in getSavedListDetails`() = runTest {
        val itemWithNullTitle = listDetailsItem.copy(title = null, originalTitle = "Original Title")
        val responseWithNullTitle = listDetailsResponse.copy(items = listOf(itemWithNullTitle))
        val successResponse = Response.success(responseWithNullTitle)
        coEvery { savedListApiService.getListDetails(LIST_ID, PAGE) } returns successResponse

        val result = dataSource.getSavedListDetails(LIST_ID, PAGE, PAGE_SIZE)

        assertThat(result.pagedItems.data).hasSize(1)
        assertThat(result.pagedItems.data[0].movie.title).isEqualTo("Original Title")
    }

    @Test
    fun `should create request with correct mediaId when addMovieToSavedList is called with different movieId`() = runTest {
        val differentMovieId = 999L
        val expectedRequest = AddListItemRequest(mediaId = differentMovieId)
        val expectedResponse = AddListItemResponse(
            statusCode = 1,
            statusMessage = "Success"
        )
        val capturedRequest = slot<AddListItemRequest>()

        coEvery {
            savedListApiService.addMovieToSavedList(LIST_ID, capture(capturedRequest))
        } returns Response.success(expectedResponse)

        dataSource.addMovieToSavedList(LIST_ID, differentMovieId, SESSION_ID)

        assertThat(capturedRequest.captured.mediaId).isEqualTo(differentMovieId)
        assertThat(capturedRequest.captured).isEqualTo(expectedRequest)
    }

    @Test
    fun `should create request with correct mediaId when removeMovieFromSavedList is called with different movieId`() = runTest {
        val differentMovieId = 999L
        val expectedRequest = RemoveListItemRequest(mediaId = differentMovieId)
        val expectedResponse = RemoveListItemResponse(
            statusCode = 13,
            statusMessage = "The item/record was deleted successfully."
        )
        val capturedRequest = slot<RemoveListItemRequest>()

        coEvery {
            savedListApiService.removeMovieFromSavedList(LIST_ID, capture(capturedRequest))
        } returns Response.success(expectedResponse)

        dataSource.removeMovieFromSavedList(LIST_ID, differentMovieId, SESSION_ID)

        assertThat(capturedRequest.captured.mediaId).isEqualTo(differentMovieId)
        assertThat(capturedRequest.captured).isEqualTo(expectedRequest)
    }



    @Test
    fun `should call API with correct listId when addMovieToSavedList is called with different listId`() = runTest {
        val differentListId = 888L
        val successResponse = Response.success(
            AddListItemResponse(
                statusMessage = "Success",
                statusCode = 1
            )
        )

        coEvery { savedListApiService.addMovieToSavedList(differentListId, addItemRequest) } returns successResponse

        dataSource.addMovieToSavedList(differentListId, MOVIE_ID, SESSION_ID)

        coVerify(exactly = 1) { savedListApiService.addMovieToSavedList(differentListId, addItemRequest) }
    }

    companion object {
        const val LIST_ID = 123L
        const val MOVIE_ID = 456L
        const val ACCOUNT_ID = 789L
        const val PAGE = 1
        const val PAGE_SIZE = 20
        const val TOTAL_PAGES = 5
        const val TOTAL_RESULTS = 100
        const val SESSION_ID = "session123"
        const val LIST_TITLE = "My Favorite Movies"
        const val LIST_DESCRIPTION = "A collection of my favorite movies"
        const val ITEM_COUNT = 10
        const val STATUS_MESSAGE = "Successfully created list"
        const val STATUS_CODE = 201
        const val MOVIE_TITLE = "Test Movie"
        const val ORIGINAL_TITLE = "Original Test Movie"
        const val OVERVIEW = "Movie overview"
        const val RELEASE_DATE = "2023-01-01"
        const val POSTER_PATH = "/poster.jpg"
        const val VOTE_AVERAGE = 7.5

        val createListRequest = CreateListRequest(name = LIST_TITLE)
        val addItemRequest = AddListItemRequest(mediaId = MOVIE_ID)
        val removeItemRequest = RemoveListItemRequest(mediaId = MOVIE_ID)

        val createSavedListResponse = CreateSavedListResponse(
            listId = LIST_ID,
            statusMessage = STATUS_MESSAGE,
            statusCode = STATUS_CODE,
            success = true
        )

        val createSavedListResponseWithNulls = CreateSavedListResponse(
            listId = null,
            statusMessage = null,
            statusCode = null,
            success = null
        )

        val deleteSavedListResponse = DeleteSavedListResponse(
            statusMessage = "List deleted successfully",
            statusCode = 200,
        )

        val userListDto = UserListsResponse.UserListDto(
            id = LIST_ID,
            description = LIST_DESCRIPTION,
            favoriteCount = 5,
            itemCount = ITEM_COUNT,
            langCode = "en",
            listType = "movie",
            name = LIST_TITLE,
            posterPath = POSTER_PATH
        )

        val userListDtoWithNulls = UserListsResponse.UserListDto(
            id = null,
            description = null,
            favoriteCount = null,
            itemCount = null,
            langCode = null,
            listType = null,
            name = null,
            posterPath = null
        )

        val userListsResponse = UserListsResponse(
            page = PAGE,
            results = listOf(userListDto),
            totalPages = TOTAL_PAGES,
            totalResults = TOTAL_RESULTS
        )

        val userListsResponseWithNulls = UserListsResponse(
            page = PAGE,
            results = listOf(userListDto, userListDtoWithNulls),
            totalPages = TOTAL_PAGES,
            totalResults = TOTAL_RESULTS
        )

        val userListsResponseEmpty = UserListsResponse(
            page = PAGE,
            results = emptyList(),
            totalPages = 0,
            totalResults = 0
        )

        val userListsResponseNullResults = UserListsResponse(
            page = PAGE,
            results = null,
            totalPages = TOTAL_PAGES,
            totalResults = TOTAL_RESULTS
        )

        val listDetailsItem = ListDetailsResponse.Item(
            id = MOVIE_ID,
            title = MOVIE_TITLE,
            originalTitle = ORIGINAL_TITLE,
            overview = OVERVIEW,
            releaseDate = RELEASE_DATE,
            posterPath = POSTER_PATH,
            voteAverage = VOTE_AVERAGE,
            genreIds = listOf(28L, 12L),
            adult = false,
            backdropPath = "/backdrop.jpg",
            originalLanguage = "en",
            popularity = 8.5,
            video = false,
            voteCount = 1000
        )

        val listDetailsResponse = ListDetailsResponse(
            id = LIST_ID,
            name = LIST_TITLE,
            description = LIST_DESCRIPTION,
            itemCount = ITEM_COUNT,
            page = PAGE,
            totalPages = TOTAL_PAGES,
            totalResults = TOTAL_RESULTS,
            items = listOf(listDetailsItem),
            createdBy = "user123",
            favoriteCount = 5,
            posterPath = POSTER_PATH,
        )

        val listDetailsResponseWithNulls = ListDetailsResponse(
            id = null,
            name = null,
            description = null,
            itemCount = null,
            page = PAGE,
            totalPages = TOTAL_PAGES,
            totalResults = TOTAL_RESULTS,
            items = null,
            createdBy = null,
            favoriteCount = null,
            posterPath = null,
        )
        val expectedSavedListDto = SavedListDto(
            id = LIST_ID,
            name = LIST_TITLE,
            itemCount = ITEM_COUNT
        )

        val expectedSavedListDtoWithDefaults = SavedListDto(
            id = 0L,
            name = "",
            itemCount = 0
        )

        val expectedMovieDto = MovieDto(
            id = MOVIE_ID,
            title = MOVIE_TITLE,
            genres = emptyList(),
            imdbRating = VOTE_AVERAGE,
            userRating = null,
            releaseDate = RELEASE_DATE,
            overview = OVERVIEW,
            posterPictureURL = "https://image.tmdb.org/t/p/w500$POSTER_PATH",
            trailerURL = "",
            runtimeMinutes = 0
        )

        val expectedSavableMovieDto = SavableMovieDto(
            movie = expectedMovieDto,
            isSaved = false,
            listId = null
        )
    }
}