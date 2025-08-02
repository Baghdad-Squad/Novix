package com.baghdad.viewmodel.trendingMovie

import com.baghdad.entity.media.Genre
import com.baghdad.entity.media.Movie
import com.google.common.truth.Truth.assertThat
import kotlinx.datetime.LocalDate
import org.junit.jupiter.api.Test

class TrendingMovieMapperTest {

    @Test
    fun `should map movie correctly when movie has full valid data`() {
        val result = testMovieFull.toMovieUiState()

        assertThat(result.id).isEqualTo(123L)
        assertThat(result.posterPictureURL).isEqualTo("https://example.com/poster.jpg")
        assertThat(result.isSaved).isFalse()
    }

    @Test
    fun `should map movie correctly when id is zero`() {
        val result = testMovieZeroId.toMovieUiState()

        assertThat(result.id).isEqualTo(0L)
        assertThat(result.posterPictureURL).isEmpty()
        assertThat(result.isSaved).isFalse()
    }

    @Test
    fun `should map movie correctly when id is negative`() {
        val result = testMovieNegativeId.toMovieUiState()

        assertThat(result.id).isEqualTo(-1L)
        assertThat(result.posterPictureURL).isEqualTo("https://example.com/negative.jpg")
        assertThat(result.isSaved).isFalse()
    }

    @Test
    fun `should map movie correctly when poster URL is empty`() {
        val result = testMovieEmptyPoster.toMovieUiState()

        assertThat(result.id).isEqualTo(456L)
        assertThat(result.posterPictureURL).isEmpty()
        assertThat(result.isSaved).isFalse()
    }

    @Test
    fun `should map movie correctly when poster URL is very long`() {
        val result = testMovieLongUrl.toMovieUiState()

        assertThat(result.id).isEqualTo(789L)
        assertThat(result.posterPictureURL).isEqualTo(longUrl)
        assertThat(result.isSaved).isFalse()
    }

    @Test
    fun `should map genre correctly when genre has full valid data`() {
        val result = genreFull.toGenreUiState()

        assertThat(result.id).isEqualTo(28L)
        assertThat(result.name).isEqualTo("Action")
    }

    @Test
    fun `should map genre correctly when id is zero`() {
        val result = genreZeroId.toGenreUiState()

        assertThat(result.id).isEqualTo(0L)
        assertThat(result.name).isEqualTo("Unknown")
    }

    @Test
    fun `should map genre correctly when id is negative`() {
        val result = genreNegativeId.toGenreUiState()

        assertThat(result.id).isEqualTo(-5L)
        assertThat(result.name).isEqualTo("Invalid Genre")
    }

    @Test
    fun `should map genre correctly when name is empty`() {
        val result = genreEmptyName.toGenreUiState()

        assertThat(result.id).isEqualTo(12L)
        assertThat(result.name).isEmpty()
    }

    @Test
    fun `should map genre correctly when name has special characters`() {
        val result = genreSpecialChar.toGenreUiState()

        assertThat(result.id).isEqualTo(35L)
        assertThat(result.name).isEqualTo("Sci-Fi & Fantasy")
    }

    @Test
    fun `should map genre correctly when name is very long`() {
        val result = genreLongName.toGenreUiState()

        assertThat(result.id).isEqualTo(99L)
        assertThat(result.name).isEqualTo(longName)
    }

    @Test
    fun `should map genre correctly when name is unicode characters`() {
        val result = genreUnicode.toGenreUiState()

        assertThat(result.id).isEqualTo(40L)
        assertThat(result.name).isEqualTo("アクション")
    }

    @Test
    fun `should return different instances when mapping multiple movies`() {
        val result1 = movie1.toMovieUiState()
        val result2 = movie2.toMovieUiState()

        assertThat(result1.id).isEqualTo(1L)
        assertThat(result1.posterPictureURL).isEqualTo("https://example.com/poster1.jpg")
        assertThat(result2.id).isEqualTo(2L)
        assertThat(result2.posterPictureURL).isEqualTo("https://example.com/poster2.jpg")
        assertThat(result1).isNotSameInstanceAs(result2)
    }

