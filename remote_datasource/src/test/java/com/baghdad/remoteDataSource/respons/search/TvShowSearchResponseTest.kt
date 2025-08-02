package com.baghdad.remoteDataSource.respons.search

import com.baghdad.remoteDataSource.response.search.TvShowSearchResponse
import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

class TvShowSearchResponseTest {

    @Test
    fun `should create TvShowSearchResponse with full data when all fields are provided`() {
        // Given
        val expectedResults = listOf(
            TvShowSearchResponse.Result(
                adult = false,
                backdropPath = "/breakingbad_backdrop.jpg",
                genreIds = listOf(18, 80),
                id = 101,
                originalLanguage = "en",
                originalTitle = "Breaking Bad",
                overview = "A chemistry teacher turned meth producer.",
                popularity = 99.9,
                posterPath = "/breakingbad.jpg",
                releaseDate = "2008-01-20",
                title = "Breaking Bad",
                video = null,
                voteAverage = 9.5,
                voteCount = 1500
            ),
            TvShowSearchResponse.Result(
                adult = true,
                backdropPath = "/got_backdrop.jpg",
                genreIds = listOf(10765, 18),
                id = 202,
                originalLanguage = "en",
                originalTitle = "Game of Thrones",
                overview = "Nine noble families fight for control over the lands of Westeros.",
                popularity = 98.1,
                posterPath = "/got.jpg",
                releaseDate = "2011-04-17",
                title = "Game of Thrones",
                video = false,
                voteAverage = 9.3,
                voteCount = 2000
            )
        )

        // When
        val response = TvShowSearchResponse(
            page = 1,
            results = expectedResults,
            totalPages = 5,
            totalResults = 100
        )

        // Then
        assertThat(response.page).isEqualTo(1)
        assertThat(response.totalPages).isEqualTo(5)
        assertThat(response.totalResults).isEqualTo(100)
        assertThat(response.results).hasSize(2)

        val first = response.results!![0]
        assertThat(first!!.id).isEqualTo(101)
        assertThat(first.title).isEqualTo("Breaking Bad")
        assertThat(first.genreIds).containsExactly(18, 80).inOrder()
        assertThat(first.voteAverage).isEqualTo(9.5)
        assertThat(first.voteCount).isEqualTo(1500)
        assertThat(first.adult).isFalse()
        assertThat(first.video).isNull()
    }

    @Test
    fun `should create TvShowSearchResponse with default values when no fields are provided`() {
        // Given & When
        val response = TvShowSearchResponse()

        // Then
        assertThat(response.page).isNull()
        assertThat(response.results).isNull()
        assertThat(response.totalPages).isNull()
        assertThat(response.totalResults).isNull()
    }

    @Test
    fun `should create Result with default values when no fields are provided`() {
        // Given & When
        val result = TvShowSearchResponse.Result()

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
    fun `should create TvShowSearchResponse with empty results list when results provided as empty`() {
        // Given
        val response = TvShowSearchResponse(
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
        val result = TvShowSearchResponse.Result(
            title = "Stranger Things",
            popularity = 92.7
        )

        // Then
        assertThat(result.id).isNull()
        assertThat(result.title).isEqualTo("Stranger Things")
        assertThat(result.popularity).isEqualTo(92.7)
        assertThat(result.genreIds).isNull()
        assertThat(result.releaseDate).isNull()
        assertThat(result.voteAverage).isNull()
    }

    @Test
    fun `should handle null result inside results list`() {
        // Given
        val response = TvShowSearchResponse(
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