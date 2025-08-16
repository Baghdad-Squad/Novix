package com.baghdad.viewmodel.savedListDetails

import androidx.lifecycle.SavedStateHandle
import androidx.paging.PagingData
import app.cash.turbine.test
import com.baghdad.domain.model.pagination.PagedResult
import com.baghdad.domain.model.savedList.SavedListDetails
import com.baghdad.domain.model.savedList.SavedMovie
import com.baghdad.domain.usecase.savedList.DeleteSavedListUseCase
import com.baghdad.domain.usecase.savedList.GetSavedListDetailsUseCase
import com.baghdad.domain.usecase.savedList.RemoveMovieFromSavedListUseCase
import com.baghdad.entity.media.Movie
import com.baghdad.entity.savedList.SavedList
import com.baghdad.viewmodel.savedListDetails.SavedListDetailsScreenState.SavedListDetailsMovieUiState
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
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
class SavedListDetailsViewModelTest {
    private val getSavedListDetailsUseCase = mockk<GetSavedListDetailsUseCase>(relaxed = true)
    private val deleteSavedListUseCase = mockk<DeleteSavedListUseCase>(relaxed = true)
    private val removeMovieFromSavedListUseCase =
        mockk<RemoveMovieFromSavedListUseCase>(relaxed = true)

    val testDispatcher = StandardTestDispatcher()
    lateinit var viewModel: SavedListDetailsViewModel

    fun createViewModel() = SavedListDetailsViewModel(
        getSavedListDetailsUseCase = getSavedListDetailsUseCase,
        deleteSavedListUseCase = deleteSavedListUseCase,
        removeMovieFromSavedListUseCase = removeMovieFromSavedListUseCase,
        savedStateHandle = SavedStateHandle(mapOf("listId" to 1)),
        defaultDispatcher = testDispatcher
    )

    @BeforeEach
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `should get saved list details when the view model is created`() = runTest {
        coEvery {
            getSavedListDetailsUseCase.invoke(
                listId = 1, page = 1, pageSize = 20
            )
        } returns savedListDetailsSample

        viewModel = createViewModel()
        advanceUntilIdle()

        coVerify(exactly = 1) {
            getSavedListDetailsUseCase(
                listId = any(), page = any(), pageSize = any()
            )
        }
    }

    @Test
    fun `should return updated saved list details when getSavedListDetailsUseCase returns success`() =
        runTest {
            coEvery {
                getSavedListDetailsUseCase.invoke(
                    listId = 1, page = 1, pageSize = 20
                )
            } returns savedListDetailsSample

            viewModel = createViewModel()
            advanceUntilIdle()

            val state = viewModel.uiState.value.mediaFlow
            assertThat(state).isNotEqualTo(flowOf<PagingData<SavedListDetailsMovieUiState>>())
        }


    @Test
    fun `should emit navigate back when onBackClicked`() = runTest {
        viewModel = createViewModel()

        viewModel.onBackClick()

        val effect = viewModel.uiEffect.first()
        assertThat(effect).isEqualTo(SavedListDetailsEffect.NavigateBack)
    }

    @Test
    fun `should call deleteSavedListUseCase when onDeleteClicked`() = runTest {
        viewModel = createViewModel()

        viewModel.onRemoveSavedMovieClick(1L)
        advanceUntilIdle()

        coVerify { removeMovieFromSavedListUseCase(listId = any(), movieId = 1) }
    }

    @Test
    fun `should show delete list bottom sheet when onDelete clicked`() = runTest {
        viewModel = createViewModel()

        viewModel.onDeleteClick()

        val currentState = viewModel.uiState.value
        assertThat(currentState.isConfirmDeleteDialogVisible).isTrue()
    }

    @Test
    fun `should navigate to movie detail screen when onMovieClicked`() = runTest {
        viewModel = createViewModel()

        viewModel.onMovieClick(1)
        val effect = viewModel.uiEffect.first()

        assertThat(effect).isEqualTo(SavedListDetailsEffect.NavigateToMovieDetails(1))
    }

    @Test
    fun `should call removeMovieFromSavedListUseCase when onRemoveSavedMovieClick`() = runTest {
        viewModel = createViewModel()

        viewModel.onRemoveSavedMovieClick(1L)
        advanceUntilIdle()

        coVerify { removeMovieFromSavedListUseCase(listId = any(), movieId = 1) }
    }

    @Test
    fun `should call deleteSavedListUseCase when onDeleteListBottomSheetDeleteClick`() = runTest {
        val viewModel = createViewModel()

        viewModel.onDeleteListBottomSheetDeleteClick()
        advanceUntilIdle()

        coVerify { deleteSavedListUseCase(any()) }
    }

    @Test
    fun `should navigate back when onDeleteListBottomSheetCancelClick done successfully`() =
        runTest {
            val viewModel = createViewModel()
            coEvery {
                deleteSavedListUseCase(any())
            } just runs

            viewModel.onDeleteListBottomSheetDeleteClick()

            viewModel.uiEffect.test {
                val effect = awaitItem()
                assertThat(effect).isEqualTo(SavedListDetailsEffect.NavigateBack)
            }
        }

    @Test
    fun `should reload data of list when onSnackBarActionLabelClick`() = runTest {
        val viewModel = createViewModel()

        viewModel.onSnackBarActionLabelClick()
        advanceUntilIdle()

        coVerify(exactly = 2) {
            getSavedListDetailsUseCase.invoke(
                listId = any(), page = any(), pageSize = any()
            )
        }
    }


    private companion object {
        val savedListSample = SavedList(
            id = 1, name = "OMER", itemCount = 5
        )
        val moviesSample = listOf<Movie>(
            Movie(
                id = 1,
                title = "Movie 1",
                genres = emptyList(),
                averageRating = 7.0,
                userRating = null,
                releaseDate = LocalDate(2023, 1, 1),
                overview = "First movie",
                posterImageURL = "https://example.com/poster1.jpg",
                trailerURL = "https://example.com/trailer1.mp4",
                runtimeMinutes = 100
            ), Movie(
                id = 2,
                title = "Movie 2",
                genres = emptyList(),
                averageRating = 8.0,
                userRating = null,
                releaseDate = LocalDate(2023, 2, 1),
                overview = "Second movie",
                posterImageURL = "https://example.com/poster2.jpg",
                trailerURL = "https://example.com/trailer2.mp4",
                runtimeMinutes = 120
            ), Movie(
                id = 3,
                title = "Movie 3",
                genres = emptyList(),
                averageRating = 9.0,
                userRating = null,
                releaseDate = LocalDate(2023, 3, 1),
                overview = "Third movie",
                posterImageURL = "https://example.com/poster3.jpg",
                trailerURL = "https://example.com/trailer3.mp4",
                runtimeMinutes = 140
            ), Movie(
                id = 4,
                title = "Movie 4",
                genres = emptyList(),
                averageRating = 6.0,
                userRating = null,
                releaseDate = LocalDate(2023, 4, 1),
                overview = "Fourth movie",
                posterImageURL = "https://example.com/poster4.jpg",
                trailerURL = "https://example.com/trailer4.mp4",
                runtimeMinutes = 160
            )
        )
        val savedMediaItemsSample = moviesSample.map {
            SavedMovie(
                movie = it, isSaved = true, listId = 1
            )
        }
        val savedListDetailsSample = SavedListDetails(
            savedList = savedListSample, pagedItems = PagedResult(
                data = savedMediaItemsSample, nextPage = null, prevPage = null
            )
        )
    }
}
