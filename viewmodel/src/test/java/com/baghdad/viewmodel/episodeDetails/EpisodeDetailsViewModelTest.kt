package com.baghdad.viewmodel.episodeDetails

import com.baghdad.domain.usecase.episode.GetEpisodeCastMembersUseCase
import com.baghdad.domain.usecase.episode.GetEpisodeDetailsUseCase
import com.baghdad.entity.media.Episode
import com.baghdad.entity.media.Genre
import com.baghdad.entity.person.Actor
import com.baghdad.entity.person.CastMember
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlinx.datetime.LocalDate
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
class EpisodeDetailsViewModelTest {

    private lateinit var getEpisodeCastMembersUseCase: GetEpisodeCastMembersUseCase
    private lateinit var getEpisodeDetailsUseCase: GetEpisodeDetailsUseCase
    private lateinit var episodeDetailsViewModel: EpisodeDetailsViewModel

    private val testDispatcher = StandardTestDispatcher()
    private val tvShowId = 123L
    private val seasonNumber = 1
    private val episodeNumber = 1

    @BeforeEach
    fun setUp() {
        Dispatchers.setMain(testDispatcher)

        getEpisodeCastMembersUseCase = mockk()
        getEpisodeDetailsUseCase = mockk()

        coEvery { getEpisodeCastMembersUseCase(any(), any(), any()) } returns createMockCastMembers()
        coEvery { getEpisodeDetailsUseCase(any(), any(), any()) } returns createMockEpisode()

        episodeDetailsViewModel = EpisodeDetailsViewModel(
            tvShowId = tvShowId,
            seasonNumber = seasonNumber,
            episodeNumber = episodeNumber,
            getEpisodeCastMembersUseCase = getEpisodeCastMembersUseCase,
            getEpisodeDetailsUseCase = getEpisodeDetailsUseCase
        )
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }


    @Test
    fun ` should send NavigateBack effect when onBackClick is clicked`() = runTest {
        // Given
        testDispatcher.scheduler.advanceUntilIdle()

        var receivedEffect: EpisodeDetailsScreenEffect? = null
        val job = launch {
            episodeDetailsViewModel.uiEffect.collect { effect ->
                receivedEffect = effect
            }
        }
        // When
        episodeDetailsViewModel.onBackClick()
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        assertEquals(EpisodeDetailsScreenEffect.NavigateBack, receivedEffect)
        job.cancel()
    }

    @Test
    fun `onReadMoreOverviewClick should toggle isOverviewExpanded state when click on onReadMoreOverviewClick`() = runTest {
        testDispatcher.scheduler.advanceUntilIdle()

        // Given
        val initialState = episodeDetailsViewModel.uiState.value.isOverviewExpanded

        // When
        episodeDetailsViewModel.onReadMoreOverviewClick()
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        val finalState = episodeDetailsViewModel.uiState.value.isOverviewExpanded
        assertEquals(!initialState, finalState)
    }

    @Test
    fun `should send NavigateToCategoryTvShows effect when onCategoryClick clicked`() = runTest {
        testDispatcher.scheduler.advanceUntilIdle()

        // Given
        val categoryId = 28L
        var receivedEffect: EpisodeDetailsScreenEffect? = null
        val job = launch {
            episodeDetailsViewModel.uiEffect.collect { effect ->
                receivedEffect = effect
            }
        }
        // When
        episodeDetailsViewModel.onCategoryClick(categoryId)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        assertEquals(EpisodeDetailsScreenEffect.NavigateToCategoryTvShows(categoryId), receivedEffect)
        job.cancel()
    }

    @Test
    fun `should send NavigateToActorDetails effect when onGuestOfHonorClick clicked`() = runTest {
        testDispatcher.scheduler.advanceUntilIdle()
        // Given
        val guestId = 456L
        var receivedEffect: EpisodeDetailsScreenEffect? = null
        val job = launch {
            episodeDetailsViewModel.uiEffect.collect { effect ->
                receivedEffect = effect
            }
        }
        // When
        episodeDetailsViewModel.onGuestOfHonorClick(guestId)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        assertEquals(EpisodeDetailsScreenEffect.NavigateToActorDetails(guestId), receivedEffect)
        job.cancel()
    }

