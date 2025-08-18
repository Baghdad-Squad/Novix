package com.baghdad.viewmodel.tvShowDetails

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.baghdad.domain.exception.NoInternetException
import com.baghdad.domain.usecase.login.IsUserLoggedInUseCase
import com.baghdad.domain.usecase.tvShow.AddTvShowRateUseCase
import com.baghdad.domain.usecase.tvShow.GetTvShowAccountStatesUseCase
import com.baghdad.domain.usecase.tvShow.GetTvShowCastMembersUseCase
import com.baghdad.domain.usecase.tvShow.GetTvShowDetailsUseCase
import com.baghdad.domain.usecase.tvShow.GetTvShowSeasonEpisodesUseCase
import com.baghdad.domain.usecase.userWatchedMedia.AddUserWatchedMediaUseCase
import com.baghdad.entity.media.Episode
import com.baghdad.entity.media.TvShow
import com.baghdad.entity.person.Actor
import com.baghdad.entity.person.CastMember
import com.baghdad.viewmodel.errorStates.BaseSnackBarMessage
import com.baghdad.viewmodel.shared.BottomSheetType
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
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
class TvShowDetailsViewModelTest {

    private val getTvShowDetailsUseCase = mockk<GetTvShowDetailsUseCase>()
    private val getTvShowCastMembersUseCase = mockk<GetTvShowCastMembersUseCase>()
    private val getTvShowSeasonEpisodesUseCase = mockk<GetTvShowSeasonEpisodesUseCase>()
    private val addUserWatchedMediaUseCase = mockk<AddUserWatchedMediaUseCase>()
    private val getTvShowAccountStatesUseCase = mockk<GetTvShowAccountStatesUseCase>()
    private val addTvShowRateUseCase = mockk<AddTvShowRateUseCase>()
    private val isUserLoggedInUseCase = mockk<IsUserLoggedInUseCase>()

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var viewModel: TvShowDetailsViewModel

    private val tvShowId = 123L
    private val rating = 8

    @BeforeEach
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        setupHappyPathMocks()
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun setupHappyPathMocks() {
        coEvery { getTvShowDetailsUseCase(any()) } returns TestData.createMockTvShow()
        coEvery { getTvShowCastMembersUseCase(any()) } returns TestData.createMockCastMembers()
        coEvery { getTvShowSeasonEpisodesUseCase(any(), any()) } returns TestData.createMockEpisodes()
        coEvery { addUserWatchedMediaUseCase(any(), any(), any(), any()) } returns Unit
        coEvery { getTvShowAccountStatesUseCase(any()) } returns true
        coEvery { addTvShowRateUseCase(any(), any()) } returns Unit
        coEvery { isUserLoggedInUseCase() } returns true
    }

    @Test
    fun `when viewModel is initialized, should load all tv show data`() = runTest {
        viewModel = createViewModel()
        advanceUntilIdle()

        viewModel.uiState.test {
            val state = awaitItem()
            assertThat(state.tvShowInfo).isNotNull()
            assertThat(state.castMembers).isNotEmpty()
            assertThat(state.episodes).isNotEmpty()
        }
    }

    @Test
    fun `when read more overview is clicked, should toggle text expansion`() = runTest {
        viewModel = createViewModel()
        val initialState = viewModel.uiState.value.isTextExpanded

        viewModel.onClickReadMoreOverview()
        advanceUntilIdle()

        viewModel.uiState.test {
            val state = awaitItem()
            assertThat(state.isTextExpanded).isEqualTo(!initialState)
        }
    }

    @Test
    fun `when season tab is clicked, should update selected season and fetch episodes`() = runTest {
        viewModel = createViewModel()
        val seasonIndex = 1

        viewModel.onClickSeasonTab(seasonIndex)
        advanceUntilIdle()

        viewModel.uiState.test {
            val state = awaitItem()
            assertThat(state.selectedSeasonIndex).isEqualTo(seasonIndex)
        }
        coVerify { getTvShowSeasonEpisodesUseCase.invoke(tvShowId, seasonIndex + 1) }
    }

    @Test
    fun `when star button is clicked and user is logged in, should show rating bottom sheet`() = runTest {
        viewModel = createViewModel()

        viewModel.onClickStarButton()
        advanceUntilIdle()

        viewModel.uiState.test {
            val state = awaitItem()
            assertThat(state.ratingStatus.isBottomSheetVisible).isTrue()
            assertThat(state.ratingStatus.bottomSheetType).isEqualTo(BottomSheetType.ShowRating)
        }
    }

    @Test
    fun `when star button is clicked and user is not logged in, should show login bottom sheet`() = runTest {
        coEvery { isUserLoggedInUseCase() } returns false
        viewModel = createViewModel()

        viewModel.onClickStarButton()
        advanceUntilIdle()

        viewModel.uiState.test {
            val state = awaitItem()
            assertThat(state.ratingStatus.isBottomSheetVisible).isTrue()
            assertThat(state.ratingStatus.bottomSheetType).isEqualTo(BottomSheetType.RequireLogin)
        }
    }

