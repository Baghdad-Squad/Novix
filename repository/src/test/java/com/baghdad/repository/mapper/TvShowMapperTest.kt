package com.baghdad.repository.mapper

import com.baghdad.repository.model.SearchQueryDto
import com.baghdad.repository.model.TvShowDto
import com.google.common.truth.Truth.assertThat
import kotlinx.datetime.LocalDate
import org.junit.jupiter.api.Test

class TvShowMapperTest {

    @Test
    fun `TvShowDto toEntity should map correctly with valid data`() {
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

    @Test
    fun `TvShowDto toEntity should handle empty release date with default value`() {
        // Given
        val tvShowDto = createMockTvShowDto().copy(releaseDate = "")

        // When
        val result = tvShowDto.toEntity()

        // Then
        assertThat(result.releaseDate).isEqualTo(LocalDate(1990, 1, 1))
    }

    @Test
    fun `TvShowDto toEntity should handle blank release date with default value`() {
        // Given
        val tvShowDto = createMockTvShowDto().copy(releaseDate = "   ")

        // When
        val result = tvShowDto.toEntity()

        // Then
        assertThat(result.releaseDate).isEqualTo(LocalDate(1990, 1, 1))
    }

    @Test
    fun `TvShowDto toEntity should handle null user rating`() {
        // Given
        val tvShowDto = createMockTvShowDto().copy(userRating = null)

        // When
        val result = tvShowDto.toEntity()

        // Then
        assertThat(result.userRating).isNull()
    }

    @Test
    fun `TvShowDto toEntity should handle empty genres list`() {
        // Given
        val tvShowDto = createMockTvShowDto().copy(genres = emptyList())

        // When
        val result = tvShowDto.toEntity()

        // Then
        assertThat(result.genres).isEmpty()
    }

    @Test
    fun `TvShowDto toEntity should handle empty header images list`() {
        // Given
        val tvShowDto = createMockTvShowDto().copy(headerImagesURLs = emptyList())

        // When
        val result = tvShowDto.toEntity()

        // Then
        assertThat(result.headerImagesURLs).isEmpty()
    }

    @Test
    fun `TvShowDto toEntity should handle multiple genres`() {
        // Given
        val tvShowDto = createMockTvShowDto().copy(
            genres = listOf(
                createMockGenreDto(35L, "Comedy"),
                createMockGenreDto(18L, "Drama"),
                createMockGenreDto(10751L, "Family")
            )
        )

        // When
        val result = tvShowDto.toEntity()

        // Then
        assertThat(result.genres.size).isEqualTo(3)
        assertThat(result.genres[0].id).isEqualTo(35L)
        assertThat(result.genres[0].name).isEqualTo("Comedy")
        assertThat(result.genres[1].id).isEqualTo(18L)
        assertThat(result.genres[1].name).isEqualTo("Drama")
        assertThat(result.genres[2].id).isEqualTo(10751L)
        assertThat(result.genres[2].name).isEqualTo("Family")
    }

    @Test
    fun `TvShowDto toSearchQueryDto should map correctly`() {
        // Given
        val tvShowDto = createMockTvShowDto()
        val query = "test tv show query"

        // When
        val result = tvShowDto.toSearchQueryDto(query)

        // Then
        assertThat(result.queryName).isEqualTo(query)
        assertThat(result.mediaId).isEqualTo(tvShowDto.id)
        assertThat(result.mediaType).isEqualTo(SearchQueryDto.MediaType.TV_SHOW)
    }

    @Test
    fun `TvShowDto toSearchQueryDto should handle different queries`() {
        // Given
        val tvShowDto = createMockTvShowDto()
        val query1 = "comedy series"
        val query2 = "drama shows"

        // When
        val result1 = tvShowDto.toSearchQueryDto(query1)
        val result2 = tvShowDto.toSearchQueryDto(query2)

        // Then
        assertThat(result1.queryName).isEqualTo(query1)
        assertThat(result1.mediaId).isEqualTo(tvShowDto.id)
        assertThat(result1.mediaType).isEqualTo(SearchQueryDto.MediaType.TV_SHOW)
        assertThat(result2.queryName).isEqualTo(query2)
        assertThat(result2.mediaId).isEqualTo(tvShowDto.id)
        assertThat(result2.mediaType).isEqualTo(SearchQueryDto.MediaType.TV_SHOW)
    }

    companion object {
        private fun createMockTvShowDto() = TvShowDto(
            id = 123L,
            title = "Test TV Show",
            genres = listOf(createMockGenreDto(35L, "Comedy")),
            imdbRating = 8.5,
            userRating = 8.0,
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