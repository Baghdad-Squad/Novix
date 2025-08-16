package com.baghdad.viewmodel.mapper

import com.baghdad.domain.model.savedList.SavedMovie
import com.baghdad.entity.media.Genre
import com.baghdad.entity.media.Movie
import com.baghdad.viewmodel.topMoviePicks.toUIState
import com.google.common.truth.Truth.assertThat
import kotlinx.datetime.LocalDate
import org.junit.jupiter.api.Test

class TopMoviePicksMapperTest {

    @Test
    fun `toUIState should map Movie to MovieUiState correctly when it called`() {
        val result = SAVED_MOVIE.toUIState()

        assertThat(result.id).isEqualTo(123L)
        assertThat(result.posterPictureURL).isEqualTo("https://example.com/poster.jpg")
    }

    @Test
    fun `toUIState should handle empty posterImageURL correctly when it called`() {
        val movieWithEmptyPoster = SAVED_MOVIE.copy(SAVED_MOVIE.movie.copy(posterImageURL = ""))
        val result = movieWithEmptyPoster.toUIState()

        assertThat(result.posterPictureURL).isEmpty()
    }

    companion object {

        private val GENRE = Genre(1L, "Action")

        private val SAVED_MOVIE =
            SavedMovie(
                movie = Movie(
                    id = 123L,
                    title = "Test Movie",
                    genres = listOf(GENRE),
                    averageRating = 8.5,
                    userRating = null,
                    releaseDate = LocalDate.parse("2023-01-15"),
                    overview = "Test movie overview",
                    posterImageURL = "https://example.com/poster.jpg",
                    trailerURL = "https://example.com/trailer.mp4",
                    runtimeMinutes = 120
                ),
                isSaved = false,
                listId = null
            )
    }
}