    @Test
    fun `should return different instances when mapping multiple genres`() {
        val result1 = genre1.toGenreUiState()
        val result2 = genre2.toGenreUiState()

        assertThat(result1.id).isEqualTo(1L)
        assertThat(result1.name).isEqualTo("Action")
        assertThat(result2.id).isEqualTo(2L)
        assertThat(result2.name).isEqualTo("Comedy")
        assertThat(result1).isNotSameInstanceAs(result2)
    }

    companion object {
        private val longUrl = "https://example.com/" + "very-long-path/".repeat(50) + "poster.jpg"
        private val longName = "Very Long Genre Name ".repeat(20).trim()

        val testMovieFull = Movie(
            id = 123L,
            title = "Test Movie",
            genres = emptyList(),
            averageRating = 8.5,
            userRating = 9.0,
            releaseDate = LocalDate(2023, 12, 25),
            overview = "Test overview",
            posterImageURL = "https://example.com/poster.jpg",
            trailerURL = "https://example.com/trailer.mp4",
            runtimeMinutes = 120
        )

        val testMovieZeroId = Movie(
            id = 0L,
            title = "Zero ID Movie",
            genres = emptyList(),
            averageRating = 0.0,
            userRating = null,
            releaseDate = LocalDate(2020, 1, 1),
            overview = "",
            posterImageURL = "",
            trailerURL = "",
            runtimeMinutes = 0
        )

        val testMovieNegativeId = Movie(
            id = -1L,
            title = "Negative ID Movie",
            genres = emptyList(),
            averageRating = 5.0,
            userRating = null,
            releaseDate = LocalDate(2021, 6, 15),
            overview = "Negative test",
            posterImageURL = "https://example.com/negative.jpg",
            trailerURL = "https://example.com/negative.mp4",
            runtimeMinutes = 90
        )

        val testMovieEmptyPoster = Movie(
            id = 456L,
            title = "No Poster Movie",
            genres = emptyList(),
            averageRating = 7.2,
            userRating = 8.0,
            releaseDate = LocalDate(2022, 3, 10),
            overview = "No poster test",
            posterImageURL = "",
            trailerURL = "https://example.com/trailer.mp4",
            runtimeMinutes = 105
        )

        val testMovieLongUrl = Movie(
            id = 789L,
            title = "Long URL Movie",
            genres = emptyList(),
            averageRating = 6.8,
            userRating = null,
            releaseDate = LocalDate(2024, 8, 20),
            overview = "Long URL test",
            posterImageURL = longUrl,
            trailerURL = "https://example.com/trailer.mp4",
            runtimeMinutes = 140
        )

        val genreFull = Genre(id = 28L, name = "Action")
        val genreZeroId = Genre(id = 0L, name = "Unknown")
        val genreNegativeId = Genre(id = -5L, name = "Invalid Genre")
        val genreEmptyName = Genre(id = 12L, name = "")
        val genreSpecialChar = Genre(id = 35L, name = "Sci-Fi & Fantasy")
        val genreLongName = Genre(id = 99L, name = longName)
        val genreUnicode = Genre(id = 40L, name = "アクション")

        val movie1 = Movie(
            id = 1L,
            title = "Movie 1",
            genres = emptyList(),
            averageRating = 7.0,
            userRating = null,
            releaseDate = LocalDate(2023, 1, 1),
            overview = "First movie",
            posterImageURL = "https://example.com/poster1.jpg",
            trailerURL = "https://example.com/trailer1.mp4",
            runtimeMinutes = 100
        )

        val movie2 = Movie(
            id = 2L,
            title = "Movie 2",
            genres = emptyList(),
            averageRating = 8.0,
            userRating = 9.0,
            releaseDate = LocalDate(2023, 2, 1),
            overview = "Second movie",
            posterImageURL = "https://example.com/poster2.jpg",
            trailerURL = "https://example.com/trailer2.mp4",
            runtimeMinutes = 120
        )

        val genre1 = Genre(id = 1L, name = "Action")
        val genre2 = Genre(id = 2L, name = "Comedy")
    }
}
