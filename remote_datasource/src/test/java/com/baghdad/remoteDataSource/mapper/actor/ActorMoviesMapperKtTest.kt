package com.baghdad.remoteDataSource.mapper.actor

import com.baghdad.remoteDataSource.response.actor.ActorMovieDto
import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

class ActorMovieMapperTest {

    @Test
    fun `toDto should map fields correctly`() {
        val dto = ActorMovieDto(
            id = 123,
            title = "Inception",
            voteAverage = 8.8,
            releaseDate = "2010-07-16",
            overview = "A mind-bending thriller",
            posterPath = "/poster.jpg"
        )

        val result = dto.toDto()

        assertThat(result.id).isEqualTo(123L)
        assertThat(result.title).isEqualTo("Inception")
        assertThat(result.imdbRating).isEqualTo(8.8)
        assertThat(result.releaseDate).isEqualTo("2010-07-16")
        assertThat(result.overview).isEqualTo("A mind-bending thriller")
        assertThat(result.posterPictureURL).isEqualTo("https://image.tmdb.org/t/p/w500/poster.jpg")
        assertThat(result.genres).isEmpty()
        assertThat(result.userRating).isNull()
        assertThat(result.runtimeMinutes).isEqualTo(0)
        assertThat(result.trailerURL).isEqualTo("")
    }

    @Test
    fun `toDto should handle nulls and defaults correctly`() {
        val dto = ActorMovieDto(
            id = null,
            title = null,
            voteAverage = null,
            releaseDate = null,
            overview = null,
            posterPath = null
        )

        val result = dto.toDto()

        assertThat(result.id).isEqualTo(0L)
        assertThat(result.title).isEqualTo("")
        assertThat(result.imdbRating).isEqualTo(0.0)
        assertThat(result.releaseDate).isEqualTo("0001-01-01")
        assertThat(result.overview).isEqualTo("")
        assertThat(result.posterPictureURL).isEqualTo("https://image.tmdb.org/t/p/w500")
    }
}
