package com.baghdad.remoteDataSource.respons.tvShow

import com.baghdad.remoteDataSource.response.movie.Genre
import com.baghdad.remoteDataSource.response.tvShow.TVShowDetailsResponse
import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

class TVShowDetailsResponseTest {

    @Test
    fun `should create TVShowDetailsResponse with full data when all fields are provided`() {
        // Given
        val expectedGenres = listOf(
            Genre(id = 18, name = "Drama"),
            Genre(id = 80, name = "Crime")
        )

        // When
        val response = TVShowDetailsResponse(
            genres = expectedGenres,
            id = 101,
            name = "Breaking Bad",
            numberOfSeasons = 5,
            overview = "A chemistry teacher turned meth producer.",
            posterPath = "/breakingbad.jpg",
            voteAverage = 9.5,
            firstAirDate = "2008-01-20"
        )

        // Then
        assertThat(response.id).isEqualTo(101)
        assertThat(response.name).isEqualTo("Breaking Bad")
        assertThat(response.numberOfSeasons).isEqualTo(5)
        assertThat(response.overview).isEqualTo("A chemistry teacher turned meth producer.")
        assertThat(response.posterPath).isEqualTo("/breakingbad.jpg")
        assertThat(response.voteAverage).isEqualTo(9.5)
        assertThat(response.firstAirDate).isEqualTo("2008-01-20")
        assertThat(response.genres).isNotNull()
        assertThat(response.genres!!.size).isEqualTo(2)
        assertThat(response.genres!!.map { it.name }).containsExactly("Drama", "Crime")
    }

    @Test
    fun `should create TVShowDetailsResponse with default values when no fields are provided`() {
        // When
        val response = TVShowDetailsResponse()

        // Then
        assertThat(response.id).isNull()
        assertThat(response.name).isNull()
        assertThat(response.numberOfSeasons).isNull()
        assertThat(response.overview).isNull()
        assertThat(response.posterPath).isNull()
        assertThat(response.voteAverage).isNull()
        assertThat(response.firstAirDate).isNull()
        assertThat(response.genres).isNull()
    }

    @Test
    fun `should create TVShowDetailsResponse with partial data when some fields are provided`() {
        // When
        val response = TVShowDetailsResponse(
            name = "Dark",
            voteAverage = 8.7
        )

        // Then
        assertThat(response.name).isEqualTo("Dark")
        assertThat(response.voteAverage).isEqualTo(8.7)
        assertThat(response.id).isNull()
        assertThat(response.numberOfSeasons).isNull()
        assertThat(response.genres).isNull()
        assertThat(response.overview).isNull()
    }
}
