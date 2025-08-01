package com.baghdad.remoteDataSource.mapper

import com.baghdad.remoteDataSource.mapper.movie.toDto
import com.baghdad.remoteDataSource.response.MovieResult
import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

class MovieResultMapperKtTest {

    @Test
    fun `should map fields correctly when valid data provided`() {
        val movieResult = MovieResult(
            id = 10,
            title = "Interstellar",
            genreIds = listOf(12, 18),
            voteAverage = 8.6,
            releaseDate = "2014-11-07",
            overview = "Space exploration",
            posterPath = "/poster.jpg"
        )

        val movieDto = movieResult.toDto(movieResult.genreIds)

        assertThat(movieDto.id).isEqualTo(10L)
        assertThat(movieDto.title).isEqualTo("Interstellar")
        assertThat(movieDto.genres).hasSize(2)
        assertThat(movieDto.imdbRating).isEqualTo(8.6)
        assertThat(movieDto.posterPictureURL).isEqualTo("https://image.tmdb.org/t/p/w500/poster.jpg")
    }

    @Test
    fun `should use defaults when fields are null`() {
        val movieResult = MovieResult(
            id = null,
            title = null,
            genreIds = null,
            voteAverage = null,
            releaseDate = null,
            overview = null,
            posterPath = null
        )

        val movieDto = movieResult.toDto()

        assertThat(movieDto.id).isEqualTo(0L)
        assertThat(movieDto.title).isEqualTo("Untitled")
        assertThat(movieDto.genres).isEmpty()
        assertThat(movieDto.releaseDate).isEqualTo("0001-01-01")
        assertThat(movieDto.overview).isEqualTo("No overview available.")
        assertThat(movieDto.posterPictureURL).isEmpty()
    }

    @Test
    fun `should use empty genres when genreIds parameter not passed`() {
        val movieResult = MovieResult(
            id = 30,
            title = "Default GenreIds",
            genreIds = listOf(99, 100),
            voteAverage = 5.5,
            releaseDate = "2023-03-03",
            overview = "Testing defaults",
            posterPath = "/poster3.jpg"
        )

        val movieDto = movieResult.toDto()

        assertThat(movieDto.genres).isEmpty()
    }
}