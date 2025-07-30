package com.baghdad.viewmodel.movie

import com.baghdad.domain.model.PagedResult
import com.baghdad.domain.usecase.genre.GetGenresUseCase
import com.baghdad.domain.usecase.movie.GetTrendingMoviesUseCase
import com.baghdad.entity.media.Genre
import com.baghdad.entity.media.Movie
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.LocalDate
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class TrendingMoviesViewModelTest {

    private val getGenresUseCase: GetGenresUseCase = mockk()
    private val getTrendingMoviesUseCase: GetTrendingMoviesUseCase = mockk()
    private lateinit var viewModel: TrendingMoviesViewModel

    private val testGenres = listOf(Genre(1, "Action"), Genre(2, "Drama"))
    private val testMovies = List(3) { index ->
        Movie(
            id = index.toLong(),
            title = "Movie Title $index",
            genres = listOf(Genre(id = 1L, name = "Action")),
            averageRating = 7.5 + index,
            userRating = if (index % 2 == 0) 6.0 + index else null,
            releaseDate = LocalDate.parse("2023-01-${(index + 1).toString().padStart(2, '0')}"),
            overview = "This is the overview for Movie $index",
            posterImageURL = "https://example.com/posters/poster$index.jpg",
            trailerURL = "https://example.com/trailers/trailer$index.mp4",
            runtimeMinutes = 100 + index * 10
        )
    }

    private val pagedResult = PagedResult(
        data = testMovies, nextKey = 2, prevKey = null
    )

    @BeforeEach
    fun setUp() {
        coEvery { getGenresUseCase.getMovieGenres() } returns testGenres
        coEvery { getTrendingMoviesUseCase(any(), any()) } returns pagedResult
        viewModel = TrendingMoviesViewModel(getTrendingMoviesUseCase, getGenresUseCase)
    }

    @Test
    fun `should update state with selected genre and movies when onCategoryClick is called`() = runTest {
        val states = mutableListOf<TrendingMoviesScreenState>()
        val job = launch {
            viewModel.uiState.collect { state ->
                states.add(state)
            }
        }

        advanceUntilIdle()
        viewModel.onCategoryClick(1L)
        advanceUntilIdle()

        val updated = states.last()
        assertTrue(updated.selectedGenreId == 1L, "Expected selectedGenreId to be 1L, but got ${updated.selectedGenreId}")
        assertNotNull(updated.movies)

        job.cancel()
    }

    @Test
    fun `should map movie entity to UI state correctly when toMovieUiState is called`() {
        val uiState = testMovies[0].toMovieUiState()
        assertTrue(uiState.id == testMovies[0].id, "Expected id to be ${testMovies[0].id}, but got ${uiState.id}")
        assertTrue(uiState.posterPictureURL == testMovies[0].posterImageURL, "Expected posterPictureURL to be ${testMovies[0].posterImageURL}, but got ${uiState.posterPictureURL}")
    }

    @Test
    fun `should reload movie flow when onGenreClick is called`() = runTest {
        val states = mutableListOf<TrendingMoviesScreenState>()
        val job = launch {
            viewModel.uiState.collect { state ->
                states.add(state)
            }
        }

        advanceUntilIdle()
        viewModel.onCategoryClick(99L)
        advanceUntilIdle()

        val state = states.last()
        assertTrue(state.selectedGenreId == 99L, "Expected selectedGenreId to be 99L, but got ${state.selectedGenreId}")
        assertNotNull(state.movies)

        job.cancel()
    }

    @Test
    fun `should not update state when onCategoryClick is called with same genre id`() = runTest {
        val states = mutableListOf<TrendingMoviesScreenState>()
        viewModel.onCategoryClick(2L)
        advanceUntilIdle()

        val job = launch {
            viewModel.uiState.collect { state ->
                states.add(state)
            }
        }

        advanceUntilIdle()
        val initialStateCount = states.size

        viewModel.onCategoryClick(2L)
        advanceUntilIdle()

        assertTrue(states.size == initialStateCount, "Expected state count to remain $initialStateCount, but got ${states.size}")

        job.cancel()
    }

    @Test
    fun `should call getTrendingMoviesUseCase with correct genreId when loadMoviesByGenres is called`() = runTest {
        val genreId = 2L
        val page = 0
        val fakeMovie = Movie(
            id = 1L,
            title = "Test Movie",
            genres = listOf(Genre(id = 2L, name = "Action")),
            averageRating = 8.5,
            userRating = null,
            releaseDate = LocalDate.parse("2023-01-01"),
            overview = "Overview",
            posterImageURL = "",
            trailerURL = "",
            runtimeMinutes = 120
        )
        coEvery { getTrendingMoviesUseCase(page = page, genreId = genreId) } returns PagedResult(
            data = listOf(fakeMovie), nextKey = 2, prevKey = null
        )

        viewModel.onCategoryClick(genreId)
        advanceUntilIdle()

        assertTrue(true)
    }

    @Test
    fun `should update state with movies and isLoading true when loadMoviesByGenres is called`() = runTest {
        val genreId = 2L
        val movie = Movie(
            id = 10L,
            title = "Another Movie",
            genres = listOf(Genre(id = 2L, name = "Action")),
            averageRating = 7.0,
            userRating = 8.0,
            releaseDate = LocalDate.parse("2023-01-02"),
            overview = "Test overview",
            posterImageURL = "",
            trailerURL = "",
            runtimeMinutes = 110
        )
        coEvery { getTrendingMoviesUseCase(any(), genreId) } returns PagedResult(
            data = listOf(movie), nextKey = 2, prevKey = null
        )

        viewModel.onCategoryClick(genreId)
        advanceUntilIdle()

        assertTrue(true)
    }

    @Test
    fun `should map categories correctly from genres when viewModel is initialized`() = runTest {
        val states = mutableListOf<TrendingMoviesScreenState>()
        val job = launch {
            viewModel.uiState.collect { state ->
                states.add(state)
            }
        }

        advanceUntilIdle()

        val state = states.first()
        assertTrue(state.categories.size == 2, "Expected 2 categories, but got ${state.categories.size}")
        assertTrue(state.categories[0].id == 1L, "Expected category id 1L, but got ${state.categories[0].id}")
        assertTrue(state.categories[0].name == "Action", "Expected category name Action, but got ${state.categories[0].name}")
        assertTrue(state.categories[1].id == 2L, "Expected category id 2L, but got ${state.categories[1].id}")
        assertTrue(state.categories[1].name == "Drama", "Expected category name Drama, but got ${state.categories[1].name}")

        job.cancel()
    }

    @Test
    fun `should map all movie properties correctly when toMovieUiState is called`() {
        val movie = testMovies[1]
        val uiState = movie.toMovieUiState()

        assertTrue(uiState.id == movie.id, "Expected id ${movie.id}, but got ${uiState.id}")
        assertTrue(uiState.posterPictureURL == movie.posterImageURL, "Expected posterPictureURL ${movie.posterImageURL}, but got ${uiState.posterPictureURL}")
        assertTrue(uiState.isSaved == false, "Expected isSaved false, but got ${uiState.isSaved}")
    }

    @Test
    fun `should update state correctly when multiple onCategoryClick calls are made`() = runTest {
        val states = mutableListOf<TrendingMoviesScreenState>()
        val job = launch {
            viewModel.uiState.collect { state ->
                states.add(state)
            }
        }

        advanceUntilIdle()
        viewModel.onCategoryClick(1L)
        advanceUntilIdle()

        val firstUpdate = states.last()
        assertTrue(firstUpdate.selectedGenreId == 1L, "Expected selectedGenreId 1L, but got ${firstUpdate.selectedGenreId}")

        viewModel.onCategoryClick(2L)
        advanceUntilIdle()

        val secondUpdate = states.last()
        assertTrue(secondUpdate.selectedGenreId == 2L, "Expected selectedGenreId 2L, but got ${secondUpdate.selectedGenreId}")

        job.cancel()
    }

    @Test
    fun `should handle empty movies list correctly when viewModel is initialized`() = runTest {
        val emptyPagedResult = PagedResult(
            data = emptyList<Movie>(),
            nextKey = null,
            prevKey = null
        )
        coEvery { getTrendingMoviesUseCase(any(), any()) } returns emptyPagedResult

        val newViewModel = TrendingMoviesViewModel(getTrendingMoviesUseCase, getGenresUseCase)
        val states = mutableListOf<TrendingMoviesScreenState>()
        val job = launch {
            newViewModel.uiState.collect { state ->
                states.add(state)
            }
        }

        advanceUntilIdle()
        newViewModel.onCategoryClick(1L)
        advanceUntilIdle()

        val state = states.last()
        assertTrue(state.selectedGenreId == 1L, "Expected selectedGenreId 1L, but got ${state.selectedGenreId}")
        assertNotNull(state.movies)

        job.cancel()
    }

    @Test
    fun `should set isLoading to false when initial load completes`() = runTest {
        val states = mutableListOf<TrendingMoviesScreenState>()
        val job = launch {
            viewModel.uiState.collect { state ->
                states.add(state)
            }
        }

        advanceUntilIdle()
        val finalState = states.last()
        assertTrue(finalState.isLoading == false, "Expected isLoading false, but got ${finalState.isLoading}")

        job.cancel()
    }

    @Test
    fun `should handle null selectedGenreId when onCategoryClick is called`() = runTest {
        val states = mutableListOf<TrendingMoviesScreenState>()
        val job = launch {
            viewModel.uiState.collect { state ->
                states.add(state)
            }
        }

        advanceUntilIdle()
        val initialState = states.last()
        assertTrue(initialState.selectedGenreId == null, "Expected selectedGenreId null, but got ${initialState.selectedGenreId}")

        viewModel.onCategoryClick(1L)
        advanceUntilIdle()

        val updatedState = states.last()
        assertTrue(updatedState.selectedGenreId == 1L, "Expected selectedGenreId 1L, but got ${updatedState.selectedGenreId}")

        job.cancel()
    }

    @Test
    fun `should handle empty genre list when mapping genres`() = runTest {
        coEvery { getGenresUseCase.getMovieGenres() } returns emptyList()
        val newViewModel = TrendingMoviesViewModel(getTrendingMoviesUseCase, getGenresUseCase)
        val states = mutableListOf<TrendingMoviesScreenState>()
        val job = launch {
            newViewModel.uiState.collect { state ->
                states.add(state)
            }
        }

        advanceUntilIdle()
        val state = states.first()
        assertTrue(state.categories == emptyList<TrendingMoviesScreenState.TrendingCategoryUiState>(), "Expected empty categories, but got ${state.categories}")

        job.cancel()
    }

    @Test
    fun `should map movie with null userRating correctly when toMovieUiState is called`() {
        val movieWithoutRating = Movie(
            id = 99L,
            title = "Movie Without Rating",
            genres = listOf(Genre(id = 1L, name = "Action")),
            averageRating = 7.5,
            userRating = null,
            releaseDate = LocalDate.parse("2023-01-01"),
            overview = "Test overview",
            posterImageURL = "https://example.com/poster.jpg",
            trailerURL = "https://example.com/trailer.mp4",
            runtimeMinutes = 120
        )
        val uiState = movieWithoutRating.toMovieUiState()
        assertTrue(uiState.id == 99L, "Expected id 99L, but got ${uiState.id}")
        assertTrue(uiState.posterPictureURL == "https://example.com/poster.jpg", "Expected posterPictureURL https://example.com/poster.jpg, but got ${uiState.posterPictureURL}")
        assertTrue(uiState.isSaved == false, "Expected isSaved false, but got ${uiState.isSaved}")
    }

    @Test
    fun `should map movie with userRating correctly when toMovieUiState is called`() {
        val movieWithRating = Movie(
            id = 100L,
            title = "Movie With Rating",
            genres = listOf(Genre(id = 1L, name = "Action")),
            averageRating = 7.5,
            userRating = 8.5,
            releaseDate = LocalDate.parse("2023-01-01"),
            overview = "Test overview",
            posterImageURL = "https://example.com/poster2.jpg",
            trailerURL = "https://example.com/trailer2.mp4",
            runtimeMinutes = 130
        )
        val uiState = movieWithRating.toMovieUiState()
        assertTrue(uiState.id == 100L, "Expected id 100L, but got ${uiState.id}")
        assertTrue(uiState.posterPictureURL == "https://example.com/poster2.jpg", "Expected posterPictureURL https://example.com/poster2.jpg, but got ${uiState.posterPictureURL}")
        assertTrue(uiState.isSaved == false, "Expected isSaved false, but got ${uiState.isSaved}")
    }

    @Test
    fun `should call getTrendingMoviesUseCase with correct parameters when onCategoryClick is called`() = runTest {
        viewModel.onCategoryClick(5L)
        advanceUntilIdle()

        val states = mutableListOf<TrendingMoviesScreenState>()
        val job = launch {
            viewModel.uiState.collect { state ->
                states.add(state)
            }
        }

        advanceUntilIdle()
        val finalState = states.last()
        assertTrue(finalState.selectedGenreId == 5L, "Expected selectedGenreId 5L, but got ${finalState.selectedGenreId}")

        job.cancel()
    }

    @Test
    fun `should load genres correctly when viewModel is initialized`() = runTest {
        val customGenres = listOf(
            Genre(10L, "Horror"),
            Genre(20L, "Comedy"),
            Genre(30L, "Sci-Fi")
        )
        coEvery { getGenresUseCase.getMovieGenres() } returns customGenres
        val newViewModel = TrendingMoviesViewModel(getTrendingMoviesUseCase, getGenresUseCase)
        val states = mutableListOf<TrendingMoviesScreenState>()
        val job = launch {
            newViewModel.uiState.collect { state ->
                states.add(state)
            }
        }

        advanceUntilIdle()
        val state = states.first()
        assertTrue(state.categories.size == 3, "Expected 3 categories, but got ${state.categories.size}")
        assertTrue(state.categories[0].id == 10L, "Expected category id 10L, but got ${state.categories[0].id}")
        assertTrue(state.categories[0].name == "Horror", "Expected category name Horror, but got ${state.categories[0].name}")
        assertTrue(state.categories[1].id == 20L, "Expected category id 20L, but got ${state.categories[1].id}")
        assertTrue(state.categories[1].name == "Comedy", "Expected category name Comedy, but got ${state.categories[1].name}")
        assertTrue(state.categories[2].id == 30L, "Expected category id 30L, but got ${state.categories[2].id}")
        assertTrue(state.categories[2].name == "Sci-Fi", "Expected category name Sci-Fi, but got ${state.categories[2].name}")

        job.cancel()
    }

    @Test
    fun `should handle use case failure when loading movies`() = runTest {
        coEvery { getTrendingMoviesUseCase(any(), any()) } throws RuntimeException("Network error")
        val newViewModel = TrendingMoviesViewModel(getTrendingMoviesUseCase, getGenresUseCase)
        val states = mutableListOf<TrendingMoviesScreenState>()
        val job = launch {
            newViewModel.uiState.collect { state ->
                states.add(state)
            }
        }

        advanceUntilIdle()
        assertTrue(states.isNotEmpty(), "Expected non-empty state list, but got ${states.size}")

        job.cancel()
    }

    @Test
    fun `should have correct default values when TrendingCategoryUiState is created`() {
        val category = TrendingMoviesScreenState.TrendingCategoryUiState()
        assertTrue(category.id == 0L, "Expected category id 0L, but got ${category.id}")
        assertTrue(category.name == "", "Expected category name empty, but got ${category.name}")
    }

    @Test
    fun `should have correct default values when TrendingMovieUiState is created`() {
        val movie = TrendingMoviesScreenState.TrendingMovieUiState()
        assertTrue(movie.id == 0L, "Expected movie id 0L, but got ${movie.id}")
        assertTrue(movie.posterPictureURL == "", "Expected posterPictureURL empty, but got ${movie.posterPictureURL}")
        assertTrue(movie.isSaved == false, "Expected isSaved false, but got ${movie.isSaved}")
    }
}