package com.baghdad.domain.usecase.movie

import com.baghdad.domain.repository.MovieRepository
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

class GetMovieGenresUseCaseTest {

    private val movieRepository = mockk<MovieRepository>()
    private val getMovieGenresUseCase = GetMovieGenresUseCase(movieRepository)

    @Test
    fun `getMovieGenres should return a list of genres`() = runTest {
        coEvery { movieRepository.getGenres() } returns genres

        val result = getMovieGenresUseCase.getMovieGenres()

        assertThat(result).isEqualTo(genres)
    }

    private companion object {
        val genres = MovieMock.GENRES
    }
}