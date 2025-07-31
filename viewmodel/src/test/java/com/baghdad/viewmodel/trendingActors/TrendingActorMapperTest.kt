package com.baghdad.viewmodel.trendingActors

import com.baghdad.entity.person.Actor
import kotlinx.datetime.LocalDate
import org.junit.jupiter.api.Test
import com.google.common.truth.Truth.assertThat

class TrendingActorMapperTest {

    @Test
    fun `should map correctly when Actor is valid`() {
        val actor = Actor(
            id = 123L,
            name = "Leonardo DiCaprio",
            profilePictureURL = "https://example.com/profiles/leonardo.jpg",
            birthDate = LocalDate(1974, 11, 11),
            placeOfBirth = "Los Angeles, California",
            deathDate = null,
            biography = "Bio of Leonardo DiCaprio",
            headerPictures = listOf("header1.jpg", "header2.jpg"),
            department = "Acting"
        )

        val uiState = actor.toTrendingActorsUi()

        assertThat(uiState.id).isEqualTo(123L)
        assertThat(uiState.name).isEqualTo("Leonardo DiCaprio")
        assertThat(uiState.profilePictureURL).isEqualTo("https://example.com/profiles/leonardo.jpg")
    }

    @Test
    fun `should map correctly when name is empty`() {
        val actor = Actor(
            id = 456L,
            name = "",
            profilePictureURL = "https://example.com/profiles/unknown.jpg",
            birthDate = LocalDate(1974, 11, 11),
            placeOfBirth = "Los Angeles, California",
            deathDate = null,
            biography = "Bio of Leonardo DiCaprio",
            headerPictures = listOf("header1.jpg", "header2.jpg"),
            department = "Acting"
        )

        val uiState = actor.toTrendingActorsUi()

        assertThat(uiState.id).isEqualTo(456L)
        assertThat(uiState.name).isEqualTo("")
        assertThat(uiState.profilePictureURL).isEqualTo("https://example.com/profiles/unknown.jpg")
    }

    @Test
    fun `should map correctly when profilePictureURL is empty`() {
        val actor = Actor(
            id = 789L,
            name = "Unknown Actor",
            profilePictureURL = "",
            birthDate = LocalDate(1974, 11, 11),
            placeOfBirth = "Los Angeles, California",
            deathDate = null,
            biography = "Bio of Leonardo DiCaprio",
            headerPictures = listOf("header1.jpg", "header2.jpg"),
            department = "Acting"
        )

        val uiState = actor.toTrendingActorsUi()

        assertThat(uiState.id).isEqualTo(789L)
        assertThat(uiState.name).isEqualTo("Unknown Actor")
        assertThat(uiState.profilePictureURL).isEqualTo("")
    }

    @Test
    fun `should map correctly when name is long`() {
        val longName =
            "Very Long Actor Name That Might Be Used In Some Special Cases Or Testing Scenarios"
        val actor = Actor(
            id = 999L,
            name = longName,
            profilePictureURL = "https://example.com/profiles/long-name-actor.jpg",
            birthDate = LocalDate(1974, 11, 11),
            placeOfBirth = "Los Angeles, California",
            deathDate = null,
            biography = "Bio of Leonardo DiCaprio",
            headerPictures = listOf("header1.jpg", "header2.jpg"),
            department = "Acting"
        )

        val uiState = actor.toTrendingActorsUi()

        assertThat(uiState.id).isEqualTo(999L)
        assertThat(uiState.name).isEqualTo(longName)
        assertThat(uiState.profilePictureURL).isEqualTo("https://example.com/profiles/long-name-actor.jpg")
    }

