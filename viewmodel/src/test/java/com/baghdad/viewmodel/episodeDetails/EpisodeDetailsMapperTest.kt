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
    fun `toUiState should map Episode to EpisodeUiState`() {
        val result = episode.toUiState()

        assertThat(result).isEqualTo(expectedEpisodeUiState)
    }

    @Test
    fun `toUiState should map Episode with null releasedDate to EpisodeUiState with empty releasedDate`() {
        val episodeWithNullReleasedDate = episode.copy(releasedDate = null)

        val result = episodeWithNullReleasedDate.toUiState()

        assertThat(result.releasedDate).isEmpty()
    }

    @Test
    fun `toUiState should map Episode with null userRating to EpisodeUiState with default userRating`() {
        val episodeWithNullUserRating = episode.copy(userRating = null)

        val result = episodeWithNullUserRating.toUiState()

        assertThat(result.userRating).isEqualTo(0)
    }

    @Test
    fun `toUiStates should map List of Genre to List of CategoryUiState`() {
        val genre = Genre(id = 1, name = "Action")
        val genres = listOf(genre, genre.copy(id = 2), genre.copy(id = 3))
        val categoryUiState = EpisodeDetailsScreenState.CategoryUiState(
            id = 1,
            name = "Action"
        )
        val expectedCategoryUiStates = listOf(
            categoryUiState,
            categoryUiState.copy(id = 2),
            categoryUiState.copy(id = 3)
        )

        val result = genres.toUiStates()

        assertThat(result).isEqualTo(expectedCategoryUiStates)
    }

    @Test
    fun `toUiState should map CastMember to GuestsOfHonerUiState`() {

        val castMember = CastMember(
            actor = Actor(
                id = 1,
                name = "Actor Name",
                profilePictureURL = "https://example.com/profile.jpg",
                biography = "Actor biography",
                birthDate = LocalDate(2002, 2, 22),
                placeOfBirth = "Actor place of birth",
                deathDate = LocalDate(2052, 2, 22),
                headerPictures = listOf("https://example.com/profile.jpg"),
                department = "Actor department"
            ),
            characterName = "Character Name"
        )
        val expectedCastMemberUiState = EpisodeDetailsScreenState.GuestsOfHonerUiState(
            id = 1,
            name = "Actor Name",
            profilePictureURL = "https://example.com/profile.jpg",
            characterName = "Character Name"
        )

        val result = castMember.toUiState()

        assertThat(result).isEqualTo(expectedCastMemberUiState)
    }

    companion object {
        val episode = Episode(
            id = 1,
            title = "Episode Title",
            episodeNumber = 1,
            rating = 8.5,
            duration = "20 minutes",
            releasedDate = LocalDate(2002, 2, 22),
            trailerUrl = "https://example.com/trailer",
            currentSeason = 1,
            userRating = 7,
            overview = "Episode overview",
            genres = emptyList(),
            headerPictures = emptyList()
        )

        val expectedEpisodeUiState = EpisodeDetailsScreenState.EpisodeUiState(
            id = 1,
            title = "Episode Title",
            episodeNumber = 1,
            rating = 8,
            duration = "20 minutes",
            releasedDate = "22-02-2002",
            currentSeason = 1,
            overview = "Episode overview",
            headerPictures = emptyList(),
            userRating = 7,
            categories = emptyList()
        )
    }
}