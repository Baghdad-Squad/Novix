package com.baghdad.remoteDataSource

import com.baghdad.remoteDataSource.apiService.ActorApiService
import com.baghdad.remoteDataSource.response.actor.ActorDetailsResponse
import com.baghdad.remoteDataSource.response.actor.ActorImagesResponse
import com.baghdad.remoteDataSource.response.actor.ActorMoviesResponse
import com.baghdad.remoteDataSource.response.actor.ActorTvShowDto
import com.baghdad.remoteDataSource.response.actor.ActorTvShowsResponse
import com.baghdad.remoteDataSource.response.actor.ImageResponse
import com.baghdad.remoteDataSource.response.actor.TrendingActorResponse
import com.baghdad.repository.exception.NoInternetNetworkException
import com.baghdad.repository.logger.Logger
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import retrofit2.Response

class RemoteActorDataSourceImplTest {

    private lateinit var dataSource: RemoteActorDataSourceImpl
    private lateinit var apiService: ActorApiService
    private lateinit var logger: Logger

    @BeforeEach
    fun setUp() {
        apiService = mockk(relaxed = true)
        logger = mockk(relaxed = true)
        dataSource = RemoteActorDataSourceImpl(apiService, logger)
    }

    @Test
    fun `getActorImages should return formatted image URLs`() = runTest {
        // Given
        val personId = 456L
        val apiResponse = ActorImagesResponse(
            profiles = listOf(
                ImageResponse(filePath = "/image1.jpg"),
                ImageResponse(filePath = "/image2.jpg")
            )
        )
        coEvery { apiService.getActorImages(personId) } returns Response.success(apiResponse)

        // When
        val result = dataSource.getActorImages(personId)

        // Then
        assertEquals(2, result.size)
        assertEquals("https://image.tmdb.org/t/p/w500/image1.jpg", result[0])
        assertEquals("https://image.tmdb.org/t/p/w500/image2.jpg", result[1])
    }

    @Test
    fun `getActorMovies should return empty list when API returns null cast`() = runTest {
        // Given
        val personId = 789L
        coEvery { apiService.getActorMovies(personId) } returns Response.success(
            ActorMoviesResponse(
                cast = null
            )
        )

        // When
        val result = dataSource.getActorMovies(personId)

        // Then
        assertTrue(result.isEmpty())
    }

    @Test
    fun `getActorImages should return empty list when profiles is empty`() = runTest {
        // Given
        val personId = 456L
        val apiResponse = ActorImagesResponse(profiles = emptyList())
        coEvery { apiService.getActorImages(personId) } returns Response.success(apiResponse)

        // When
        val result = dataSource.getActorImages(personId)

        // Then
        assertTrue(result.isEmpty())
    }

    @Test
    fun `getActorMovies should handle empty cast list`() = runTest {
        // Given
        val personId = 789L
        coEvery { apiService.getActorMovies(personId) } returns Response.success(
            ActorMoviesResponse(cast = emptyList())
        )

        // When
        val result = dataSource.getActorMovies(personId)

        // Then
        assertTrue(result.isEmpty())
    }

    @Test
    fun `getTrendingActors should handle empty results list`() = runTest {
        // Given
        val page = 1
        val apiResponse = TrendingActorResponse(
            page = page,
            results = emptyList(),
            totalPages = 0,
            totalResults = 0
        )
        coEvery { apiService.getTrendingActors(page) } returns Response.success(apiResponse)

        // When
        val result = dataSource.getTrendingActors(page)

        // Then
        assertTrue(result.data.isEmpty())
    }

    @Test
    fun `getActorImages should throw exception on API failure`() = runTest {
        // Given
        val personId = 456L
        coEvery { apiService.getActorImages(personId) } throws _root_ide_package_.io.ktor.utils.io.errors.IOException()

        // When/Then
        assertThrows<NoInternetNetworkException> {
            dataSource.getActorImages(personId)
        }
    }

    @Test
    fun `getActorDetails should handle partial data`() = runTest {
        // Given
        val personId = 123L
        val response = ActorDetailsResponse(
            id = personId.toInt(),
            name = null,
            biography = null,
            profilePath = null
        )
        coEvery { apiService.getActorDetails(personId) } returns Response.success(response)

        // When
        val result = dataSource.getActorDetails(personId)

        // Then
        assertEquals(personId, result.id)
        assertEquals("", result.name)
        assertEquals("", result.biography)
        assertThat(result.headerPictures.isEmpty())
    }

    @Test
    fun `getActorTvShows should return mapped TV shows`() = runTest {
        // Given
        val personId = 101L
        val response = ActorTvShowsResponse(
            cast = listOf(
                ActorTvShowDto(id = 1, name = "Show 1"),
                ActorTvShowDto(id = 2, name = "Show 2")
            )
        )
        coEvery { apiService.getActorTvShows(personId) } returns Response.success(response)

        // When
        val result = dataSource.getActorTvShows(personId)

        // Then
        assertEquals(2, result.size)
        assertEquals("Show 1", result[0].title)
        assertEquals("Show 2", result[1].title)
    }

    @Test
    fun `getActorTvShows should return empty list when cast is null`() = runTest {
        // Given
        val personId = 101L
        val response = ActorTvShowsResponse(cast = null)
        coEvery { apiService.getActorTvShows(personId) } returns Response.success(response)

        // When
        val result = dataSource.getActorTvShows(personId)

        // Then
        assertTrue(result.isEmpty())
    }
}