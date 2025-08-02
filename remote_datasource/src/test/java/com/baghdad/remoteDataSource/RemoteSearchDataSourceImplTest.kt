package com.baghdad.remoteDataSource

import com.baghdad.remoteDataSource.apiService.SearchApiService
import com.baghdad.remoteDataSource.response.search.ActorSearchResponse
import com.baghdad.remoteDataSource.response.search.MovieSearchResponse
import com.baghdad.remoteDataSource.response.search.TvShowSearchResponse
import com.baghdad.repository.logger.Logger
import com.baghdad.repository.model.GenreDto
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import retrofit2.Response

class RemoteSearchDataSourceImplTest {

    private lateinit var dataSource: RemoteSearchDataSourceImpl
    private lateinit var searchApiService: SearchApiService
    private lateinit var logger: Logger

    @BeforeEach
    fun setUp() {
        searchApiService = mockk(relaxed = true)
        logger = mockk(relaxed = true)
        dataSource = RemoteSearchDataSourceImpl(searchApiService, logger)
    }

    @Test
    fun `should return paged movie results when searching movies`() = runTest {
        // Given
        val query = "test"
        val page = 1
        val genres = listOf(GenreDto(1, "Action", GenreDto.GenreType.MOVIE))
        val response = MovieSearchResponse(
            page = page,
            results = listOf(
                MovieSearchResponse.Result(
                    id = 1,
                    title = "Test Movie",
                    posterPath = "/poster.jpg"
                )
            ),
            totalPages = 10
        )
        coEvery { searchApiService.searchMovies(query, page) } returns Response.success(response)

        // When
        val result = dataSource.searchMovies(query, page, genres)

        // Then
        assertThat(result.data).hasSize(1)
        assertThat(result.data[0].id).isEqualTo(1L)
    }

    @Test
    fun `should return total results count when getting movies result count`() = runTest {
        // Given
        val title = "test"
        val response = MovieSearchResponse(totalResults = 100)
        coEvery { searchApiService.getMoviesResultCount(title) } returns Response.success(response)

        // When
        val result = dataSource.getMoviesResultCount(title)

        // Then
        assertThat(result).isEqualTo(100)
    }

    @Test
    fun `should return paged tv show results when searching tv shows`() = runTest {
        // Given
        val query = "test"
        val page = 1
        val genres = listOf(GenreDto(1, "Drama", GenreDto.GenreType.TV_SHOW))
        val response = TvShowSearchResponse(
            page = page,
            results = listOf(
                TvShowSearchResponse.Result(
                    id = 1,
                    title = "Test Show",
                    posterPath = "/poster.jpg"
                )
            ),
            totalPages = 5
        )
        coEvery { searchApiService.searchTvShows(query, page) } returns Response.success(response)

        // When
        val result = dataSource.searchTvShows(query, page, genres)

        // Then
        assertThat(result.data).hasSize(1)
        assertThat(result.data[0].id).isEqualTo(1L)
    }

    @Test
    fun `should return total results count when getting tv shows result count`() = runTest {
        // Given
        val title = "test"
        val response = TvShowSearchResponse(totalResults = 50)
        coEvery { searchApiService.getTvShowsResultCount(title) } returns Response.success(response)

        // When
        val result = dataSource.getTvShowsResultCount(title)

        // Then
        assertThat(result).isEqualTo(50)
    }

    @Test
    fun `should return paged actor results when searching actors`() = runTest {
        // Given
        val query = "john"
        val page = 1
        val response = ActorSearchResponse(
            page = page,
            results = listOf(
                ActorSearchResponse.Result(
                    id = 1,
                    name = "John Doe",
                    profilePath = "/profile.jpg"
                )
            ),
            totalPages = 3
        )
        coEvery { searchApiService.searchActors(query, page) } returns Response.success(response)

        // When
        val result = dataSource.searchActors(query, page)

        // Then
        assertThat(result.data).hasSize(1)
        assertThat(result.data[0].id).isEqualTo(1L)
    }

    @Test
    fun `should return total results count when getting actors result count`() = runTest {
        // Given
        val name = "john"
        val response = ActorSearchResponse(totalResults = 25)
        coEvery { searchApiService.getActorsResultCount(name) } returns Response.success(response)

        // When
        val result = dataSource.getActorsResultCount(name)

        // Then
        assertThat(result).isEqualTo(25)
    }

    @Test
    fun `should return empty list when searching movies with no results`() = runTest {
        // Given
        val query = "none"
        val page = 1
        val genres = emptyList<GenreDto>()
        val response = MovieSearchResponse(results = emptyList())
        coEvery { searchApiService.searchMovies(query, page) } returns Response.success(response)

        // When
        val result = dataSource.searchMovies(query, page, genres)

        // Then
        assertThat(result.data).isEmpty()
    }

    @Test
    fun `should return empty list when searching tv shows with null results`() = runTest {
        // Given
        val query = "none"
        val page = 1
        val genres = emptyList<GenreDto>()
        val response = TvShowSearchResponse(results = null)
        coEvery { searchApiService.searchTvShows(query, page) } returns Response.success(response)

        // When
        val result = dataSource.searchTvShows(query, page, genres)

        // Then
        assertThat(result.data).isEmpty()
    }

    @Test
    fun `should return 0 when getting movies result count with null`() = runTest {
        // Given
        val title = "unknown"
        val response = MovieSearchResponse(totalResults = null)
        coEvery { searchApiService.getMoviesResultCount(title) } returns Response.success(response)

        // When
        val result = dataSource.getMoviesResultCount(title)

        // Then
        assertThat(result).isEqualTo(0)
    }

    @Test
    fun `should return 0 when getting tv shows result count with null`() = runTest {
        // Given
        val title = "unknown"
        val response = TvShowSearchResponse(totalResults = null)
        coEvery { searchApiService.getTvShowsResultCount(title) } returns Response.success(response)

        // When
        val result = dataSource.getTvShowsResultCount(title)

        // Then
        assertThat(result).isEqualTo(0)
    }

    @Test
    fun `should return 0 when getting actors result count with null`() = runTest {
        // Given
        val name = "unknown"
        val response = ActorSearchResponse(totalResults = null)
        coEvery { searchApiService.getActorsResultCount(name) } returns Response.success(response)

        // When
        val result = dataSource.getActorsResultCount(name)

        // Then
        assertThat(result).isEqualTo(0)
    }
}