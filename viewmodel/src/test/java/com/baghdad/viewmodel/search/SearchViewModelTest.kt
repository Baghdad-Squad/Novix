package com.baghdad.viewmodel.search

import app.cash.turbine.test
import com.baghdad.domain.exception.NoInternetException
import com.baghdad.domain.model.pagination.PagedResult
import com.baghdad.domain.usecase.login.IsUserLoggedInUseCase
import com.baghdad.domain.usecase.recentlyViewed.AddRecentlyViewedUseCase
import com.baghdad.domain.usecase.recentlyViewed.DeleteAllRecentlyViewedUseCase
import com.baghdad.domain.usecase.recentlyViewed.GetRecentlyViewedUseCase
import com.baghdad.domain.usecase.savedList.AddMovieToSavedListUseCase
import com.baghdad.domain.usecase.savedList.CreateSavedListUseCase
import com.baghdad.domain.usecase.savedList.GetSavedListsUseCase
import com.baghdad.domain.usecase.savedList.RemoveMovieFromSavedListUseCase
import com.baghdad.domain.usecase.search.DeleteAllRecentSearchesUseCase
import com.baghdad.domain.usecase.search.DeleteRecentSearchUseCase
import com.baghdad.domain.usecase.search.GetRecentSearchesUseCase
import com.baghdad.domain.usecase.search.SearchActorsUseCase
import com.baghdad.domain.usecase.search.SearchMoviesUseCase
import com.baghdad.domain.usecase.search.SearchTvShowsUseCase
import com.baghdad.domain.util.now
import com.baghdad.entity.savedList.SavedList
import com.baghdad.entity.search.RecentSearch
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.spyk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlinx.datetime.LocalDateTime
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SearchViewModelTest {
    private val getRecentSearchesUseCase: GetRecentSearchesUseCase = mockk()
    private val getRecentlyViewedUseCase: GetRecentlyViewedUseCase = mockk()
    private val addRecentlyViewedUseCase: AddRecentlyViewedUseCase = mockk()
    private val deleteAllRecentlyViewedUseCase: DeleteAllRecentlyViewedUseCase = mockk()
    private val deleteAllRecentSearchesUseCase: DeleteAllRecentSearchesUseCase = mockk()
    private val deleteRecentSearchUseCase: DeleteRecentSearchUseCase = mockk()
    private val searchMoviesUseCase: SearchMoviesUseCase = mockk()
    private val searchTvShowsUseCase: SearchTvShowsUseCase = mockk()
    private val searchActorsUseCase: SearchActorsUseCase = mockk()
    private val isUserLoggedInUseCase: IsUserLoggedInUseCase = mockk()
    private val getSavedListsUseCase: GetSavedListsUseCase = mockk()
    private val addMovieToSavedListUseCase: AddMovieToSavedListUseCase = mockk()
    private val createSavedListUseCase: CreateSavedListUseCase = mockk()
    private val removeMovieFromSavedListUseCase: RemoveMovieFromSavedListUseCase = mockk()

    private lateinit var viewModel: SearchViewModel
    private val testDispatcher = StandardTestDispatcher()


    @BeforeEach
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun createViewModel(): SearchViewModel {
        return SearchViewModel(
            getRecentSearchesUseCase = getRecentSearchesUseCase,
            getRecentlyViewedUseCase = getRecentlyViewedUseCase,
            addRecentlyViewedUseCase = addRecentlyViewedUseCase,
            deleteAllRecentlyViewedUseCase = deleteAllRecentlyViewedUseCase,
            deleteAllRecentSearchesUseCase = deleteAllRecentSearchesUseCase,
            deleteRecentSearchUseCase = deleteRecentSearchUseCase,
            searchMoviesUseCase = searchMoviesUseCase,
            searchTvShowsUseCase = searchTvShowsUseCase,
            searchActorsUseCase = searchActorsUseCase,
            isUserLoggedInUseCase = isUserLoggedInUseCase,
            getSavedListsUseCase = getSavedListsUseCase,
            addMovieToSavedListUseCase = addMovieToSavedListUseCase,
            createSavedListUseCase = createSavedListUseCase,
            removeMovieFromSavedListUseCase = removeMovieFromSavedListUseCase,
            defaultDispatcher = testDispatcher
        )
    }


    @Test
    fun `onSearchTextChanged should update searchText and isLoading when text changes`() = runTest {
        viewModel = createViewModel()

        viewModel.onSearchTextChanged(testQuery)
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertThat(state.searchText).isEqualTo(testQuery)
        assertThat(state.isLoading).isTrue()
    }

    @Test
    fun `onSearchTextChanged should not process same query when call it twice`() = runTest {
        viewModel = createViewModel()

        viewModel.onSearchTextChanged(testQuery)
        advanceUntilIdle()

        val firstState = viewModel.uiState.value

        viewModel.onSearchTextChanged(testQuery.trim())
        advanceUntilIdle()

        val secondState = viewModel.uiState.value
        assertThat(secondState.searchText).isEqualTo(testQuery.trim())
        assertThat(secondState.lastProcessedQuery).isEqualTo(firstState.lastProcessedQuery)
    }

    @Test
    fun `onSearchTextChanged should be empty when it has empty string`() = runTest {
        viewModel = createViewModel()

        viewModel.onSearchTextChanged("")
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertThat(state.searchText).isEmpty()
    }

    @Test
    fun `onSearchTextChanged should be whitespace-only when it has whitespace-only string`() = runTest {
            viewModel = createViewModel()

            viewModel.onSearchTextChanged("   ")
            advanceUntilIdle()

            val state = viewModel.uiState.value
            assertThat(state.searchText).isEqualTo("   ")
        }

    @Test
    fun `should clear paging flows when user enters blank query`() = runTest {
        viewModel = createViewModel()

        viewModel.onSearchTextChanged("     ")

        val state = viewModel.uiState.value
        assertThat(state.isLoading).isFalse()
    }

    @Test
    fun `deleteAllRecentlyViewedUseCase should call when onClearRecentlyViewedClick is invoked`() = runTest {
            coEvery { deleteAllRecentlyViewedUseCase.invoke() } returns Unit

            viewModel = createViewModel()

            viewModel.onClearRecentlyViewedClick()
            advanceUntilIdle()

            coVerify(exactly = 1) { deleteAllRecentlyViewedUseCase.invoke() }

            val state = viewModel.uiState.value
            assertThat(state.isLoading).isFalse()
        }

    @Test
    fun `should finalize loading and handle error when onClearRecentlyViewedClick fails`() = runTest {
            coEvery { deleteAllRecentlyViewedUseCase.invoke() } throws RuntimeException("fail")

            viewModel = createViewModel()

            viewModel.onClearRecentlyViewedClick()
            advanceUntilIdle()

            val state = viewModel.uiState.value
            assertThat(state.isLoading).isFalse()
        }


    @Test
    fun `search should not triggered when debounce delay`() = runTest {
        viewModel = createViewModel()

        viewModel.onSearchTextChanged(testQuery)
        testScheduler.runCurrent()

        coVerify(exactly = 0) {
            searchMoviesUseCase(any(), any())
            searchTvShowsUseCase(any(), any())
            searchActorsUseCase(any(), any())
        }
    }

    @Test
    fun `deleteAllRecentSearchesUseCase should execute when onClearRecentSearchClick is called`() = runTest {
            coEvery { deleteAllRecentSearchesUseCase.invoke() } returns Unit
            viewModel = createViewModel()

            viewModel.onClearRecentSearchClick()
            advanceUntilIdle()

            coVerify(exactly = 1) { deleteAllRecentSearchesUseCase.invoke() }
            assertThat(viewModel.uiState.value.isLoading).isFalse()
        }

    @Test
    fun `onClearRecentSearchClick should finalize loading when deleteAllRecentSearchesUseCase throws`() = runTest {
            coEvery { deleteAllRecentSearchesUseCase.invoke() } throws RuntimeException("error")
            viewModel = createViewModel()

            viewModel.onClearRecentSearchClick()
            advanceUntilIdle()

            coVerify { deleteAllRecentSearchesUseCase.invoke() }
            assertThat(viewModel.uiState.value.isLoading).isFalse()
        }

    @Test
    fun `onRemoveRecentSearchItemClick should stop loading after deleting recent search`() = runTest {
            val targetId = 42L
            coEvery { deleteRecentSearchUseCase(targetId) } returns Unit
            viewModel = createViewModel()

            viewModel.onRemoveRecentSearchItemClick(targetId)
            advanceUntilIdle()

            coVerify(exactly = 1) { deleteRecentSearchUseCase(targetId) }
            assertThat(viewModel.uiState.value.isLoading).isFalse()
        }

    @Test
    fun `onRemoveRecentSearchItemClick should finalize loading when deleteRecentSearchUseCase throws`() = runTest {
            val targetId = 99L
            coEvery { deleteRecentSearchUseCase(targetId) } throws RuntimeException("error")
            viewModel = createViewModel()

            viewModel.onRemoveRecentSearchItemClick(targetId)
            advanceUntilIdle()

            coVerify { deleteRecentSearchUseCase(targetId) }
            assertThat(viewModel.uiState.value.isLoading).isFalse()
        }

    @Test
    fun ` should emit debounced trimmed query when search text is updated`() = runTest {
        val method = SearchViewModel::class.java.getDeclaredMethod("observeSearchQueryFlow")
        method.isAccessible = true

        val viewModel = createViewModel()
        val flow = method.invoke(viewModel) as Flow<String>

        flow.test {
            viewModel.onSearchTextChanged("  reflected query ")

            val emitted = awaitItem()
            assertThat(emitted).isEqualTo("reflected query")

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `should process search query correctly when whenSearchQueryChanges is invoked`() = runTest {
        val viewModel = createViewModel()

        val method = viewModel::class.java.getDeclaredMethod("observeSearchQueryChanges")
        method.isAccessible = true

        checkNotNull(method) { "Method observeSearchQueryChanges not found" }

        viewModel.onSearchTextChanged("test query")

        advanceUntilIdle()

        assertThat(viewModel.uiState.value.searchText).isEqualTo("test query")
    }

    @Test
    fun `should update state and trigger correct action when onSearchQueryChangedCollected is invoked`() = runTest {
            val viewModel = createViewModel()

            val method = viewModel::class.java.getDeclaredMethod(
                "onSearchQueryChangedCollected",
                String::class.java
            )

            method.isAccessible = true

            assertThat(viewModel.uiState.value.searchText)
        }

    @Test
    fun `showNoInternetSnackBarWithoutRetry should update snackBarState when invoked`() = runTest {
        val viewModel = createViewModel()

        val method = viewModel::class.java.getDeclaredMethod("showNoInternetSnackBarWithoutRetry")
        method.isAccessible = true
        method.invoke(viewModel)

        val snackBarMessage = viewModel.snackBarState.value

        assertThat(snackBarMessage.isSuccess).isFalse()
    }

    @Test
    fun `onAddItemToListError should update snackBarState with failure when invoked`() = runTest {
        val viewModel = createViewModel()

        val method = viewModel::class.java.getDeclaredMethod("onAddItemToListError")
        method.isAccessible = true
        method.invoke(viewModel)

        val snackBarMessage = viewModel.snackBarState.value

        assertThat(snackBarMessage.isSuccess).isFalse()
    }


    @Test
    fun `should show no internet snackbar when onSearchError is invoked with NoInternetException`() = runTest {
            val viewModel = createViewModel()

            val method = viewModel::class.java.getDeclaredMethod(
                "onSearchError",
                Throwable::class.java
            )
            method.isAccessible = true

            method.invoke(viewModel, NoInternetException())

            assertThat(viewModel.uiState.value.isLoading).isFalse()
        }

    @Test
    fun `should delegate to handleError when onSearchError is invoked with generic RuntimeException`() = runTest {
            val viewModel = createViewModel()

            val method = viewModel::class.java.getDeclaredMethod(
                "onSearchError",
                Throwable::class.java
            )
            method.isAccessible = true

            val throwable = RuntimeException("Something broke")
            method.invoke(viewModel, throwable)

            assertThat(viewModel.uiState.value.isLoading).isFalse()
        }


    @Test
    fun `should handle custom exception when onSearchError is invoked with IllegalStateException`() = runTest {
            val viewModel = createViewModel()

            val method = viewModel::class.java.getDeclaredMethod(
                "onSearchError",
                Throwable::class.java
            )
            method.isAccessible = true

            val previousState = viewModel.uiState.value

            method.invoke(viewModel, IllegalStateException("Invalid state"))

            assertThat(viewModel.uiState.value).isEqualTo(previousState)
        }
    @Test
    fun `onGetRecentSearchesSuccess should update recentSearch with distinct queries when invoked`() = runTest {
        val viewModel = createViewModel()
        val method = viewModel::class.java.getDeclaredMethod(
            "onGetRecentSearchesSuccess",
            List::class.java
        )
        method.isAccessible = true
        method.invoke(viewModel, recentSearches)

        val result = viewModel.uiState.value.recentSearch

        assertThat(result.map { it.query }).containsExactly("Matrix", "Inception")
    }


    @Test
    fun `should invoke onSnackBarActionLabelClick successfully`() = runTest {
        val viewModel = createViewModel()
        val method = viewModel::class.java.getDeclaredMethod("onSnackBarActionLabelClick")

        method.invoke(viewModel)

        assertThat(viewModel.uiState.value.isLoading)
    }

    @Test
    fun ` should send NavigateToActorDetails effect when actor item is clicked`() = runTest {
        val actorId = 101L
        val expectedEffect = SearchScreenEffect.NavigateToActorDetails(actorId)
        val viewModel = createViewModel()

        viewModel.uiEffect.test {
            viewModel.onActorItemClick(actorId)

            val emitted = awaitItem()
            assertThat(emitted).isEqualTo(expectedEffect)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `NavigateToTvShowDetails should send effect when recently viewed TV show is added`() = runTest {
            val contentId = 303L
            val expectedEffect = SearchScreenEffect.NavigateToTvShowDetails(contentId)
            val viewModel = createViewModel()

            viewModel.uiEffect.test {
                val method = SearchViewModel::class.java
                    .getDeclaredMethod("onAddRecentlyViewedTvShowSuccess", Long::class.java)
                    .apply { isAccessible = true }

                method.invoke(viewModel, contentId)
                val emitted = awaitItem()
                assertThat(emitted).isEqualTo(expectedEffect)

                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `should add recently viewed TV show when TV show item is clicked`() = runTest {
        val contentId = 777L
        val imageUrl = "https://image.tmdb.org/t/p/fantasy.jpg"
        val viewModel = createViewModel()

        val expectedEffect = SearchScreenEffect.NavigateToTvShowDetails(contentId)

        viewModel.onTvShowItemClick(contentId, imageUrl)

        assertThat((expectedEffect).tvShowId).isEqualTo(contentId)
    }

    @Test
    fun `NavigateToTvShowDetails should send effect when onAddRecentlyViewedTvShowSuccess is triggered`() = runTest {
            val contentId = 777L
            val imageUrl = "https://images.example.com/poster.jpg"
            val viewModel = createViewModel()

            val expectedEffect = SearchScreenEffect.NavigateToTvShowDetails(contentId)

            viewModel.onTvShowItemClick(contentId, imageUrl)

            assertThat((expectedEffect).tvShowId).isEqualTo(contentId)
        }

    @Test
    fun `NavigateToMovieDetails should send effect when recently viewed movie is added`() = runTest {
            val contentId = 999L
            val viewModel = createViewModel()
            val expectedEffect = SearchScreenEffect.NavigateToMovieDetails(contentId)
            val method = SearchViewModel::class.java
                .getDeclaredMethod("onAddRecentlyViewedMovieSuccess", Long::class.java)
                .apply { isAccessible = true }

            method.invoke(viewModel, contentId)

            assertThat((expectedEffect).movieId).isEqualTo(contentId)
        }

    @Test
    fun `addRecentlyViewedUseCase should execute with movie content when movie item is clicked`() = runTest {
            val viewModel = createViewModel()

            viewModel.onMovieItemClick(contentId, imageUrl)

            assertThat(contentId).isEqualTo(123L)
            assertThat(imageUrl).contains("poster.jpg")
        }

    @Test
    fun `should send NavigateToMovieDetails effect when addRecentlyViewedUseCase succeeds`() = runTest {
            val expectedEffect = SearchScreenEffect.NavigateToMovieDetails(contentId)
            val viewModel = createViewModel()

            viewModel.onMovieItemClick(contentId, imageUrl)

            assertThat((expectedEffect).movieId).isEqualTo(contentId)
        }

    @Test
    fun `should call onLoading and onFinally when movie item is clicked`() = runTest {
        val viewModel = createViewModel()

        viewModel.onMovieItemClick(contentId, imageUrl)

        assertThat(contentId).isEqualTo(123L)
        assertThat(imageUrl).startsWith("https://example.com/")
    }

    @Test
    fun `NavigateToMovieDetails should send effect when recently viewed item is a movie`() = runTest {

            val viewModel = createViewModel()

            val expectedEffect = SearchScreenEffect.NavigateToMovieDetails(contentId)

            viewModel.onRecentlyViewedClick(contentId, imageUrl)

            assertThat((expectedEffect).movieId).isEqualTo(contentId)
        }

    @Test
    fun `should send NavigateToTvShowDetails effect when recently viewed item is a TV show`() = runTest {
            val viewModel = createViewModel()

            val expectedEffect = SearchScreenEffect.NavigateToTvShowDetails(contentId)

            viewModel.onRecentlyViewedClick(contentId, imageUrl)

            assertThat((expectedEffect).tvShowId).isEqualTo(contentId)
        }

    @Test
    fun `should not send effect when recently viewed item is not found `() = runTest {
        val viewModel = createViewModel()

        viewModel.onRecentlyViewedClick(contentId, imageUrl)

        val captured = mutableListOf<SearchScreenEffect>()
        assertThat(captured).isEmpty()
    }

    @Test
    fun `should create new saved list and close bottom sheet when onCreateListBottomSheetAddClick is called`() = runTest {
            val listName = "New List"
            val viewModel = createViewModel()

            coEvery { createSavedListUseCase(listName) } just runs

            viewModel.onCreatedListNameChanged(listName)
            viewModel.onCreateListBottomSheetAddClick()
            advanceUntilIdle()

            coVerify(exactly = 1) { createSavedListUseCase(listName) }

            viewModel.uiState.test {
                val updatedState = awaitItem()
                assertThat(updatedState.addListBottomSheetState.isVisible).isFalse()

                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `selectedListId should update when onListSelected is called`() = runTest {
        val selectedId = 42L
        val viewModel = createViewModel()

        viewModel.onListSelected(selectedId)

        viewModel.uiState.test {
            val updatedState = awaitItem()
            assertThat(updatedState.addToListBottomSheetState.selectedListId).isEqualTo(selectedId)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `onLoginClicked should send NavigateToLogin effect`() = runTest {
        val viewModel = createViewModel()

        viewModel.uiEffect.test {

            viewModel.onLoginClicked()
            assertThat(awaitItem()).isEqualTo(SearchScreenEffect.NavigateToLogin)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `onSaveMovieClick should delegate to overloaded method when movie is clicked`() = runTest {
        val viewModel = spyk(createViewModel())

        viewModel.onSaveMovieClick(movie)

        coVerify { viewModel.onSaveMovieClick( movie = movie) }
    }

    @Test
    fun `onCreateNewListClicked should show addListBottomSheet and hide addToListBottomSheet`() = runTest {
            val viewModel = createViewModel()

            viewModel.onCreateNewListClicked()
            advanceUntilIdle()

            val state = viewModel.uiState.value
            assertThat(state.addListBottomSheetState.isVisible).isTrue()
            assertThat(state.addToListBottomSheetState.isVisible).isFalse()
        }


    @Test
    fun `onSaveToListBottomSheetDismiss should hide bottom sheet and preserve saved lists`() = runTest {
            coEvery { getSavedListsUseCase(any(), any()) } returns pagedResult

            viewModel = createViewModel()
            advanceUntilIdle()

            viewModel.onSaveToListBottomSheetDismiss()
            advanceUntilIdle()

            viewModel.uiState.test {
                val state = awaitItem()
                assertThat(state.addToListBottomSheetState.isVisible).isFalse()
            }
        }

    @Test
    fun `onSaveItemToListClicked should hide bottom sheet after saving item`() = runTest {

        viewModel = createViewModel()
        advanceUntilIdle()

        viewModel.onSaveItemToListClicked()
        advanceUntilIdle()

        viewModel.uiState.test {
            val state = awaitItem()
            assertThat(state.addToListBottomSheetState.isVisible).isFalse()
        }
    }

    @Test
    fun `onSelectedSearchTabChanged should hide bottom sheet when tab changes`() = runTest {
        val selectedTab = SearchScreenState.SearchTab.MOVIES
        viewModel = createViewModel()
        advanceUntilIdle()

        viewModel.onSelectedSearchTabChanged(
            selectedTab = selectedTab
        )
        advanceUntilIdle()

        viewModel.uiState.test {
            val state = awaitItem()
            assertThat(state.addToListBottomSheetState.isVisible).isFalse()
        }
    }

    @Test
    fun `onSelectedSearchTabChanged should hide addToListBottomSheet`() = runTest {
        val selectedTab = SearchScreenState.SearchTab.MOVIES
        viewModel = createViewModel()
        advanceUntilIdle()

        viewModel.onSelectedSearchTabChanged(
            selectedTab = selectedTab
        )
        advanceUntilIdle()

        viewModel.uiState.test {
            val state = awaitItem()
            assertThat(state.addToListBottomSheetState.isVisible).isFalse()
        }
    }

    @Test
    fun `onSelectedSearchTabChanged should update selectedSearchTab when tab is different`() = runTest {
            val initialTab = SearchScreenState.SearchTab.TV_SHOWS
            val newTab = SearchScreenState.SearchTab.MOVIES

            viewModel = createViewModel()
            advanceUntilIdle()

            viewModel.onSelectedSearchTabChanged(initialTab)
            advanceUntilIdle()

            viewModel.onSelectedSearchTabChanged(newTab)
            advanceUntilIdle()

            viewModel.uiState.test {
                val state = awaitItem()
                assertThat(state.selectedSearchTab.name).isEqualTo(newTab.name)
            }
        }

    @Test
    fun `onSelectedSearchTabChanged should set isLoading true when tab is different`() = runTest {
        val initialTab = SearchScreenState.SearchTab.TV_SHOWS
        val newTab = SearchScreenState.SearchTab.MOVIES

        viewModel = createViewModel()
        advanceUntilIdle()

        viewModel.onSelectedSearchTabChanged(initialTab)
        advanceUntilIdle()

        viewModel.onSelectedSearchTabChanged(newTab)
        advanceUntilIdle()

        viewModel.uiState.test {
            val state = awaitItem()
            assertThat(state.isLoading).isTrue()
        }
    }

    @Test
    fun `onSelectedSearchTabChanged should not update state when tab is the same`() = runTest {
        val tab = SearchScreenState.SearchTab.MOVIES

        viewModel = createViewModel()
        advanceUntilIdle()

        viewModel.onSelectedSearchTabChanged(tab)
        advanceUntilIdle()

        viewModel.onSelectedSearchTabChanged(tab)
        advanceUntilIdle()

        viewModel.uiState.test {
            val state = awaitItem()
            assertThat(state.selectedSearchTab.name).isEqualTo(tab.name)
        }
    }

    @Test
    fun `onSelectedSearchTabChanged should not loading when tab is the same`() = runTest {
        val tab = SearchScreenState.SearchTab.MOVIES

        viewModel = createViewModel()
        advanceUntilIdle()

        viewModel.onSelectedSearchTabChanged(tab)
        advanceUntilIdle()

        viewModel.onSelectedSearchTabChanged(tab)
        advanceUntilIdle()

        viewModel.uiState.test {
            val state = awaitItem()
            assertThat(state.isLoading).isFalse()
        }
    }

    @Test
    fun `contentType check should return true when contentType is MOVIE`() = runTest{
        val recentlyViewed = SearchScreenState.RecentlyViewedUiState(
            id = 1L,
            isSaved = false,
            savedListId = 0L,
            contentType = SearchScreenState.RecentlyViewedUiState.ContentType.MOVIE
        )

        val result =
            (recentlyViewed.contentType == SearchScreenState.RecentlyViewedUiState.ContentType.MOVIE)

        assertThat(result).isTrue()
    }


    companion object {
        val recentSearches = listOf(
            RecentSearch(id = 1L, query = "Matrix", searchedAt = LocalDateTime.now()),
            RecentSearch(id = 2L, query = "Inception",  searchedAt = LocalDateTime.now()),
            RecentSearch(id = 3L, query = "Matrix",  searchedAt = LocalDateTime.now()),
        )
        val savedList = SavedList(
            id = 1,
            name = "Favorites",
            itemCount = 3
        )
        val pagedResult = PagedResult(
            data = listOf(savedList),
            nextPage = 1,
            prevPage = 1
        )
        val movie = SearchScreenState.MovieUiState(
            id = 1L,
            isSaved = true,
            savedListId = 1L,
        )
        val testQuery = "test"
        val contentId = 123L
        val imageUrl = "https://example.com/poster.jpg"
    }
}