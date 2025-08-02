package com.baghdad.viewmodel.categoryTvShows

import com.baghdad.domain.model.PagedResult
import com.baghdad.domain.usecase.genre.GetGenresUseCase
import com.baghdad.domain.usecase.genre.GetTvShowGenreNameByIdUseCase
import com.baghdad.domain.usecase.tvShow.GetTvShowsByGenreUseCase
import com.baghdad.entity.media.Genre
import com.baghdad.entity.media.TvShow
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
class CategoryTvShowsViewModelTest {

    private val getTvShowsByGenreUseCase: GetTvShowsByGenreUseCase = mockk()
    private val getGenresUseCase: GetGenresUseCase = mockk()
    private lateinit var getCategoryNameByIdUseCase: GetTvShowGenreNameByIdUseCase
    private lateinit var viewModel: CategoryTvShowsViewModel
    private val testDispatcher = StandardTestDispatcher()

    private val genre = Genre(id = 1L, name = "Comedy")

    private val testTvShow = TvShow(
        id = 1L,
        title = "Test Tv Show",
        genres = listOf(genre),
        averageRating = 9.0,
        userRating = null,
        releaseDate = LocalDate.parse("2023-05-01"),
        overview = "Test overview",
        posterImageURL = "https://example.com/poster.jpg",
        trailerURL = "https://example.com/trailer.mp4",
        headerImagesURLs = listOf("header.jpg"),
        numberOfSeasons = 3
    )

    @BeforeEach
    fun setup() {
        Dispatchers.setMain(testDispatcher)

        coEvery { getTvShowsByGenreUseCase(any(), any()) } returns PagedResult(
            data = listOf(testTvShow),
            nextKey = 2,
            prevKey = null
        )

        coEvery { getGenresUseCase.getTvShowGenres() } returns listOf(genre)

        getCategoryNameByIdUseCase = GetTvShowGenreNameByIdUseCase(getGenresUseCase)

        viewModel = CategoryTvShowsViewModel(
            categoryId = 1L,
            getTvShowsCategoryUseCase = getTvShowsByGenreUseCase,
            getCategoryNameByIdUseCase = getCategoryNameByIdUseCase,
            ioDispatcher = testDispatcher
        )
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `should update categoryName when getCategoryNameByIdUseCase called`() = runTest {
        val states = mutableListOf<CategoryTvShowsState>()
        val job = launch { viewModel.uiState.collect { states.add(it) } }

        advanceUntilIdle()
        assertThat(states.last().categoryName).isEqualTo("Comedy")
        job.cancel()
    }

    @Test
    fun `should update tvShowsFlow when getTvShowsByGenreUseCase called`() = runTest {
        val states = mutableListOf<CategoryTvShowsState>()
        val job = launch { viewModel.uiState.collect { states.add(it) } }

        advanceUntilIdle()
        assertThat(states.last().tvShowsFlow).isNotNull()
        job.cancel()
    }

    @Test
    fun `should emit NavigateBack when onBackClicked`() = runTest {
        val effects = mutableListOf<CategoryTvShowsEffect>()
        val job = launch { viewModel.uiEffect.collect { effects.add(it) } }

        viewModel.onBackClicked()
        advanceUntilIdle()

        assertThat(effects).containsExactly(CategoryTvShowsEffect.NavigateBack)
        job.cancel()
    }

    @Test
    fun `should emit NavigateToTvShowDetails when onTvShowClicked`() = runTest {
        val effects = mutableListOf<CategoryTvShowsEffect>()
        val job = launch { viewModel.uiEffect.collect { effects.add(it) } }

        viewModel.onTvShowClicked(5L)
        advanceUntilIdle()

        assertThat(effects).containsExactly(CategoryTvShowsEffect.NavigateToTvShowDetails(5L))
        job.cancel()
    }

    @Test
    fun `should map TvShow to TvShowUiState correctly`() {
        val uiState = testTvShow.toUiState()
        assertThat(uiState.id).isEqualTo(testTvShow.id)
        assertThat(uiState.posterPictureURL).isEqualTo(testTvShow.posterImageURL)
        assertThat(uiState.isSaved).isFalse()
    }

    @Test
    fun `should not crash when getCategoryNameByIdUseCase throws`() = runTest {
        coEvery { getGenresUseCase.getTvShowGenres() } throws RuntimeException("fail")
        viewModel = CategoryTvShowsViewModel(
            1L,
            getTvShowsByGenreUseCase,
            GetTvShowGenreNameByIdUseCase(getGenresUseCase),
            testDispatcher
        )

        val states = mutableListOf<CategoryTvShowsState>()
        val job = launch { viewModel.uiState.collect { states.add(it) } }

        advanceUntilIdle()
        assertThat(states.last().categoryName).isEqualTo("")
        job.cancel()
    }

    @Test
    fun `should return empty posterPictureURL when TvShow has empty poster`() {
        val tvShow = testTvShow.copy(posterImageURL = "")
        val uiState = tvShow.toUiState()

        assertThat(uiState.posterPictureURL).isEqualTo("")
    }

    @Test
    fun `should reflect id change correctly when mapped to UiState`() {
        val tvShow = testTvShow.copy(id = 99L)
        val uiState = tvShow.toUiState()

        assertThat(uiState.id).isEqualTo(99L)
    }

    @Test
    fun `should preserve posterImageURL in mapping`() {
        val url = "https://my.tv/poster.jpg"
        val tvShow = testTvShow.copy(posterImageURL = url)
        val uiState = tvShow.toUiState()

        assertThat(uiState.posterPictureURL).isEqualTo(url)
    }

    @Test
    fun `should update moviesFlow and set isLoading to false when paging flow collected`() =
        runTest {
            // Given
            coEvery { getTvShowsByGenreUseCase(1L, any()) } returns PagedResult(
                data = listOf(testTvShow),
                nextKey = null,
                prevKey = null
            )

            viewModel = CategoryTvShowsViewModel(
                categoryId = 1L,
                getTvShowsCategoryUseCase = getTvShowsByGenreUseCase,
                getCategoryNameByIdUseCase = getCategoryNameByIdUseCase,
                ioDispatcher = testDispatcher
            )

            advanceUntilIdle()
            // When
            val items = collectAndSnapshot(
                flow = viewModel.uiState.value.tvShowsFlow,
            )

            // Then
            assertThat(items).isNotEmpty()
            assertThat(viewModel.uiState.value.isLoading).isFalse()
        }


}
