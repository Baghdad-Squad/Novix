package com.baghdad.viewmodel.mapper

import com.baghdad.entity.media.Episode
import com.baghdad.entity.media.Genre
import com.baghdad.entity.media.TvShow
import com.baghdad.entity.person.Actor
import com.baghdad.entity.person.CastMember
import com.baghdad.viewmodel.tvShowDetails.toUiState
import com.google.common.truth.Truth.assertThat
import kotlinx.datetime.LocalDate
import org.junit.jupiter.api.Test

class TvShowDetailsMapperTest {

    @Test
    fun `TvShow toUiState should map correctly`() {
        val result = tvShow.toUiState()

        assertThat(result.title).isEqualTo("Test TV Show")
        assertThat(result.genres).hasSize(1)
        assertThat(result.genres[0].name).isEqualTo("Drama")
        assertThat(result.rating).isEqualTo(9.0)
        assertThat(result.releaseDate).isNotEmpty()
        assertThat(result.seasonCount).isEqualTo(3)
        assertThat(result.overView).isEqualTo("Test TV show overview")
        assertThat(result.trailerURL).isEqualTo("https://example.com/trailer.mp4")
        assertThat(result.posterPictureURL).isEqualTo("https://example.com/poster.jpg")
    }

    @Test
    fun `Genre toUiState should map correctly`() {
        val result = genre.toUiState()

        assertThat(result.id).isEqualTo(1L)
        assertThat(result.name).isEqualTo("Drama")
    }

    @Test
    fun `CastMember toUiState should map correctly`() {
        val result = castMember.toUiState()

        assertThat(result.id).isEqualTo(789L)
        assertThat(result.name).isEqualTo("John Doe")
        assertThat(result.imageUrl).isEqualTo("https://example.com/profile.jpg")
        assertThat(result.characterName).isEqualTo("Character Name")
    }

    @Test
    fun `Episode toUiState should map correctly`() {
        val result = episode.toUiState()

        assertThat(result.id).isEqualTo(101L)
        assertThat(result.name).isEqualTo("Episode Title")
        assertThat(result.episodeNumber).isEqualTo(5)
        assertThat(result.rating).isEqualTo(8.5)
        assertThat(result.duration).isEqualTo(45)
        assertThat(result.releaseDate).isNotEmpty()
        assertThat(result.currentSeason).isEqualTo(2)
    }

    @Test
    fun `Episode toUiState should handle null release date`() {
        val episodeWithNullDate = episode.copy(releasedDate = null)
        val result = episodeWithNullDate.toUiState()

        assertThat(result.releaseDate).isEmpty()
    }

    @Test
    fun `Episode toUiState should handle invalid duration string`() {
        val episodeWithInvalidDuration = episode.copy(duration = "invalid")
        val result = episodeWithInvalidDuration.toUiState()

        assertThat(result.duration).isEqualTo(0)
    }

    private val genre = Genre(1L, "Drama")

    private val tvShow = TvShow(
        id = 456L,
        title = "Test TV Show",
        genres = listOf(genre),
        averageRating = 9.0,
        userRating = null,
        releaseDate = LocalDate.parse("2023-02-20"),
        overview = "Test TV show overview",
        posterImageURL = "https://example.com/poster.jpg",
        trailerURL = "https://example.com/trailer.mp4",
        headerImagesURLs = listOf("https://example.com/header1.jpg"),
        numberOfSeasons = 3
    )

    private val actor = Actor(
        id = 789L,
        name = "John Doe",
        profilePictureURL = "https://example.com/profile.jpg",
        biography = "Actor biography",
        birthDate = LocalDate.parse("1980-01-01"),
        placeOfBirth = "New York, USA",
        deathDate = null,
        department = "Acting",
        headerPictures = listOf("https://example.com/header1.jpg")
    )

    private val castMember = CastMember(
        actor = actor,
        characterName = "Character Name"
    )

    private val episode = Episode(
        id = 101L,
        title = "Episode Title",
        episodeNumber = 5,
        rating = 8.5,
        duration = "45",
        releasedDate = LocalDate.parse("2023-03-15"),
        currentSeason = 2,
        overview = "Test episode overview",
        headerPictures = listOf("/header1.jpg", "/header2.jpg"),
        trailerUrl = "https://example.com/trailer.mp4",
        genres = emptyList(),
    )
}