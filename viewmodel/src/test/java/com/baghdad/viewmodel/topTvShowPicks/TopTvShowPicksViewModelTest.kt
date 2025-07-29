package com.baghdad.viewmodel.topTvShowPicks

import com.baghdad.domain.usecase.actor.GetActorTvShowUseCase
import com.baghdad.entity.media.Genre
import com.baghdad.entity.media.TvShow
import com.baghdad.viewmodel.errorStates.BaseSnackBarMessage
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlinx.datetime.LocalDate
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
class TopTvShowPicksViewModelTest {

    private lateinit var getActorTvShowUseCase: GetActorTvShowUseCase
    private lateinit var viewModel: TopTvShowPicksViewModel
    private val actorId = 123L
    private val tvShowId = 1L
    private val testDispatcher = StandardTestDispatcher()

    @BeforeEach
    fun setUp() = runTest {
        Dispatchers.setMain(testDispatcher)
        getActorTvShowUseCase = mockk(relaxed = true)
        coEvery { getActorTvShowUseCase(actorId) } returns mockedTvShow()
        viewModel = TopTvShowPicksViewModel(actorId, getActorTvShowUseCase, testDispatcher)
        advanceUntilIdle()
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
        testDispatcher.scheduler.cancel()
        TestScope().cancel()
    }


    @Test
    fun `onTvShowDetailsClick should send NavigateToMovieDetails effect`() = runTest {
        // Given
        var receivedEffect: TopTvShowPicksEffect? = null
        val job = launch {
            viewModel.uiEffect.collect { effect ->
                receivedEffect = effect
            }
        }
        // When
        viewModel.onTvShowDetailsClick(tvShowId)
        advanceUntilIdle()
        // Then
        assertTrue(receivedEffect is TopTvShowPicksEffect.NavigateToTvShowDetails)
        assertEquals(
            tvShowId,
            (receivedEffect as TopTvShowPicksEffect.NavigateToTvShowDetails).tvShowId
        )
        job.cancel()
    }


    @Test
    fun `onSaveTvShowClick should toggle isSaved state for specific movie`() = runTest {
        advanceUntilIdle()
        // Given
        val initialState = viewModel.uiState.value
        val initialMovie = initialState.tvShows.find { it.id == tvShowId }
        assertEquals(false, initialMovie?.isSaved)
        // When
        viewModel.onSaveTvShowClick(tvShowId)
        advanceUntilIdle()
        // Then
        val updatedState = viewModel.uiState.value
        val updatedMovie = updatedState.tvShows.find { it.id == tvShowId }
        assertTrue(updatedMovie?.isSaved == true)
    }

    @Test
    fun `onBackClick should send NavigateBack effect`() = runTest {
        // Given
        var receivedEffect: TopTvShowPicksEffect? = null
        val job = launch {
            viewModel.uiEffect.collect { effect ->
                receivedEffect = effect
            }
        }
        // When
        viewModel.onBackClick()
        advanceUntilIdle()
        // Then
        assertTrue(receivedEffect is TopTvShowPicksEffect.NavigateBack)
        job.cancel()
    }

    @Test
    fun `mapThrowableToErrorMessage should return UnknownError`() {
        // Given
        val throwable = RuntimeException("Test error")
        // When
        val result = viewModel.mapThrowableToErrorMessage(throwable)
        // Then
        assertEquals(BaseSnackBarMessage.UnknownError, result)
    }


    companion object {
        private fun mockedTvShow(): List<TvShow> = listOf(
            TvShow(
                id = 1L,
                title = "Test TvShow 1",
                genres = listOf(Genre(16L, "Animation")),
                averageRating = 8.0,
                userRating = 7.5,
                releaseDate = LocalDate.parse("2023-01-01"),
                overview = "Test movie overview 1",
                posterImageURL = "/movie_poster_1.jpg",
                trailerURL = "https://youtube.com/watch?v=test1",
                headerImagesURLs = listOf(
                    "/header_image_1.jpg",
                    "/header_image_2.jpg"
                ),
                numberOfSeasons = 3,
            ),
            TvShow(
                id = 2L,
                title = "Test TvShow 2",
                genres = listOf(Genre(35L, "Comedy")),
                averageRating = 7.5,
                userRating = 8.0,
                releaseDate = LocalDate.parse("2023-02-01"),
                overview = "Test movie overview 2",
                posterImageURL = "/movie_poster_2.jpg",
                trailerURL = "https://youtube.com/watch?v=test2",
                headerImagesURLs = listOf(
                    "/header_image_1.jpg",
                    "/header_image_2.jpg"
                ),
                numberOfSeasons = 1,
            ),
            TvShow(
                id = 3L,
                title = "Test TvShow 3",
                genres = listOf(Genre(18L, "Drama")),
                averageRating = 9.0,
                userRating = 8.5,
                releaseDate = LocalDate.parse("2023-03-01"),
                overview = "Test movie overview 3",
                posterImageURL = "/movie_poster_3.jpg",
                trailerURL = "https://youtube.com/watch?v=test3",
                headerImagesURLs = listOf(
                    "/header_image_1.jpg",
                    "/header_image_2.jpg"
                ),
                numberOfSeasons = 12,
            )
        )
    }
}