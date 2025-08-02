package com.baghdad.remoteDataSource.response.movie

import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

class MovieDetailsResponseTest {

    @Test
    fun `should create MovieDetailsResponse with full data when all fields are provided`() {
        // Given
        val expectedGenres = listOf(
            Genre(id = 1L, name = "Action"),
            Genre(id = 2L, name = "Sci-Fi")
        )

        // When
        val response = MovieDetailsResponse(
            id = 101L,
            title = "Inception",
            genres = expectedGenres,
            voteAverage = 8.8,
            releaseDate = "2010-07-16",
            overview = "A thief who steals corporate secrets...",
            posterPath = "/inception.jpg",
            runtime = 148
        )

        // Then
        assertThat(response.id).isEqualTo(101L)
        assertThat(response.title).isEqualTo("Inception")
        assertThat(response.genres).hasSize(2)
        assertThat(response.genres!![0].name).isEqualTo("Action")
        assertThat(response.voteAverage).isEqualTo(8.8)
        assertThat(response.releaseDate).isEqualTo("2010-07-16")
        assertThat(response.overview).isEqualTo("A thief who steals corporate secrets...")
        assertThat(response.posterPath).isEqualTo("/inception.jpg")
        assertThat(response.runtime).isEqualTo(148)
    }

    @Test
    fun `should create MovieDetailsResponse with default values when no fields are provided`() {
        // Given & When
        val response = MovieDetailsResponse()

        // Then
        assertThat(response.id).isNull()
        assertThat(response.title).isNull()
        assertThat(response.genres).isNull()
        assertThat(response.voteAverage).isNull()
        assertThat(response.releaseDate).isNull()
        assertThat(response.overview).isNull()
        assertThat(response.posterPath).isNull()
        assertThat(response.runtime).isNull()
    }

    @Test
    fun `should create Genre with default values when no fields are provided`() {
        // Given & When
        val genre = Genre()

        // Then
        assertThat(genre.id).isNull()
        assertThat(genre.name).isNull()
    }

    @Test
    fun `should create MovieDetailsResponse with empty genres list when genres provided as empty`() {
        // Given
        val response = MovieDetailsResponse(
            id = 200L,
            title = "Empty Genres Movie",
            genres = emptyList()
        )

        // Then
        assertThat(response.id).isEqualTo(200L)
        assertThat(response.title).isEqualTo("Empty Genres Movie")
        assertThat(response.genres).isEmpty()
    }

    @Test
    fun `should create MovieDetailsResponse with partial data when some fields are provided`() {
        // When
        val response = MovieDetailsResponse(
            title = "Avatar",
            voteAverage = 7.9
        )

        // Then
        assertThat(response.id).isNull()
        assertThat(response.title).isEqualTo("Avatar")
        assertThat(response.voteAverage).isEqualTo(7.9)
        assertThat(response.genres).isNull()
        assertThat(response.runtime).isNull()
    }

    @Test
    fun `should create MovieDetailsResponse with multiple genres`() {
        // Given
        val expectedGenres = listOf(
            Genre(id = 3L, name = "Drama"),
            Genre(id = 4L, name = "Thriller"),
            Genre(id = 5L, name = "Mystery")
        )

        // When
        val response = MovieDetailsResponse(
            id = 300L,
            title = "Drama Thriller",
            genres = expectedGenres
        )

        // Then
        assertThat(response.id).isEqualTo(300L)
        assertThat(response.title).isEqualTo("Drama Thriller")
        assertThat(response.genres).hasSize(3)
        assertThat(response.genres!![2].name).isEqualTo("Mystery")
    }
}
