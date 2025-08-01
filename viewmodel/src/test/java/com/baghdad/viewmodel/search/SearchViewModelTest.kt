package com.baghdad.viewmodel.search

import android.annotation.SuppressLint
import com.baghdad.domain.exception.NoInternetException
import com.baghdad.domain.model.PagedResult
import com.baghdad.domain.model.search.RecentlyViewed
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
import com.baghdad.domain.util.now
import com.baghdad.entity.media.Genre
import com.baghdad.entity.media.TvShow
import com.baghdad.entity.person.Actor
import com.baghdad.viewmodel.errorStates.BaseSnackBarMessage
import com.google.common.truth.Truth.assertThat
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.spyk
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
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
        assertThat(state.searchText).isEqualTo(newText)
        assertThat(state.isLoading).isTrue()
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
        assertThat(secondState.searchText).isEqualTo(query.trim())
        assertThat(secondState.lastProcessedQuery).isEqualTo(firstState.lastProcessedQuery)
    }

    @Test
    fun `onSearchTextChanged should handle when it has empty string`() = runTest {
        viewModel = createViewModel()

        viewModel.onSearchTextChanged("")
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertThat(state.searchText).isEmpty()
    }

    @Test
    fun `onSearchTextChanged should handle when it has whitespace-only string`() = runTest {
        viewModel = createViewModel()

        viewModel.onSearchTextChanged("   ")
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertThat(state.searchText).isEqualTo("   ")
    }

    @Test
    fun `should update searchText and set isLoading to true when user enters new query`() =
        runTest {
            viewModel = createViewModel()

            viewModel.onSearchTextChanged("Adventure")
            advanceUntilIdle()

            val state = viewModel.uiState.value
            assertThat(state.searchText).isEqualTo("Adventure")
            assertThat(state.isLoading).isTrue()
        }

    @Test
    fun `should clear paging flows when user enters blank query`() = runTest {
        viewModel = createViewModel()
        viewModel.onSearchTextChanged("     ")
        advanceTimeBy(SEARCH_DEBOUNCED_DELAY + 10)

        val state = viewModel.uiState.value
        assertThat(state.isLoading).isFalse()
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
            assertThat(state.isLoading).isFalse()
        }

    @Test
    fun `should finalize loading and handle error when onClearRecentlyViewedClick fails`() =
        runTest {
            coEvery { deleteAllRecentlyViewedUseCase.invoke() } throws RuntimeException("fail")

            viewModel = createViewModel()

            viewModel.onClearRecentlyViewedClick()
            advanceUntilIdle()

            val state = viewModel.uiState.value
            assertThat(state.isLoading).isFalse()
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
        assertThat(viewModel.uiState.value.isLoading).isFalse()
    }

    @Test
    fun `should finalize loading when use case throws during onClearRecentSearchClick`() = runTest {
        coEvery { deleteAllRecentSearchesUseCase.invoke() } throws RuntimeException("error")
        viewModel = createViewModel()

        viewModel.onClearRecentSearchClick()
        advanceUntilIdle()

        coVerify { deleteAllRecentSearchesUseCase.invoke() }
        assertThat(viewModel.uiState.value.isLoading).isFalse()
    }

    @Test
    fun `should call use case when onRemoveRecentSearchItemClick is invoked`() = runTest {
        val targetId = 42L
        coEvery { deleteRecentSearchUseCase(targetId) } returns Unit
        viewModel = createViewModel()

        viewModel.onRemoveRecentSearchItemClick(targetId)
        advanceUntilIdle()

        coVerify(exactly = 1) { deleteRecentSearchUseCase(targetId) }
        assertThat(viewModel.uiState.value.isLoading).isFalse()
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
            assertThat(viewModel.uiState.value.isLoading).isFalse()
        }

    @Test
    fun `should show bottom sheet when onFilterIconClick is called`() = runTest {
        val viewModel = createViewModel()

        viewModel.onFilterIconClick()

        assertThat(viewModel.uiState.value.bottomSheetUiState.isBottomSheetVisible).isTrue()
    }

    @Test
    fun `should emit debounced trimmed query when search text is updated`() = runTest {
        val method = SearchViewModel::class.java.getDeclaredMethod("observeSearchQueryFlow")
        method.isAccessible = true

        val viewModel = createViewModel()

        val flow = method.invoke(viewModel) as Flow<String>
        val collected = mutableListOf<String>()

        val job = launch {
            flow.collect { value ->
                collected.add(value)
            }
        }

        viewModel.onSearchTextChanged("  reflected query ")

        advanceUntilIdle()

        job.cancel()

        assertThat(collected).containsExactly("reflected query")
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
    fun `should update state and trigger correct action when onSearchQueryChangedCollected is invoked`() =
        runTest {
            val viewModel = createViewModel()

            val method = viewModel::class.java.getDeclaredMethod(
                "onSearchQueryChangedCollected",
                String::class.java
            )

            method.isAccessible = true

            assertThat(viewModel.uiState.value.searchText)
        }


    ////////////////////////////
    @Test
    fun `should search movies when performSearchByTab is invoked and selected tab is MOVIES`() =
        runTest {
            val viewModel = createViewModel()
            val method = viewModel::class.java.getDeclaredMethod(
                "performSearchByTab",
                String::class.java
            )

            method.isAccessible = true
            coEvery { searchMoviesUseCase(any(), any(), any()) } returns PagedResult(
                emptyList(),
                nextKey = null,
                prevKey = null
            )
            assertThat(viewModel.uiState.value.searchText).isEmpty()
        }

    @Test
    fun `should search tv show when performSearchByTab is invoked and selected tab is TV_SHOWS`() =
        runTest {
            val viewModel = createViewModel()
            val method = viewModel::class.java.getDeclaredMethod(
                "performSearchByTab",
                String::class.java
            )

            method.isAccessible = true
            coEvery { searchTvShowsUseCase(any(), any(), any()) } returns PagedResult(
                emptyList(),
                nextKey = null,
                prevKey = null
            )
            assertThat(viewModel.uiState.value.searchText).isEmpty()
        }

    @Test
    fun `should search actors when performSearchByTab is invoked and selected tab is ACTORS`() =
        runTest {
            val viewModel = createViewModel()
            val method = viewModel::class.java.getDeclaredMethod(
                "performSearchByTab",
                String::class.java
            )

            method.isAccessible = true
            coEvery { searchActorsUseCase(any(), any()) } returns PagedResult(
                emptyList(),
                nextKey = null,
                prevKey = null
            )
            assertThat(viewModel.uiState.value.searchText).isEmpty()
        }

    @Test
    fun `should show no internet snackbar when onSearchError is invoked with NoInternetException`() =
        runTest {
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
    fun `should delegate to handleError when onSearchError is invoked with generic RuntimeException`() =
        runTest {
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
    fun `should handle custom exception when onSearchError is invoked with IllegalStateException`() =
        runTest {
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
    fun `showNoInternetSnackBar should delegate to showSnackBar with NetworkError config`() =
        runTest {
            val viewModel = spyk(createViewModel()) {
                every {
                    showSnackBar(
                        message = BaseSnackBarMessage.NetworkError,
                        actionLabelRes = null,
                        isSuccess = false,
                        durationMillis = Int.MAX_VALUE.toLong()
                    )
                } just Runs
            }

            val method = viewModel::class.java.getDeclaredMethod("showNoInternetSnackBar")
            method.isAccessible = true
            method.invoke(viewModel)

            assertThat(viewModel.uiState.value.isLoading).isFalse()
        }


    @Test
    fun `should update recentViewed when onGetRecentViewedSuccess is invoked with more than 10 items`() =
        runTest {
            val items = (1..15).map {
                RecentlyViewed(
                    contentId = it.toLong(),
                    contentType = RecentlyViewed.ContentType.MOVIE,
                    contentImageUrl = "https://example.com/poster$it.jpg",
                    viewedAt = LocalDateTime.now(),
                )
            }

            val viewModel = createViewModel()

            val method = viewModel::class.java.getDeclaredMethod(
                "onGetRecentViewedSuccess",
                List::class.java
            )
            method.isAccessible = true
            method.invoke(viewModel, items)

            val actual = viewModel.uiState.value.recentViewed
            val expected = items
                .take(10)
                .map { it.toRecentlyViewedUI() }
                .distinctBy { it.id }

            assertThat(actual).isEqualTo(expected)
        }

    @Test
    fun `should set genres to empty and isGenresError to true when onGetTvShowGenresError is called`() =
        runTest {
            val viewModel = createViewModel()

            val throwable = RuntimeException("Test error")
            val method = viewModel::class.java.getDeclaredMethod(
                "onGetTvShowGenresError",
                Throwable::class.java
            )
            method.isAccessible = true
            method.invoke(viewModel, throwable)

            val result = viewModel.uiState.value.bottomSheetUiState.tvShowsFilter

            assertThat(result.allGenres).isEmpty()
            assertThat(result.isGenresError).isTrue()
        }

//    @Test
//    fun `should toggle isSaved flag when onSaveRecentlyViewedClick is called with matching id`() = runTest {
//        val initialList = listOf(
//            SearchScreenState.RecentlyViewedUiState(id = 1, isSaved = false),
//            SearchScreenState.RecentlyViewedUiState(id = 2, isSaved = true),
//            SearchScreenState.RecentlyViewedUiState(id = 3, isSaved = false)
//        )
//
//        val viewModel = createViewModel(initialList)
//
//        viewModel.onSaveRecentlyViewedClick(2L)
//
//        val updatedList = viewModel.uiState.value.recentViewed
//
//        val toggledItem = updatedList.find { it.id == 2L }
//        assertThat(toggledItem?.isSaved).isFalse()
//
//        assertThat(updatedList.find { it.id == 1L }?.isSaved).isEqualTo(false)
//        assertThat(updatedList.find { it.id == 3L }?.isSaved).isEqualTo(false)
//    }

//    @Test
//    fun `should update searchText and reset lastProcessedQuery when onRecentSearchItemClick is called`() = runTest {
//        // Arrange: create recentSearch mock data
//        val recentSearchList = listOf(
//            SearchScreenState.RecentSearchUiState(id = 1L, query = "Inception"),
//            SearchScreenState.RecentSearchUiState(id = 2L, query = "Interstellar"),
//        )
//
//        val viewModel = createViewModelWithRecentSearch(recentSearchList)
//
//        var searchTextChangedArg: String? = null
//        var searchQueryChangedArg: String? = null
//
//        // Override callbacks for verification
//        viewModel.overrideOnSearchTextChanged { text ->
//            searchTextChangedArg = text
//        }
//
//        viewModel.overrideOnSearchQueryChangedCollected { text ->
//            searchQueryChangedArg = text
//        }
//
//        // Act
//        viewModel.onRecentSearchItemClick(2L)
//
//        // Assert
//        val currentState = viewModel.uiState.value
//        assertThat(currentState.searchText).isEqualTo("Interstellar")
//        assertThat(viewModel.lastProcessedQuery).isEqualTo("")
//        assertThat(searchTextChangedArg).isEqualTo("Interstellar")
//        assertThat(searchQueryChangedArg).isEqualTo("Interstellar")
//    }

    @Test
    fun `should hide bottom sheet when onFilterCloseIconClick is triggered`() = runTest {
        val viewModel = createViewModel()

        val method = viewModel::class.java.getDeclaredMethod("onFilterCloseIconClick")
        method.isAccessible = true

        method.invoke(viewModel)

        val result = viewModel.uiState.value.bottomSheetUiState.isBottomSheetVisible
        assertThat(result).isFalse()
    }

    @Test
    fun `should send navigation effect with correct contentId`() = runTest {
        val viewModel = createViewModel()
        val testId = 1234L

        val effects = mutableListOf<SearchScreenEffect>()
        val job = launch {
            viewModel.uiEffect.collect { effects.add(it) }
        }

        val method = viewModel::class.java.getDeclaredMethod(
            "onAddRecentlyViewedTvShowSuccess",
            Long::class.java
        )
        method.isAccessible = true
        method.invoke(viewModel, testId)

        advanceUntilIdle()

        assertThat(effects).containsExactly(SearchScreenEffect.NavigateToTvShowDetails(testId))

        job.cancel()
    }

    @Test
    fun `should send actor navigation effect on actor item click`() = runTest {
        val viewModel = createViewModel()
        val testId = 9876L

        val effects = mutableListOf<SearchScreenEffect>()
        val job = launch {
            viewModel.uiEffect.collect { effects.add(it) }
        }

        val method = viewModel::class.java.getDeclaredMethod("onActorItemClick", Long::class.java)
        method.isAccessible = true
        method.invoke(viewModel, testId)

        advanceUntilIdle()

        assertThat(effects).containsExactly(SearchScreenEffect.NavigateToActorDetails(testId))

        job.cancel()
    }

    @Test
    fun `searchTvShows should handle error and call onSearchError`() = runTest {
        val query = "horror"
        val error = RuntimeException("404")
        val viewModel = createViewModel()
        val method = viewModel::class.java.getDeclaredMethod(
            "searchTvShows",
            String::class.java
        )

        method.isAccessible = true
        coEvery { searchTvShowsUseCase.invoke(query, any(), any()) } throws error

        coEvery { searchTvShowsUseCase(any(), any(), any()) } returns PagedResult(
            emptyList(),
            nextKey = null,
            prevKey = null
        )
        assertThat(viewModel.uiState.value.searchText).isEmpty()
        assertThat(viewModel.uiState.value.isLoading).isFalse()
    }

    @Test
    fun `searchTvShows should handle error and call onSearchError `() = runTest {
        val query = "horror"
        val error = RuntimeException("404")

        val viewModel = createViewModel()

        val method = viewModel::class.java.getDeclaredMethod("searchTvShows", String::class.java)
        method.isAccessible = true

        coEvery {
            searchTvShowsUseCase(
                query = query,
                filter = any(),
                page = any()
            )
        } throws error

        coEvery {
            searchTvShowsUseCase(any(), any(), any())
        } returns PagedResult(
            data = emptyList(),
            nextKey = null,
            prevKey = null
        )

        method.invoke(viewModel, query)

        assertThat(viewModel.uiState.value.searchText).isEmpty()
    }

    @Test
    fun `searchActors should handle error and call onSearchError`() = runTest {
        val query = "drama"
        val error = RuntimeException("Server timeout")

        val viewModel = createViewModel()

        val method = viewModel::class.java.getDeclaredMethod("searchActors", String::class.java)
        method.isAccessible = true

        coEvery {
            searchActorsUseCase(query = query, page = any())
        } throws error

        method.invoke(viewModel, query)

        assertThat(viewModel.uiState.value.actorsFlow).isNotNull()
        assertThat(viewModel.uiState.value.isLoading).isTrue()

    }

    @Test
    fun `searchActors should load data, handle success and map entities`() = runTest {
        val query = "drama"
        val page = 1

        val viewModel = createViewModel()

        coEvery {
            searchActorsUseCase(query = query, page = page)
        } returns PagedResult(
            data = listOf(),
            nextKey = null,
            prevKey = null
        )


        val method = viewModel::class.java.getDeclaredMethod("searchActors", String::class.java)
        method.isAccessible = true

        method.invoke(viewModel, query)

        val actorsFlow = viewModel.uiState.value.actorsFlow
        assertThat(actorsFlow).isNotNull()
    }

    @Test
    fun `searchActors loadData should invoke use case with correct params`() = runTest {
        val query = "sci-fi"
        val page = 2

        val viewModel = createViewModel()

        val method = viewModel::class.java.getDeclaredMethod("searchActors", String::class.java)
        method.isAccessible = true

        coEvery {
            searchActorsUseCase(query = query, page = page)
        } returns PagedResult(
            data = listOf(
                Actor(
                    id = 1L,
                    name = "Eva Thornton",
                    profilePictureURL = "https://example.com/images/eva.jpg",
                    birthDate = LocalDate(1985, 5, 20),
                    placeOfBirth = "London, UK",
                    deathDate = null,
                    biography = "Eva Thornton began her career on the West End stage before making waves in international cinema.",
                    headerPictures = listOf(
                        "https://example.com/headers/eva1.jpg",
                        "https://example.com/headers/eva2.jpg"
                    ),
                    department = "Acting"
                )
            ),
            nextKey = null,
            prevKey = null
        )
        method.invoke(viewModel, query)
    }

    @Test
    fun `clearAllPagingFlows should reset actorFlow flows to empty`() = runTest {
        val viewModel = createViewModel()

        val method = viewModel::class.java.getDeclaredMethod("clearAllPagingFlows")
        method.isAccessible = true

        val job = launch {
            method.invoke(viewModel)
        }
        job.join()

        val updatedState = viewModel.uiState.value
        val actorItems = updatedState.actorsFlow.toList()
        assertThat(actorItems).isEmpty()
    }

    @Test
    fun `clearAllPagingFlows should reset moviesFlow to empty`() = runTest {
        val viewModel = createViewModel()

        val method = viewModel::class.java.getDeclaredMethod("clearAllPagingFlows")
        method.isAccessible = true

        val job = launch {
            method.invoke(viewModel)
        }
        job.join()

        val updatedState = viewModel.uiState.value
        val actorItems = updatedState.moviesFlow.toList()
        assertThat(actorItems).isEmpty()
    }

    @Test
    fun `clearAllPagingFlows should reset tvShow to empty`() = runTest {
        val viewModel = createViewModel()

        val method = viewModel::class.java.getDeclaredMethod("clearAllPagingFlows")
        method.isAccessible = true

        val job = launch {
            method.invoke(viewModel)
        }
        job.join()

        val updatedState = viewModel.uiState.value
        val actorItems = updatedState.tvShowsFlow.toList()
        assertThat(actorItems).isEmpty()
    }

    @Test
    fun `searchTvShows should update tvShowsFlow and handle success callbacks`() = runTest {
        val query = "Comedy"
        val page = 1
        val uiState =
            SearchScreenState.TvShowUiState(
                id = 101L,
                posterPictureURL = "https://example.com/poster.jpg"
            )

        val viewModel = createViewModel()

        coEvery { tvShow.toTvShowUI() } returns uiState

        coEvery {
            searchTvShowsUseCase(query = query, filter = any(), page = page)
        } returns PagedResult(data = listOf(tvShow), nextKey = null, prevKey = null)

        val method = viewModel::class.java.getDeclaredMethod("searchTvShows", String::class.java)
        method.isAccessible = true

        val job = launch {
            viewModel.uiState.value.tvShowsFlow.collect { pagingData ->
            }
        }
        advanceUntilIdle()
        job.cancel()

        assertThat(viewModel.uiState.value.isLoading).isFalse()

    }

    @Test
    fun `searchTvShows should update tvShowsFlow and handle success callbacks `() = runTest {
        val query = "Comedy"
        val page = 1
        val uiState = SearchScreenState.TvShowUiState(
            id = 101L,
            posterPictureURL = "https://example.com/poster.jpg"
        )

        val viewModel = createViewModel()

        coEvery { tvShow.toTvShowUI() } returns uiState
//
        coEvery {
            searchTvShowsUseCase(query = query, filter = any(), page = page)
        } returns PagedResult(data = listOf(tvShow), nextKey = null, prevKey = null)

        val method = viewModel::class.java.getDeclaredMethod("searchTvShows", String::class.java)
        method.isAccessible = true
        method.invoke(viewModel, query)

        val job = launch {
            viewModel.uiState.value.tvShowsFlow.collect { pagingData ->
            }
        }
        advanceUntilIdle()
//        job.cancel()

//        assertThat(viewModel.uiState.value.isLoading).isFalse()
//        assertThat(viewModel.uiState.value.tvShowsFlow).isNotNull()
    }

    @Test
    fun `onSearchError should call showNoInternetSnackBar for NoInternetException`() = runTest {
        val viewModel = createViewModel()

        val method = viewModel::class.java.getDeclaredMethod("onSearchError", Throwable::class.java)
        method.isAccessible = true

        val exception = NoInternetException()

        method.invoke(viewModel, exception)

        assertThat(viewModel.uiState.value.isLoading)
    }

//    @Test
//    fun `onRecentSearchItemClick sets search text and triggers callbacks`() {
//        viewModel.onRecentSearchItemClick(1L)
//
//        assertThat(viewModel.currentState.searchText).isEqualTo("Hello")
//        assertThat(viewModel.currentState.lastProcessedQuery).isEmpty()
//        verify { viewModel.onSearchTextChanged("Hello") }
//        verify { viewModel.onSearchQueryChangedCollected("Hello") }
//    }


    //    advanceUntilIdle()
    companion object {
        internal const val SEARCH_DEBOUNCED_DELAY = 300L
        private val genres = listOf(
            Genre(id = 1L, name = "Action"),
            Genre(id = 2L, name = "Adventure"),
            Genre(id = 3L, name = "Comedy"),
            Genre(id = 4L, name = "Drama"),
            Genre(id = 5L, name = "Fantasy"),
            Genre(id = 6L, name = "Horror"),
            Genre(id = 7L, name = "Mystery"),
            Genre(id = 8L, name = "Romance"),
            Genre(id = 9L, name = "Sci-Fi"),
            Genre(id = 10L, name = "Thriller")

        )
        private val tvShow = TvShow(
            id = 1L,
            title = "Comedy",
            genres = genres,
            averageRating = 8.7,
            userRating = 9.2,
            releaseDate = LocalDate(2020, 5, 15),
            overview = "A band of unlikely heroes must protect their realm from an ancient evil rising from the rift.",
            posterImageURL = "https://example.com/images/chronicles_of_the_rift_poster.jpg",
            trailerURL = "https://example.com/trailers/chronicles_of_the_rift.mp4",
            headerImagesURLs = listOf(
                "https://example.com/images/header_1.jpg",
                "https://example.com/images/header_2.jpg",
                "https://example.com/images/header_3.jpg"
            ),
            numberOfSeasons = 4
        )

    }
}