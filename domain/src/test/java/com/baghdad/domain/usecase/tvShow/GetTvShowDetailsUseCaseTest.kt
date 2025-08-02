package com.baghdad.domain.usecase.tvShow

import com.baghdad.domain.repository.TvShowRepository
import com.baghdad.entity.person.Actor
import com.baghdad.entity.person.CastMember
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.LocalDate
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class GetTvShowDetailsUseCaseTest {


    private lateinit var tvShowRepository: TvShowRepository
    private lateinit var getTvShowCastMembersUseCase: GetTvShowCastMembersUseCase

    @BeforeEach
    fun setup() {
        tvShowRepository = mockk()
        getTvShowCastMembersUseCase = GetTvShowCastMembersUseCase(tvShowRepository)
    }

    @Test
    fun `getTvShowCastMembersUseCase() should return cast members when repository returns data`() =
        runTest {
            val tvId = 100L
            coEvery { tvShowRepository.getTvShowCastMembers(tvId) } returns castMembers

            val result = getTvShowCastMembersUseCase(tvId)

            assertThat(result).isEqualTo(castMembers)
        }

    @Test
    fun `getTvShowCastMembersUseCase() should return empty list when no cast members exist`() =
        runTest {
            val tvId = 101L
            coEvery { tvShowRepository.getTvShowCastMembers(tvId) } returns emptyList()

            val result = getTvShowCastMembersUseCase(tvId)

            assertThat(result).isEmpty()
        }

    @Test
    fun `getTvShowCastMembersUseCase() should handle special characters in actor names`() =
        runTest {
            val tvId = 103L
            val specialCharacterCast = listOf(
                CastMember(
                    actor = Actor(
                        id = 4L,
                        name = "Édgar Ramírez",
                        profilePictureURL = "",
                        birthDate = LocalDate(1977, 3, 25),
                        placeOfBirth = "San Cristóbal, Venezuela",
                        deathDate = null,
                        biography = "",
                        headerPictures = emptyList(),
                        department = "Acting"
                    ),
                    characterName = "Giancarlo"
                )
            )
            coEvery { tvShowRepository.getTvShowCastMembers(tvId) } returns specialCharacterCast

            val result = getTvShowCastMembersUseCase(tvId)

            assertThat(result).hasSize(1)
            assertThat(result[0].actor.name).isEqualTo("Édgar Ramírez")
            assertThat(result[0].actor.placeOfBirth).contains("San Cristóbal")
        }

    @Test
    fun `getTvShowCastMembersUseCase() should return cast members with minimal required fields`() =
        runTest {
            val tvId = 104L
            val minimalCast = listOf(
                CastMember(
                    actor = Actor(
                        id = 5L,
                        name = "Unknown Actor",
                        profilePictureURL = "",
                        birthDate = LocalDate(2000, 1, 1),
                        placeOfBirth = "",
                        deathDate = null,
                        biography = "",
                        headerPictures = emptyList(),
                        department = "Unknown"
                    ),
                    characterName = "Extra"
                )
            )
            coEvery { tvShowRepository.getTvShowCastMembers(tvId) } returns minimalCast

            val result = getTvShowCastMembersUseCase(tvId)

            assertThat(result).hasSize(1)
            assertThat(result[0].actor.name).isEqualTo("Unknown Actor")
            assertThat(result[0].characterName).isEqualTo("Extra")
        }

    @Test
    fun `getTvShowCastMembersUseCase() should preserve all actor details in returned cast`() =
        runTest {
            val tvId = 105L
            coEvery { tvShowRepository.getTvShowCastMembers(tvId) } returns castMembers

            val result = getTvShowCastMembersUseCase(tvId)

            assertThat(result[0].actor.biography).isEqualTo("Bryan Cranston is an American actor, director, and producer.")
            assertThat(result[1].actor.birthDate).isEqualTo(LocalDate(1979, 8, 27))
            assertThat(result[0].characterName).isEqualTo("Walter White")
            assertThat(result[1].characterName).isEqualTo("Jesse Pinkman")
        }

    @Test
    fun `getTvShowCastMembersUseCase() should make exactly one repository call per invocation`() =
        runTest {
            val tvId = 106L
            coEvery { tvShowRepository.getTvShowCastMembers(tvId) } returns castMembers

            getTvShowCastMembersUseCase(tvId)

            coVerify(exactly = 1) { tvShowRepository.getTvShowCastMembers(tvId) }
        }

    companion object {
        val castMembers = listOf(
            CastMember(
                actor = Actor(
                    id = 1L,
                    name = "Bryan Cranston",
                    profilePictureURL = "https://example.com/profiles/bryan.jpg",
                    birthDate = LocalDate(1956, 3, 7),
                    placeOfBirth = "San Fernando Valley, California, USA",
                    deathDate = null,
                    biography = "Bryan Cranston is an American actor, director, and producer.",
                    headerPictures = emptyList(),
                    department = "Acting"
                ),
                characterName = "Walter White"
            ),
            CastMember(
                actor = Actor(
                    id = 2L,
                    name = "Aaron Paul",
                    profilePictureURL = "https://example.com/profiles/aaron.jpg",
                    birthDate = LocalDate(1979, 8, 27),
                    placeOfBirth = "Emmett, Idaho, USA",
                    deathDate = null,
                    biography = "Aaron Paul is an American actor best known for his role in Breaking Bad.",
                    headerPictures = emptyList(),
                    department = "Acting"
                ),
                characterName = "Jesse Pinkman"
            )
        )
    }
}