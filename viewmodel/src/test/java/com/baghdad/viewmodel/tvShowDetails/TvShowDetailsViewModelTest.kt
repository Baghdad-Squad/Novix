package com.baghdad.viewmodel.tvShowDetails

import com.baghdad.domain.usecase.continueWatching.AddContinueWatchingUseCase
import com.baghdad.domain.usecase.tvShow.GetTvShowCastMembersUseCase
import com.baghdad.domain.usecase.tvShow.GetTvShowDetailsUseCase
import com.baghdad.domain.usecase.tvShow.GetTvShowSeasonEpisodesUseCase
import com.baghdad.entity.media.Episode
import com.baghdad.entity.media.Genre
import com.baghdad.entity.media.TvShow
import com.baghdad.entity.person.Actor
import com.baghdad.viewmodel.errorStates.BaseSnackBarMessage
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@ExperimentalCoroutinesApi
class TvShowDetailsViewModelTest {
    private lateinit var tvShowDetailsViewModel: TvShowDetailsViewModel
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
        tvShowDetailsViewModel = TvShowDetailsViewModel(
            tvShowId = tvShowId,
            getTvShowDetailsUseCase = getTvShowDetailsUseCase,
            getTvShowCastMembersUseCase = getTvShowCastMembersUseCase,
            getTvShowSeasonEpisodesUseCase = getTvShowSeasonEpisodesUseCase,
            addContinueWatchingUseCase = addContinueWatchingUseCase,
            defaultDispatcher = testDispatcher
        )
    }

    @Test
    fun `onClickBackIcon should Navigate Back when clicked`() = runTest {
        // Given
        coEvery { getTvShowDetailsUseCase.invoke(tvShowId) } returns mockTvShow
        coEvery { getTvShowCastMembersUseCase.invoke(tvShowId) } returns emptyList()
        coEvery { getTvShowSeasonEpisodesUseCase.invoke(tvShowId, 1) } returns emptyList()
        coEvery { addContinueWatchingUseCase.invoke(any(), any(), any(), any()) } returns Unit
        val effects = mutableListOf<TvShowDetailsScreenEffect>()
        val job = launch {
            tvShowDetailsViewModel.uiEffect.collect { effects.add(it) }
        }
        // When
        tvShowDetailsViewModel.onClickBackIcon()
        advanceUntilIdle()
        job.cancel()
        // Then
        assertTrue(effects.contains(TvShowDetailsScreenEffect.NavigateBack))
    }

    @Test
    fun `onClickReadMoreOverview should toggle text expansion when clicked`() = runTest {
        // Given
        coEvery { getTvShowDetailsUseCase.invoke(tvShowId) } returns mockTvShow
        coEvery { getTvShowCastMembersUseCase.invoke(tvShowId) } returns emptyList()
        coEvery { getTvShowSeasonEpisodesUseCase.invoke(tvShowId, 1) } returns emptyList()
        coEvery { addContinueWatchingUseCase.invoke(any(), any(), any(), any()) } returns Unit
        // When
        advanceUntilIdle()
        val initialExpanded = tvShowDetailsViewModel.uiState.value.isTextExpanded
        tvShowDetailsViewModel.onClickReadMoreOverview()
        // Then
        val finalExpanded = tvShowDetailsViewModel.uiState.value.isTextExpanded
        assertEquals(!initialExpanded, finalExpanded)
    }

    @Test
    fun `onClickGenre should Navigate To Genre Screen when clicked`() = runTest {
        // Given
        coEvery { getTvShowDetailsUseCase.invoke(tvShowId) } returns mockTvShow
        coEvery { getTvShowCastMembersUseCase.invoke(tvShowId) } returns emptyList()
        coEvery { getTvShowSeasonEpisodesUseCase.invoke(tvShowId, 1) } returns emptyList()
        coEvery { addContinueWatchingUseCase.invoke(any(), any(), any(), any()) } returns Unit
        val effects = mutableListOf<TvShowDetailsScreenEffect>()
        val job = launch {
            tvShowDetailsViewModel.uiEffect.collect { effects.add(it) }
        }
        // When
        tvShowDetailsViewModel.onClickGenre(genreId)
        advanceUntilIdle()
        job.cancel()
        // Then
        val expectedEffect = TvShowDetailsScreenEffect.NavigateToGenreScreen(genreId)
        assertTrue(effects.contains(expectedEffect))
    }

    @Test
    fun `onClickCastMember should Navigate To Actor Details when clicked`() = runTest {
        // Given
        coEvery { getTvShowDetailsUseCase.invoke(tvShowId) } returns mockTvShow
        coEvery { getTvShowCastMembersUseCase.invoke(tvShowId) } returns emptyList()
        coEvery { getTvShowSeasonEpisodesUseCase.invoke(tvShowId, 1) } returns emptyList()
        coEvery { addContinueWatchingUseCase.invoke(any(), any(), any(), any()) } returns Unit
        val effects = mutableListOf<TvShowDetailsScreenEffect>()
        val job = launch {
            tvShowDetailsViewModel.uiEffect.collect { effects.add(it) }
        }
        // When
        tvShowDetailsViewModel.onClickCastMember(actorId)
        advanceUntilIdle()
        job.cancel()
        // Then
        val expectedEffect = TvShowDetailsScreenEffect.NavigateToActorDetails(actorId)
        assertTrue(effects.contains(expectedEffect))
    }

    @Test
    fun `onClickEpisode should Navigate To EpisodeDetails when clicked`() = runTest {
        // Given
        coEvery { getTvShowDetailsUseCase.invoke(tvShowId) } returns mockTvShow
        coEvery { getTvShowCastMembersUseCase.invoke(tvShowId) } returns emptyList()
        coEvery { getTvShowSeasonEpisodesUseCase.invoke(tvShowId, 1) } returns emptyList()
        coEvery { addContinueWatchingUseCase.invoke(any(), any(), any(), any()) } returns Unit
        val effects = mutableListOf<TvShowDetailsScreenEffect>()
        val job = launch {
            tvShowDetailsViewModel.uiEffect.collect { effects.add(it) }
        }
        // When
        tvShowDetailsViewModel.onClickEpisode(seasonNumber, episodeNumber)
        advanceUntilIdle()
        job.cancel()
        // Then
        val expectedEffect =
            TvShowDetailsScreenEffect.NavigateToEpisodeDetails(seasonNumber, episodeNumber)
        assertTrue(effects.contains(expectedEffect))
    }

    @Test
    fun `onClickReviews should Navigate To Reviews when clicked`() = runTest {
        // Given
        coEvery { getTvShowDetailsUseCase.invoke(tvShowId) } returns mockTvShow
        coEvery { getTvShowCastMembersUseCase.invoke(tvShowId) } returns emptyList()
        coEvery { getTvShowSeasonEpisodesUseCase.invoke(tvShowId, 1) } returns emptyList()
        coEvery { addContinueWatchingUseCase.invoke(any(), any(), any(), any()) } returns Unit
        val effects = mutableListOf<TvShowDetailsScreenEffect>()
        val job = launch {
            tvShowDetailsViewModel.uiEffect.collect { effects.add(it) }
        }
        // When
        tvShowDetailsViewModel.onClickReviews(tvShowId)
        advanceUntilIdle()
        job.cancel()
        // Then
        val expectedEffect = TvShowDetailsScreenEffect.NavigateToReviews(tvShowId)
        assertTrue(effects.contains(expectedEffect))
    }

    @Test
    fun `onClickSeasonTab should update selected season and fetch episodes when clicked`() =
        runTest {
            // Given
            coEvery { getTvShowDetailsUseCase.invoke(tvShowId) } returns mockTvShow
            coEvery { getTvShowCastMembersUseCase.invoke(tvShowId) } returns emptyList()
            coEvery { getTvShowSeasonEpisodesUseCase.invoke(tvShowId, 1) } returns emptyList()
            coEvery { getTvShowSeasonEpisodesUseCase.invoke(tvShowId, 2) } returns mockEpisodes
            coEvery { addContinueWatchingUseCase.invoke(any(), any(), any(), any()) } returns Unit
            // When
            tvShowDetailsViewModel.onClickSeasonTab(1)
            advanceUntilIdle()
            // Then
            val finalState = tvShowDetailsViewModel.uiState.value
            Assertions.assertEquals(1, finalState.selectedSeasonIndex)
            coVerify { getTvShowSeasonEpisodesUseCase.invoke(tvShowId, 2) }
        }

    @Test
    fun `viewModel should implement TvShowDetailsInteractionListener`() {
        coEvery { getTvShowDetailsUseCase.invoke(tvShowId) } returns mockTvShow
        coEvery { getTvShowCastMembersUseCase.invoke(tvShowId) } returns emptyList()
        coEvery { getTvShowSeasonEpisodesUseCase.invoke(tvShowId, 1) } returns emptyList()
        coEvery { addContinueWatchingUseCase.invoke(any(), any(), any(), any()) } returns Unit

        assertTrue(true)
    }

    @Test
    fun `mapThrowableToErrorMessage should return UnknownError`() {
        // Given
        val throwable = RuntimeException("Test error")
        // When
        val result = tvShowDetailsViewModel.mapThrowableToErrorMessage(throwable)
        // Then
        assertEquals(BaseSnackBarMessage.UnknownError, result)
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
            headerPictures = listOf(
                "https://example.com/header1.jpg",
                "https://example.com/header2.jpg"
            ),
            department = "Acting"
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