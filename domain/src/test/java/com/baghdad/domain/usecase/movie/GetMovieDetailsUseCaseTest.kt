package com.baghdad.domain.usecase.movie

import com.baghdad.domain.exception.NetworkException
import com.baghdad.domain.repository.MovieRepository
import com.baghdad.domain.testHelper.getSampleSavedMovie
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows


class GetMovieDetailsUseCaseTest {
    lateinit var getMovieDetailsUseCase: GetMovieDetailsUseCase
    lateinit var movieRepository: MovieRepository
    val sampleSavedMovie = getSampleSavedMovie()

    @BeforeEach
    fun setup() {
        movieRepository = mockk()
        getMovieDetailsUseCase = GetMovieDetailsUseCase(movieRepository)
    }

    @Test
    fun `getMovieDetailsUseCase() should return movie details when repository returns data successfully`() = runTest {
        val movieId = 1L
        coEvery { movieRepository.getMovieDetails(movieId) } returns sampleSavedMovie

        val result = getMovieDetailsUseCase.invoke(movieId)

        assertThat(result).isEqualTo(sampleSavedMovie)
    }

    @Test
    fun `getMovieDetailsUseCase() should throw NetworkException when repository fails`() = runTest {
        val movieId = 2L
        coEvery { movieRepository.getMovieDetails(movieId) } throws NetworkException()

        assertThrows<NetworkException> {
            getMovieDetailsUseCase.invoke(movieId)
        }
    }
}