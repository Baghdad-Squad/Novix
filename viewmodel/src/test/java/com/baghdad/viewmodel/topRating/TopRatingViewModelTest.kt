package com.baghdad.viewmodel.topRating

import com.baghdad.domain.usecase.genre.GetGenresUseCase
import com.baghdad.domain.usecase.login.IsUserLoggedInUseCase
import com.baghdad.domain.usecase.savedList.AddMovieToSavedListUseCase
import com.baghdad.domain.usecase.savedList.CreateSavedListUseCase
import com.baghdad.domain.usecase.savedList.GetSavedListsUseCase
import com.baghdad.domain.usecase.savedList.RemoveMovieFromSavedListUseCase
import com.baghdad.domain.usecase.topRated.GetMovieTopRatingUseCase
import com.baghdad.domain.usecase.topRated.GetTvShowTopRatingUseCase
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
class TopRatingViewModelTest {
    private lateinit var getMovieTopRatingUseCase: GetMovieTopRatingUseCase
    private lateinit var getTvShowTopRatingUseCase: GetTvShowTopRatingUseCase
    private lateinit var getGenresUseCase: GetGenresUseCase
    private lateinit var isUserLoggedInUseCase: IsUserLoggedInUseCase
    private lateinit var getSavedListsUseCase: GetSavedListsUseCase
    private lateinit var addMovieToSavedListUseCase: AddMovieToSavedListUseCase
    private lateinit var createSavedListUseCase: CreateSavedListUseCase
    private lateinit var removeMovieFromSavedListUseCase: RemoveMovieFromSavedListUseCase

    private lateinit var viewModel: TopRatingViewModel
    private val testDispatcher = StandardTestDispatcher()

