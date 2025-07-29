package com.baghdad.remoteDataSource.mapper


import com.baghdad.repository.model.GenreDto
import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

class GenreMapperTest {

    private val availableGenres = listOf(
        GenreDto(id = 1L, name = "Action",type = GenreDto.GenreType.MOVIE),
        GenreDto(id = 2L, name = "Comedy", type = GenreDto.GenreType.MOVIE),
        GenreDto(id = 3L, name = "Drama", type = GenreDto.GenreType.TV_SHOW),
    )

    @Test
    fun `mapGenreIdsToGenres should return matching genres only`() {
        val genreIds = listOf(1, 3)

        val result = mapGenreIdsToGenres(genreIds, availableGenres)

        assertThat(result).containsExactly(
            GenreDto(1L, "Action",type = GenreDto.GenreType.MOVIE),
            GenreDto(3L, "Drama", type = GenreDto.GenreType.TV_SHOW)
        )
    }

    @Test
    fun `mapGenreIdsToGenres should return empty list if genreIds is empty`() {
        val result = mapGenreIdsToGenres(emptyList(), availableGenres)

        assertThat(result).isEmpty()
    }

    @Test
    fun `mapGenreIdsToGenres should return empty list if availableGenres is null`() {
        val result = mapGenreIdsToGenres(listOf(1, 2), null)

        assertThat(result).isEmpty()
    }

    @Test
    fun `mapGenreIdsToGenres should skip unmatched ids`() {
        val genreIds = listOf(1, 99, 2)

        val result = mapGenreIdsToGenres(genreIds, availableGenres)

        assertThat(result).containsExactly(
            GenreDto(1L, "Action",GenreDto.GenreType.MOVIE),
            GenreDto(2L, "Comedy", GenreDto.GenreType.MOVIE)
        )
    }

    @Test
    fun `mapGenreIdsToGenres should preserve order of ids`() {
        val genreIds = listOf(3, 1)

        val result = mapGenreIdsToGenres(genreIds, availableGenres)

        assertThat(result).containsExactly(
            GenreDto(3L, "Drama",GenreDto.GenreType.TV_SHOW),
            GenreDto(1L, "Action",GenreDto.GenreType.MOVIE)
        ).inOrder()
    }
}
