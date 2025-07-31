package com.baghdad.viewmodel.trendingMovie

import com.baghdad.domain.model.PagedResult
import com.baghdad.domain.usecase.genre.GetGenresUseCase
import com.baghdad.domain.usecase.movie.GetTrendingMoviesUseCase
import com.baghdad.entity.media.Genre
import com.baghdad.entity.media.Movie
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.LocalDate
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class TrendingMoviesMapperViewModelTest {

    private val getGenresUseCase: GetGenresUseCase = mockk()
    private val getTrendingMoviesUseCase: GetTrendingMoviesUseCase = mockk()
    private lateinit var viewModel: TrendingMoviesViewModel

    @BeforeEach
    fun setUp() {
        coEvery { getGenresUseCase.getMovieGenres() } returns testGenres
        coEvery { getTrendingMoviesUseCase(any(), any()) } coAnswers { pagedResult }
        viewModel = TrendingMoviesViewModel(getTrendingMoviesUseCase, getGenresUseCase)
    }

    @Test
    fun `shouldUpdateStateAndLoadMovies whenOnCategoryClickCalled`() = runTest {
        val states = mutableListOf<TrendingMoviesScreenState>()
        val job = launch { viewModel.uiState.collect { states.add(it) } }

        advanceUntilIdle()

        viewModel.onCategoryClick(1L)

        advanceUntilIdle()

        assertThat(states.last().selectedGenreId).isEqualTo(1L)
        assertThat(states.last().movies).isNotNull()

        job.cancel()
    }

    @Test
    fun `shouldMapMovieEntityToUiState correctly`() {

        val uiState = testMovies[0].toMovieUiState()

        assertThat(uiState.id).isEqualTo(testMovies[0].id)
        assertThat(uiState.posterPictureURL).isEqualTo(testMovies[0].posterImageURL)
    }

    @Test
    fun `shouldReloadMovieFlow whenOnGenreClickCalled`() = runTest {
        val states = mutableListOf<TrendingMoviesScreenState>()
        val job = launch { viewModel.uiState.collect { states.add(it) } }

        advanceUntilIdle()

        viewModel.onCategoryClick(99L)

        advanceUntilIdle()

        assertThat(states.last().selectedGenreId).isEqualTo(99L)
        assertThat(states.last().movies).isNotNull()

        job.cancel()
    }

    @Test
    fun `shouldNotUpdateState whenOnCategoryClickWithSameId`() = runTest {

        val states = mutableListOf<TrendingMoviesScreenState>()

        viewModel.onCategoryClick(2L)
        advanceUntilIdle()

        val job = launch { viewModel.uiState.collect { states.add(it) } }

        advanceUntilIdle()

        val initialCount = states.size
        viewModel.onCategoryClick(2L)
        advanceUntilIdle()

        assertThat(states.size).isEqualTo(initialCount)
        job.cancel()
    }

    @Test
    fun `shouldMapCategoriesCorrectly whenGenresLoaded`() = runTest {

        val states = mutableListOf<TrendingMoviesScreenState>()
        val job = launch { viewModel.uiState.collect { states.add(it) } }

        advanceUntilIdle()

        val state = states.first()

        assertThat(state.categories.size).isEqualTo(2)
        assertThat(state.categories[0].name).isEqualTo("Action")
        assertThat(state.categories[1].name).isEqualTo("Drama")

        job.cancel()
    }

    @Test
    fun `shouldMapMoviePropertiesCorrectly whenToMovieUiStateCalled`() {

        val movie = testMovies[1]
        val uiState = movie.toMovieUiState()

        assertThat(uiState.id).isEqualTo(movie.id)
        assertThat(uiState.posterPictureURL).isEqualTo(movie.posterImageURL)
        assertThat(uiState.isSaved).isEqualTo(false)
    }

    @Test
    fun `shouldUpdateStateCorrectly whenMultipleCategoryClicks`() = runTest {

        val states = mutableListOf<TrendingMoviesScreenState>()
        val job = launch { viewModel.uiState.collect { states.add(it) } }

        advanceUntilIdle()

        viewModel.onCategoryClick(1L)
        advanceUntilIdle()

        assertThat(states.last().selectedGenreId).isEqualTo(1L)

        viewModel.onCategoryClick(2L)
        advanceUntilIdle()

        assertThat(states.last().selectedGenreId).isEqualTo(2L)

        job.cancel()
    }

    @Test
    fun `shouldHandleEmptyMoviesListCorrectly`() = runTest {

        coEvery { getTrendingMoviesUseCase(any(), any()) } returns PagedResult(
            emptyList(),
            null,
            null
        )
        val newViewModel = TrendingMoviesViewModel(getTrendingMoviesUseCase, getGenresUseCase)
        val states = mutableListOf<TrendingMoviesScreenState>()
        val job = launch { newViewModel.uiState.collect { states.add(it) } }

        advanceUntilIdle()

        newViewModel.onCategoryClick(1L)

        advanceUntilIdle()
        assertThat(states.last().selectedGenreId).isEqualTo(1L)
        assertThat(states.last().movies).isNotNull()

        job.cancel()
    }

    @Test
    fun `shouldWork whenOnCategoryClickWithNullSelectedGenreId`() = runTest {
        val states = mutableListOf<TrendingMoviesScreenState>()
        val job = launch { viewModel.uiState.collect { states.add(it) } }

        advanceUntilIdle()

        assertThat(states.last().selectedGenreId).isEqualTo(null)

        viewModel.onCategoryClick(1L)
        advanceUntilIdle()

        assertThat(states.last().selectedGenreId).isEqualTo(1L)

        job.cancel()
    }

    @Test
    fun `shouldHandleEmptyGenreList whenGenreMappingCalled`() = runTest {

        coEvery { getGenresUseCase.getMovieGenres() } returns emptyList()
        val newViewModel = TrendingMoviesViewModel(getTrendingMoviesUseCase, getGenresUseCase)
        val states = mutableListOf<TrendingMoviesScreenState>()
        val job = launch { newViewModel.uiState.collect { states.add(it) } }

        advanceUntilIdle()

        assertThat(states.first().categories).isEqualTo(emptyList<TrendingMoviesScreenState.TrendingCategoryUiState>())

        job.cancel()
    }

    @Test
    fun `shouldMapCorrectly whenMovieHasNullUserRating`() {

        val movie = testMovies.first().copy(userRating = null)
        val uiState = movie.toMovieUiState()

        assertThat(uiState.id).isEqualTo(movie.id)
        assertThat(uiState.posterPictureURL).isEqualTo(movie.posterImageURL)
        assertThat(uiState.isSaved).isEqualTo(false)
    }

    @Test
    fun `shouldMapCorrectly whenMovieHasUserRating`() {

        val movie = testMovies.first().copy(userRating = 9.0)
        val uiState = movie.toMovieUiState()

        assertThat(uiState.id).isEqualTo(movie.id)
        assertThat(uiState.posterPictureURL).isEqualTo(movie.posterImageURL)
        assertThat(uiState.isSaved).isEqualTo(false)
    }

    @Test
    fun `shouldCallUseCaseWithCorrectParameters whenCategoryClicked`() = runTest {

        viewModel.onCategoryClick(5L)
        advanceUntilIdle()

        val states = mutableListOf<TrendingMoviesScreenState>()
        val job = launch { viewModel.uiState.collect { states.add(it) } }

        advanceUntilIdle()
        assertThat(states.last().selectedGenreId).isEqualTo(5L)

        job.cancel()
    }

    @Test
    fun `shouldLoadGenresCorrectly whenViewModelInitialized`() = runTest {

        coEvery { getGenresUseCase.getMovieGenres() } returns listOf(
            Genre(10L, "Horror"), Genre(20L, "Comedy"), Genre(30L, "Sci-Fi")
        )
        val vm = TrendingMoviesViewModel(getTrendingMoviesUseCase, getGenresUseCase)
        val states = mutableListOf<TrendingMoviesScreenState>()
        val job = launch { vm.uiState.collect { states.add(it) } }
        advanceUntilIdle()
        val state = states.first()

        assertThat(state.categories[0].name).isEqualTo("Horror")
        assertThat(state.categories[1].name).isEqualTo("Comedy")
        assertThat(state.categories[2].name).isEqualTo("Sci-Fi")

        job.cancel()
    }

    @Test
    fun `shouldHaveCorrectDefaultValues whenTrendingCategoryUiStateCreated`() {

        val category = TrendingMoviesScreenState.TrendingCategoryUiState()

        assertThat(category.id).isEqualTo(0L)
        assertThat(category.name).isEqualTo("")
    }

    @Test
    fun `shouldHaveCorrectDefaultValues whenTrendingMovieUiStateCreated`() {

        val movie = TrendingMoviesScreenState.TrendingMovieUiState()

        assertThat(movie.id).isEqualTo(0L)
        assertThat(movie.posterPictureURL).isEqualTo("")
        assertThat(movie.isSaved).isEqualTo(false)
    }

    @Test
    fun `shouldWorkIndependently whenMultipleMoviesMappedToUiState`() {

        val movie1 = testMovies[0]
        val movie2 = testMovies[1]
        val ui1 = movie1.toMovieUiState()
        val ui2 = movie2.toMovieUiState()

        assertThat(ui1.id).isNotEqualTo(ui2.id)
        assertThat(ui1.posterPictureURL).isNotEqualTo(ui2.posterPictureURL)
    }

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
}
