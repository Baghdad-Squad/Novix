package com.baghdad.viewmodel.tvShowDetails

import com.baghdad.domain.model.ContinueWatching
import com.baghdad.domain.usecase.continueWatching.AddContinueWatchingUseCase
import com.baghdad.domain.usecase.tvShow.GetTvShowCastMembersUseCase
import com.baghdad.domain.usecase.tvShow.GetTvShowDetailsUseCase
import com.baghdad.domain.usecase.tvShow.GetTvShowSeasonEpisodesUseCase
import com.baghdad.entity.media.Episode
import com.baghdad.entity.media.TvShow
import com.baghdad.entity.person.Actor
import com.baghdad.entity.person.CastMember
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@ExperimentalCoroutinesApi
class TvShowDetailsViewModelTest {

    private lateinit var viewModel: TvShowDetailsViewModel
    private lateinit var getTvShowDetailsUseCase: GetTvShowDetailsUseCase
    private lateinit var getTvShowCastMembersUseCase: GetTvShowCastMembersUseCase
    private lateinit var getTvShowSeasonEpisodesUseCase: GetTvShowSeasonEpisodesUseCase
    private lateinit var addContinueWatchingUseCase: AddContinueWatchingUseCase
    private val testDispatcher = StandardTestDispatcher()

    @BeforeEach
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        getTvShowDetailsUseCase = mockk()
        getTvShowCastMembersUseCase = mockk()
        getTvShowSeasonEpisodesUseCase = mockk()
        addContinueWatchingUseCase = mockk()
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun createViewModel(): TvShowDetailsViewModel {
        return TvShowDetailsViewModel(
            tvShowId = TV_SHOW_ID,
            getTvShowDetailsUseCase = getTvShowDetailsUseCase,
            getTvShowCastMembersUseCase = getTvShowCastMembersUseCase,
            getTvShowSeasonEpisodesUseCase = getTvShowSeasonEpisodesUseCase,
            addContinueWatchingUseCase = addContinueWatchingUseCase
        )
    }

    @Test
    fun `init() should call all required use cases when viewModel is created`() = runTest {
        coEvery { getTvShowDetailsUseCase.invoke(TV_SHOW_ID) } returns mockTvShow
        coEvery { getTvShowCastMembersUseCase.invoke(TV_SHOW_ID) } returns mockCastMembers
        coEvery { getTvShowSeasonEpisodesUseCase.invoke(TV_SHOW_ID, 1) } returns mockEpisodes
        coEvery { addContinueWatchingUseCase.invoke(any(), any(), any(), any()) } returns Unit

        viewModel = createViewModel()
        advanceUntilIdle()

        coVerify { getTvShowDetailsUseCase.invoke(TV_SHOW_ID) }
        coVerify { getTvShowCastMembersUseCase.invoke(TV_SHOW_ID) }
        coVerify { getTvShowSeasonEpisodesUseCase.invoke(TV_SHOW_ID, 1) }
        coVerify { addContinueWatchingUseCase.invoke(any(), any(), any(), any()) }
    }

    @Test
    fun `onClickBackIcon() should send NavigateBack effect when back icon is clicked`() = runTest {
        coEvery { getTvShowDetailsUseCase.invoke(TV_SHOW_ID) } returns mockTvShow
        coEvery { getTvShowCastMembersUseCase.invoke(TV_SHOW_ID) } returns emptyList()
        coEvery { getTvShowSeasonEpisodesUseCase.invoke(TV_SHOW_ID, 1) } returns emptyList()
        coEvery { addContinueWatchingUseCase.invoke(any(), any(), any(), any()) } returns Unit

        viewModel = createViewModel()

        val effects = mutableListOf<TvShowDetailsScreenEffect>()
        val job = launch {
            viewModel.uiEffect.collect { effects.add(it) }
        }

        viewModel.onClickBackIcon()
        advanceUntilIdle()
        job.cancel()

        assertThat(effects).contains(TvShowDetailsScreenEffect.NavigateBack)
    }

    @Test
    fun `onClickReadMoreOverview() should toggle isTextExpanded when clicked`() = runTest {
        coEvery { getTvShowDetailsUseCase.invoke(TV_SHOW_ID) } returns mockTvShow
        coEvery { getTvShowCastMembersUseCase.invoke(TV_SHOW_ID) } returns emptyList()
        coEvery { getTvShowSeasonEpisodesUseCase.invoke(TV_SHOW_ID, 1) } returns emptyList()
        coEvery { addContinueWatchingUseCase.invoke(any(), any(), any(), any()) } returns Unit

        viewModel = createViewModel()
        advanceUntilIdle()

        val initialExpanded = viewModel.uiState.value.isTextExpanded
        viewModel.onClickReadMoreOverview()

        val finalExpanded = viewModel.uiState.value.isTextExpanded
        assertThat(finalExpanded).isEqualTo(!initialExpanded)
    }

