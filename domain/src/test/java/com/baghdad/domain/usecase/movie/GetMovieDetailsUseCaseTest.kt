package com.baghdad.domain.usecase.movie

import com.baghdad.domain.repository.MovieRepository
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

class GetMovieDetailsUseCaseTest {

    private val movieRepository = mockk<MovieRepository>()
    private val getMovieDetailsUseCase = GetMovieDetailsUseCase(movieRepository)

    @Test
    fun `getMovieDetailsUseCase should return movie details when successfully`() =
        runTest {
            coEvery { movieRepository.getMovieDetails(movieId) } returns savedMovie

            val result = getMovieDetailsUseCase.invoke(movieId)

            assertThat(result).isEqualTo(savedMovie)
        }

    private companion object {
        val movieId = MovieMock.MOVIE_ID
        val savedMovie = MovieMock.SAVED_MOVIE
    }
}