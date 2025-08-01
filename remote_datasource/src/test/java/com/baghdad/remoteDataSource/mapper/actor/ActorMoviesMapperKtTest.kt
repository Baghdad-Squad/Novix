package com.baghdad.remoteDataSource.mapper.actor

import com.baghdad.remoteDataSource.response.actor.ActorMovieDto
import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

class ActorMovieMapperTest {

    @Test
    fun `should map fields correctly when toDto is called`() {
        // Given
        val result = ACTOR_MOVIE_DTO.toDto()

        // Then
        assertThat(result.id).isEqualTo(ACTOR_MOVIE_DTO.id)
        assertThat(result.title).isEqualTo(ACTOR_MOVIE_DTO.title)
        assertThat(result.imdbRating).isEqualTo(ACTOR_MOVIE_DTO.voteAverage)
        assertThat(result.releaseDate).isEqualTo(ACTOR_MOVIE_DTO.releaseDate)
        assertThat(result.overview).isEqualTo(ACTOR_MOVIE_DTO.overview)
        assertThat(result.posterPictureURL).isEqualTo(POSTER_URL + ACTOR_MOVIE_DTO.posterPath)
        assertThat(result.genres).isEmpty()
        assertThat(result.userRating).isNull()
        assertThat(result.runtimeMinutes).isEqualTo(0)
        assertThat(result.trailerURL).isEmpty()
    }

    @Test
    fun `should handle nulls and defaults correctly when mapping toDto`() {
        // Given
        val emptyActorMovieDto = ACTOR_MOVIE_DTO.copy(
            id = null,
            title = null,
            voteAverage = null,
            releaseDate = null,
            overview = null,
            posterPath = null
        )

        // When
        val result = emptyActorMovieDto.toDto()

        // Then
        assertThat(result.id).isEqualTo(0L)
        assertThat(result.title).isEqualTo("")
        assertThat(result.imdbRating).isEqualTo(0.0)
        assertThat(result.releaseDate).isEqualTo("0001-01-01")
        assertThat(result.overview).isEqualTo("")
        assertThat(result.posterPictureURL).isEqualTo("https://image.tmdb.org/t/p/w500")
    }

    companion object {
        const val POSTER_URL = "https://image.tmdb.org/t/p/w500"
        val ACTOR_MOVIE_DTO = ActorMovieDto(
            id = 123,
            title = "Inception",
            voteAverage = 8.8,
            releaseDate = "2010-07-16",
            overview = "A mind-bending thriller",
            posterPath = "/poster.jpg"
        )
    }
}
