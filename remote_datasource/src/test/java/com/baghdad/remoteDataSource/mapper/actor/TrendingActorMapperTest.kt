package com.baghdad.remoteDataSource.mapper.actor

import com.baghdad.remoteDataSource.response.actor.TrendingActorDetails
import com.baghdad.remoteDataSource.response.actor.TrendingActorResponse
import com.baghdad.repository.model.ActorDto
import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

class TrendingActorMapperTest {

    @Test
    fun `should map actor details correctly when toDto is called`() {
        // When
        val dto: ActorDto = TRENDING_ACTOR_DETAILS.toDto()

        // Then
        assertThat(dto.id).isEqualTo(TRENDING_ACTOR_DETAILS.id)
        assertThat(dto.name).isEqualTo(TRENDING_ACTOR_DETAILS.name)
        assertThat(dto.imageUrl).isEqualTo(URL + TRENDING_ACTOR_DETAILS.profilePath)
        assertThat(dto.biography).isEmpty()
        assertThat(dto.birthdayDate).isNull()
        assertThat(dto.deathDate).isNull()
        assertThat(dto.placeOfBirth).isEmpty()
        assertThat(dto.headerPictures).isEmpty()
        assertThat(dto.department).isEmpty()
    }

    @Test
    fun `should handle nulls safely when toDto is called`() {
        // Given
        val details = TRENDING_ACTOR_DETAILS.copy(
            id = null,
            name = null,
            profilePath = null
        )

        // When
        val dto = details.toDto()

        // Then
        assertThat(dto.id).isEqualTo(0L)
        assertThat(dto.name).isEqualTo("")
        assertThat(dto.imageUrl).isEqualTo("https://image.tmdb.org/t/p/w500null")
    }

    @Test
    fun `should map multiple results correctly when toPagedActorDtos is called`() {
        // Given
        val response = TrendingActorResponse(
            page = 1,
            totalPages = 3,
            results = listOf(
                TrendingActorDetails(id = 1, name = "Actor 1", profilePath = "/a1.jpg"),
                TrendingActorDetails(id = 2, name = "Actor 2", profilePath = "/a2.jpg")
            )
        )

        // When
        val result = response.toPagedActorDtos()

        // Then
        assertThat(result.data).hasSize(2)
        assertThat(result.data[0].name).isEqualTo("Actor 1")
        assertThat(result.data[1].imageUrl).contains("/a2.jpg")
        assertThat(result.nextKey).isEqualTo(2)
        assertThat(result.prevKey).isNull()
    }

    @Test
    fun `should return empty list when results is null in toPagedActorDtos`() {
        // Given
        val response = TrendingActorResponse(
            page = 1,
            totalPages = 1,
            results = null
        )

        // When
        val result = response.toPagedActorDtos()

        // Then
        assertThat(result.data).isEmpty()
        assertThat(result.nextKey).isNull()
        assertThat(result.prevKey).isNull()
    }

    companion object {

        const val URL = "https://image.tmdb.org/t/p/w500"

        val TRENDING_ACTOR_DETAILS = TrendingActorDetails(
            id = 1,
            name = "test",
            profilePath = "/profile.jpg",
            knownForDepartment = "acting"
        )
    }
}
