package com.baghdad.domain.usecase.episode

import com.baghdad.domain.repository.EpisodeRepository
import com.baghdad.domain.testHelper.getSampleActor
import com.baghdad.entity.person.CastMember
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.LocalDate
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class GetEpisodeCastMembersUseCaseTest {

    private lateinit var episodeRepository: EpisodeRepository
    private lateinit var getEpisodeCastMembersUseCase: GetEpisodeCastMembersUseCase


    @BeforeEach
    fun setUp() {
        episodeRepository = mockk(relaxed = true)
        getEpisodeCastMembersUseCase = GetEpisodeCastMembersUseCase(episodeRepository)
    }

    @Test
    fun `should return cast members when executing with valid tvShowId, seasonNumber and episodeNumber`() =
        runTest {
            coEvery {
                episodeRepository.getEpisodeCastMembers(TV_SHOW_ID, SEASON_NUMBER, EPISODE_NUMBER)
            } returns expectedCastMembers

            val result = getEpisodeCastMembersUseCase(TV_SHOW_ID, SEASON_NUMBER, EPISODE_NUMBER)

            assertThat(result).isEqualTo(expectedCastMembers)
        }

    @Test
    fun `should return empty list when not exist tv show`() = runTest {
        coEvery {
            episodeRepository.getEpisodeCastMembers(
                NOT_EXISTENT_TV_SHOW,
                SEASON_NUMBER,
                EPISODE_NUMBER
            )
        } returns emptyList()


        val result =
            getEpisodeCastMembersUseCase(NOT_EXISTENT_TV_SHOW, SEASON_NUMBER, EPISODE_NUMBER)

        assertThat(result).isEmpty()
    }

    @Test
    fun `should returns empty list when non-existent season number `() = runTest {
        coEvery {
            episodeRepository.getEpisodeCastMembers(TV_SHOW_ID, NON_EXISTENT_SEASON, EPISODE_NUMBER)
        } returns emptyList()

        val result = getEpisodeCastMembersUseCase(TV_SHOW_ID, NON_EXISTENT_SEASON, EPISODE_NUMBER)

        assertThat(result).isEmpty()
    }

    @Test
    fun `should return empty list when non-existent episode number `() = runTest {
        coEvery {
            episodeRepository.getEpisodeCastMembers(TV_SHOW_ID, SEASON_NUMBER, NON_EXISTENT_EPISODE)
        } returns emptyList()

        val result = getEpisodeCastMembersUseCase(TV_SHOW_ID, SEASON_NUMBER, NON_EXISTENT_EPISODE)

        assertThat(result).isEmpty()
    }

    companion object {
        private val expectedCastMembers = listOf(
            CastMember(
                actor = getSampleActor(),
                characterName = "Main Character",
            ),
            CastMember(
                actor = getSampleActor(
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
        private const val TV_SHOW_ID = 123L
        private const val NOT_EXISTENT_TV_SHOW = 423442L

        private const val SEASON_NUMBER = 2
        private const val NON_EXISTENT_SEASON = 45

        private const val EPISODE_NUMBER = 1
        private const val NON_EXISTENT_EPISODE = 47
    }
}

