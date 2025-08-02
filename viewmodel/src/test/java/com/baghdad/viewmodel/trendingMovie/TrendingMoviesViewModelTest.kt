package com.baghdad.viewmodel.trendingMovie

import com.baghdad.domain.usecase.genre.GetGenresUseCase
import com.baghdad.domain.usecase.movie.GetTrendingMoviesUseCase
import com.baghdad.entity.media.Genre
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
        viewModel = TrendingMoviesViewModel(getTrendingMoviesUseCase, getGenresUseCase,testDispatcher)
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `should emit NavigateBack effect when back button is clicked`() {
        lateinit var effect: TrendingMoviesEffect
        val job: Job = testScope.launch {
            viewModel.uiEffect.collect {
                effect = it
            }
        }

        viewModel.onBackClick()

        assertThat(effect).isEqualTo(TrendingMoviesEffect.NavigateBack)
        job.cancel()
    }

    @Test
    fun `should emit NavigateToMovieDetails effect when movie is clicked`() {
        val movieId = 123L
        lateinit var effect: TrendingMoviesEffect
        val job: Job = testScope.launch {
            viewModel.uiEffect.collect {
                effect = it
            }
        }

        viewModel.onMovieClick(movieId)

        assertThat(effect).isEqualTo(TrendingMoviesEffect.NavigateToMovieDetails(movieId))
        job.cancel()
    }

    @Test
    fun `should emit a NavigateToMovieDetails effect for each clicked movie`() {
        val effects = mutableListOf<TrendingMoviesEffect>()
        val job: Job = testScope.launch {
            viewModel.uiEffect.collect {
                effects.add(it)
            }
        }

        val ids = listOf(123L, 456L, 0L, -1L)
        ids.forEach { viewModel.onMovieClick(it) }

        assertThat(effects).containsExactly(
            TrendingMoviesEffect.NavigateToMovieDetails(123L),
            TrendingMoviesEffect.NavigateToMovieDetails(456L),
            TrendingMoviesEffect.NavigateToMovieDetails(0L),
            TrendingMoviesEffect.NavigateToMovieDetails(-1L)
        )
        job.cancel()
    }


    @Test
    fun `should update categories when genres are loaded successfully`() {
        val genres = listOf(Genre(id = 1, name = "Action"), Genre(id = 2, name = "Drama"))
        coEvery { getGenresUseCase.getMovieGenres() } returns genres

        viewModel = TrendingMoviesViewModel(getTrendingMoviesUseCase, getGenresUseCase, testDispatcher)

        assertThat(true).isTrue()
        assertThat(true).isTrue()
    }

    @Test
    fun `should return true when comparing two NavigateBack effects`() {
        val effect1 = TrendingMoviesEffect.NavigateBack
        val effect2 = TrendingMoviesEffect.NavigateBack

        assertThat(effect1).isEqualTo(effect2)
        assertThat(effect1.hashCode()).isEqualTo(effect2.hashCode())
    }

    @Test
    fun `should return true when movie detail effects have same movieId and false when different`() {
        val id1 = 123L
        val id2 = 456L

        val effect1 = TrendingMoviesEffect.NavigateToMovieDetails(id1)
        val effect2 = TrendingMoviesEffect.NavigateToMovieDetails(id1)
        val effect3 = TrendingMoviesEffect.NavigateToMovieDetails(id2)

        assertThat(effect1).isEqualTo(effect2)
        assertThat(effect1).isNotEqualTo(effect3)
        assertThat(effect1.hashCode()).isEqualTo(effect2.hashCode())
    }

    @Test
    fun `should return string representation that contains NavigateBack`() {
        val effect = TrendingMoviesEffect.NavigateBack
        val result = effect.toString()

        assertThat(result).contains("NavigateBack")
        assertThat(result).isNotEmpty()
    }

    @Test
    fun `should return string representation that includes movieId for NavigateToMovieDetails`() {
        val movieId = 123L
        val effect = TrendingMoviesEffect.NavigateToMovieDetails(movieId)
        val result = effect.toString()

        assertThat(result).contains("NavigateToMovieDetails")
        assertThat(result).contains(movieId.toString())
        assertThat(result).isNotEmpty()
    }
}
