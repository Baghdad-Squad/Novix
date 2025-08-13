package com.baghdad.viewmodel.review

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.baghdad.domain.usecase.review.GetMovieReviewsUseCase
import com.baghdad.domain.usecase.review.GetTvShowReviewsUseCase
import com.baghdad.entity.media.Review
import com.google.common.truth.Truth.assertThat
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.*
import kotlinx.datetime.LocalDate
import org.junit.jupiter.api.*

@OptIn(ExperimentalCoroutinesApi::class)
class ReviewViewModelTest {

    private val mockGetMovieReviews = GetMovieReviewsUseCase(mockk())
    private val mockGetTvReviews = GetTvShowReviewsUseCase(mockk())
    private val testDispatcher = StandardTestDispatcher()

    private lateinit var reviewViewModel: ReviewViewModel

    private val mediaId = 123L

    @BeforeEach
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
        clearAllMocks()
    }

    private fun createSavedStateHandle(type: ContentType) =
        SavedStateHandle(mapOf("mediaId" to mediaId, "mediaType" to type))

    private fun createViewModel(type: ContentType) =
        ReviewViewModel(
            getMovieReviewsUseCase = mockGetMovieReviews,
            getTvShowReviewsUseCase = mockGetTvReviews,
            savedStateHandle = createSavedStateHandle(type),
            ioDispatcher = testDispatcher
        )

    @Test
    fun `should call movie reviews use case when media type is movie`() = runTest {
        coEvery { mockGetMovieReviews(mediaId) } returns emptyList()

        createViewModel(ContentType.MOVIE)
       advanceUntilIdle()

        coVerify(exactly = 1) { mockGetMovieReviews(mediaId) }
    }

    @Test
    fun `should call tv reviews use case when media type is series`() = runTest {
        coEvery { mockGetTvReviews(mediaId) } returns emptyList()

        createViewModel(ContentType.SERIES)
       advanceUntilIdle()

        coVerify(exactly = 1) { mockGetTvReviews(mediaId) }
    }

    @Test
    fun `should set state to empty when movie type has no reviews`() = runTest {
        coEvery { mockGetMovieReviews(mediaId) } returns emptyList()

        reviewViewModel = createViewModel(ContentType.MOVIE)
       advanceUntilIdle()

        assertThat(reviewViewModel.uiState.value.reviews).isEmpty()
    }

    @Test
    fun `should populate state with reviews when series type has reviews`() = runTest {
        val reviews = listOf(mockReview,secondMockReview)
        coEvery { mockGetTvReviews(mediaId) } returns reviews

        reviewViewModel = createViewModel(ContentType.SERIES)
       advanceUntilIdle()

        val state = reviewViewModel.uiState.value
        assertThat(state.isLoading).isFalse()
        assertThat(state.reviews).containsExactlyElementsIn(reviews.map { it.toUiState() })
    }

    @Test
    fun `should set state to empty when series type throws error`() = runTest {
        coEvery { mockGetTvReviews(mediaId) } throws RuntimeException()

        reviewViewModel = createViewModel(ContentType.SERIES)
       advanceUntilIdle()

        assertThat(reviewViewModel.uiState.value.isLoading).isFalse()
        assertThat(reviewViewModel.uiState.value.reviews).isEmpty()
    }

    @Test
    fun `should call tv reviews use case again when loadReviewsForSeries is invoked`() = runTest {
        coEvery { mockGetTvReviews(mediaId) } returns emptyList()

        reviewViewModel = createViewModel(ContentType.SERIES)
       advanceUntilIdle()

        reviewViewModel.loadReviewsForSeries()
       advanceUntilIdle()

        coVerify(exactly = 2) { mockGetTvReviews(mediaId) }
    }

    @Test
    fun `should Navigate Back when onNavigateBack is called`() = runTest {
        coEvery { mockGetMovieReviews(mediaId) } returns emptyList()
        reviewViewModel = createViewModel(ContentType.MOVIE)

        reviewViewModel.onNavigateBack()

        reviewViewModel.uiEffect.test {
            assertThat(awaitItem()).isEqualTo(ReviewScreenEffect.NavigateBack)
        }

    }


    companion object{
        val mockReview = Review(
            id = "1",
            authorName = "Smith",
            authorAvatarUrl ="https://example.com/avatar.jpg",
            contentTitle = "MovieBuff1967",
            rating = 5.5,
            reviewText = "Amazing plot!",
            postedDate = LocalDate(2023, 7, 5)
        )

        val secondMockReview = Review(
            id = "2",
            authorName = "max",
            authorAvatarUrl ="https://2example.com/avatar.jpg",
            contentTitle = "MovieBuff1968",
            rating = 5.5,
            reviewText = "Amazing plot!",
            postedDate = LocalDate(2023, 7, 5)
        )

        val reviewState = ReviewScreenState.ReviewUiState(
            id = "1",
            authorName = "Smith",
            authorAvatarUrl ="https://example.com/avatar.jpg",
            contentTitle = "MovieBuff1967",
            rating = 5.5,
            reviewText = "Amazing plot!",
            postedDate = "2023-07-05",
            isExpanded = false
        )

        val secondReviewState = ReviewScreenState.ReviewUiState(
            id = "2",
            authorName = "max",
            authorAvatarUrl ="https://2example.com/avatar.jpg",
            contentTitle = "MovieBuff1968",
            rating = 5.5,
            reviewText = "Amazing plot!",
            postedDate = "2023-07-05",
            isExpanded = false
        )

    }
}
