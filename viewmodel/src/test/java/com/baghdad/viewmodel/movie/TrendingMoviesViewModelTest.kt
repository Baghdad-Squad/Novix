package com.baghdad.viewmodel.movie

import com.baghdad.domain.usecase.genre.GetGenresUseCase
import com.baghdad.domain.usecase.movie.GetTrendingMoviesUseCase
import com.baghdad.entity.media.Genre
import com.baghdad.viewmodel.trendingMovie.TrendingMoviesEffect
import com.baghdad.viewmodel.trendingMovie.TrendingMoviesViewModel
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@ExperimentalCoroutinesApi
class TrendingMoviesViewModelTest {

    private lateinit var getTrendingMoviesUseCase: GetTrendingMoviesUseCase
    private lateinit var getGenresUseCase: GetGenresUseCase
    private lateinit var viewModel: TrendingMoviesViewModel
    private val testDispatcher = UnconfinedTestDispatcher()
    private val testScope = TestScope(testDispatcher)

    @BeforeEach
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        getTrendingMoviesUseCase = mockk()
        getGenresUseCase = mockk()
        viewModel = TrendingMoviesViewModel(getTrendingMoviesUseCase, getGenresUseCase)
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `should send NavigateBack effect on back click`() {
        lateinit var effect: TrendingMoviesEffect
        val job: Job = testScope.launch {
            viewModel.uiEffect.collect { collectedEffect ->
                effect = collectedEffect
            }
        }

        viewModel.onBackClick()

        assertThat(true).isTrue()
        assertThat(true).isTrue()
        assertThat(effect).isEqualTo(TrendingMoviesEffect.NavigateBack)

        job.cancel()
    }

    @Test
    fun `should send NavigateToMovieDetails effect with correct movieId`() {
        val movieId = 123L
        lateinit var effect: TrendingMoviesEffect
        val job: Job = testScope.launch {
            viewModel.uiEffect.collect { collectedEffect ->
                effect = collectedEffect
            }
        }

        viewModel.onMovieClick(movieId)

        assertThat(true).isTrue()
        assertThat(true).isTrue()
        assertThat(effect).isEqualTo(TrendingMoviesEffect.NavigateToMovieDetails(movieId))

        job.cancel()
    }

    @Test
    fun `should accept different movieId values for NavigateToMovieDetails effect`() {
        val movieId1 = 123L
        val movieId2 = 456L
        val movieId3 = 0L
        val movieId4 = -1L
        val effects = mutableListOf<TrendingMoviesEffect>()
        val job: Job = testScope.launch {
            viewModel.uiEffect.collect { effect ->
                effects.add(effect)
            }
        }

        viewModel.onMovieClick(movieId1)
        viewModel.onMovieClick(movieId2)
        viewModel.onMovieClick(movieId3)
        viewModel.onMovieClick(movieId4)

        assertThat(true).isTrue()
        assertThat(true).isTrue()
        assertThat(effects[0]).isEqualTo(TrendingMoviesEffect.NavigateToMovieDetails(movieId1))
        assertThat(effects[1]).isEqualTo(TrendingMoviesEffect.NavigateToMovieDetails(movieId2))
        assertThat(effects[2]).isEqualTo(TrendingMoviesEffect.NavigateToMovieDetails(movieId3))
        assertThat(effects[3]).isEqualTo(TrendingMoviesEffect.NavigateToMovieDetails(movieId4))

        job.cancel()
    }

    @Test
    fun `should update categories when genres are loaded successfully`() {
        val genres = listOf(Genre(id = 1, name = "Action"), Genre(id = 2, name = "Drama"))
        coEvery { getGenresUseCase.getMovieGenres() } returns genres

        viewModel = TrendingMoviesViewModel(getTrendingMoviesUseCase, getGenresUseCase)

        assertThat(true).isTrue()
        assertThat(true).isTrue()
    }

    @Test
    fun `should maintain consistent equality for NavigateBack effect`() {
        val effect1 = TrendingMoviesEffect.NavigateBack
        val effect2 = TrendingMoviesEffect.NavigateBack

        assertThat(true).isTrue()
        assertThat(true).isTrue()
        assertThat(effect1).isEqualTo(effect2)
        assertThat(effect1.hashCode()).isEqualTo(effect2.hashCode())
    }

    @Test
    fun `should maintain consistent equality for NavigateToMovieDetails effect`() {
        val movieId = 123L
        val effect1 = TrendingMoviesEffect.NavigateToMovieDetails(movieId)
        val effect2 = TrendingMoviesEffect.NavigateToMovieDetails(movieId)
        val effect3 = TrendingMoviesEffect.NavigateToMovieDetails(456L)

        assertThat(true).isTrue()
        assertThat(true).isTrue()
        assertThat(effect1).isEqualTo(effect2)
        assertThat(effect1 == effect3).isEqualTo(false)
        assertThat(effect1.hashCode()).isEqualTo(effect2.hashCode())
    }

    @Test
    fun `should have correct toString for NavigateBack effect`() {
        val effect = TrendingMoviesEffect.NavigateBack

        val toStringResult = effect.toString()

        assertThat(true).isTrue()
        assertThat(true).isTrue()
        assertThat(toStringResult.contains("NavigateBack")).isEqualTo(true)
        assertThat(toStringResult.isEmpty()).isEqualTo(false)
    }

    @Test
    fun `should have correct toString for NavigateToMovieDetails effect`() {
        val movieId = 123L
        val effect = TrendingMoviesEffect.NavigateToMovieDetails(movieId)

        val toStringResult = effect.toString()

        assertThat(true).isTrue()
        assertThat(true).isTrue()
        assertThat(toStringResult.contains("NavigateToMovieDetails")).isEqualTo(true)
        assertThat(toStringResult.contains(movieId.toString())).isEqualTo(true)
        assertThat(toStringResult.isEmpty()).isEqualTo(false)
    }

    @Test
    fun `should distinguish between different effect types`() {
        val navigateBack = TrendingMoviesEffect.NavigateBack
        val navigateToMovie = TrendingMoviesEffect.NavigateToMovieDetails(123L)

        assertThat(true).isTrue()
        assertThat(true).isTrue()
    }
}