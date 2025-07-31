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
    fun `should return updated effect to navigate to movie details when onMovieDetailsClick`() =
        runTest {
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
            // Then
            assertThat(
                receivedEffect is TopRatingEffect.NavigateToMovieDetails,
            ).isTrue()
            job.cancel()
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `should return updated effect to navigate to tv show details when onTvShowDetailsClick`() =
        runTest {
            // Given
            var receivedEffect: TopRatingEffect? = null
            val tvShowId = 123L
            val job = launch {
                topRatingViewModel.uiEffect.collect { effect ->
                    receivedEffect = effect
                }
            }
            // When
            topRatingViewModel.onTvShowDetailsClick(tvShowId)
            advanceUntilIdle()
            // Then
            assertThat(
                receivedEffect is TopRatingEffect.NavigateToTvShowDetails,
            ).isTrue()
            job.cancel()
        }

    @Test
    fun `should return updated genre tab when onGenreClick`() = runTest {
        // Given
        val genreId = 1L
        // When
        topRatingViewModel.onGenreClick(genreId)
        // Then
        assertThat(
            topRatingViewModel.uiState.value.selectedMovieGenreId == genreId
        ).isTrue()
    }

    @Test
    fun `should return updated tv show genre tab when onTvShowGenreClick`() {
        // Given
        val genreId = 1L
        topRatingViewModel.onSelectedTab(TopRatingTab.TV_SHOWS)
        // When
        topRatingViewModel.onGenreClick(genreId)
        // Then
        assertThat(
            topRatingViewModel.uiState.value.selectedTvShowGenreId == genreId
        ).isTrue()
    }

    @Test
    fun `should return updated effect to navigate back when onBackClick`() = runTest {
        // Given
        var receivedEffect: TopRatingEffect? = null
        val job = launch() {
            topRatingViewModel.uiEffect.collect { effect ->
                receivedEffect = effect
            }
        }

        // When
        topRatingViewModel.onBackClick()
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        assertThat(
            receivedEffect is TopRatingEffect.NavigateBack
        ).isTrue()
        job.cancel()
    }

    @Test
    fun `should return updated tab state to movies when selected tab is movies`() {
        // Given
        val selectedTab = TopRatingTab.MOVIES
        // When
        topRatingViewModel.onSelectedTab(selectedTab)
        // Then
        assertThat(
            topRatingViewModel.uiState.value.selectedTab == selectedTab
        ).isTrue()
    }

    @Test
    fun `should return updated tab state to tv show when selected tab is tv show`() {
        // Given
        val selectedTab = TopRatingTab.TV_SHOWS
        // When
        topRatingViewModel.onSelectedTab(selectedTab)
        // Then
        assertThat(
            topRatingViewModel.uiState.value.selectedTab == selectedTab
        ).isTrue()
    }

    @Test
    fun `should not throw exceptions when onSaveTvShowClick`() = runTest {
        // Given
        val tvShowId = 1L
        // When
        topRatingViewModel.onSaveTvShowClick(tvShowId)
    }

    @Test
    fun `should not throw exception when onSaveMovieClick`() = runTest {
        // Given
        val movieId = 1L
        // When
        topRatingViewModel.onSaveMovieClick(movieId)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `should return updated ui state with genres when selected tab is movies`() = runTest {
        // Given
        val genres = listOf(
            Genre(10, "Drama"),
            Genre(11, "Fantasy")
        )

        // When
        coEvery { getGenresUseCase.getMovieGenres() } returns genres
        topRatingViewModel.onSelectedTab(TopRatingTab.MOVIES)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        val state = topRatingViewModel.uiState.value
        val expected = genres.map { it.toTopRatingGenreUiState() }
        assertThat(state.genres).isEqualTo(expected)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `should return updated ui state with genres when selected tab is tv shows`() = runTest {
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