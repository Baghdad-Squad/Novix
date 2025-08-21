package com.baghdad.domain.usecase.movie

import com.baghdad.domain.repository.MovieRepository
import com.baghdad.domain.usecase.genre.GenreMock
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

class GetMovieTopRatingUseCaseTest {

    private val movieRepository = mockk<MovieRepository>()
    private val getMovieTopRatingUseCase = GetMovieTopRatingUseCase(movieRepository)

    @Test
    fun `getTopRatedMovies should return all movies when genreId is null`() = runTest {

        coEvery { movieRepository.getTopRatedMovies(page) } returns savedMoviesResult

        val result = getMovieTopRatingUseCase(page, genreId = null)

        assertThat(result.data).containsExactlyElementsIn(savedMoviesResult.data)
    }

    @Test
    fun `getTopRatedMovies should return filtered movies when genreId is provided`() = runTest {
        coEvery { movieRepository.getTopRatedMovies(page) } returns savedMoviesResult

        val result = getMovieTopRatingUseCase(page, genreId = genreId)

        assertThat(result.data).containsExactlyElementsIn(
            savedMoviesResult.data.filter { it.movie.genres.any { genre -> genre.id == genreId } }
        )
    }

    private companion object {
        val page = MovieMock.PAGE
        val savedMoviesResult = MovieMock.SAVED_MOVIES_PAGED_RESULT
        val genreId = GenreMock.GENRE_ID
    }
}