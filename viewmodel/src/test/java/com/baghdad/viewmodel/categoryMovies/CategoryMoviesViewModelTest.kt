package com.baghdad.viewmodel.categoryMovies

import androidx.lifecycle.SavedStateHandle
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
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
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
    private var viewModel: CategoryMoviesViewModel

    init {
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

    @Test
    fun `should update categoryName when getGenreName called`() = runTest {
        val states = mutableListOf<CategoryMoviesState>()
        val job = launch { viewModel.uiState.collect { states.add(it) } }

        advanceUntilIdle()

        assertThat(states.last().categoryName).isEqualTo("Action")
        job.cancel()
    }

    @Test
    fun `should update moviesFlow when getGenreMovies called`() = runTest {
        val states = mutableListOf<CategoryMoviesState>()
        val job = launch { viewModel.uiState.collect { states.add(it) } }

        advanceUntilIdle()

        assertThat(states.last().moviesFlow).isNotNull()
        job.cancel()
    }

    @Test
    fun `should emit NavigateBack when onBackClicked`() = runTest {
        val effects = mutableListOf<CategoryMoviesEffect>()
        val job = launch { viewModel.uiEffect.collect { effects.add(it) } }

        viewModel.onBackClick()

        advanceUntilIdle()

        assertThat(effects).containsExactly(CategoryMoviesEffect.NavigateBack)
        job.cancel()
    }

    @Test
    fun `should emit NavigateToMovieDetails when onMovieClicked with valid ID`() = runTest {
        val effects = mutableListOf<CategoryMoviesEffect>()
        val job = launch { viewModel.uiEffect.collect { effects.add(it) } }

        viewModel.onMovieClicked(42L)

        advanceUntilIdle()

        assertThat(effects).containsExactly(CategoryMoviesEffect.NavigateToMovieDetails(42L))
        job.cancel()
    }

    @Test
    fun `should map Movie to MovieUiState correctly when toUiState called`() {
        val uiState = testMovie

        assertThat(uiState.id).isEqualTo(testMovie.id)
        assertThat(uiState.posterImageURL).isEqualTo(testMovie.posterImageURL)
    }

    @Test
    fun `should not crash when getMovieGenreNameByIdUseCase fails`() = runTest {
        coEvery { getMovieGenreNameByIdUseCase(1L) } throws RuntimeException("Failed")
        val savedStateHandle = SavedStateHandle(mapOf("categoryId" to 1L))
        viewModel = CategoryMoviesViewModel(
            savedStateHandle = savedStateHandle,
            getGenreMoviesUseCase = getGenreMoviesUseCase,
            getMovieGenreNameByIdUseCase = getMovieGenreNameByIdUseCase,
            ioDispatcher = testDispatcher,
            isUserLoggedInUseCase = isUserLoggedInUseCase,
            getSavedListsUseCase = getSavedListsUseCase,
            addMovieToSavedListUseCase = addMovieToSavedListUseCase,
            createSavedListUseCase = createSavedListUseCase,
            removeMovieFromSavedListUseCase = removeMovieFromSavedListUseCase,
        )
        val states = mutableListOf<CategoryMoviesState>()
        val job = launch { viewModel.uiState.collect { states.add(it) } }

        advanceUntilIdle()
        assertThat(states.last().categoryName).isEqualTo("")
        job.cancel()
    }

    @Test
    fun `should be empty when moviesFlow default`() = runTest {
        val defaultState = CategoryMoviesState()

        assertThat(defaultState.moviesFlow).isNotNull()
    }

    @Test
    fun `should return empty posterPictureURL when Movie has empty poster`() {
        val movie = testMovie.copy(posterImageURL = "")

        assertThat(movie.posterImageURL).isEqualTo("")
    }

    @Test
    fun `should reflect in uiState when Movie has different id`() {
        val movie = testMovie.copy(id = 99L)
        val uiState = movie

        assertThat(uiState.id).isEqualTo(99L)
    }

    @Test
    fun `should keep posterImageURL unchanged when mapping to uiState`() {
        val movie = testMovie.copy(posterImageURL = longUrl)

        assertThat(movie.posterImageURL).isEqualTo(longUrl)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `should update moviesFlow and set isLoading to false when paging flow collected`() = runTest {
        coEvery { getGenreMoviesUseCase(1L, any(), any()) } returns customPagedResult
        val savedStateHandle = SavedStateHandle(mapOf("categoryId" to 1L))
        viewModel = CategoryMoviesViewModel(
            savedStateHandle = savedStateHandle,
            getGenreMoviesUseCase = getGenreMoviesUseCase,
            getMovieGenreNameByIdUseCase = getMovieGenreNameByIdUseCase,
            ioDispatcher = testDispatcher,
            isUserLoggedInUseCase = isUserLoggedInUseCase,
            getSavedListsUseCase = getSavedListsUseCase,
            addMovieToSavedListUseCase = addMovieToSavedListUseCase,
            createSavedListUseCase = createSavedListUseCase,
            removeMovieFromSavedListUseCase = removeMovieFromSavedListUseCase,
        )

        advanceUntilIdle()
        val items = collectAndSnapshot(flow = viewModel.uiState.value.moviesFlow)
        assertThat(items).isNotEmpty()
        assertThat(viewModel.uiState.value.isLoading).isFalse()
    }

    @Test
    fun `onSnackBarActionLabelClick should show no internet snackBar when NoInternetException is thrown`() = runTest {
            coEvery { getMovieGenreNameByIdUseCase(any()) } throws NoInternetException()
            val emittedSnackBarMessages = mutableListOf<BaseSnackBarMessage>()
            val job = launch { viewModel.snackBarState.collect { emittedSnackBarMessages.add(it.message) } }
            viewModel.onSnackBarActionLabelClick()

            advanceUntilIdle()
            job.cancel()
            assertThat(emittedSnackBarMessages).contains(BaseSnackBarMessage.NetworkError)
        }

    @Test
    fun `should update isUserLoggedIn to true when user is logged in`() = runTest {
        coEvery { isUserLoggedInUseCase() } returns true
        coEvery { getSavedListsUseCase(any(), any()) } returns defaultSavedListPagedResult
        val savedStateHandle = SavedStateHandle(mapOf("categoryId" to 1L))
        viewModel = CategoryMoviesViewModel(
            savedStateHandle = savedStateHandle,
            getGenreMoviesUseCase = getGenreMoviesUseCase,
            getMovieGenreNameByIdUseCase = getMovieGenreNameByIdUseCase,
            ioDispatcher = testDispatcher,
            isUserLoggedInUseCase = isUserLoggedInUseCase,
            getSavedListsUseCase = getSavedListsUseCase,
            addMovieToSavedListUseCase = addMovieToSavedListUseCase,
            createSavedListUseCase = createSavedListUseCase,
            removeMovieFromSavedListUseCase = removeMovieFromSavedListUseCase,
        )
        val states = mutableListOf<CategoryMoviesState>()
        val job = launch { viewModel.uiState.collect { states.add(it) } }

        advanceUntilIdle()
        assertThat(states.last().isUserLoggedIn).isTrue()
        job.cancel()
    }

    @Test
    fun `should update isUserLoggedIn to false when user is not logged in`() = runTest {
        coEvery { isUserLoggedInUseCase() } returns false
        val states = mutableListOf<CategoryMoviesState>()
        val job = launch { viewModel.uiState.collect { states.add(it) } }

        advanceUntilIdle()
        assertThat(states.last().isUserLoggedIn).isFalse()
        job.cancel()
    }


    @Test
    fun `should emit NavigateToLogin when onLoginClick called`() = runTest {
        val effects = mutableListOf<CategoryMoviesEffect>()
        val job = launch { viewModel.uiEffect.collect { effects.add(it) } }
        viewModel.onLoginClick()

        advanceUntilIdle()
        assertThat(effects).containsExactly(CategoryMoviesEffect.NavigateToLogin)
        job.cancel()
    }

    @Test
    fun `should show addToListBottomSheet when onMovieToListClick called with unsaved item`() = runTest {
        val states = mutableListOf<CategoryMoviesState>()
        val job = launch { viewModel.uiState.collect { states.add(it) } }

        viewModel.onMovieToListClick(movieUiStateFalse)

        advanceUntilIdle()
        assertThat(states.last().addToListBottomSheetState.isVisible).isTrue()
        assertThat(states.last().addToListBottomSheetState.selectedItemId).isEqualTo(1L)
        job.cancel()
    }

    @Test
    fun `should dismiss addToListBottomSheet when onSaveToListBottomSheetDismiss called`() = runTest {
        val states = mutableListOf<CategoryMoviesState>()
        val job = launch { viewModel.uiState.collect { states.add(it) } }
        viewModel.onSaveToListBottomSheetDismiss()

        advanceUntilIdle()
        assertThat(states.last().addToListBottomSheetState.isVisible).isFalse()
        job.cancel()
    }

    @Test
    fun `should update selectedListId when onListSelected called`() = runTest {
        val states = mutableListOf<CategoryMoviesState>()
        val job = launch { viewModel.uiState.collect { states.add(it) } }
        viewModel.onListSelected(10L)

        advanceUntilIdle()
        assertThat(states.last().addToListBottomSheetState.selectedListId).isEqualTo(10L)
        job.cancel()
    }

    @Test
    fun `should add movie to selected list when onSaveItemToListClick called`() = runTest {
        viewModel.onMovieToListClick(movieUiStateFalse)
        viewModel.onListSelected(5L)
        coEvery { addMovieToSavedListUseCase(any(), any()) } returns Unit

        viewModel.onSaveItemToListClick()

        advanceUntilIdle()
        coVerify { addMovieToSavedListUseCase(5L, 1L) }
    }

    @Test
    fun `should show success snackbar when item saved successfully`() = runTest {
        viewModel.onMovieToListClick(movieUiStateFalse)
        viewModel.onListSelected(5L)
        coEvery { addMovieToSavedListUseCase(any(), any()) } returns Unit

        val snackBarMessages = mutableListOf<BaseSnackBarMessage>()
        val job = launch { viewModel.snackBarState.collect { snackBarMessages.add(it.message) } }
        viewModel.onSaveItemToListClick()

        advanceUntilIdle()

        assertThat(snackBarMessages).contains(BaseSnackBarMessage.SavedItemSuccessfully)
        job.cancel()
    }

    @Test
    fun `should show network error when adding item to list fails`() = runTest {
        viewModel.onMovieToListClick(movieUiStateFalse)
        viewModel.onListSelected(5L)
        coEvery { addMovieToSavedListUseCase(any(), any()) } throws RuntimeException()

        val snackBarMessages = mutableListOf<BaseSnackBarMessage>()
        val job = launch { viewModel.snackBarState.collect { snackBarMessages.add(it.message) } }
        viewModel.onSaveItemToListClick()

        advanceUntilIdle()

        assertThat(snackBarMessages).contains(BaseSnackBarMessage.NetworkError)
        job.cancel()
    }

    @Test
    fun `should show createListBottomSheet when onCreateNewListClick called`() = runTest {
        val states = mutableListOf<CategoryMoviesState>()
        val job = launch { viewModel.uiState.collect { states.add(it) } }
        viewModel.onCreateNewListClick()

        advanceUntilIdle()

        assertThat(states.last().addListBottomSheetState.isVisible).isTrue()
        assertThat(states.last().addToListBottomSheetState.isVisible).isFalse()
        job.cancel()
    }

    @Test
    fun `should update list name when onCreatedListNameChanged called`() = runTest {
        val states = mutableListOf<CategoryMoviesState>()
        val job = launch { viewModel.uiState.collect { states.add(it) } }
        viewModel.onCreatedListNameChanged("My New List")

        advanceUntilIdle()

        assertThat(states.last().addListBottomSheetState.listName).isEqualTo("My New List")
        job.cancel()
    }

    @Test
    fun `should dismiss createListBottomSheet and show addToListBottomSheet when onCreateListBottomSheetDismiss called`() = runTest {
        val states = mutableListOf<CategoryMoviesState>()
        val job = launch { viewModel.uiState.collect { states.add(it) } }

        viewModel.onCreateListBottomSheetDismiss()

        advanceUntilIdle()

        assertThat(states.last().addListBottomSheetState.isVisible).isFalse()
        assertThat(states.last().addListBottomSheetState.listName).isEqualTo("")
        assertThat(states.last().addToListBottomSheetState.isVisible).isTrue()
        job.cancel()
    }

    @Test
    fun `should not call addMovieToSavedListUseCase when selectedListId is null`() = runTest {
        viewModel.onMovieToListClick(movieUiStateFalse)

        viewModel.onSaveItemToListClick()

        advanceUntilIdle()

        coVerify(exactly = 0) { addMovieToSavedListUseCase(any(), any()) }
    }

    @Test
    fun `should dismiss addToListBottomSheet when item removed successfully`() = runTest {
        coEvery { removeMovieFromSavedListUseCase(any(), any()) } returns Unit

        val states = mutableListOf<CategoryMoviesState>()
        val job = launch { viewModel.uiState.collect { states.add(it) } }

        viewModel.onMovieToListClick(movieUiStateTrue)

        advanceUntilIdle()

        assertThat(states.last().addToListBottomSheetState.isVisible).isFalse()
        job.cancel()
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
            posterPictureURL = "url" ,
            isSaved = true,
            savedListId = 5L
        )
        val movieUiStateFalse = CategoryMoviesState.MovieUiState(
            id = 1L,
            posterPictureURL = "url",
            isSaved = false,
            savedListId = 0L
        )
    }
}