package com.baghdad.remoteDataSource.respons.movie

import com.baghdad.remoteDataSource.response.movie.PopularMoviesResponse
import com.google.common.truth.Truth
import org.junit.jupiter.api.Test

class PopularMoviesResponseTest {

    @Test
    fun `should create PopularMoviesResponse with full data when all fields are provided`() {
        // Given
        val expectedResults = listOf(
            PopularMoviesResponse.Result(
                adult = false,
                backdropPath = "/backdrop1.jpg",
                genreIds = listOf(28L, 12L, 878L),
                id = 101L,
                originalLanguage = "en",
                originalTitle = "Inception",
                overview = "A thief who steals corporate secrets...",
                popularity = 80.5,
                posterPath = "/poster1.jpg",
                releaseDate = "2010-07-16",
                title = "Inception",
                video = false,
                voteAverage = 8.8,
                voteCount = 20000
            ),
            PopularMoviesResponse.Result(
                adult = true,
                backdropPath = "/backdrop2.jpg",
                genreIds = listOf(18L, 80L),
                id = 202L,
                originalLanguage = "fr",
                originalTitle = "Le Film",
                overview = "French movie overview...",
                popularity = 60.3,
                posterPath = "/poster2.jpg",
                releaseDate = "2021-05-10",
                title = "Le Film",
                video = true,
                voteAverage = 7.5,
                voteCount = 1200
            )
        )

        // When
        val response = PopularMoviesResponse(
            page = 1,
            results = expectedResults,
            totalPages = 10,
            totalResults = 200
        )

        // Then
        Truth.assertThat(response.page).isEqualTo(1)
        Truth.assertThat(response.totalPages).isEqualTo(10)
        Truth.assertThat(response.totalResults).isEqualTo(200)
        Truth.assertThat(response.results).hasSize(2)

        val first = response.results!![0]
        Truth.assertThat(first!!.id).isEqualTo(101L)
        Truth.assertThat(first.title).isEqualTo("Inception")
        Truth.assertThat(first.genreIds).containsExactly(28L, 12L, 878L).inOrder()
        Truth.assertThat(first.voteAverage).isEqualTo(8.8)
        Truth.assertThat(first.voteCount).isEqualTo(20000)
    }

    @Test
    fun `should create PopularMoviesResponse with default values when no fields are provided`() {
        // Given & When
        val response = PopularMoviesResponse()

        // Then
        Truth.assertThat(response.page).isNull()
        Truth.assertThat(response.results).isNull()
        Truth.assertThat(response.totalPages).isNull()
        Truth.assertThat(response.totalResults).isNull()
    }

    @Test
    fun `should create Result with default values when no fields are provided`() {
        // Given & When
        val result = PopularMoviesResponse.Result()

        // Then
        Truth.assertThat(result.adult).isNull()
        Truth.assertThat(result.backdropPath).isNull()
        Truth.assertThat(result.genreIds).isNull()
        Truth.assertThat(result.id).isNull()
        Truth.assertThat(result.originalLanguage).isNull()
        Truth.assertThat(result.originalTitle).isNull()
        Truth.assertThat(result.overview).isNull()
        Truth.assertThat(result.popularity).isNull()
        Truth.assertThat(result.posterPath).isNull()
        Truth.assertThat(result.releaseDate).isNull()
        Truth.assertThat(result.title).isNull()
        Truth.assertThat(result.video).isNull()
        Truth.assertThat(result.voteAverage).isNull()
        Truth.assertThat(result.voteCount).isNull()
    }

    @Test
    fun `should create PopularMoviesResponse with empty results list when results provided as empty`() {
        // Given
        val response = PopularMoviesResponse(
            page = 2,
            results = emptyList(),
            totalPages = 5,
            totalResults = 50
        )

        // Then
        Truth.assertThat(response.page).isEqualTo(2)
        Truth.assertThat(response.results).isEmpty()
        Truth.assertThat(response.totalPages).isEqualTo(5)
        Truth.assertThat(response.totalResults).isEqualTo(50)
    }

    @Test
    fun `should create Result with partial data when some fields are provided`() {
        // When
        val result = PopularMoviesResponse.Result(
            title = "Avatar",
            voteAverage = 7.9
        )

        // Then
        Truth.assertThat(result.id).isNull()
        Truth.assertThat(result.title).isEqualTo("Avatar")
        Truth.assertThat(result.voteAverage).isEqualTo(7.9)
        Truth.assertThat(result.genreIds).isNull()
        Truth.assertThat(result.video).isNull()
    }

    @Test
    fun `should handle null result inside results list`() {
        // Given
        val response = PopularMoviesResponse(
            page = 3,
            results = listOf(null),
            totalPages = 7,
            totalResults = 70
        )

        // Then
        Truth.assertThat(response.page).isEqualTo(3)
        Truth.assertThat(response.results).hasSize(1)
        Truth.assertThat(response.results!![0]).isNull()
        Truth.assertThat(response.totalPages).isEqualTo(7)
        Truth.assertThat(response.totalResults).isEqualTo(70)
    }

    @Test
    fun `should handle multiple genreIds inside Result`() {
        // Given
        val result = PopularMoviesResponse.Result(
            id = 500L,
            title = "Genre Rich Movie",
            genreIds = listOf(12L, 14L, 16L, 18L)
        )

        // Then
        Truth.assertThat(result.id).isEqualTo(500L)
        Truth.assertThat(result.title).isEqualTo("Genre Rich Movie")
        Truth.assertThat(result.genreIds).containsExactly(12L, 14L, 16L, 18L).inOrder()
    }
}