package com.baghdad.domain.usecase.episode

import com.baghdad.domain.repository.EpisodeRepository
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

class GetEpisodeCastMembersUseCaseTest {

    private lateinit var episodeRepository: EpisodeRepository
    private lateinit var getEpisodeCastMembersUseCase: GetEpisodeCastMembersUseCase
    private val expectedCastMembers = listOf(
        CastMember(
            actor = Actor(
                id = 1L,
                name = "John Doe",
                profilePictureURL = "https://example.com/profile.jpg",
                birthDate = LocalDate(2020, 1, 1),
                placeOfBirth = "New York",
                deathDate = null,
                biography = "An actor known for his roles in various films.",
                headerPictures = listOf(
                    "https://example.com/header1.jpg",
                    "https://example.com/header2.jpg"
                ),
                department = "Acting",
            ),
            characterName = "Main Character",
        ),
        CastMember(
            actor = Actor(
                id = 2L,
                name = "Jane Smith",
                profilePictureURL = "https://example.com/profile2.jpg",
                birthDate = LocalDate(2019, 5, 15),
                placeOfBirth = "Los Angeles",
                deathDate = null,
                biography = "An actress known for her versatility.",
                headerPictures = listOf(
                    "https://example.com/header3.jpg",
                    "https://example.com/header4.jpg"
                ),
                department = "Acting",
            ),
            characterName = "Supporting Character",
        ),
        CastMember(
            actor = Actor(
                id = 3L,
                name = "Alice Johnson",
                profilePictureURL = "https://example.com/profile3.jpg",
                birthDate = LocalDate(2018, 3, 10),
                placeOfBirth = "Chicago",
                deathDate = null,
                biography = "An actress with a strong presence on screen.",
                headerPictures = listOf(
                    "https://example.com/header5.jpg",
                    "https://example.com/header6.jpg"
                ),
                department = "Acting",
            ),
            characterName = "Guest Star",
        )
    )

    @BeforeEach
    fun setUp() {
        episodeRepository = mockk(relaxed = true)
        getEpisodeCastMembersUseCase = GetEpisodeCastMembersUseCase(episodeRepository)
    }

    @Test
    fun `execute with valid tvId seasonNumber and episodeNumber returns expected cast members`() =
        runTest {
            // Given
            val tvId = 123L
            val seasonNumber = 2
            val episodeNumber = 1

            coEvery {
                episodeRepository.getEpisodeCastMembers(tvId, seasonNumber, episodeNumber)
            } returns expectedCastMembers

            // When
            val result = getEpisodeCastMembersUseCase(tvId, seasonNumber, episodeNumber)

            // Then
            assertThat(result).isEqualTo(expectedCastMembers)
            coVerify(exactly = 1) {
                episodeRepository.getEpisodeCastMembers(tvId, seasonNumber, episodeNumber)
            }
        }

    @Test
    fun `return empty list when not exist tv show`() = runTest {
        // Given
        val tvId = 999L
        val seasonNumber = 2
        val episodeNumber = 1

        coEvery {
            episodeRepository.getEpisodeCastMembers(tvId, seasonNumber, episodeNumber)
        } returns emptyList()


        val result = getEpisodeCastMembersUseCase(tvId, seasonNumber, episodeNumber)

        // Then
        assertThat(result).isEmpty()
        coVerify(exactly = 1) {
            episodeRepository.getEpisodeCastMembers(tvId, seasonNumber, episodeNumber)
        }
    }

    @Test
    fun `returns empty list when non-existent season number `() = runTest {
        // Given
        val tvId = 123L
        val nonExistentSeason = 999
        val episodeNumber = 1

        coEvery {
            episodeRepository.getEpisodeCastMembers(tvId, nonExistentSeason, episodeNumber)
        } returns emptyList()

        // When
        val result = getEpisodeCastMembersUseCase(tvId, nonExistentSeason, episodeNumber)

        // Then
        assertThat(result).isEmpty()
        coVerify(exactly = 1) {
            episodeRepository.getEpisodeCastMembers(tvId, nonExistentSeason, episodeNumber)
        }
    }


    @Test
    fun `returns empty list when non-existent episode number `() = runTest {
        // Given
        val tvId = 123L
        val seasonNumber = 2
        val nonExistentEpisode = 999

        coEvery {
            episodeRepository.getEpisodeCastMembers(tvId, seasonNumber, nonExistentEpisode)
        } returns emptyList()

        // When
        val result = getEpisodeCastMembersUseCase(tvId, seasonNumber, nonExistentEpisode)

        // Then
        assertThat(result).isEmpty()
        coVerify(exactly = 1) {
            episodeRepository.getEpisodeCastMembers(tvId, seasonNumber, nonExistentEpisode)
        }
    }


}

