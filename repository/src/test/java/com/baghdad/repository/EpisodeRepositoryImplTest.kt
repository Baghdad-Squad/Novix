package com.baghdad.repository

import com.baghdad.entity.person.CastMember
import com.baghdad.repository.DummyDataFactory.createMockCastMember
import com.baghdad.repository.DummyDataFactory.createMockCastMemberDto
import com.baghdad.repository.DummyDataFactory.createMockEpisode
import com.baghdad.repository.DummyDataFactory.createMockEpisodeDto
import com.baghdad.repository.DummyDataFactory.createMockGenre
import com.baghdad.repository.DummyDataFactory.createMockGenreDto
import com.baghdad.repository.datasource.remote.RemoteEpisodeDataSource
import com.baghdad.repository.datasource.remote.RemoteTvShowDataSource
import com.baghdad.repository.model.TvShowDto
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
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

    }
}