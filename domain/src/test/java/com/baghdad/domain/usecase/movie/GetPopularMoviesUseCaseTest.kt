package com.baghdad.domain.usecase.movie

import com.baghdad.domain.model.savedList.SavedMovie
import com.baghdad.domain.repository.MovieRepository
import com.baghdad.entity.media.Movie
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.LocalDate
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class GetPopularMoviesUseCaseTest {

    private lateinit var repository: MovieRepository
    private lateinit var useCase: GetPopularMoviesUseCase

    @BeforeEach
    fun setUp() {
        repository = mockk()
        useCase = GetPopularMoviesUseCase(repository)
    }

    @Test
    fun `getPopularMoviesUseCase should return movies when repository succeeds`() = runTest {
        coEvery { repository.getPopularMovies() } returns savedMovies

        val result = useCase.invoke()

        assertThat(result).isEqualTo(savedMovies)
    }

    companion object {
        val movie = Movie(
            id = 1,
            title = "Movie 1",
            genres = emptyList(),
            averageRating = 7.5,
            userRating = null,
            releaseDate = LocalDate(2020, 1, 1),
            overview = "Overview of Movie 1",
            posterImageURL = "http://example.com/poster1.jpg",
            runtimeMinutes = 120,
            trailerURL = "http://example.com/trailer1.mp4"
        )


        val savedMovie = SavedMovie(
            movie = movie,
            isSaved = true,
            listId = 1
        )

        val savedMovies = listOf(
            savedMovie,
            savedMovie.copy(movie = movie.copy(id = 2), listId = 2)
        )

    }
}