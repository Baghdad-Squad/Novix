package com.baghdad.viewmodel.topRating

import app.cash.turbine.test
import com.baghdad.domain.usecase.login.IsUserLoggedInUseCase
import com.baghdad.domain.usecase.movie.GetMovieGenresUseCase
import com.baghdad.domain.usecase.movie.GetMovieTopRatingUseCase
import com.baghdad.domain.usecase.savedList.AddMovieToSavedListUseCase
import com.baghdad.domain.usecase.savedList.CreateSavedListUseCase
import com.baghdad.domain.usecase.savedList.GetSavedListsUseCase
import com.baghdad.domain.usecase.savedList.RemoveMovieFromSavedListUseCase
import com.baghdad.domain.usecase.tvShow.GetTvShowGenresUseCase
import com.baghdad.domain.usecase.tvShow.GetTvShowTopRatingUseCase
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
class TopRatingViewModelTest {
    private val getMovieTopRatingUseCase = mockk<GetMovieTopRatingUseCase>()
    private val getTvShowTopRatingUseCase = mockk<GetTvShowTopRatingUseCase>()
    private val getMovieGenresUseCase = mockk<GetMovieGenresUseCase>()
    private val getTvShowGenresUseCase = mockk<GetTvShowGenresUseCase>()
    private val isUserLoggedInUseCase = mockk<IsUserLoggedInUseCase>()
    private val getSavedListsUseCase = mockk<GetSavedListsUseCase>()
    private val addMovieToSavedListUseCase = mockk<AddMovieToSavedListUseCase>()
    private val createSavedListUseCase = mockk<CreateSavedListUseCase>()
    private val removeMovieFromSavedListUseCase = mockk<RemoveMovieFromSavedListUseCase>()

    private lateinit var viewModel: TopRatingViewModel
    private val testDispatcher = StandardTestDispatcher()

    @BeforeEach
    fun setUp() {
        Dispatchers.setMain(testDispatcher)

        viewModel = TopRatingViewModel(
            getMovieTopRatingUseCase,
            getTvShowTopRatingUseCase,
            getMovieGenresUseCase,
            getTvShowGenresUseCase,
            isUserLoggedInUseCase,
            getSavedListsUseCase,
            addMovieToSavedListUseCase,
            createSavedListUseCase,
            removeMovieFromSavedListUseCase,
        )
    }

    @Test
    fun `onLoginClick should navigate to login screen when it is clicked`() = runTest {
        viewModel.onLoginClick()

        viewModel.uiEffect.test {
            val effect = awaitItem()
            assertThat(effect is TopRatingEffect.NavigateToLogin).isTrue()
        }
    }

    @Test
    fun `onMovieDetailsClick should navigate to movie details screen when it is clicked`() =
        runTest {
            val movieId = 123L

            viewModel.onMovieDetailsClick(movieId)

            viewModel.uiEffect.test {
                val effect = awaitItem()
                assertThat(effect is TopRatingEffect.NavigateToMovieDetails).isTrue()
            }
        }

    @Test
    fun `onTvShowDetailsClick should navigate to tv show details screen when it is clicked`() =
        runTest {
            val tvShowId = 123L

            viewModel.onTvShowDetailsClick(tvShowId)

            viewModel.uiEffect.test {
                val effect = awaitItem()
                assertThat(effect is TopRatingEffect.NavigateToTvShowDetails).isTrue()
            }
        }

    @Test
    fun `onBackClick should navigate back when it is clicked`() = runTest {
        viewModel.onBackClick()

        viewModel.uiEffect.test {
            val effect = awaitItem()
            assertThat(effect is TopRatingEffect.NavigateBack).isTrue()
        }
    }

    @Test
    fun `onGenreClick should return updated genre tab when selected tab is movies`() = runTest {
        val genreId = 1L
        viewModel.onSelectedTab(TopRatingTab.MOVIES)

        viewModel.onGenreClick(genreId)

        assertThat(viewModel.uiState.value.selectedMovieGenreId == genreId).isTrue()
    }

    @Test
    fun `onGenreClick should return updated genre tab when selected tab is tv shows`() = runTest {
        val genreId = 1L
        viewModel.onSelectedTab(TopRatingTab.TV_SHOWS)

        viewModel.onGenreClick(genreId)

        assertThat(viewModel.uiState.value.selectedTvShowGenreId == genreId).isTrue()
    }

    @Test
    fun `onSelectedTab should return genres and fetch movies when selected tab is movies`() =
        runTest {

            viewModel.onSelectedTab(TopRatingTab.TV_SHOWS)
            coEvery { getMovieGenresUseCase.getMovieGenres() } returns emptyList()
            val selectedTab = TopRatingTab.MOVIES

            viewModel.onSelectedTab(selectedTab)
            advanceUntilIdle()

            assertThat(viewModel.uiState.value.selectedTab == selectedTab).isTrue()
            coVerify { getMovieGenresUseCase.getMovieGenres() }
        }

