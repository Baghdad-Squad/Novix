package com.baghdad.remoteDataSource.mapper.movie

import com.baghdad.remoteDataSource.response.MovieResult
import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

class MovieResultMapperTest {

    @Test
    fun `should map fields correctly when valid data provided`() {
        // Given
        val movieResult = MovieResult(
            id = 10,
            title = "Interstellar",
            genreIds = listOf(12, 18),
            voteAverage = 8.6,
            releaseDate = "2014-11-07",
            overview = "Space exploration",
            posterPath = "/poster.jpg"
        )

        // When
        val movieDto = movieResult.toDto(movieResult.genreIds)

        // Then
        assertThat(movieDto.id).isEqualTo(movieResult.id)
        assertThat(movieDto.title).isEqualTo(movieResult.title)
        assertThat(movieDto.genres).hasSize(2)
        assertThat(movieDto.imdbRating).isEqualTo(movieResult.voteAverage)
        assertThat(movieDto.posterPictureURL)
            .isEqualTo("https://image.tmdb.org/t/p/w500${movieResult.posterPath}")
    }

    @Test
    fun `should use defaults when fields are null`() {
        // Given
        val movieResult = MovieResult(
            id = null,
            title = null,
            genreIds = null,
            voteAverage = null,
            releaseDate = null,
            overview = null,
            posterPath = null
        )

        // When
        val movieDto = movieResult.toDto()

        // Then
        assertThat(movieDto.id).isEqualTo(0L)
        assertThat(movieDto.title).isEqualTo("Untitled")
        assertThat(movieDto.genres).isEmpty()
        assertThat(movieDto.releaseDate).isEqualTo("0001-01-01")
        assertThat(movieDto.overview).isEqualTo("No overview available.")
        assertThat(movieDto.posterPictureURL).isEmpty()
    }

    @Test
    fun `should use empty genres when genreIds parameter not passed`() {
        // Given
        val movieResult = MovieResult(
            id = 30,
            title = "Default GenreIds",
            genreIds = listOf(99, 100),
            voteAverage = 5.5,
            releaseDate = "2023-03-03",
            overview = "Testing defaults",
            posterPath = "/poster3.jpg"
        )

        // When
        val movieDto = movieResult.toDto()

        // Then
        assertThat(movieDto.genres).isEmpty()
    }
}