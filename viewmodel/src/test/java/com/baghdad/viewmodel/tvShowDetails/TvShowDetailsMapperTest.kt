package com.baghdad.viewmodel.tvShowDetails

import com.baghdad.entity.media.Episode
import com.baghdad.entity.media.Genre
import com.baghdad.entity.media.TvShow
import com.baghdad.entity.person.Actor
import com.baghdad.entity.person.CastMember
import com.google.common.truth.Truth.assertThat
import kotlinx.datetime.LocalDate
import org.junit.jupiter.api.Test

class TvShowMapperTest {

    @Test
    fun `Genre toUiState should map all fields correctly`() {
        val genre = Genre(
            id = genreId1,
            name = genreName1
        )

        val result = genre.toUiState()

        assertThat(genreId1).isEqualTo(result.id)
        assertThat(genreName1).isEqualTo(result.name)
    }

    @Test
    fun `CastMember toUiState should map all fields correctly`() {
        val actor = Actor(
            id = actorId,
            name = actorName,
            profilePictureURL = actorImageUrl,
            birthDate = LocalDate.parse("1956-03-18"),
            placeOfBirth = "California",
            deathDate = null,
            biography = "",
            headerPictures = emptyList(),
            department = ""
        )
        val castMember = CastMember(
            actor = actor,
            characterName = characterName
        )

        val result = castMember.toUiState()

        assertThat(actorId).isEqualTo(result.id)
        assertThat(actorName).isEqualTo(result.name)
        assertThat(actorImageUrl).isEqualTo(result.imageUrl)
        assertThat(characterName).isEqualTo(result.characterName)
    }

    @Test
    fun `TvShow toUiState with zero rating should map correctly`() {
        val tvShow = TvShow(
            id = tvShowId,
            title = tvShowTitle,
            genres = emptyList(),
            averageRating = 0.0,
            releaseDate = LocalDate.parse("2002-04-26"),
            numberOfSeasons = 0,
            overview = "",
            trailerURL = "",
            posterImageURL = "",
            headerImagesURLs = emptyList(),
            userRating = null
        )

        val result = tvShow.toUiState()

        assertThat(result.rating).isEqualTo(0.0)
        assertThat(result.seasonCount).isEqualTo(0)
        assertThat(result.overView).isEqualTo("")
        assertThat(result.trailerURL).isEqualTo("")
        assertThat(result.posterPictureURL).isEqualTo("")
    }

    @Test
    fun `Episode toUiState with zero values should map correctly`() {
        val episode = Episode(
            id = episodeId,
            title = "",
            episodeNumber = 0,
            rating = 0.0,
            duration = "",
            releasedDate = LocalDate.parse("2002-04-26"),
            currentSeason = 0,
            overview = "",
            genres = emptyList(),
            headerPictures = emptyList(),
            userRating = 0,
            trailerUrl = ""
        )

        val result = episode.toUiState()

        assertThat(episodeId).isEqualTo(result.id)
        assertThat("").isEqualTo(result.name)
        assertThat(0).isEqualTo(result.episodeNumber)
        assertThat(0.0).isEqualTo(result.rating)
        assertThat(0).isEqualTo(result.duration)
        assertThat(0).isEqualTo(result.currentSeason)
    }

    @Test
    fun `multiple genres should be mapped in correct order`() {
        val genres = listOf(
            Genre(id = genreId1, name = genreName1),
            Genre(id = genreId2, name = genreName2),
            Genre(id = genreId3, name = genreName3)
        )
        val tvShow = TvShow(
            id = tvShowId,
            title = tvShowTitle,
            genres = genres,
            averageRating = tvShowRating,
            releaseDate = LocalDate.parse("2002-04-04"),
            numberOfSeasons = seasonCount,
            overview = tvShowOverview,
            trailerURL = trailerUrl,
            posterImageURL = posterUrl,
            headerImagesURLs = emptyList(),
            userRating = null
        )

        val result = tvShow.toUiState()

        assertThat(3).isEqualTo(result.genres.size)
        assertThat(genreId1).isEqualTo(result.genres[0].id)
        assertThat(genreName1).isEqualTo(result.genres[0].name)
        assertThat(genreId2).isEqualTo(result.genres[1].id)
        assertThat(genreName2).isEqualTo(result.genres[1].name)
        assertThat(genreId3).isEqualTo(result.genres[2].id)
        assertThat(genreName3).isEqualTo(result.genres[2].name)
    }

    @Test
    fun `CastMember toUiState with empty strings should map correctly`() {
        val actor = Actor(
            id = actorId,
            name = "",
            profilePictureURL = "",
            birthDate = LocalDate.parse("1956-03-18"),
            placeOfBirth = "",
            deathDate = null,
            biography = "",
            headerPictures = emptyList(),
            department = ""
        )
        val castMember = CastMember(
            actor = actor,
            characterName = ""
        )

        val result = castMember.toUiState()

        assertThat(actorId).isEqualTo(result.id)
        assertThat("").isEqualTo(result.name)
        assertThat("").isEqualTo(result.imageUrl)
        assertThat("").isEqualTo(result.characterName)
    }

    private companion object {
        const val tvShowId = 123L
        const val tvShowTitle = "Breaking Bad"
        const val tvShowRating = 9.5
        const val seasonCount = 5
        const val tvShowOverview = "A high school chemistry teacher turned methamphetamine producer"
        const val trailerUrl = "https://trailer.com/breakingbad"
        const val posterUrl = "https://poster.com/breakingbad.jpg"


        const val genreId1 = 1L
        const val genreName1 = "Drama"
        const val genreId2 = 2L
        const val genreName2 = "Crime"
        const val genreId3 = 3L
        const val genreName3 = "Thriller"

        const val actorId = 456L
        const val actorName = "Bryan Cranston"
        const val actorImageUrl = "https://actor.com/bryan.jpg"
        const val characterName = "Walter White"

        const val episodeId = 789L

    }
}