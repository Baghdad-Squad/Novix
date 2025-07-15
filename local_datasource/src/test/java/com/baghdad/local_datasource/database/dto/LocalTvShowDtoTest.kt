package com.baghdad.local_datasource.database.dto

import com.baghdad.repository.model.GenreDto
import com.baghdad.repository.model.TvShowDto
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class LocalTvShowDtoTest {


    @Test
    fun `toDto   Happy path conversion from LocalTvShowDto to TvShowDto`() {
        val dto = localTvShow.toDto()

        assertEquals(localTvShow.id, dto.id)
        assertEquals(localTvShow.title, dto.title)
        assertEquals(emptyList<GenreDto>(), dto.genres)
        assertEquals(localTvShow.imdbRating, dto.imdbRating, 0.001)
        assertEquals(localTvShow.userRating, dto.userRating)
        assertEquals(localTvShow.releaseDate, dto.releaseDate)
        assertEquals(localTvShow.overview, dto.overview)
        assertEquals(localTvShow.posterPictureURL, dto.posterPictureURL)
        assertEquals(localTvShow.numberOfSeasons, dto.numberOfSeasons)
    }

    @Test
    fun `toEntity   Happy path conversion from TvShowDto to LocalTvShowDto`() {
        val entity = tvShowDto.toEntity()

        assertEquals(tvShowDto.id, entity.id)
        assertEquals(tvShowDto.title, entity.title)
        assertEquals(emptyList<String>(), entity.genres)
        assertEquals(tvShowDto.imdbRating, entity.imdbRating, 0.001)
        assertEquals(tvShowDto.userRating, entity.userRating)
        assertEquals(tvShowDto.releaseDate, entity.releaseDate)
        assertEquals(tvShowDto.overview, entity.overview)
        assertEquals(tvShowDto.posterPictureURL, entity.posterPictureURL)
        assertEquals(tvShowDto.numberOfSeasons, entity.numberOfSeasons)
    }

    private val localTvShow = LocalTvShowDto(
        id = 301L,
        title = "Neon Pulse",
        genres = listOf("Sci-Fi", "Drama"),
        imdbRating = 8.7,
        userRating = 9.2,
        releaseDate = "2025-08-19",
        overview = "A retro hacker rewires society from his bedroom.",
        posterPictureURL = "https://example.com/neon.jpg",
        numberOfSeasons = 3
    )

    private val tvShowDto = TvShowDto(
        id = 302L,
        title = "Shadow Circuit",
        genres = listOf(),
        imdbRating = 7.9,
        userRating = null,
        releaseDate = "2024-03-05",
        overview = "Rebellion brews inside a neural surveillance system.",
        posterPictureURL = "https://example.com/shadow.jpg",
        numberOfSeasons = 2
    )

}
