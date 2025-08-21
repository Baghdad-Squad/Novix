import app.cash.turbine.test
import com.baghdad.domain.usecase.login.IsUserLoggedInUseCase
import com.baghdad.domain.usecase.movie.GetMovieGenresUseCase
import com.baghdad.domain.usecase.movie.GetTrendingMoviesUseCase
import com.baghdad.domain.usecase.savedList.AddMovieToSavedListUseCase
import com.baghdad.domain.usecase.savedList.CreateSavedListUseCase
import com.baghdad.domain.usecase.savedList.GetSavedListsUseCase
import com.baghdad.domain.usecase.savedList.RemoveMovieFromSavedListUseCase
import com.baghdad.viewmodel.trendingMovie.TrendingMoviesEffect
import com.baghdad.viewmodel.trendingMovie.TrendingMoviesScreenState
import com.baghdad.viewmodel.trendingMovie.TrendingMoviesViewModel
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test


@ExperimentalCoroutinesApi
class TrendingMoviesViewModelTest {

    private val getTrendingMoviesUseCase = mockk<GetTrendingMoviesUseCase>()
    private val getMovieGenresUseCase = mockk<GetMovieGenresUseCase>()
    private val isUserLoggedInUseCase = mockk<IsUserLoggedInUseCase>()
    private val getSavedListsUseCase = mockk<GetSavedListsUseCase>()
    private val addMovieToSavedListUseCase = mockk<AddMovieToSavedListUseCase>()
    private val createSavedListUseCase = mockk<CreateSavedListUseCase>()
    private val removeMovieFromSavedListUseCase = mockk<RemoveMovieFromSavedListUseCase>()

    private val testDispatcher = StandardTestDispatcher()
    lateinit var viewModel: TrendingMoviesViewModel

    @BeforeEach
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }


    @Test
    fun `onBackClicked should emit NavigateBack effect`() = runTest {
        viewModel = createViewModel()

        viewModel.onBackClicked()

        viewModel.uiEffect.test {
            assertThat(awaitItem()).isEqualTo(TrendingMoviesEffect.NavigateBack)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `onMovieClicked should emit NavigateToMovieDetails effect with correct movieId`() =
        runTest {
            viewModel = createViewModel()
            advanceUntilIdle()
            viewModel.onMovieClicked(MOVIE_ID)

            viewModel.uiEffect.test {
                assertThat(awaitItem())
                    .isEqualTo(TrendingMoviesEffect.NavigateToMovieDetails(MOVIE_ID))
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `onLoginClicked should emit NavigateToLogin effect`() = runTest {
        viewModel = createViewModel()

        viewModel.onLoginClicked()

        viewModel.uiEffect.test {
            assertThat(awaitItem()).isEqualTo(TrendingMoviesEffect.NavigateToLogin)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `onCreateNewListClicked should show addListBottomSheet and hide addToListBottomSheet`() {
        viewModel = createViewModel()

        viewModel.onCreateNewListClicked()

        val state = viewModel.uiState.value
        assertThat(state.addListBottomSheetState.isVisible).isTrue()
        assertThat(state.addToListBottomSheetState.isVisible).isFalse()
    }

    @Test
    fun `onCreateListBottomSheetDismiss should hide addListBottomSheet and show addToListBottomSheet`() {
        viewModel = createViewModel()

        viewModel.onCreateListBottomSheetDismiss()

        val state = viewModel.uiState.value
        assertThat(state.addListBottomSheetState.isVisible).isFalse()
        assertThat(state.addToListBottomSheetState.isVisible).isTrue()
        assertThat(state.addListBottomSheetState.listName).isEmpty()
    }

    @Test
    fun `onSaveToListBottomSheetDismiss should hide addToListBottomSheet`() {
        viewModel = createViewModel()

        viewModel.onSaveToListBottomSheetDismiss()

        val state = viewModel.uiState.value
        assertThat(state.addToListBottomSheetState.isVisible).isFalse()
    }

    @Test
    fun `onListSelected should update selectedListId in uiState when a list is selected`() {
        viewModel = createViewModel()

        viewModel.onListSelected(LIST_ID)

        val state = viewModel.uiState.value
        assertThat(state.addToListBottomSheetState.selectedListId).isEqualTo(LIST_ID)
    }

    @Test
    fun `onCreatedListNameChanged should update listName in state`() {
        viewModel = createViewModel()

        viewModel.onCreatedListNameChanged(LIST_NAME)

        val state = viewModel.uiState.value
        assertThat(state.addListBottomSheetState.listName).isEqualTo(LIST_NAME)
    }

    @Test
    fun `onCategoryClicked with different categoryId should update selectedGenreId`() = runTest {
        viewModel = createViewModel()

        viewModel.onCategoryClicked(CATEGORY_ID)

        assertThat(viewModel.uiState.value.selectedGenreId).isEqualTo(CATEGORY_ID)
    }

    @Test
    fun `onSaveMovieClick should show addToListBottomSheet when movie is not saved`() {
        viewModel = createViewModel()

        viewModel.onSaveMovieClick(MOVIE)

        val state = viewModel.uiState.value
        assertThat(state.addToListBottomSheetState.isVisible).isTrue()
    }

    @Test
    fun `onSaveMovieClick should save exactly movie when movie is not saved`() {
        viewModel = createViewModel()

        viewModel.onSaveMovieClick(MOVIE)

        val state = viewModel.uiState.value
        assertThat(state.addToListBottomSheetState.selectedItemId).isEqualTo(MOVIE_ID)
    }

    @Test
    fun `onSaveMovieClick with unsaved movie should update addToListBottomSheetState`() = runTest {
        viewModel = createViewModel()

        viewModel.onSaveMovieClick(MOVIE)

        val state = viewModel.uiState.value.addToListBottomSheetState
        assertThat(state.isVisible).isTrue()
        assertThat(state.selectedItemId).isEqualTo(MOVIE_ID)
    }

    @Test
    fun `onCreateListBottomSheetAddClick should dismiss add list bottom sheet and refresh saved lists on success`() =
        runTest {
            coEvery { createSavedListUseCase(any()) } returns Unit
            viewModel = createViewModel()
            viewModel.onCreatedListNameChanged(LIST_NAME)

            viewModel.onCreateListBottomSheetAddClick()

            val addListState = viewModel.uiState.value.addListBottomSheetState
            assertThat(addListState.isVisible).isFalse()
        }

    companion object {
        private const val MOVIE_ID = 456L
        private const val LIST_ID = 789L
        private const val LIST_NAME = "New List"
        private const val CATEGORY_ID = 123L

        val MOVIE = TrendingMoviesScreenState.TrendingMovieUiState(
            id = MOVIE_ID,
            isSaved = false,
            savedListId = LIST_ID
        )
    }

    private fun createViewModel(): TrendingMoviesViewModel {
        return TrendingMoviesViewModel(
            getTrendingMoviesUseCase = getTrendingMoviesUseCase,
            getMovieGenresUseCase = getMovieGenresUseCase,
            isUserLoggedInUseCase = isUserLoggedInUseCase,
            getSavedListsUseCase = getSavedListsUseCase,
            addMovieToSavedListUseCase = addMovieToSavedListUseCase,
            createSavedListUseCase = createSavedListUseCase,
            removeMovieFromSavedListUseCase = removeMovieFromSavedListUseCase,
            defaultDispatcher = testDispatcher
        )
    }
}