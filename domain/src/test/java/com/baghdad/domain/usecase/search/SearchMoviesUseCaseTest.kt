package com.baghdad.domain.usecase.search

import com.baghdad.domain.repository.SearchRepository
import com.baghdad.domain.usecase.movie.MovieMock
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

class SearchMoviesUseCaseTest {

    private val searchRepository = mockk<SearchRepository>()
    private val searchMoviesUseCase = SearchMoviesUseCase(searchRepository)

    @Test
    fun `searchMoviesUseCase should call repository exactly once`() = runTest {
        coEvery { searchRepository.searchMoviesByTitle(query, 1) } returns movieResult

        searchMoviesUseCase(query, 1)

        coVerify(exactly = 1) { searchRepository.searchMoviesByTitle(query, 1) }
    }

    private companion object {
        val movieResult = MovieMock.SAVED_MOVIES_PAGED_RESULT
        val query = "action"
    }
}
