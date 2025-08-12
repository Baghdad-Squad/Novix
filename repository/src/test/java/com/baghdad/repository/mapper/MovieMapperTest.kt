package com.baghdad.repository.mapper

import com.baghdad.entity.media.Genre
import com.baghdad.entity.media.Movie
import com.baghdad.repository.dummyData.DummyDataFactory.createMockMovieDto
import com.google.common.truth.Truth.assertThat
import kotlinx.datetime.LocalDate
import org.junit.jupiter.api.Test

class MovieMapperTest {
    @Test
    fun `toEntity should return Movie entity when given valid MovieDto`() {
        // Given
        val movieDto = createMockMovieDto().first()

        // When
        val result = movieDto.toEntity()

        // Then
        val expectedResult = Movie(
            id = 1L,
            title = "Test Movie",
            genres = listOf(
                Genre(id = 1L, name = "Action"),
                Genre(id = 2L, name = "Adventure")
            ),
            averageRating = 8.0,
            userRating = 7.5,
            releaseDate = LocalDate.parse("2023-01-01"),
            overview = "This is a test movie overview.",
            posterImageURL = "/movie_poster.jpg",
            trailerURL = "https://youtube.com/watch?v=test_movie_trailer",
            runtimeMinutes = 120
        )

        assertThat(result).isEqualTo(expectedResult)
    }
}