package com.baghdad.remoteDataSource.mapper.search

import com.baghdad.remoteDataSource.response.search.ActorSearchResponse
import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

class ActorSearchResponseMapperTest {

    @Test
    fun `should map actors correctly when results contain valid data`() {
        // Given
        val response = ActorSearchResponse(
            page = 1,
            totalPages = 3,
            results = listOf(
                ActorSearchResponse.Result(
                    id = 101,
                    name = "Leonardo DiCaprio",
                    profilePath = "/leo.jpg",
                    knownForDepartment = "Acting"
                )
            )
        )

        // When
        val result = response.toPagedActorDtos()

        // Then
        assertThat(result.data).hasSize(1)
        val actor = result.data.first()
        assertThat(actor.id).isEqualTo(response.results?.first()?.id)
        assertThat(actor.name).isEqualTo(response.results?.first()?.name)
        assertThat(actor.imageUrl)
            .isEqualTo("https://image.tmdb.org/t/p/w500${response.results?.first()?.profilePath}")
        assertThat(actor.department)
            .isEqualTo(response.results?.first()?.knownForDepartment)
        assertThat(result.nextKey).isEqualTo(2)
        assertThat(result.prevKey).isNull()
    }

    @Test
    fun `should return empty list when results is null`() {
        // Given
        val response = ActorSearchResponse(
            page = 1,
            totalPages = 1,
            results = null
        )

        // When
        val result = response.toPagedActorDtos()

        // Then
        assertThat(result.data).isEmpty()
    }

    @Test
    fun `should use defaults when fields are null`() {
        // Given
        val response = ActorSearchResponse(
            page = 1,
            totalPages = 1,
            results = listOf(
                ActorSearchResponse.Result(
                    id = null,
                    name = null,
                    profilePath = null,
                    knownForDepartment = null
                )
            )
        )

        // When
        val result = response.toPagedActorDtos()

        // Then
        assertThat(result.data).isEmpty()
    }

    @Test
    fun `should skip actors when id is null`() {
        // Given
        val response = ActorSearchResponse(
            page = 2,
            totalPages = 5,
            results = listOf(
                ActorSearchResponse.Result(
                    id = null,
                    name = "No ID Actor",
                    profilePath = "/noid.jpg",
                    knownForDepartment = "Directing"
                )
            )
        )

        // When
        val result = response.toPagedActorDtos()

        // Then
        assertThat(result.data).isEmpty()
        assertThat(result.prevKey).isEqualTo(1)
    }
}