    @Test
    fun `onClickGenre() should send NavigateToGenreScreen effect with correct genreId when genre is clicked`() =
        runTest {
            coEvery { getTvShowDetailsUseCase.invoke(TV_SHOW_ID) } returns mockTvShow
            coEvery { getTvShowCastMembersUseCase.invoke(TV_SHOW_ID) } returns emptyList()
            coEvery { getTvShowSeasonEpisodesUseCase.invoke(TV_SHOW_ID, 1) } returns emptyList()
            coEvery { addContinueWatchingUseCase.invoke(any(), any(), any(), any()) } returns Unit

            viewModel = createViewModel()

            val effects = mutableListOf<TvShowDetailsScreenEffect>()
            val job = launch {
                viewModel.uiEffect.collect { effects.add(it) }
            }

            viewModel.onClickGenre(GENRE_ID)
            advanceUntilIdle()
            job.cancel()

            val expectedEffect = TvShowDetailsScreenEffect.NavigateToGenreScreen(GENRE_ID)
            assertThat(effects).contains(expectedEffect)
        }

    @Test
    fun `onClickCastMember() should send NavigateToActorDetails effect with correct actorId when cast member is clicked`() =
        runTest {
            coEvery { getTvShowDetailsUseCase.invoke(TV_SHOW_ID) } returns mockTvShow
            coEvery { getTvShowCastMembersUseCase.invoke(TV_SHOW_ID) } returns emptyList()
            coEvery { getTvShowSeasonEpisodesUseCase.invoke(TV_SHOW_ID, 1) } returns emptyList()
            coEvery { addContinueWatchingUseCase.invoke(any(), any(), any(), any()) } returns Unit

            viewModel = createViewModel()

            val effects = mutableListOf<TvShowDetailsScreenEffect>()
            val job = launch {
                viewModel.uiEffect.collect { effects.add(it) }
            }

            viewModel.onClickCastMember(ACTOR_ID)
            advanceUntilIdle()
            job.cancel()

            val expectedEffect = TvShowDetailsScreenEffect.NavigateToActorDetails(ACTOR_ID)
            assertThat(effects).contains(expectedEffect)
        }

    @Test
    fun `onClickEpisode() should send NavigateToEpisodeDetails effect with correct season and episode numbers when episode is clicked`() =
        runTest {
            coEvery { getTvShowDetailsUseCase.invoke(TV_SHOW_ID) } returns mockTvShow
            coEvery { getTvShowCastMembersUseCase.invoke(TV_SHOW_ID) } returns emptyList()
            coEvery { getTvShowSeasonEpisodesUseCase.invoke(TV_SHOW_ID, 1) } returns emptyList()
            coEvery { addContinueWatchingUseCase.invoke(any(), any(), any(), any()) } returns Unit

            viewModel = createViewModel()

            val effects = mutableListOf<TvShowDetailsScreenEffect>()
            val job = launch {
                viewModel.uiEffect.collect { effects.add(it) }
            }

            viewModel.onClickEpisode(SEASON_NUMBER, EPISODE_NUMBER)
            advanceUntilIdle()
            job.cancel()

            val expectedEffect =
                TvShowDetailsScreenEffect.NavigateToEpisodeDetails(SEASON_NUMBER, EPISODE_NUMBER)
            assertThat(effects).contains(expectedEffect)
        }

    @Test
    fun `onClickReviews() should send NavigateToReviews effect with correct tvShowId when reviews are clicked`() =
        runTest {
            coEvery { getTvShowDetailsUseCase.invoke(TV_SHOW_ID) } returns mockTvShow
            coEvery { getTvShowCastMembersUseCase.invoke(TV_SHOW_ID) } returns emptyList()
            coEvery { getTvShowSeasonEpisodesUseCase.invoke(TV_SHOW_ID, 1) } returns emptyList()
            coEvery { addContinueWatchingUseCase.invoke(any(), any(), any(), any()) } returns Unit

            viewModel = createViewModel()

            val effects = mutableListOf<TvShowDetailsScreenEffect>()
            val job = launch {
                viewModel.uiEffect.collect { effects.add(it) }
            }

            viewModel.onClickReviews(TV_SHOW_ID)
            advanceUntilIdle()
            job.cancel()

            val expectedEffect = TvShowDetailsScreenEffect.NavigateToReviews(TV_SHOW_ID)
            assertThat(effects).contains(expectedEffect)
        }

