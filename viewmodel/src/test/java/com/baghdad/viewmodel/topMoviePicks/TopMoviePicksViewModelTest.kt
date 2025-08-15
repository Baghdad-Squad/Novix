package com.baghdad.viewmodel.topMoviePicks

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.baghdad.domain.exception.NoInternetException
import com.baghdad.domain.model.savedList.SavedMovie
import com.baghdad.domain.usecase.actor.GetActorMoviesUseCase
import com.baghdad.domain.usecase.login.IsUserLoggedInUseCase
import com.baghdad.domain.usecase.savedList.AddMovieToSavedListUseCase
import com.baghdad.domain.usecase.savedList.CreateSavedListUseCase
import com.baghdad.domain.usecase.savedList.GetSavedListsUseCase
import com.baghdad.domain.usecase.savedList.RemoveMovieFromSavedListUseCase
import com.baghdad.entity.media.Genre
import com.baghdad.entity.media.Movie
import com.baghdad.viewmodel.errorStates.BaseSnackBarMessage
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
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
    private val getActorMoviesUseCase = mockk<GetActorMoviesUseCase>()
    private val createSavedListUseCase = mockk<CreateSavedListUseCase>()
    private val isUserLoggedInUseCase = mockk<IsUserLoggedInUseCase>()
    private val addMovieToSavedListUseCase = mockk<AddMovieToSavedListUseCase>()
    private val removeMovieFromListUseCase = mockk<RemoveMovieFromSavedListUseCase>()
    private val getSavedListsUseCase = mockk<GetSavedListsUseCase>()
    private lateinit var viewModel: TopMoviePicksViewModel
    private val testDispatcher = StandardTestDispatcher()

    private val actorId = 123L
    private val movieId = 1L

    @BeforeEach
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        coEvery { getActorMoviesUseCase(actorId) } returns mockedMovies()

        viewModel = TopMoviePicksViewModel(
            savedStateHandle = savedStateHandle,
            getActorMoviesUseCase = getActorMoviesUseCase,
            createSavedListUseCase = createSavedListUseCase,
            ioDispatcher = testDispatcher,
            isUserLoggedInUseCase = isUserLoggedInUseCase,
            addMovieToSavedListUseCase = addMovieToSavedListUseCase,
            removeMovieFromListUseCase = removeMovieFromListUseCase,
            getSavedListsUseCase = getSavedListsUseCase,
        )
    }

    @Test
    fun `onMovieDetailsClicked should Navigate To MovieDetails when clicked`() = runTest {

        viewModel.onMovieDetailsClicked(movieId)

        viewModel.uiEffect.test {
            val effect = awaitItem()
            assertThat(effect is TopMoviePicksEffect.NavigateToMovieDetails).isTrue()
            assertThat((effect as TopMoviePicksEffect.NavigateToMovieDetails).movieId == movieId).isTrue()
        }
    }

    @Test
    fun `onSaveMovieClicked should open bottom sheet when movie is not saved`() = runTest {

        val initialState = viewModel.uiState.value
        val movie = initialState.movies.find { it.id == movieId }!!

        viewModel.onSaveMovieClicked(movie)
        advanceUntilIdle()

        val updatedState = viewModel.uiState.value
        assertThat(movie.isSaved).isFalse()
        assertThat(updatedState.addToListBottomSheetState.isVisible).isTrue()
        assertThat(updatedState.addToListBottomSheetState.selectedItemId).isEqualTo(movieId)
    }

    @Test
    fun `onSaveMovieClicked should remove movie from list when is saved`() = runTest {

        val initialState = viewModel.uiState.value
        val movie = initialState.movies.find { it.id == movieId }!!.copy(isSaved = true)
        coEvery { removeMovieFromListUseCase(any(), any()) } returns Unit

        viewModel.onSaveMovieClicked(movie)
        advanceUntilIdle()

        coVerify { removeMovieFromListUseCase(any(), any()) }
    }

    @Test
    fun `onSaveItemToListClicked should add movie to saved list when list is selected`() = runTest {

        viewModel.onListSelected(123L)
        coEvery { addMovieToSavedListUseCase(any(), any()) } returns Unit

        viewModel.onSaveItemToListClicked()
        advanceUntilIdle()

        coVerify { addMovieToSavedListUseCase(any(), any()) }
    }

    @Test
    fun `onSaveItemToListClicked should not add movie to saved list when list is not selected`() =
        runTest {

            coEvery { addMovieToSavedListUseCase(any(), any()) } returns Unit

            viewModel.onSaveItemToListClicked()
            advanceUntilIdle()

            coVerify(inverse = true) { addMovieToSavedListUseCase(any(), any()) }
        }

    @Test
    fun `onBackClicked should Navigate Back when clicked`() = runTest {

        viewModel.onBackClicked()

        viewModel.uiEffect.test {
            val effect = awaitItem()
            assertThat(effect is TopMoviePicksEffect.NavigateBack).isTrue()
        }
    }

    @Test
    fun `onCreatedListNameChanged should change list name when it is clicked`() = runTest {

        val listName = "action"

        viewModel.onCreatedListNameChanged(listName)
        advanceUntilIdle()

        assertThat(viewModel.uiState.value.addListBottomSheetState.listName == listName).isTrue()
    }

    @Test
    fun `onCreateListBottomSheetDismiss should hide bottom sheet and show addToListBottomSheet when it is clicked`() =
        runTest {

            viewModel.onCreateListBottomSheetDismiss()
            advanceUntilIdle()

            val addListBottomSheetState = viewModel.uiState.value.addListBottomSheetState
            val addToListBottomSheetState = viewModel.uiState.value.addToListBottomSheetState
            assertThat(addListBottomSheetState.isVisible == false).isTrue()
            assertThat(addListBottomSheetState.listName == "").isTrue()
            assertThat(addListBottomSheetState.isLoading == false).isTrue()
            assertThat(addToListBottomSheetState.isVisible == true).isTrue()
        }

    @Test
    fun `onCreateListBottomSheetAddClick should create new list when it is clicked`() = runTest {

        viewModel.onCreatedListNameChanged("action")
        coEvery { createSavedListUseCase(any()) } returns Unit

        viewModel.onCreateListBottomSheetAddClick()
        advanceUntilIdle()

        coVerify { createSavedListUseCase(any()) }
    }

    @Test
    fun `onSnackBarActionLabelClicked should show no internet snackBar when NoInternetException is thrown`() =
        runTest {

            coEvery { getActorMoviesUseCase(actorId) } throws NoInternetException()
            val emittedSnackBarMessages = mutableListOf<BaseSnackBarMessage>()
            val job = launch {
                viewModel.snackBarState.collect {
                    emittedSnackBarMessages.add(it.message)
                }
            }

            viewModel.onSnackBarActionLabelClicked()
            advanceUntilIdle()

            assertThat(emittedSnackBarMessages).contains(BaseSnackBarMessage.NetworkError)
            job.cancel()

        }

    companion object {
        private val savedStateHandle = SavedStateHandle(
            mapOf(
                "actorId" to 123L,
            )
        )

        private fun mockedMovies(): List<SavedMovie> = listOf(
            SavedMovie(
                movie = Movie(
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
                isSaved = false,
                listId = null,
            ),
            SavedMovie(
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
                isSaved = false,
                listId = null,
            ),
            SavedMovie(
                movie = Movie(
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
                ),
                isSaved = false,
                listId = null,
            ),
        )
    }
}
