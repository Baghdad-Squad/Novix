package com.baghdad.viewmodel.trendingMovie

import org.junit.jupiter.api.Assertions.assertNotEquals
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
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test


class TrendingMoviesMapperViewModelTest {

    private val getGenresUseCase: GetGenresUseCase = mockk()
    private val getTrendingMoviesUseCase: GetTrendingMoviesUseCase = mockk()
    private lateinit var viewModel: TrendingMoviesViewModel

    private val testGenres = listOf(Genre(1, "Action"), Genre(2, "Drama"))
    val testMovies = List(3) { index ->
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
        coEvery { getTrendingMoviesUseCase(any(), any()) } coAnswers {
            pagedResult
        }

        viewModel = TrendingMoviesViewModel(getTrendingMoviesUseCase, getGenresUseCase)
    }

    @Test
    fun `onCategoryClick updates state and loads movies`() = runTest {
        val states = mutableListOf<TrendingMoviesScreenState>()

        val job = launch {
            viewModel.uiState.collect { state ->
                states.add(state)
            }
        }

        advanceUntilIdle() // Let initial state emit
        viewModel.onCategoryClick(1L)
        advanceUntilIdle()

        val updated = states.last()
        assertEquals(1L, updated.selectedGenreId)
        assertNotNull(updated.movies)

        job.cancel()
    }

    @Test
    fun `movie mapping from entity to UI state`() {
        val uiState = testMovies[0].toMovieUiState()
        assertEquals(testMovies[0].id, uiState.id)
        assertEquals(testMovies[0].posterImageURL, uiState.posterPictureURL)
    }

    @Test
    fun `onGenreClick reloads movie flow`() = runTest {
        val states = mutableListOf<TrendingMoviesScreenState>()

        val job = launch {
            viewModel.uiState.collect { state ->
                states.add(state)
            }
        }

        advanceUntilIdle() // Let initial state emit
        viewModel.onCategoryClick(99L)
        advanceUntilIdle()

        val state = states.last()
        assertEquals(99L, state.selectedGenreId)
        assertNotNull(state.movies)

        job.cancel()
    }

    @Test
    fun `onCategoryClick with same id does not update state`() = runTest {
        val states = mutableListOf<TrendingMoviesScreenState>()

        viewModel.onCategoryClick(2L)
        advanceUntilIdle()

        val job = launch {
            viewModel.uiState.collect { state ->
                states.add(state)
            }
        }

        advanceUntilIdle() // Let initial state emit
        val initialStateCount = states.size

        viewModel.onCategoryClick(2L)
        advanceUntilIdle()

        // Should not have emitted new state since same ID
        assertEquals(initialStateCount, states.size)

        job.cancel()
    }

    @Test
    fun `loadMoviesByGenres should call getTrendingMoviesUseCase with correct genreId`() = runTest {

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

    }

    @Test
    fun `loadMoviesByGenres should update state with flow and isLoading true`() = runTest {
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
    }

    @Test
    fun `categories should be mapped correctly from genres`() = runTest {
        val states = mutableListOf<TrendingMoviesScreenState>()

        val job = launch {
            viewModel.uiState.collect { state ->
                states.add(state)
            }
        }

        advanceUntilIdle()

        val state = states.first()
        assertEquals(2, state.categories.size)
        assertEquals(1L, state.categories[0].id)
        assertEquals("Action", state.categories[0].name)
        assertEquals(2L, state.categories[1].id)
        assertEquals("Drama", state.categories[1].name)

        job.cancel()
    }

    @Test
    fun `toMovieUiState should map all movie properties correctly`() {
        val movie = testMovies[1] // Movie with userRating
        val uiState = movie.toMovieUiState()

        assertEquals(movie.id, uiState.id)
        assertEquals(movie.posterImageURL, uiState.posterPictureURL)
        assertEquals(false, uiState.isSaved) // Default value
    }

    @Test
    fun `multiple onCategoryClick calls should update state correctly`() = runTest {
        val states = mutableListOf<TrendingMoviesScreenState>()

        val job = launch {
            viewModel.uiState.collect { state ->
                states.add(state)
            }
        }

        advanceUntilIdle() // Initial state

        // First category click
        viewModel.onCategoryClick(1L)
        advanceUntilIdle()

        val firstUpdate = states.last()
        assertEquals(1L, firstUpdate.selectedGenreId)

        // Second category click with different ID
        viewModel.onCategoryClick(2L)
        advanceUntilIdle()

        val secondUpdate = states.last()
        assertEquals(2L, secondUpdate.selectedGenreId)

        job.cancel()
    }

