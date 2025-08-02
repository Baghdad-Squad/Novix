package com.baghdad.remoteDataSource.respons.movie

import com.baghdad.remoteDataSource.response.movie.TrendingMovieResponse
import com.google.common.truth.Truth
import org.junit.jupiter.api.Test

class TrendingMovieResponseTest {

    @Test
    fun `should create TrendingMovieResponse with full data when all fields are provided`() {
        // Given
        val expectedResults = listOf(
            TrendingMovieResponse.Result(
                id = 101L,
                title = "Inception",
                posterPath = "/poster1.jpg",
                backdropPath = "/backdrop1.jpg",
                overview = "A thief who steals corporate secrets...",
                releaseDate = "2010-07-16",
                voteAverage = 8.8,
                voteCount = 20000,
                popularity = 90.5,
                genreIds = listOf(28, 12, 878)
            ),
            TrendingMovieResponse.Result(
                id = 202L,
                title = "Interstellar",
                posterPath = "/poster2.jpg",
                backdropPath = "/backdrop2.jpg",
                overview = "A team of explorers travel through a wormhole...",
                releaseDate = "2014-11-07",
                voteAverage = 8.6,
                voteCount = 15000,
                popularity = 85.3,
                genreIds = listOf(12, 18)
            )
        )

        // When
        val response = TrendingMovieResponse(
            page = 1,
            results = expectedResults,
            totalPages = 5,
            totalResults = 100
        )

        // Then
        Truth.assertThat(response.page).isEqualTo(1)
        Truth.assertThat(response.totalPages).isEqualTo(5)
        Truth.assertThat(response.totalResults).isEqualTo(100)
        Truth.assertThat(response.results).hasSize(2)

        val first = response.results!![0]
        Truth.assertThat(first.id).isEqualTo(101L)
        Truth.assertThat(first.title).isEqualTo("Inception")
        Truth.assertThat(first.posterPath).isEqualTo("/poster1.jpg")
        Truth.assertThat(first.backdropPath).isEqualTo("/backdrop1.jpg")
        Truth.assertThat(first.overview).isEqualTo("A thief who steals corporate secrets...")
        Truth.assertThat(first.releaseDate).isEqualTo("2010-07-16")
        Truth.assertThat(first.voteAverage).isEqualTo(8.8)
        Truth.assertThat(first.voteCount).isEqualTo(20000)
        Truth.assertThat(first.popularity).isEqualTo(90.5)
        Truth.assertThat(first.genreIds).containsExactly(28, 12, 878).inOrder()
    }

    @Test
    fun `should create TrendingMovieResponse with default values when no fields are provided`() {
        // Given & When
        val response = TrendingMovieResponse()

        // Then
        Truth.assertThat(response.page).isNull()
        Truth.assertThat(response.results).isNull()
        Truth.assertThat(response.totalPages).isNull()
        Truth.assertThat(response.totalResults).isNull()
    }

    @Test
    fun `should create Result with default values when no fields are provided`() {
        // Given & When
        val result = TrendingMovieResponse.Result()

        // Then
        Truth.assertThat(result.id).isNull()
        Truth.assertThat(result.title).isNull()
        Truth.assertThat(result.posterPath).isNull()
        Truth.assertThat(result.backdropPath).isNull()
        Truth.assertThat(result.overview).isNull()
        Truth.assertThat(result.releaseDate).isNull()
        Truth.assertThat(result.voteAverage).isNull()
        Truth.assertThat(result.voteCount).isNull()
        Truth.assertThat(result.popularity).isNull()
        Truth.assertThat(result.genreIds).isNull()
    }

    @Test
    fun `should create TrendingMovieResponse with empty results list when results provided as empty`() {
        // Given
        val response = TrendingMovieResponse(
            page = 2,
            results = emptyList(),
            totalPages = 3,
            totalResults = 30
        )

        // Then
        Truth.assertThat(response.page).isEqualTo(2)
        Truth.assertThat(response.results).isEmpty()
        Truth.assertThat(response.totalPages).isEqualTo(3)
        Truth.assertThat(response.totalResults).isEqualTo(30)
    }

    @Test
    fun `should create Result with partial data when some fields are provided`() {
        // When
        val result = TrendingMovieResponse.Result(
            title = "Avatar",
            voteAverage = 7.9
        )

        // Then
        Truth.assertThat(result.id).isNull()
        Truth.assertThat(result.title).isEqualTo("Avatar")
        Truth.assertThat(result.voteAverage).isEqualTo(7.9)
        Truth.assertThat(result.posterPath).isNull()
        Truth.assertThat(result.genreIds).isNull()
    }

    @Test
    fun `should create Result with multiple genreIds`() {
        // Given
        val result = TrendingMovieResponse.Result(
            id = 300L,
            title = "Genre Rich Movie",
            genreIds = listOf(14, 16, 18, 20)
        )

        // Then
        Truth.assertThat(result.id).isEqualTo(300L)
        Truth.assertThat(result.title).isEqualTo("Genre Rich Movie")
        Truth.assertThat(result.genreIds).containsExactly(14, 16, 18, 20).inOrder()
    }
}