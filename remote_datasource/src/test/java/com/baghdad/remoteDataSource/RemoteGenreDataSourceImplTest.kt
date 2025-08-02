package com.baghdad.remoteDataSource

import com.baghdad.remoteDataSource.apiService.GenreApiService
import com.baghdad.remoteDataSource.response.GenreItemDto
import com.baghdad.remoteDataSource.response.GenreListResponse
import com.baghdad.repository.logger.Logger
import com.baghdad.repository.model.GenreDto
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import retrofit2.Response

class RemoteGenreDataSourceImplTest {

    private lateinit var dataSource: RemoteGenreDataSourceImpl
    private lateinit var genreApiService: GenreApiService
    private lateinit var logger: Logger

    @BeforeEach
    fun setUp() {
        genreApiService = mockk(relaxed = true)
        logger = mockk(relaxed = true)
        dataSource = RemoteGenreDataSourceImpl(genreApiService, logger)
    }

    @Test
    fun `should return movie genres when API call succeeds`() = runTest {
        // Given
        val response = GenreListResponse(
            genres = listOf(
                GenreItemDto(id = 1, name = "Action"),
                GenreItemDto(id = 2, name = "Comedy")
            )
        )
        coEvery { genreApiService.getMovieGenre() } returns Response.success(response)

        // When
        val result = dataSource.getMovieGenre("en")

        // Then
        assertThat(result).hasSize(2)
        assertThat(result[0].id).isEqualTo(1)
        assertThat(result[0].name).isEqualTo("Action")
        assertThat(result[0].type).isEqualTo(GenreDto.GenreType.MOVIE)
        assertThat(result[1].id).isEqualTo(2)
        assertThat(result[1].name).isEqualTo("Comedy")
        assertThat(result[1].type).isEqualTo(GenreDto.GenreType.MOVIE)
    }

    @Test
    fun `should return TV show genres when API call succeeds`() = runTest {
        // Given
        val response = GenreListResponse(
            genres = listOf(
                GenreItemDto(id = 3, name = "Drama"),
                GenreItemDto(id = 4, name = "Sci-Fi")
            )
        )
        coEvery { genreApiService.getTvShowGenre() } returns Response.success(response)

        // When
        val result = dataSource.getTvShowGenre("en")

        // Then
        assertThat(result).hasSize(2)
        assertThat(result[0].id).isEqualTo(3)
        assertThat(result[0].name).isEqualTo("Drama")
        assertThat(result[0].type).isEqualTo(GenreDto.GenreType.TV_SHOW)
        assertThat(result[1].id).isEqualTo(4)
        assertThat(result[1].name).isEqualTo("Sci-Fi")
        assertThat(result[1].type).isEqualTo(GenreDto.GenreType.TV_SHOW)
    }

    @Test
    fun `should return empty list when movie genres API returns empty list`() = runTest {
        // Given
        val response = GenreListResponse(genres = emptyList())
        coEvery { genreApiService.getMovieGenre() } returns Response.success(response)

        // When
        val result = dataSource.getMovieGenre("en")

        // Then
        assertThat(result).isEmpty()
    }

    @Test
    fun `should return empty list when TV show genres API returns empty list`() = runTest {
        // Given
        val response = GenreListResponse(genres = emptyList())
        coEvery { genreApiService.getTvShowGenre() } returns Response.success(response)

        // When
        val result = dataSource.getTvShowGenre("en")

        // Then
        assertThat(result).isEmpty()
    }

    @Test
    fun `should include genres with empty names when fetching movie genres`() = runTest {
        // Given
        val response = GenreListResponse(
            genres = listOf(
                GenreItemDto(id = 1, name = ""),
                GenreItemDto(id = 2, name = "Valid Genre")
            )
        )
        coEvery { genreApiService.getMovieGenre() } returns Response.success(response)

        // When
        val result = dataSource.getMovieGenre("en")

        // Then
        assertThat(result).hasSize(2)
        assertThat(result[0].id).isEqualTo(1)
        assertThat(result[0].name).isEmpty()
        assertThat(result[1].id).isEqualTo(2)
        assertThat(result[1].name).isEqualTo("Valid Genre")
    }
}
