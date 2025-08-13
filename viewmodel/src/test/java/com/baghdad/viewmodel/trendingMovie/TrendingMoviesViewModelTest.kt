package com.baghdad.viewmodel.trendingMovie

import com.baghdad.domain.usecase.login.IsUserLoggedInUseCase
import com.baghdad.domain.usecase.movie.GetMovieGenresUseCase
import com.baghdad.domain.usecase.movie.GetTrendingMoviesUseCase
import com.baghdad.domain.usecase.savedList.AddMovieToSavedListUseCase
import com.baghdad.domain.usecase.savedList.CreateSavedListUseCase
import com.baghdad.domain.usecase.savedList.GetSavedListsUseCase
import com.baghdad.domain.usecase.savedList.RemoveMovieFromSavedListUseCase
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@ExperimentalCoroutinesApi
class TrendingMoviesViewModelTest {

    private lateinit var getTrendingMoviesUseCase: GetTrendingMoviesUseCase
    private lateinit var getMovieGenresUseCase: GetMovieGenresUseCase
    private lateinit var isUserLoggedInUseCase: IsUserLoggedInUseCase
    private lateinit var getSavedListsUseCase: GetSavedListsUseCase
    private lateinit var addMovieToSavedListUseCase: AddMovieToSavedListUseCase
    private lateinit var createSavedListUseCase: CreateSavedListUseCase
    private lateinit var removeMovieFromSavedListUseCase: RemoveMovieFromSavedListUseCase
    private lateinit var viewModel: TrendingMoviesViewModel
    private val testDispatcher = UnconfinedTestDispatcher()
    private val testScope = TestScope(testDispatcher)

    @BeforeEach
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        getTrendingMoviesUseCase = mockk()
        getMovieGenresUseCase = mockk()
        isUserLoggedInUseCase = mockk()
        getSavedListsUseCase = mockk()
        addMovieToSavedListUseCase = mockk()
        createSavedListUseCase = mockk()
        removeMovieFromSavedListUseCase = mockk()
        viewModel = TrendingMoviesViewModel(
            getTrendingMoviesUseCase,
            getMovieGenresUseCase,
            isUserLoggedInUseCase,
            getSavedListsUseCase,
            addMovieToSavedListUseCase,
            createSavedListUseCase,
            removeMovieFromSavedListUseCase,
            testDispatcher
        )
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `should send NavigateBack effect when back button is clicked`() {
        // Given
        var effect: TrendingMoviesEffect = TrendingMoviesEffect.NavigateBack
        val job: Job = testScope.launch { viewModel.uiEffect.collect { effect = it } }

        // When
        viewModel.onBackClicked()

        // Then
        assertThat(effect).isEqualTo(TrendingMoviesEffect.NavigateBack)
        job.cancel()
    }

    @Test
    fun `should send NavigateToMovieDetails effect when movie is clicked`() {
        // Given
        var effect: TrendingMoviesEffect = TrendingMoviesEffect.NavigateToMovieDetails(MOVIE_ID)
        val job: Job = testScope.launch { viewModel.uiEffect.collect { effect = it } }

        // When
        viewModel.onMovieClicked(MOVIE_ID)

        //Then
        assertThat(effect).isEqualTo(TrendingMoviesEffect.NavigateToMovieDetails(MOVIE_ID))
        job.cancel()
    }

    @Test
    fun `should send NavigationToLogin effect when login button is clicked`() {
        // Given
        var effect: TrendingMoviesEffect = TrendingMoviesEffect.NavigateToLogin
        val job: Job = testScope.launch { viewModel.uiEffect.collect { effect = it } }

        // When
        viewModel.onLoginClicked()

        // Then
        assertThat(effect).isEqualTo(TrendingMoviesEffect.NavigateToLogin)
        job.cancel()
    }

    @Test
    fun `should create new list when create new list button is clicked`() {
        // When
        viewModel.onCreateNewListClicked()

        // Then
        val state = viewModel.uiState.value
        assertThat(state.addListBottomSheetState.isVisible).isTrue()
        assertThat(state.addToListBottomSheetState.isVisible).isFalse()
    }

    @Test
    fun `should close add list bottom sheet when dismiss button is clicked`() {
        // When
        viewModel.onCreateListBottomSheetDismiss()

        // Then
        val state = viewModel.uiState.value
        assertThat(state.addListBottomSheetState.isVisible).isFalse()
        assertThat(state.addToListBottomSheetState.isVisible).isTrue()
    }

    @Test
    fun `should close add to list bottom sheet when dismiss button is clicked`() {
        // When
        viewModel.onSaveToListBottomSheetDismiss()

        // Then
        val state = viewModel.uiState.value
        assertThat(state.addToListBottomSheetState.isVisible).isFalse()
    }

    @Test
    fun `should selected list id when list is selected`() {
        // Given
        val listId = 123L

        // When
        viewModel.onListSelected(listId)

        // Then
        val state = viewModel.uiState.value
        assertThat(state.addToListBottomSheetState.selectedListId).isEqualTo(listId)
    }

    @Test
    fun `should load movies when clicked category is different from current`() = runTest {
        // Given
        coEvery { getMovieGenresUseCase.getMovieGenres() } returns mockk()

        // When
        viewModel.onCategoryClicked(CATEGORY_ID)

        // Then
        coVerify { getMovieGenresUseCase.getMovieGenres() }
    }

    @Test
    fun `should not load movies when clicked category is same as current`() {
        // Given
        coEvery { getMovieGenresUseCase.getMovieGenres() } returns mockk()
        viewModel.onCategoryClicked(CATEGORY_ID)

        // When
        viewModel.onCategoryClicked(CATEGORY_ID)

        // Then
        coVerify(exactly = 1) { getMovieGenresUseCase.getMovieGenres() }
    }

    @Test
    fun `should call loadGenres when SnackBar action label is clicked`() {
        // Given
        coEvery { getMovieGenresUseCase.getMovieGenres() } returns mockk()
        viewModel.onCategoryClicked(CATEGORY_ID)

        // When
        viewModel.onSnackBarActionLabelClicked(CATEGORY_ID)

        // Then
        coVerify(exactly = 2) { getMovieGenresUseCase.getMovieGenres() }
    }

    @Test
    fun `should change list name when list name is changed`() {
        // When
        viewModel.onCreatedListNameChanged(LIST_NAME)

        // Then
        val state = viewModel.uiState.value
        assertThat(state.addListBottomSheetState.listName).isEqualTo(LIST_NAME)
    }

    @Test
    fun `should create new list when create list button is clicked`() = runTest {
        // Given
        coEvery { createSavedListUseCase(title = LIST_NAME) } returns mockk()
        viewModel.onCreatedListNameChanged(LIST_NAME)


        // When
        viewModel.onCreateListBottomSheetAddClick()

        // Then
        coVerify { createSavedListUseCase(title = LIST_NAME) }

    }

    companion object {
        const val MOVIE_ID = 456L
        const val LIST_NAME = "New List"
        const val CATEGORY_ID = 123L

    }

}