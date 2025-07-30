package com.baghdad.remoteDataSource.mapper.actor

import com.baghdad.remoteDataSource.response.actor.TrendingActorDetails
import com.baghdad.remoteDataSource.response.actor.TrendingActorResponse
import com.baghdad.repository.model.ActorDto
import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

class TrendingActorMapperTest {

    @Test
    fun `toDto should map actor details correctly`() {
        val details = TrendingActorDetails(
            id = 123,
            name = "carlos sanchez",
            profilePath = "/profile.jpg",
            knownForDepartment = "Acting"
        )

        val dto: ActorDto = details.toDto()

        assertThat(dto.id).isEqualTo(123L)
        assertThat(dto.name).isEqualTo("carlos sanchez")
        assertThat(dto.imageUrl).isEqualTo("https://image.tmdb.org/t/p/w500/profile.jpg")
        assertThat(dto.biography).isEmpty()
        assertThat(dto.birthdayDate).isNull()
        assertThat(dto.deathDate).isNull()
        assertThat(dto.placeOfBirth).isEmpty()
        assertThat(dto.headerPictures).isEmpty()
        assertThat(dto.department).isEmpty()
    }

    @Test
    fun `toDto should handle nulls safely`() {
        val details = TrendingActorDetails(
            id = null,
            name = null,
            profilePath = null
        )

        val dto = details.toDto()

        assertThat(dto.id).isEqualTo(0L)
        assertThat(dto.name).isEqualTo("")
        assertThat(dto.imageUrl).isEqualTo("https://image.tmdb.org/t/p/w500null")
    }

    @Test
    fun `toPagedActorDtos should map multiple results correctly`() {
        val response = TrendingActorResponse(
            page = 1,
            totalPages = 3,
            results = listOf(
                TrendingActorDetails(id = 1, name = "Actor 1", profilePath = "/a1.jpg"),
                TrendingActorDetails(id = 2, name = "Actor 2", profilePath = "/a2.jpg")
            )
        )

        val result = response.toPagedActorDtos()

        assertThat(result.data).hasSize(2)
        assertThat(result.data[0].name).isEqualTo("Actor 1")
        assertThat(result.data[1].imageUrl).contains("/a2.jpg")
        assertThat(result.nextKey).isEqualTo(2)
        assertThat(result.prevKey).isNull()
    }

    @Test
    fun `toPagedActorDtos should return empty list if results is null`() {
        val response = TrendingActorResponse(
            page = 1,
            totalPages = 1,
            results = null
        )

        val result = response.toPagedActorDtos()

        assertThat(result.data).isEmpty()
        assertThat(result.nextKey).isNull()
        assertThat(result.prevKey).isNull()
    }
}
