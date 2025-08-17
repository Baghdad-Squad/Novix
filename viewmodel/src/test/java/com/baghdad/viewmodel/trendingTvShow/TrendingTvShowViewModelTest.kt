package com.baghdad.viewmodel.trendingTvShow

import app.cash.turbine.test
import com.baghdad.domain.usecase.tvShow.GetTrendingTvShowUseCase
import com.baghdad.domain.usecase.tvShow.GetTvShowGenresUseCase
import com.baghdad.entity.media.Genre
import com.baghdad.viewmodel.topTvShowPicks.MockTvShow
import com.baghdad.viewmodel.trendingTvShow.TrendingTvShowScreenState.GenreUiState
import com.google.common.truth.Truth.assertThat
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
class TrendingTvShowViewModelTest {
    private val testDispatcher = StandardTestDispatcher()

    private val getTrendingTvShowUseCase = mockk<GetTrendingTvShowUseCase>()
    private val getTvShowGenresUseCase = mockk<GetTvShowGenresUseCase>()

    private lateinit var viewModel: TrendingTvShowViewModel


    @BeforeEach
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @Test
    fun `should load genres and trendingTvShows when viewModel initialized`() = runTest {
        viewModel = createViewModel()

        viewModel.uiState.test {
            assertThat(awaitItem().genres).isEmpty()
        }
    }

    @Test
    fun `onTvShowClicked should send NavigateToTvShowDetails effect with correct tvShowId when tv show clicked`() =
        runTest {
            val tvShow = MockTvShow.TV_SHOW
            viewModel = createViewModel()

            viewModel.onTvShowClicked(tvShow.id)

            viewModel.uiEffect.test {
                assertThat(awaitItem()).isEqualTo(
                    TrendingTvShowEffect.NavigateToTvShowDetails(tvShow.id)
                )
            }
        }

    @Test
    fun `onBackIconClicked should send NavigateBack effect when back icon clicked`() = runTest {
        viewModel = createViewModel()

        viewModel.onBackIconClicked()

        viewModel.uiEffect.test {
            assertThat(awaitItem()).isEqualTo(TrendingTvShowEffect.NavigateBack)
        }
    }

    @Test
    fun `onSnackBarActionLabelClicked should refresh data when snackBar action label clicked`() =
        runTest {
            viewModel = createViewModel()

            viewModel.onSnackBarActionLabelClicked()

            viewModel.uiState.test {
                assertThat(awaitItem().selectedGenreId).isEqualTo(null)
            }
        }


    @Test
    fun `onGenreClicked should update trendingTvShows and selectedGenreId when genre clicked`() =
        runTest {
            viewModel = createViewModel()

            viewModel.onGenreClicked(genre.id)

            viewModel.uiState.test {
                assertThat(awaitItem().selectedGenreId).isEqualTo(genre.id)
            }
        }

    @Test
    fun `toUiState should return correct GenreUiState when genre mapped to ui state`() {
        val expectedGenre = GenreUiState(id = genre.id, name = genre.name)

        val genreUi = genre.toUiState()

        assertThat(genreUi).isEqualTo(expectedGenre)
    }

    companion object {
        private val genre = Genre(id = 1L, name = "Action")
    }

    private fun createViewModel(): TrendingTvShowViewModel {
        return TrendingTvShowViewModel(
            getTrendingTvShowUseCase = getTrendingTvShowUseCase,
            getTvShowGenresUseCase = getTvShowGenresUseCase,
            ioDispatcher = testDispatcher
        )
    }
}