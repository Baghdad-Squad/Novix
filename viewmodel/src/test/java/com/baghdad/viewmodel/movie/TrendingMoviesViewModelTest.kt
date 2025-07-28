package com.baghdad.viewmodel.movie

import app.cash.turbine.test
import com.baghdad.domain.exception.NoInternetException
import com.baghdad.domain.exception.UnknownException
import com.baghdad.domain.model.PagedResult
import com.baghdad.domain.usecase.genre.GetGenresUseCase
import com.baghdad.domain.usecase.movie.GetTrendingMoviesUseCase
import com.baghdad.entity.media.Genre
import com.baghdad.entity.media.Movie
import com.baghdad.viewmodel.errorStates.BaseSnackBarMessage
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.LocalDate
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
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
        data = testMovies,
        nextKey = 2,
        prevKey = null
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

}
