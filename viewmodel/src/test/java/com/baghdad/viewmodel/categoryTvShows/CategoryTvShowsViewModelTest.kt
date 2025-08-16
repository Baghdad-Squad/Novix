package com.baghdad.viewmodel.categoryTvShows

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.baghdad.domain.usecase.genre.GetTvShowGenreNameByIdUseCase
import com.baghdad.domain.usecase.tvShow.GetTvShowsByGenreUseCase
import com.baghdad.viewmodel.categoryTvShows.CategoryTvShowsState.TvShowUiState
import com.baghdad.viewmodel.home.FakeHomeScreenData.tvShow
import com.baghdad.viewmodel.topTvShowPicks.MockTvShow
import com.google.common.truth.Truth.assertThat
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CategoryTvShowsViewModelTest {

    private val getTvShowsByGenreUseCase = mockk<GetTvShowsByGenreUseCase>()
    private val getCategoryNameByIdUseCase = mockk<GetTvShowGenreNameByIdUseCase>()

    private lateinit var viewModel: CategoryTvShowsViewModel
    private val testDispatcher = StandardTestDispatcher()

    @BeforeEach
    fun setup() {
        Dispatchers.setMain(testDispatcher)
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }


    @Test
    fun `onBackClicked should emit NavigateBack when back button clicked`() = runTest {
        viewModel = createViewModel()

        viewModel.onBackClicked()

        viewModel.uiEffect.test {
            assertThat(awaitItem()).isEqualTo(CategoryTvShowsEffect.NavigateBack)
        }
    }

    @Test
    fun `onTvShowClicked should emit NavigateToTvShowDetails when tv show clicked`() = runTest {
        viewModel = createViewModel()

        viewModel.onTvShowClicked(tvShowId = tvShow.id)

        viewModel.uiEffect.test {
            assertThat(awaitItem())
                .isEqualTo(CategoryTvShowsEffect.NavigateToTvShowDetails(tvShow.id))
        }
    }

    @Test
    fun `toUiState should map TvShow to TvShowUiState correctly`() {
        val testTvShow = MockTvShow.TV_SHOW
        val expectedUiState = TvShowUiState(
            id = testTvShow.id,
            posterPictureURL = testTvShow.posterImageURL
        )

        val uiState = testTvShow.toUiState()

        assertThat(uiState).isEqualTo(expectedUiState)
    }

    @Test
    fun `onSuccessGetCategoryName should update categoryName when category name changed`() =
        runTest {
            viewModel = createViewModel()

            viewModel.onSuccessGetCategoryName(tvShow.title)

            assertThat(viewModel.uiState.value.categoryName).isEqualTo(tvShow.title)
        }

    @Test
    fun `onSnackBarActionLabelClick should hide snackBar when action label clicked`() =
        runTest {
            viewModel = createViewModel()

            viewModel.onSnackBarActionLabelClick()
            assertThat(viewModel.snackBarState.value.isVisible).isFalse()
        }

    private fun createViewModel(): CategoryTvShowsViewModel {
        return CategoryTvShowsViewModel(
            savedStateHandle = SavedStateHandle(mapOf("categoryId" to 1L)),
            getTvShowsCategoryUseCase = getTvShowsByGenreUseCase,
            getCategoryNameByIdUseCase = getCategoryNameByIdUseCase,
            ioDispatcher = testDispatcher
        )
    }
}