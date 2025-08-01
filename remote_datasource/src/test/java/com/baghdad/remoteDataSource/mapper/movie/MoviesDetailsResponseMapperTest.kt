package com.baghdad.remoteDataSource.mapper.movie

import com.baghdad.remoteDataSource.response.movie.Genre
import com.baghdad.remoteDataSource.response.movie.MovieDetailsResponse
import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

class MoviesDetailsResponseMapperTest {

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
            posterPath = "/darkKnight.jpg",
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

    @Test
    fun `should return null when id is null`() {
        // Given
        val genre = Genre(id = null, name = "Action")

        // When
        val result = genre.toGenreDto()

        // Then
        assertThat(result).isNull()
    }

    @Test
    fun `should return null when name is null`() {
        // Given
        val genre = Genre(id = 1L, name = null)

        // When
        val result = genre.toGenreDto()

        // Then
        assertThat(result).isNull()
    }

    @Test
    fun `should return null when name is blank`() {
        // Given
        val genre = Genre(id = 2L, name = "   ")

        // When
        val result = genre.toGenreDto()

        // Then
        assertThat(result).isNull()
    }

    @Test
    fun `should return GenreDto when id and name are valid`() {
        // Given
        val genre = Genre(id = 3L, name = "Comedy")

        // When
        val result = genre.toGenreDto()

        // Then
        assertThat(result).isNotNull()
        assertThat(result?.id).isEqualTo(3L)
        assertThat(result?.name).isEqualTo("Comedy")
    }
}