    @Test
    fun `onSelectedTab should return genres and fetch tv shows when selected tab is tv shows`() =
        runTest {
            val selectedTab = TopRatingTab.TV_SHOWS
            coEvery { getTvShowGenresUseCase.getTvShowGenres() } returns emptyList()

            viewModel.onSelectedTab(selectedTab)
            advanceUntilIdle()

            assertThat(viewModel.uiState.value.selectedTab == selectedTab).isTrue()
            coVerify { getTvShowGenresUseCase.getTvShowGenres() }
        }

    @Test
    fun `onSnackBarActionLabelClick should hide snackBar and return genres and fetch tv shows when selected tab is tv shows`() =
        runTest {

            viewModel.onSelectedTab(TopRatingTab.TV_SHOWS)
            coEvery { getTvShowGenresUseCase.getTvShowGenres() } returns emptyList()

            viewModel.onSnackBarActionLabelClick()
            advanceUntilIdle()

            assertThat(viewModel.snackBarState.value.isVisible == false).isTrue()
            coVerify { getTvShowGenresUseCase.getTvShowGenres() }
        }

    @Test
    fun `onSnackBarActionLabelClick should hide snackBar and return genres and fetch movies when selected tab is movies`() =
        runTest {

            coEvery { getMovieGenresUseCase.getMovieGenres() } returns emptyList()

            viewModel.onSnackBarActionLabelClick()
            advanceUntilIdle()

            assertThat(viewModel.snackBarState.value.isVisible == false).isTrue()
            coVerify { getMovieGenresUseCase.getMovieGenres() }
        }

    @Test
    fun `onCreateNewListClick should show list bottom sheet when it is clicked`() = runTest {

        viewModel.onCreateNewListClick()
        advanceUntilIdle()

        assertThat(viewModel.uiState.value.addListBottomSheetState.isVisible).isTrue()
        assertThat(viewModel.uiState.value.addToListBottomSheetState.isVisible).isTrue()
    }

    @Test
    fun `onListSelected should update selectedListId when it is clicked`() = runTest {

        val selectedListId = 123L

        viewModel.onListSelected(selectedListId)
        advanceUntilIdle()

        assertThat(viewModel.uiState.value.addToListBottomSheetState.selectedListId == selectedListId).isTrue()
    }

    @Test
    fun `onCreatedListNameChanged should update listName when it is clicked`() = runTest {

        val listName = "action"

        viewModel.onCreatedListNameChanged(listName)
        advanceUntilIdle()

        assertThat(viewModel.uiState.value.addListBottomSheetState.listName == listName).isTrue()
    }

    @Test
    fun `onCreateListBottomSheetDismiss should hide bottom sheet and show addToListBottomSheet when it is clicked`() =
        runTest {

            viewModel.onCreateListBottomSheetDismiss()
            advanceUntilIdle()

            val addListState = viewModel.uiState.value.addListBottomSheetState
            val addMovieToListState = viewModel.uiState.value.addToListBottomSheetState
            with(addListState) {
                assertThat(isVisible).isFalse()
                assertThat(listName == "").isTrue()
                assertThat(isLoading).isFalse()
            }
            assertThat(addMovieToListState.isVisible).isTrue()
        }

    @Test
    fun `onTopRatingItemSaveClick should show bottom sheet when movie not saved`() =
        runTest {
            val movie = MOVIE_UI_STATE.copy(isSaved = false)

            viewModel.onTopRatingItemSaveClick(movie)
            advanceUntilIdle()

            val addToListBottomSheetState = viewModel.uiState.value.addToListBottomSheetState
            assertThat(addToListBottomSheetState.isVisible).isTrue()
            assertThat(addToListBottomSheetState.selectedItemId == movie.id).isTrue()
            assertThat(addToListBottomSheetState.selectedListId == null).isTrue()
        }

    @Test
    fun `onTopRatingItemSaveClick should remove save item when movie is saved`() = runTest {
        coEvery { removeMovieFromSavedListUseCase(any(), any()) } returns Unit

        viewModel.onTopRatingItemSaveClick(MOVIE_UI_STATE)
        advanceUntilIdle()

        coVerify { removeMovieFromSavedListUseCase(any(), any()) }
    }

    @Test
    fun `onCreateListBottomSheetAddClick should create new list when it is clicked`() = runTest {
        coEvery { createSavedListUseCase(any()) } returns Unit

        viewModel.onCreateListBottomSheetAddClick()

        viewModel.uiState.test {
            val state = awaitItem()
            assertThat(state.addListBottomSheetState.isVisible).isFalse()
            assertThat(state.addToListBottomSheetState.isVisible).isTrue()
        }
        coVerify { createSavedListUseCase(any()) }
    }

    @Test
    fun `onSaveMovieClick should save movie to list when it is clicked`() = runTest {

        viewModel.onListSelected(123L)

        viewModel.onSaveItemToListClicked()
        advanceUntilIdle()

        coVerify { addMovieToSavedListUseCase(any(), any()) }
    }

    companion object {
        private val MOVIE_UI_STATE = TopRatingState.MovieUiState(
            id = 123L,
            isSaved = true,
            posterPictureURL = "",
            savedListId = 123L
        )
    }
}
