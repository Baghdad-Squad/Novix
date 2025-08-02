package com.baghdad.remoteDataSource.respons.actor

import com.baghdad.remoteDataSource.response.actor.ActorMovieDto
import com.baghdad.remoteDataSource.response.actor.ActorMoviesResponse
import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

class ActorMoviesResponseTest {

    @Test
    fun `should create ActorMoviesResponse with full data when all fields are provided`() {
        // Given
        val expectedMovies = listOf(
            ActorMovieDto(
                id = 1,
                title = "Inception",
                genreIds = listOf(28, 878, 12),
                voteAverage = 8.8,
                releaseDate = "2010-07-16",
                overview = "A thief who steals corporate secrets...",
                posterPath = "/inception.jpg"
            ),
            ActorMovieDto(
                id = 2,
                title = "The Revenant",
                genreIds = listOf(18, 12),
                voteAverage = 8.0,
                releaseDate = "2015-12-25",
                overview = "A frontiersman on a fur trading expedition...",
                posterPath = "/revenant.jpg"
            )
        )

        // When
        val actorMoviesResponse = ActorMoviesResponse(cast = expectedMovies)

        // Then
        assertThat(actorMoviesResponse.cast).isNotNull()
        assertThat(actorMoviesResponse.cast!!.size).isEqualTo(2)

        val firstMovie = actorMoviesResponse.cast[0]
        assertThat(firstMovie.id).isEqualTo(expectedMovies.first().id)
        assertThat(firstMovie.title).isEqualTo("Inception")
        assertThat(firstMovie.genreIds).containsExactly(28, 878, 12).inOrder()
        assertThat(firstMovie.voteAverage).isEqualTo(expectedMovies.first().voteAverage)
        assertThat(firstMovie.releaseDate).isEqualTo(expectedMovies.first().releaseDate)
        assertThat(firstMovie.overview).isEqualTo(expectedMovies.first().overview)
        assertThat(firstMovie.posterPath).isEqualTo(expectedMovies.first().posterPath)
    }

    @Test
    fun `should create ActorMoviesResponse with default values when no fields are provided`() {
        // Given & When
        val actorMoviesResponse = ActorMoviesResponse()

        // Then
        assertThat(actorMoviesResponse.cast).isNull()
    }

    @Test
    fun `should create ActorMovieDto with default values when no fields are provided`() {
        // Given & When
        val movie = ActorMovieDto()

        // Then
        assertThat(movie.id).isNull()
        assertThat(movie.title).isNull()
        assertThat(movie.genreIds).isNull()
        assertThat(movie.voteAverage).isNull()
        assertThat(movie.releaseDate).isNull()
        assertThat(movie.overview).isNull()
        assertThat(movie.posterPath).isNull()
    }

    @Test
    fun `should create ActorMoviesResponse with empty cast list when cast provided as empty`() {
        // Given
        val expectedMovies = emptyList<ActorMovieDto>()

        // When
        val actorMoviesResponse = ActorMoviesResponse(cast = expectedMovies)

        // Then
        assertThat(actorMoviesResponse.cast).isEmpty()
    }

    @Test
    fun `should create ActorMovieDto with partial data when some fields are provided`() {
        // When
        val movie = ActorMovieDto(
            title = "Titanic",
            voteAverage = 7.9
        )

        // Then
        assertThat(movie.id).isNull()
        assertThat(movie.title).isEqualTo("Titanic")
        assertThat(movie.genreIds).isNull()
        assertThat(movie.voteAverage).isEqualTo(7.9)
        assertThat(movie.releaseDate).isNull()
        assertThat(movie.overview).isNull()
        assertThat(movie.posterPath).isNull()
    }
}