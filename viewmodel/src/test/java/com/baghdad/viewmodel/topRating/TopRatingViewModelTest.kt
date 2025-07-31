package com.baghdad.viewmodel.topRating

import com.baghdad.domain.usecase.genre.GetGenresUseCase
import com.baghdad.domain.usecase.topRated.GetMovieTopRatingUseCase
import com.baghdad.domain.usecase.topRated.GetTvShowTopRatingUseCase
import com.baghdad.entity.media.Genre
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class TopRatingViewModelTest {
    private lateinit var getMovieTopRatingUseCase: GetMovieTopRatingUseCase
    private lateinit var getTvShowTopRatingUseCase: GetTvShowTopRatingUseCase
    private lateinit var getGenresUseCase: GetGenresUseCase
    private lateinit var topRatingViewModel: TopRatingViewModel
    private val testDispatcher = StandardTestDispatcher()

    @OptIn(ExperimentalCoroutinesApi::class)
    @BeforeEach
    fun setUp() {
        getMovieTopRatingUseCase = mockk()
        getTvShowTopRatingUseCase = mockk()
        getGenresUseCase = mockk()
        topRatingViewModel = TopRatingViewModel(
            getMovieTopRatingUseCase,
            getTvShowTopRatingUseCase,
            getGenresUseCase,
            testDispatcher
        )
        Dispatchers.setMain(testDispatcher)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `onMovieDetailsClick navigation`() = runTest {
        // Given
        var receivedEffect: TopRatingEffect? = null
        val movieId = 123L
        val job = launch {
            topRatingViewModel.uiEffect.collect { effect ->
                receivedEffect = effect
            }
        }
        // When
        topRatingViewModel.onMovieDetailsClick(movieId)

        advanceUntilIdle()
        println(receivedEffect)
        // Then
        assertTrue(
            receivedEffect is TopRatingEffect.NavigateToMovieDetails,
        )
        job.cancel()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `onTvShowDetailsClick navigation`() = runTest {
        // Given
        var receivedEffect: TopRatingEffect? = null
        val tvShowId = 1L
        val job = launch {
            topRatingViewModel.uiEffect.collect { effect ->
                receivedEffect = effect
            }
        }
        // When
        topRatingViewModel.onTvShowDetailsClick(tvShowId)
        advanceUntilIdle()
        // Then
        assertTrue(
            receivedEffect is TopRatingEffect.NavigateToTvShowDetails,
        )
        job.cancel()

    }

    @Test
    fun `onGenreClick movies tab with new genre`() = runTest {
        // Given
        val genreId = 1L
        // When
        topRatingViewModel.onGenreClick(genreId)
        // Then
        assertTrue(
            topRatingViewModel.uiState.value.selectedMovieGenreId == genreId
        )
    }

    @Test
    fun `onGenreClick tv shows tab with new genre`() {
        // Given
        val genreId = 1L
        topRatingViewModel.onSelectedTab(TopRatingTab.TV_SHOWS)
        // When
        topRatingViewModel.onGenreClick(genreId)
        // Then
        assertTrue(
            topRatingViewModel.uiState.value.selectedTvShowGenreId == genreId
        )
    }

    @Test
    fun `onBackClick navigation`() = runTest {
        // Given
        var receivedEffect: TopRatingEffect? = null
        val job = launch {
            topRatingViewModel.uiEffect.collect { effect ->
                receivedEffect = effect
            }
        }

        // When
        topRatingViewModel.onBackClick()

        // Then
        assertTrue(
            receivedEffect is TopRatingEffect.NavigateBack
        )
        job.cancel()
    }

    @Test
    fun `onSelectedTab movies tab`() {
        // Given
        val selectedTab = TopRatingTab.MOVIES
        // When
        topRatingViewModel.onSelectedTab(selectedTab)
        // Then
        assertTrue(
            topRatingViewModel.uiState.value.selectedTab == selectedTab
        )
    }

    @Test
    fun `onSelectedTab tv shows tab`() {
        // Given
        val selectedTab = TopRatingTab.TV_SHOWS
        // When
        topRatingViewModel.onSelectedTab(selectedTab)
        // Then
        assertTrue(
            topRatingViewModel.uiState.value.selectedTab == selectedTab
        )
    }

    @Test
    fun `onSaveTvShowClick snackbar`() = runTest {
        // Given
        val tvShowId = 1L
        // When
        topRatingViewModel.onSaveTvShowClick(tvShowId)
    }

    @Test
    fun `onSaveMovieClick snackbar`() = runTest {
        // Given
        val movieId = 1L
        // When
        topRatingViewModel.onSaveMovieClick(movieId)
    }


    @Test
    fun `mapThrowableToErrorMessage default`() {
        // Verify that for any given `Throwable`, the method returns `BaseSnackBarMessage.UnknownError`.
        // TODO implement test
    }

    @Test
    fun `mapThrowableToErrorMessage with null throwable`() {
        // Although the parameter is non-null, consider a (hypothetical or defensive) test where if a null could be passed (e.g., via Java interop), it still returns `BaseSnackBarMessage.UnknownError` or handles it gracefully (though current signature prevents this).
        // TODO implement test
    }

    @Test
    fun `mapThrowableToErrorMessage with specific exception types`() {
        // If there were specific error mappings in the future, test each. For now, confirm `IOException`, `RuntimeException`, `CustomException` all map to `BaseSnackBarMessage.UnknownError`.
        // TODO implement test
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `getMovieGenres should update uiState with genres`() = runTest {
        // Given
        val genres = listOf(
            Genre(1, "Action"),
            Genre(2, "Comedy")
        )

        // When
        coEvery { getGenresUseCase.getTvShowGenres() } returns genres
        topRatingViewModel.onSelectedTab(TopRatingTab.MOVIES)
        advanceUntilIdle()

        // Then
        val state = topRatingViewModel.uiState.value
        val expected = genres.map { it.toTopRatingGenreUiState() }
        assertThat(state.genres).isEqualTo(expected)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `getTvShowGenres should update uiState with genres`() = runTest {
        // Given
        val genres = listOf(
            Genre(10, "Drama"),
            Genre(11, "Fantasy")
        )
        // When
        coEvery { getGenresUseCase.getTvShowGenres() } returns genres
        topRatingViewModel.onSelectedTab(TopRatingTab.TV_SHOWS)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        val state = topRatingViewModel.uiState.value
        val expected = genres.map { it.toTopRatingGenreUiState() }

        assertThat(state.genres).isEqualTo(expected)
    }
}