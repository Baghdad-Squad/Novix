package com.baghdad.repository

import com.baghdad.entity.person.CastMember
import com.baghdad.repository.datasource.remote.RemoteEpisodeDataSource
import com.baghdad.repository.datasource.remote.RemoteTvShowDataSource
import com.baghdad.repository.dummyData.DummyDataFactory.createMockCastMember
import com.baghdad.repository.dummyData.DummyDataFactory.createMockCastMemberDto
import com.baghdad.repository.dummyData.DummyDataFactory.createMockEpisode
import com.baghdad.repository.dummyData.DummyDataFactory.createMockEpisodeDto
import com.baghdad.repository.dummyData.DummyDataFactory.createMockGenre
import com.baghdad.repository.dummyData.DummyDataFactory.createMockGenreDto
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
    fun `getEpisodeCastMembers should return list of cast members when remote call succeeds`() =
        runTest {
            // Given
            val tvId = 123L
            val seasonNumber = 2
            val episodeNumber = 5
            val mockCastMemberDtos = listOf(createMockCastMemberDto())
            val expectedCastMembers = listOf(createMockCastMember())
            coEvery {
                remoteEpisodeDataSource.getEpisodeCastMembers(
                    tvId,
                    seasonNumber,
                    episodeNumber
                )
            } returns mockCastMemberDtos
            // When
            val result =
                episodeRepositoryImpl.getEpisodeCastMembers(tvId, seasonNumber, episodeNumber)
            // Then
            assertThat(expectedCastMembers == result).isTrue()
            coVerify {
                remoteEpisodeDataSource.getEpisodeCastMembers(
                    tvId,
                    seasonNumber,
                    episodeNumber
                )
            }
        }

    @Test
    fun `getEpisodeCastMembers should return empty list when no cast members found`() = runTest {
        // Given
        val tvId = 123L
        val seasonNumber = 2
        val episodeNumber = 5
        coEvery {
            remoteEpisodeDataSource.getEpisodeCastMembers(
                tvId,
                seasonNumber,
                episodeNumber
            )
        } returns emptyList()
        // When
        val result = episodeRepositoryImpl.getEpisodeCastMembers(tvId, seasonNumber, episodeNumber)
        // Then
        assertThat(emptyList<CastMember>() == result).isTrue()
        coVerify {
            remoteEpisodeDataSource.getEpisodeCastMembers(
                tvId,
                seasonNumber,
                episodeNumber
            )
        }
    }

    @Test
    fun `getEpisodeDetails should return episode with trailer and images when remote call succeeds`() =
        runTest {
            // Given
            val tvId = 123L
            val seasonNumber = 2
            val episodeNumber = 5
            val mockEpisodeDto = createMockEpisodeDto()
            val mockTrailerUrl = "https://youtube.com/watch?v=trailer123"
            val mockTvShowImages =
                listOf("/image1.jpg", "/image2.jpg", "/image3.jpg", "/image4.jpg", "/image5.jpg")
            val mockTvShowDetails = createMockTvShowDetailsDto()
            val expectedEpisode = createMockEpisode().copy(
                trailerUrl = mockTrailerUrl,
                headerPictures = mockTvShowImages.take(10),
                genres = mockTvShowDetails.genres.map { createMockGenre(it.id, it.name) }
            )
            coEvery {
                remoteEpisodeDataSource.getEpisodeDetails(
                    tvId,
                    seasonNumber,
                    episodeNumber
                )
            } returns mockEpisodeDto
            coEvery {
                remoteEpisodeDataSource.getEpisodeTrailer(
                    tvId,
                    seasonNumber,
                    episodeNumber
                )
            } returns mockTrailerUrl
            coEvery { remoteTvShowDataSource.getTvShowImages(tvId) } returns mockTvShowImages
            coEvery { remoteTvShowDataSource.getTvShowDetails(tvId) } returns mockTvShowDetails
            // When
            val result = episodeRepositoryImpl.getEpisodeDetails(tvId, seasonNumber, episodeNumber)
            // Then
            assertThat(expectedEpisode.id == result.id).isTrue()
            assertThat(expectedEpisode.title == result.title).isTrue()
            assertThat(expectedEpisode.trailerUrl == result.trailerUrl).isTrue()
            assertThat(mockTvShowImages.take(10) == result.headerPictures).isTrue()
            assertThat(expectedEpisode.genres.size == result.genres.size).isTrue()
            assertThat(expectedEpisode.genres[0].id == result.genres[0].id).isTrue()
            assertThat(expectedEpisode.genres[0].name == result.genres[0].name).isTrue()
            coVerify {
                remoteEpisodeDataSource.getEpisodeDetails(
                    tvId,
                    seasonNumber,
                    episodeNumber
                )
            }
            coVerify {
                remoteEpisodeDataSource.getEpisodeTrailer(
                    tvId,
                    seasonNumber,
                    episodeNumber
                )
            }
            coVerify { remoteTvShowDataSource.getTvShowImages(tvId) }
            coVerify { remoteTvShowDataSource.getTvShowDetails(tvId) }
        }

    @Test
    fun `getEpisodeDetails should limit tv show images to 10 when more than 10 images available`() =
        runTest {
            // Given
            val tvId = 123L
            val seasonNumber = 2
            val episodeNumber = 5
            val mockEpisodeDto = createMockEpisodeDto()
            val mockTrailerUrl = "https://youtube.com/watch?v=trailer123"
            val mockTvShowImages = (1..15).map { "/image$it.jpg" }
            val mockTvShowDetails = createMockTvShowDetailsDto()
            coEvery {
                remoteEpisodeDataSource.getEpisodeDetails(
                    tvId,
                    seasonNumber,
                    episodeNumber
                )
            } returns mockEpisodeDto
            coEvery {
                remoteEpisodeDataSource.getEpisodeTrailer(
                    tvId,
                    seasonNumber,
                    episodeNumber
                )
            } returns mockTrailerUrl
            coEvery { remoteTvShowDataSource.getTvShowImages(tvId) } returns mockTvShowImages
            coEvery { remoteTvShowDataSource.getTvShowDetails(tvId) } returns mockTvShowDetails
            // When
            val result = episodeRepositoryImpl.getEpisodeDetails(tvId, seasonNumber, episodeNumber)
            // Then
            assertThat(10 == result.headerPictures.size).isTrue()
            assertThat(mockTvShowImages.take(10) == result.headerPictures).isTrue()
            coVerify {
                remoteEpisodeDataSource.getEpisodeDetails(
                    tvId,
                    seasonNumber,
                    episodeNumber
                )
            }
            coVerify {
                remoteEpisodeDataSource.getEpisodeTrailer(
                    tvId,
                    seasonNumber,
                    episodeNumber
                )
            }
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