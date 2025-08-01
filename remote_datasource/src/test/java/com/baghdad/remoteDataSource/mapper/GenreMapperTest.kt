package com.baghdad.remoteDataSource.mapper


import com.baghdad.repository.model.GenreDto
import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

class GenreMapperTest {

    @Test
    fun `should return matching genres only when mapGenreIdsToGenres is called`() {
        // Given
        val genreIds = listOf(1, 3)

        // When
        val result = mapGenreIdsToGenres(genreIds, GENRES)

        // Then
        assertThat(result).hasSize(2)
        assertThat(result).containsExactly(
            GenreDto(1L, "Action", type = GenreDto.GenreType.MOVIE),
            GenreDto(3L, "Drama", type = GenreDto.GenreType.TV_SHOW)
        )
    }

    @Test
    fun `should return empty list when genreIds is empty`() {
        // When
        val result = mapGenreIdsToGenres(emptyList(), GENRES)

        // Then
        assertThat(result).isEmpty()
    }

    @Test
    fun `should return empty list when availableGenres is null`() {
        // When
        val result = mapGenreIdsToGenres(listOf(1, 2), null)

        // Then
        assertThat(result).isEmpty()
    }

    @Test
    fun `should skip unmatched ids when mapping genreIds to genres`() {
        // Given
        val genreIds = listOf(1, 99, 2)

        // When
        val result = mapGenreIdsToGenres(genreIds, GENRES)

        // Then
        assertThat(result).hasSize(2)
        assertThat(result).containsExactly(
            GenreDto(1L, "Action", GenreDto.GenreType.MOVIE),
            GenreDto(2L, "Comedy", GenreDto.GenreType.MOVIE)
        )
    }

    @Test
    fun `should preserve order of ids when mapping genreIds to genres`() {
        // Given
        val genreIds = listOf(3, 1)

        // When
        val result = mapGenreIdsToGenres(genreIds, GENRES)

        // Then
        assertThat(result).containsExactly(
            GenreDto(3L, "Drama", GenreDto.GenreType.TV_SHOW),
            GenreDto(1L, "Action", GenreDto.GenreType.MOVIE)
        ).inOrder()
    }

    companion object {
        private val GENRES = listOf(
            GenreDto(id = 1L, name = "Action", type = GenreDto.GenreType.MOVIE),
            GenreDto(id = 2L, name = "Comedy", type = GenreDto.GenreType.MOVIE),
            GenreDto(id = 3L, name = "Drama", type = GenreDto.GenreType.TV_SHOW),
        )

    }
}