    @Test
    fun `should show add to list bottom sheet when onSaveEpisodeClick clicked`() = runTest {
        testDispatcher.scheduler.advanceUntilIdle()

        // When
        episodeDetailsViewModel.onSaveEpisodeClick()
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        assertTrue(episodeDetailsViewModel.uiState.value.addToListBottomSheetState.isVisible)
    }

    @Test
    fun `onDismissAddToListBottomSheetClick should hide add to list bottom sheet`() = runTest {
        testDispatcher.scheduler.advanceUntilIdle()

        // When
        episodeDetailsViewModel.onSaveEpisodeClick()
        testDispatcher.scheduler.advanceUntilIdle()

        episodeDetailsViewModel.onDismissAddToListBottomSheetClick()
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        assertFalse(episodeDetailsViewModel.uiState.value.addToListBottomSheetState.isVisible)
    }

    @Test
    fun `should send NavigateToLogin effect when onLoginClick clicked`() = runTest {
        testDispatcher.scheduler.advanceUntilIdle()

        // Given
        var receivedEffect: EpisodeDetailsScreenEffect? = null
        val job = launch {
            episodeDetailsViewModel.uiEffect.collect { effect ->
                receivedEffect = effect
            }
        }
        // When
        episodeDetailsViewModel.onLoginClick()
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        assertEquals(EpisodeDetailsScreenEffect.NavigateToLogin, receivedEffect)
        job.cancel()
    }

    @Test
    fun `should show rate episode bottom sheet onRateEpisodeClick clicked`() = runTest {
        testDispatcher.scheduler.advanceUntilIdle()
        // When
        episodeDetailsViewModel.onRateEpisodeClick()
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        assertTrue(episodeDetailsViewModel.uiState.value.rateEpisodeBottomSheetState.isVisible)
    }

    @Test
    fun `should hide rate episode bottom sheet when onDismissRatingBottomSheet `() = runTest {
        testDispatcher.scheduler.advanceUntilIdle()

        // When
        episodeDetailsViewModel.onRateEpisodeClick()
        testDispatcher.scheduler.advanceUntilIdle()

        episodeDetailsViewModel.onDismissRatingBottomSheet()
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        assertFalse(episodeDetailsViewModel.uiState.value.rateEpisodeBottomSheetState.isVisible)
    }

    @Test
    fun `failure should handle error gracefully when getEpisodeDetails`() = runTest {
        val exception = RuntimeException()
        coEvery { getEpisodeDetailsUseCase(any(), any(), any()) } throws exception

        episodeDetailsViewModel = EpisodeDetailsViewModel(
            tvShowId = tvShowId,
            seasonNumber = seasonNumber,
            episodeNumber = episodeNumber,
            getEpisodeCastMembersUseCase = getEpisodeCastMembersUseCase,
            getEpisodeDetailsUseCase = getEpisodeDetailsUseCase
        )

        testDispatcher.scheduler.advanceUntilIdle()

        coVerify { getEpisodeDetailsUseCase(tvShowId, seasonNumber, episodeNumber) }
        assertFalse(episodeDetailsViewModel.uiState.value.isEpisodeDetailsLoading)
    }

    @Test
    fun `failure should handle error gracefully when getEpisodeCastMembers`() = runTest {
        val exception = RuntimeException()
        coEvery { getEpisodeCastMembersUseCase(any(), any(), any()) } throws exception

        episodeDetailsViewModel = EpisodeDetailsViewModel(
            tvShowId = tvShowId,
            seasonNumber = seasonNumber,
            episodeNumber = episodeNumber,
            getEpisodeCastMembersUseCase = getEpisodeCastMembersUseCase,
            getEpisodeDetailsUseCase = getEpisodeDetailsUseCase
        )

        testDispatcher.scheduler.advanceUntilIdle()

        coVerify { getEpisodeCastMembersUseCase(tvShowId, seasonNumber, episodeNumber) }
        assertFalse(episodeDetailsViewModel.uiState.value.isEpisodeCastMembersLoading)
    }

    @Test
    fun `onPlayTrailerClick should do nothing for now`() = runTest {
        episodeDetailsViewModel.onPlayTrailerClick()
    }

