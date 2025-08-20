package com.baghdad.domain.usecase.movie

import com.baghdad.domain.repository.MovieRepository
import com.baghdad.domain.testHelper.getSavedMovies
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class GetMoviesByGenreUseCaseTest {

    private val movieRepository = mockk<MovieRepository>()
    private val getMoviesByGenreUseCase = GetMoviesByGenreUseCase(movieRepository)

    @Test
    fun `should call repository with correct parameters when successful`() = runTest {
        coEvery {
            movieRepository.getMoviesByGenre(GENRE_ID, PAGE, PAGE_SIZE)
        } returns getSavedMovies()

        getMoviesByGenreUseCase(GENRE_ID, PAGE, PAGE_SIZE)

        coVerify(exactly = 1) { movieRepository.getMoviesByGenre(GENRE_ID, PAGE, PAGE_SIZE) }
    }

    @Test
    fun `should return repository result when successful`() = runTest {
        val expectedResult = getSavedMovies()
        coEvery {
            movieRepository.getMoviesByGenre(GENRE_ID, PAGE, PAGE_SIZE)
        } returns expectedResult

        val result = getMoviesByGenreUseCase(GENRE_ID, PAGE, PAGE_SIZE)

        assertThat(result).isEqualTo(expectedResult)
    }

    @Test
    fun `should propagate exception when repository throws`() = runTest {
        coEvery {
            movieRepository.getMoviesByGenre(GENRE_ID, PAGE, PAGE_SIZE)
        } throws RuntimeException("Network error")

        val thrown = assertThrows<RuntimeException> {
            getMoviesByGenreUseCase(GENRE_ID, PAGE, PAGE_SIZE)
        }
        assertThat(thrown.message).isEqualTo("Network error")
    }

    companion object {
        private const val PAGE_SIZE = 20
        private const val GENRE_ID = 22L
        private const val PAGE = 1
    }
}