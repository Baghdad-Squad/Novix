package com.baghdad.viewmodel.categoryMovies

import com.baghdad.domain.exception.NoInternetException
import com.baghdad.domain.model.PagedResult
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
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlinx.datetime.LocalDate
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CategoryMoviesViewModelTest {

    private val getGenreMoviesUseCase: GetMoviesByGenreUseCase = mockk(relaxed = true)
    private val getMovieGenreNameByIdUseCase: GetMovieGenreNameByIdUseCase = mockk()
    private lateinit var viewModel: CategoryMoviesViewModel
    private val testDispatcher = StandardTestDispatcher()

    @BeforeEach
    fun setup() {
        Dispatchers.setMain(testDispatcher)

        coEvery { getGenreMoviesUseCase(any(), any()) } returns PagedResult(
            data = listOf(testMovie),
            nextKey = 2,
            prevKey = null
        )

        coEvery { getMovieGenreNameByIdUseCase(any()) } returns Genre(1L, "Action")

        viewModel = CategoryMoviesViewModel(
            genreId = 1L,
            getGenreMoviesUseCase = getGenreMoviesUseCase,
            getMovieGenreNameByIdUseCase = getMovieGenreNameByIdUseCase,
            ioDispatcher = testDispatcher
        )
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
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

        viewModel.onBackClicked()
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
        val uiState = testMovie.toUiState()

        assertThat(uiState.id).isEqualTo(testMovie.id)
        assertThat(uiState.posterPictureURL).isEqualTo(testMovie.posterImageURL)
        assertThat(uiState.isSaved).isFalse()
    }

    @Test
    fun `should not crash when getMovieGenreNameByIdUseCase fails`() = runTest {
        coEvery { getMovieGenreNameByIdUseCase(1L) } throws RuntimeException("Failed")
        viewModel = CategoryMoviesViewModel(
            1L,
            getGenreMoviesUseCase,
            getMovieGenreNameByIdUseCase,
            testDispatcher
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
        val uiState = movie.toUiState()

        assertThat(uiState.posterPictureURL).isEqualTo("")
    }

    @Test
    fun `should reflect in uiState when Movie has different id`() {
        val movie = testMovie.copy(id = 99L)
        val uiState = movie.toUiState()

        assertThat(uiState.id).isEqualTo(99L)
    }

    @Test
    fun `should keep posterImageURL unchanged when mapping to uiState`() {
        val longUrl = "https://example.com/very/long/path/poster.jpg"
        val movie = testMovie.copy(posterImageURL = longUrl)
        val uiState = movie.toUiState()

        assertThat(uiState.posterPictureURL).isEqualTo(longUrl)
    }

    @Test
    fun `should  update moviesFlow and set isLoading to false when paging flow collected`() =
        runTest {
            // Given
            coEvery { getGenreMoviesUseCase(1L, any()) } returns PagedResult(
                data = listOf(testMovie),
                nextKey = null,
                prevKey = null
            )

            viewModel = CategoryMoviesViewModel(
                genreId = 1L,
                getGenreMoviesUseCase = getGenreMoviesUseCase,
                getMovieGenreNameByIdUseCase = getMovieGenreNameByIdUseCase,
                ioDispatcher = testDispatcher
            )

            advanceUntilIdle()
            // When
            val items = collectAndSnapshot(
                flow = viewModel.uiState.value.moviesFlow,
            )

            // Then
            assertThat(items).isNotEmpty()
            assertThat(viewModel.uiState.value.isLoading).isFalse()
        }

    @Test
    fun `onSnackBarActionLabelClick should show no internet snackBar when NoInternetException is thrown`() =
        runTest {
            // Given
            coEvery { getMovieGenreNameByIdUseCase(any()) } throws NoInternetException()
            val emittedSnackBarMessages = mutableListOf<BaseSnackBarMessage>()

            val job = launch {
                viewModel.snackBarState.collect {
                    emittedSnackBarMessages.add(it.message)
                }
            }

            // When
            viewModel.onSnackBarActionLabelClick()
            advanceUntilIdle()
            job.cancel()

            // Then
            assertThat(emittedSnackBarMessages).contains(BaseSnackBarMessage.NetworkError)
        }

    private val testMovie = Movie(
        id = 1L,
        title = "Test Movie",
        genres = listOf(Genre(1L, "Action")),
        averageRating = 8.5,
        userRating = null,
        releaseDate = LocalDate.parse("2023-01-01"),
        overview = "Some movie",
        posterImageURL = "https://example.com/poster.jpg",
        trailerURL = "https://example.com/trailer.mp4",
        runtimeMinutes = 100
    )

}
