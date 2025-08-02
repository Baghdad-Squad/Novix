package com.baghdad.viewmodel.review

import androidx.lifecycle.SavedStateHandle
import com.baghdad.domain.usecase.review.GetMovieReviewsUseCase
import com.baghdad.domain.usecase.review.GetTvShowReviewsUseCase
import com.google.common.truth.Truth.assertThat
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

    @Test
    fun `reviewViewModel() should call movie reviews use case when content type is movie`() =
        runTest {
            // Given
            coEvery { mockGetMovieReviewsUseCase(contentId) } returns emptyList()

            val savedStateHandle = SavedStateHandle(
                mapOf(
                    "mediaId" to 1L,
                    "mediaType" to ContentType.MOVIE,
                ))

                    // When
                    ReviewViewModel(
                        getMovieReviewsUseCase = mockGetMovieReviewsUseCase,
                        getSeriesReviewsUseCase = mockGetTvShowReviewsUseCase,
                        savedStateHandle = savedStateHandle,
                        ioDispatcher = testDispatcher
                    )
                            testDispatcher . scheduler . advanceUntilIdle ()

                            // Then
                            coVerify (exactly = 1) { mockGetMovieReviewsUseCase(contentId) }
                            coVerify (exactly = 0) { mockGetTvShowReviewsUseCase(any()) }
        }

    @Test
    fun `reviewViewModel() should return empty state when movie reviews list is empty`() = runTest {
        // Given
        coEvery { mockGetMovieReviewsUseCase(contentId) } returns emptyList()
        val savedStateHandle = SavedStateHandle(
            mapOf(
                "mediaId" to 1L,
                "mediaType" to ContentType.MOVIE,
            ))

        // When
        val viewModel = ReviewViewModel(
            getMovieReviewsUseCase = mockGetMovieReviewsUseCase,
            getSeriesReviewsUseCase = mockGetTvShowReviewsUseCase,
            savedStateHandle = savedStateHandle,
            ioDispatcher = testDispatcher
        )
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        val state = viewModel.uiState.value
        assertThat(state.isLoading).isFalse()
        assertThat(state.reviews).isEmpty()
    }

    @Test
    fun `reviewViewModel() should call series reviews use case when content type is series`() =
        runTest {
            // Given
            coEvery { mockGetTvShowReviewsUseCase(contentId) } returns emptyList()
            val savedStateHandle = SavedStateHandle(
                mapOf(
                    "mediaId" to 1L,
                    "mediaType" to ContentType.MOVIE,
                ))

            // When
            ReviewViewModel(
                getMovieReviewsUseCase = mockGetMovieReviewsUseCase,
                getSeriesReviewsUseCase = mockGetTvShowReviewsUseCase,
                savedStateHandle = savedStateHandle,
                ioDispatcher = testDispatcher
            )
            testDispatcher.scheduler.advanceUntilIdle()

            // Then
            coVerify(exactly = 1) { mockGetTvShowReviewsUseCase(contentId) }
            coVerify(exactly = 0) { mockGetMovieReviewsUseCase(any()) }
        }

    @Test
    fun `reviewViewModel() should return empty state when series reviews list is empty`() =
        runTest {
            // Given
            coEvery { mockGetTvShowReviewsUseCase(contentId) } returns emptyList()
            val savedStateHandle = SavedStateHandle(
                mapOf(
                    "mediaId" to 1L,
                    "mediaType" to ContentType.MOVIE,
                ))

            // When
            val viewModel = ReviewViewModel(
                getMovieReviewsUseCase = mockGetMovieReviewsUseCase,
                getSeriesReviewsUseCase = mockGetTvShowReviewsUseCase,
                savedStateHandle = savedStateHandle,
                ioDispatcher = testDispatcher
            )
            testDispatcher.scheduler.advanceUntilIdle()

            // Then
            val state = viewModel.uiState.value
            assertThat(state.isLoading).isFalse()
            assertThat(state.reviews).isEmpty()
        }

    @Test
    fun `reviewViewModel() should return empty list when series reviews loading fails`() = runTest {
        // Given
        val exception = RuntimeException()
        coEvery { mockGetTvShowReviewsUseCase(contentId) } throws exception
        val savedStateHandle = SavedStateHandle(
            mapOf(
                "mediaId" to 1L,
                "mediaType" to ContentType.MOVIE,
            ))

        // When
        val viewModel = ReviewViewModel(
            getMovieReviewsUseCase = mockGetMovieReviewsUseCase,
            getSeriesReviewsUseCase = mockGetTvShowReviewsUseCase,
            savedStateHandle = savedStateHandle ,
            ioDispatcher = testDispatcher
        )
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        val state = viewModel.uiState.value
        assertThat(state.isLoading).isFalse()
        assertThat(state.reviews).isEmpty()
    }

    @Test
    fun `loadReviewsForSeries() should call series reviews use case when triggered manually`() =
        runTest {
            // Given
            coEvery { mockGetTvShowReviewsUseCase(contentId) } returns emptyList()
            val savedStateHandle = SavedStateHandle(
                mapOf(
                    "mediaId" to 1L,
                    "mediaType" to ContentType.MOVIE,
                ))

            val viewModel = ReviewViewModel(
                getMovieReviewsUseCase = mockGetMovieReviewsUseCase,
                getSeriesReviewsUseCase = mockGetTvShowReviewsUseCase,
                savedStateHandle = savedStateHandle,
                ioDispatcher = testDispatcher
            )
            testDispatcher.scheduler.advanceUntilIdle()

            // When
            viewModel.loadReviewsForSeries()
            testDispatcher.scheduler.advanceUntilIdle()

            // Then
            coVerify(exactly = 2) { mockGetTvShowReviewsUseCase(contentId) }
        }

    @Test
    fun `onNavigateBack() should return NavigateBack effect when called repeatedly`() =
        runTest {
            // Given
            coEvery { mockGetMovieReviewsUseCase(contentId) } returns emptyList()
            val savedStateHandle = SavedStateHandle(
                mapOf(
                    "mediaId" to 1L,
                    "mediaType" to ContentType.MOVIE,
                ))


            val viewModel = ReviewViewModel(
                getMovieReviewsUseCase = mockGetMovieReviewsUseCase,
                getSeriesReviewsUseCase = mockGetTvShowReviewsUseCase,
                savedStateHandle = savedStateHandle,
                ioDispatcher = testDispatcher
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
            assertThat(capturedEffects).hasSize(2)
            assertThat(capturedEffects).containsExactly(
                ReviewScreenEffect.NavigateBack,
                ReviewScreenEffect.NavigateBack
            )

            job.cancel()
        }
}