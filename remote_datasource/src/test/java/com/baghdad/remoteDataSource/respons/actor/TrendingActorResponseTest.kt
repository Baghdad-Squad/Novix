package com.baghdad.remoteDataSource.respons.actor

import com.baghdad.remoteDataSource.response.actor.TrendingActorDetails
import com.baghdad.remoteDataSource.response.actor.TrendingActorResponse
import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

class TrendingActorResponseTest {

    @Test
    fun `should create TrendingActorResponse with full data when all fields are provided`() {
        // Given
        val expectedResults = listOf(
            TrendingActorDetails(
                adult = false,
                id = 1,
                name = "Leonardo DiCaprio",
                originalName = "Leonardo Wilhelm DiCaprio",
                mediaType = "person",
                popularity = 99.9,
                gender = 2,
                knownForDepartment = "Acting",
                profilePath = "/leo.jpg"
            ),
            TrendingActorDetails(
                adult = false,
                id = 2,
                name = "Scarlett Johansson",
                originalName = "Scarlett Ingrid Johansson",
                mediaType = "person",
                popularity = 95.4,
                gender = 1,
                knownForDepartment = "Acting",
                profilePath = "/scarlett.jpg"
            )
        )

        // When
        val trendingActorResponse = TrendingActorResponse(
            page = 1,
            results = expectedResults,
            totalPages = 10,
            totalResults = 200
        )

        // Then
        assertThat(trendingActorResponse.page).isEqualTo(1)
        assertThat(trendingActorResponse.results).isNotNull()
        assertThat(trendingActorResponse.results!!.size).isEqualTo(2)
        assertThat(trendingActorResponse.totalPages).isEqualTo(10)
        assertThat(trendingActorResponse.totalResults).isEqualTo(200)

        val firstActor = trendingActorResponse.results!![0]
        assertThat(firstActor.adult).isFalse()
        assertThat(firstActor.id).isEqualTo(1)
        assertThat(firstActor.name).isEqualTo("Leonardo DiCaprio")
        assertThat(firstActor.originalName).isEqualTo("Leonardo Wilhelm DiCaprio")
        assertThat(firstActor.mediaType).isEqualTo("person")
        assertThat(firstActor.popularity).isEqualTo(99.9)
        assertThat(firstActor.gender).isEqualTo(2)
        assertThat(firstActor.knownForDepartment).isEqualTo("Acting")
        assertThat(firstActor.profilePath).isEqualTo("/leo.jpg")
    }

    @Test
    fun `should create TrendingActorResponse with default values when no fields are provided`() {
        // Given & When
        val trendingActorResponse = TrendingActorResponse()

        // Then
        assertThat(trendingActorResponse.page).isNull()
        assertThat(trendingActorResponse.results).isNull()
        assertThat(trendingActorResponse.totalPages).isNull()
        assertThat(trendingActorResponse.totalResults).isNull()
    }

    @Test
    fun `should create TrendingActorDetails with default values when no fields are provided`() {
        // Given & When
        val actorDetails = TrendingActorDetails()

        // Then
        assertThat(actorDetails.adult).isNull()
        assertThat(actorDetails.id).isNull()
        assertThat(actorDetails.name).isNull()
        assertThat(actorDetails.originalName).isNull()
        assertThat(actorDetails.mediaType).isNull()
        assertThat(actorDetails.popularity).isNull()
        assertThat(actorDetails.gender).isNull()
        assertThat(actorDetails.knownForDepartment).isNull()
        assertThat(actorDetails.profilePath).isNull()
    }

    @Test
    fun `should create TrendingActorResponse with empty results list when results provided as empty`() {
        // Given
        val expectedResults = emptyList<TrendingActorDetails>()

        // When
        val trendingActorResponse = TrendingActorResponse(
            page = 1,
            results = expectedResults,
            totalPages = 5,
            totalResults = 0
        )

        // Then
        assertThat(trendingActorResponse.page).isEqualTo(1)
        assertThat(trendingActorResponse.results).isEmpty()
        assertThat(trendingActorResponse.totalPages).isEqualTo(5)
        assertThat(trendingActorResponse.totalResults).isEqualTo(0)
    }

    @Test
    fun `should create TrendingActorDetails with partial data when some fields are provided`() {
        // When
        val actorDetails = TrendingActorDetails(
            name = "Tom Hardy",
            popularity = 87.6
        )

        // Then
        assertThat(actorDetails.adult).isNull()
        assertThat(actorDetails.id).isNull()
        assertThat(actorDetails.name).isEqualTo(actorDetails.name)
        assertThat(actorDetails.originalName).isNull()
        assertThat(actorDetails.mediaType).isNull()
        assertThat(actorDetails.popularity).isEqualTo(actorDetails.popularity)
        assertThat(actorDetails.gender).isNull()
        assertThat(actorDetails.knownForDepartment).isNull()
        assertThat(actorDetails.profilePath).isNull()
    }

    @Test
    fun `should have null page and totalPages when TrendingActorResponse created without providing values`() {
        // Given & When
        val response = TrendingActorResponse()

        // Then
        assertThat(response.page).isNull()
        assertThat(response.totalPages).isNull()
    }

    @Test
    fun `should set page and totalPages when values are provided`() {
        // When
        val response = TrendingActorResponse(
            page = 5,
            totalPages = 20
        )

        // Then
        assertThat(response.page).isEqualTo(5)
        assertThat(response.totalPages).isEqualTo(20)
    }

    @Test
    fun `should handle empty results list when results is provided as empty`() {
        // Given
        val response = TrendingActorResponse(results = emptyList())

        // Then
        assertThat(response.results).isEmpty()
    }
}