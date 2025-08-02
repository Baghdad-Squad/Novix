package com.baghdad.remoteDataSource.respons.tvShow

import com.baghdad.remoteDataSource.response.tvShow.PopularTvShowsResponse
import com.google.common.truth.Truth
import org.junit.jupiter.api.Test

class PopularTvShowsResponseTest {

    @Test
    fun `should create PopularTvShowsResponse with full data when all fields are provided`() {
        // Given
        val expectedResults = listOf(
            PopularTvShowsResponse.Result(
                adult = false,
                backdropPath = "/backdrop1.jpg",
                firstAirDate = "2010-10-31",
                genreIds = listOf(18L, 80L),
                id = 101L,
                name = "The Walking Dead",
                originCountry = listOf("US"),
                originalLanguage = "en",
                originalName = "The Walking Dead",
                overview = "Zombie apocalypse survival story.",
                popularity = 85.6,
                posterPath = "/twd.jpg",
                voteAverage = 8.2,
                voteCount = 1500
            ),
            PopularTvShowsResponse.Result(
                adult = false,
                backdropPath = "/backdrop2.jpg",
                firstAirDate = "2016-07-15",
                genreIds = listOf(18L, 9648L, 10765L),
                id = 202L,
                name = "Stranger Things",
                originCountry = listOf("US"),
                originalLanguage = "en",
                originalName = "Stranger Things",
                overview = "Supernatural thriller set in the 80s.",
                popularity = 95.3,
                posterPath = "/st.jpg",
                voteAverage = 8.7,
                voteCount = 2000
            )
        )

        // When
        val response = PopularTvShowsResponse(
            page = 1,
            results = expectedResults,
            totalPages = 10,
            totalResults = 200
        )

        // Then
        Truth.assertThat(response.page).isEqualTo(1)
        Truth.assertThat(response.totalPages).isEqualTo(10)
        Truth.assertThat(response.totalResults).isEqualTo(200)
        Truth.assertThat(response.results).isNotNull()
        Truth.assertThat(response.results).hasSize(2)

        val firstShow = response.results!![0]!!
        Truth.assertThat(firstShow.name).isEqualTo("The Walking Dead")
        Truth.assertThat(firstShow.genreIds).containsExactly(18L, 80L).inOrder()
        Truth.assertThat(firstShow.originCountry).containsExactly("US")
        Truth.assertThat(firstShow.voteAverage).isEqualTo(8.2)
        Truth.assertThat(firstShow.voteCount).isEqualTo(1500)

        val secondShow = response.results[1]!!
        Truth.assertThat(secondShow.name).isEqualTo("Stranger Things")
        Truth.assertThat(secondShow.genreIds).containsExactly(18L, 9648L, 10765L).inOrder()
        Truth.assertThat(secondShow.originCountry).containsExactly("US")
        Truth.assertThat(secondShow.voteAverage).isEqualTo(8.7)
        Truth.assertThat(secondShow.voteCount).isEqualTo(2000)
    }

    @Test
    fun `should create PopularTvShowsResponse with default values when no fields are provided`() {
        // Given & When
        val response = PopularTvShowsResponse()

        // Then
        Truth.assertThat(response.page).isNull()
        Truth.assertThat(response.results).isNull()
        Truth.assertThat(response.totalPages).isNull()
        Truth.assertThat(response.totalResults).isNull()
    }

    @Test
    fun `should create PopularTvShowsResponse with empty results list when results provided as empty`() {
        // Given & When
        val response = PopularTvShowsResponse(
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
    fun `should create Result with default values when no fields are provided`() {
        // Given & When
        val result = PopularTvShowsResponse.Result()

        // Then
        Truth.assertThat(result.adult).isNull()
        Truth.assertThat(result.backdropPath).isNull()
        Truth.assertThat(result.firstAirDate).isNull()
        Truth.assertThat(result.genreIds).isNull()
        Truth.assertThat(result.id).isNull()
        Truth.assertThat(result.name).isNull()
        Truth.assertThat(result.originCountry).isNull()
        Truth.assertThat(result.originalLanguage).isNull()
        Truth.assertThat(result.originalName).isNull()
        Truth.assertThat(result.overview).isNull()
        Truth.assertThat(result.popularity).isNull()
        Truth.assertThat(result.posterPath).isNull()
        Truth.assertThat(result.voteAverage).isNull()
        Truth.assertThat(result.voteCount).isNull()
    }

    @Test
    fun `should create Result with partial data when some fields are provided`() {
        // When
        val result = PopularTvShowsResponse.Result(
            name = "Dark",
            voteAverage = 9.0
        )

        // Then
        Truth.assertThat(result.id).isNull()
        Truth.assertThat(result.name).isEqualTo("Dark")
        Truth.assertThat(result.voteAverage).isEqualTo(9.0)
        Truth.assertThat(result.genreIds).isNull()
        Truth.assertThat(result.originCountry).isNull()
    }

    @Test
    fun `should allow null values for all fields in Result`() {
        // Given & When
        val result = PopularTvShowsResponse.Result(
            adult = null,
            backdropPath = null,
            firstAirDate = null,
            genreIds = null,
            id = null,
            name = null,
            originCountry = null,
            originalLanguage = null,
            originalName = null,
            overview = null,
            popularity = null,
            posterPath = null,
            voteAverage = null,
            voteCount = null
        )

        // Then
        Truth.assertThat(result.adult).isNull()
        Truth.assertThat(result.genreIds).isNull()
        Truth.assertThat(result.originCountry).isNull()
    }

    @Test
    fun `should handle empty genreIds and originCountry lists`() {
        // Given & When
        val result = PopularTvShowsResponse.Result(
            genreIds = emptyList(),
            originCountry = emptyList()
        )

        // Then
        Truth.assertThat(result.genreIds).isEmpty()
        Truth.assertThat(result.originCountry).isEmpty()
    }
}