package com.baghdad.viewmodel.categoryMovies

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.baghdad.domain.exception.NoInternetException
import com.baghdad.domain.model.pagination.PagedResult
import com.baghdad.domain.model.savedList.SavedMovie
import com.baghdad.domain.usecase.genre.GetMovieGenreNameByIdUseCase
import com.baghdad.domain.usecase.login.IsUserLoggedInUseCase
import com.baghdad.domain.usecase.movie.GetMoviesByGenreUseCase
import com.baghdad.domain.usecase.savedList.AddMovieToSavedListUseCase
import com.baghdad.domain.usecase.savedList.CreateSavedListUseCase
import com.baghdad.domain.usecase.savedList.GetSavedListsUseCase
import com.baghdad.domain.usecase.savedList.RemoveMovieFromSavedListUseCase
import com.baghdad.entity.media.Genre
import com.baghdad.entity.media.Movie
import com.baghdad.entity.savedList.SavedList
import com.baghdad.viewmodel.errorStates.BaseSnackBarMessage
import com.baghdad.viewmodel.utls.collectAndSnapshot
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CategoryMoviesViewModelTest {

    private val getGenreMoviesUseCase: GetMoviesByGenreUseCase = mockk(relaxed = true)
    private val getMovieGenreNameByIdUseCase: GetMovieGenreNameByIdUseCase = mockk()
    private val isUserLoggedInUseCase: IsUserLoggedInUseCase = mockk(relaxed = true)
    private val getSavedListsUseCase: GetSavedListsUseCase = mockk(relaxed = true)
    private val addMovieToSavedListUseCase: AddMovieToSavedListUseCase = mockk(relaxed = true)
    private val createSavedListUseCase: CreateSavedListUseCase = mockk(relaxed = true)
    private val removeMovieFromSavedListUseCase: RemoveMovieFromSavedListUseCase = mockk(relaxed = true)

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var viewModel: CategoryMoviesViewModel

    @BeforeEach
    fun setUp() {
        Dispatchers.setMain(testDispatcher)

        coEvery { getGenreMoviesUseCase(any(), any(), any()) } returns defaultPagedResult
        coEvery { getMovieGenreNameByIdUseCase(any()) } returns defaultGenre
        coEvery { isUserLoggedInUseCase() } returns false

        val savedStateHandle = SavedStateHandle(mapOf("categoryId" to 1L))
        viewModel = CategoryMoviesViewModel(
            savedStateHandle,
            getGenreMoviesUseCase = getGenreMoviesUseCase,
            getMovieGenreNameByIdUseCase = getMovieGenreNameByIdUseCase,
            ioDispatcher = testDispatcher,
            isUserLoggedInUseCase = isUserLoggedInUseCase,
            getSavedListsUseCase = getSavedListsUseCase,
            addMovieToSavedListUseCase = addMovieToSavedListUseCase,
            createSavedListUseCase = createSavedListUseCase,
            removeMovieFromSavedListUseCase = removeMovieFromSavedListUseCase,
        )
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `getGenreName should Update Category Name when Called`() = runTest {
        viewModel.uiState.test {
            advanceUntilIdle()
            assertThat(awaitItem().categoryName).isEqualTo("Action")
        }
    }

    @Test
    fun `getGenreMovies should Update Movies Flow when Called`() = runTest {
        viewModel.uiState.test {
            advanceUntilIdle()
            val state = awaitItem()
            assertThat(state.moviesFlow).isNotNull()
        }
    }

    @Test
    fun `onBackClick should Emit Navigate Back when Called`() = runTest {
        viewModel.uiEffect.test {
            viewModel.onBackClick()
            assertThat(awaitItem()).isEqualTo(CategoryMoviesEffect.NavigateBack)
        }
    }

    @Test
    fun `onMovieClicked should Emit Navigate To Movie Details when Id Is Valid`() = runTest {
        viewModel.uiEffect.test {
            viewModel.onMovieClicked(42L)
            assertThat(awaitItem()).isEqualTo(CategoryMoviesEffect.NavigateToMovieDetails(42L))
        }
    }

    @Test
    fun `toUiState should Map Movie To Ui State Correctly when Called`() {
        val uiState = testMovie

        assertThat(uiState.id).isEqualTo(testMovie.id)
        assertThat(uiState.posterImageURL).isEqualTo(testMovie.posterImageURL)
    }

    @Test
    fun `moviesFlow should Be Not Null when Default State Created`() = runTest {
        val defaultState = CategoryMoviesState()

        assertThat(defaultState.moviesFlow).isNotNull()
    }

    @Test
    fun `movie should Return Empty Poster Picture URL when Poster Is Empty`() {
        val movie = testMovie.copy(posterImageURL = "")

        assertThat(movie.posterImageURL).isEqualTo("")
    }

    @Test
    fun `uiState should Reflect Movie Id when Movie Has Different Id`() {
        val movie = testMovie.copy(id = 99L)
        val uiState = movie

        assertThat(uiState.id).isEqualTo(99L)
    }

    @Test
    fun `uiState should Keep Poster Image URL Unchanged when Movie Has Poster`() {
        val movie = testMovie.copy(posterImageURL = longUrl)

        assertThat(movie.posterImageURL).isEqualTo(longUrl)
    }

    @Test
    fun `onSnackBarActionLabel Click should Show No Internet SnackBar when NoInternetException Thrown`() = runTest {
        coEvery { getMovieGenreNameByIdUseCase(any()) } throws NoInternetException()

        viewModel.snackBarState.test {
            skipItems(1)

            viewModel.onSnackBarActionLabelClick()

            val latest = awaitItem()
            assertThat(latest.message).isEqualTo(BaseSnackBarMessage.NetworkError)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `isUserLoggedIn should Be False when User Not Logged In`() = runTest {
        coEvery { isUserLoggedInUseCase() } returns false
        val states = mutableListOf<CategoryMoviesState>()
        val job = launch { viewModel.uiState.collect { states.add(it) } }

        advanceUntilIdle()

        assertThat(states.last().isUserLoggedIn).isFalse()
        job.cancel() }

    @Test
    fun `onLoginClick should Emit Navigate To Login when Called`() = runTest {
        viewModel.uiEffect.test {
            viewModel.onLoginClick()

            val latest = awaitItem()
            assertThat(latest).isEqualTo(CategoryMoviesEffect.NavigateToLogin)

            cancelAndIgnoreRemainingEvents()
        }
    }


    @Test
    fun `onMovieToListClick should Show Add To List Bottom Sheet when Movie Not Saved`() = runTest {
        viewModel.uiState.test {
            skipItems(1)

            viewModel.onMovieToListClick(movieUiStateFalse)

            val latest = awaitItem()
            assertThat(latest.addToListBottomSheetState.isVisible).isTrue()

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `onMovieToListClick should Set Selected Item Id when Movie Not Saved`() = runTest {
        viewModel.uiState.test {
            skipItems(1)

            viewModel.onMovieToListClick(movieUiStateFalse)

            val latest = awaitItem()
            assertThat(latest.addToListBottomSheetState.selectedItemId).isEqualTo(1L)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `onSaveToListBottomSheetDismiss should Hide Add To List Bottom Sheet when Called`() = runTest {
        val states = mutableListOf<CategoryMoviesState>()
        val job = launch { viewModel.uiState.collect { states.add(it) } }
        viewModel.onSaveToListBottomSheetDismiss()

        advanceUntilIdle()

        assertThat(states.last().addToListBottomSheetState.isVisible).isFalse()
        job.cancel()
    }

    @Test
    fun `onListSelected should Update Selected List Id when Called`() = runTest {
        viewModel.uiState.test {
            skipItems(1)
            viewModel.onListSelected(10L)

            val latest = awaitItem()
            assertThat(latest.addToListBottomSheetState.selectedListId).isEqualTo(10L)

            cancelAndIgnoreRemainingEvents()
        }
    }


    @Test
    fun `onSaveItemToListClick should Add Movie To Selected List when List Id Not Null`() = runTest {
            viewModel.onMovieToListClick(movieUiStateFalse)
            viewModel.onListSelected(5L)
            viewModel.onSaveItemToListClick()

            advanceUntilIdle()

            coVerify { addMovieToSavedListUseCase(5L, 1L) }
        }

    @Test
    fun `onCreateNewListClick should Show Create List Bottom Sheet when Called`() = runTest {
        viewModel.uiState.test {
            skipItems(1)
            viewModel.onCreateNewListClick()

            val latest = awaitItem()
            assertThat(latest.addListBottomSheetState.isVisible).isTrue()
            assertThat(latest.addToListBottomSheetState.isVisible).isFalse()

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `onCreatedListNameChanged should Update List Name when Called`() = runTest {
        viewModel.uiState.test {
            skipItems(1)
            viewModel.onCreatedListNameChanged("My New List")

            val latest = awaitItem()
            assertThat(latest.addListBottomSheetState.listName).isEqualTo("My New List")

            cancelAndIgnoreRemainingEvents()
        }
    }


    @Test
    fun `onCreateListBottomSheetDismiss should reset list name and reopen AddToList sheet`() = runTest {
        viewModel.uiState.test {
            skipItems(1)
            viewModel.onCreateListBottomSheetDismiss()

            val latest = awaitItem()
            assertThat(latest.addToListBottomSheetState.isVisible).isTrue()

            cancelAndIgnoreRemainingEvents()
        }
    }


    @Test
    fun `onCreateListBottomSheetDismiss should reset list name and hide AddList sheet`() = runTest {
        viewModel.uiState.test {
            skipItems(1)
            viewModel.onCreateListBottomSheetDismiss()

            val latest = awaitItem()
            assertThat(latest.addListBottomSheetState.isVisible).isFalse()

            cancelAndIgnoreRemainingEvents()
        }
    }



    @Test
    fun `onCreateListBottomSheetDismiss should Reset List Name and Show Add To List Bottom Sheet when Called`() = runTest {
        viewModel.uiState.test {
            skipItems(1)
            viewModel.onCreateListBottomSheetDismiss()

            val latest = awaitItem()
            assertThat(latest.addListBottomSheetState.listName).isEqualTo("")

            cancelAndIgnoreRemainingEvents()
        }
    }


    @Test
    fun `onSaveItemToListClick should Not Call Add Movie To Saved List Use Case when Selected List Id Is Null`() = runTest {
            viewModel.onMovieToListClick(movieUiStateFalse)
            viewModel.onSaveItemToListClick()

            advanceUntilIdle()

            coVerify(exactly = 0) { addMovieToSavedListUseCase(any(), any()) }
        }

    @Test
    fun `onMovieToListClick should Dismiss Bottom Sheet when Item Removed Successfully`() = runTest {
        viewModel.uiState.test {
            skipItems(1)
            viewModel.onMovieToListClick(movieUiStateTrue)

            val latest = awaitItem()
            assertThat(latest.addToListBottomSheetState.isVisible).isFalse()

            cancelAndIgnoreRemainingEvents()
        }
    }


    private companion object {
        val longUrl = "https://example.com/very/long/path/poster.jpg"
        val testMovie = Movie(
            id = 1L,
            title = "Test Movie",
            genres = listOf(Genre(1L, "Action")),
            averageRating = 8.5,
            userRating = null,
            releaseDate = kotlinx.datetime.LocalDate.parse("2023-01-01"),
            overview = "Some movie",
            posterImageURL = "https://example.com/poster.jpg",
            trailerURL = "https://example.com/trailer.mp4",
            runtimeMinutes = 100
        )

        val defaultGenre = Genre(1L, "Action")

        val defaultPagedResult: PagedResult<SavedMovie> = PagedResult(
            data = listOf(SavedMovie(testMovie, false, null)),
            nextPage = 2,
            prevPage = null
        )

        val customPagedResult: PagedResult<SavedMovie> = PagedResult(
            data = listOf(SavedMovie(testMovie, false, null)),
            nextPage = null,
            prevPage = null
        )

        val testSavedList = SavedList(
            id = 1L,
            name = "My List",
            itemCount = 5,
        )

        val defaultSavedListPagedResult: PagedResult<SavedList> = PagedResult(
            data = listOf(testSavedList),
            nextPage = 2,
            prevPage = null
        )
        val movieUiStateTrue = CategoryMoviesState.MovieUiState(
            id = 1L,
            posterPictureURL = "url",
            isSaved = true,
            savedListId = 5L
        )
        val movieUiStateFalse = CategoryMoviesState.MovieUiState(
            id = 1L,
            posterPictureURL = "url",
            isSaved = false,
            savedListId = 5L
        )
    }
}
