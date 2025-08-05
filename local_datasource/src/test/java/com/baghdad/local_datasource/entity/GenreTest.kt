//package com.baghdad.local_datasource.entity
//
//import com.baghdad.local_datasource.roomDB.entity.Genre
//import com.baghdad.local_datasource.roomDB.entity.toDto
//import com.baghdad.repository.model.GenreDto
//import com.google.common.truth.Truth.assertThat
//import org.junit.jupiter.api.Assertions.assertThrows
//import org.junit.jupiter.api.Test
//
//class GenreTest {
//
//    @Test
//    fun `should convert Genre to GenreDto with all fields correctly`() {
//        // When
//        val result = GENRE.toDto()
//
//        // Then
//        assertThat(result.id).isEqualTo(1L)
//        assertThat(result.name).isEqualTo("Action")
//        assertThat(result.type).isEqualTo(GenreDto.GenreType.MOVIE)
//    }
//
//    @Test
//    fun `should handle boundary id values when toDto is called`() {
//        // Given
//        val maxLongGenre = Genre(Long.MAX_VALUE, "Max", "MOVIE")
//        val minLongGenre = Genre(Long.MIN_VALUE, "Min", "TV_SHOW")
//
//        // When
//        val maxResult = maxLongGenre.toDto()
//        val minResult = minLongGenre.toDto()
//
//        // Then
//        assertThat(maxResult.id).isEqualTo(Long.MAX_VALUE)
//        assertThat(minResult.id).isEqualTo(Long.MIN_VALUE)
//    }
//
//    @Test
//    fun `should handle zero and negative id values when toDto is called`() {
//        // Given
//        val zeroGenre = GENRE.copy(id = 0L)
//        val negativeGenre = GENRE.copy(id = -123L)
//
//        // When
//        val zeroResult = zeroGenre.toDto()
//        val negativeResult = negativeGenre.toDto()
//
//        // Then
//        assertThat(zeroResult.id).isEqualTo(0L)
//        assertThat(negativeResult.id).isEqualTo(-123L)
//    }
//
//    @Test
//    fun `should throw exception when toDto is called with invalid GenreType`() {
//        // Given
//        val invalidGenre = GENRE.copy(type = "INVALID_TYPE")
//
//        // Then
//        assertThrows(IllegalArgumentException::class.java) {
//            invalidGenre.toDto()
//        }
//    }
//
//    @Test
//    fun `should preserve name and id fields when toDto is called`() {
//        // Given
//        val genre = GENRE.copy(name = "Science Fiction", id = 42L)
//
//        // When
//        val result = genre.toDto()
//
//        // Then
//        assertThat(result.id).isEqualTo(42L)
//        assertThat(result.name).isEqualTo("Science Fiction")
//    }
//
//    @Test
//    fun `should handle empty name field when toDto is called`() {
//        // Given
//        val emptyNameGenre = Genre(5L, "", "MOVIE")
//
//        // When
//        val result = emptyNameGenre.toDto()
//
//        // Then
//        assertThat(result.name).isEmpty()
//    }
//
//    companion object {
//        val GENRE = Genre(
//            id = 1L,
//            name = "Action",
//            type = "MOVIE"
//        )
//    }
//}
