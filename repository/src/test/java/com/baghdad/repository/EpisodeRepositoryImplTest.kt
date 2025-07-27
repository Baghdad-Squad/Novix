package com.baghdad.repository

import com.baghdad.entity.media.Episode
import com.baghdad.entity.person.CastMember
import com.baghdad.repository.datasource.remote.RemoteEpisodeDataSource
import com.baghdad.repository.datasource.remote.RemoteTvShowDataSource
import com.baghdad.repository.model.ActorDto
import com.baghdad.repository.model.CastMemberDto
import com.baghdad.repository.model.EpisodeDto
import com.baghdad.repository.model.GenreDto
import com.baghdad.repository.model.TvShowDto
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.LocalDate
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class EpisodeRepositoryImplTest {

    private lateinit var remoteEpisodeDataSource: RemoteEpisodeDataSource
    private lateinit var remoteTvShowDataSource: RemoteTvShowDataSource
    private lateinit var episodeRepositoryImpl: EpisodeRepositoryImpl

    @BeforeEach
    fun setUp() {
        remoteTvShowDataSource = mockk()
        remoteEpisodeDataSource = mockk()
        episodeRepositoryImpl = EpisodeRepositoryImpl(
            remoteEpisodeDataSource = remoteEpisodeDataSource,
            remoteTvShowDataSource = remoteTvShowDataSource
        )
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
    fun `getEpisodeDetails should return episode with trailer and images when remote call succeeds`() = runTest {
        // Given
        val tvId = 123L
        val seasonNumber = 2
        val episodeNumber = 5
        val mockEpisodeDto = createMockEpisodeDto()
        val mockTrailerUrl = "https://youtube.com/watch?v=trailer123"
        val mockTvShowImages = listOf("/image1.jpg", "/image2.jpg", "/image3.jpg", "/image4.jpg", "/image5.jpg")
        val mockTvShowDetails = createMockTvShowDetailsDto()
        val expectedEpisode = createMockEpisode().copy(
            trailerUrl = mockTrailerUrl,
            headerPictures = mockTvShowImages.take(10),
            genres = mockTvShowDetails.genres.map { createMockGenre(it.id, it.name) }
        )

        coEvery { remoteEpisodeDataSource.getEpisodeDetails(tvId, seasonNumber, episodeNumber) } returns mockEpisodeDto
        coEvery { remoteEpisodeDataSource.getEpisodeTrailer(tvId, seasonNumber, episodeNumber) } returns mockTrailerUrl
        coEvery { remoteTvShowDataSource.getTvShowImages(tvId) } returns mockTvShowImages
        coEvery { remoteTvShowDataSource.getTvShowDetails(tvId) } returns mockTvShowDetails

        val result = episodeRepositoryImpl.getEpisodeDetails(tvId, seasonNumber, episodeNumber)

        assertEquals(expectedEpisode.id, result.id)
        assertEquals(expectedEpisode.title, result.title)
        assertEquals(expectedEpisode.trailerUrl, result.trailerUrl)
        assertEquals(mockTvShowImages.take(10), result.headerPictures)
        assertEquals(expectedEpisode.genres.size, result.genres.size)
        assertEquals(expectedEpisode.genres[0].id, result.genres[0].id)
        assertEquals(expectedEpisode.genres[0].name, result.genres[0].name)
        coVerify { remoteEpisodeDataSource.getEpisodeDetails(tvId, seasonNumber, episodeNumber) }
        coVerify { remoteEpisodeDataSource.getEpisodeTrailer(tvId, seasonNumber, episodeNumber) }
        coVerify { remoteTvShowDataSource.getTvShowImages(tvId) }
        coVerify { remoteTvShowDataSource.getTvShowDetails(tvId) }
    }

    @Test
    fun `getEpisodeDetails should limit tv show images to 10 when more than 10 images available`() = runTest {
        // Given
        val tvId = 123L
        val seasonNumber = 2
        val episodeNumber = 5
        val mockEpisodeDto = createMockEpisodeDto()
        val mockTrailerUrl = "https://youtube.com/watch?v=trailer123"
        val mockTvShowImages = (1..15).map { "/image$it.jpg" }
        val mockTvShowDetails = createMockTvShowDetailsDto()

        coEvery { remoteEpisodeDataSource.getEpisodeDetails(tvId, seasonNumber, episodeNumber) } returns mockEpisodeDto
        coEvery { remoteEpisodeDataSource.getEpisodeTrailer(tvId, seasonNumber, episodeNumber) } returns mockTrailerUrl
        coEvery { remoteTvShowDataSource.getTvShowImages(tvId) } returns mockTvShowImages
        coEvery { remoteTvShowDataSource.getTvShowDetails(tvId) } returns mockTvShowDetails

        val result = episodeRepositoryImpl.getEpisodeDetails(tvId, seasonNumber, episodeNumber)

        assertEquals(10, result.headerPictures.size)
        assertEquals(mockTvShowImages.take(10), result.headerPictures)
        coVerify { remoteEpisodeDataSource.getEpisodeDetails(tvId, seasonNumber, episodeNumber) }
        coVerify { remoteEpisodeDataSource.getEpisodeTrailer(tvId, seasonNumber, episodeNumber) }
        coVerify { remoteTvShowDataSource.getTvShowImages(tvId) }
        coVerify { remoteTvShowDataSource.getTvShowDetails(tvId) }
    }

    @Test
    fun `getEpisodeDetails should handle empty tv show images list`() = runTest {
        // Given
        val tvId = 123L
        val seasonNumber = 2
        val episodeNumber = 5
        val mockEpisodeDto = createMockEpisodeDto()
        val mockTrailerUrl = "https://youtube.com/watch?v=trailer123"
        val emptyImagesList = emptyList<String>()
        val mockTvShowDetails = createMockTvShowDetailsDto()

        coEvery { remoteEpisodeDataSource.getEpisodeDetails(tvId, seasonNumber, episodeNumber) } returns mockEpisodeDto
        coEvery { remoteEpisodeDataSource.getEpisodeTrailer(tvId, seasonNumber, episodeNumber) } returns mockTrailerUrl
        coEvery { remoteTvShowDataSource.getTvShowImages(tvId) } returns emptyImagesList
        coEvery { remoteTvShowDataSource.getTvShowDetails(tvId) } returns mockTvShowDetails

        val result = episodeRepositoryImpl.getEpisodeDetails(tvId, seasonNumber, episodeNumber)

        assertEquals(emptyList<String>(), result.headerPictures)
        assertEquals(mockTrailerUrl, result.trailerUrl)
        coVerify { remoteEpisodeDataSource.getEpisodeDetails(tvId, seasonNumber, episodeNumber) }
        coVerify { remoteEpisodeDataSource.getEpisodeTrailer(tvId, seasonNumber, episodeNumber) }
        coVerify { remoteTvShowDataSource.getTvShowImages(tvId) }
        coVerify { remoteTvShowDataSource.getTvShowDetails(tvId) }
    }

    @Test
    fun `getEpisodeDetails should handle empty genres list from tv show`() = runTest {
        // Given
        val tvId = 123L
        val seasonNumber = 2
        val episodeNumber = 5
        val mockEpisodeDto = createMockEpisodeDto()
        val mockTrailerUrl = "https://youtube.com/watch?v=trailer123"
        val mockTvShowImages = listOf("/image1.jpg", "/image2.jpg")
        val mockTvShowDetails = createMockTvShowDetailsDto().copy(genres = emptyList())

        coEvery { remoteEpisodeDataSource.getEpisodeDetails(tvId, seasonNumber, episodeNumber) } returns mockEpisodeDto
        coEvery { remoteEpisodeDataSource.getEpisodeTrailer(tvId, seasonNumber, episodeNumber) } returns mockTrailerUrl
        coEvery { remoteTvShowDataSource.getTvShowImages(tvId) } returns mockTvShowImages
        coEvery { remoteTvShowDataSource.getTvShowDetails(tvId) } returns mockTvShowDetails

        val result = episodeRepositoryImpl.getEpisodeDetails(tvId, seasonNumber, episodeNumber)

        assertEquals(emptyList<com.baghdad.entity.media.Genre>(), result.genres)
        assertEquals(mockTrailerUrl, result.trailerUrl)
        assertEquals(mockTvShowImages, result.headerPictures)
        coVerify { remoteEpisodeDataSource.getEpisodeDetails(tvId, seasonNumber, episodeNumber) }
        coVerify { remoteEpisodeDataSource.getEpisodeTrailer(tvId, seasonNumber, episodeNumber) }
        coVerify { remoteTvShowDataSource.getTvShowImages(tvId) }
        coVerify { remoteTvShowDataSource.getTvShowDetails(tvId) }
    }

    companion object {

        private fun createMockTvShowDetailsDto() = TvShowDto(
            id = 123L,
            title = "Test TV Show",
            genres = listOf(
                createMockGenreDto(18, "Drama"),
                createMockGenreDto(35, "Comedy")
            ),
            imdbRating = 7.9,
            userRating = 8.1,
            releaseDate = "2023-01-01",
            overview = "Test overview for TV Show",
            posterPictureURL = "/tv_poster.jpg",
            numberOfSeasons = 3,
            trailerURL = " ",
            headerImagesURLs = listOf("/header1.jpg", "/header2.jpg", "/header3.jpg")
        )

        private fun createMockGenreDto(id: Long, name: String) = GenreDto(
            id = id,
            name = name,
            type = GenreDto.GenreType.TV_SHOW
        )

        private fun createMockGenre(id: Long, name: String) = com.baghdad.entity.media.Genre(
            id = id,
            name = name
        )

        private fun createMockEpisodeDto() = EpisodeDto(
            id = 456L,
            title = "Test Episode",
            episodeNumber = 5,
            rating = 8.5,
            duration = "45 min",
            releasedDate = "2023-05-15",
            currentSeason = 2,
            overview = "Test episode overview",
            headerPictures = listOf("/header1.jpg", "/header2.jpg"),
            genres = emptyList(),
            trailerUrl = " "
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
            headerPictures = listOf("/image1.jpg", "/image2.jpg", "/image3.jpg"),
            genres = emptyList(),
            trailerUrl = " "
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