package com.baghdad.remoteDataSource.respons.tvShow

import com.baghdad.remoteDataSource.response.tvShow.TrendingTvShowsResponse
import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

class TrendingTvShowsResponseTest {

    @Test
    fun `should create TrendingTvShowsResponse with full data when all fields are provided`() {
        // Given
        val expectedResults = listOf(
            TrendingTvShowsResponse.TrendingTvShow(
                adult = false,
                backdropPath = "/backdrop1.jpg",
                id = 101,
                name = "Breaking Bad",
                originalName = "Breaking Bad",
                overview = "A chemistry teacher turned meth producer.",
                posterPath = "/poster1.jpg",
                mediaType = "tv",
                originalLanguage = "en",
                genreIds = listOf(18, 80),
                popularity = 99.5,
                firstAirDate = "2008-01-20",
                voteAverage = 9.5,
                voteCount = 1200,
                originCountry = listOf("US")
            ),
            TrendingTvShowsResponse.TrendingTvShow(
                adult = false,
                backdropPath = "/backdrop2.jpg",
                id = 202,
                name = "Friends",
                originalName = "Friends",
                overview = "A sitcom about six friends.",
                posterPath = "/poster2.jpg",
                mediaType = "tv",
                originalLanguage = "en",
                genreIds = listOf(35, 18),
                popularity = 85.4,
                firstAirDate = "1994-09-22",
                voteAverage = 8.9,
                voteCount = 900,
                originCountry = listOf("US")
            )
        )

        // When
        val response = TrendingTvShowsResponse(
            page = 1,
            results = expectedResults,
            totalPages = 5,
            totalResults = 100
        )

        // Then
        assertThat(response.page).isEqualTo(1)
        assertThat(response.results).isNotNull()
        assertThat(response.results!!.size).isEqualTo(2)
        assertThat(response.totalPages).isEqualTo(5)
        assertThat(response.totalResults).isEqualTo(100)

        val firstShow = response.results[0]!!
        assertThat(firstShow.name).isEqualTo("Breaking Bad")
        assertThat(firstShow.voteAverage).isEqualTo(9.5)
        assertThat(firstShow.originCountry).containsExactly("US")
    }

    @Test
    fun `should create TrendingTvShowsResponse with default values when no fields are provided`() {
        // When
        val response = TrendingTvShowsResponse()

        // Then
        assertThat(response.page).isNull()
        assertThat(response.results).isNull()
        assertThat(response.totalPages).isNull()
        assertThat(response.totalResults).isNull()
    }

    @Test
    fun `should create TrendingTvShowsResponse with empty results when results provided as empty`() {
        // When
        val response = TrendingTvShowsResponse(results = emptyList())

        // Then
        assertThat(response.results).isEmpty()
    }

    @Test
    fun `should create TrendingTvShow with default values when no fields are provided`() {
        // When
        val tvShow = TrendingTvShowsResponse.TrendingTvShow()

        // Then
        assertThat(tvShow.adult).isNull()
        assertThat(tvShow.backdropPath).isNull()
        assertThat(tvShow.id).isNull()
        assertThat(tvShow.name).isNull()
        assertThat(tvShow.originalName).isNull()
        assertThat(tvShow.overview).isNull()
        assertThat(tvShow.posterPath).isNull()
        assertThat(tvShow.mediaType).isNull()
        assertThat(tvShow.originalLanguage).isNull()
        assertThat(tvShow.genreIds).isNull()
        assertThat(tvShow.popularity).isNull()
        assertThat(tvShow.firstAirDate).isNull()
        assertThat(tvShow.voteAverage).isNull()
        assertThat(tvShow.voteCount).isNull()
        assertThat(tvShow.originCountry).isNull()
    }

    @Test
    fun `should create TrendingTvShow with partial data when some fields are provided`() {
        // When
        val tvShow = TrendingTvShowsResponse.TrendingTvShow(
            name = "Dark",
            voteAverage = 8.8
        )

        // Then
        assertThat(tvShow.name).isEqualTo("Dark")
        assertThat(tvShow.voteAverage).isEqualTo(8.8)
        assertThat(tvShow.id).isNull()
        assertThat(tvShow.overview).isNull()
    }
}
