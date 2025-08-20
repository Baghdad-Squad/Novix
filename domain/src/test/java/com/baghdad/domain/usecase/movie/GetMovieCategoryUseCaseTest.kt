package com.baghdad.domain.usecase.movie

import com.baghdad.domain.repository.MovieRepository
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

class GetMovieCategoryUseCaseTest {

    private val movieRepository = mockk<MovieRepository>()
    private val getMovieCategoryUseCase = GetMovieCategoryUseCase(movieRepository)

    @Test
    fun `getMovieCategoryUseCase should return movie category when successfully`() = runTest {
        coEvery { movieRepository.getMovieDetails(movieId) } returns savedMovie

        val result = getMovieCategoryUseCase(movieId)

        assertThat(result).isEqualTo(savedMovie.movie.genres)
    }

    private companion object {
        val savedMovie = MovieMock.SAVED_MOVIE
        val movieId = MovieMock.MOVIE.id
    }
}