package com.baghdad.viewmodel.trendingActor

import com.baghdad.entity.person.Actor
import com.baghdad.viewmodel.trendingActors.toTrendingActorsUi
import kotlinx.datetime.LocalDate
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class TrendingActorsMapperKtTest {
    @Test
    fun `should map Actor to TrendingActorUiState`() {
        val trendingActor = Actor(
            id = 22,
            name = "Mahmoud Tarek",
            profilePictureURL = "Test",
            birthDate = LocalDate.Companion.parse("2002-02-22"),
            placeOfBirth = "Egypt",
            deathDate = LocalDate.Companion.parse("2052-02-02"),
            biography = "Test",
            headerPictures = emptyList(),
            department = "Test"
        )

        val result = trendingActor.toTrendingActorsUi()

        Assertions.assertEquals(trendingActor.id, result.id)
        Assertions.assertEquals(trendingActor.name, result.name)
        Assertions.assertEquals(trendingActor.profilePictureURL, result.profilePictureURL)
    }
}