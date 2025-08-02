package com.baghdad.remoteDataSource.mapper.actor

import com.baghdad.remoteDataSource.response.actor.ActorTvShowDto
import com.baghdad.remoteDataSource.response.actor.ActorTvShowsResponse
import com.google.common.truth.Truth
import org.junit.jupiter.api.Test

class ActorTvShowsResponseTest {

    @Test
    fun `should create ActorTvShowsResponse with full data when all fields are provided`() {
        // Given
        val expectedShows = listOf(
            ActorTvShowDto(
                id = 101,
                name = "Breaking Bad",
                genreIds = listOf(18, 80),
                overview = "A chemistry teacher turned meth producer.",
                posterPath = "/breakingbad.jpg",
                voteAverage = 9.5,
                firstAirDate = "2008-01-20",
                originalName = "Breaking Bad"
            ),
            ActorTvShowDto(
                id = 202,
                name = "Better Call Saul",
                genreIds = listOf(18, 80),
                overview = "A prequel to Breaking Bad.",
                posterPath = "/bettercallsaul.jpg",
                voteAverage = 8.9,
                firstAirDate = "2015-02-08",
                originalName = "Better Call Saul"
            )
        )

        // When
        val response = ActorTvShowsResponse(cast = expectedShows)

        // Then
        Truth.assertThat(response.cast).isNotNull()
        Truth.assertThat(response.cast).hasSize(2)

        val firstShow = response.cast!![0]
        Truth.assertThat(firstShow.id).isEqualTo(101)
        Truth.assertThat(firstShow.name).isEqualTo("Breaking Bad")
        Truth.assertThat(firstShow.genreIds).containsExactly(18, 80).inOrder()
        Truth.assertThat(firstShow.overview).isEqualTo("A chemistry teacher turned meth producer.")
        Truth.assertThat(firstShow.posterPath).isEqualTo("/breakingbad.jpg")
        Truth.assertThat(firstShow.voteAverage).isEqualTo(9.5)
        Truth.assertThat(firstShow.firstAirDate).isEqualTo("2008-01-20")
        Truth.assertThat(firstShow.originalName).isEqualTo("Breaking Bad")
    }

    @Test
    fun `should create ActorTvShowsResponse with default values when no fields are provided`() {
        // Given & When
        val response = ActorTvShowsResponse()

        // Then
        Truth.assertThat(response.cast).isNull()
    }

    @Test
    fun `should create ActorTvShowDto with default values when no fields are provided`() {
        // Given & When
        val tvShow = ActorTvShowDto()

        // Then
        Truth.assertThat(tvShow.id).isNull()
        Truth.assertThat(tvShow.name).isNull()
        Truth.assertThat(tvShow.genreIds).isNull()
        Truth.assertThat(tvShow.overview).isNull()
        Truth.assertThat(tvShow.posterPath).isNull()
        Truth.assertThat(tvShow.voteAverage).isNull()
        Truth.assertThat(tvShow.firstAirDate).isNull()
        Truth.assertThat(tvShow.originalName).isNull()
    }

    @Test
    fun `should create ActorTvShowsResponse with empty cast list when cast provided as empty`() {
        // Given
        val response = ActorTvShowsResponse(cast = emptyList())

        // Then
        Truth.assertThat(response.cast).isEmpty()
    }

    @Test
    fun `should create ActorTvShowDto with partial data when some fields are provided`() {
        // Given
        val expectedName = "Stranger Things"
        val expectedVote = 8.7

        // When
        val tvShow = ActorTvShowDto(
            name = expectedName,
            voteAverage = expectedVote
        )

        // Then
        Truth.assertThat(tvShow.id).isNull()
        Truth.assertThat(tvShow.name).isEqualTo(expectedName)
        Truth.assertThat(tvShow.genreIds).isNull()
        Truth.assertThat(tvShow.overview).isNull()
        Truth.assertThat(tvShow.posterPath).isNull()
        Truth.assertThat(tvShow.voteAverage).isEqualTo(expectedVote)
        Truth.assertThat(tvShow.firstAirDate).isNull()
        Truth.assertThat(tvShow.originalName).isNull()
    }

    @Test
    fun `should allow null values for all ActorTvShowDto fields`() {
        // Given & When
        val tvShow = ActorTvShowDto(
            id = null,
            name = null,
            genreIds = null,
            overview = null,
            posterPath = null,
            voteAverage = null,
            firstAirDate = null,
            originalName = null
        )

        // Then
        Truth.assertThat(tvShow.id).isNull()
        Truth.assertThat(tvShow.name).isNull()
        Truth.assertThat(tvShow.genreIds).isNull()
        Truth.assertThat(tvShow.overview).isNull()
        Truth.assertThat(tvShow.posterPath).isNull()
        Truth.assertThat(tvShow.voteAverage).isNull()
        Truth.assertThat(tvShow.firstAirDate).isNull()
        Truth.assertThat(tvShow.originalName).isNull()
    }
}