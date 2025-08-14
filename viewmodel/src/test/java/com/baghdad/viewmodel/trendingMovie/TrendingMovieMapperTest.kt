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
        val expectedUiState = TrendingMoviesScreenState.TrendingMovieUiState(
            id = savedMovie.movie.id,
            posterPictureURL = savedMovie.movie.posterImageURL,
            isSaved = savedMovie.isSaved,
            savedListId = savedMovie.listId ?: -1L
        )

        val result = savedMovie.toMovieUiState()

        assertThat(result).isEqualTo(expectedUiState)
    }

    @Test
    fun `should map savedMovie to MovieUiState using default listId when listId is null`() {
        val savedMovie = savedMovie.copy(listId = null)

        val result = savedMovie.toMovieUiState()

        assertThat(result.savedListId).isEqualTo(savedMovie.listId ?: -1L)
    }

    @Test
    fun `should map genre to UI model when all data is valid`() {
        val genre = Genre(id = 1L, name = "Action")
        val expectedGenreUiState = TrendingMoviesScreenState.TrendingCategoryUiState(
            id = genre.id,
            name = genre.name
        )

        val result = genre.toGenreUiState()

        assertThat(result).isEqualTo(expectedGenreUiState)
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
            listId = -1
        )
    }
}