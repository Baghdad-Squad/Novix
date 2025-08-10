package com.baghdad.viewmodel.episodeDetails

import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

class EpisodeDetailsScreenStateTest {

    @Test
    fun `EpisodeDetailsScreenState should initialize with default values when default constructor is used`() {
        // When
        val state = EpisodeDetailsScreenState()

        // Then
        assertThat(state.isEpisodeDetailsLoading).isFalse()
        assertThat(state.isEpisodeCastMembersLoading).isFalse()
        assertThat(state.episode).isEqualTo(EpisodeDetailsScreenState.EpisodeUiState())
        assertThat(state.guestsOfHonor).isEmpty()
        assertThat(state.isOverviewExpanded).isFalse()
        assertThat(state.isSavedToList).isFalse()
        assertThat(state.isRated).isTrue()
        assertThat(state.addToListBottomSheetState.isVisible).isFalse()
        assertThat(state.rateEpisodeBottomSheetState.isVisible).isFalse()
        assertThat(state.isLoading).isFalse()
    }

    @Test
    fun `EpisodeDetailsScreenState should set all properties correctly when constructed with parameters`() {
        // Given
        val episodeUiState = createEpisodeUiState()
        val guests = listOf(createGuestOfHonor())

        // When
        val state = EpisodeDetailsScreenState(
            isEpisodeDetailsLoading = true,
            isEpisodeCastMembersLoading = true,
            episode = episodeUiState,
            guestsOfHonor = guests,
            isOverviewExpanded = true,
            isSavedToList = true,
            isRated = true,
            addToListBottomSheetState = EpisodeDetailsScreenState.AddToListBottomSheetState(true),
            rateEpisodeBottomSheetState = EpisodeDetailsScreenState.RateEpisodeBottomSheetState(true)
        )

        // Then
        assertThat(state.isEpisodeDetailsLoading).isTrue()
        assertThat(state.isEpisodeCastMembersLoading).isTrue()
        assertThat(state.episode).isEqualTo(episodeUiState)
        assertThat(state.guestsOfHonor).isEqualTo(guests)
        assertThat(state.isOverviewExpanded).isTrue()
        assertThat(state.isSavedToList).isTrue()
        assertThat(state.isRated).isTrue()
        assertThat(state.addToListBottomSheetState.isVisible).isTrue()
        assertThat(state.rateEpisodeBottomSheetState.isVisible).isTrue()
        assertThat(state.isLoading).isTrue()
    }

    @Test
    fun `isLoading should be true when either isEpisodeDetailsLoading or isEpisodeCastMembersLoading is true`() {
        // Given
        val loadingDetailsState = EpisodeDetailsScreenState(isEpisodeDetailsLoading = true)
        val loadingCastState = EpisodeDetailsScreenState(isEpisodeCastMembersLoading = true)
        val loadingBothState = EpisodeDetailsScreenState(
            isEpisodeDetailsLoading = true,
            isEpisodeCastMembersLoading = true
        )
        val notLoadingState = EpisodeDetailsScreenState()

        // Then
        assertThat(loadingDetailsState.isLoading).isTrue()
        assertThat(loadingCastState.isLoading).isTrue()
        assertThat(loadingBothState.isLoading).isTrue()
        assertThat(notLoadingState.isLoading).isFalse()
    }

    @Test
    fun `EpisodeUiState should initialize with default values when default constructor is used`() {
        // When
        val episodeUiState = EpisodeDetailsScreenState.EpisodeUiState()

        // Then
        assertThat(episodeUiState.id).isEqualTo(0L)
        assertThat(episodeUiState.title).isEmpty()
        assertThat(episodeUiState.episodeNumber).isEqualTo(0)
        assertThat(episodeUiState.rating).isEqualTo(0)
        assertThat(episodeUiState.trailerUrl).isEmpty()
        assertThat(episodeUiState.duration).isEmpty()
        assertThat(episodeUiState.releasedDate).isEmpty()
        assertThat(episodeUiState.currentSeason).isEqualTo(0)
        assertThat(episodeUiState.overview).isEmpty()
        assertThat(episodeUiState.categories).isEmpty()
        assertThat(episodeUiState.headerPictures).isEmpty()
    }

