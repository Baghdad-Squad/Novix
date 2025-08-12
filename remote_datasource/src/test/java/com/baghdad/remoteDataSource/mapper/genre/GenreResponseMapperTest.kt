package com.baghdad.remoteDataSource.mapper.genre

import com.baghdad.remoteDataSource.response.genre.GenreListResponse
import com.baghdad.repository.model.GenreDto
import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

class GenreResponseMapperTest {
    companion object {
        private val GENRE_TYPE = GenreDto.GenreType.MOVIE

        private val ACTION_GENRE = GenreListResponse.GenreItemDto(
            id = 1L,
            name = "Action"
        )

        private val DRAMA_GENRE = GenreListResponse.GenreItemDto(
            id = 2L,
            name = "Drama"
        )

        private val NULL_ID_GENRE = GenreListResponse.GenreItemDto(
            id = null,
            name = "Unknown"
        )

        private val EXPECTED_ACTION_DTO = GenreDto(
            id = 1L,
            name = "Action",
            type = GENRE_TYPE
        )

        private val EXPECTED_DRAMA_DTO = GenreDto(
            id = 2L,
            name = "Drama",
            type = GENRE_TYPE
        )

        private val EXPECTED_NULL_ID_DTO = GenreDto(
            id = -1L,
            name = "Unknown",
            type = GENRE_TYPE
        )
    }

    @Test
    fun `should map all genres to dto with given genre type`() {
        val response = GenreListResponse(
            genres = listOf(ACTION_GENRE, DRAMA_GENRE)
        )

        val result = response.toDto(GENRE_TYPE)

        assertThat(result).containsExactly(EXPECTED_ACTION_DTO, EXPECTED_DRAMA_DTO)
    }

    @Test
    fun `should map genre with null id to dto with default id`() {
        val response = GenreListResponse(
            genres = listOf(NULL_ID_GENRE)
        )

        val result = response.toDto(GENRE_TYPE)

        assertThat(result).containsExactly(EXPECTED_NULL_ID_DTO)
    }

    @Test
    fun `should return empty list when genres list is empty`() {
        val response = GenreListResponse(
            genres = emptyList()
        )

        val result = response.toDto(GENRE_TYPE)

        assertThat(result).isEmpty()
    }
}