    @Test
    fun `when rating is submitted, should hide bottom sheet and show success`() = runTest {
        viewModel = createViewModel()

        viewModel.onRatingChanged(rating)
        viewModel.onClickSubmitRating(rating)
        advanceUntilIdle()

        viewModel.uiState.test {
            val state = awaitItem()
            assertThat(state.ratingStatus.isBottomSheetVisible).isFalse()
            assertThat(state.isRated).isTrue()
        }

        viewModel.snackBarState.test {
            val message = awaitItem().message
            assertThat(message).isEqualTo(BaseSnackBarMessage.ItemRateSuccessfully)
        }
    }

    @Test
    fun `when rating bottom sheet is dismissed, should reset rating`() = runTest {
        viewModel = createViewModel()

        viewModel.onRatingChanged(rating)
        viewModel.onDismissRatingBottomSheet()
        advanceUntilIdle()

        viewModel.uiState.test {
            val state = awaitItem()
            assertThat(state.tvShowInfo.userRating).isEqualTo(0)
        }
    }

    @Test
    fun `when snackbar action is clicked, should retry loading data`() = runTest {
        coEvery { getTvShowDetailsUseCase(any()) } throws NoInternetException() andThen
                TestData.createMockTvShow()

        viewModel = createViewModel()
        advanceUntilIdle()

        viewModel.onSnackBarActionLabelClick()
        advanceUntilIdle()

        coVerify(exactly = 2) { getTvShowDetailsUseCase.invoke(tvShowId) }
    }


    private fun createViewModel(): TvShowDetailsViewModel {
        val savedStateHandle = SavedStateHandle(mapOf("tvShowId" to tvShowId))
        return TvShowDetailsViewModel(
            savedStateHandle = savedStateHandle,
            getTvShowDetailsUseCase = getTvShowDetailsUseCase,
            getTvShowCastMembersUseCase = getTvShowCastMembersUseCase,
            getTvShowSeasonEpisodesUseCase = getTvShowSeasonEpisodesUseCase,
            addUserWatchedMediaUseCase = addUserWatchedMediaUseCase,
            getTvShowAccountStatesUseCase = getTvShowAccountStatesUseCase,
            addTvShowRateUseCase = addTvShowRateUseCase,
            isUserLoggedInUseCase = isUserLoggedInUseCase,
            ioDispatcher = testDispatcher
        )
    }

    private object TestData {
        const val TEST_TV_SHOW_TITLE = "Test TV Show"
        const val TEST_TV_SHOW_OVERVIEW = "Test overview"
        const val TEST_TV_SHOW_POSTER_URL = "https://example.com/poster.jpg"
        const val TEST_TV_SHOW_TRAILER_URL = "https://youtube.com/watch?v=test"
        const val TEST_ACTOR_NAME = "John Doe"
        const val TEST_ACTOR_PROFILE_URL = "/john_doe.jpg"
        const val TEST_EPISODE_TITLE = "Test Episode"

        fun createMockTvShow() = TvShow(
            id = 123L,
            title = TEST_TV_SHOW_TITLE,
            overview = TEST_TV_SHOW_OVERVIEW,
            averageRating = 8.5,
            releaseDate = LocalDate.parse("2023-01-01"),
            posterImageURL = TEST_TV_SHOW_POSTER_URL,
            headerImagesURLs = listOf("https://example.com/header1.jpg"),
            trailerURL = TEST_TV_SHOW_TRAILER_URL,
            numberOfSeasons = 3,
            genres = emptyList(),
            userRating = null
        )

        fun createMockCastMembers() = listOf(
            CastMember(
                actor = Actor(
                    id = 1L,
                    name = TEST_ACTOR_NAME,
                    profilePictureURL = TEST_ACTOR_PROFILE_URL,
                    birthDate = LocalDate.parse("1980-01-01"),
                    placeOfBirth = "New York, USA",
                    deathDate = null,
                    biography = "Famous actor",
                    headerPictures = listOf("/header1.jpg"),
                    department = "Acting"
                ),
                characterName = "Hero"
            )
        )

        fun createMockEpisodes() = listOf(
            Episode(
                id = 1L,
                title = TEST_EPISODE_TITLE,
                overview = "Test episode overview",
                episodeNumber = 1,
                rating = 8.0,
                duration = "45 minutes",
                releasedDate = LocalDate.parse("2023-01-01"),
                trailerUrl = "https://youtube.com/watch?v=test_episode",
                currentSeason = 1,
                userRating = null,
                genres = emptyList(),
                headerPictures = listOf("https://example.com/episode_header.jpg"),
            )
        )
    }
}