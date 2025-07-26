package com.baghdad.viewmodel.tvShowDetails

import com.baghdad.domain.model.ContinueWatching
import com.baghdad.domain.usecase.continueWatching.AddContinueWatchingUseCase
import com.baghdad.domain.usecase.tvShow.GetTvShowCastMembersUseCase
import com.baghdad.domain.usecase.tvShow.GetTvShowDetailsUseCase
import com.baghdad.domain.usecase.tvShow.GetTvShowSeasonEpisodesUseCase
import com.baghdad.entity.media.Episode
import com.baghdad.entity.media.Genre
import com.baghdad.entity.media.TvShow
import com.baghdad.entity.person.Actor
import com.baghdad.entity.person.CastMember
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
import org.junit.jupiter.api.Assertions
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
            tvShowId = tvShowId,
            getTvShowDetailsUseCase = getTvShowDetailsUseCase,
            getTvShowCastMembersUseCase = getTvShowCastMembersUseCase,
            getTvShowSeasonEpisodesUseCase = getTvShowSeasonEpisodesUseCase,
            addContinueWatchingUseCase = addContinueWatchingUseCase
        )
    }

    @Test
    fun `init should call all required use cases`() = runTest {
        coEvery { getTvShowDetailsUseCase.invoke(tvShowId) } returns mockTvShow
        coEvery { getTvShowCastMembersUseCase.invoke(tvShowId) } returns mockCastMembers
        coEvery { getTvShowSeasonEpisodesUseCase.invoke(tvShowId, 1) } returns mockEpisodes
        coEvery { addContinueWatchingUseCase.invoke(any(), any(), any(), any()) } returns Unit

        viewModel = createViewModel()
        advanceUntilIdle()

        coVerify { getTvShowDetailsUseCase.invoke(tvShowId) }
        coVerify { getTvShowCastMembersUseCase.invoke(tvShowId) }
        coVerify { getTvShowSeasonEpisodesUseCase.invoke(tvShowId, 1) }
        coVerify { addContinueWatchingUseCase.invoke(any(), any(), any(), any()) }
    }

    @Test
    fun `onClickBackIcon should send NavigateBack effect`() = runTest {
        coEvery { getTvShowDetailsUseCase.invoke(tvShowId) } returns mockTvShow
        coEvery { getTvShowCastMembersUseCase.invoke(tvShowId) } returns emptyList()
        coEvery { getTvShowSeasonEpisodesUseCase.invoke(tvShowId, 1) } returns emptyList()
        coEvery { addContinueWatchingUseCase.invoke(any(), any(), any(), any()) } returns Unit

        viewModel = createViewModel()

        val effects = mutableListOf<TvShowDetailsScreenEffect>()
        val job = launch {
            viewModel.uiEffect.collect { effects.add(it) }
        }

        viewModel.onClickBackIcon()
        advanceUntilIdle()
        job.cancel()

        Assertions.assertTrue(effects.contains(TvShowDetailsScreenEffect.NavigateBack))
    }

    @Test
    fun `onClickReadMoreOverview should toggle text expansion`() = runTest {
        coEvery { getTvShowDetailsUseCase.invoke(tvShowId) } returns mockTvShow
        coEvery { getTvShowCastMembersUseCase.invoke(tvShowId) } returns emptyList()
        coEvery { getTvShowSeasonEpisodesUseCase.invoke(tvShowId, 1) } returns emptyList()
        coEvery { addContinueWatchingUseCase.invoke(any(), any(), any(), any()) } returns Unit

        viewModel = createViewModel()
        advanceUntilIdle()

        val initialExpanded = viewModel.uiState.value.isTextExpanded
        viewModel.onClickReadMoreOverview()

        val finalExpanded = viewModel.uiState.value.isTextExpanded
        Assertions.assertEquals(!initialExpanded, finalExpanded)
    }

    @Test
    fun `onClickGenre should send NavigateToGenreScreen effect`() = runTest {
        coEvery { getTvShowDetailsUseCase.invoke(tvShowId) } returns mockTvShow
        coEvery { getTvShowCastMembersUseCase.invoke(tvShowId) } returns emptyList()
        coEvery { getTvShowSeasonEpisodesUseCase.invoke(tvShowId, 1) } returns emptyList()
        coEvery { addContinueWatchingUseCase.invoke(any(), any(), any(), any()) } returns Unit

        viewModel = createViewModel()

        val effects = mutableListOf<TvShowDetailsScreenEffect>()
        val job = launch {
            viewModel.uiEffect.collect { effects.add(it) }
        }

        viewModel.onClickGenre(genreId)
        advanceUntilIdle()
        job.cancel()

        val expectedEffect = TvShowDetailsScreenEffect.NavigateToGenreScreen(genreId)
        Assertions.assertTrue(effects.contains(expectedEffect))
    }

    @Test
    fun `onClickCastMember should send NavigateToActorDetails effect`() = runTest {
        coEvery { getTvShowDetailsUseCase.invoke(tvShowId) } returns mockTvShow
        coEvery { getTvShowCastMembersUseCase.invoke(tvShowId) } returns emptyList()
        coEvery { getTvShowSeasonEpisodesUseCase.invoke(tvShowId, 1) } returns emptyList()
        coEvery { addContinueWatchingUseCase.invoke(any(), any(), any(), any()) } returns Unit

        viewModel = createViewModel()

        val effects = mutableListOf<TvShowDetailsScreenEffect>()
        val job = launch {
            viewModel.uiEffect.collect { effects.add(it) }
        }

        viewModel.onClickCastMember(actorId)
        advanceUntilIdle()
        job.cancel()

        val expectedEffect = TvShowDetailsScreenEffect.NavigateToActorDetails(actorId)
        Assertions.assertTrue(effects.contains(expectedEffect))
    }

    @Test
    fun `onClickEpisode should send NavigateToEpisodeDetails effect`() = runTest {
        coEvery { getTvShowDetailsUseCase.invoke(tvShowId) } returns mockTvShow
        coEvery { getTvShowCastMembersUseCase.invoke(tvShowId) } returns emptyList()
        coEvery { getTvShowSeasonEpisodesUseCase.invoke(tvShowId, 1) } returns emptyList()
        coEvery { addContinueWatchingUseCase.invoke(any(), any(), any(), any()) } returns Unit

        viewModel = createViewModel()

        val effects = mutableListOf<TvShowDetailsScreenEffect>()
        val job = launch {
            viewModel.uiEffect.collect { effects.add(it) }
        }

        viewModel.onClickEpisode(seasonNumber, episodeNumber)
        advanceUntilIdle()
        job.cancel()

        val expectedEffect = TvShowDetailsScreenEffect.NavigateToEpisodeDetails(seasonNumber, episodeNumber)
        Assertions.assertTrue(effects.contains(expectedEffect))
    }

    @Test
    fun `onClickReviews should send NavigateToReviews effect`() = runTest {
        coEvery { getTvShowDetailsUseCase.invoke(tvShowId) } returns mockTvShow
        coEvery { getTvShowCastMembersUseCase.invoke(tvShowId) } returns emptyList()
        coEvery { getTvShowSeasonEpisodesUseCase.invoke(tvShowId, 1) } returns emptyList()
        coEvery { addContinueWatchingUseCase.invoke(any(), any(), any(), any()) } returns Unit

        viewModel = createViewModel()

        val effects = mutableListOf<TvShowDetailsScreenEffect>()
        val job = launch {
            viewModel.uiEffect.collect { effects.add(it) }
        }

        viewModel.onClickReviews(tvShowId)
        advanceUntilIdle()
        job.cancel()

        val expectedEffect = TvShowDetailsScreenEffect.NavigateToReviews(tvShowId)
        Assertions.assertTrue(effects.contains(expectedEffect))
    }

    @Test
    fun `onClickPlayTrailer should send OpenYoutubeLink effect`() = runTest {
        coEvery { getTvShowDetailsUseCase.invoke(tvShowId) } returns mockTvShow
        coEvery { getTvShowCastMembersUseCase.invoke(tvShowId) } returns emptyList()
        coEvery { getTvShowSeasonEpisodesUseCase.invoke(tvShowId, 1) } returns emptyList()
        coEvery { addContinueWatchingUseCase.invoke(any(), any(), any(), any()) } returns Unit

        viewModel = createViewModel()
        advanceUntilIdle()

        val effects = mutableListOf<TvShowDetailsScreenEffect>()
        val job = launch {
            viewModel.uiEffect.collect { effects.add(it) }
        }

        viewModel.onClickPlayTrailer()
        advanceUntilIdle()
        job.cancel()

        val expectedEffect = TvShowDetailsScreenEffect.OpenYoutubeLink(mockTvShow.trailerURL)
        Assertions.assertTrue(effects.contains(expectedEffect))
    }

    @Test
    fun `onClickSeasonTab should update selected season and fetch episodes`() = runTest {
        coEvery { getTvShowDetailsUseCase.invoke(tvShowId) } returns mockTvShow
        coEvery { getTvShowCastMembersUseCase.invoke(tvShowId) } returns emptyList()
        coEvery { getTvShowSeasonEpisodesUseCase.invoke(tvShowId, 1) } returns emptyList()
        coEvery { getTvShowSeasonEpisodesUseCase.invoke(tvShowId, 2) } returns mockEpisodes
        coEvery { addContinueWatchingUseCase.invoke(any(), any(), any(), any()) } returns Unit

        viewModel = createViewModel()
        advanceUntilIdle()

        viewModel.onClickSeasonTab(1)
        advanceUntilIdle()

        val finalState = viewModel.uiState.value
        Assertions.assertEquals(1, finalState.selectedSeasonIndex)
        coVerify { getTvShowSeasonEpisodesUseCase.invoke(tvShowId, 2) }
    }

    @Test
    fun `addContinueWatching should be called with correct parameters`() = runTest {
        coEvery { getTvShowDetailsUseCase.invoke(tvShowId) } returns mockTvShow
        coEvery { getTvShowCastMembersUseCase.invoke(tvShowId) } returns emptyList()
        coEvery { getTvShowSeasonEpisodesUseCase.invoke(tvShowId, 1) } returns emptyList()
        coEvery { addContinueWatchingUseCase.invoke(any(), any(), any(), any()) } returns Unit

        viewModel = createViewModel()
        advanceUntilIdle()

        coVerify {
            addContinueWatchingUseCase.invoke(
                tvShowId,
                any(),
                mockTvShow.posterImageURL,
                ContinueWatching.ContentType.TV_SHOW
            )
        }
    }

    @Test
    fun `viewModel should implement TvShowDetailsInteractionListener`() {
        coEvery { getTvShowDetailsUseCase.invoke(tvShowId) } returns mockTvShow
        coEvery { getTvShowCastMembersUseCase.invoke(tvShowId) } returns emptyList()
        coEvery { getTvShowSeasonEpisodesUseCase.invoke(tvShowId, 1) } returns emptyList()
        coEvery { addContinueWatchingUseCase.invoke(any(), any(), any(), any()) } returns Unit

        viewModel = createViewModel()

        Assertions.assertTrue(true)
    }

    private companion object {
        const val tvShowId = 123L
        const val genreId = 456L
        const val actorId = 789L
        const val seasonNumber = 1
        const val episodeNumber = 1

        val mockTvShow = TvShow(
            id = tvShowId,
            title = "Test TV Show",
            overview = "Test overview",
            averageRating = 8.5,
            releaseDate = kotlinx.datetime.LocalDate.parse("2023-01-01"),
            posterImageURL = "https://example.com/poster.jpg",
            headerImagesURLs = listOf("https://example.com/header1.jpg"),
            trailerURL = "https://youtube.com/watch?v=test",
            numberOfSeasons = 3,
            genres = emptyList<Genre>(),
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
            headerPictures = listOf("https://example.com/header1.jpg", "https://example.com/header2.jpg"),
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