package com.baghdad.remoteDataSource.mapper

import com.baghdad.remoteDataSource.mapper.movie.toDto
import com.baghdad.remoteDataSource.response.movie.Genre
import com.baghdad.remoteDataSource.response.movie.MovieDetailsResponse
import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

class MoviesDetailsResponseMapperKtTest {

    private val url = "https://image.tmdb.org/t/p/w500"

    @Test
    fun `should map fields correctly when MovieDetailsResponse has valid data`() {
        // Given
        val response = MovieDetailsResponse(
            id = 42,
            title = "The Dark Knight",
            genres = listOf(Genre(id = 28, name = "Action")),
            voteAverage = 9.0,
            releaseDate = "2008-07-18",
            overview = "Batman vs Joker",
            posterPath = "/darkknight.jpg",
            runtime = 152
        )

        // When
        val dto = response.toDto(userRating = 8.5)

        // Then
        assertThat(dto.id).isEqualTo(response.id)
        assertThat(dto.title).isEqualTo(response.title)
        assertThat(dto.genres).hasSize(response.genres?.size ?: 0)
        assertThat(dto.genres.first().name).isEqualTo(response.genres?.first()?.name)
        assertThat(dto.imdbRating).isEqualTo(response.voteAverage)
        assertThat(dto.userRating).isEqualTo(8.5)
        assertThat(dto.runtimeMinutes).isEqualTo(response.runtime)
        assertThat(dto.posterPictureURL).isEqualTo(url + response.posterPath)
    }

    @Test
    fun `should use defaults when fields are null`() {
        // Given
        val response = MovieDetailsResponse(
            id = null,
            title = null,
            genres = null,
            voteAverage = null,
            releaseDate = null,
            overview = null,
            posterPath = null,
            runtime = null
        )

        // When
        val dto = response.toDto()

        // Then
        assertThat(dto.id).isEqualTo(0)
        assertThat(dto.title).isEqualTo("Untitled")
        assertThat(dto.genres).isEqualTo(emptyList<Genre>())
        assertThat(dto.imdbRating).isEqualTo(0.0)
        assertThat(dto.releaseDate).isEqualTo("Unknown")
        assertThat(dto.overview).isEqualTo("No overview available.")
        assertThat(dto.posterPictureURL).isEmpty()
        assertThat(dto.runtimeMinutes).isEqualTo(0)
        assertThat(dto.userRating).isEqualTo(response.voteAverage)
    }
}