package com.baghdad.local_datasource.entity

import com.baghdad.local_datasource.roomDB.entity.Genre
import com.baghdad.local_datasource.roomDB.entity.Movie
import com.baghdad.local_datasource.roomDB.entity.toDto
import com.baghdad.local_datasource.roomDB.entity.toDtos
import com.baghdad.local_datasource.roomDB.entity.toLocalDto
import com.baghdad.repository.model.GenreDto
import com.baghdad.repository.model.MovieDto
import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

class MovieTest {

    @Test
    fun `should convert MovieDto to Movie when toLocalDto is called`() {
        // When
        val result = MOVIE_DTO.toLocalDto()

        // Then
        assertThat(result.genres).containsExactly(1L, 2L)
        assertThat(result.title).isEqualTo("Test Movie")
    }

    @Test
    fun `should convert Movie to MovieDto when toDto is called`() {
        // When
        val result = MOVIE.toDto(listOf(genreDto1, genreDto2))

        // Then
        assertThat(result.genres).containsExactly(genreDto1, genreDto2)
        assertThat(result.trailerURL).isEmpty()
    }

    @Test
    fun `should convert list of Movies to MovieDtos when toDtos is called`() {
        // Given
        val movies = listOf(MOVIE, MOVIE.copy(id = 200L, genres = listOf(2L), title = "Movie 2"))
        val genresMap = mapOf(1L to genre1, 2L to genre2)

        // When
        val result = movies.toDtos(genresMap)

        // Then
        assertThat(result).hasSize(2)
        assertThat(result[0].genres).containsExactly(genreDto1)
        assertThat(result[1].genres).containsExactly(genreDto2)
    }

    @Test
    fun `should handle empty genres list when converting from MovieDto`() {
        // Given
        val movieDto = MOVIE_DTO.copy(genres = emptyList())

        // When
        val result = movieDto.toLocalDto()

        // Then
        assertThat(result.genres).isEmpty()
    }

    @Test
    fun `should handle empty genres list when converting from Movie`() {
        // Given
        val movie = MOVIE.copy(genres = emptyList())

        // When
        val result = movie.toDto(emptyList())

        // Then
        assertThat(result.genres).isEmpty()
    }

    @Test
    fun `should ignore missing genres when toDtos is called`() {
        // Given
        val movie = MOVIE.copy(genres = listOf(1L, 99L))
        val genresMap = mapOf(1L to genre1)

        // When
        val result = listOf(movie).toDtos(genresMap)

        // Then
        assertThat(result[0].genres).containsExactly(genreDto1)
    }

    @Test
    fun `should preserve all non-genre fields when converting MovieDto to Movie`() {
        // When
        val result = MOVIE_DTO.toLocalDto()

        // Then
        assertThat(result.id).isEqualTo(MOVIE_DTO.id)
        assertThat(result.title).isEqualTo(MOVIE_DTO.title)
        assertThat(result.runtimeMinutes).isEqualTo(MOVIE_DTO.runtimeMinutes)
        assertThat(result.posterPictureURL).isEqualTo(MOVIE_DTO.posterPictureURL)
    }

    companion object {
        private val genreDto1 = GenreDto(1L, "Action", GenreDto.GenreType.MOVIE)
        private val genreDto2 = GenreDto(2L, "Comedy", GenreDto.GenreType.MOVIE)
        private val genre1 = Genre(1L, "Action", "MOVIE")
        private val genre2 = Genre(2L, "Comedy", "MOVIE")

        val MOVIE_DTO = MovieDto(
            id = 101L,
            title = "Test Movie",
            genres = listOf(genreDto1, genreDto2),
            imdbRating = 7.5,
            userRating = 4.2,
            releaseDate = "2023-01-01",
            overview = "Test overview",
            posterPictureURL = "poster.jpg",
            runtimeMinutes = 120,
            trailerURL = ""
        )

        val MOVIE = Movie(
            id = 102L,
            title = "Another Movie",
            genres = listOf(1L),
            imdbRating = 8.0,
            userRating = 4.5,
            releaseDate = "2023-02-01",
            overview = "Another overview",
            posterPictureURL = "another_poster.jpg",
            runtimeMinutes = 90
        )
    }
}