    @Test
    fun `should map correctly when profilePictureURL is long`() {
        val longUrl =
            "https://very-long-domain-name.example.com/api/v1/images/profiles/high-resolution/actor-profile-image-12345.jpg"
        val actor = Actor(
            id = 1000L,
            name = "Actor With Long URL",
            profilePictureURL = longUrl,
            birthDate = LocalDate(1974, 11, 11),
            placeOfBirth = "Los Angeles, California",
            deathDate = null,
            biography = "Bio of Leonardo DiCaprio",
            headerPictures = listOf("header1.jpg", "header2.jpg"),
            department = "Acting"
        )

        val uiState = actor.toTrendingActorsUi()

        assertThat(uiState.id).isEqualTo(1000L)
        assertThat(uiState.name).isEqualTo("Actor With Long URL")
        assertThat(uiState.profilePictureURL).isEqualTo(longUrl)
    }

    @Test
    fun `should map correctly when name has special characters`() {
        val specialName = "Zoë Saldaña-Perez"
        val actor = Actor(
            id = 2000L,
            name = specialName,
            profilePictureURL = "https://example.com/profiles/zoe.jpg",
            birthDate = LocalDate(1974, 11, 11),
            placeOfBirth = "Los Angeles, California",
            deathDate = null,
            biography = "Bio of Leonardo DiCaprio",
            headerPictures = listOf("header1.jpg", "header2.jpg"),
            department = "Acting"
        )

        val uiState = actor.toTrendingActorsUi()

        assertThat(uiState.id).isEqualTo(2000L)
        assertThat(uiState.name).isEqualTo(specialName)
        assertThat(uiState.profilePictureURL).isEqualTo("https://example.com/profiles/zoe.jpg")
    }

    @Test
    fun `should map correctly when id is zero`() {
        val actor = Actor(
            id = 0L,
            name = "Zero ID Actor",
            profilePictureURL = "https://example.com/profiles/zero.jpg",
            birthDate = LocalDate(1974, 11, 11),
            placeOfBirth = "Los Angeles, California",
            deathDate = null,
            biography = "Bio of Leonardo DiCaprio",
            headerPictures = listOf("header1.jpg", "header2.jpg"),
            department = "Acting"
        )

        val uiState = actor.toTrendingActorsUi()

        assertThat(uiState.id).isEqualTo(0L)
        assertThat(uiState.name).isEqualTo("Zero ID Actor")
        assertThat(uiState.profilePictureURL).isEqualTo("https://example.com/profiles/zero.jpg")
    }

    @Test
    fun `should map correctly when id is negative`() {
        val actor = Actor(
            id = -1L,
            name = "Negative ID Actor",
            profilePictureURL = "https://example.com/profiles/negative.jpg",
            birthDate = LocalDate(1974, 11, 11),
            placeOfBirth = "Los Angeles, California",
            deathDate = null,
            biography = "Bio of Leonardo DiCaprio",
            headerPictures = listOf("header1.jpg", "header2.jpg"),
            department = "Acting"
        )

        val uiState = actor.toTrendingActorsUi()

        assertThat(uiState.id).isEqualTo(-1L)
        assertThat(uiState.name).isEqualTo("Negative ID Actor")
        assertThat(uiState.profilePictureURL).isEqualTo("https://example.com/profiles/negative.jpg")
    }

    @Test
    fun `should map correctly when id is Long_MAX_VALUE`() {
        val actor = Actor(
            id = Long.MAX_VALUE,
            name = "Max ID Actor",
            profilePictureURL = "https://example.com/profiles/max.jpg",
            birthDate = LocalDate(1974, 11, 11),
            placeOfBirth = "Los Angeles, California",
            deathDate = null,
            biography = "Bio of Leonardo DiCaprio",
            headerPictures = listOf("header1.jpg", "header2.jpg"),
            department = "Acting"
        )

        val uiState = actor.toTrendingActorsUi()

        assertThat(uiState.id).isEqualTo(Long.MAX_VALUE)
        assertThat(uiState.name).isEqualTo("Max ID Actor")
        assertThat(uiState.profilePictureURL).isEqualTo("https://example.com/profiles/max.jpg")
    }

