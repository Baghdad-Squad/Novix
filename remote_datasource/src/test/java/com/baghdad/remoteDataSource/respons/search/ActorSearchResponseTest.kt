package com.baghdad.remoteDataSource.respons.search

import com.baghdad.remoteDataSource.response.search.ActorSearchResponse
import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test


class ActorSearchResponseTest {

    @Test
    fun `should create ActorSearchResponse with full data when all fields are provided`() {
        // Given
        val expectedResults = listOf(
            ActorSearchResponse.Result(
                adult = false,
                gender = 2,
                id = 101,
                knownForDepartment = "Acting",
                name = "Leonardo DiCaprio",
                originalName = "Leonardo Wilhelm DiCaprio",
                popularity = 99.9,
                profilePath = "/leonardo.jpg"
            ),
            ActorSearchResponse.Result(
                adult = true,
                gender = 1,
                id = 202,
                knownForDepartment = "Directing",
                name = "Sofia Coppola",
                originalName = "Sofia Carmina Coppola",
                popularity = 85.5,
                profilePath = "/sofia.jpg"
            )
        )

        // When
        val response = ActorSearchResponse(
            page = 1,
            results = expectedResults,
            totalPages = 3,
            totalResults = 60
        )

        // Then
        assertThat(response.page).isEqualTo(1)
        assertThat(response.totalPages).isEqualTo(3)
        assertThat(response.totalResults).isEqualTo(60)
        assertThat(response.results).hasSize(2)

        val first = response.results!![0]
        assertThat(first!!.id).isEqualTo(101)
        assertThat(first.name).isEqualTo("Leonardo DiCaprio")
        assertThat(first.gender).isEqualTo(2)
        assertThat(first.knownForDepartment).isEqualTo("Acting")
        assertThat(first.originalName).isEqualTo("Leonardo Wilhelm DiCaprio")
        assertThat(first.popularity).isEqualTo(99.9)
        assertThat(first.profilePath).isEqualTo("/leonardo.jpg")
    }

    @Test
    fun `should create ActorSearchResponse with default values when no fields are provided`() {
        // Given & When
        val response = ActorSearchResponse()

        // Then
        assertThat(response.page).isNull()
        assertThat(response.results).isNull()
        assertThat(response.totalPages).isNull()
        assertThat(response.totalResults).isNull()
    }

    @Test
    fun `should create Result with default values when no fields are provided`() {
        // Given & When
        val result = ActorSearchResponse.Result()

        // Then
        assertThat(result.adult).isNull()
        assertThat(result.gender).isNull()
        assertThat(result.id).isNull()
        assertThat(result.knownForDepartment).isNull()
        assertThat(result.name).isNull()
        assertThat(result.originalName).isNull()
        assertThat(result.popularity).isNull()
        assertThat(result.profilePath).isNull()
    }

    @Test
    fun `should create ActorSearchResponse with empty results list when results provided as empty`() {
        // Given
        val response = ActorSearchResponse(
            page = 2,
            results = emptyList(),
            totalPages = 0,
            totalResults = 0
        )

        // Then
        assertThat(response.page).isEqualTo(2)
        assertThat(response.results).isEmpty()
        assertThat(response.totalPages).isEqualTo(0)
        assertThat(response.totalResults).isEqualTo(0)
    }

    @Test
    fun `should create Result with partial data when some fields are provided`() {
        // When
        val result = ActorSearchResponse.Result(
            name = "Emma Watson",
            popularity = 88.8
        )

        // Then
        assertThat(result.id).isNull()
        assertThat(result.name).isEqualTo("Emma Watson")
        assertThat(result.popularity).isEqualTo(88.8)
        assertThat(result.gender).isNull()
        assertThat(result.knownForDepartment).isNull()
    }

    @Test
    fun `should handle null result inside results list`() {
        // Given
        val response = ActorSearchResponse(
            page = 3,
            results = listOf(null),
            totalPages = 1,
            totalResults = 1
        )

        // Then
        assertThat(response.page).isEqualTo(3)
        assertThat(response.results).hasSize(1)
        assertThat(response.results!![0]).isNull()
        assertThat(response.totalPages).isEqualTo(1)
        assertThat(response.totalResults).isEqualTo(1)
    }
}