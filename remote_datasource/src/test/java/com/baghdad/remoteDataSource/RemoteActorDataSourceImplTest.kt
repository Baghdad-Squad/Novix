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
import okio.IOException
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
    fun `should return formatted image URLs when fetching actor images`() = runTest {
        // Given
        val apiResponse = ActorImagesResponse(
            profiles = listOf(
                ImageResponse(filePath = "/image1.jpg"),
                ImageResponse(filePath = "/image2.jpg")
            )
        )
        coEvery { apiService.getActorImages(456L) } returns Response.success(apiResponse)

        // When
        val result = dataSource.getActorImages(456L)

        // Then
        assertThat(result.size).isEqualTo(2)
        assertThat(result[0])
            .isEqualTo("https://image.tmdb.org/t/p/w500${apiResponse.profiles?.get(0)?.filePath}")
        assertThat(result[1])
            .isEqualTo("https://image.tmdb.org/t/p/w500${apiResponse.profiles?.get(1)?.filePath}")
    }

    @Test
    fun `should return empty list when actor movies API returns null cast`() = runTest {
        // Given
        coEvery { apiService.getActorMovies(789L) } returns Response.success(
            ActorMoviesResponse(cast = null)
        )

        // When
        val result = dataSource.getActorMovies(789L)

        // Then
        assertThat(result).isEmpty()
    }

    @Test
    fun `should return empty list when actor images profiles is empty`() = runTest {
        // Given
        val apiResponse = ActorImagesResponse(profiles = emptyList())
        coEvery { apiService.getActorImages(456L) } returns Response.success(apiResponse)

        // When
        val result = dataSource.getActorImages(456L)

        // Then
        assertThat(result).isEmpty()
    }

    @Test
    fun `should return empty list when actor movies cast is empty`() = runTest {
        // Given
        coEvery { apiService.getActorMovies(789L) } returns Response.success(
            ActorMoviesResponse(cast = emptyList())
        )

        // When
        val result = dataSource.getActorMovies(789L)

        // Then
        assertThat(result).isEmpty()
    }

    @Test
    fun `should return empty list when trending actors results is empty`() = runTest {
        // Given
        val apiResponse = TrendingActorResponse(
            page = 1,
            results = emptyList(),
            totalPages = 0,
            totalResults = 0
        )
        coEvery { apiService.getTrendingActors(1) } returns Response.success(apiResponse)

        // When
        val result = dataSource.getTrendingActors(1)

        // Then
        assertThat(result.data).isEmpty()
    }

    @Test
    fun `should throw NoInternetNetworkException when fetching actor images fails`() = runTest {
        // Given
        coEvery { apiService.getActorImages(456L) } throws IOException()

        // When/Then
        assertThrows<NoInternetNetworkException> {
            dataSource.getActorImages(456L)
        }
    }

    @Test
    fun `should handle partial data when fetching actor details`() = runTest {
        // Given
        val response = ActorDetailsResponse(
            id = 123L.toInt(),
            name = null,
            biography = null,
            profilePath = null
        )
        coEvery { apiService.getActorDetails(123L) } returns Response.success(response)

        // When
        val result = dataSource.getActorDetails(123L)

        // Then
        assertThat(result.id).isEqualTo(123L)
        assertThat(result.name).isEmpty()
        assertThat(result.biography).isEmpty()
        assertThat(result.headerPictures.isEmpty())
    }

    @Test
    fun `should return mapped TV shows when fetching actor TV shows`() = runTest {
        // Given
        val response = ActorTvShowsResponse(
            cast = listOf(
                ActorTvShowDto(id = 1, name = "Show 1"),
                ActorTvShowDto(id = 2, name = "Show 2")
            )
        )
        coEvery { apiService.getActorTvShows(101L) } returns Response.success(response)

        // When
        val result = dataSource.getActorTvShows(101L)

        // Then
        assertThat(result.size).isEqualTo(2)
        assertThat(result[0].title).isEqualTo("Show 1")
        assertThat(result[1].title).isEqualTo("Show 2")
    }

    @Test
    fun `should return empty list when actor TV shows cast is null`() = runTest {
        // Given
        val response = ActorTvShowsResponse(cast = null)
        coEvery { apiService.getActorTvShows(101L) } returns Response.success(response)

        // When
        val result = dataSource.getActorTvShows(101L)

        // Then
        assertTrue(result.isEmpty())
    }
}
