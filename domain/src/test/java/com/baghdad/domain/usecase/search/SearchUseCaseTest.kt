package com.baghdad.domain.usecase.search

import com.baghdad.domain.model.search.SearchFilter
import com.baghdad.domain.model.search.SearchResult
import com.baghdad.domain.repository.SearchRepository
import com.baghdad.domain.testHelper.getTestMovie
import com.baghdad.domain.testHelper.getTestTvShow
import com.baghdad.entity.media.Genre
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.LocalDate
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class SearchUseCaseTest {

    private lateinit var searchRepository: SearchRepository
    private lateinit var searchUseCase: SearchUseCase

    private val genreAction = Genre(id = 1, name = "Action")
    private val genreDrama = Genre(id = 2, name = "Drama")

    private val movie1 = getTestMovie(
        id = 1L,
        title = "Movie A",
        imdbRating = 8.5,
        releaseDate = LocalDate(year = 2020, month = 1, day = 1),
        genres = listOf(genreAction)
    )

    private val movie2 = getTestMovie(
        id = 2L,
        title = "Movie B",
        imdbRating = 6.0,
        releaseDate = LocalDate(2010, 1, 1),
        genres = listOf(genreDrama)
    )

    private val tvShow1 = getTestTvShow(
        id = 1L,
        title = "Show A",
        imdbRating = 7.8,
        releaseDate = LocalDate(2022, 1, 1),
        genres = listOf(genreDrama),
    )

    private val tvShow2 = getTestTvShow(
        id = 2L,
        title = "Show B",
        imdbRating = 5.0,
        releaseDate = LocalDate(2005, 1, 1),
        genres = listOf(genreAction)
    )

    @BeforeEach
    fun setUp() {
        searchRepository = mockk(relaxed = true)
        searchUseCase = SearchUseCase(searchRepository)
    }

    @Test
    fun `should return filtered result based on rating, year and genre`() = runTest {
        val unfilteredResult = SearchResult(
            movies = listOf(movie1, movie2),
            tvShows = listOf(tvShow1, tvShow2),
            actors = emptyList()
        )
        coEvery { searchRepository.searchByName(any()) } returns unfilteredResult

        val filter = SearchFilter(
            minimumYear = 2015,
            maximumYear = 2023,
            minimumRating = 7,
            selectedGenres = listOf(genreDrama)
        )

        val result = searchUseCase("test", filter)

        assertThat(result.movies).isEmpty()
        assertThat(result.tvShows).containsExactly(tvShow1)
    }

    @Test
    fun `should return all results when filter is default (no restrictions)`() = runTest {
        val unfilteredResult = SearchResult(
            movies = listOf(movie1, movie2),
            tvShows = listOf(tvShow1, tvShow2),
            actors = emptyList()
        )
        coEvery { searchRepository.searchByName(any()) } returns unfilteredResult

        val filter = SearchFilter(
            minimumYear = Int.MIN_VALUE,
            maximumYear = Int.MAX_VALUE,
            minimumRating = 0,
            selectedGenres = emptyList()
        )

        val result = searchUseCase("test", filter)

        assertThat(result.movies).containsExactlyElementsIn(unfilteredResult.movies)
        assertThat(result.tvShows).containsExactlyElementsIn(unfilteredResult.tvShows)
    }

    @Test
    fun `should return empty results when no items match filter`() = runTest {
        val unfilteredResult = SearchResult(
            movies = listOf(movie2),
            tvShows = listOf(tvShow2),
            actors = emptyList()
        )
        coEvery { searchRepository.searchByName(any()) } returns unfilteredResult

        val filter = SearchFilter(
            minimumYear = 2020,
            maximumYear = 2023,
            minimumRating = 9,
            selectedGenres = listOf(genreDrama)
        )

        val result = searchUseCase("test", filter)

        assertThat(result.movies).isEmpty()
        assertThat(result.tvShows).isEmpty()
    }

    @Test
    fun `should propagate exception when repository throws`() = runTest {
        val exception = RuntimeException()
        coEvery { searchRepository.searchByName(any()) } throws exception

        val filter = SearchFilter(
            minimumYear = 2000,
            maximumYear = 2023,
            minimumRating = 0,
            selectedGenres = emptyList()
        )

        val thrown = runCatching { searchUseCase("test", filter) }.exceptionOrNull()

        assertThat(thrown).isNotNull()
        assertThat(thrown).isInstanceOf(RuntimeException::class.java)
    }
}
