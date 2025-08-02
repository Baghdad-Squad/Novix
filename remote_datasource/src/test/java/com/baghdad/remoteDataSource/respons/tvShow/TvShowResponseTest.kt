package com.baghdad.remoteDataSource.respons.tvShow

import com.baghdad.remoteDataSource.response.movie.Genre
import com.baghdad.remoteDataSource.response.tvShow.TVShowDetailsResponse
import com.baghdad.remoteDataSource.response.tvShow.TvShowResponse
import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

class TvShowResponseTest {

    @Test
    fun `should create TvShowResponse with full data when all fields are provided`() {
        // Given
        val expectedShows = listOf(
            TVShowDetailsResponse(
                genres = listOf(Genre(id = 1, name = "Drama")),
                id = 101,
                name = "Breaking Bad",
                numberOfSeasons = 5,
                overview = "A chemistry teacher turned meth producer.",
                posterPath = "/breakingbad.jpg",
                voteAverage = 9.5,
                firstAirDate = "2008-01-20"
            ),
            TVShowDetailsResponse(
                genres = listOf(Genre(id = 2, name = "Crime")),
                id = 202,
                name = "Better Call Saul",
                numberOfSeasons = 6,
                overview = "A prequel to Breaking Bad.",
                posterPath = "/bettercallsaul.jpg",
                voteAverage = 8.9,
                firstAirDate = "2015-02-08"
            )
        )

        // When
        val response = TvShowResponse(
            page = 1,
            results = expectedShows,
            totalPages = 10,
            totalResults = 200
        )

        // Then
        assertThat(response.page).isEqualTo(1)
        assertThat(response.results).isNotNull()
        assertThat(response.results!!.size).isEqualTo(2)
        assertThat(response.totalPages).isEqualTo(10)
        assertThat(response.totalResults).isEqualTo(200)

        val firstShow = response.results[0]
        assertThat(firstShow.id).isEqualTo(101)
        assertThat(firstShow.name).isEqualTo("Breaking Bad")
        assertThat(firstShow.genres!!.first().name).isEqualTo("Drama")
    }

    @Test
    fun `should create TvShowResponse with default values when no fields are provided`() {
        // When
        val response = TvShowResponse()

        // Then
        assertThat(response.page).isNull()
        assertThat(response.results).isNull()
        assertThat(response.totalPages).isNull()
        assertThat(response.totalResults).isNull()
    }

    @Test
    fun `should create TvShowResponse with empty results when provided`() {
        // When
        val response = TvShowResponse(
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
    fun `should create TvShowResponse with partial data when some fields are provided`() {
        // When
        val response = TvShowResponse(
            page = 3,
            results = listOf(
                TVShowDetailsResponse(
                    id = 303,
                    name = "Stranger Things"
                )
            )
        )

        // Then
        assertThat(response.page).isEqualTo(3)
        assertThat(response.results).isNotNull()
        assertThat(response.results!!.first().id).isEqualTo(303)
        assertThat(response.results.first().name).isEqualTo("Stranger Things")
        assertThat(response.totalPages).isNull()
        assertThat(response.totalResults).isNull()
    }
}
