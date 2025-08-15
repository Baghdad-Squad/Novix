package com.baghdad.viewmodel.continueWatching

import app.cash.turbine.test
import com.baghdad.domain.model.continueWatching.UserWatchedMedia
import com.baghdad.domain.model.pagination.PagedResult
import com.baghdad.domain.usecase.login.IsUserLoggedInUseCase
import com.baghdad.domain.usecase.savedList.AddMovieToSavedListUseCase
import com.baghdad.domain.usecase.savedList.CreateSavedListUseCase
import com.baghdad.domain.usecase.savedList.GetSavedListsUseCase
import com.baghdad.domain.usecase.savedList.RemoveMovieFromSavedListUseCase
import com.baghdad.domain.usecase.userWatchedMedia.GetUserWatchedMediaMovieGenresUseCase
import com.baghdad.domain.usecase.userWatchedMedia.GetUserWatchedMediaMoviesByGenreUseCase
import com.baghdad.domain.usecase.userWatchedMedia.GetUserWatchedMediaMoviesUseCase
import com.baghdad.domain.usecase.userWatchedMedia.GetUserWatchedMediaTvShowGenresUseCase
import com.baghdad.domain.usecase.userWatchedMedia.GetUserWatchedMediaTvShowsByGenreUseCase
import com.baghdad.domain.usecase.userWatchedMedia.GetUserWatchedMediaTvShowsUseCase
import com.baghdad.entity.media.Genre
import com.baghdad.entity.savedList.SavedList
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class ContinueWatchingViewModelTest {
    val getUserWatchedMediaTvShowGenres: GetUserWatchedMediaTvShowGenresUseCase = mockk()
    val getUserWatchedMediaMovieGenres: GetUserWatchedMediaMovieGenresUseCase =
        mockk(relaxed = true)
    val getUserWatchedMediaMoviesUseCase: GetUserWatchedMediaMoviesUseCase = mockk()
    val getUserWatchedMediaTvShowsUseCase: GetUserWatchedMediaTvShowsUseCase = mockk()
    val getUserWatchedMediaMoviesByGenreUseCase: GetUserWatchedMediaMoviesByGenreUseCase = mockk()
    val getUserWatchedMediaTvShowsByGenreUseCase: GetUserWatchedMediaTvShowsByGenreUseCase = mockk()
    val isUserLoggedInUseCase: IsUserLoggedInUseCase = mockk()
    val getSavedListsUseCase: GetSavedListsUseCase = mockk()
    val addMovieToSavedListUseCase: AddMovieToSavedListUseCase = mockk()
    val createSavedListUseCase: CreateSavedListUseCase = mockk()
    val removeMovieFromSavedListUseCase: RemoveMovieFromSavedListUseCase = mockk()
    private val testDispatcher = StandardTestDispatcher()


    private fun createViewModel(): ContinueWatchingViewModel {
        return ContinueWatchingViewModel(
            getContinueWatchingTvShowGenres = getUserWatchedMediaTvShowGenres,
            getContinueWatchingMovieGenres = getUserWatchedMediaMovieGenres,
            getUserWatchedMediaMoviesUseCase = getUserWatchedMediaMoviesUseCase,
            getUserWatchedMediaTvShowsUseCase = getUserWatchedMediaTvShowsUseCase,
            getUserWatchedMediaMoviesByGenreUseCase = getUserWatchedMediaMoviesByGenreUseCase,
            getUserWatchedMediaTvShowsByGenreUseCase = getUserWatchedMediaTvShowsByGenreUseCase,
            isUserLoggedInUseCase = isUserLoggedInUseCase,
            getSavedListsUseCase = getSavedListsUseCase,
            addMovieToSavedListUseCase = addMovieToSavedListUseCase,
            createSavedListUseCase = createSavedListUseCase,
            removeMovieFromSavedListUseCase = removeMovieFromSavedListUseCase,
            defaultDispatcher = testDispatcher
        )
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @BeforeEach
    fun setup() {
        Dispatchers.setMain(testDispatcher)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `should navigate back when onBackClick is called`() = runTest {
        val viewModel = createViewModel()
        viewModel.uiEffect.test {
            viewModel.onBackClick()
            assertThat(awaitItem()).isEqualTo(ContinueWatchingScreenEffect.NavigateBack)
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `should navigate to movie details when onMediaClick  is called and content type is movie`() =
        runTest {
            val viewModel = createViewModel()
            val movieId = 1L
            val contentType = ContinueWatchingState.ContinueWatchingMovieUiState.ContentType.MOVIE

            viewModel.onMediaClick(movieId, contentType)

            viewModel.uiEffect.test {
                assertThat(awaitItem()).isEqualTo(
                    ContinueWatchingScreenEffect.NavigateToMovieDetails(movieId)
                )
            }
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `should navigate to tv show details when onMediaClick is called and content type is tv show`() =
        runTest {
            val viewModel = createViewModel()
            val tvShowId = 1L
            val contentType = ContinueWatchingState.ContinueWatchingMovieUiState.ContentType.TV_SHOW

            viewModel.onMediaClick(tvShowId, contentType)

            viewModel.uiEffect.test {
                assertThat(awaitItem()).isEqualTo(
                    ContinueWatchingScreenEffect.NavigateToTvShowDetails(tvShowId)
                )
            }
        }

    @Test
    fun `should navigate to login when onLoginClicked is called`() = runTest {
        val viewModel = createViewModel()

        viewModel.onLoginClicked()

        viewModel.uiEffect.test {
            assertThat(awaitItem()).isEqualTo(ContinueWatchingScreenEffect.NavigateToLogin)
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `should update movie genre tab when onGenreTabClick is called`() = runTest {
        val viewModel = createViewModel()
        val genreId = 1L
        coEvery { getUserWatchedMediaMovieGenres() } returns flowOf(genres)

        viewModel.onGenreClick(genreId)

        viewModel.uiState.test {
            assertThat(awaitItem().selectedMovieGenreId).isEqualTo(genreId)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `should update tv show genre tab when onGenreTabClick is called`() = runTest {
        val viewModel = createViewModel()
        val genreId = 1L
        coEvery { getUserWatchedMediaTvShowGenres() } returns flowOf(genres)

        viewModel.onSelectedTab(isMovieTab = false)
        viewModel.onGenreClick(genreId)

        viewModel.uiState.test {
            assertThat(awaitItem().selectedTvShowGenreId).isEqualTo(genreId)
        }
    }

    @Test
    fun `should update selected tab when onSelectedTab is called`() = runTest {
        val viewModel = createViewModel()
        val isMovieTab = false

        viewModel.onSelectedTab(isMovieTab)

        viewModel.uiState.test {
            assertThat(awaitItem().selectedMediaTabIsMovie).isEqualTo(isMovieTab)
        }
    }

    @Test
    fun `should update movie save state when onMovieSaveClick is called and isSaved is false`() =
        runTest {
            val viewModel = createViewModel()
            val movie = userWatchedMedia.toContinueWatchingUiState()

            viewModel.onMovieSaveClick(movie)

            viewModel.uiState.test {
                assertThat(awaitItem().addToListBottomSheetState.isVisible).isTrue()
            }
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `should update movie state to false when onMovieSaveClick is called and isSaved is true`() =
        runTest {
            val viewModel = createViewModel()
            val movie = userWatchedMedia.copy(isSaved = true).toContinueWatchingUiState()
            coEvery { removeMovieFromSavedListUseCase(any(), any()) } just runs

            viewModel.onMovieSaveClick(movie)
            advanceUntilIdle()

            viewModel.uiState.test {
                assertThat(awaitItem().addToListBottomSheetState.isVisible).isFalse()
            }
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `should save item to list when onSaveItemToListClicked is called and selected tab is movie`() =
        runTest {
            val viewModel = createViewModel()
            coEvery { addMovieToSavedListUseCase(any(), any()) } just runs

            viewModel.onSaveItemToListClicked()
            advanceUntilIdle()

            assertThat(viewModel.uiState.value.addToListBottomSheetState.isVisible).isFalse()
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `should save item to list when onSaveItemToListClicked is called and selected tab is tv show`() =
        runTest {
            val viewModel = createViewModel()
            coEvery { addMovieToSavedListUseCase(any(), any()) } just runs

            viewModel.onSelectedTab(isMovieTab = false)
            viewModel.onSaveItemToListClicked()
            advanceUntilIdle()

            assertThat(viewModel.uiState.value.addToListBottomSheetState.isVisible).isFalse()
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `should create new list when onCreateNewListClicked is called`() = runTest {
        val viewModel = createViewModel()
        coEvery { createSavedListUseCase(any()) } just runs

        viewModel.onCreateNewListClicked()
        advanceUntilIdle()

        assertThat(viewModel.uiState.value.addListBottomSheetState.isVisible).isTrue()
        assertThat(viewModel.uiState.value.addToListBottomSheetState.isVisible).isFalse()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `should set selected list when onListSelected is called`() = runTest {
        val viewModel = createViewModel()
        val listId = 1L

        viewModel.onListSelected(listId)
        advanceUntilIdle()

        viewModel.uiState.test {
            assertThat(awaitItem().addToListBottomSheetState.selectedListId).isEqualTo(listId)
        }

    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `should set list name when onCreatedListNameChanged is called`() = runTest {
        val viewModel = createViewModel()
        val listName = "New List"

        viewModel.onCreatedListNameChanged(listName)
        advanceUntilIdle()

        viewModel.uiState.test {
            assertThat(awaitItem().addListBottomSheetState.listName).isEqualTo(listName)
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `should dismiss list bottom sheet when onCreateListBottomSheetDismiss is called`() =
        runTest {
            val viewModel = createViewModel()

            viewModel.onCreateListBottomSheetDismiss()
            advanceUntilIdle()

            viewModel.uiState.test {
                assertThat(awaitItem().addListBottomSheetState.isVisible).isFalse()
            }
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `should create new saved list when onCreateListBottomSheetAddClick is called`() = runTest {
        val viewModel = createViewModel()
        coEvery { createSavedListUseCase(any()) } just runs
        val listName = "New List"

        viewModel.onCreatedListNameChanged(listName)
        viewModel.onCreateListBottomSheetAddClick()
        advanceUntilIdle()

        viewModel.uiState.test {
            assertThat(awaitItem().addListBottomSheetState.isVisible).isFalse()
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `should check if user is logged in and get saved lists when user is logged in and view model is initialized`() =
        runTest {
            val viewModel = createViewModel()
            coEvery { isUserLoggedInUseCase() } returns true
            coEvery { getSavedListsUseCase(any(), any()) } returns pagedSavedList

            advanceUntilIdle()

            assertThat(viewModel.uiState.value.isUserLoggedIn).isTrue()
        }


    private companion object {
        val userWatchedMedia = UserWatchedMedia(
            contentId = 123L,
            genreIds = listOf(1, 2, 3),
            contentImageUrl = "https://example.com/image.jpg",
            contentType = UserWatchedMedia.ContentType.MOVIE,
            isSaved = false,
            listId = 456L,
            userId = 1,
        )

        val pagedMovie = PagedResult(
            data = listOf(userWatchedMedia),
            nextKey = null,
            prevKey = null,
        )

        val savedList = SavedList(
            id = 1L,
            name = "List 1",
            itemCount = 1,
        )

        val pagedSavedList = PagedResult(
            data = listOf(savedList),
            nextKey = null,
            prevKey = null,
        )

        val genres = listOf(
            Genre(1, "Action"),
            Genre(2, "Comedy"),
            Genre(3, "Drama")
        )
    }

}

