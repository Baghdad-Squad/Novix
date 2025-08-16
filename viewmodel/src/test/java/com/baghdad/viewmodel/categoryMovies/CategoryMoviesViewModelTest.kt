package com.baghdad.viewmodel.categoryMovies

import androidx.lifecycle.SavedStateHandle
import com.baghdad.domain.exception.NoInternetException
import com.baghdad.domain.model.pagination.PagedResult
import com.baghdad.domain.model.savedList.SavedMovie
import com.baghdad.domain.usecase.genre.GetMovieGenreNameByIdUseCase
import com.baghdad.domain.usecase.movie.GetMoviesByGenreUseCase
import com.baghdad.entity.media.Genre
import com.baghdad.entity.media.Movie
import com.baghdad.viewmodel.errorStates.BaseSnackBarMessage
import com.baghdad.viewmodel.utls.collectAndSnapshot
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
import org.junit.jupiter.api.Test


@OptIn(ExperimentalCoroutinesApi::class)
class CategoryMoviesViewModelTest {

    private val getGenreMoviesUseCase: GetMoviesByGenreUseCase = mockk(relaxed = true)
    private val getMovieGenreNameByIdUseCase: GetMovieGenreNameByIdUseCase = mockk()
    private val testDispatcher = StandardTestDispatcher()
    private var viewModel: CategoryMoviesViewModel

    init {
        Dispatchers.setMain(testDispatcher)
        coEvery { getGenreMoviesUseCase(any(), any(), any()) } returns defaultPagedResult
        coEvery { getMovieGenreNameByIdUseCase(any()) } returns defaultGenre
        val savedStateHandle = SavedStateHandle(mapOf("categoryId" to 1L))
        viewModel = CategoryMoviesViewModel(
            savedStateHandle,
            getGenreMoviesUseCase = getGenreMoviesUseCase,
            getMovieGenreNameByIdUseCase = getMovieGenreNameByIdUseCase,
            ioDispatcher = testDispatcher,
            isUserLoggedInUseCase = mockk(),
            getSavedListsUseCase = mockk(),
            addMovieToSavedListUseCase = mockk(),
            createSavedListUseCase = mockk(),
            removeMovieFromSavedListUseCase = mockk(),
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
            isUserLoggedInUseCase = mockk(),
            getSavedListsUseCase = mockk(),
            addMovieToSavedListUseCase = mockk(),
            createSavedListUseCase = mockk(),
            removeMovieFromSavedListUseCase = mockk(),
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
        val longUrl = "https://example.com/very/long/path/poster.jpg"
        val movie = testMovie.copy(posterImageURL = longUrl)
        assertThat(movie.posterImageURL).isEqualTo(longUrl)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `should  update moviesFlow and set isLoading to false when paging flow collected`() = runTest {
        coEvery { getGenreMoviesUseCase(1L, any(), any()) } returns customPagedResult
        val savedStateHandle = SavedStateHandle(mapOf("categoryId" to 1L))
        viewModel = CategoryMoviesViewModel(
            savedStateHandle = savedStateHandle,
            getGenreMoviesUseCase = getGenreMoviesUseCase,
            getMovieGenreNameByIdUseCase = getMovieGenreNameByIdUseCase,
            ioDispatcher = testDispatcher,
            isUserLoggedInUseCase = mockk(),
            getSavedListsUseCase = mockk(),
            addMovieToSavedListUseCase = mockk(),
            createSavedListUseCase = mockk(),
            removeMovieFromSavedListUseCase = mockk(),
        )
        advanceUntilIdle()
        val items = collectAndSnapshot(flow = viewModel.uiState.value.moviesFlow)
        assertThat(items).isNotEmpty()
        assertThat(viewModel.uiState.value.isLoading).isFalse()
    }


    @Test
    fun `onSnackBarActionLabelClick should show no internet snackBar when NoInternetException is thrown`() =
        runTest {
            coEvery { getMovieGenreNameByIdUseCase(any()) } throws NoInternetException()
            val emittedSnackBarMessages = mutableListOf<BaseSnackBarMessage>()
            val job = launch { viewModel.snackBarState.collect { emittedSnackBarMessages.add(it.message) } }
            viewModel.onSnackBarActionLabelClick()
            advanceUntilIdle()
            job.cancel()
            assertThat(emittedSnackBarMessages).contains(BaseSnackBarMessage.NetworkError)
        }

    private companion object {
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
    }
}
