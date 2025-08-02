package com.baghdad.remoteDataSource.respons.movie

import com.baghdad.remoteDataSource.response.movie.DiscoverMovieResponse
import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

class DiscoverMovieResponseTest {

    @Test
    fun `should create DiscoverMovieResponse with full data when all fields are provided`() {
        // Given
        val expectedPage = 1
        val expectedTotalPages = 10
        val expectedTotalResults = 200
        val expectedResults = listOf(
            DiscoverMovieResponse.Result(
                adult = false,
                backdropPath = "/backdrop.jpg",
                genreIds = listOf(28L, 12L),
                id = 101L,
                originalLanguage = "en",
                originalTitle = "Inception",
                overview = "A mind-bending thriller",
                popularity = 123.4,
                posterPath = "/poster.jpg",
                releaseDate = "2010-07-16",
                title = "Inception",
                video = false,
                voteAverage = 8.8,
                voteCount = 12000
            )
        )

        // When
        val response = DiscoverMovieResponse(
            page = expectedPage,
            results = expectedResults,
            totalPages = expectedTotalPages,
            totalResults = expectedTotalResults
        )

        // Then
        assertThat(response.page).isEqualTo(expectedPage)
        assertThat(response.totalPages).isEqualTo(expectedTotalPages)
        assertThat(response.totalResults).isEqualTo(expectedTotalResults)
        assertThat(response.results).isNotNull()
        assertThat(response.results!!.size).isEqualTo(1)

        val movie = response.results!![0]
        assertThat(movie!!.id).isEqualTo(101L)
        assertThat(movie.title).isEqualTo("Inception")
        assertThat(movie.genreIds).containsExactly(28L, 12L).inOrder()
        assertThat(movie.voteAverage).isEqualTo(8.8)
        assertThat(movie.adult).isFalse()
    }

    @Test
    fun `should create DiscoverMovieResponse with default values when no fields are provided`() {
        // Given & When
        val response = DiscoverMovieResponse()

        // Then
        assertThat(response.page).isNull()
        assertThat(response.results).isNull()
        assertThat(response.totalPages).isNull()
        assertThat(response.totalResults).isNull()
    }

    @Test
    fun `should create Result with default values when no fields are provided`() {
        // Given & When
        val result = DiscoverMovieResponse.Result()

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
    fun `should create DiscoverMovieResponse with empty results list when results provided as empty`() {
        // Given
        val response = DiscoverMovieResponse(
            page = 2,
            results = emptyList(),
            totalPages = 5,
            totalResults = 50
        )

        // Then
        assertThat(response.page).isEqualTo(2)
        assertThat(response.results).isEmpty()
        assertThat(response.totalPages).isEqualTo(5)
        assertThat(response.totalResults).isEqualTo(50)
    }

    @Test
    fun `should create Result with partial data when some fields are provided`() {
        // Given
        val expectedTitle = "Avatar"
        val expectedVoteAverage = 7.9

        // When
        val result = DiscoverMovieResponse.Result(
            title = expectedTitle,
            voteAverage = expectedVoteAverage
        )

        // Then
        assertThat(result.id).isNull()
        assertThat(result.title).isEqualTo(expectedTitle)
        assertThat(result.voteAverage).isEqualTo(expectedVoteAverage)
        assertThat(result.genreIds).isNull()
    }

    @Test
    fun `should handle null result inside results list`() {
        // Given
        val response = DiscoverMovieResponse(
            results = listOf(null)
        )

        // Then
        assertThat(response.results!!.size).isEqualTo(1)
        assertThat(response.results!![0]).isNull()
    }

    @Test
    fun `should handle empty genreIds list when genreIds is provided as empty`() {
        // Given
        val result = DiscoverMovieResponse.Result(
            id = 303L,
            title = "Interstellar",
            genreIds = emptyList()
        )

        // Then
        assertThat(result.id).isEqualTo(303L)
        assertThat(result.title).isEqualTo("Interstellar")
        assertThat(result.genreIds).isEmpty()
    }
}