    @Test
    fun `should map correctly when all values are empty`() {
        val actor = Actor(
            id = 0L,
            name = "",
            profilePictureURL = "",
            birthDate = LocalDate(1974, 11, 11),
            placeOfBirth = "Los Angeles, California",
            deathDate = null,
            biography = "Bio of Leonardo DiCaprio",
            headerPictures = listOf("header1.jpg", "header2.jpg"),
            department = "Acting"
        )

        val uiState = actor.toTrendingActorsUi()

        assertThat(uiState.id).isEqualTo(0L)
        assertThat(uiState.name).isEqualTo("")
        assertThat(uiState.profilePictureURL).isEqualTo("")
    }

    @Test
    fun `should map multiple actors correctly when called independently`() {
        val actor1 = Actor(
            id = 1L,
            name = "Actor One",
            profilePictureURL = "https://example.com/profiles/actor1.jpg",
            birthDate = LocalDate(1974, 11, 11),
            placeOfBirth = "Los Angeles, California",
            deathDate = null,
            biography = "Bio of Leonardo DiCaprio",
            headerPictures = listOf("header1.jpg", "header2.jpg"),
            department = "Acting"
        )

        val uiState1 = actor1.toTrendingActorsUi()

        assertThat(uiState1.id).isEqualTo(1L)
        assertThat(uiState1.name).isEqualTo("Actor One")
        assertThat(uiState1.profilePictureURL).isEqualTo("https://example.com/profiles/actor1.jpg")

    }

    @Test
    fun `should map correctly when name has whitespace`() {
        val nameWithWhitespace = "  Robert   Downey   Jr.  "
        val actor = Actor(
            id = 3000L,
            name = nameWithWhitespace,
            profilePictureURL = "https://example.com/profiles/rdj.jpg",
            birthDate = LocalDate(1974, 11, 11),
            placeOfBirth = "Los Angeles, California",
            deathDate = null,
            biography = "Bio of Leonardo DiCaprio",
            headerPictures = listOf("header1.jpg", "header2.jpg"),
            department = "Acting"
        )

        val uiState = actor.toTrendingActorsUi()

        assertThat(uiState.id).isEqualTo(3000L)
        assertThat(uiState.name).isEqualTo(nameWithWhitespace)
        assertThat(uiState.profilePictureURL).isEqualTo("https://example.com/profiles/rdj.jpg")
    }

    @Test
    fun `should map correctly when name has numeric characters`() {
        val nameWithNumbers = "R2-D2 Voice Actor"
        val actor = Actor(
            id = 4000L,
            name = nameWithNumbers,
            profilePictureURL = "https://example.com/profiles/r2d2.jpg",
            birthDate = LocalDate(1974, 11, 11),
            placeOfBirth = "Los Angeles, California",
            deathDate = null,
            biography = "Bio of Leonardo DiCaprio",
            headerPictures = listOf("header1.jpg", "header2.jpg"),
            department = "Acting"
        )

        val uiState = actor.toTrendingActorsUi()

        assertThat(uiState.id).isEqualTo(4000L)
        assertThat(uiState.name).isEqualTo(nameWithNumbers)
        assertThat(uiState.profilePictureURL).isEqualTo("https://example.com/profiles/r2d2.jpg")
    }

    @Test
    fun `should preserve original object immutability when mapping to uiState`() {
        val originalActor = Actor(
            id = 5000L,
            name = "Original Actor",
            profilePictureURL = "https://example.com/profiles/original.jpg",
            birthDate = LocalDate(1974, 11, 11),
            placeOfBirth = "Los Angeles, California",
            deathDate = null,
            biography = "Bio of Leonardo DiCaprio",
            headerPictures = listOf("header1.jpg", "header2.jpg"),
            department = "Acting"
        )

        val uiState = originalActor.toTrendingActorsUi()

        assertThat(uiState.id).isEqualTo(originalActor.id)
        assertThat(uiState.name).isEqualTo(originalActor.name)
        assertThat(uiState.profilePictureURL).isEqualTo(originalActor.profilePictureURL)

    }
}
