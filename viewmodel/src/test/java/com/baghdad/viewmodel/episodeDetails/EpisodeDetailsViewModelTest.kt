package com.baghdad.viewmodel.episodeDetails

import androidx.lifecycle.SavedStateHandle
import com.baghdad.domain.model.MediaAccountStates
import com.baghdad.domain.usecase.episode.AddEpisodeRateUseCase
import com.baghdad.domain.usecase.episode.GetEpisodeAccountStatesUseCase
import com.baghdad.domain.usecase.episode.GetEpisodeCastMembersUseCase
import com.baghdad.domain.usecase.episode.GetEpisodeDetailsUseCase
import com.baghdad.domain.usecase.login.IsUserLoggedInUseCase
import com.baghdad.entity.media.Episode
import com.baghdad.entity.media.Genre
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
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlinx.datetime.LocalDate
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
class EpisodeDetailsViewModelTest {

    @BeforeEach
    fun setUp() {
        Dispatchers.setMain(testDispatcher)

        getEpisodeCastMembersUseCase = mockk()
        getEpisodeDetailsUseCase = mockk()
        getEpisodeAccountStatesUseCase = mockk()
        isUserLoggedInUseCase = mockk()
        addEpisodeRateUseCase = mockk()


        coEvery { getEpisodeCastMembersUseCase(any(), any(), any()) } returns createMockCastMembers()
        coEvery { getEpisodeDetailsUseCase(any(), any(), any()) } returns createMockEpisode()
        coEvery { getEpisodeAccountStatesUseCase(any(), any(), any()) } returns createMockAccountStates()
        coEvery { isUserLoggedInUseCase() } returns true
        coEvery { addEpisodeRateUseCase(any(), any(), any(), any()) } returns Unit

        val savedStateHandle = SavedStateHandle(mapOf(
            "tvShowId" to 1L,
            "seasonNumber" to seasonNumber,
            "episodeNumber" to episodeNumber,

        ))

        episodeDetailsViewModel = EpisodeDetailsViewModel(
            getEpisodeCastMembersUseCase = getEpisodeCastMembersUseCase,
            getEpisodeDetailsUseCase = getEpisodeDetailsUseCase,
            addEpisodeRateUseCase = addEpisodeRateUseCase,
            getEpisodeAccountStatesUseCase = getEpisodeAccountStatesUseCase,
            isUserLoggedInUseCase = isUserLoggedInUseCase,
            ioDispatcher = testDispatcher,
            savedStateHandle = savedStateHandle
        )
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `onBackClick should emit NavigateBack effect when called`() = runTest {
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
        assertThat(receivedEffect).isEqualTo(EpisodeDetailsScreenEffect.NavigateBack)
        job.cancel()
    }

    @Test
    fun `onReadMoreOverviewClick should toggle isOverviewExpanded state when called`() = runTest {
        testDispatcher.scheduler.advanceUntilIdle()

        // Given
        val initialState = episodeDetailsViewModel.uiState.value.isOverviewExpanded

        // When
        episodeDetailsViewModel.onReadMoreOverviewClick()
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        assertThat(episodeDetailsViewModel.uiState.value.isOverviewExpanded).isEqualTo(!initialState)
    }

    @Test
    fun `onCategoryClick should emit NavigateToCategoryTvShows effect with correct id when called`() = runTest {
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
        assertThat(receivedEffect).isEqualTo(EpisodeDetailsScreenEffect.NavigateToCategoryTvShows(categoryId))
        job.cancel()
    }

    @Test
    fun `onGuestOfHonorClick should emit NavigateToActorDetails effect with correct id when called`() = runTest {
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
        assertThat(receivedEffect).isEqualTo(EpisodeDetailsScreenEffect.NavigateToActorDetails(guestId))
        job.cancel()
    }

    @Test
    fun `onSaveEpisodeClick should show addToListBottomSheet when called`() = runTest {
        testDispatcher.scheduler.advanceUntilIdle()

        // When
        episodeDetailsViewModel.onSaveEpisodeClick()
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        assertThat(episodeDetailsViewModel.uiState.value.addToListBottomSheetState.isVisible).isTrue()
    }

    @Test
    fun `onDismissAddToListBottomSheetClick should hide addToListBottomSheet when called`() = runTest {
        testDispatcher.scheduler.advanceUntilIdle()

        // Given
        episodeDetailsViewModel.onSaveEpisodeClick()
        testDispatcher.scheduler.advanceUntilIdle()

        // When
        episodeDetailsViewModel.onDismissAddToListBottomSheetClick()
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        assertThat(episodeDetailsViewModel.uiState.value.addToListBottomSheetState.isVisible).isFalse()
    }

    @Test
    fun `onLoginClick should emit NavigateToLogin effect when called`() = runTest {
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
        assertThat(receivedEffect).isEqualTo(EpisodeDetailsScreenEffect.NavigateToLogin)
        job.cancel()
    }

    @Test
    fun `onDismissRatingBottomSheet should hide rateEpisodeBottomSheet when called`() = runTest {
        testDispatcher.scheduler.advanceUntilIdle()

        // Given
        episodeDetailsViewModel.onClickStarButton()
        testDispatcher.scheduler.advanceUntilIdle()

        // When
        episodeDetailsViewModel.onDismissRatingBottomSheet()
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        assertThat(episodeDetailsViewModel.uiState.value.rateEpisodeBottomSheetState.isVisible).isFalse()
    }

    @Test
    fun `getEpisodeDetails should handle errors gracefully when exception occurs`() = runTest {
        // Given
        val exception = RuntimeException()
        coEvery { getEpisodeDetailsUseCase(any(), any(), any()) } throws exception

        val savedStateHandle = SavedStateHandle(mapOf(
            "tvShowId" to tvShowId,
            "seasonNumber" to seasonNumber,
            "episodeNumber" to episodeNumber,

            ))
        episodeDetailsViewModel = EpisodeDetailsViewModel(
            savedStateHandle,
            getEpisodeCastMembersUseCase = getEpisodeCastMembersUseCase,
            getEpisodeDetailsUseCase = getEpisodeDetailsUseCase,
            addEpisodeRateUseCase = addEpisodeRateUseCase,
            getEpisodeAccountStatesUseCase = getEpisodeAccountStatesUseCase,
            isUserLoggedInUseCase = isUserLoggedInUseCase,
            ioDispatcher = testDispatcher
        )

        // When
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        coVerify { getEpisodeDetailsUseCase(tvShowId, seasonNumber, episodeNumber) }
        assertThat(episodeDetailsViewModel.uiState.value.isEpisodeDetailsLoading).isFalse()
    }

    @Test
    fun `getEpisodeCastMembers should handle errors gracefully when exception occurs`() = runTest {
        // Given
        val exception = RuntimeException()
        coEvery { getEpisodeCastMembersUseCase(any(), any(), any()) } throws exception
        val savedStateHandle = SavedStateHandle(mapOf(
            "tvShowId" to tvShowId,
            "seasonNumber" to seasonNumber,
            "episodeNumber" to episodeNumber,

            ))
        episodeDetailsViewModel = EpisodeDetailsViewModel(
            savedStateHandle = savedStateHandle,
            getEpisodeCastMembersUseCase = getEpisodeCastMembersUseCase,
            getEpisodeDetailsUseCase = getEpisodeDetailsUseCase,
            addEpisodeRateUseCase = addEpisodeRateUseCase,
            getEpisodeAccountStatesUseCase = getEpisodeAccountStatesUseCase,
            isUserLoggedInUseCase = isUserLoggedInUseCase,
            ioDispatcher = testDispatcher
        )

        // When
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        coVerify { getEpisodeCastMembersUseCase(tvShowId, seasonNumber, episodeNumber) }
        assertThat(episodeDetailsViewModel.uiState.value.isEpisodeCastMembersLoading).isFalse()
    }

    @Test
    fun `onPlayTrailerClick should do nothing when called`() = runTest {
        // When
        episodeDetailsViewModel.onPlayTrailerClick()

    }

    @Test
    fun `uiState should have correct default values when initialized`() = runTest {
        // Given
        val initialState = EpisodeDetailsScreenState()

        // Then
        assertThat(initialState.isEpisodeDetailsLoading).isFalse()
        assertThat(initialState.isEpisodeCastMembersLoading).isFalse()
        assertThat(initialState.episode).isEqualTo(EpisodeDetailsScreenState.EpisodeUiState())
        assertThat(initialState.guestsOfHonor).isEmpty()
        assertThat(initialState.isOverviewExpanded).isFalse()
        assertThat(initialState.isSavedToList).isFalse()
        assertThat(initialState.isRated).isTrue()
        assertThat(initialState.addToListBottomSheetState.isVisible).isFalse()
        assertThat(initialState.rateEpisodeBottomSheetState.isVisible).isFalse()
    }

    @Test
    fun `EpisodeUiState should have correct default values when initialized`() = runTest {
        // Given
        val defaultUiState = EpisodeDetailsScreenState.EpisodeUiState()

        // Then
        assertThat(defaultUiState.id).isEqualTo(0L)
        assertThat(defaultUiState.title).isEmpty()
        assertThat(defaultUiState.episodeNumber).isEqualTo(0)
        assertThat(defaultUiState.rating).isEqualTo(0)
        assertThat(defaultUiState.trailerUrl).isEmpty()
        assertThat(defaultUiState.duration).isEmpty()
        assertThat(defaultUiState.releasedDate).isEmpty()
        assertThat(defaultUiState.currentSeason).isEqualTo(0)
        assertThat(defaultUiState.overview).isEmpty()
        assertThat(defaultUiState.categories).isEmpty()
        assertThat(defaultUiState.headerPictures).isEmpty()
    }

    @Test
    fun `GuestsOfHonerUiState should have correct default values when initialized`() = runTest {
        // Given
        val defaultGuestState = EpisodeDetailsScreenState.GuestsOfHonerUiState()

        // Then
        assertThat(defaultGuestState.id).isEqualTo(0L)
        assertThat(defaultGuestState.name).isEmpty()
        assertThat(defaultGuestState.characterName).isEmpty()
        assertThat(defaultGuestState.profilePictureURL).isEmpty()
    }

    @Test
    fun `CategoryUiState should have correct default values when initialized`() = runTest {
        // Given
        val defaultCategoryState = EpisodeDetailsScreenState.CategoryUiState()

        // Then
        assertThat(defaultCategoryState.id).isEqualTo(0L)
        assertThat(defaultCategoryState.name).isEmpty()
    }

    @Test
    fun `AddToListBottomSheetState should have correct default values when initialized`() = runTest {
        // Given
        val defaultBottomSheetState = EpisodeDetailsScreenState.AddToListBottomSheetState()

        // Then
        assertThat(defaultBottomSheetState.isVisible).isFalse()
    }

    @Test
    fun `RateEpisodeBottomSheetState should have correct default values when initialized`() = runTest {
        // Given
        val defaultRateState = EpisodeDetailsScreenState.RateEpisodeBottomSheetState()

        // Then
        assertThat(defaultRateState.isVisible).isFalse()
    }

    companion object {
        private fun createMockEpisode() = Episode(
            id = 123L,
            title = "Test Episode",
            episodeNumber = 1,
            rating = 8.0,
            duration = "45m",
            releasedDate = LocalDate.parse("2023-01-01"),
            trailerUrl = "https://youtube.com/watch?v=test",
            currentSeason = 1,
            overview = "Test episode overview",
            genres = listOf(
                Genre(28L, "Action"),
                Genre(35L, "Comedy")
            ),
            userRating = 5,
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
    private fun createMockAccountStates() = MediaAccountStates(
        isMediaRated = true
    )

    private lateinit var getEpisodeCastMembersUseCase: GetEpisodeCastMembersUseCase
    private lateinit var getEpisodeDetailsUseCase: GetEpisodeDetailsUseCase
    private lateinit var episodeDetailsViewModel: EpisodeDetailsViewModel
    private lateinit var getEpisodeAccountStatesUseCase: GetEpisodeAccountStatesUseCase
    private lateinit var isUserLoggedInUseCase: IsUserLoggedInUseCase
    private lateinit var addEpisodeRateUseCase: AddEpisodeRateUseCase

    private val testDispatcher = StandardTestDispatcher()
    private val tvShowId = 123L
    private val seasonNumber = 1
    private val episodeNumber = 1
}