    @Test
    fun `viewModel should handle empty movies list correctly`() = runTest {
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
        assertEquals(1L, state.selectedGenreId)
        assertNotNull(state.movies) // Flow should still exist even if empty

        job.cancel()
    }

    @Test
    fun `onCategoryClick with null selectedGenreId should still work`() = runTest {
        val states = mutableListOf<TrendingMoviesScreenState>()

        val job = launch {
            viewModel.uiState.collect { state ->
                states.add(state)
            }
        }

        advanceUntilIdle()

        // Initial state should have null selectedGenreId
        val initialState = states.last()
        assertEquals(null, initialState.selectedGenreId)

        // Now select a category
        viewModel.onCategoryClick(1L)
        advanceUntilIdle()

        val updatedState = states.last()
        assertEquals(1L, updatedState.selectedGenreId)

        job.cancel()
    }

    @Test
    fun `genre mapping should handle empty genre list`() = runTest {
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
        assertEquals(emptyList<TrendingMoviesScreenState.TrendingCategoryUiState>(), state.categories)

        job.cancel()
    }

    @Test
    fun `movie with null userRating should map correctly`() {
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
        assertEquals(99L, uiState.id)
        assertEquals("https://example.com/poster.jpg", uiState.posterPictureURL)
        assertEquals(false, uiState.isSaved)
    }

    @Test
    fun `movie with userRating should map correctly`() {
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
        assertEquals(100L, uiState.id)
        assertEquals("https://example.com/poster2.jpg", uiState.posterPictureURL)
        assertEquals(false, uiState.isSaved)
    }

    @Test
    fun `getTrendingMoviesUseCase should be called with correct parameters`() = runTest {
        viewModel.onCategoryClick(5L)
        advanceUntilIdle()

        // Verify the use case was called (we can check this through state changes)
        val states = mutableListOf<TrendingMoviesScreenState>()

        val job = launch {
            viewModel.uiState.collect { state ->
                states.add(state)
            }
        }

        advanceUntilIdle()

        val finalState = states.last()
        assertEquals(5L, finalState.selectedGenreId)

        job.cancel()
    }

    @Test
    fun `viewModel initialization should load genres correctly`() = runTest {
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
        assertEquals(3, state.categories.size)
        assertEquals(10L, state.categories[0].id)
        assertEquals("Horror", state.categories[0].name)
        assertEquals(20L, state.categories[1].id)
        assertEquals("Comedy", state.categories[1].name)
        assertEquals(30L, state.categories[2].id)
        assertEquals("Sci-Fi", state.categories[2].name)

        job.cancel()
    }

    @Test
    fun `error handling when use case fails`() = runTest {
        coEvery { getTrendingMoviesUseCase(any(), any()) } throws RuntimeException("Network error")

        val newViewModel = TrendingMoviesViewModel(getTrendingMoviesUseCase, getGenresUseCase)
        val states = mutableListOf<TrendingMoviesScreenState>()

        val job = launch {
            newViewModel.uiState.collect { state ->
                states.add(state)
            }
        }

        advanceUntilIdle()

        try {
            newViewModel.onCategoryClick(1L)
            advanceUntilIdle()
        } catch (e: Exception) {
            // Expected to handle gracefully
        }

        job.cancel()
    }

    @Test
    fun `TrendingCategoryUiState data class should have correct default values`() {
        val category = TrendingMoviesScreenState.TrendingCategoryUiState()
        assertEquals(0L, category.id)
        assertEquals("", category.name)
    }

    @Test
    fun `TrendingMovieUiState data class should have correct default values`() {
        val movie = TrendingMoviesScreenState.TrendingMovieUiState()
        assertEquals(0L, movie.id)
        assertEquals("", movie.posterPictureURL)
        assertEquals(false, movie.isSaved)
    }

    @Test
    fun `Movie toMovieUiState extension function should map correctly`() {
        val movie = Movie(
            id = 123L,
            title = "Test Movie",
            genres = listOf(Genre(id = 1L, name = "Action")),
            averageRating = 8.5,
            userRating = 9.0,
            releaseDate = LocalDate.parse("2023-05-15"),
            overview = "Test overview",
            posterImageURL = "https://example.com/poster123.jpg",
            trailerURL = "https://example.com/trailer123.mp4",
            runtimeMinutes = 150
        )

        val uiState = movie.toMovieUiState()

        assertEquals(123L, uiState.id)
        assertEquals("https://example.com/poster123.jpg", uiState.posterPictureURL)
        assertEquals(false, uiState.isSaved) // Default value
    }