    @Test
    fun `state should have correct initial values`() = runTest {
        val initialState = EpisodeDetailsScreenState()

        assertFalse(initialState.isEpisodeDetailsLoading)
        assertFalse(initialState.isEpisodeCastMembersLoading)
        assertEquals(EpisodeDetailsScreenState.EpisodeUiState(), initialState.episode)
        assertTrue(initialState.guestsOfHonor.isEmpty())
        assertFalse(initialState.isOverviewExpanded)
        assertFalse(initialState.isSavedToList)
        assertFalse(initialState.isRated)
        assertFalse(initialState.addToListBottomSheetState.isVisible)
        assertFalse(initialState.rateEpisodeBottomSheetState.isVisible)
    }

    @Test
    fun `EpisodeUiState should have correct default values`() = runTest {
        val defaultUiState = EpisodeDetailsScreenState.EpisodeUiState()

        assertEquals(0L, defaultUiState.id)
        assertEquals("", defaultUiState.title)
        assertEquals(0, defaultUiState.episodeNumber)
        assertEquals(0.0, defaultUiState.rating)
        assertEquals("", defaultUiState.trailerUrl)
        assertEquals("", defaultUiState.duration)
        assertEquals("", defaultUiState.releasedDate)
        assertEquals(0, defaultUiState.currentSeason)
        assertEquals("", defaultUiState.overview)
        assertTrue(defaultUiState.categories.isEmpty())
        assertTrue(defaultUiState.headerPictures.isEmpty())
    }

    @Test
    fun `GuestsOfHonerUiState should have correct default values`() = runTest {
        val defaultGuestState = EpisodeDetailsScreenState.GuestsOfHonerUiState()

        assertEquals(0L, defaultGuestState.id)
        assertEquals("", defaultGuestState.name)
        assertEquals("", defaultGuestState.characterName)
        assertEquals("", defaultGuestState.profilePictureURL)
    }

    @Test
    fun `CategoryUiState should have correct default values`() = runTest {
        val defaultCategoryState = EpisodeDetailsScreenState.CategoryUiState()

        assertEquals(0L, defaultCategoryState.id)
        assertEquals("", defaultCategoryState.name)
    }

    @Test
    fun `AddToListBottomSheetState should have correct default values`() = runTest {
        val defaultBottomSheetState = EpisodeDetailsScreenState.AddToListBottomSheetState()
        assertFalse(defaultBottomSheetState.isVisible)
    }

    @Test
    fun `RateEpisodeBottomSheetState should have correct default values`() = runTest {
        val defaultRateState = EpisodeDetailsScreenState.RateEpisodeBottomSheetState()
        assertFalse(defaultRateState.isVisible)
    }

    companion object {
        private fun createMockEpisode() = Episode(
            id = 123L,
            title = "Test Episode",
            episodeNumber = 1,
            rating = 8.5,
            duration = "45m",
            releasedDate = LocalDate.parse("2023-01-01"),
            trailerUrl = "https://youtube.com/watch?v=test",
            currentSeason = 1,
            overview = "Test episode overview",
            genres = listOf(
                Genre(28L, "Action"),
                Genre(35L, "Comedy")
            ),
            headerPictures = listOf(
                "/header1.jpg",
                "/header2.jpg"
            )
        )

        private fun createMockCastMembers() = listOf(
            CastMember(
                actor = Actor(
                    id = 1L,
                    name = "John Doe",
                    profilePictureURL = "/john_doe.jpg",
                    birthDate = LocalDate.parse("1980-01-01"),
                    placeOfBirth = "New York, USA",
                    deathDate = null,
                    biography = "Famous actor",
                    headerPictures = listOf("/header1.jpg"),
                    department = "Acting"
                ),
                characterName = "Guest Star"
            ),
            CastMember(
                actor = Actor(
                    id = 2L,
                    name = "Jane Smith",
                    profilePictureURL = "/jane_smith.jpg",
                    birthDate = LocalDate.parse("1985-01-01"),
                    placeOfBirth = "Los Angeles, USA",
                    deathDate = null,
                    biography = "Talented actress",
                    headerPictures = listOf("/header2.jpg"),
                    department = "Acting"
                ),
                characterName = "Special Guest"
            )
        )
    }

}