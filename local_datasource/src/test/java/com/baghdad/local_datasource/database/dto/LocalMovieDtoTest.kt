package com.baghdad.local_datasource.database.dto

import com.baghdad.repository.model.GenreDto
import com.baghdad.repository.model.MovieDto
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class LocalMovieDtoTest {

    @Test
    fun `toEntity   Happy path conversion from MovieDto to LocalMovieDto`() {
        val entity = movieDto.toEntity()

        Assertions.assertEquals(movieDto.id, entity.id)
        Assertions.assertEquals(movieDto.title, entity.title)
        Assertions.assertEquals(emptyList<String>(), entity.genres)
        Assertions.assertEquals(movieDto.imdbRating, entity.imdbRating, 0.001)
        Assertions.assertEquals(movieDto.userRating, entity.userRating)
        Assertions.assertEquals(movieDto.releaseDate, entity.releaseDate)
        Assertions.assertEquals(movieDto.overview, entity.overview)
        Assertions.assertEquals(movieDto.posterPictureURL, entity.posterPictureURL)
        Assertions.assertEquals(movieDto.runtimeMinutes, entity.runtimeMinutes)
    }

    @Test
    fun `toDto   Happy path conversion from LocalMovieDto to MovieDto`() {
        val dto = localMovieDto.toDto()

        Assertions.assertEquals(localMovieDto.id, dto.id)
        Assertions.assertEquals(localMovieDto.title, dto.title)
        Assertions.assertEquals(emptyList<GenreDto>(), dto.genres)
        Assertions.assertEquals(localMovieDto.imdbRating, dto.imdbRating, 0.001)
        Assertions.assertEquals(localMovieDto.userRating, dto.userRating)
        Assertions.assertEquals(localMovieDto.releaseDate, dto.releaseDate)
        Assertions.assertEquals(localMovieDto.overview, dto.overview)
        Assertions.assertEquals(localMovieDto.posterPictureURL, dto.posterPictureURL)
        Assertions.assertEquals(localMovieDto.runtimeMinutes, dto.runtimeMinutes)
    }

    private val movieDto = MovieDto(
        id = 101L,
        title = "Echoes of Mars",
        genres = listOf(),
        imdbRating = 7.6,
        userRating = 8.2,
        releaseDate = "2024-11-15",
        overview = "Martian voices whisper in the void.",
        posterPictureURL = "https://example.com/echo.jpg",
        runtimeMinutes = 106
    )

    private val localMovieDto = LocalMovieDto(
        id = 101L,
        title = "Echoes of Mars",
        genres = listOf(),
        imdbRating = 7.6,
        userRating = 8.2,
        releaseDate = "2024-11-15",
        overview = "Martian voices whisper in the void.",
        posterPictureURL = "https://example.com/echo.jpg",
        runtimeMinutes = 106
    )
}