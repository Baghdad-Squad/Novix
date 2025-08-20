package com.baghdad.domain.usecase.movie

import com.baghdad.domain.repository.MovieRepository
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

class GetMoviesByGenreUseCaseTest {

    private val movieRepository = mockk<MovieRepository>()
    private val getMoviesByGenreUseCase = GetMoviesByGenreUseCase(movieRepository)

    @Test
    fun `getMoviesByGenreUseCase should call repository with correct parameters when successful`() =
        runTest {
            coEvery {
                movieRepository.getMoviesByGenre(genreId, page, pageSize)
            } returns savedMovieResult

            getMoviesByGenreUseCase(genreId, page, pageSize)

            coVerify(exactly = 1) { movieRepository.getMoviesByGenre(genreId, page, pageSize) }
        }

    @Test
    fun `getMoviesByGenreUseCase should return repository result when successful`() = runTest {
        coEvery {
            movieRepository.getMoviesByGenre(genreId, page, pageSize)
        } returns savedMovieResult

        val result = getMoviesByGenreUseCase(genreId, page, pageSize)

        assertThat(result).isEqualTo(savedMovieResult)
    }

    private companion object {
        val pageSize = MovieMock.PAGE_SIZE
        val genreId = MovieMock.GENRE_ID
        val page = MovieMock.PAGE
        val savedMovieResult = MovieMock.SAVED_MOVIES_PAGED_RESULT
    }
}