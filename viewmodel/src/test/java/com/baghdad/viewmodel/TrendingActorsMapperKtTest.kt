package com.baghdad.viewmodel

import com.baghdad.entity.person.Actor
import com.baghdad.viewmodel.trendingActors.toTrendingActorsUi
import kotlinx.datetime.LocalDate
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class TrendingActorsMapperKtTest {
    @Test
    fun `should map Actor to TrendingActorUiState`() {
        // Given
        val trendingActor = Actor(
            id = 22,
            name = "Mahmoud Tarek",
            profilePictureURL = "Test",
            birthDate = LocalDate.parse("2002-02-22"),
            placeOfBirth = "Egypt",
            deathDate = LocalDate.parse("2052-02-02"),
            biography = "Test",
            headerPictures = emptyList(),
            department = "Test"
        )

        // When
        val result = trendingActor.toTrendingActorsUi()

        // Then
        assertEquals(trendingActor.id, result.id)
        assertEquals(trendingActor.name, result.name)
        assertEquals(trendingActor.profilePictureURL, result.profilePictureURL)
    }
}