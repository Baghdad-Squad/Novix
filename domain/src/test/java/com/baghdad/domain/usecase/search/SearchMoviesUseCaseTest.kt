package com.baghdad.domain.usecase.search

import com.baghdad.domain.model.pagination.PagedResult
import com.baghdad.domain.repository.SearchRepository
import com.baghdad.domain.testHelper.getSampleSavedMovie
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test


class SearchMoviesUseCaseTest {

    private lateinit var searchRepository: SearchRepository
    private lateinit var searchMoviesUseCase: SearchMoviesUseCase
    val sampleSavedMovie = PagedResult(
        data = listOf(getSampleSavedMovie()),
        nextPage = null,
        prevPage = null
    )

    @BeforeEach
    fun setUp() {
        searchRepository = mockk()
        searchMoviesUseCase = SearchMoviesUseCase(searchRepository)
    }

//    @Test
//    fun `searchMoviesUseCase() should return movies as returned by repository`() = runTest {
//        val query = "action"
//        coEvery { searchRepository.searchMoviesByTitle(query, 1) } returns sampleSavedMovie
//
//        val result = searchMoviesUseCase(query, 1)
//
//        assertThat(result.data).hasSize(2)
//        assertThat(result.data[0].movie.title).isEqualTo("The Dark Knight")
//        assertThat(result.data[1].movie.title).isEqualTo("Inception")
//    }

//    @Test
//    fun `searchMoviesUseCase() should preserve pagination keys from repository`() = runTest {
//        val query = "action"
//        coEvery { searchRepository.searchMoviesByTitle(query, 1) } returns sampleSavedMovie
//
//        val result = searchMoviesUseCase(query, 1)
//
//        assertThat(result.prevPage).isNull()
//        assertThat(result.nextPage).isEqualTo(2)
//    }

    @Test
    fun `searchMoviesUseCase() should call repository exactly once`() = runTest {
        val query = "action"
        coEvery { searchRepository.searchMoviesByTitle(query, 1) } returns sampleSavedMovie

        searchMoviesUseCase(query, 1)

        coVerify(exactly = 1) { searchRepository.searchMoviesByTitle(query, 1) }
    }
}
