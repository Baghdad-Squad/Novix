package com.baghdad.viewmodel.trendingTvShow

import com.baghdad.domain.model.PagedResult
import com.baghdad.domain.usecase.genre.GetGenresUseCase
import com.baghdad.domain.usecase.tvShow.GetTrendingTvShowUseCase
import com.baghdad.entity.media.Genre
import com.baghdad.entity.media.TvShow
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.LocalDate
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class TrendingTvShowViewModelTest {

    private val getGenresUseCase: GetGenresUseCase = mockk()
    private val getTrendingTvShowUseCase: GetTrendingTvShowUseCase = mockk()
    private lateinit var viewModel: TrendingTvShowViewModel

    private val genres = listOf(
        Genre(id = 1L, name = "Action"),
        Genre(id = 2L, name = "Drama")
    )

    private val tvShows = listOf(
        TvShow(
            id = 10L,
            title = "Breaking Show",
            genres = listOf(genres.first()),
            averageRating = 8.5,
            userRating = 7.0,
            releaseDate = LocalDate.parse("2022-05-01"),
            overview = "Good show",
            posterImageURL = "poster.jpg",
            trailerURL = "trailer.mp4",
            headerImagesURLs = listOf("header1.jpg", "header2.jpg"),
            numberOfSeasons = 3
        )
    )

    @BeforeEach
    fun setUp() {
        coEvery { getGenresUseCase.getTvShowGenres() } returns genres
        coEvery { getTrendingTvShowUseCase(any(), any()) } returns PagedResult(tvShows, nextKey = null, prevKey = null)
        viewModel = TrendingTvShowViewModel(getTrendingTvShowUseCase, getGenresUseCase)
    }

    @Test
    fun `when viewModel initialized then genres and trendingTvShows loaded`() = runTest {
        val states = mutableListOf<TrendingTvShowScreenState>()
        val job = launch {
            viewModel.uiState.collect { states.add(it) }
        }
        advanceUntilIdle()

        val state = states.last()
        assertThat(state.genres).hasSize(2)
        assertThat(state.genres[0].name).isEqualTo("Action")
        assertThat(state.genres[1].name).isEqualTo("Drama")
        job.cancel()
    }

    @Test
    fun `when genre clicked then trendingTvShows updated and selectedGenreId changes`() = runTest {
        val states = mutableListOf<TrendingTvShowScreenState>()
        val job = launch {
            viewModel.uiState.collect { states.add(it) }
        }
        advanceUntilIdle()

        viewModel.onGenreClick(1L)
        advanceUntilIdle()

        val lastState = states.last()
        assertThat(lastState.selectedGenreId).isEqualTo(1L)
        job.cancel()
    }

    @Test
    fun `when genre clicked with same ID then state not updated again`() = runTest {
        val states = mutableListOf<TrendingTvShowScreenState>()
        val job = launch {
            viewModel.uiState.collect { states.add(it) }
        }
        advanceUntilIdle()

        viewModel.onGenreClick(2L)
        advanceUntilIdle()
        val countAfterFirst = states.size

        viewModel.onGenreClick(2L)
        advanceUntilIdle()
        val countAfterSecond = states.size

        assertThat(countAfterFirst).isEqualTo(countAfterSecond)
        job.cancel()
    }


    @Test
    fun `when genre mapped to ui state then correct GenreUiState returned`() {
        val genreUi = genres[0].toUiState()
        assertThat(genreUi.id).isEqualTo(1L)
        assertThat(genreUi.name).isEqualTo("Action")
    }

    @Test
    fun `when tvShow mapped to ui state then correct TvShowUiState returned`() {
        val tvShowUi = tvShows[0].toUiState()
        assertThat(tvShowUi.id).isEqualTo(10L)
        assertThat(tvShowUi.posterPictureURL).isEqualTo("poster.jpg")
        assertThat(tvShowUi.isSaved).isFalse()
    }

    @Test
    fun `when onSaveTvShowClick called then does nothing`() {
        viewModel.onSaveTvShowClick(1L)
    }

}
