package com.baghdad.remoteDataSource.response.tvShow

import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

class TopRatedTvShowSearchResponseTest {

    @Test
    fun `should create TopRatedTvShowSearchResponse with full data when all fields are provided`() {
        // Given
        val expectedResults = listOf(
            TopRatedTvShowSearchResponse.Result(
                adult = false,
                backdropPath = "/backdrop1.jpg",
                genreIds = listOf(18, 80),
                id = 101,
                originalLanguage = "en",
                originalTitle = "Breaking Bad",
                overview = "A chemistry teacher turned meth producer.",
                popularity = 99.5,
                posterPath = "/poster1.jpg",
                releaseDate = "2008-01-20",
                title = "Breaking Bad",
                video = false,
                voteAverage = 9.5,
                voteCount = 1200
            ),
            TopRatedTvShowSearchResponse.Result(
                adult = false,
                backdropPath = "/backdrop2.jpg",
                genreIds = listOf(18, 35),
                id = 202,
                originalLanguage = "en",
                originalTitle = "Friends",
                overview = "A sitcom about six friends.",
                popularity = 85.4,
                posterPath = "/poster2.jpg",
                releaseDate = "1994-09-22",
                title = "Friends",
                video = false,
                voteAverage = 8.9,
                voteCount = 900
            )
        )

        // When
        val response = TopRatedTvShowSearchResponse(
            page = 1,
            results = expectedResults,
            totalPages = 10,
            totalResults = 200
        )

        // Then
        assertThat(response.page).isEqualTo(1)
        assertThat(response.results).isNotNull()
        assertThat(response.results!!.size).isEqualTo(2)
        assertThat(response.totalPages).isEqualTo(10)
        assertThat(response.totalResults).isEqualTo(200)

        val firstResult = response.results[0]!!
        assertThat(firstResult.title).isEqualTo("Breaking Bad")
        assertThat(firstResult.voteAverage).isEqualTo(9.5)
    }

    @Test
    fun `should create TopRatedTvShowSearchResponse with default values when no fields are provided`() {
        // When
        val response = TopRatedTvShowSearchResponse()

        // Then
        assertThat(response.page).isNull()
        assertThat(response.results).isNull()
        assertThat(response.totalPages).isNull()
        assertThat(response.totalResults).isNull()
    }

    @Test
    fun `should create TopRatedTvShowSearchResponse with empty results list when provided`() {
        // When
        val response = TopRatedTvShowSearchResponse(results = emptyList())

        // Then
        assertThat(response.results).isEmpty()
    }

    @Test
    fun `should create Result with default values when no fields are provided`() {
        // When
        val result = TopRatedTvShowSearchResponse.Result()

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
    fun `should create Result with partial data when some fields are provided`() {
        // When
        val result = TopRatedTvShowSearchResponse.Result(
            title = "Dark",
            voteAverage = 9.0
        )

        // Then
        assertThat(result.title).isEqualTo("Dark")
        assertThat(result.voteAverage).isEqualTo(9.0)
        assertThat(result.id).isNull()
        assertThat(result.overview).isNull()
    }
}
