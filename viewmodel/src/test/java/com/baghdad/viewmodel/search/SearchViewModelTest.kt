package com.baghdad.viewmodel.search

import com.baghdad.domain.exception.NoInternetException
import com.baghdad.domain.model.PagedResult
import com.baghdad.domain.model.search.RecentlyViewed
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
import kotlinx.coroutines.launch
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
    private lateinit var viewModel: SearchViewModel
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
    fun `should init call all required use cases`() = runTest {
        coEvery { getRecentSearchesUseCase() } returns flowOf(emptyList())
        coEvery { getRecentlyViewedUseCase() } returns flowOf(emptyList())
        coEvery { addRecentlyViewedUseCase(any(), any(), any()) } returns Unit
        coEvery { deleteAllRecentlyViewedUseCase() } returns Unit
        coEvery { deleteAllRecentSearchesUseCase() } returns Unit
        coEvery { deleteRecentSearchUseCase(any()) } returns Unit
        coEvery { searchMoviesUseCase(any(), any()) } returns PagedResult(
            emptyList(),
            nextKey = null,
            prevKey = null
        )
        coEvery { searchTvShowsUseCase(any(), any()) } returns PagedResult(
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
    fun `should onSearchTextChanged update searchText and isLoading when text changes`() = runTest {
        viewModel = createViewModel()
        val newText = "new query"

        viewModel.onSearchTextChanged(newText)
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertThat(state.searchText).isEqualTo(newText)
        assertThat(state.isLoading).isTrue()
    }

    @Test
    fun `should onSearchTextChanged  not process same query when call it twice`() = runTest {
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
    fun `should onSearchTextChanged handle when it has empty string`() = runTest {
        viewModel = createViewModel()

        viewModel.onSearchTextChanged("")
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertThat(state.searchText).isEmpty()
    }

    @Test
    fun `should onSearchTextChanged handle when it has whitespace-only string`() = runTest {
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
    fun `should not trigger search when debounce delay`() = runTest {
        val testQuery = "test"
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

    @Test
    fun `should search movies when performSearchByTab is invoked and selected tab is MOVIES`() =
        runTest {
            val viewModel = createViewModel()
            val method = viewModel::class.java.getDeclaredMethod(
                "performSearchByTab",
                String::class.java
            )

            method.isAccessible = true
            coEvery { searchMoviesUseCase(any(), any()) } returns PagedResult(
                emptyList(),
                nextKey = null,
                prevKey = null
            )
            assertThat(viewModel.uiState.value.searchText).isEmpty()
        }

    @Test
    fun `should search TV shows when performSearchByTab is called and selected tab is TV_SHOWS`() =
        runTest {
            val viewModel = createViewModel()
            val method = viewModel::class.java.getDeclaredMethod(
                "performSearchByTab",
                String::class.java
            )

            method.isAccessible = true
            coEvery { searchTvShowsUseCase(any(), any()) } returns PagedResult(
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
    fun `should delegate to showSnackBar with NetworkError config when showNoInternetSnackBar is invoked`() =
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
    fun `should invoke onSnackBarActionLabelClick successfully`() = runTest {
        val viewModel = createViewModel()
        val method = viewModel::class.java.getDeclaredMethod("onSnackBarActionLabelClick")

        method.invoke(viewModel)

        assertThat(viewModel.uiState.value.isLoading)
    }

    @Test
    fun `should send NavigateToActorDetails effect when actor item is clicked`() {
        val actorId = 101L
        val expectedEffect = SearchScreenEffect.NavigateToActorDetails(actorId)
        val viewModel = createViewModel()

        viewModel.onActorItemClick(actorId)

        assertThat((expectedEffect).actorId).isEqualTo(
            actorId
        )
    }

    @Test
    fun `should send NavigateToTvShowDetails effect when recently viewed TV show is added`() {
        val contentId = 303L
        val expectedEffect = SearchScreenEffect.NavigateToTvShowDetails(contentId)
        val viewModel = createViewModel()

        val method = SearchViewModel::class.java
            .getDeclaredMethod("onAddRecentlyViewedTvShowSuccess", Long::class.java)
            .apply { isAccessible = true }

        method.invoke(viewModel, contentId)

        assertThat((expectedEffect).tvShowId).isEqualTo(
            contentId
        )
    }

    @Test
    fun `should add recently viewed TV show and send NavigateToTvShowDetails effect when TV show item is clicked`() {
        val contentId = 777L
        val imageUrl = "https://image.tmdb.org/t/p/fantasy.jpg"
        val viewModel = createViewModel()

        val expectedEffect = SearchScreenEffect.NavigateToTvShowDetails(contentId)

        viewModel.onTvShowItemClick(contentId, imageUrl)

        assertThat((expectedEffect).tvShowId).isEqualTo(
            contentId
        )
    }

    @Test
    fun `should send NavigateToTvShowDetails effect when onAddRecentlyViewedTvShowSuccess is triggered by success`() {
        val contentId = 555L
        val imageUrl = "https://images.example.com/poster.jpg"
        val viewModel = createViewModel()

        val expectedEffect = SearchScreenEffect.NavigateToTvShowDetails(contentId)

        viewModel.onTvShowItemClick(contentId, imageUrl)

        assertThat((expectedEffect).tvShowId).isEqualTo(
            contentId
        )
    }

    @Test
    fun `should send NavigateToMovieDetails effect when recently viewed movie is added`() {
        val contentId = 999L
        val viewModel = createViewModel()
        val expectedEffect = SearchScreenEffect.NavigateToMovieDetails(contentId)
        val method = SearchViewModel::class.java
            .getDeclaredMethod("onAddRecentlyViewedMovieSuccess", Long::class.java)
            .apply { isAccessible = true }

        method.invoke(viewModel, contentId)

        assertThat((expectedEffect).movieId).isEqualTo(
            contentId
        )
    }

    @Test
    fun `should execute addRecentlyViewedUseCase with movie content when movie item is clicked`() {
        val contentId = 123L
        val imageUrl = "https://example.com/poster.jpg"
        val viewModel = createViewModel()

        viewModel.onMovieItemClick(contentId, imageUrl)

        assertThat(contentId).isEqualTo(123L)
        assertThat(imageUrl).contains("poster.jpg")
    }

    @Test
    fun `should send NavigateToMovieDetails effect when addRecentlyViewedUseCase succeeds`() {
        val contentId = 456L
        val imageUrl = "https://example.com/movie.jpg"
        val expectedEffect = SearchScreenEffect.NavigateToMovieDetails(contentId)
        val viewModel = createViewModel()

        viewModel.onMovieItemClick(contentId, imageUrl)

        assertThat((expectedEffect).movieId).isEqualTo(
            contentId
        )
    }

    @Test
    fun `should call onLoading and onFinally when movie item is clicked`() {
        val contentId = 789L
        val imageUrl = "https://example.com/cinema.jpg"
        val viewModel = createViewModel()

        viewModel.onMovieItemClick(contentId, imageUrl)

        assertThat(contentId).isEqualTo(789L)
        assertThat(imageUrl).startsWith("https://example.com/")
    }

    @Test
    fun `should send NavigateToMovieDetails effect when recently viewed item is a movie`() {
        val contentId = 101L
        val imageUrl = "https://example.com/movie.jpg"
        val viewModel = createViewModel()

        val expectedEffect = SearchScreenEffect.NavigateToMovieDetails(contentId)

        viewModel.onRecentlyViewedClick(contentId, imageUrl)

        assertThat((expectedEffect).movieId).isEqualTo(
            contentId
        )
    }

    @Test
    fun `should send NavigateToTvShowDetails effect when recently viewed item is a TV show`() {
        val contentId = 202L
        val imageUrl = "https://example.com/tv.jpg"
        val viewModel = createViewModel()

        val expectedEffect = SearchScreenEffect.NavigateToTvShowDetails(contentId)

        viewModel.onRecentlyViewedClick(contentId, imageUrl)

        assertThat((expectedEffect).tvShowId).isEqualTo(
            contentId
        )
    }

    @Test
    fun `should not send effect when recently viewed item is not found `() {
        val contentId = 303L
        val imageUrl = "https://example.com/missing.jpg"
        val viewModel = createViewModel()

        viewModel.onRecentlyViewedClick(contentId, imageUrl)

        val captured = mutableListOf<SearchScreenEffect>()
        assertThat(captured).isEmpty()
    }
}