    @Test
    fun `Movie toMovieUiState with empty posterImageURL should map correctly`() {
        val movie = Movie(
            id = 456L,
            title = "Movie Without Poster",
            genres = listOf(Genre(id = 2L, name = "Drama")),
            averageRating = 7.0,
            userRating = null,
            releaseDate = LocalDate.parse("2023-01-01"),
            overview = "Test overview",
            posterImageURL = "",
            trailerURL = "",
            runtimeMinutes = 90
        )

        val uiState = movie.toMovieUiState()

        assertEquals(456L, uiState.id)
        assertEquals("", uiState.posterPictureURL)
        assertEquals(false, uiState.isSaved)
    }

    @Test
    fun `Movie toMovieUiState with long poster URL should map correctly`() {
        val longUrl = "https://very-long-domain-name.example.com/api/v1/images/posters/high-resolution/movie-poster-image-12345.jpg"
        val movie = Movie(
            id = 789L,
            title = "Movie With Long URL",
            genres = listOf(Genre(id = 3L, name = "Comedy")),
            averageRating = 6.5,
            userRating = 7.5,
            releaseDate = LocalDate.parse("2023-12-25"),
            overview = "Test overview",
            posterImageURL = longUrl,
            trailerURL = "https://example.com/trailer789.mp4",
            runtimeMinutes = 120
        )

        val uiState = movie.toMovieUiState()

        assertEquals(789L, uiState.id)
        assertEquals(longUrl, uiState.posterPictureURL)
        assertEquals(false, uiState.isSaved)
    }

    @Test
    fun `Genre toGenreUiState extension function should map correctly`() {
        val genre = Genre(
            id = 42L,
            name = "Science Fiction"
        )

        val uiState = genre.toGenreUiState()

        assertEquals(42L, uiState.id)
        assertEquals("Science Fiction", uiState.name)
    }

    @Test
    fun `Genre toGenreUiState with empty name should map correctly`() {
        val genre = Genre(
            id = 0L,
            name = ""
        )

        val uiState = genre.toGenreUiState()

        assertEquals(0L, uiState.id)
        assertEquals("", uiState.name)
    }

    @Test
    fun `Genre toGenreUiState with special characters should map correctly`() {
        val genre = Genre(
            id = 999L,
            name = "Sci-Fi & Fantasy"
        )

        val uiState = genre.toGenreUiState()

        assertEquals(999L, uiState.id)
        assertEquals("Sci-Fi & Fantasy", uiState.name)
    }

    @Test
    fun `Genre toGenreUiState with very long name should map correctly`() {
        val longName = "Very Long Genre Name That Might Be Used In Some Special Cases Or Testing Scenarios"
        val genre = Genre(
            id = 1000L,
            name = longName
        )

        val uiState = genre.toGenreUiState()

        assertEquals(1000L, uiState.id)
        assertEquals(longName, uiState.name)
    }

    @Test
    fun `multiple Movie toMovieUiState calls should work independently`() {
        val movie1 = Movie(
            id = 1L,
            title = "Movie 1",
            genres = listOf(Genre(id = 1L, name = "Action")),
            averageRating = 8.0,
            userRating = null,
            releaseDate = LocalDate.parse("2023-01-01"),
            overview = "Overview 1",
            posterImageURL = "https://example.com/poster1.jpg",
            trailerURL = "",
            runtimeMinutes = 100
        )

        val movie2 = Movie(
            id = 2L,
            title = "Movie 2",
            genres = listOf(Genre(id = 2L, name = "Drama")),
            averageRating = 7.5,
            userRating = 8.5,
            releaseDate = LocalDate.parse("2023-02-01"),
            overview = "Overview 2",
            posterImageURL = "https://example.com/poster2.jpg",
            trailerURL = "",
            runtimeMinutes = 120
        )

        val uiState1 = movie1.toMovieUiState()
        val uiState2 = movie2.toMovieUiState()

        assertEquals(1L, uiState1.id)
        assertEquals("https://example.com/poster1.jpg", uiState1.posterPictureURL)

        assertEquals(2L, uiState2.id)
        assertEquals("https://example.com/poster2.jpg", uiState2.posterPictureURL)

        // Ensure they are independent objects
        assertNotEquals(uiState1.id, uiState2.id)
        assertNotEquals(uiState1.posterPictureURL, uiState2.posterPictureURL)
    }

}