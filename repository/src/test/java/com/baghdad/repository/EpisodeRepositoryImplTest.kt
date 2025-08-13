package com.baghdad.repository

import com.baghdad.repository.datasource.remote.RemoteEpisodeDataSource
import com.baghdad.repository.datasource.remote.RemoteTvShowDataSource
import com.baghdad.repository.dummyData.DummyDataFactory.DummyDataFactory.createMockCastMember
import com.baghdad.repository.dummyData.DummyDataFactory.DummyDataFactory.createMockCastMemberDto
import com.baghdad.repository.dummyData.DummyDataFactory.DummyDataFactory.createMockEpisode
import com.baghdad.repository.dummyData.DummyDataFactory.DummyDataFactory.createMockEpisodeDto
import com.baghdad.repository.dummyData.DummyDataFactory.DummyDataFactory.createMockGenreDto
import com.baghdad.repository.model.CastMemberDto
import com.baghdad.repository.model.EpisodeDto
import com.baghdad.repository.model.GenreDto
import com.baghdad.repository.model.TvShowDto
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class EpisodeRepositoryImplTest {
    private lateinit var remoteEpisodeDataSource: RemoteEpisodeDataSource
    private lateinit var remoteTvShowDataSource: RemoteTvShowDataSource
    private lateinit var episodeRepositoryImpl: EpisodeRepositoryImpl

    private val tvId = 123L
    private val seasonNumber = 2
    private val episodeNumber = 5

    @BeforeEach
    fun setUp() {
        remoteTvShowDataSource = mockk()
        remoteEpisodeDataSource = mockk()
        episodeRepositoryImpl =
            EpisodeRepositoryImpl(remoteEpisodeDataSource, remoteTvShowDataSource)
    }

    @Test
    fun `getEpisodeCastMembers returns cast members when remote call succeeds`() = runTest {
        val castMemberDtos = listOf(createMockCastMemberDto())
        val expectedCastMembers = listOf(createMockCastMember())

        mockGetEpisodeCastMembers(castMemberDtos)

        val result = episodeRepositoryImpl.getEpisodeCastMembers(tvId, seasonNumber, episodeNumber)

        assertThat(result).isEqualTo(expectedCastMembers)
        verifyGetEpisodeCastMembersCalled()
    }

    @Test
    fun `getEpisodeCastMembers returns empty list when no cast members found`() = runTest {
        mockGetEpisodeCastMembers(emptyList())

        val result = episodeRepositoryImpl.getEpisodeCastMembers(tvId, seasonNumber, episodeNumber)

        assertThat(result).isEmpty()
        verifyGetEpisodeCastMembersCalled()
    }

    @Test
    fun `getEpisodeDetails returns episode with additional data when remote calls succeed`() =
        runTest {
            val episodeDto = createMockEpisodeDto()
            val trailerUrl = "https://youtube.com/watch?v=episode_trailer"
            val tvShowImages = listOf("/image1.jpg", "/image2.jpg", "/image3.jpg")
            val mockGenres = listOf(
                createMockGenreDto(18, "Drama"),
                createMockGenreDto(35, "Comedy")
            )

            mockGetEpisodeDetails(episodeDto)
            mockGetEpisodeTrailer(trailerUrl)
            mockGetTvShowImages(tvShowImages)
            mockGetTvShowDetails(mockGenres)

            val result = episodeRepositoryImpl.getEpisodeDetails(tvId, seasonNumber, episodeNumber)

            val expectedEpisode = createMockEpisode().copy(
                trailerUrl = trailerUrl,
                headerPictures = tvShowImages
            )

            assertThat(result.id).isEqualTo(expectedEpisode.id)
            assertThat(result.title).isEqualTo(expectedEpisode.title)
            assertThat(result.trailerUrl).isEqualTo(trailerUrl)
            assertThat(result.headerPictures).isEqualTo(tvShowImages)

            verifyGetEpisodeDetailsCalled()
            verifyGetEpisodeTrailerCalled()
            verifyGetTvShowImagesCalled()
            verifyGetTvShowDetailsCalled()
        }

    @Test
    fun `addTvEpisodeRate calls remote data source with correct parameters`() = runTest {
        val rating = 8

        coEvery {
            remoteEpisodeDataSource.addEpisodeRate(tvId, seasonNumber, episodeNumber, rating)
        } returns Unit

        episodeRepositoryImpl.addTvEpisodeRate(tvId, seasonNumber, episodeNumber, rating)

        coVerify {
            remoteEpisodeDataSource.addEpisodeRate(tvId, seasonNumber, episodeNumber, rating)
        }
    }

    private fun mockGetEpisodeCastMembers(returnValue: List<CastMemberDto>) {
        coEvery {
            remoteEpisodeDataSource.getEpisodeCastMembers(tvId, seasonNumber, episodeNumber)
        } returns returnValue
    }

    private fun mockGetEpisodeDetails(episodeDto: EpisodeDto) {
        coEvery {
            remoteEpisodeDataSource.getEpisodeDetails(tvId, seasonNumber, episodeNumber)
        } returns episodeDto
    }

    private fun mockGetEpisodeTrailer(trailerUrl: String) {
        coEvery {
            remoteEpisodeDataSource.getEpisodeTrailer(tvId, seasonNumber, episodeNumber)
        } returns trailerUrl
    }

    private fun mockGetTvShowImages(images: List<String>) {
        coEvery {
            remoteTvShowDataSource.getTvShowImages(tvId)
        } returns images
    }

    private fun mockGetTvShowDetails(genres: List<GenreDto>) {
        val mockTvShowDetails = mockk<TvShowDto> {
            coEvery { this@mockk.genres } returns genres
        }

        coEvery {
            remoteTvShowDataSource.getTvShowDetails(tvId)
        } returns mockTvShowDetails
    }


    private fun verifyGetEpisodeCastMembersCalled() {
        coVerify {
            remoteEpisodeDataSource.getEpisodeCastMembers(tvId, seasonNumber, episodeNumber)
        }
    }

    private fun verifyGetEpisodeDetailsCalled() {
        coVerify {
            remoteEpisodeDataSource.getEpisodeDetails(tvId, seasonNumber, episodeNumber)
        }
    }

    private fun verifyGetEpisodeTrailerCalled() {
        coVerify {
            remoteEpisodeDataSource.getEpisodeTrailer(tvId, seasonNumber, episodeNumber)
        }
    }

    private fun verifyGetTvShowImagesCalled() {
        coVerify { remoteTvShowDataSource.getTvShowImages(tvId) }
    }

    private fun verifyGetTvShowDetailsCalled() {
        coVerify { remoteTvShowDataSource.getTvShowDetails(tvId) }
    }
}