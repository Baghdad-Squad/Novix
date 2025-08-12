package com.baghdad.repository

import com.baghdad.repository.datasource.remote.RemoteEpisodeDataSource
import com.baghdad.repository.datasource.remote.RemoteTvShowDataSource
import com.baghdad.repository.dummyData.DummyDataFactory.createMockCastMember
import com.baghdad.repository.dummyData.DummyDataFactory.createMockCastMemberDto
import com.baghdad.repository.dummyData.DummyDataFactory.createMockEpisodeDto
import com.baghdad.repository.model.CastMemberDto
import com.baghdad.repository.model.EpisodeDto
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

    @BeforeEach
    fun setUp() {
        remoteTvShowDataSource = mockk()
        remoteEpisodeDataSource = mockk()
        episodeRepositoryImpl =
            EpisodeRepositoryImpl(remoteEpisodeDataSource, remoteTvShowDataSource)
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

    // --- الاختبارات ---

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
    fun `getEpisodeDetails limits tv show images to 10 when more than 10 images available`() =
        runTest {
            val mockEpisodeDto = createMockEpisodeDto()
            val mockTrailerUrl = "https://youtube.com/watch?v=trailer123"
            val mockTvShowImages = (1..15).map { "/image$it.jpg" }

            mockGetEpisodeDetails(mockEpisodeDto)
            mockGetEpisodeTrailer(mockTrailerUrl)
            mockGetTvShowImages(mockTvShowImages)
            val result = episodeRepositoryImpl.getEpisodeDetails(tvId, seasonNumber, episodeNumber)

            assertThat(result.headerPictures).hasSize(10)
            assertThat(result.headerPictures).isEqualTo(mockTvShowImages.take(10))

            verifyGetEpisodeDetailsCalled()
            verifyGetEpisodeTrailerCalled()
            verifyGetTvShowImagesCalled()
            verifyGetTvShowDetailsCalled()
        }
    private val tvId = 123L
    private val seasonNumber = 2
    private val episodeNumber = 5

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
}