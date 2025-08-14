package com.baghdad.viewmodel.continueWatching

import app.cash.turbine.test
import com.baghdad.domain.model.continueWatching.UserWatchedMedia
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
import com.google.common.truth.Truth.assertThat
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class ContinueWatchingViewModelTest {
    val getUserWatchedMediaTvShowGenres: GetUserWatchedMediaTvShowGenresUseCase = mockk()
    val getUserWatchedMediaMovieGenres: GetUserWatchedMediaMovieGenresUseCase = mockk()
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
    private lateinit var viewModel: ContinueWatchingViewModel

    @OptIn(ExperimentalCoroutinesApi::class)
    @BeforeEach
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        viewModel = ContinueWatchingViewModel(
            getUserWatchedMediaTvShowGenres,
            getUserWatchedMediaMovieGenres,
            getUserWatchedMediaMoviesUseCase,
            getUserWatchedMediaTvShowsUseCase,
            getUserWatchedMediaMoviesByGenreUseCase,
            getUserWatchedMediaTvShowsByGenreUseCase,
            isUserLoggedInUseCase,
            getSavedListsUseCase,
            addMovieToSavedListUseCase,
            createSavedListUseCase,
            removeMovieFromSavedListUseCase,
            testDispatcher
        )
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `should navigate back when onBackClick is called`() = runTest {
        viewModel.uiEffect.test {
            viewModel.onBackClick()
            assertThat(awaitItem()).isEqualTo(ContinueWatchingScreenEffect.NavigateBack)
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `should navigate to movie details when onMediaClick  is called and content type is movie`() =
        runTest {
            val movieId = 1L
            val contentType = ContinueWatchingState.ContinueWatchingMovieUiState.ContentType.MOVIE
            viewModel.uiEffect.test {
                viewModel.onMediaClick(movieId, contentType)
                assertThat(awaitItem()).isEqualTo(
                    ContinueWatchingScreenEffect.NavigateToMovieDetails(movieId)
                )
            }
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `should navigate to tv show details when onMediaClick is called and content type is tv show`() =
        runTest {
            val tvShowId = 1L
            val contentType = ContinueWatchingState.ContinueWatchingMovieUiState.ContentType.TV_SHOW

            viewModel.uiEffect.test {
                viewModel.onMediaClick(tvShowId, contentType)

                assertThat(awaitItem()).isEqualTo(
                    ContinueWatchingScreenEffect.NavigateToTvShowDetails(tvShowId)
                )
            }
        }

    @Test
    fun `should navigate to login when onLoginClicked is called`() = runTest {
        viewModel.uiEffect.test {
            viewModel.onLoginClicked()
            assertThat(awaitItem()).isEqualTo(ContinueWatchingScreenEffect.NavigateToLogin)
        }
    }

    private companion object {
        val userWatchedMedia = UserWatchedMedia(
            contentId = 123L,
            genreIds = listOf(1, 2, 3),
            contentImageUrl = "https://example.com/image.jpg",
            contentType = UserWatchedMedia.ContentType.MOVIE,
            isSaved = true,
            listId = 456L,
            userId = 1,
        )

        val genres = listOf(
            Genre(1, "Action"),
            Genre(2, "Comedy"),
            Genre(3, "Drama")
        )
    }

}

