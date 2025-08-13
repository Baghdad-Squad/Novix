package com.baghdad.viewmodel.continueWatching

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
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class ContinueWatchingViewModelTest {
    private lateinit var getUserWatchedMediaTvShowGenres: GetUserWatchedMediaTvShowGenresUseCase
    private lateinit var getUserWatchedMediaMovieGenres: GetUserWatchedMediaMovieGenresUseCase
    private lateinit var getUserWatchedMediaMoviesUseCase: GetUserWatchedMediaMoviesUseCase
    private lateinit var getUserWatchedMediaTvShowsUseCase: GetUserWatchedMediaTvShowsUseCase
    private lateinit var getUserWatchedMediaMoviesByGenreUseCase: GetUserWatchedMediaMoviesByGenreUseCase
    private lateinit var getUserWatchedMediaTvShowsByGenreUseCase: GetUserWatchedMediaTvShowsByGenreUseCase
    private lateinit var isUserLoggedInUseCase: IsUserLoggedInUseCase
    private lateinit var getSavedListsUseCase: GetSavedListsUseCase
    private lateinit var addMovieToSavedListUseCase: AddMovieToSavedListUseCase
    private lateinit var createSavedListUseCase: CreateSavedListUseCase
    private lateinit var removeMovieFromSavedListUseCase: RemoveMovieFromSavedListUseCase
    private val testDispatcher = StandardTestDispatcher()
    private lateinit var viewModel: ContinueWatchingViewModel

    @OptIn(ExperimentalCoroutinesApi::class)
    @BeforeEach
    fun setup() {
        Dispatchers.setMain(testDispatcher)

        getUserWatchedMediaTvShowGenres = mockk()
        getUserWatchedMediaMovieGenres = mockk()
        getUserWatchedMediaMoviesUseCase = mockk()
        getUserWatchedMediaTvShowsUseCase = mockk()
        getUserWatchedMediaMoviesByGenreUseCase = mockk()
        getUserWatchedMediaTvShowsByGenreUseCase = mockk()
        isUserLoggedInUseCase = mockk()
        getSavedListsUseCase = mockk()
        addMovieToSavedListUseCase = mockk()
        createSavedListUseCase = mockk()
        removeMovieFromSavedListUseCase = mockk()
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
        // Given
        var receivedEffect: ContinueWatchingScreenEffect? = null
        val job: Job = launch {
            viewModel.uiEffect.collect { effect ->
                receivedEffect = effect
            }
        }
        // When
        viewModel.onBackClick()
        advanceUntilIdle()

        // Then
        assertThat(receivedEffect).isEqualTo(ContinueWatchingScreenEffect.NavigateBack)
        job.cancel()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `should navigate to movie details when onMediaClick  is called and content type is movie`() =
        runTest {
            // Given
            val movieId = 123L
            val contentType = ContinueWatchingState.ContinueWatchingMovieUiState.ContentType.MOVIE
            var receivedEffect: ContinueWatchingScreenEffect? = null
            val job: Job = launch {
                viewModel.uiEffect.collect { effect ->
                    receivedEffect = effect
                }
            }
            // When
            viewModel.onMediaClick(movieId, contentType)
            advanceUntilIdle()

            // Then
            assertThat(receivedEffect).isEqualTo(
                ContinueWatchingScreenEffect.NavigateToMovieDetails(
                    movieId
                )
            )
            job.cancel()
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `should navigate to tv show details when onMediaClick is called and content type is tv show`() =
        runTest {
            // Given
            val tvShowId = 1L
            val contentType = ContinueWatchingState.ContinueWatchingMovieUiState.ContentType.TV_SHOW
            var receivedEffect: ContinueWatchingScreenEffect? = null
            val job: Job = launch {
                viewModel.uiEffect.collect { effect ->
                    receivedEffect = effect
                }
            }
            // When
            viewModel.onMediaClick(tvShowId, contentType)
            advanceUntilIdle()

            // Then
            assertThat(receivedEffect).isEqualTo(
                ContinueWatchingScreenEffect.NavigateToTvShowDetails(
                    tvShowId
                )
            )
            job.cancel()
        }

    @Test
    fun `should navigate to login when onLoginClick is called`() = runTest {
        // Given
        var receivedEffect: ContinueWatchingScreenEffect? = null
        val job: Job = launch {
            viewModel.uiEffect.collect { effect ->
                receivedEffect = effect
            }
        }

        // When
        viewModel.onLoginClicked()
        advanceUntilIdle()

        // Then
        assertThat(receivedEffect).isEqualTo(ContinueWatchingScreenEffect.NavigateToLogin)
        job.cancel()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `should get genres when onGenreClick is called`() = runTest {
        // Given
        val genreId = 1L
        var receivedState: ContinueWatchingState? = null
        val job: Job = launch {
            viewModel.uiState.collect { state ->
                receivedState = state
            }
        }
        coEvery { getUserWatchedMediaMovieGenres.invoke() } returns flowOf(genres)
        // When
        viewModel.onGenreClick(genreId)
        advanceUntilIdle()
        // Then
        println(viewModel.uiState.value.genres)
        assertThat(receivedState?.selectedMovieGenreId).isEqualTo(genreId)
        job.cancel()
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

