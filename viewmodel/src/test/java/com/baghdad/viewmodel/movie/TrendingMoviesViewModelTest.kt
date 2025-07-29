package com.baghdad.viewmodel.movie

import app.cash.turbine.test
import com.baghdad.domain.model.PagedResult
import com.baghdad.domain.usecase.genre.GetGenresUseCase
import com.baghdad.domain.usecase.movie.GetTrendingMoviesUseCase
import com.baghdad.entity.media.Genre
import com.baghdad.entity.media.Movie
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.LocalDate
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test


class TrendingMoviesViewModelTest {

    private val getGenresUseCase: GetGenresUseCase = mockk()
    private val getTrendingMoviesUseCase: GetTrendingMoviesUseCase = mockk()
    private lateinit var viewModel: TrendingMoviesViewModel

    private val testGenres = listOf(Genre(1, "Action"), Genre(2, "Drama"))
    val testMovies = List(3) { index ->
        Movie(
            id = index.toLong(),
            title = "Movie Title $index",
            genres = listOf(Genre(id = 1L, name = "Action")),
            averageRating = 7.5 + index,
            userRating = if (index % 2 == 0) 6.0 + index else null,
            releaseDate = LocalDate.parse("2023-01-${(index + 1).toString().padStart(2, '0')}"),
            overview = "This is the overview for Movie $index",
            posterImageURL = "https://example.com/posters/poster$index.jpg",
            trailerURL = "https://example.com/trailers/trailer$index.mp4",
            runtimeMinutes = 100 + index * 10
        )

    }

    private val pagedResult = PagedResult(
        data = testMovies, nextKey = 2, prevKey = null
    )

    @BeforeEach
    fun setUp() {
        coEvery { getGenresUseCase.getMovieGenres() } returns testGenres
        coEvery { getTrendingMoviesUseCase(any(), any()) } coAnswers {
            pagedResult
        }

        viewModel = TrendingMoviesViewModel(getTrendingMoviesUseCase, getGenresUseCase)
    }

    @Test
    fun `onBackClick emits NavigateBack effect`() = runTest {
        viewModel.uiEffect.test {
            viewModel.onBackClick()
            assertEquals(TrendingMoviesEffect.NavigateBack, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `onMovieClick emits NavigateToMovieDetails`() = runTest {
        viewModel.uiEffect.test {
            viewModel.onMovieClick(10L)
            assertEquals(TrendingMoviesEffect.NavigateToMovieDetails(10L), awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `onCategoryClick updates state and loads movies`() = runTest {
        viewModel.uiState.test {
            skipItems(1)
            viewModel.onCategoryClick(1L)
            advanceUntilIdle()
            val updated = awaitItem()
            assertEquals(1L, updated.selectedGenreId)
            assertNotNull(updated.movies)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `movie mapping from entity to UI state`() {
        val uiState = testMovies[0].toMovieUiState()
        assertEquals(testMovies[0].id, uiState.id)
        assertEquals(testMovies[0].posterImageURL, uiState.posterPictureURL)
    }

    @Test
    fun `onGenreClick reloads movie flow`() = runTest {
        viewModel.uiState.test {
            skipItems(1)
            viewModel.onCategoryClick(99L)
            advanceUntilIdle()
            val state = awaitItem()
            assertEquals(99L, state.selectedGenreId)
            assertNotNull(state.movies)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `onCategoryClick with same id does not update state`() = runTest {
        viewModel.onCategoryClick(2L)
        advanceUntilIdle()
        viewModel.uiState.test {
            skipItems(1)
            viewModel.onCategoryClick(2L)
            advanceUntilIdle()
            expectNoEvents()
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `loadMoviesByGenres should call getTrendingMoviesUseCase with correct genreId`() = runTest {

        val genreId = 2L
        val page = 0
        val fakeMovie = Movie(
            id = 1L,
            title = "Test Movie",
            genres = listOf(Genre(id = 2L, name = "Action")),
            averageRating = 8.5,
            userRating = null,
            releaseDate = LocalDate.parse("2023-01-01"),
            overview = "Overview",
            posterImageURL = "",
            trailerURL = "",
            runtimeMinutes = 120
        )
        coEvery { getTrendingMoviesUseCase(page = page, genreId = genreId) } returns PagedResult(
            data = listOf(fakeMovie), nextKey = 2, prevKey = null
        )

    }

    @Test
    fun `loadMoviesByGenres should update state with flow and isLoading true`() = runTest {
        val genreId = 2L
        val movie = Movie(
            id = 10L,
            title = "Another Movie",
            genres = listOf(Genre(id = 2L, name = "Action")),
            averageRating = 7.0,
            userRating = 8.0,
            releaseDate = LocalDate.parse("2023-01-02"),
            overview = "Test overview",
            posterImageURL = "",
            trailerURL = "",
            runtimeMinutes = 110
        )

        coEvery { getTrendingMoviesUseCase(any(), genreId) } returns PagedResult(
            data = listOf(movie), nextKey = 2, prevKey = null
        )
    }

}
