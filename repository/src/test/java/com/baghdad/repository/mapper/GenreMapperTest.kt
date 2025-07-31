package com.baghdad.repository.mapper

import com.baghdad.repository.model.GenreDto
import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

class GenreMapperTest {

    @Test
    fun `GenreDto toEntity should map correctly with valid data`() {
        // Given
        val genreDto = createMockGenreDto(28L, "Action")

        // When
        val result = genreDto.toEntity()

        // Then
        assertThat(result.id).isEqualTo(28L)
        assertThat(result.name).isEqualTo("Action")
    }

    @Test
    fun `GenreDto toEntity should handle different genre types`() {
        // Given
        val movieGenreDto = createMockGenreDto(28L, "Action")
        val tvShowGenreDto = GenreDto(35L, "Comedy", GenreDto.GenreType.TV_SHOW)

        // When
        val movieGenre = movieGenreDto.toEntity()
        val tvShowGenre = tvShowGenreDto.toEntity()

        // Then
        assertThat(movieGenre.id).isEqualTo(28L)
        assertThat(movieGenre.name).isEqualTo("Action")
        assertThat(tvShowGenre.id).isEqualTo(35L)
        assertThat(tvShowGenre.name).isEqualTo("Comedy")
    }

    @Test
    fun `List of GenreDto toEntities should map correctly`() {
        // Given
        val genreDtos = listOf(
            createMockGenreDto(28L, "Action"),
            createMockGenreDto(35L, "Comedy"),
            createMockGenreDto(18L, "Drama")
        )

        // When
        val result = genreDtos.toEntities()

        // Then
        assertThat(result.size).isEqualTo(3)
        assertThat(result[0].id).isEqualTo(28L)
        assertThat(result[0].name).isEqualTo("Action")
        assertThat(result[1].id).isEqualTo(35L)
        assertThat(result[1].name).isEqualTo("Comedy")
        assertThat(result[2].id).isEqualTo(18L)
        assertThat(result[2].name).isEqualTo("Drama")
    }

    @Test
    fun `Empty list of GenreDto toEntities should return empty list`() {
        // Given
        val genreDtos = emptyList<GenreDto>()

        // When
        val result = genreDtos.toEntities()

        // Then
        assertThat(result).isEmpty()
    }

    @Test
    fun `Single GenreDto in list toEntities should map correctly`() {
        // Given
        val genreDtos = listOf(createMockGenreDto(28L, "Action"))

        // When
        val result = genreDtos.toEntities()

        // Then
        assertThat(result.size).isEqualTo(1)
        assertThat(result[0].id).isEqualTo(28L)
        assertThat(result[0].name).isEqualTo("Action")
    }

    companion object {
        private fun createMockGenreDto(id: Long, name: String) = GenreDto(
            id = id,
            name = name,
            type = GenreDto.GenreType.MOVIE
        )
    }
} 