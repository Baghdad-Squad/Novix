import app.cash.turbine.test
import com.baghdad.domain.exception.NetworkException
import com.baghdad.domain.exception.NoInternetException
import com.baghdad.domain.model.pagination.PagedResult
import com.baghdad.domain.model.savedList.SavedMovie
import com.baghdad.domain.usecase.appConfigurations.GetAppLanguageUseCase
import com.baghdad.domain.usecase.continueWatching.ObserveContinueWatchingUseCase
import com.baghdad.domain.usecase.login.IsUserLoggedInUseCase
import com.baghdad.domain.usecase.movie.GetMovieGenresUseCase
import com.baghdad.domain.usecase.movie.GetMovieTopRatingUseCase
import com.baghdad.domain.usecase.movie.GetPopularMoviesUseCase
import com.baghdad.domain.usecase.movie.GetUpcomingMoviesUseCase
import com.baghdad.domain.usecase.savedList.AddMovieToSavedListUseCase
import com.baghdad.domain.usecase.savedList.CreateSavedListUseCase
import com.baghdad.domain.usecase.savedList.GetSavedListsUseCase
import com.baghdad.domain.usecase.savedList.RemoveMovieFromSavedListUseCase
import com.baghdad.domain.usecase.tvShow.GetPopularTvShowsUseCase
import com.baghdad.entity.media.Genre
import com.baghdad.entity.media.Movie
import com.baghdad.entity.savedList.SavedList
import com.baghdad.viewmodel.errorStates.BaseSnackBarMessage
import com.baghdad.viewmodel.home.HomeScreenEffect
import com.baghdad.viewmodel.home.HomeScreenState
import com.baghdad.viewmodel.home.HomeViewModel
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
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
class HomeViewModelTest {

    val getPopularMoviesUseCase: GetPopularMoviesUseCase = mockk(relaxed = true)
    val getPopularTvShowsUseCase: GetPopularTvShowsUseCase = mockk(relaxed = true)
    val getMovieTopRatingUseCase: GetMovieTopRatingUseCase = mockk(relaxed = true)
    val getUpcomingMoviesUseCase: GetUpcomingMoviesUseCase = mockk(relaxed = true)
    val isUserLoggedInUseCase: IsUserLoggedInUseCase = mockk(relaxed = true)
    val getSavedListsUseCase: GetSavedListsUseCase = mockk(relaxed = true)
    val addMovieToSavedListUseCase: AddMovieToSavedListUseCase = mockk()
    val createSavedListUseCase: CreateSavedListUseCase = mockk(relaxed = true)
    val removeMovieFromSavedListUseCase: RemoveMovieFromSavedListUseCase = mockk(relaxed = true)
    val getAppLanguageUseCase: GetAppLanguageUseCase = mockk(relaxed = true)
    val observeContinueWatchingUseCase: ObserveContinueWatchingUseCase = mockk(relaxed = true)
    val getMovieGenresUseCase: GetMovieGenresUseCase = mockk(relaxed = true)
    private lateinit var viewModel: HomeViewModel

    private val testDispatcher = StandardTestDispatcher()


