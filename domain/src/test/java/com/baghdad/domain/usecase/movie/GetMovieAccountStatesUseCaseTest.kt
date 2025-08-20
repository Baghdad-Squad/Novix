package com.baghdad.domain.usecase.movie

import com.baghdad.domain.repository.MovieRepository
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

class GetMovieAccountStatesUseCaseTest {
    private val repository = mockk<MovieRepository>()
    private val getMovieAccountStatesUseCase = GetMovieAccountStatesUseCase(repository)

    @Test
    fun `getMovieStates should return true when movie is saved`() = runTest {
        val movieId = 123L
        coEvery { repository.getMovieStates(movieId) } returns true

        val result = getMovieAccountStatesUseCase(movieId)
        assertThat(result).isTrue()
    }


}