    @Test
    fun `EpisodeUiState should set all properties correctly when constructed with parameters`() {
        // Given
        val categories = listOf(createCategoryUiState())
        val headerPictures = listOf(HEADER_IMAGE)

        // When
        val episodeUiState = EpisodeDetailsScreenState.EpisodeUiState(
            id = EPISODE_ID,
            title = EPISODE_TITLE,
            episodeNumber = EPISODE_NUMBER,
            rating = RATING,
            trailerUrl = TRAILER_URL,
            duration = DURATION,
            releasedDate = RELEASE_DATE,
            currentSeason = CURRENT_SEASON,
            overview = OVERVIEW,
            categories = categories,
            headerPictures = headerPictures
        )

        // Then
        assertThat(episodeUiState.id).isEqualTo(EPISODE_ID)
        assertThat(episodeUiState.title).isEqualTo(EPISODE_TITLE)
        assertThat(episodeUiState.episodeNumber).isEqualTo(EPISODE_NUMBER)
        assertThat(episodeUiState.rating).isEqualTo(RATING)
        assertThat(episodeUiState.trailerUrl).isEqualTo(TRAILER_URL)
        assertThat(episodeUiState.duration).isEqualTo(DURATION)
        assertThat(episodeUiState.releasedDate).isEqualTo(RELEASE_DATE)
        assertThat(episodeUiState.currentSeason).isEqualTo(CURRENT_SEASON)
        assertThat(episodeUiState.overview).isEqualTo(OVERVIEW)
        assertThat(episodeUiState.categories).isEqualTo(categories)
        assertThat(episodeUiState.headerPictures).isEqualTo(headerPictures)
    }

    @Test
    fun `EpisodeUiState should create new instance with updated properties when copy is called`() {
        // Given
        val original = createEpisodeUiState()

        // When
        val copied = original.copy(title = "Copied Title")

        // Then
        assertThat(copied.title).isEqualTo("Copied Title")
        assertThat(original.title).isEqualTo(EPISODE_TITLE)
        assertThat(copied.id).isEqualTo(original.id)
    }

    @Test
    fun `GuestsOfHonerUiState should initialize with default values when default constructor is used`() {
        // When
        val guest = EpisodeDetailsScreenState.GuestsOfHonerUiState()

        // Then
        assertThat(guest.id).isEqualTo(0L)
        assertThat(guest.name).isEmpty()
        assertThat(guest.characterName).isEmpty()
        assertThat(guest.profilePictureURL).isEmpty()
    }

    @Test
    fun `GuestsOfHonerUiState should set all properties correctly when constructed with parameters`() {
        // When
        val guest = EpisodeDetailsScreenState.GuestsOfHonerUiState(
            id = GUEST_ID,
            name = GUEST_NAME,
            characterName = CHARACTER_NAME,
            profilePictureURL = PROFILE_PIC
        )

        // Then
        assertThat(guest.id).isEqualTo(GUEST_ID)
        assertThat(guest.name).isEqualTo(GUEST_NAME)
        assertThat(guest.characterName).isEqualTo(CHARACTER_NAME)
        assertThat(guest.profilePictureURL).isEqualTo(PROFILE_PIC)
    }

    @Test
    fun `GuestsOfHonerUiState should create new instance with updated properties when copy is called`() {
        // Given
        val original = createGuestOfHonor()

        // When
        val copied = original.copy(name = "Copied Name")

        // Then
        assertThat(copied.name).isEqualTo("Copied Name")
        assertThat(original.name).isEqualTo(GUEST_NAME)
    }

    @Test
    fun `CategoryUiState should initialize with default values when default constructor is used`() {
        // When
        val category = EpisodeDetailsScreenState.CategoryUiState()

        // Then
        assertThat(category.id).isEqualTo(0L)
        assertThat(category.name).isEmpty()
    }

    @Test
    fun `CategoryUiState should set all properties correctly when constructed with parameters`() {
        // When
        val category = EpisodeDetailsScreenState.CategoryUiState(
            id = CATEGORY_ID,
            name = CATEGORY_NAME
        )

        // Then
        assertThat(category.id).isEqualTo(CATEGORY_ID)
        assertThat(category.name).isEqualTo(CATEGORY_NAME)
    }

    @Test
    fun `CategoryUiState should create new instance with updated properties when copy is called`() {
        // Given
        val original = createCategoryUiState()

        // When
        val copied = original.copy(name = "Copied Category")

        // Then
        assertThat(copied.name).isEqualTo("Copied Category")
        assertThat(original.name).isEqualTo(CATEGORY_NAME)
    }

    @Test
    fun `AddToListBottomSheetState should update visibility correctly when copy is called`() {
        // Given
        val customState = EpisodeDetailsScreenState.AddToListBottomSheetState(true)

        // When
        val copied = customState.copy(isVisible = false)

        // Then
        assertThat(copied.isVisible).isFalse()
        assertThat(customState.isVisible).isTrue()
    }

