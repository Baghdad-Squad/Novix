package com.baghdad.viewmodel.categoryTvShows

import androidx.paging.PagingData
import com.baghdad.domain.model.PagedResult
import com.baghdad.domain.usecase.genre.GetGenresUseCase
import com.baghdad.domain.usecase.genre.GetTvShowGenreNameByIdUseCase
import com.baghdad.domain.usecase.tvShow.GetTvShowsByGenreUseCase
import com.baghdad.entity.media.Genre
import com.baghdad.entity.media.TvShow
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.LocalDate
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class CategoryTvShowsViewModelTest {

    private val categoryId = 1L
    private val getTvShowsByGenreUseCase: GetTvShowsByGenreUseCase = mockk()
    private val getGenresUseCase: GetGenresUseCase = mockk()

    private lateinit var viewModel: CategoryTvShowsViewModel

    private val tvShow = TvShow(
        id = 10L,
        title = "Test Show",
        genres = emptyList(),
        averageRating = 8.0,
        userRating = 7.0,
        releaseDate = LocalDate.parse("2024-01-01"),
        overview = "Test overview",
        posterImageURL = "poster.jpg",
        trailerURL = "trailer.mp4",
        headerImagesURLs = listOf("header.jpg"),
        numberOfSeasons = 2
    )

    private val pagingFlow = flowOf(PagingData.from(listOf(tvShow)))
    private val genre = Genre(id = categoryId, name = "Comedy")

    @BeforeEach
    fun setup() {
        coEvery { getTvShowsByGenreUseCase(categoryId, any()) } returns
                PagedResult(listOf(tvShow), 1, 1)
        coEvery { getGenresUseCase.getTvShowGenres() } returns listOf(genre)

        val getCategoryNameByIdUseCase = GetTvShowGenreNameByIdUseCase(getGenresUseCase)
        viewModel = CategoryTvShowsViewModel(categoryId, getTvShowsByGenreUseCase, getCategoryNameByIdUseCase)
    }

    @Test
    fun `when init then should load tv shows and category name`() = runTest {
        val states = mutableListOf<CategoryTvShowsState>()
        val job = launch { viewModel.uiState.collect { states.add(it) } }

        advanceUntilIdle()

        assertThat(states.last().categoryName).isEqualTo("Comedy")
        job.cancel()
    }

    @Test
    fun `when onSuccessGetCategoryName called then should update category name in state`() = runTest {
        viewModel.onSuccessGetCategoryName("Horror")
        val states = mutableListOf<CategoryTvShowsState>()
        val job = launch { viewModel.uiState.collect { states.add(it) } }

        advanceUntilIdle()

        assertThat(states.last().categoryName).isEqualTo("Horror")
        job.cancel()
    }

    @Test
    fun `when onErrorGetCategoryName called then should do nothing and not crash`() {
        viewModel.onErrorGetCategoryName(Exception("Network"))
        // pass if no exception thrown
    }

    @Test
    fun `when tvShow mapped to UiState then should return correct data`() {
        val uiState = tvShow.toUiState()

        assertThat(uiState.id).isEqualTo(tvShow.id)
        assertThat(uiState.posterPictureURL).isEqualTo(tvShow.posterImageURL)
        assertThat(uiState.isSaved).isFalse()
    }

    @Test
    fun `when viewModel receives paging data then should update tvShowsFlow`() = runTest {
        val states = mutableListOf<CategoryTvShowsState>()
        val job = launch { viewModel.uiState.collect { states.add(it) } }

        advanceUntilIdle()

        assertThat(states.last().tvShowsFlow).isNotNull()
        job.cancel()
    }

    @Test
    fun `when getTvShowsByCategoryId called in init then should update tvShowsFlow in state`() = runTest {
        val states = mutableListOf<CategoryTvShowsState>()
        val job = launch { viewModel.uiState.collect { states.add(it) } }

        advanceUntilIdle()

        assertThat(states.last().tvShowsFlow).isNotNull()
        job.cancel()
    }

}
