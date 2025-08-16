package com.baghdad.domain.usecase.movie

import com.baghdad.entity.media.Genre
import com.baghdad.entity.media.Movie
import com.baghdad.domain.repository.MovieRepository
import com.baghdad.domain.testHelper.getSampleMovie
import com.baghdad.domain.testHelper.getSampleSavedMovie
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.LocalDate
import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class GetUpcomingMoviesUseCaseTest {

    private lateinit var movieRepository: MovieRepository
    private lateinit var useCase: GetUpcomingMoviesUseCase


    @BeforeEach
    fun setUp() {
        movieRepository = mockk()
        useCase = GetUpcomingMoviesUseCase(movieRepository)
    }

    @Test
    fun `invoke() should return upcoming movies from repository`() = runTest {
        // Given
        val genreId = 1L
        val movies = sampleSavedMovie
        coEvery { movieRepository.getUpcomingMovies(genreId) } returns movies

        // When
        val result = useCase(genreId)

        // Then
        assertThat(result).isEqualTo(movies)
        coVerify(exactly = 1) { movieRepository.getUpcomingMovies(genreId) }
    }

    companion object {
        private val sampleSavedMovie = listOf(
            getSampleSavedMovie(),
            getSampleSavedMovie(
                movie = getSampleMovie(
                    id = 2L,
                    title = "The god father",
                    genres = listOf(Genre(1L, "Action"), Genre(2L, "Drama")),
                    imdbRating = 4.5,
                    userRating = 5.0,
                    releaseDate = LocalDate(2004, 2, 18),
                    overview = "The aging patriarch of an organized crime dynasty transfers control of his clandestine empire to his reluctant son.",
                    posterPictureURL = "https://image.tmdb.org/t/p/w500/q6y0Go1tsGEsmtFryDOJo3dEmqu.jpg",
                    trailerURL = "https://www.youtube.com/watch?v=sY1S34973zA",
                    runtimeMinutes = 175
                ),
                isSaved = true,
                listId = 40
            )
        )
    }
}
