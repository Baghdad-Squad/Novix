package com.baghdad.domain.usecase.search

import com.baghdad.domain.repository.SearchRepository
import com.baghdad.domain.usecase.tvShow.TvShowMock
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

class SearchTvShowsUseCaseTest {

    private val searchRepository = mockk<SearchRepository>()
    private val searchTvShowsUseCase = SearchTvShowsUseCase(searchRepository)

    @Test
    fun `searchTvShowsUseCase should return tv shows as returned by repository`() = runTest {
        coEvery { searchRepository.searchTvShowsByName(query, page) } returns tvShowResult

        val result = searchTvShowsUseCase(query, page)

        assertThat(result).isEqualTo(tvShowResult)
    }

    private companion object {
        val tvShowResult = TvShowMock.TV_SHOW_RESULT
        val query = "action"
        val page = 1
    }
}