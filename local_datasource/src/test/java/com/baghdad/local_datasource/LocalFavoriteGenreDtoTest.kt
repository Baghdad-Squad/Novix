package com.baghdad.local_datasource

import com.baghdad.local_datasource.roomDB.entity.LocalFavoriteGenreDto
import com.baghdad.local_datasource.roomDB.entity.toDto
import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

class LocalFavoriteGenreDtoTest {

    @Test
    fun `should create LocalFavoriteGenreDto and check fields`() {
        // Then
        assertThat(LOCAL_FAVORITE_GENRE_DTO.genreId).isEqualTo(1L)
        assertThat(LOCAL_FAVORITE_GENRE_DTO.name).isEqualTo("Action")
        assertThat(LOCAL_FAVORITE_GENRE_DTO.count).isEqualTo(5)
        assertThat(LOCAL_FAVORITE_GENRE_DTO.timeStamp).isEqualTo(123456789L)
    }

    @Test
    fun `should show default values are applied correctly`() {
        // Given
        val dto = LocalFavoriteGenreDto(
            genreId = 2L,
            name = "Comedy"
        )

        // Then
        assertThat(dto.genreId).isEqualTo(2L)
        assertThat(dto.name).isEqualTo("Comedy")
        assertThat(dto.count).isEqualTo(0)
        assertThat(dto.timeStamp).isGreaterThan(0L)
    }

    @Test
    fun `should create LocalFavoriteGenreDto from DTO`() {
        // When
        val result = LOCAL_FAVORITE_GENRE_DTO.toDto()

        // Then
        assertThat(result.genreId).isEqualTo(LOCAL_FAVORITE_GENRE_DTO.genreId)
        assertThat(result.name).isEqualTo(LOCAL_FAVORITE_GENRE_DTO.name)
        assertThat(result.count).isEqualTo(LOCAL_FAVORITE_GENRE_DTO.count)
        assertThat(result.timeStamp).isEqualTo(LOCAL_FAVORITE_GENRE_DTO.timeStamp)
    }

    companion object {
        val LOCAL_FAVORITE_GENRE_DTO = LocalFavoriteGenreDto(
            genreId = 1L,
            name = "Action",
            count = 5,
            timeStamp = 123456789L
        )
    }
}
