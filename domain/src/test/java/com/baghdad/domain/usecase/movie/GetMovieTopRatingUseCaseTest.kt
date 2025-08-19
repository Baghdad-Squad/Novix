package com.baghdad.domain.usecase.movie

import com.baghdad.domain.repository.MovieRepository
import com.baghdad.domain.testHelper.getSavedMovies
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test


class GetMovieTopRatingUseCaseTest {

    private val movieRepository = mockk<MovieRepository>()
    private var getMovieTopRatingUseCase: GetMovieTopRatingUseCase =
        GetMovieTopRatingUseCase(movieRepository)

    @Test
    fun `getTopRatedMovies should return all movies when genreId is null`() = runTest {

        coEvery { movieRepository.getTopRatedMovies(PAGE) } returns getSavedMovies()

        val result = getMovieTopRatingUseCase(PAGE, genreId = null)

        assertThat(result.data).containsExactlyElementsIn(getSavedMovies().data)
    }

    @Test
    fun `getTopRatedMovies should return filtered movies when genreId is provided`() = runTest {
        coEvery { movieRepository.getTopRatedMovies(PAGE) } returns getSavedMovies()

        val result = getMovieTopRatingUseCase(PAGE, genreId = GENRE_ID)

        assertThat(result.data).containsExactlyElementsIn(
            getSavedMovies().data.filter { it.movie.genres.any { genre -> genre.id == GENRE_ID } }
        )
    }

    private companion object {
        const val PAGE = 2
        const val GENRE_ID = 2L
    }
}