    @Test
    fun `viewModel should initialize with non-null uiState when created`() = runTest {
        coEvery { getTvShowDetailsUseCase.invoke(TV_SHOW_ID) } returns mockTvShow
        coEvery { getTvShowCastMembersUseCase.invoke(TV_SHOW_ID) } returns emptyList()
        coEvery { getTvShowSeasonEpisodesUseCase.invoke(TV_SHOW_ID, 1) } returns emptyList()
        coEvery { addContinueWatchingUseCase.invoke(any(), any(), any(), any()) } returns Unit

        viewModel = createViewModel()
        advanceUntilIdle()

        coVerify { getTvShowDetailsUseCase.invoke(TV_SHOW_ID) }
        coVerify { getTvShowCastMembersUseCase.invoke(TV_SHOW_ID) }

        assertThat(viewModel.uiState.value).isNotNull()
    }

    @Test
    fun `onClickSeasonTab() should update selectedSeasonIndex and fetch episodes when season tab is clicked`() =
        runTest {
            coEvery { getTvShowDetailsUseCase.invoke(TV_SHOW_ID) } returns mockTvShow
            coEvery { getTvShowCastMembersUseCase.invoke(TV_SHOW_ID) } returns emptyList()
            coEvery { getTvShowSeasonEpisodesUseCase.invoke(TV_SHOW_ID, 1) } returns emptyList()
            coEvery { getTvShowSeasonEpisodesUseCase.invoke(TV_SHOW_ID, 2) } returns mockEpisodes
            coEvery { addContinueWatchingUseCase.invoke(any(), any(), any(), any()) } returns Unit

            viewModel = createViewModel()
            advanceUntilIdle()

            viewModel.onClickSeasonTab(1)
            advanceUntilIdle()

            val finalState = viewModel.uiState.value
            assertThat(finalState.selectedSeasonIndex).isEqualTo(1)
            coVerify { getTvShowSeasonEpisodesUseCase.invoke(TV_SHOW_ID, 2) }
        }

    @Test
    fun `addContinueWatchingUseCase() should be called with correct parameters when viewModel is initialized`() =
        runTest {
            coEvery { getTvShowDetailsUseCase.invoke(TV_SHOW_ID) } returns mockTvShow
            coEvery { getTvShowCastMembersUseCase.invoke(TV_SHOW_ID) } returns emptyList()
            coEvery { getTvShowSeasonEpisodesUseCase.invoke(TV_SHOW_ID, 1) } returns emptyList()
            coEvery { addContinueWatchingUseCase.invoke(any(), any(), any(), any()) } returns Unit

            viewModel = createViewModel()
            advanceUntilIdle()

            coVerify {
                addContinueWatchingUseCase.invoke(
                    TV_SHOW_ID,
                    any(),
                    any(),
                    ContinueWatching.ContentType.TV_SHOW
                )
            }
        }

    private companion object {
        const val TV_SHOW_ID = 123L
        const val GENRE_ID = 456L
        const val ACTOR_ID = 789L
        const val SEASON_NUMBER = 1
        const val EPISODE_NUMBER = 1

        val mockTvShow = TvShow(
            id = TV_SHOW_ID,
            title = "Test TV Show",
            overview = "Test overview",
            averageRating = 8.5,
            releaseDate = kotlinx.datetime.LocalDate.parse("2023-01-01"),
            posterImageURL = "https://example.com/poster.jpg",
            headerImagesURLs = listOf("https://example.com/header1.jpg"),
            trailerURL = "https://youtube.com/watch?v=test",
            numberOfSeasons = 3,
            genres = emptyList(),
            userRating = null
        )
        val mockActor = Actor(
            id = 1L,
            name = "Test Actor",
            profilePictureURL = "https://example.com/profile.jpg",
            birthDate = kotlinx.datetime.LocalDate.parse("1990-01-01"),
            placeOfBirth = "Baghdad",
            deathDate = null,
            biography = "This is a test biography.",
            headerPictures = listOf(
                "https://example.com/header1.jpg",
                "https://example.com/header2.jpg"
            ),
            department = "Acting"
        )

        val mockCastMembers = listOf(
            CastMember(
                actor = mockActor,
                characterName = "Test Character"
            )
        )

        val mockEpisodes = listOf(
            Episode(
                id = 1L,
                title = "Test Episode",
                episodeNumber = 1,
                rating = 8.0,
                duration = "45 min",
                releasedDate = kotlinx.datetime.LocalDate.parse("2023-01-01"),
                trailerUrl = "https://youtube.com/watch?v=testEpisode",
                currentSeason = 1,
                overview = "Test episode overview",
                genres = emptyList(),
                headerPictures = listOf("https://example.com/ep-header.jpg")
            )
        )
    }
}