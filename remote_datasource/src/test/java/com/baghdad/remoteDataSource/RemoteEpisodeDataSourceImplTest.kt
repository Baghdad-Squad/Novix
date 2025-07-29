package com.baghdad.remoteDataSource


import com.baghdad.remoteDataSource.apiService.EpisodeApiService
import com.baghdad.remoteDataSource.response.CastMembersResponse
import com.baghdad.remoteDataSource.response.episode.EpisodeDetailsResponse
import com.baghdad.remoteDataSource.response.episode.EpisodeImageResponse
import com.baghdad.remoteDataSource.response.episode.EpisodeVideosResponse
import com.baghdad.repository.logger.Logger
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import retrofit2.Response


class RemoteEpisodeDataSourceImplTest {

    private lateinit var dataSource: RemoteEpisodeDataSourceImpl
    private lateinit var apiService: EpisodeApiService
    private lateinit var logger: Logger

    @BeforeEach
    fun setUp() {
        apiService = mockk(relaxed = true)
        logger = mockk(relaxed = true)
        dataSource = RemoteEpisodeDataSourceImpl(apiService, logger)
    }

    @Test
    fun `getEpisodeDetails should return episode details when API call succeeds`() = runTest {
        // Given
        val tvId = 1L
        val seasonNumber = 1
        val episodeNumber = 1
        val response = EpisodeDetailsResponse(
            id = 123,
            name = "Test Episode",
            overview = "Test overview",

            )
        coEvery {
            apiService.getEpisodeDetails(tvId, seasonNumber, episodeNumber)
        } returns Response.success(response)

        // When
        val result = dataSource.getEpisodeDetails(tvId, seasonNumber, episodeNumber)

        // Then
        assertThat(result.id).isEqualTo(123L)
        assertThat(result.title).isEqualTo("Test Episode")
        assertThat(result.overview).isEqualTo("Test overview")
    }

    @Test
    fun `getEpisodeCastMembers should return empty list when API returns null cast`() = runTest {
        // Given
        val tvId = 1L
        val seasonNumber = 1
        val episodeNumber = 1
        coEvery {
            apiService.getEpisodeCastMembers(tvId, seasonNumber, episodeNumber)
        } returns Response.success(CastMembersResponse(cast = null))

        // When
        val result = dataSource.getEpisodeCastMembers(tvId, seasonNumber, episodeNumber)

        // Then
        assertThat(result).isEmpty()
    }

    @Test
    fun `getEpisodeImages should return formatted image URLs`() = runTest {
        // Given
        val tvId = 1L
        val seasonNumber = 1
        val episodeNumber = 1
        val response = EpisodeImageResponse(
            stills = listOf(
                EpisodeImageResponse.Still(filePath = "/image1.jpg"),
                EpisodeImageResponse.Still(filePath = "/image2.jpg")
            )
        )
        coEvery {
            apiService.getEpisodeImages(tvId, seasonNumber, episodeNumber)
        } returns Response.success(response)

        // When
        val result = dataSource.getEpisodeImages(tvId, seasonNumber, episodeNumber)

        // Then
        assertThat(result).hasSize(2)
        assertThat(result[0]).isEqualTo("https://image.tmdb.org/t/p/w500/image1.jpg")
        assertThat(result[1]).isEqualTo("https://image.tmdb.org/t/p/w500/image2.jpg")
    }

    @Test
    fun `getEpisodeImages should return empty list when no stills available`() = runTest {
        // Given
        val tvId = 1L
        val seasonNumber = 1
        val episodeNumber = 1
        val response = EpisodeImageResponse(stills = emptyList())
        coEvery {
            apiService.getEpisodeImages(tvId, seasonNumber, episodeNumber)
        } returns Response.success(response)

        // When
        val result = dataSource.getEpisodeImages(tvId, seasonNumber, episodeNumber)

        // Then
        assertThat(result).isEmpty()
    }


    @Test
    fun `getEpisodeDetails should handle minimal data response`() = runTest {
        // Given
        val tvId = 1L
        val seasonNumber = 1
        val episodeNumber = 1
        val response = EpisodeDetailsResponse(
            id = 123,
            name = null,
            overview = null,
        )
        coEvery {
            apiService.getEpisodeDetails(tvId, seasonNumber, episodeNumber)
        } returns Response.success(response)

        // When
        val result = dataSource.getEpisodeDetails(tvId, seasonNumber, episodeNumber)

        // Then
        assertThat(result.id).isEqualTo(123L)
        assertThat(result.title).isEmpty()
        assertThat(result.overview).isEmpty()
    }

    @Test
    fun `getEpisodeTrailer should return empty string when no valid trailer found`() = runTest {
        // Given
        val tvId = 1L
        val seasonNumber = 1
        val episodeNumber = 1
        val response = EpisodeVideosResponse(results = emptyList())
        coEvery {
            apiService.getEpisodeTrailer(tvId, seasonNumber, episodeNumber)
        } returns Response.success(response)

        // When
        val result = dataSource.getEpisodeTrailer(tvId, seasonNumber, episodeNumber)

        // Then
        assertThat(result).isEmpty()
    }
}