//package com.baghdad.viewmodel.categoryTvShows
//
//import com.baghdad.domain.model.PagedResult
//import com.baghdad.domain.usecase.genre.GetGenresUseCase
//import com.baghdad.domain.usecase.genre.GetTvShowGenreNameByIdUseCase
//import com.baghdad.domain.usecase.tvShow.GetTvShowsByGenreUseCase
//import com.baghdad.entity.media.Genre
//import com.baghdad.entity.media.TvShow
//import com.google.common.truth.Truth.assertThat
//import io.mockk.coEvery
//import io.mockk.mockk
//import kotlinx.coroutines.launch
//import kotlinx.coroutines.test.advanceUntilIdle
//import kotlinx.coroutines.test.runTest
//import kotlinx.datetime.LocalDate
//import org.junit.jupiter.api.BeforeEach
//import org.junit.jupiter.api.Test
//
//class CategoryTvShowsViewModelTest {
//
//    private val categoryId = 1L
//    private val getTvShowsByGenreUseCase: GetTvShowsByGenreUseCase = mockk()
//    private val getGenresUseCase: GetGenresUseCase = mockk()
//
//    private lateinit var viewModel: CategoryTvShowsViewModel
//
//    @BeforeEach
//    fun setup() {
//        coEvery { getTvShowsByGenreUseCase(categoryId, any()) } returns
//                PagedResult(listOf(tvShow), 1, 1)
//        coEvery { getGenresUseCase.getTvShowGenres() } returns listOf(genre)
//
//        val getCategoryNameByIdUseCase = GetTvShowGenreNameByIdUseCase(getGenresUseCase)
//        viewModel = CategoryTvShowsViewModel(categoryId, getTvShowsByGenreUseCase, getCategoryNameByIdUseCase)
//    }
//
//    @Test
//    fun `should load tv shows and category name when init`() = runTest {
//        val states = mutableListOf<CategoryTvShowsState>()
//        val job = launch { viewModel.uiState.collect { states.add(it) } }
//
//        advanceUntilIdle()
//
//        assertThat(states.last().categoryName).isEqualTo("Comedy")
//        job.cancel()
//    }
//
//    @Test
//    fun `should update category name in state when onSuccessGetCategoryName called`() = runTest {
//        viewModel.onSuccessGetCategoryName("Horror")
//        val states = mutableListOf<CategoryTvShowsState>()
//        val job = launch { viewModel.uiState.collect { states.add(it) } }
//
//        advanceUntilIdle()
//
//        assertThat(states.last().categoryName).isEqualTo("Horror")
//        job.cancel()
//    }
//
//    @Test
//    fun `should do nothing and not crash when onErrorGetCategoryName called`() {
//        viewModel.onErrorGetCategoryName(Exception("Network"))
//        // pass if no exception thrown
//    }
//
//    @Test
//    fun `should return correct data when tvShow mapped to UiState`() {
//        val uiState = tvShow.toUiState()
//
//        assertThat(uiState.id).isEqualTo(tvShow.id)
//        assertThat(uiState.posterPictureURL).isEqualTo(tvShow.posterImageURL)
//        assertThat(uiState.isSaved).isFalse()
//    }
//
//    @Test
//    fun `should update tvShowsFlow when viewModel receives paging data`() = runTest {
//        val states = mutableListOf<CategoryTvShowsState>()
//        val job = launch { viewModel.uiState.collect { states.add(it) } }
//
//        advanceUntilIdle()
//
//        assertThat(states.last().tvShowsFlow).isNotNull()
//        job.cancel()
//    }
//
//    @Test
//    fun `should update tvShowsFlow in state when getTvShowsByCategoryId called in init`() = runTest {
//        val states = mutableListOf<CategoryTvShowsState>()
//        val job = launch { viewModel.uiState.collect { states.add(it) } }
//
//        advanceUntilIdle()
//
//        assertThat(states.last().tvShowsFlow).isNotNull()
//        job.cancel()
//    }
//
//    private val tvShow = TvShow(
//        id = 10L,
//        title = "Test Show",
//        genres = emptyList(),
//        averageRating = 8.0,
//        userRating = 7.0,
//        releaseDate = LocalDate.parse("2024-01-01"),
//        overview = "Test overview",
//        posterImageURL = "poster.jpg",
//        trailerURL = "trailer.mp4",
//        headerImagesURLs = listOf("header.jpg"),
//        numberOfSeasons = 2
//    )
//
//    private val genre = Genre(id = categoryId, name = "Comedy")
//}
