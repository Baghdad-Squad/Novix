package com.baghdad.viewmodel.continueWatching

import com.baghdad.domain.model.ContinueWatching
import com.baghdad.domain.model.PagedResult
import com.baghdad.domain.usecase.continueWatching.GetAllContinueWatchingByGenreUseCase
import com.baghdad.domain.usecase.continueWatching.GetAllContinueWatchingUseCase
import com.baghdad.domain.usecase.genre.GetGenresUseCase
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

class ContinueWatchingViewModelTest {
    private lateinit var getGenresUseCase: GetGenresUseCase
    private lateinit var getAllContinueWatchingUseCase: GetAllContinueWatchingUseCase
    private lateinit var getAllContinueWatchingByGenreUseCase: GetAllContinueWatchingByGenreUseCase
    private lateinit var continueWatchingViewModel: ContinueWatchingViewModel
    private val testDispatcher = StandardTestDispatcher()

    @OptIn(ExperimentalCoroutinesApi::class)
    @BeforeEach
    fun setup() {
        getGenresUseCase = mockk()
        getAllContinueWatchingUseCase = mockk()
        getAllContinueWatchingByGenreUseCase = mockk()
        continueWatchingViewModel = ContinueWatchingViewModel(
            getGenresUseCase,
            getAllContinueWatchingUseCase,
            getAllContinueWatchingByGenreUseCase,
            testDispatcher
        )
        Dispatchers.setMain(testDispatcher)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `onMediaClick with TV SHOW content type should send NavigateToTvShowDetails effect when navigate to tv show details`() =
        runTest {
            // Given
            var receivedEffect: ContinueWatchingScreenEffect? = null
            val job = launch {
                continueWatchingViewModel.uiEffect.collect { effect ->
                    receivedEffect = effect
                }
            }
            // When
            continueWatchingViewModel.onMediaClick(
                1L,
                ContinueWatchingState.ContinueWatchingMovieUiState.ContentType.TV_SHOW
            )
            advanceUntilIdle()
            // Then
            assertThat(receivedEffect is ContinueWatchingScreenEffect.NavigateToTvShowDetails).isTrue()
            job.cancel()
        }

    @Test
    fun `onSelectedTab should update tab and refresh genres and tv shows when selected tab is movie`() =
        runTest {
            // Given
            coEvery { getGenresUseCase.getTvShowGenres() } returns emptyList()
            coEvery { getAllContinueWatchingUseCase(any()) } returns PagedResult(emptyList(), 1, 0)
            // When
            continueWatchingViewModel.onSelectedTab(false)
            // Then
            assertThat(continueWatchingViewModel.uiState.value.selectedMediaTabIsMovie).isFalse()
        }

    @Test
    fun `onGenreClick with a new  valid genreId should update selected genre and reload media when current selectedGenreId is null`() {
        // when
        continueWatchingViewModel.onGenreClick(1L)
        val state = continueWatchingViewModel.uiState.value

        // Then
        assertThat(state.selectedMovieGenreId).isEqualTo(1L)
        assertThat(state.isLoading).isTrue()
    }


    @Test
    fun `onGenreClick changes selected genre and triggers movie reload should update selected genre and reload media when current selectedGenreId is null`() =
        runTest {
            continueWatchingViewModel.onGenreClick(1L)

            val state = continueWatchingViewModel.uiState.value
            assertThat(state.selectedMovieGenreId).isEqualTo(1L)
            assertThat(state.isLoading).isTrue()
        }

    @Test
    fun `onGenreClick change selected genre and triggers tv show reload should update selected genre and reload media when current selectedGenreId is null`() =
        runTest {
            // Given
            continueWatchingViewModel.onSelectedTab(false)
            // When
            continueWatchingViewModel.onGenreClick(1L)
            val state = continueWatchingViewModel.uiState.value
            // Then
            assertThat(state.selectedTvShowGenreId).isEqualTo(1L)
            assertThat(state.isLoading).isTrue()
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `onMediaClick with a valid movieId should send NavigateToMovieDetails effect when navigate to movie details`() =
        runTest {
            // Given
            var receivedEffect: ContinueWatchingScreenEffect? = null
            val job = launch {
                continueWatchingViewModel.uiEffect.collect { effect ->
                    receivedEffect = effect
                }
            }
            // When
            continueWatchingViewModel.onMediaClick(
                1L,
                ContinueWatchingState.ContinueWatchingMovieUiState.ContentType.MOVIE
            )
            advanceUntilIdle()
            // Then
            assertThat(receivedEffect is ContinueWatchingScreenEffect.NavigateToMovieDetails).isTrue()
            job.cancel()
        }

    @Test
    fun `onSelectedTab to Movie tab should update tab and reload genres and Movie when selected tab is movie`() {
        // Given
        continueWatchingViewModel.onSelectedTab(true)
        val state = continueWatchingViewModel.uiState.value
        assertThat(state.selectedMediaTabIsMovie).isTrue()
        assertThat(state.isLoading).isTrue()
        assertThat(state.selectedMovieGenreId).isNull()
    }

    @Test
    fun `onSelectedTab updates tab and reloads genres and tv show should update tab and reload genres and tv show when selected tab is tv show`() =
        runTest {
            // When
            continueWatchingViewModel.onSelectedTab(false)
            val state = continueWatchingViewModel.uiState.value
            // Then
            assertThat(state.selectedMediaTabIsMovie).isFalse()
            assertThat(state.isLoading).isTrue()
            assertThat(state.selectedMovieGenreId).isNull()
        }

    @Test
    fun `onSelectedTab to TV Show tab should update tab and reload genres and media when selected tab is tv show`() {
        // when
        continueWatchingViewModel.onSelectedTab(false)
        val state = continueWatchingViewModel.uiState.value
        // Then
        assertThat(state.selectedMediaTabIsMovie).isFalse()
        assertThat(state.isLoading).isTrue()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `should get movies and genres when selected tab is movie`() = runTest {
        // Given
        coEvery { getGenresUseCase.getMovieGenres() } returns listOf(
            Genre(1L, "Action"),
            Genre(2L, "Action")
        )
        coEvery { getAllContinueWatchingUseCase(0) } returns PagedResult(
            listOf(
                ContinueWatching(
                    contentType = ContinueWatching.ContentType.MOVIE,
                    contentId = 1L,
                    genreIds = listOf(
                        1L, 2L
                    ),
                    userId = 1L,
                    contentImageUrl = ""
                )
            ), 2, 1
        )

        // When
        continueWatchingViewModel.onSelectedTab(true)
        testDispatcher.scheduler.advanceUntilIdle()
        // Then
        assertThat(continueWatchingViewModel.uiState.value.selectedMediaTabIsMovie).isTrue()
    }
}