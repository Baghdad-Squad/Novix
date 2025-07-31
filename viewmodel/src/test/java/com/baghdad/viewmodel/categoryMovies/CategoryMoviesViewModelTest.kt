package com.baghdad.viewmodel.categoryMovies

import com.baghdad.domain.model.PagedResult
import com.baghdad.domain.usecase.genre.GetMovieGenreNameByIdUseCase
import com.baghdad.domain.usecase.movie.GetMoviesByGenreUseCase
import com.baghdad.entity.media.Genre
import com.baghdad.entity.media.Movie
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.*
import kotlinx.datetime.LocalDate
import org.junit.jupiter.api.*
import io.mockk.*

@OptIn(ExperimentalCoroutinesApi::class)
class CategoryMoviesViewModelTest {

    private val getGenreMoviesUseCase: GetMoviesByGenreUseCase = mockk()
    private val getMovieGenreNameByIdUseCase: GetMovieGenreNameByIdUseCase = mockk()

    private lateinit var viewModel: CategoryMoviesViewModel
    private val testDispatcher = StandardTestDispatcher()

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
            getMovieGenreNameByIdUseCase = getMovieGenreNameByIdUseCase
        )
    }


    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `when getGenreName called then should update categoryName`() = runTest {
        val states = mutableListOf<CategoryMoviesState>()
        val job = launch { viewModel.uiState.collect { states.add(it) } }

        advanceUntilIdle()

        assertThat(states.last().categoryName).isEqualTo("Action")
        job.cancel()
    }

    @Test
    fun `when getGenreMovies called then should update moviesFlow`() = runTest {
        val states = mutableListOf<CategoryMoviesState>()
        val job = launch { viewModel.uiState.collect { states.add(it) } }

        advanceUntilIdle()

        assertThat(states.last().moviesFlow).isNotNull()
        job.cancel()
    }

    @Test
    fun `when onBackClicked then should emit NavigateBack`() = runTest {
        val effects = mutableListOf<CategoryMoviesEffect>()
        val job = launch { viewModel.uiEffect.collect { effects.add(it) } }

        viewModel.onBackClicked()
        advanceUntilIdle()

        assertThat(effects).containsExactly(CategoryMoviesEffect.NavigateBack)
        job.cancel()
    }

    @Test
    fun `when onMovieClicked with valid ID then should emit NavigateToMovieDetails`() = runTest {
        val effects = mutableListOf<CategoryMoviesEffect>()
        val job = launch { viewModel.uiEffect.collect { effects.add(it) } }

        viewModel.onMovieClicked(42L)
        advanceUntilIdle()

        assertThat(effects).containsExactly(CategoryMoviesEffect.NavigateToMovieDetails(42L))
        job.cancel()
    }

    @Test
    fun `when toUiState called then should map Movie to MovieUiState correctly`() {
        val uiState = testMovie.toUiState()

        assertThat(uiState.id).isEqualTo(testMovie.id)
        assertThat(uiState.posterPictureURL).isEqualTo(testMovie.posterImageURL)
        assertThat(uiState.isSaved).isFalse()
    }

    @Test
    fun `when getMovieGenreNameByIdUseCase fails then should not crash`() = runTest {
        coEvery { getMovieGenreNameByIdUseCase(1L) } throws RuntimeException("Failed")
        viewModel = CategoryMoviesViewModel(1L, getGenreMoviesUseCase, getMovieGenreNameByIdUseCase)

        advanceUntilIdle()

        val states = mutableListOf<CategoryMoviesState>()
        val job = launch { viewModel.uiState.collect { states.add(it) } }

        advanceUntilIdle()
        assertThat(states.last().categoryName).isEqualTo("")
        job.cancel()
    }

    @Test
    fun `when moviesFlow default then should be empty`() = runTest {
        val defaultState = CategoryMoviesState()
        assertThat(defaultState.moviesFlow).isNotNull()
    }

    @Test
    fun `when Movie has empty poster then should return empty posterPictureURL`() {
        val movie = testMovie.copy(posterImageURL = "")
        val uiState = movie.toUiState()
        assertThat(uiState.posterPictureURL).isEqualTo("")
    }

    @Test
    fun `when Movie has different id then should reflect in uiState`() {
        val movie = testMovie.copy(id = 99L)
        val uiState = movie.toUiState()
        assertThat(uiState.id).isEqualTo(99L)
    }

    @Test
    fun `when Movie has long poster URL then should keep it in uiState`() {
        val longUrl = "https://example.com/very/long/path/poster.jpg"
        val movie = testMovie.copy(posterImageURL = longUrl)
        val uiState = movie.toUiState()
        assertThat(uiState.posterPictureURL).isEqualTo(longUrl)
    }
}
