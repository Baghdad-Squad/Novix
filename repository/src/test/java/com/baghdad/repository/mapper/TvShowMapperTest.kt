package com.baghdad.repository.mapper

import com.baghdad.repository.model.TvShowDto
import com.google.common.truth.Truth.assertThat
import kotlinx.datetime.LocalDate
import org.junit.jupiter.api.Test

class TvShowMapperTest {

    @Test
    fun `should map TvShowDto to entity correctly when data is valid`() {
        // Given
        val tvShowDto = createMockTvShowDto()

        // When
        val result = tvShowDto.toEntity()

        // Then
        assertThat(result.id).isEqualTo(123L)
        assertThat(result.title).isEqualTo("Test TV Show")
        assertThat(result.genres.size).isEqualTo(1)
        assertThat(result.genres[0].id).isEqualTo(35L)
        assertThat(result.genres[0].name).isEqualTo("Comedy")
        assertThat(result.averageRating).isEqualTo(8.5)
        assertThat(result.userRating).isEqualTo(8.0)
        assertThat(result.releaseDate).isEqualTo(LocalDate.parse("2023-01-01"))
        assertThat(result.overview).isEqualTo("Test TV show overview")
        assertThat(result.posterImageURL).isEqualTo("/tv_show_poster.jpg")
        assertThat(result.headerImagesURLs.size).isEqualTo(2)
        assertThat(result.headerImagesURLs[0]).isEqualTo("/header1.jpg")
        assertThat(result.headerImagesURLs[1]).isEqualTo("/header2.jpg")
        assertThat(result.trailerURL).isEqualTo(" ")
        assertThat(result.numberOfSeasons).isEqualTo(3)
    }

    companion object {
        private fun createMockTvShowDto() = TvShowDto(
            id = 123L,
            title = "Test TV Show",
            genres = listOf(createMockGenreDto(35L, "Comedy")),
            imdbRating = 8.5,
            userRating = 8,
            releaseDate = "2023-01-01",
            overview = "Test TV show overview",
            posterPictureURL = "/tv_show_poster.jpg",
            headerImagesURLs = listOf("/header1.jpg", "/header2.jpg"),
            trailerURL = " ",
            numberOfSeasons = 3
        )

        private fun createMockGenreDto(id: Long, name: String) = com.baghdad.repository.model.GenreDto(
            id = id,
            name = name,
            type = com.baghdad.repository.model.GenreDto.GenreType.TV_SHOW
        )
    }
} 