    @BeforeEach
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        getMovieTopRatingUseCase = mockk(relaxed = true)
        getTvShowTopRatingUseCase = mockk(relaxed = true)
        getGenresUseCase = mockk(relaxed = true)
        isUserLoggedInUseCase = mockk(relaxed = true)
        getSavedListsUseCase = mockk(relaxed = true)
        addMovieToSavedListUseCase = mockk(relaxed = true)
        createSavedListUseCase = mockk(relaxed = true)
        removeMovieFromSavedListUseCase = mockk(relaxed = true)
        viewModel = TopRatingViewModel(
            getMovieTopRatingUseCase,
            getTvShowTopRatingUseCase,
            getGenresUseCase,
            isUserLoggedInUseCase,
            getSavedListsUseCase,
            addMovieToSavedListUseCase,
            createSavedListUseCase,
            removeMovieFromSavedListUseCase,
        )
    }

    @Test
    fun `onLoginClick should navigate to login screen when it is clicked`() =
        runTest {

            // Given
            var receivedEffect: TopRatingEffect? = null
            val job = launch {
                viewModel.uiEffect.collect { effect ->
                    receivedEffect = effect
                }
            }

            // When
            viewModel.onLoginClick()
            advanceUntilIdle()

            // Then
            assertThat(
                receivedEffect is TopRatingEffect.NavigateToLogin,
            ).isTrue()
            job.cancel()
        }

    @Test
    fun `onMovieDetailsClick should navigate to movie details screen when it is clicked`() =
        runTest {
            // Given
            var receivedEffect: TopRatingEffect? = null
            val movieId = 123L
            val job = launch {
                viewModel.uiEffect.collect { effect ->
                    receivedEffect = effect
                }
            }
            // When
            viewModel.onMovieDetailsClick(movieId)

            advanceUntilIdle()
            // Then
            assertThat(
                receivedEffect is TopRatingEffect.NavigateToMovieDetails,
            ).isTrue()
            job.cancel()
        }

    @Test
    fun `onTvShowDetailsClick should navigate to tv show details screen when it is clicked`() =
        runTest {
            // Given
            var receivedEffect: TopRatingEffect? = null
            val tvShowId = 123L
            val job = launch {
                viewModel.uiEffect.collect { effect ->
                    receivedEffect = effect
                }
            }
            // When
            viewModel.onTvShowDetailsClick(tvShowId)
            advanceUntilIdle()
            // Then
            assertThat(
                receivedEffect is TopRatingEffect.NavigateToTvShowDetails,
            ).isTrue()
            job.cancel()
        }

    @Test
    fun `onBackClick should navigate back when it is clicked`() = runTest {
        // Given
        var receivedEffect: TopRatingEffect? = null
        val job = launch() {
            viewModel.uiEffect.collect { effect ->
                receivedEffect = effect
            }
        }

        // When
        viewModel.onBackClick()
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        assertThat(
            receivedEffect is TopRatingEffect.NavigateBack
        ).isTrue()
        job.cancel()
    }

    @Test
    fun `onGenreClick should return updated genre tab when selected tab is movies`() = runTest {
        // Given
        val genreId = 1L
        viewModel.onSelectedTab(TopRatingTab.MOVIES)
        // When
        viewModel.onGenreClick(genreId)
        // Then
        assertThat(
            viewModel.uiState.value.selectedMovieGenreId == genreId
        ).isTrue()
    }

    @Test
    fun `onGenreClick should return updated genre tab when selected tab is tv shows`() = runTest {
        // Given
        val genreId = 1L
        viewModel.onSelectedTab(TopRatingTab.TV_SHOWS)
        // When
        viewModel.onGenreClick(genreId)
        // Then
        assertThat(
            viewModel.uiState.value.selectedTvShowGenreId == genreId
        ).isTrue()
    }

    @Test
    fun `onSelectedTab should return genres and fetch movies when selected tab is movies`() =
        runTest {
            // Given
            viewModel.onSelectedTab(TopRatingTab.TV_SHOWS)
            coEvery { getGenresUseCase.getMovieGenres() } returns emptyList()
            val selectedTab = TopRatingTab.MOVIES
            // When
            viewModel.onSelectedTab(selectedTab)
            advanceUntilIdle()
            // Then
            assertThat(
                viewModel.uiState.value.selectedTab == selectedTab
            ).isTrue()
            coVerify { getGenresUseCase.getMovieGenres() }
        }

    @Test
    fun `onSelectedTab should return genres and fetch tv shows when selected tab is tv shows`() =
        runTest {
            // Given
            val selectedTab = TopRatingTab.TV_SHOWS
            coEvery { getGenresUseCase.getTvShowGenres() } returns emptyList()
            // When
            viewModel.onSelectedTab(selectedTab)
            advanceUntilIdle()
            // Then
            assertThat(
                viewModel.uiState.value.selectedTab == selectedTab
            ).isTrue()
            coVerify { getGenresUseCase.getTvShowGenres() }
        }

    @Test
    fun `onSnackBarActionLabelClick should hide snackBar and return genres and fetch tv shows when selected tab is tv shows`() =
        runTest {
            // Given
            viewModel.onSelectedTab(TopRatingTab.TV_SHOWS)
            coEvery { getGenresUseCase.getTvShowGenres() } returns emptyList()
            // When
            viewModel.onSnackBarActionLabelClick()
            advanceUntilIdle()
            // Then
            assertThat(
                viewModel.snackBarState.value.isVisible == false
            ).isTrue()
            coVerify { getGenresUseCase.getTvShowGenres() }
        }

    @Test
    fun `onSnackBarActionLabelClick should hide snackBar and return genres and fetch movies when selected tab is movies`() =
        runTest {
            // Given
            coEvery { getGenresUseCase.getMovieGenres() } returns emptyList()
            // When
            viewModel.onSnackBarActionLabelClick()
            advanceUntilIdle()
            // Then
            assertThat(
                viewModel.snackBarState.value.isVisible == false
            ).isTrue()
            coVerify { getGenresUseCase.getMovieGenres() }
        }

    @Test
    fun `onCreateNewListClick should show list bottom sheet when it is clicked`() = runTest {
        // Given
        val addListBottomSheetState = true
        val addToListBottomSheetState = false
        // When
        viewModel.onCreateNewListClick()
        advanceUntilIdle()
        // Then
        assertThat(
            viewModel.uiState.value.addListBottomSheetState.isVisible == addListBottomSheetState
        ).isTrue()
        assertThat(
            viewModel.uiState.value.addToListBottomSheetState.isVisible == addToListBottomSheetState
        ).isTrue()

    }

    @Test
    fun `onListSelected should update selectedListId when it is clicked`() = runTest {
        // Given
        val selectedListId = 123L
        // When
        viewModel.onListSelected(selectedListId)
        advanceUntilIdle()
        // Then
        assertThat(
            viewModel.uiState.value.addToListBottomSheetState.selectedListId == selectedListId
        ).isTrue()
    }

    @Test
    fun `onCreatedListNameChanged should update listName when it is clicked`() = runTest {
        // Given
        val listName = "action"
        // When
        viewModel.onCreatedListNameChanged(listName)
        advanceUntilIdle()
        // Then
        assertThat(
            viewModel.uiState.value.addListBottomSheetState.listName == listName
        ).isTrue()
    }

    @Test
    fun `onCreateListBottomSheetDismiss should hide bottom sheet and show addToListBottomSheet when it is clicked`() =
        runTest {
            // When
            viewModel.onCreateListBottomSheetDismiss()
            advanceUntilIdle()
            // Then
            val addListBottomSheetState = viewModel.uiState.value.addListBottomSheetState
            val addToListBottomSheetState = viewModel.uiState.value.addToListBottomSheetState
            assertThat(addListBottomSheetState.isVisible == false).isTrue()
            assertThat(addListBottomSheetState.listName == "").isTrue()
            assertThat(addListBottomSheetState.isLoading == false).isTrue()
            assertThat(addToListBottomSheetState.isVisible == true).isTrue()
        }

    @Test
    fun `onTopRatingItemSaveClick should update addToListBottomSheetState when it is clicked`() =
        runTest {
            // Given
            val item = TopRatingState.MovieUiState(
                id = 123L,
                isSaved = false,
                posterPictureURL = "",
                savedListId = 123L
            )
            // When
            viewModel.onTopRatingItemSaveClick(item)
            advanceUntilIdle()
            // Then
            val addToListBottomSheetState = viewModel.uiState.value.addToListBottomSheetState
            assertThat(addToListBottomSheetState.isVisible == true).isTrue()
            assertThat(addToListBottomSheetState.selectedItemId == item.id).isTrue()
            assertThat(addToListBottomSheetState.selectedListId == null).isTrue()
        }

    @Test
    fun `onTopRatingItemSaveClick should remove save item when it is clicked`() = runTest {
        // Given
        val item = TopRatingState.MovieUiState(
            id = 123L,
            isSaved = true,
            posterPictureURL = "",
            savedListId = 123L
        )
        coEvery { removeMovieFromSavedListUseCase(any(), any()) } returns Unit
        // When
        viewModel.onTopRatingItemSaveClick(item)
        advanceUntilIdle()
        // Then
        coVerify { removeMovieFromSavedListUseCase(any(), any()) }
    }

    @Test
    fun `onCreateListBottomSheetAddClick should create new list when it is clicked`() = runTest {
        //Given
        coEvery { createSavedListUseCase(any()) } returns Unit
        val states = mutableListOf<TopRatingState>()
        // When
        val job = launch {
            viewModel.uiState.collect { states.add(it) }
        }
        viewModel.onCreateListBottomSheetAddClick()
        advanceUntilIdle()
        // Then
        assertThat(states.last().addListBottomSheetState.isVisible == false).isTrue()
        assertThat(states.last().addToListBottomSheetState.isVisible == true).isTrue()
        coVerify { createSavedListUseCase(any()) }
        job.cancel()
    }

    @Test
    fun `onSaveMovieClick should save movie to list when it is clicked`() = runTest {
        // Given
        viewModel.onListSelected(123L)
        // When
        viewModel.onSaveMovieClick()
        advanceUntilIdle()
        // Then
        coVerify { addMovieToSavedListUseCase(any(), any()) }

    }

}