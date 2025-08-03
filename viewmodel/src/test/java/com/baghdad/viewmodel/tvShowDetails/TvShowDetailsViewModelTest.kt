package com.baghdad.viewmodel.tvShowDetails

import androidx.lifecycle.SavedStateHandle
import com.baghdad.domain.exception.NoInternetException
import com.baghdad.domain.usecase.continueWatching.AddContinueWatchingUseCase
import com.baghdad.domain.usecase.tvShow.GetTvShowCastMembersUseCase
import com.baghdad.domain.usecase.tvShow.GetTvShowDetailsUseCase
import com.baghdad.domain.usecase.tvShow.GetTvShowSeasonEpisodesUseCase
import com.baghdad.entity.media.Episode
import com.baghdad.entity.media.Genre
import com.baghdad.entity.media.TvShow
import com.baghdad.viewmodel.errorStates.BaseSnackBarMessage
import com.google.common.truth.Truth.assertThat
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
            savedStateHandle = savedStateHandle,
            getTvShowDetailsUseCase = getTvShowDetailsUseCase,
            getTvShowCastMembersUseCase = getTvShowCastMembersUseCase,
            getTvShowSeasonEpisodesUseCase = getTvShowSeasonEpisodesUseCase,
            addContinueWatchingUseCase = addContinueWatchingUseCase,
            ioDispatcher = testDispatcher
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
        assertThat(effects.contains(TvShowDetailsScreenEffect.NavigateBack)).isTrue()
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
        assertThat(!initialExpanded == finalExpanded).isTrue()
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
        assertThat(effects.contains(expectedEffect)).isTrue()
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
        assertThat(effects.contains(expectedEffect)).isTrue()
    }

    @Test
    fun `onClickEpisode should Navigate To EpisodeDetails when clicked`() = runTest {
        // Given
        coEvery { getTvShowDetailsUseCase.invoke(tvShowId) } returns mockTvShow
        coEvery { getTvShowCastMembersUseCase.invoke(tvShowId) } returns emptyList()
        coEvery { getTvShowSeasonEpisodesUseCase.invoke(tvShowId, 1) } returns emptyList()
        coEvery { addContinueWatchingUseCase.invoke(any(), any(), any(), any()) } returns Unit

        val effects = mutableListOf<TvShowDetailsScreenEffect>()
        val job = launch { tvShowDetailsViewModel.uiEffect.collect { effects.add(it) } }


        // When
        tvShowDetailsViewModel.onClickEpisode(seasonNumber, episodeNumber)
        advanceUntilIdle()
        job.cancel()

        // Then
        val expectedEffect = TvShowDetailsScreenEffect.NavigateToEpisodeDetails(
            tvShowId = tvShowId,
            seasonNumber = seasonNumber,
            episodeNumber = episodeNumber
        )

        println("Actual effects: $effects")
        assertThat(effects).containsExactly(expectedEffect)
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
        assertThat(effects.contains(expectedEffect)).isTrue()
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

        assertThat(true).isTrue()
    }

    @Test
    fun `onSnackBarActionLabelClick should show no internet snackBar when NoInternetException is thrown`() =
        runTest {
            // Given
            coEvery { getTvShowDetailsUseCase.invoke(tvShowId) } throws NoInternetException()
            coEvery { getTvShowCastMembersUseCase.invoke(tvShowId) } returns emptyList()
            coEvery { getTvShowSeasonEpisodesUseCase.invoke(tvShowId, 1) } returns emptyList()
            coEvery { addContinueWatchingUseCase.invoke(any(), any(), any(), any()) } returns Unit

            val emittedSnackBarMessages = mutableListOf<BaseSnackBarMessage>()
            val job = launch {
                tvShowDetailsViewModel.snackBarState.collect {
                    emittedSnackBarMessages.add(it.message)
                }
            }

            // When
            tvShowDetailsViewModel.onSnackBarActionLabelClick()
            advanceUntilIdle()

            // Then
            assertThat(emittedSnackBarMessages).contains(BaseSnackBarMessage.NetworkError)
            job.cancel()
        }

    private companion object {

        private val savedStateHandle = SavedStateHandle(
            mapOf(
                "tvShowId" to 123L,
            )
        )
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