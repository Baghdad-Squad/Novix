package com.baghdad.viewmodel.trendingMovie

import com.baghdad.domain.model.savedList.SavedMovie
import com.baghdad.entity.media.Genre
import com.baghdad.entity.media.Movie
import com.google.common.truth.Truth.assertThat
import kotlinx.datetime.LocalDate
import org.junit.jupiter.api.Test

class TrendingMovieMapperTest {

    @Test
    fun `should map savedMovie fields to MovieUiState when movie is saved`() {
        val result = savedMovie.toMovieUiState()

        // Then
        assertThat(result.id).isEqualTo(savedMovie.movie.id)
        assertThat(result.posterPictureURL).isEqualTo(savedMovie.movie.posterImageURL)
        assertThat(result.isSaved).isTrue()
    }

    @Test
    fun `should map savedMovie to MovieUiState using default listId when listId is null`() {
        val savedMovie = savedMovie.copy(listId = null)

        val result = savedMovie.toMovieUiState()

        assertThat(result.savedListId).isEqualTo(savedMovie.listId ?: -1L)
    }

    @Test
    fun `should map genre to UI model when all data is valid`() {
        val result = genre.toGenreUiState()

        // Then
        assertThat(result.id).isEqualTo(genre.id)
        assertThat(result.name).isEqualTo(genre.name)
    }

    companion object {
        val movie = Movie(
            id = 22L,
            title = "Movie",
            genres = emptyList(),
            averageRating = 8.5,
            userRating = 9.0,
            releaseDate = LocalDate(2002, 2, 22),
            overview = "overview",
            posterImageURL = "https://example.com/poster.jpg",
            trailerURL = "https://example.com/trailer.mp4",
            runtimeMinutes = 120
        )

        val savedMovie = SavedMovie(
            movie = movie,
            isSaved = true,
            listId = 22
        )

        val genre = Genre(id = 1L, name = "Action")
    }
}