    @BeforeEach
    fun setup() {
        Dispatchers.setMain(testDispatcher)
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun createViewModel(): HomeViewModel {
        return HomeViewModel(
            getPopularMoviesUseCase = getPopularMoviesUseCase,
            getPopularTvShowsUseCase = getPopularTvShowsUseCase,
            getMovieTopRatingUseCase = getMovieTopRatingUseCase,
            getUpcomingMoviesUseCase = getUpcomingMoviesUseCase,
            isUserLoggedInUseCase = isUserLoggedInUseCase,
            getSavedListsUseCase = getSavedListsUseCase,
            addMovieToSavedListUseCase = addMovieToSavedListUseCase,
            createSavedListUseCase = createSavedListUseCase,
            removeMovieFromSavedListUseCase = removeMovieFromSavedListUseCase,
            observeContinueWatchingUseCase = observeContinueWatchingUseCase,
            getAppLanguageUseCase = getAppLanguageUseCase,
            getMovieGenresUseCase = getMovieGenresUseCase,
            defaultDispatcher = testDispatcher
        )
    }


    @Test
    fun `should have empty popular items when fetching with empty popular movies and tv shows`() =
        runTest {
            coEvery { getPopularMoviesUseCase.invoke() } returns emptyList()
            coEvery { getPopularTvShowsUseCase.invoke() } returns emptyList()

            viewModel = createViewModel()
            advanceUntilIdle()

            viewModel.uiState.test {
                val state = awaitItem()
                assertThat(state.popularItems).isEmpty()
            }
        }

    @Test
    fun `should have empty top rating items when fetching with empty top rating movies`() =
        runTest {
            coEvery {
                getMovieTopRatingUseCase.invoke(
                    any(), any()
                ).data
            } returns emptyList<SavedMovie>()

            viewModel = createViewModel()
            advanceUntilIdle()
            viewModel.uiState.test {
                val state = awaitItem()
                assertThat(state.topRatingItems).isEmpty()
            }
        }

    @Test
    fun `should have empty continue watching when no items observed`() = runTest {
        // Given
        coEvery { observeContinueWatchingUseCase.invoke() } returns flowOf(emptyList())

        // When
        viewModel = createViewModel()
        advanceUntilIdle()

        // Then
        viewModel.uiState.test {
            val state = awaitItem()
            assertThat(state.continueWatchingItems).isEmpty()
        }
    }

    @Test
    fun `should set popular loading false when popular items finished loading`() = runTest {
        // Given
        coEvery { getPopularMoviesUseCase.invoke() } returns emptyList()
        coEvery { getPopularTvShowsUseCase.invoke() } returns emptyList()

        viewModel = createViewModel()
        advanceUntilIdle()
        viewModel.uiState.test {
            val state = awaitItem()
            assertThat(state.isPopularLoading).isFalse()
        }
    }

    @Test
    fun `should update upcoming movies when genre is selected`() = runTest {
        coEvery { getUpcomingMoviesUseCase.invoke(any()) } returns savableMoviesSample

        viewModel = createViewModel()
        advanceUntilIdle()
        viewModel.onUpcomingGenreSelected(HomeScreenState.GenreUiState(1, "Drama"))
        advanceUntilIdle()

        viewModel.uiState.test {
            val state = awaitItem()
            assertThat(state.upcomingItems.last().id).isEqualTo(savableMoviesSample.last().movie.id)
        }
    }

    @Test
    fun `should not reload upcoming items when same genre is selected`() = runTest {
        viewModel = createViewModel()
        advanceUntilIdle()
        val initialState = viewModel.uiState.value
        val genreId = initialState.selectedUpcomingGenreId ?: 1L

        viewModel.onUpcomingGenreSelected(HomeScreenState.GenreUiState(genreId, "Drama"))
        advanceUntilIdle()

        coVerify(exactly = 1) { getUpcomingMoviesUseCase.invoke(genreId) }
    }

    @Test
    fun `should emit NavigateToMovieDetails when popular movie item clicked`() = runTest {
        viewModel = createViewModel()
        viewModel.onPopularItemClicked(
            HomeScreenState.PopularItemUiState(
                id = 1, type = HomeScreenState.PopularItemUiState.Type.MOVIE
            )
        )
        viewModel.uiEffect.test {
            val effect = awaitItem()
            assertThat(effect).isEqualTo(HomeScreenEffect.NavigateToMovieDetails(1))
        }

    }

    @Test
    fun `should emit NavigateToTvShowDetails when popular tv show item clicked`() = runTest {
        viewModel = createViewModel()
        viewModel.onPopularItemClicked(
            HomeScreenState.PopularItemUiState(
                id = 2, type = HomeScreenState.PopularItemUiState.Type.TV_SHOW
            )
        )
        viewModel.uiEffect.test {
            val effect = awaitItem()
            assertThat(effect).isEqualTo(HomeScreenEffect.NavigateToTvShowDetails(2))
        }
    }

    @Test
    fun `should emit NavigateToMovies when onMoviesClicked`() = runTest {
        viewModel = createViewModel()
        viewModel.onMoviesClicked()
        viewModel.uiEffect.test {
            val effect = awaitItem()
            assertThat(effect).isEqualTo(HomeScreenEffect.NavigateToMovies)
        }
    }

    @Test
    fun `should emit NavigateToTvShows when onTvShowsClicked`() = runTest {
        viewModel = createViewModel()
        viewModel.onTvShowsClicked()
        viewModel.uiEffect.test {
            val effect = awaitItem()
            assertThat(effect).isEqualTo(HomeScreenEffect.NavigateToTvShows)
        }
    }

    @Test
    fun `should emit NavigateToActors when onActorsClicked`() = runTest {
        viewModel = createViewModel()
        viewModel.onActorsClicked()
        viewModel.uiEffect.test {
            val effect = awaitItem()
            assertThat(effect).isEqualTo(HomeScreenEffect.NavigateToActors)
        }

    }

    @Test
    fun `should emit NavigateToMovieDetails when top rating item clicked`() = runTest {
        viewModel = createViewModel()
        viewModel.onTopRatingItemClicked(HomeScreenState.TopRatingItemUiState(id = 3))
        viewModel.uiEffect.test {
            val effect = awaitItem()
            assertThat(effect).isEqualTo(HomeScreenEffect.NavigateToMovieDetails(3))
        }
    }

    @Test
    fun `should emit NavigateToTopRating when onViewAllTopRatingClicked`() = runTest {
        viewModel = createViewModel()
        viewModel.onViewAllTopRatingClicked()
        viewModel.uiEffect.test {
            val effect = awaitItem()
            assertThat(effect).isEqualTo(HomeScreenEffect.NavigateToTopRating)
        }
    }

    @Test
    fun `should emit NavigateToMovieDetails when continue watching item clicked`() = runTest {
        viewModel = createViewModel()
        viewModel.onContinueWatchingItemClicked(
            HomeScreenState.ContinueWatchingItemUiState(
                id = 5, contentType = HomeScreenState.ContinueWatchingItemUiState.ContentType.MOVIE
            )
        )
        viewModel.uiEffect.test {
            val effect = awaitItem()
            assertThat(effect).isEqualTo(HomeScreenEffect.NavigateToMovieDetails(5))
        }
    }

    @Test
    fun `should emit NavigateToContinueWatching when onViewAllContinueWatchingClicked`() = runTest {
        viewModel = createViewModel()
        viewModel.onViewAllContinueWatchingClicked()
        viewModel.uiEffect.test {
            val effect = awaitItem()
            assertThat(effect).isEqualTo(HomeScreenEffect.NavigateToContinueWatching)
        }
    }

    @Test
    fun `should emit NavigateToMovieDetails when upcoming item clicked`() = runTest {
        viewModel = createViewModel()
        viewModel.onUpcomingItemClicked(HomeScreenState.UpcomingItemUiState(id = 9))

        viewModel.uiEffect.test {
            val effect = awaitItem()
            assertThat(effect).isEqualTo(HomeScreenEffect.NavigateToMovieDetails(9))
        }
    }

    @Test
    fun `should update upcomingGenres when getMovieGenres succeeds`() = runTest {
        coEvery { getMovieGenresUseCase.getMovieGenres() } returns genresSample

        viewModel = createViewModel()
        advanceUntilIdle()

        viewModel.uiState.test {
            val state = awaitItem()
            assertThat(state.upcomingGenres).isEqualTo(genresSample.map {
                HomeScreenState.GenreUiState(
                    it.id, it.name
                )
            })
        }
    }

    @Test
    fun `should set top rating loading false when top rating movies finished loading`() = runTest {
        coEvery {
            getMovieTopRatingUseCase.invoke(
                any(), any()
            ).data
        } returns emptyList<SavedMovie>()

        viewModel = createViewModel()
        advanceUntilIdle()

        viewModel.uiState.test {
            val state = awaitItem()
            assertThat(state.isTopRatingLoading).isFalse()
        }
    }

    @Test
    fun `should set upcoming loading false when upcoming movies finished loading`() = runTest {
        coEvery { getUpcomingMoviesUseCase.invoke(any()) } returns emptyList()

        viewModel = createViewModel()
        advanceUntilIdle()

        viewModel.uiState.test {
            val state = awaitItem()
            assertThat(state.isUpcomingMoviesLoading).isFalse()
        }
    }

    @Test
    fun `should set upcoming genres loading false when movie genres finished loading`() = runTest {
        viewModel = createViewModel()
        advanceUntilIdle()

        viewModel.uiState.test {
            val state = awaitItem()
            assertThat(state.isUpcomingGenresLoading).isFalse()
        }
    }

    @Test
    fun `should update selected item id when onPopularItemSaveClicked called`() = runTest {
        viewModel = createViewModel()
        viewModel.onPopularItemSaveClicked(
            HomeScreenState.PopularItemUiState(
                id = 1, type = HomeScreenState.PopularItemUiState.Type.MOVIE
            )
        )
        advanceUntilIdle()
        viewModel.uiState.test {
            val state = awaitItem()
            assertThat(state.addToListBottomSheetState.selectedItemId).isEqualTo(1)
        }
    }

    @Test
    fun `should update selected item id when onTopRatingItemSaveClicked called`() = runTest {
        viewModel = createViewModel()
        viewModel.onTopRatingItemSaveClicked(HomeScreenState.TopRatingItemUiState(id = 2))
        advanceUntilIdle()
        viewModel.uiState.test {
            val state = awaitItem()
            assertThat(state.addToListBottomSheetState.selectedItemId).isEqualTo(2)
        }
    }

    @Test
    fun `should update selected item id when onContinueWatchingItemSaveClicked called`() = runTest {
        viewModel = createViewModel()
        viewModel.onContinueWatchingItemSaveClicked(
            HomeScreenState.ContinueWatchingItemUiState(
                id = 3, contentType = HomeScreenState.ContinueWatchingItemUiState.ContentType.MOVIE
            )
        )
        advanceUntilIdle()
        viewModel.uiState.test {
            val state = awaitItem()
            assertThat(state.addToListBottomSheetState.selectedItemId).isEqualTo(3)
        }
    }

    @Test
    fun `should update selected item id when onUpcomingItemSaveClicked called`() =
        runTest(testDispatcher) {
        viewModel = createViewModel()
        viewModel.onUpcomingItemSaveClicked(HomeScreenState.UpcomingItemUiState(id = 4))
        advanceUntilIdle()
            viewModel.uiState.test {
                val state = awaitItem()
                assertThat(state.addToListBottomSheetState.selectedItemId).isEqualTo(4)
            }
    }

    @Test
    fun `should return UnknownError using reflection`() {
        val method = HomeViewModel::class.java.getDeclaredMethod(
            "mapThrowableToErrorMessage", Throwable::class.java
        )

        method.isAccessible = true

        val viewModel = createViewModel()

        val result = method.invoke(viewModel, Throwable("test")) as BaseSnackBarMessage
        assertThat(result).isEqualTo(BaseSnackBarMessage.UnknownError)
    }

    @Test
    fun `should navigate to login screen when onLoginClicked`() = runTest {
        viewModel = createViewModel()
        viewModel.onLoginClicked()
        viewModel.uiEffect.test {
            val effect = awaitItem()
            assertThat(effect).isEqualTo(HomeScreenEffect.NavigateToLogin)
        }
    }

    @Test
    fun `should show error snackbar when getPopularMoviesUseCase throw NetworkException`() =
        runTest {
            coEvery { getPopularMoviesUseCase.invoke() } throws NetworkException()
            viewModel = createViewModel()
            advanceUntilIdle()
            viewModel.snackBarState.test {
                val state = awaitItem()
                assertThat(state.isSuccess).isFalse()
            }
        }

    @Test
    fun `should show no internet snackbar when getPopularMoviesUseCase throw NoInternetException`() =
        runTest {
            coEvery { getPopularMoviesUseCase.invoke() } throws NoInternetException()
            viewModel = createViewModel()
            advanceUntilIdle()
            viewModel.snackBarState.test {
                val state = awaitItem()
                assertThat(state.message).isEqualTo(BaseSnackBarMessage.NetworkError)
                assertThat(state.isSuccess).isFalse()
            }
        }

    @Test
    fun `should reload data when onSnackBarActionLabelClicked`() = runTest {
        val viewModel = createViewModel()
        viewModel.onSnackBarActionLabelClicked()
        advanceUntilIdle()
        coVerify(exactly = 2) { getPopularMoviesUseCase.invoke() }
    }

    @Test
    fun `should update selectedItemId when onTopRatingItemSaveClicked`() = runTest {
        viewModel = createViewModel()
        viewModel.onListSelected(1L)
        viewModel.onTopRatingItemSaveClicked(
            HomeScreenState.TopRatingItemUiState(
                id = 5L, savedListId = 1L
            )
        )

        viewModel.uiState.test {
            val state = awaitItem()
            assertThat(state.addToListBottomSheetState.selectedItemId).isEqualTo(5L)
        }
    }

    @Test
    fun `should update selected list id when onListSelected`() = runTest {
        viewModel = createViewModel()
        viewModel.onListSelected(1L)
        viewModel.uiState.test {
            val state = awaitItem()
            assertThat(state.addToListBottomSheetState.selectedListId).isEqualTo(1L)
        }
    }

    @Test
    fun `should show add to list bottom sheet when onTopRatingItemSaveClicked`() = runTest {
        viewModel = createViewModel()
        viewModel.onListSelected(1L)
        viewModel.onTopRatingItemSaveClicked(
            HomeScreenState.TopRatingItemUiState(
                id = 5L, savedListId = 1L
            )
        )

        viewModel.uiState.test {
            val state = awaitItem()
            assertThat(state.addToListBottomSheetState.isVisible).isTrue()
        }
    }

    @Test
    fun `should add item to list when onSaveItemToListClicked`() = runTest {
        viewModel = createViewModel()
        viewModel.onTopRatingItemSaveClicked(
            HomeScreenState.TopRatingItemUiState(
                id = 5L, savedListId = 1L
            )
        )
        viewModel.onListSelected(1L)
        viewModel.onSaveItemToListClicked()
        advanceUntilIdle()
        viewModel.uiState.test {
            val state = awaitItem()
            println(state.addToListBottomSheetState)
            coVerify {
                addMovieToSavedListUseCase(
                    listId = 1L,
                    movieId = 5L
                )
            }
        }
    }

    @Test
    fun `should hide add to list bottom sheet when onSaveItemToListClicked complete successfully`() =
        runTest {
            viewModel = createViewModel()
            coEvery { addMovieToSavedListUseCase(any(), any()) } returns Unit
            viewModel.onSaveItemToListClicked()
            advanceUntilIdle()
            viewModel.uiState.test {
                val state = awaitItem()
                assertThat(state.addToListBottomSheetState.isVisible).isFalse()
            }
        }

    @Test
    fun `should hide add to list bottom sheet when onSaveToListBottomSheetDismiss is called`() =
        runTest {
            viewModel = createViewModel()
            viewModel.onSaveToListBottomSheetDismiss()
            viewModel.uiState.test {
                val state = awaitItem()
                assertThat(state.addToListBottomSheetState.isVisible).isFalse()
            }
        }

    @Test
    fun `should hide add to list bottom sheet when onCreateNewListClicked`() = runTest {
        val viewModel = createViewModel()
        viewModel.onCreateNewListClicked()
        viewModel.uiState.test {
            val state = awaitItem()
            assertThat(state.addToListBottomSheetState.isVisible).isFalse()
        }
    }

    @Test
    fun `should show add list bottom sheet when onCreateNewListClicked`() = runTest {
        val viewModel = createViewModel()
        viewModel.onCreateNewListClicked()
        viewModel.uiState.test {
            val state = awaitItem()
            assertThat(state.addListBottomSheetState.isVisible).isTrue()
        }
    }

    @Test
    fun `should update list name successfully when onCreatedListNameChanged called`() = runTest {
        val viewModel = createViewModel()
        viewModel.onCreatedListNameChanged("Test")
        viewModel.uiState.test {
            val state = awaitItem()
            assertThat(state.addListBottomSheetState.listName).isEqualTo("Test")
        }
    }

    @Test
    fun `should hide add list bottom sheet when onSaveToListBottomSheetDismiss is called`() =
        runTest {
            val viewModel = createViewModel()
            viewModel.onTopRatingItemSaveClicked(
                HomeScreenState.TopRatingItemUiState(
                    id = 5L, savedListId = 1L
                )
            )
            viewModel.onSaveToListBottomSheetDismiss()
            viewModel.uiState.test {
                val state = awaitItem()
                assertThat(state.addListBottomSheetState.isVisible).isFalse()
            }
        }

    @Test
    fun `should remove item when click on item save icon and the item is saved`() = runTest {
        val viewModel = createViewModel()
        viewModel.onTopRatingItemSaveClicked(
            HomeScreenState.TopRatingItemUiState(
                id = 5L, savedListId = 1L, isSaved = true
            )
        )
        advanceUntilIdle()
        coVerify {
            removeMovieFromSavedListUseCase(listId = 1L, movieId = 5L)
        }
    }

    @Test
    fun `should close the create list bottom sheet when onCreateListBottomSheetDismiss is called`() =
        runTest {
            val viewModel = createViewModel()
            viewModel.onCreateNewListClicked()
            viewModel.onCreateListBottomSheetDismiss()
            viewModel.uiState.test {
                val state = awaitItem()
                assertThat(state.addListBottomSheetState.isVisible).isFalse()
            }
        }

    @Test
    fun `should add list when onCreateListBottomSheetAddClick`() = runTest {
        val viewModel = createViewModel()
        viewModel.onCreatedListNameChanged("omer")
        viewModel.onCreateListBottomSheetAddClick()
        advanceUntilIdle()
        coVerify {
            createSavedListUseCase("omer")
        }
    }

    @Test
    fun `should hide the create list bottom sheet when onCreateListBottomSheetDismiss is called`() =
        runTest {
            val viewModel = createViewModel()
            coEvery { createSavedListUseCase("omer") } returns Unit
            viewModel.onCreateNewListClicked()
            viewModel.onCreatedListNameChanged("omer")
            viewModel.onCreateListBottomSheetAddClick()
            advanceUntilIdle()
            viewModel.uiState.test {
                val state = awaitItem()
                assertThat(state.addListBottomSheetState.isVisible).isFalse()
            }
        }

    //    @Test
//    fun `should refresh current lists when new list is created `()= runTest{
//        val viewModel = createViewModel()
//        coEvery { getSavedListsUseCase(1, 20) } returns listSample
//        viewModel.onCreatedListNameChanged("omer")
//        viewModel.onCreateListBottomSheetAddClick()
//        advanceUntilIdle()
//        coVerify (exactly = 1) {
//            getSavedListsUseCase(1, 20)
//        }
//    }
    private companion object {
        val listSample = PagedResult<SavedList>(
            data = listOf(
                SavedList(
                    id = 1,
                    name = "omer",
                    itemCount = 5
                )
            ),
            nextKey = null,
            prevKey = null
        )
        val genresSample = listOf(
            Genre(1, "Drama"),
            Genre(2, "Comedy"),
        )
        val savableMoviesSample = listOf(
            SavedMovie(
                movie = Movie(
                    id = 10,
                    title = "Test",
                    genres = listOf(Genre(1, "Drama")),
                    averageRating = 1.4,
                    userRating = 1.5,
                    releaseDate = LocalDate.parse("2023-01-01"),
                    overview = "Test",
                    posterImageURL = "Test",
                    trailerURL = "Test",
                    runtimeMinutes = 5
                ), isSaved = false, listId = null
            )
        )
    }
}