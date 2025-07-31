package com.baghdad.viewmodel.topTvShowPicks

import com.baghdad.domain.usecase.actor.GetActorTvShowUseCase
import com.baghdad.entity.media.Genre
import com.baghdad.entity.media.TvShow
import com.google.common.truth.Truth.assertThat
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
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
class TopTvShowPicksViewModelTest {
    private lateinit var getActorTvShowUseCase: GetActorTvShowUseCase
    private lateinit var topTvShowPicksViewModel: TopTvShowPicksViewModel
    private val actorId = 123L
    private val tvShowId = 1L
    private val testDispatcher = StandardTestDispatcher()

    @BeforeEach
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        getActorTvShowUseCase = mockk(relaxed = true)
        coEvery { getActorTvShowUseCase(actorId) } returns mockedTvShow()
        topTvShowPicksViewModel =
            TopTvShowPicksViewModel(actorId, getActorTvShowUseCase, testDispatcher)
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
        testDispatcher.scheduler.cancel()
        TestScope().cancel()
    }


    @Test
    fun `onTvShowDetailsClick should Navigate To MovieDetails when clicked`() = runTest {
        // Given
        var receivedEffect: TopTvShowPicksEffect? = null
        val job = launch {
            topTvShowPicksViewModel.uiEffect.collect { effect ->
                receivedEffect = effect
            }
        }
        // When
        topTvShowPicksViewModel.onTvShowDetailsClick(tvShowId)
        advanceUntilIdle()
        // Then
        assertThat(receivedEffect is TopTvShowPicksEffect.NavigateToTvShowDetails).isTrue()
        assertThat(tvShowId == (receivedEffect as TopTvShowPicksEffect.NavigateToTvShowDetails).tvShowId).isTrue()
        job.cancel()
    }


    @Test
    fun `onSaveTvShowClick should toggle isSaved state for specific movie when clicked`() =
        runTest {
            advanceUntilIdle()
            // Given
            val initialState = topTvShowPicksViewModel.uiState.value
            val initialMovie = initialState.tvShows.find { it.id == tvShowId }
            assertTrue(false == initialMovie?.isSaved)
            // When
            topTvShowPicksViewModel.onSaveTvShowClick(tvShowId)
            advanceUntilIdle()
            // Then
            val updatedState = topTvShowPicksViewModel.uiState.value
            val updatedMovie = updatedState.tvShows.find { it.id == tvShowId }
            assertThat(updatedMovie?.isSaved == true).isTrue()
        }

    @Test
    fun `onBackClick should Navigate Back when clicked`() = runTest {
        // Given
        var receivedEffect: TopTvShowPicksEffect? = null
        val job = launch {
            topTvShowPicksViewModel.uiEffect.collect { effect ->
                receivedEffect = effect
            }
        }
        // When
        topTvShowPicksViewModel.onBackClick()
        advanceUntilIdle()
        // Then
        assertThat(receivedEffect is TopTvShowPicksEffect.NavigateBack).isTrue()
        job.cancel()
    }

//    @Test
//    fun `mapThrowableToErrorMessage should return UnknownError when mapping throwable to error message`() {
//        // Given
//        val throwable = RuntimeException("Test error")
//        // When
//        val result = topTvShowPicksViewModel.mapThrowableToErrorMessage(throwable)
//        // Then
//        assertThat(BaseSnackBarMessage.UnknownError == result).isTrue()
//    }


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