package com.baghdad.viewmodel.search

import com.baghdad.domain.model.PagedResult
import com.baghdad.domain.usecase.genre.GetGenresUseCase
import com.baghdad.domain.usecase.recentlyViewed.AddRecentlyViewedUseCase
import com.baghdad.domain.usecase.recentlyViewed.DeleteAllRecentlyViewedUseCase
import com.baghdad.domain.usecase.recentlyViewed.GetRecentlyViewedUseCase
import com.baghdad.domain.usecase.search.DeleteAllRecentSearchesUseCase
import com.baghdad.domain.usecase.search.DeleteRecentSearchUseCase
import com.baghdad.domain.usecase.search.GetRecentSearchesUseCase
import com.baghdad.domain.usecase.search.SearchActorsUseCase
import com.baghdad.domain.usecase.search.SearchMoviesUseCase
import com.baghdad.domain.usecase.search.SearchTvShowsUseCase
import com.google.common.truth.Truth
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SearchViewModelTest {

    private lateinit var viewModel: SearchViewModel
    private lateinit var getGenresUseCase: GetGenresUseCase
    private lateinit var getRecentSearchesUseCase: GetRecentSearchesUseCase
    private lateinit var getRecentlyViewedUseCase: GetRecentlyViewedUseCase
    private lateinit var addRecentlyViewedUseCase: AddRecentlyViewedUseCase
    private lateinit var deleteAllRecentlyViewedUseCase: DeleteAllRecentlyViewedUseCase
    private lateinit var deleteAllRecentSearchesUseCase: DeleteAllRecentSearchesUseCase
    private lateinit var deleteRecentSearchUseCase: DeleteRecentSearchUseCase
    private lateinit var searchMoviesUseCase: SearchMoviesUseCase
    private lateinit var searchTvShowsUseCase: SearchTvShowsUseCase
    private lateinit var searchActorsUseCase: SearchActorsUseCase

    private val testDispatcher = StandardTestDispatcher()

    @BeforeEach
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        getGenresUseCase = mockk()
        getRecentSearchesUseCase = mockk()
        getRecentlyViewedUseCase = mockk()
        addRecentlyViewedUseCase = mockk()
        deleteAllRecentlyViewedUseCase = mockk()
        deleteAllRecentSearchesUseCase = mockk()
        deleteRecentSearchUseCase = mockk()
        searchMoviesUseCase = mockk()
        searchTvShowsUseCase = mockk()
        searchActorsUseCase = mockk()
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun createViewModel(dispatcher: CoroutineDispatcher = StandardTestDispatcher()): SearchViewModel {
        return SearchViewModel(
            getGenresUseCase = getGenresUseCase,
            getRecentSearchesUseCase = getRecentSearchesUseCase,
            getRecentlyViewedUseCase = getRecentlyViewedUseCase,
            addRecentlyViewedUseCase = addRecentlyViewedUseCase,
            deleteAllRecentlyViewedUseCase = deleteAllRecentlyViewedUseCase,
            deleteAllRecentSearchesUseCase = deleteAllRecentSearchesUseCase,
            deleteRecentSearchUseCase = deleteRecentSearchUseCase,
            searchMoviesUseCase = searchMoviesUseCase,
            searchTvShowsUseCase = searchTvShowsUseCase,
            searchActorsUseCase = searchActorsUseCase,
            dispatcher = dispatcher
        )
    }

    @Test
    fun `init should call all required use cases`() = runTest {
        coEvery { getGenresUseCase.getMovieGenres() } returns emptyList()
        coEvery { getGenresUseCase.getTvShowGenres() } returns emptyList()
        coEvery { getRecentSearchesUseCase() } returns flowOf(emptyList())
        coEvery { getRecentlyViewedUseCase() } returns flowOf(emptyList())
        coEvery { addRecentlyViewedUseCase(any(), any(), any()) } returns Unit
        coEvery { deleteAllRecentlyViewedUseCase() } returns Unit
        coEvery { deleteAllRecentSearchesUseCase() } returns Unit
        coEvery { deleteRecentSearchUseCase(any()) } returns Unit
        coEvery { searchMoviesUseCase(any(), any(), any()) } returns PagedResult(
            emptyList(),
            nextKey = null,
            prevKey = null
        )
        coEvery { searchTvShowsUseCase(any(), any(), any()) } returns PagedResult(
            emptyList(),
            nextKey = null,
            prevKey = null
        )
        coEvery { searchActorsUseCase(any(), any()) } returns PagedResult(
            emptyList(),
            nextKey = null,
            prevKey = null
        )

        viewModel = createViewModel()
        advanceUntilIdle()
    }

    @Test
    fun `onSearchTextChanged should update searchText and isLoading when text changes`() = runTest {
        viewModel = createViewModel()
        val newText = "new query"

        viewModel.onSearchTextChanged(newText)
        advanceUntilIdle()

        val state = viewModel.uiState.value
        Truth.assertThat(state.searchText).isEqualTo(newText)
        Truth.assertThat(state.isLoading).isTrue()
    }

    @Test
    fun `onSearchTextChanged should not process same query when call it twice`() = runTest {
        viewModel = createViewModel()
        val query = "same query"

        viewModel.onSearchTextChanged(query)
        advanceUntilIdle()

        val firstState = viewModel.uiState.value

        viewModel.onSearchTextChanged(query.trim())
        advanceUntilIdle()

        val secondState = viewModel.uiState.value
        Truth.assertThat(secondState.searchText).isEqualTo(query.trim())
        Truth.assertThat(secondState.lastProcessedQuery).isEqualTo(firstState.lastProcessedQuery)
    }

    @Test
    fun `onSearchTextChanged should handle when it has empty string`() = runTest {
        viewModel = createViewModel()

        viewModel.onSearchTextChanged("")
        advanceUntilIdle()

        val state = viewModel.uiState.value
        Truth.assertThat(state.searchText).isEmpty()
    }

    @Test
    fun `onSearchTextChanged should handle when it has whitespace-only string`() = runTest {
        viewModel = createViewModel()

        viewModel.onSearchTextChanged("   ")
        advanceUntilIdle()

        val state = viewModel.uiState.value
        Truth.assertThat(state.searchText).isEqualTo("   ")
    }


    @Test
    fun `should update searchText and set isLoading to true when user enters new query`() =
        runTest {
            viewModel = createViewModel()

            viewModel.onSearchTextChanged("Adventure")
            advanceUntilIdle()

            val state = viewModel.uiState.value
            Truth.assertThat(state.searchText).isEqualTo("Adventure")
            Truth.assertThat(state.isLoading).isTrue()
        }


    @Test
    fun `should clear paging flows when user enters blank query`() = runTest {
        viewModel = createViewModel()
        viewModel.onSearchTextChanged("     ")
        advanceTimeBy(SEARCH_DEBOUNCED_DELAY + 10)

        val state = viewModel.uiState.value
        Truth.assertThat(state.isLoading).isFalse()
    }

    @Test
    fun `should call deleteAllRecentlyViewedUseCase and handle success when onClearRecentlyViewedClick is invoked`() =
        runTest {
            coEvery { deleteAllRecentlyViewedUseCase.invoke() } returns Unit

            viewModel = createViewModel()

            viewModel.onClearRecentlyViewedClick()
            advanceUntilIdle()

            coVerify(exactly = 1) { deleteAllRecentlyViewedUseCase.invoke() }

            val state = viewModel.uiState.value
            Truth.assertThat(state.isLoading).isFalse()
        }

    @Test
    fun `should finalize loading and handle error when onClearRecentlyViewedClick fails`() =
        runTest {
            coEvery { deleteAllRecentlyViewedUseCase.invoke() } throws RuntimeException("fail")

            viewModel = createViewModel()

            viewModel.onClearRecentlyViewedClick()
            advanceUntilIdle()

            val state = viewModel.uiState.value
            Truth.assertThat(state.isLoading).isFalse()

        }


    @Test
    fun `should not trigger search before debounce delay`() = runTest {
        val testQuery = "test"
        viewModel = createViewModel()

        viewModel.onSearchTextChanged(testQuery)
        testScheduler.runCurrent()

        coVerify(exactly = 0) {
            searchMoviesUseCase(any(), any(), any())
            searchTvShowsUseCase(any(), any(), any())
            searchActorsUseCase(any(), any())
        }
    }

    @Test
    fun `should execute use case when onClearRecentSearchClick is called`() = runTest {
        coEvery { deleteAllRecentSearchesUseCase.invoke() } returns Unit
        viewModel = createViewModel()

        viewModel.onClearRecentSearchClick()
        advanceUntilIdle()

        coVerify(exactly = 1) { deleteAllRecentSearchesUseCase.invoke() }
        Truth.assertThat(viewModel.uiState.value.isLoading).isFalse()
    }

    @Test
    fun `should finalize loading when use case throws during onClearRecentSearchClick`() = runTest {
        coEvery { deleteAllRecentSearchesUseCase.invoke() } throws RuntimeException("error")
        viewModel = createViewModel()

        viewModel.onClearRecentSearchClick()
        advanceUntilIdle()

        coVerify { deleteAllRecentSearchesUseCase.invoke() }
        Truth.assertThat(viewModel.uiState.value.isLoading).isFalse()
    }

    @Test
    fun `should call use case when onRemoveRecentSearchItemClick is invoked`() = runTest {
        val targetId = 42L
        coEvery { deleteRecentSearchUseCase(targetId) } returns Unit
        viewModel = createViewModel()

        viewModel.onRemoveRecentSearchItemClick(targetId)
        advanceUntilIdle()

        coVerify(exactly = 1) { deleteRecentSearchUseCase(targetId) }
        Truth.assertThat(viewModel.uiState.value.isLoading).isFalse()
    }

    @Test
    fun `should finalize loading when use case throws during onRemoveRecentSearchItemClick`() =
        runTest {
            val targetId = 99L
            coEvery { deleteRecentSearchUseCase(targetId) } throws RuntimeException("error")
            viewModel = createViewModel()

            viewModel.onRemoveRecentSearchItemClick(targetId)
            advanceUntilIdle()

            coVerify { deleteRecentSearchUseCase(targetId) }
            Truth.assertThat(viewModel.uiState.value.isLoading).isFalse()
        }

    @Test
    fun `should show bottom sheet when onFilterIconClick is called`() = runTest {
        val viewModel = createViewModel()

        viewModel.onFilterIconClick()

        Truth.assertThat(viewModel.uiState.value.bottomSheetUiState.isBottomSheetVisible).isTrue()
    }

    companion object {
        internal const val SEARCH_DEBOUNCED_DELAY = 300L
    }
}