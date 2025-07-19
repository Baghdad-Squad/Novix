package com.baghdad.repository

import com.baghdad.entity.media.Episode
import com.baghdad.entity.person.CastMember
import com.baghdad.repository.datasource.remote.RemoteEpisodeDataSource
import com.baghdad.repository.model.ActorDto
import com.baghdad.repository.model.CastMemberDto
import com.baghdad.repository.model.EpisodeDto
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.LocalDate
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class EpisodeRepositoryImplTest {

    private lateinit var remoteEpisodeDataSource: RemoteEpisodeDataSource
    private lateinit var episodeRepositoryImpl: EpisodeRepositoryImpl

    @BeforeEach
    fun setUp() {
        remoteEpisodeDataSource = mockk()
        episodeRepositoryImpl = EpisodeRepositoryImpl(
            remoteEpisodeDataSource = remoteEpisodeDataSource
        )
    }

    @Test
    fun `getEpisodeDetails should return episode when remote call succeeds`() = runTest {
        // Given
        val tvId = 123L
        val seasonNumber = 2
        val episodeNumber = 5
        val expectedEpisodeDto = createMockEpisodeDto()
        val mockImages = listOf("/image1.jpg", "/image2.jpg", "/image3.jpg", "/image4.jpg")
        val expectedEpisode = createMockEpisode()

        coEvery { remoteEpisodeDataSource.getEpisodeDetails(tvId, seasonNumber, episodeNumber) } returns expectedEpisodeDto
        coEvery { remoteEpisodeDataSource.getEpisodeImages(tvId, seasonNumber, episodeNumber) } returns mockImages

        val result = episodeRepositoryImpl.getEpisodeDetails(tvId, seasonNumber, episodeNumber)

        assertEquals(expectedEpisode, result)
        coVerify { remoteEpisodeDataSource.getEpisodeDetails(tvId, seasonNumber, episodeNumber) }
        coVerify { remoteEpisodeDataSource.getEpisodeImages(tvId, seasonNumber, episodeNumber) }
    }

    @Test
    fun `getEpisodeDetails should limit header pictures to 3 images`() = runTest {
        // Given
        val tvId = 123L
        val seasonNumber = 2
        val episodeNumber = 5
        val expectedEpisodeDto = createMockEpisodeDto()
        val mockImages = listOf("/image1.jpg", "/image2.jpg", "/image3.jpg", "/image4.jpg", "/image5.jpg")

        coEvery { remoteEpisodeDataSource.getEpisodeDetails(tvId, seasonNumber, episodeNumber) } returns expectedEpisodeDto
        coEvery { remoteEpisodeDataSource.getEpisodeImages(tvId, seasonNumber, episodeNumber) } returns mockImages

        val result = episodeRepositoryImpl.getEpisodeDetails(tvId, seasonNumber, episodeNumber)

        assertEquals(3, result.headerPictures.size)
        assertEquals(listOf("/image1.jpg", "/image2.jpg", "/image3.jpg"), result.headerPictures)
        coVerify { remoteEpisodeDataSource.getEpisodeDetails(tvId, seasonNumber, episodeNumber) }
        coVerify { remoteEpisodeDataSource.getEpisodeImages(tvId, seasonNumber, episodeNumber) }
    }

    @Test
    fun `getEpisodeDetails should throw exception when remote call fails`() = runTest {

        val tvId = 123L
        val seasonNumber = 2
        val episodeNumber = 5
        val exception = RuntimeException("Network error")
        coEvery { remoteEpisodeDataSource.getEpisodeDetails(tvId, seasonNumber, episodeNumber) } throws exception

        assertThrows<Exception> {
            episodeRepositoryImpl.getEpisodeDetails(tvId, seasonNumber, episodeNumber)
        }
    }

    @Test
    fun `getEpisodeCastMembers should return list of cast members when remote call succeeds`() = runTest {

        val tvId = 123L
        val seasonNumber = 2
        val episodeNumber = 5
        val mockCastMemberDtos = listOf(createMockCastMemberDto())
        val expectedCastMembers = listOf(createMockCastMember())

        coEvery { remoteEpisodeDataSource.getEpisodeCastMembers(tvId, seasonNumber, episodeNumber) } returns mockCastMemberDtos

        val result = episodeRepositoryImpl.getEpisodeCastMembers(tvId, seasonNumber, episodeNumber)

        assertEquals(expectedCastMembers, result)
        coVerify { remoteEpisodeDataSource.getEpisodeCastMembers(tvId, seasonNumber, episodeNumber) }
    }

    @Test
    fun `getEpisodeCastMembers should return empty list when no cast members found`() = runTest {
        // Given
        val tvId = 123L
        val seasonNumber = 2
        val episodeNumber = 5
        coEvery { remoteEpisodeDataSource.getEpisodeCastMembers(tvId, seasonNumber, episodeNumber) } returns emptyList()

        val result = episodeRepositoryImpl.getEpisodeCastMembers(tvId, seasonNumber, episodeNumber)

        assertEquals(emptyList<CastMember>(), result)
        coVerify { remoteEpisodeDataSource.getEpisodeCastMembers(tvId, seasonNumber, episodeNumber) }
    }

    @Test
    fun `getEpisodeCastMembers should throw exception when remote call fails`() = runTest {

        val tvId = 123L
        val seasonNumber = 2
        val episodeNumber = 5
        val exception = RuntimeException("Network error")
        coEvery { remoteEpisodeDataSource.getEpisodeCastMembers(tvId, seasonNumber, episodeNumber) } throws exception
        
        assertThrows<Exception> {
            episodeRepositoryImpl.getEpisodeCastMembers(tvId, seasonNumber, episodeNumber)
        }
    }

    companion object {
        private fun createMockEpisodeDto() = EpisodeDto(
            id = 456L,
            title = "Test Episode",
            episodeNumber = 5,
            rating = 8.5,
            duration = "45 min",
            releasedDate = "2023-05-15",
            currentSeason = 2,
            overview = "Test episode overview",
            headerPictures = listOf("/header1.jpg", "/header2.jpg")
        )

        private fun createMockEpisode() = Episode(
            id = 456L,
            title = "Test Episode",
            episodeNumber = 5,
            rating = 8.5,
            duration = "45 min",
            releasedDate = LocalDate.parse("2023-05-15"),
            currentSeason = 2,
            overview = "Test episode overview",
            headerPictures = listOf("/image1.jpg", "/image2.jpg", "/image3.jpg") // First 3 images
        )

        private fun createMockCastMemberDto() = CastMemberDto(
            actor = createMockActorDto(),
            characterName = "Test Character"
        )

        private fun createMockCastMember() = CastMember(
            actor = createMockActor(),
            characterName = "Test Character"
        )

        private fun createMockActorDto() = ActorDto(
            id = 789L,
            name = "Test Actor",
            imageUrl = "/actor_profile.jpg",
            biography = "Test actor biography",
            birthdayDate = "1985-03-10",
            deathDate = null,
            placeOfBirth = "Los Angeles, USA",
            headerPictures = listOf("/actor_header1.jpg", "/actor_header2.jpg"),
            department = "Acting"
        )

        private fun createMockActor() = com.baghdad.entity.person.Actor(
            id = 789L,
            name = "Test Actor",
            profilePictureURL = "/actor_profile.jpg",
            birthDate = LocalDate.parse("1985-03-10"),
            placeOfBirth = "Los Angeles, USA",
            deathDate = null,
            biography = "Test actor biography",
            headerPictures = listOf("/actor_header1.jpg", "/actor_header2.jpg"),
            department = "Acting"
        )
    }
}