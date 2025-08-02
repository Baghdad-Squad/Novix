package com.baghdad.remoteDataSource.respons.actor

import com.baghdad.remoteDataSource.response.actor.ActorTvShowDto
import com.baghdad.remoteDataSource.response.actor.ActorTvShowsResponse
import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

class ActorTvShowsResponseTest {

    @Test
    fun `should create ActorTvShowsResponse with full data when all fields are provided`() {
        // Given
        val expectedShows = listOf(
            ActorTvShowDto(
                id = 100,
                name = "Breaking Bad",
                genreIds = listOf(18, 80),
                overview = "A chemistry teacher turned meth producer.",
                posterPath = "/breakingbad.jpg",
                voteAverage = 9.5,
                firstAirDate = "2008-01-20",
                originalName = "Breaking Bad"
            )
        )

        // When
        val actorTvShowsResponse = ActorTvShowsResponse(cast = expectedShows)

        // Then
        assertThat(actorTvShowsResponse.cast).isNotNull()
        assertThat(actorTvShowsResponse.cast!!.size).isEqualTo(1)

        val firstShow = actorTvShowsResponse.cast!![0]
        assertThat(firstShow.id).isEqualTo(100)
        assertThat(firstShow.name).isEqualTo("Breaking Bad")
        assertThat(firstShow.genreIds).containsExactly(18, 80).inOrder()
        assertThat(firstShow.overview).isEqualTo("A chemistry teacher turned meth producer.")
        assertThat(firstShow.posterPath).isEqualTo("/breakingbad.jpg")
        assertThat(firstShow.voteAverage).isEqualTo(9.5)
        assertThat(firstShow.firstAirDate).isEqualTo("2008-01-20")
        assertThat(firstShow.originalName).isEqualTo("Breaking Bad")
    }

    @Test
    fun `should create ActorTvShowsResponse with default values when no fields are provided`() {
        // Given & When
        val actorTvShowsResponse = ActorTvShowsResponse()

        // Then
        assertThat(actorTvShowsResponse.cast).isNull()
    }

    @Test
    fun `should create ActorTvShowDto with default values when no fields are provided`() {
        // Given & When
        val tvShow = ActorTvShowDto()

        // Then
        assertThat(tvShow.id).isNull()
        assertThat(tvShow.name).isNull()
        assertThat(tvShow.genreIds).isNull()
        assertThat(tvShow.overview).isNull()
        assertThat(tvShow.posterPath).isNull()
        assertThat(tvShow.voteAverage).isNull()
        assertThat(tvShow.firstAirDate).isNull()
        assertThat(tvShow.originalName).isNull()
    }

    @Test
    fun `should create ActorTvShowsResponse with empty cast list when cast provided as empty`() {
        // Given
        val expectedShows = emptyList<ActorTvShowDto>()

        // When
        val actorTvShowsResponse = ActorTvShowsResponse(cast = expectedShows)

        // Then
        assertThat(actorTvShowsResponse.cast).isEmpty()
    }

    @Test
    fun `should create ActorTvShowDto with partial data when some fields are provided`() {
        // When
        val tvShow = ActorTvShowDto(
            name = "Stranger Things",
            voteAverage = 8.7
        )

        // Then
        assertThat(tvShow.id).isNull()
        assertThat(tvShow.name).isEqualTo("Stranger Things")
        assertThat(tvShow.genreIds).isNull()
        assertThat(tvShow.overview).isNull()
        assertThat(tvShow.posterPath).isNull()
        assertThat(tvShow.voteAverage).isEqualTo(8.7)
        assertThat(tvShow.firstAirDate).isNull()
        assertThat(tvShow.originalName).isNull()
    }

    @Test
    fun `should handle empty genreIds list when genreIds provided as empty`() {
        // Given
        val tvShow = ActorTvShowDto(
            id = 300,
            name = "Dark",
            genreIds = emptyList(),
            voteAverage = 8.8
        )

        // Then
        assertThat(tvShow.genreIds).isEmpty()
        assertThat(tvShow.name).isEqualTo("Dark")
        assertThat(tvShow.voteAverage).isEqualTo(8.8)
    }

    @Test
    fun `should have null cast when ActorTvShowsResponse created without providing cast`() {
        // Given & When
        val response = ActorTvShowsResponse()

        // Then
        assertThat(response.cast).isNull()
    }

    @Test
    fun `should set id when ActorTvShowDto created with id`() {
        // When
        val tvShow = ActorTvShowDto(id = 123)

        // Then
        assertThat(tvShow.id).isEqualTo(123)
        assertThat(tvShow.name).isNull()
    }

    @Test
    fun `should set name when ActorTvShowDto created with name`() {
        // When
        val tvShow = ActorTvShowDto(name = "Game of Thrones")

        // Then
        assertThat(tvShow.name).isEqualTo("Game of Thrones")
        assertThat(tvShow.id).isNull()
    }

}
