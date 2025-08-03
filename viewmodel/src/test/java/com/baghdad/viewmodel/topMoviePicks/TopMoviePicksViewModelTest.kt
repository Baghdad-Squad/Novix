package com.baghdad.viewmodel.topMoviePicks

import androidx.lifecycle.SavedStateHandle
import com.baghdad.domain.exception.NoInternetException
import com.baghdad.domain.usecase.actor.GetActorMoviesUseCase
import com.baghdad.entity.media.Genre
import com.baghdad.entity.media.Movie
import com.baghdad.viewmodel.errorStates.BaseSnackBarMessage
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
import kotlinx.datetime.LocalDate
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
class TopMoviePicksViewModelTest {
    private lateinit var getActorMoviesUseCase: GetActorMoviesUseCase
    private lateinit var topMoviePicksViewModel: TopMoviePicksViewModel
    private val actorId = 123L
    private val movieId = 1L
    private val testDispatcher = StandardTestDispatcher()

    @BeforeEach
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        getActorMoviesUseCase = mockk(relaxed = true)
        coEvery { getActorMoviesUseCase(actorId) } returns mockedMovies()

        topMoviePicksViewModel =
            TopMoviePicksViewModel(savedStateHandle, getActorMoviesUseCase, testDispatcher)
    }


    @Test
    fun `onMovieDetailsClick should Navigate To MovieDetails when clicked`() = runTest {
        // Given
        var receivedEffect: TopMoviePicksEffect? = null
        val job = launch {
            topMoviePicksViewModel.uiEffect.collect { effect ->
                receivedEffect = effect
            }
        }
        // When
        topMoviePicksViewModel.onMovieDetailsClick(movieId)
        advanceUntilIdle()
        // Then
        assertThat(receivedEffect is TopMoviePicksEffect.NavigateToMovieDetails).isTrue()
        assertThat(movieId == (receivedEffect as TopMoviePicksEffect.NavigateToMovieDetails).movieId).isTrue()
        job.cancel()
    }

    @Test
    fun `onSaveMovieClick should toggle isSaved state for specific movie when clicked`() = runTest {
        // Given
        val initialState = topMoviePicksViewModel.uiState.value
        val initialMovie = initialState.movies.find { it.id == movieId }
        assertTrue(false == initialMovie?.isSaved)
        // When
        topMoviePicksViewModel.onSaveMovieClick(movieId)
        // Then
        val updatedState = topMoviePicksViewModel.uiState.value
        val updatedMovie = updatedState.movies.find { it.id == movieId }
        assertThat(updatedMovie?.isSaved == true).isTrue()
    }

    @Test
    fun `onBackClick should Navigate Back when clicked`() = runTest {
        // Given
        var receivedEffect: TopMoviePicksEffect? = null
        val job = launch {
            topMoviePicksViewModel.uiEffect.collect { effect ->
                receivedEffect = effect
            }
        }
        // When
        topMoviePicksViewModel.onBackClick()
        advanceUntilIdle()
        // Then
        assertThat(receivedEffect is TopMoviePicksEffect.NavigateBack).isTrue()
        job.cancel()
    }

    @Test
    fun `onSnackBarActionLabelClick should show no internet snackBar when NoInternetException is thrown`() =
        runTest {
            // Given
            coEvery { getActorMoviesUseCase(actorId) } throws NoInternetException()
            val emittedSnackBarMessages = mutableListOf<BaseSnackBarMessage>()

            val job = launch {
                topMoviePicksViewModel.snackBarState.collect {
                    emittedSnackBarMessages.add(it.message)
                }
            }

            // When
            topMoviePicksViewModel.onSnackBarActionLabelClick()
            advanceUntilIdle()
            job.cancel()

            // Then
            assertThat(emittedSnackBarMessages).contains(BaseSnackBarMessage.NetworkError)
        }


    companion object {
        private val savedStateHandle = SavedStateHandle(
            mapOf(
                "actorId" to 123L,
            )
        )
        private fun mockedMovies(): List<Movie> = listOf(
            Movie(
                id = 1L,
                title = "Test Movie 1",
                genres = listOf(Genre(28L, "Action")),
                averageRating = 8.0,
                userRating = 7.5,
                releaseDate = LocalDate.parse("2023-01-01"),
                overview = "Test movie overview 1",
                posterImageURL = "/movie_poster_1.jpg",
                runtimeMinutes = 120,
                trailerURL = "https://youtube.com/watch?v=test1"
            ),
            Movie(
                id = 2L,
                title = "Test Movie 2",
                genres = listOf(Genre(35L, "Comedy")),
                averageRating = 7.5,
                userRating = 8.0,
                releaseDate = LocalDate.parse("2023-02-01"),
                overview = "Test movie overview 2",
                posterImageURL = "/movie_poster_2.jpg",
                runtimeMinutes = 95,
                trailerURL = "https://youtube.com/watch?v=test2"
            ),
            Movie(
                id = 3L,
                title = "Test Movie 3",
                genres = listOf(Genre(18L, "Drama")),
                averageRating = 9.0,
                userRating = 8.5,
                releaseDate = LocalDate.parse("2023-03-01"),
                overview = "Test movie overview 3",
                posterImageURL = "/movie_poster_3.jpg",
                runtimeMinutes = 150,
                trailerURL = "https://youtube.com/watch?v=test3"
            )
        )
    }
}