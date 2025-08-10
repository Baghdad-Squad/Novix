package com.baghdad.viewmodel.topMoviePicks

import androidx.lifecycle.SavedStateHandle
import com.baghdad.domain.exception.NoInternetException
import com.baghdad.domain.usecase.actor.GetActorMoviesUseCase
import com.baghdad.domain.usecase.login.IsUserLoggedInUseCase
import com.baghdad.domain.usecase.savedList.AddMovieToSavedListUseCase
import com.baghdad.domain.usecase.savedList.CreateSavedListUseCase
import com.baghdad.domain.usecase.savedList.GetSavedListsUseCase
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
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
class TopMoviePicksViewModelTest {
    private lateinit var getActorMoviesUseCase: GetActorMoviesUseCase
    private lateinit var createSavedListUseCase: CreateSavedListUseCase
    private lateinit var isUserLoggedInUseCase: IsUserLoggedInUseCase
    private lateinit var addMovieToSavedListUseCase: AddMovieToSavedListUseCase
    private lateinit var removeMovieFromListUseCase: AddMovieToSavedListUseCase
    private lateinit var getSavedListsUseCase: GetSavedListsUseCase
    private lateinit var topMoviePicksViewModel: TopMoviePicksViewModel
    private val actorId = 123L
    private val movieId = 1L
    private val testDispatcher = StandardTestDispatcher()

    @BeforeEach
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        getActorMoviesUseCase = mockk(relaxed = true)
        createSavedListUseCase = mockk(relaxed = true)
        isUserLoggedInUseCase = mockk(relaxed = true)
        addMovieToSavedListUseCase = mockk(relaxed = true)
        removeMovieFromListUseCase = mockk(relaxed = true)
        getSavedListsUseCase = mockk(relaxed = true)

        coEvery { getActorMoviesUseCase(actorId) } returns mockedMovies()

        topMoviePicksViewModel = TopMoviePicksViewModel(
            savedStateHandle = savedStateHandle,
            getActorMoviesUseCase = getActorMoviesUseCase,
            createSavedListUseCase = createSavedListUseCase,
            ioDispatcher = testDispatcher,
            isUserLoggedInUseCase = isUserLoggedInUseCase,
            addMovieToSavedListUseCase = addMovieToSavedListUseCase,
            removeMovieFromListUseCase = removeMovieFromListUseCase,
            getSavedListsUseCase = getSavedListsUseCase,
            defaultDispatcher = testDispatcher
        )
    }

    @Test
    fun `onMovieDetailsClicked should Navigate To MovieDetails when clicked`() = runTest {
        // Given
        var receivedEffect: TopMoviePicksEffect? = null
        val job = launch {
            topMoviePicksViewModel.uiEffect.collect { effect ->
                receivedEffect = effect
            }
        }
        // When
        topMoviePicksViewModel.onMovieDetailsClicked(movieId)
        advanceUntilIdle()
        // Then
        assertThat(receivedEffect is TopMoviePicksEffect.NavigateToMovieDetails).isTrue()
        assertThat(movieId == (receivedEffect as TopMoviePicksEffect.NavigateToMovieDetails).movieId).isTrue()
        job.cancel()
    }

    @Test
    fun `onSaveMovieClicked should open bottom sheet when movie is not saved`() = runTest {
        // Given
        val initialState = topMoviePicksViewModel.uiState.value
        val movie = initialState.movies.find { it.id == movieId }!!
        assertThat(movie.isSaved).isFalse()

        // When
        topMoviePicksViewModel.onSaveMovieClicked(movie)
        advanceUntilIdle()

        // Then
        val updatedState = topMoviePicksViewModel.uiState.value
        assertThat(updatedState.addToListBottomSheetState.isVisible).isTrue()
        assertThat(updatedState.addToListBottomSheetState.selectedItemId).isEqualTo(movieId)
    }


    @Test
    fun `onBackClicked should Navigate Back when clicked`() = runTest {
        // Given
        var receivedEffect: TopMoviePicksEffect? = null
        val job = launch {
            topMoviePicksViewModel.uiEffect.collect { effect ->
                receivedEffect = effect
            }
        }
        // When
        topMoviePicksViewModel.onBackClicked()
        advanceUntilIdle()
        // Then
        assertThat(receivedEffect is TopMoviePicksEffect.NavigateBack).isTrue()
        job.cancel()
    }

    @Test
    fun `onSnackBarActionLabelClicked should show no internet snackBar when NoInternetException is thrown`() =
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
            topMoviePicksViewModel.onSnackBarActionLabelClicked()
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