package com.baghdad.remoteDataSource.respons.search

import com.baghdad.remoteDataSource.response.search.MovieSearchResponse
import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

class MovieSearchResponseTest {

    @Test
    fun `should create MovieSearchResponse with full data when all fields are provided`() {
        // Given
        val expectedResults = listOf(
            MovieSearchResponse.Result(
                adult = false,
                backdropPath = "/backdrop1.jpg",
                genreIds = listOf(28, 12, 878),
                id = 101,
                originalLanguage = "en",
                originalTitle = "Inception",
                overview = "A thief who steals corporate secrets...",
                popularity = 98.5,
                posterPath = "/poster1.jpg",
                releaseDate = "2010-07-16",
                title = "Inception",
                video = false,
                voteAverage = 8.8,
                voteCount = 20000
            ),
            MovieSearchResponse.Result(
                adult = true,
                backdropPath = "/backdrop2.jpg",
                genreIds = listOf(18, 10749),
                id = 202,
                originalLanguage = "fr",
                originalTitle = "Amélie",
                overview = "Amélie is an innocent and naive girl in Paris...",
                popularity = 75.3,
                posterPath = "/poster2.jpg",
                releaseDate = "2001-04-25",
                title = "Amélie",
                video = null,
                voteAverage = 8.3,
                voteCount = 15000
            )
        )

        // When
        val response = MovieSearchResponse(
            page = 1,
            results = expectedResults,
            totalPages = 10,
            totalResults = 200
        )

        // Then
        assertThat(response.page).isEqualTo(1)
        assertThat(response.totalPages).isEqualTo(10)
        assertThat(response.totalResults).isEqualTo(200)
        assertThat(response.results).hasSize(2)

        val first = response.results!![0]
        assertThat(first!!.id).isEqualTo(101)
        assertThat(first.title).isEqualTo("Inception")
        assertThat(first.genreIds).containsExactly(28, 12, 878).inOrder()
        assertThat(first.voteAverage).isEqualTo(8.8)
        assertThat(first.voteCount).isEqualTo(20000)
        assertThat(first.adult).isFalse()
        assertThat(first.video).isFalse()
    }

    @Test
    fun `should create MovieSearchResponse with default values when no fields are provided`() {
        // Given & When
        val response = MovieSearchResponse()

        // Then
        assertThat(response.page).isNull()
        assertThat(response.results).isNull()
        assertThat(response.totalPages).isNull()
        assertThat(response.totalResults).isNull()
    }

    @Test
    fun `should create Result with default values when no fields are provided`() {
        // Given & When
        val result = MovieSearchResponse.Result()

        // Then
        assertThat(result.adult).isNull()
        assertThat(result.backdropPath).isNull()
        assertThat(result.genreIds).isNull()
        assertThat(result.id).isNull()
        assertThat(result.originalLanguage).isNull()
        assertThat(result.originalTitle).isNull()
        assertThat(result.overview).isNull()
        assertThat(result.popularity).isNull()
        assertThat(result.posterPath).isNull()
        assertThat(result.releaseDate).isNull()
        assertThat(result.title).isNull()
        assertThat(result.video).isNull()
        assertThat(result.voteAverage).isNull()
        assertThat(result.voteCount).isNull()
    }

    @Test
    fun `should create MovieSearchResponse with empty results list when results provided as empty`() {
        // Given
        val response = MovieSearchResponse(
            page = 2,
            results = emptyList(),
            totalPages = 0,
            totalResults = 0
        )

        // Then
        assertThat(response.page).isEqualTo(2)
        assertThat(response.results).isEmpty()
        assertThat(response.totalPages).isEqualTo(0)
        assertThat(response.totalResults).isEqualTo(0)
    }

    @Test
    fun `should create Result with partial data when some fields are provided`() {
        // When
        val result = MovieSearchResponse.Result(
            title = "Avatar",
            popularity = 88.8
        )

        // Then
        assertThat(result.id).isNull()
        assertThat(result.title).isEqualTo("Avatar")
        assertThat(result.popularity).isEqualTo(88.8)
        assertThat(result.genreIds).isNull()
        assertThat(result.releaseDate).isNull()
    }

    @Test
    fun `should handle null result inside results list`() {
        // Given
        val response = MovieSearchResponse(
            page = 3,
            results = listOf(null),
            totalPages = 1,
            totalResults = 1
        )

        // Then
        assertThat(response.page).isEqualTo(3)
        assertThat(response.results).hasSize(1)
        assertThat(response.results!![0]).isNull()
        assertThat(response.totalPages).isEqualTo(1)
        assertThat(response.totalResults).isEqualTo(1)
    }
}