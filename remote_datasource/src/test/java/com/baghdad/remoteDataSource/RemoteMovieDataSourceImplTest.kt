package com.baghdad.remoteDataSource

import com.baghdad.remoteDataSource.apiService.MovieApiService
import com.baghdad.remoteDataSource.response.CastMemberResponse
import com.baghdad.remoteDataSource.response.CastMembersResponse
import com.baghdad.remoteDataSource.response.MovieResult
import com.baghdad.remoteDataSource.response.SimilarMovieResponse
import com.baghdad.remoteDataSource.response.actor.ImageResponse
import com.baghdad.remoteDataSource.response.movie.Genre
import com.baghdad.remoteDataSource.response.movie.MovieDetailsResponse
import com.baghdad.remoteDataSource.response.movie.MovieImageResponse
import com.baghdad.repository.logger.Logger
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import retrofit2.Response


class RemoteMovieDataSourceImplTest {

    private lateinit var dataSource: RemoteMovieDataSourceImpl
    private lateinit var movieApiService: MovieApiService
    private lateinit var logger: Logger

    @BeforeEach
    fun setUp() {
        movieApiService = mockk(relaxed = true)
        logger = mockk(relaxed = true)
        dataSource = RemoteMovieDataSourceImpl(movieApiService, logger)
    }

    @Test
    fun `getMovieDetails should map response to MovieDto`() = runTest {
        // Given
        val movieId = 1L
        val response = MovieDetailsResponse(
            id = movieId,
            title = "Test Movie",
            genres = listOf(Genre(id = 1, name = "Action")),
            voteAverage = 8.5,
            releaseDate = "2023-01-01",
            overview = "Test overview",
            posterPath = "/poster.jpg",
            runtime = 120
        )
        coEvery { movieApiService.getMovieDetails(movieId) } returns Response.success(response)

        // When
        val result = dataSource.getMovieDetails(movieId)

        // Then
        assertThat(result.id).isEqualTo(movieId)
        assertThat(result.title).isEqualTo("Test Movie")
        assertThat(result.genres).hasSize(1)
        assertThat(result.imdbRating).isEqualTo(8.5)
        assertThat(result.releaseDate).isEqualTo("2023-01-01")
        assertThat(result.runtimeMinutes).isEqualTo(120)
    }

    @Test
    fun `getSimilarMovies should map response to MovieDto list`() = runTest {
        // Given
        val movieId = 1L
        val response = SimilarMovieResponse(
            results = listOf(
                MovieResult(
                    id = 1,
                    title = "Similar 1",
                    posterPath = "/similar1.jpg"
                )
            )
        )
        coEvery { movieApiService.getSimilarMovies(movieId) } returns Response.success(response)

        // When
        val result = dataSource.getSimilarMovies(movieId)

        // Then
        assertThat(result).hasSize(1)
        assertThat(result[0].id).isEqualTo(1L)
        assertThat(result[0].title).isEqualTo("Similar 1")
    }

    @Test
    fun `getMovieImages should return backdrop paths`() = runTest {
        // Given
        val movieId = 1L
        val response = MovieImageResponse(
            backdrops = listOf(
                ImageResponse(filePath = "/backdrop1.jpg")
            )
        )
        coEvery { movieApiService.getMovieImages(movieId) } returns Response.success(response)

        // When
        val result = dataSource.getMovieImages(movieId)

        // Then
        assertThat(result).containsExactly("/backdrop1.jpg")
    }

    @Test
    fun `getTopRatedMovies should return paged results`() = runTest {
        // Given
        val page = 1
        val response = SimilarMovieResponse(
            page = page,
            results = listOf(
                MovieResult(id = 1, title = "Top Movie")
            ),
            totalPages = 10
        )
        coEvery { movieApiService.getTopRatedMovies(page) } returns Response.success(response)

        // When
        val result = dataSource.getTopRatedMovies(page)

        // Then
        assertThat(result.data).hasSize(1)
    }

    @Test
    fun `getMoviesByGenre should return paged results`() = runTest {
        // Given
        val genreId = 1L
        val page = 1
        val response = SimilarMovieResponse(
            page = page,
            results = listOf(
                MovieResult(id = 1, title = "Action Movie")
            ),
            totalPages = 5
        )
        coEvery { movieApiService.getMoviesByGenre(genreId, page) } returns Response.success(
            response
        )

        // When
        val result = dataSource.getMoviesByGenre(genreId, page)

        // Then
        assertThat(result.data).hasSize(1)
    }

    @Test
    fun `getMovieCastMembers should return cast list`() = runTest {
        // Given
        val movieId = 1L
        val response = CastMembersResponse(
            cast = listOf(
                CastMemberResponse(
                    id = 1,
                    name = "Actor",
                    character = "Hero"
                )
            )
        )
        coEvery { movieApiService.getMovieCastMembers(movieId) } returns Response.success(response)

        // When
        val result = dataSource.getMovieCastMembers(movieId)

        // Then
        assertThat(result).hasSize(1)
        assertThat(result[0].actor.name).isEqualTo("Actor")
        assertThat(result[0].characterName).isEqualTo("Hero")
    }
}