    @Test
    fun `RateEpisodeBottomSheetState should update visibility correctly when copy is called`() {
        // Given
        val customState = EpisodeDetailsScreenState.RateEpisodeBottomSheetState(true)

        // When
        val copied = customState.copy(isVisible = false)

        // Then
        assertThat(copied.isVisible).isFalse()
        assertThat(customState.isVisible).isTrue()
    }

    @Test
    fun `equals should return true when comparing identical states`() {
        // Given
        val state1 = EpisodeDetailsScreenState(
            episode = createEpisodeUiState(),
            guestsOfHonor = listOf(createGuestOfHonor())
        )
        val state2 = EpisodeDetailsScreenState(
            episode = createEpisodeUiState(),
            guestsOfHonor = listOf(createGuestOfHonor())
        )

        // Then
        assertThat(state1).isEqualTo(state2)
        assertThat(state1.hashCode()).isEqualTo(state2.hashCode())
    }

    @Test
    fun `equals should return false when comparing different states`() {
        // Given
        val state1 = EpisodeDetailsScreenState(episode = createEpisodeUiState())
        val state2 = EpisodeDetailsScreenState(episode = createEpisodeUiState(title = "Different"))

        // Then
        assertThat(state1).isNotEqualTo(state2)
    }

    @Test
    fun `guestsOfHonor should maintain insertion order when multiple guests are added`() {
        // Given
        val guests = listOf(
            createGuestOfHonor(name = "Guest 1"),
            createGuestOfHonor(name = "Guest 2"),
            createGuestOfHonor(name = "Guest 3")
        )

        // When
        val state = EpisodeDetailsScreenState(guestsOfHonor = guests)

        // Then
        assertThat(state.guestsOfHonor).hasSize(3)
        assertThat(state.guestsOfHonor[0].name).isEqualTo("Guest 1")
        assertThat(state.guestsOfHonor[1].name).isEqualTo("Guest 2")
        assertThat(state.guestsOfHonor[2].name).isEqualTo("Guest 3")
    }

    @Test
    fun `categories should maintain insertion order when multiple categories are added`() {
        // Given
        val categories = listOf(
            createCategoryUiState(name = "Category 1"),
            createCategoryUiState(name = "Category 2"),
            createCategoryUiState(name = "Category 3")
        )

        // When
        val state = EpisodeDetailsScreenState(episode = createEpisodeUiState(categories = categories))

        // Then
        assertThat(state.episode.categories).hasSize(3)
        assertThat(state.episode.categories[0].name).isEqualTo("Category 1")
        assertThat(state.episode.categories[1].name).isEqualTo("Category 2")
        assertThat(state.episode.categories[2].name).isEqualTo("Category 3")
    }

    private fun createEpisodeUiState(
        id: Long = EPISODE_ID,
        title: String = EPISODE_TITLE,
        categories: List<EpisodeDetailsScreenState.CategoryUiState> = listOf(createCategoryUiState())
    ) = EpisodeDetailsScreenState.EpisodeUiState(
        id = id,
        title = title,
        episodeNumber = EPISODE_NUMBER,
        rating = RATING,
        trailerUrl = TRAILER_URL,
        duration = DURATION,
        releasedDate = RELEASE_DATE,
        currentSeason = CURRENT_SEASON,
        overview = OVERVIEW,
        categories = categories,
        headerPictures = listOf(HEADER_IMAGE)
    )

    private fun createGuestOfHonor(
        id: Long = GUEST_ID,
        name: String = GUEST_NAME
    ) = EpisodeDetailsScreenState.GuestsOfHonerUiState(
        id = id,
        name = name,
        characterName = CHARACTER_NAME,
        profilePictureURL = PROFILE_PIC
    )

    private fun createCategoryUiState(
        id: Long = CATEGORY_ID,
        name: String = CATEGORY_NAME
    ) = EpisodeDetailsScreenState.CategoryUiState(
        id = id,
        name = name
    )

    private companion object {
        const val EPISODE_ID = 123L
        const val EPISODE_TITLE = "Test Episode"
        const val EPISODE_NUMBER = 1
        const val RATING = 8
        const val DURATION = "45m"
        const val RELEASE_DATE = "2023-01-01"
        const val CURRENT_SEASON = 1
        const val OVERVIEW = "Test episode overview"
        const val TRAILER_URL = "https://youtube.com/watch?v=test"
        const val CATEGORY_ID = 28L
        const val CATEGORY_NAME = "Action"
        const val HEADER_IMAGE = "header1.jpg"
        const val GUEST_ID = 456L
        const val GUEST_NAME = "John Doe"
        const val CHARACTER_NAME = "Guest Star"
        const val PROFILE_PIC = "profile.jpg"
    }
}