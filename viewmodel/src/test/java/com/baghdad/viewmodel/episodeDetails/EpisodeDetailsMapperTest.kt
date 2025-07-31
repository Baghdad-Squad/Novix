package com.baghdad.viewmodel.episodeDetails

import com.baghdad.entity.media.Episode
import com.baghdad.entity.media.Genre
import com.baghdad.entity.person.Actor
import com.baghdad.entity.person.CastMember
import com.google.common.truth.Truth.assertThat
import kotlinx.datetime.LocalDate
import org.junit.jupiter.api.Test

class EpisodeExtensionsTest {

    @Test
    fun `Given Episode with null releasedDate, When toUiState, Then should use empty string`() {
        // Given
        val episode = Episode(
            id = 1L,
            title = "Test",
            releasedDate = null,
            episodeNumber = 1,
            rating = 0.0,
            duration = "",
            currentSeason = 1,
            overview = "",
            headerPictures = emptyList(),
            genres = emptyList(),
            trailerUrl = ""
        )

        // When
        val result = episode.toUiState()

        // Then
        assertThat(result.releasedDate).isEmpty()
    }

    @Test
    fun `Given Episode with rating, When toUiState, Then should round to first decimal`() {
        // Given
        val episode = Episode(
            id = 1L,
            title = "Test",
            rating = 7.349,
            episodeNumber = 1,
            duration = "",
            releasedDate = null,
            currentSeason = 1,
            overview = "",
            headerPictures = emptyList(),
            genres = emptyList(),
            trailerUrl = ""
        )

        // When
        val result = episode.toUiState()

        // Then
        assertThat(result.rating).isWithin(0.001).of(7.3)
    }

    @Test
    fun `Given empty genres list, When toUiStates, Then should return empty list`() {
        // Given
        val genres = emptyList<Genre>()

        // When
        val result = genres.toUiStates()

        // Then
        assertThat(result).isEmpty()
    }

    @Test
    fun `Given genres list, When toUiStates, Then should map all genres correctly`() {
        // Given
        val genres = listOf(
            Genre(1L, "Action"),
            Genre(2L, "Drama")
        )

        // When
        val result = genres.toUiStates()

        // Then
        assertThat(result).containsExactly(
            EpisodeDetailsScreenState.CategoryUiState(1L, "Action"),
            EpisodeDetailsScreenState.CategoryUiState(2L, "Drama")
        ).inOrder()
    }

    @Test
    fun `Given CastMember, When toUiState, Then should map all fields correctly`() {
        // Given
        val castMember = CastMember(
            actor = Actor(
                id = 1L,
                name = "John Doe",
                profilePictureURL = "profile.jpg",
                birthDate = LocalDate(1990, 1, 1),
                placeOfBirth = "New York",
                deathDate = LocalDate(2020, 1, 1),
                biography = "An actor known for his roles in various films.",
                headerPictures = listOf("header1.jpg", "header2.jpg"),
                department = "Acting",
            ),
            characterName = "Detective"
        )

        // When
        val result = castMember.toUiState()

        // Then
        assertThat(result).isEqualTo(
            EpisodeDetailsScreenState.GuestsOfHonerUiState(
                id = 1L,
                name = "John Doe",
                profilePictureURL = "profile.jpg",
                characterName = "Detective"
            )
        )
    }

    @Test
    fun `Given CastMember with empty fields, When toUiState, Then should handle empty values`() {
        // Given
        val castMember = CastMember(
            actor = Actor(
                id = 0L,
                name = "",
                profilePictureURL = "",
                birthDate = LocalDate(2023, 1, 1),
                placeOfBirth = "",
                deathDate = LocalDate(2023, 1, 1),
                biography = "",
                headerPictures = emptyList(),
                department = ""
            ),
            characterName = ""
        )

        // When
        val result = castMember.toUiState()

        // Then
        assertThat(result).isEqualTo(
            EpisodeDetailsScreenState.GuestsOfHonerUiState(
                id = 0L,
                name = "",
                profilePictureURL = "",
                characterName = ""
            )
        )
    }
}