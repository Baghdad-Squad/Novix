package com.baghdad.viewmodel.trendingActors

import com.baghdad.entity.person.Actor
import kotlinx.datetime.LocalDate
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.Test

class TrendingActorMapperViewModelTest {

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

        assertEquals(123L, uiState.id)
        assertEquals("Leonardo DiCaprio", uiState.name)
        assertEquals("https://example.com/profiles/leonardo.jpg", uiState.profilePictureURL)
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

        assertEquals(456L, uiState.id)
        assertEquals("", uiState.name)
        assertEquals("https://example.com/profiles/unknown.jpg", uiState.profilePictureURL)
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

        assertEquals(789L, uiState.id)
        assertEquals("Unknown Actor", uiState.name)
        assertEquals("", uiState.profilePictureURL)
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

        assertEquals(999L, uiState.id)
        assertEquals(longName, uiState.name)
        assertEquals("https://example.com/profiles/long-name-actor.jpg", uiState.profilePictureURL)
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

        assertEquals(1000L, uiState.id)
        assertEquals("Actor With Long URL", uiState.name)
        assertEquals(longUrl, uiState.profilePictureURL)
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

        assertEquals(2000L, uiState.id)
        assertEquals(specialName, uiState.name)
        assertEquals("https://example.com/profiles/zoe.jpg", uiState.profilePictureURL)
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

        assertEquals(0L, uiState.id)
        assertEquals("Zero ID Actor", uiState.name)
        assertEquals("https://example.com/profiles/zero.jpg", uiState.profilePictureURL)
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

        assertEquals(-1L, uiState.id)
        assertEquals("Negative ID Actor", uiState.name)
        assertEquals("https://example.com/profiles/negative.jpg", uiState.profilePictureURL)
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

        assertEquals(Long.MAX_VALUE, uiState.id)
        assertEquals("Max ID Actor", uiState.name)
        assertEquals("https://example.com/profiles/max.jpg", uiState.profilePictureURL)
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

        assertEquals(0L, uiState.id)
        assertEquals("", uiState.name)
        assertEquals("", uiState.profilePictureURL)
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

        val actor2 = Actor(
            id = 2L,
            name = "Actor Two",
            profilePictureURL = "https://example.com/profiles/actor2.jpg",
            birthDate = LocalDate(1974, 11, 11),
            placeOfBirth = "Los Angeles, California",
            deathDate = null,
            biography = "Bio of Leonardo DiCaprio",
            headerPictures = listOf("header1.jpg", "header2.jpg"),
            department = "Acting"
        )

        val uiState1 = actor1.toTrendingActorsUi()
        val uiState2 = actor2.toTrendingActorsUi()

        assertEquals(1L, uiState1.id)
        assertEquals("Actor One", uiState1.name)
        assertEquals("https://example.com/profiles/actor1.jpg", uiState1.profilePictureURL)

        assertEquals(2L, uiState2.id)
        assertEquals("Actor Two", uiState2.name)
        assertEquals("https://example.com/profiles/actor2.jpg", uiState2.profilePictureURL)

        assertNotEquals(uiState1.id, uiState2.id)
        assertNotEquals(uiState1.name, uiState2.name)
        assertNotEquals(uiState1.profilePictureURL, uiState2.profilePictureURL)
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

        assertEquals(3000L, uiState.id)
        assertEquals(nameWithWhitespace, uiState.name)
        assertEquals("https://example.com/profiles/rdj.jpg", uiState.profilePictureURL)
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

        assertEquals(4000L, uiState.id)
        assertEquals(nameWithNumbers, uiState.name)
        assertEquals("https://example.com/profiles/r2d2.jpg", uiState.profilePictureURL)
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

        assertEquals(originalActor.id, uiState.id)
        assertEquals(originalActor.name, uiState.name)
        assertEquals(originalActor.profilePictureURL, uiState.profilePictureURL)

        assertEquals(5000L, originalActor.id)
        assertEquals("Original Actor", originalActor.name)
        assertEquals("https://example.com/profiles/original.jpg", originalActor.profilePictureURL)
    }
}
