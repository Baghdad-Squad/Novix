package com.baghdad.viewmodel.review

import com.baghdad.domain.usecase.review.GetMovieReviewsUseCase
import com.baghdad.domain.usecase.review.GetTvShowReviewsUseCase
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ReviewViewModelTest {

    private val mockGetMovieReviewsUseCase = mockk<GetMovieReviewsUseCase>()
    private val mockGetTvShowReviewsUseCase = mockk<GetTvShowReviewsUseCase>()
    private val testDispatcher = StandardTestDispatcher()
    private val contentId = 123L

    @BeforeEach
    fun setup() {
        Dispatchers.setMain(testDispatcher)
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
        clearAllMocks()
    }

    // ============ Movie Reviews Tests ============

    @Test
    fun `GIVEN movie content type WHEN viewModel is initialized THEN movie reviews use case is called`() =
        runTest {
            // Given
            coEvery { mockGetMovieReviewsUseCase(contentId) } returns emptyList()

            // When
            ReviewViewModel(
                getMovieReviewsUseCase = mockGetMovieReviewsUseCase,
                getSeriesReviewsUseCase = mockGetTvShowReviewsUseCase,
                contentId = contentId,
                contentType = ContentType.MOVIE
            )
            testDispatcher.scheduler.advanceUntilIdle()

            // Then
            coVerify(exactly = 1) { mockGetMovieReviewsUseCase(contentId) }
            coVerify(exactly = 0) { mockGetTvShowReviewsUseCase(any()) }
        }

    @Test
    fun `GIVEN empty movie reviews list WHEN reviews are loaded successfully THEN state shows empty list`() =
        runTest {
            // Given
            coEvery { mockGetMovieReviewsUseCase(contentId) } returns emptyList()

            // When
            val viewModel = ReviewViewModel(
                getMovieReviewsUseCase = mockGetMovieReviewsUseCase,
                getSeriesReviewsUseCase = mockGetTvShowReviewsUseCase,
                contentId = contentId,
                contentType = ContentType.MOVIE
            )
            testDispatcher.scheduler.advanceUntilIdle()

            // Then
            val state = viewModel.uiState.value
            assertFalse(state.isLoading)
            assertTrue(state.reviews.isEmpty())
        }


    @Test
    fun `GIVEN series content type WHEN viewModel is initialized THEN series reviews use case is called`() =
        runTest {
            // Given
            coEvery { mockGetTvShowReviewsUseCase(contentId) } returns emptyList()

            // When
            ReviewViewModel(
                getMovieReviewsUseCase = mockGetMovieReviewsUseCase,
                getSeriesReviewsUseCase = mockGetTvShowReviewsUseCase,
                contentId = contentId,
                contentType = ContentType.SERIES
            )
            testDispatcher.scheduler.advanceUntilIdle()

            // Then
            coVerify(exactly = 1) { mockGetTvShowReviewsUseCase(contentId) }
            coVerify(exactly = 0) { mockGetMovieReviewsUseCase(any()) }
        }

    @Test
    fun `GIVEN empty series reviews list WHEN reviews are loaded successfully THEN state shows empty list`() =
        runTest {
            // Given
            coEvery { mockGetTvShowReviewsUseCase(contentId) } returns emptyList()

            // When
            val viewModel = ReviewViewModel(
                getMovieReviewsUseCase = mockGetMovieReviewsUseCase,
                getSeriesReviewsUseCase = mockGetTvShowReviewsUseCase,
                contentId = contentId,
                contentType = ContentType.SERIES
            )
            testDispatcher.scheduler.advanceUntilIdle()

            // Then
            val state = viewModel.uiState.value
            assertFalse(state.isLoading)
            assertTrue(state.reviews.isEmpty())
        }


    @Test
    fun `GIVEN series reviews loading fails WHEN exception is thrown THEN loading state is set to false`() =
        runTest {
            // Given
            val exception = RuntimeException("API error")
            coEvery { mockGetTvShowReviewsUseCase(contentId) } throws exception

            // When
            val viewModel = ReviewViewModel(
                getMovieReviewsUseCase = mockGetMovieReviewsUseCase,
                getSeriesReviewsUseCase = mockGetTvShowReviewsUseCase,
                contentId = contentId,
                contentType = ContentType.SERIES
            )
            testDispatcher.scheduler.advanceUntilIdle()

            // Then
            val state = viewModel.uiState.value
            assertFalse(state.isLoading)
            assertTrue(state.reviews.isEmpty())
        }

    @Test
    fun `GIVEN series viewModel WHEN loadReviewsForSeries is called manually THEN series reviews use case is called again`() =
        runTest {
            // Given
            coEvery { mockGetTvShowReviewsUseCase(contentId) } returns emptyList()
            val viewModel = ReviewViewModel(
                getMovieReviewsUseCase = mockGetMovieReviewsUseCase,
                getSeriesReviewsUseCase = mockGetTvShowReviewsUseCase,
                contentId = contentId,
                contentType = ContentType.SERIES
            )
            testDispatcher.scheduler.advanceUntilIdle()

            // When
            viewModel.loadReviewsForSeries()
            testDispatcher.scheduler.advanceUntilIdle()

            // Then
            coVerify(exactly = 2) { mockGetTvShowReviewsUseCase(contentId) }
        }


    @Test
    fun `GIVEN viewModel WHEN onNavigateBack is called multiple times THEN multiple NavigateBack effects are sent`() =
        runTest {
            // Given
            coEvery { mockGetMovieReviewsUseCase(contentId) } returns emptyList()
            val viewModel = ReviewViewModel(
                getMovieReviewsUseCase = mockGetMovieReviewsUseCase,
                getSeriesReviewsUseCase = mockGetTvShowReviewsUseCase,
                contentId = contentId,
                contentType = ContentType.MOVIE
            )
            testDispatcher.scheduler.advanceUntilIdle()

            val capturedEffects = mutableListOf<ReviewScreenEffect>()
            val job = launch {
                viewModel.uiEffect.collect { effect ->
                    capturedEffects.add(effect)
                }
            }

            // When
            viewModel.onNavigateBack()
            viewModel.onNavigateBack()
            testDispatcher.scheduler.advanceUntilIdle()

            // Then
            assertEquals(2, capturedEffects.size)
            assertTrue(capturedEffects.all { it is ReviewScreenEffect.NavigateBack })

            job.cancel()
        }


}
