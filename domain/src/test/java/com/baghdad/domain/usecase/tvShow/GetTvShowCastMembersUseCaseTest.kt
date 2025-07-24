package com.baghdad.domain.usecase.tvShow

import com.baghdad.domain.repository.TvShowRepository
import com.baghdad.entity.person.Actor
import com.baghdad.entity.person.CastMember
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.LocalDate
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class GetTvShowCastMembersUseCaseTest {

    @BeforeEach
    fun setup() {
        tvShowRepository = mockk()
        getTvShowCastMembersUseCase = GetTvShowCastMembersUseCase(tvShowRepository)
    }

    @Test
    fun `getTvShowCastMembersUseCase should get tv show cast members when function runs successfully`() = runTest {
        val tvShowId = 1L
        coEvery { tvShowRepository.getTvShowCastMembers(tvShowId) } returns sampleCastMembers
        val result = getTvShowCastMembersUseCase.invoke(tvShowId)
        assertThat(result).isEqualTo(sampleCastMembers)
    }

    @Test
    fun `getTvShowCastMembersUseCase should return empty list when no cast members found`() = runTest {
        val tvShowId = 2L
        coEvery { tvShowRepository.getTvShowCastMembers(tvShowId) } returns emptyList()
        val result = getTvShowCastMembersUseCase.invoke(tvShowId)
        assertThat(result).isEmpty()
    }

    @Test
    fun ` getTvShowCastMembersUseCase should return cast with valid but minimal actor data`() = runTest {
        val tvShowId = 3L
        val minimalCast = listOf(
            CastMember(
                actor = Actor(
                    id = 104L,
                    name = "Unknown Actor",
                    profilePictureURL = "",
                    birthDate = LocalDate(2000, 1, 1),
                    placeOfBirth = "",
                    deathDate = null,
                    biography = "",
                    headerPictures = emptyList(),
                    department = "Unknown"
                ),
                characterName = "Background Character"
            )
        )

        coEvery { tvShowRepository.getTvShowCastMembers(tvShowId) } returns minimalCast

        val result = getTvShowCastMembersUseCase.invoke(tvShowId)

        assertThat(result).isEqualTo(minimalCast)
        assertThat(result[0].actor.name).isEqualTo("Unknown Actor")
        assertThat(result[0].characterName).isEqualTo("Background Character")
    }

    @Test
    fun `getTvShowCastMembersUseCase should return full cast with complete actor data`() = runTest {
        val tvShowId = 4L
        coEvery { tvShowRepository.getTvShowCastMembers(tvShowId) } returns sampleCastMembers
        val result = getTvShowCastMembersUseCase.invoke(tvShowId)

        assertThat(result).hasSize(3)
        assertThat(result[0].actor.name).isEqualTo("Peter Dinklage")
        assertThat(result[0].characterName).isEqualTo("Tyrion Lannister")
        assertThat(result[1].actor.name).isEqualTo("Lena Headey")
        assertThat(result[1].characterName).isEqualTo("Cersei Lannister")
        assertThat(result[2].actor.name).isEqualTo("Emilia Clarke")
        assertThat(result[2].characterName).isEqualTo("Daenerys Targaryen")
    }

    companion object {
        private lateinit var getTvShowCastMembersUseCase: GetTvShowCastMembersUseCase
        private lateinit var tvShowRepository: TvShowRepository

        private val sampleCastMembers = listOf(
            CastMember(
                actor = Actor(
                    id = 201L,
                    name = "Peter Dinklage",
                    profilePictureURL = "https://example.com/images/peter.jpg",
                    birthDate = LocalDate(1969, 6, 11),
                    placeOfBirth = "Morristown, New Jersey, USA",
                    deathDate = null,
                    biography = "Known for his role as Tyrion Lannister in Game of Thrones.",
                    headerPictures = listOf(
                        "https://example.com/images/peter-header1.jpg",
                        "https://example.com/images/peter-header2.jpg"
                    ),
                    department = "Acting"
                ),
                characterName = "Tyrion Lannister"
            ),
            CastMember(
                actor = Actor(
                    id = 202L,
                    name = "Lena Headey",
                    profilePictureURL = "https://example.com/images/lena.jpg",
                    birthDate = LocalDate(1973, 10, 3),
                    placeOfBirth = "Hamilton, Bermuda",
                    deathDate = null,
                    biography = "Known for her role as Cersei Lannister in Game of Thrones.",
                    headerPictures = listOf(
                        "https://example.com/images/lena-header1.jpg"
                    ),
                    department = "Acting"
                ),
                characterName = "Cersei Lannister"
            ),
            CastMember(
                actor = Actor(
                    id = 203L,
                    name = "Emilia Clarke",
                    profilePictureURL = "https://example.com/images/emilia.jpg",
                    birthDate = LocalDate(1986, 10, 23),
                    placeOfBirth = "London, England",
                    deathDate = null,
                    biography = "Known for her role as Daenerys Targaryen in Game of Thrones.",
                    headerPictures = emptyList(),
                    department = "Acting"
                ),
                characterName = "Daenerys Targaryen"
            )
        )
    }
}