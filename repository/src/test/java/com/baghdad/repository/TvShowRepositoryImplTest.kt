package com.baghdad.repository

import com.baghdad.domain.repository.AuthenticationRepository
import com.baghdad.entity.media.Episode
import com.baghdad.entity.media.Genre
import com.baghdad.entity.media.Review
import com.baghdad.entity.media.TvShow
import com.baghdad.entity.person.CastMember
import com.baghdad.repository.datasource.remote.RemoteGenreDataSource
import com.baghdad.repository.datasource.remote.RemoteTvShowDataSource
import com.baghdad.repository.dummyData.DummyDataFactory.DummyDataFactory.TV_SHOW_DTO
import com.baghdad.repository.dummyData.DummyDataFactory.DummyDataFactory.createMockCastMember
import com.baghdad.repository.dummyData.DummyDataFactory.DummyDataFactory.createMockCastMemberDto
import com.baghdad.repository.dummyData.DummyDataFactory.DummyDataFactory.createMockTvShowsDto
import com.baghdad.repository.mapper.toEntity
import com.baghdad.repository.model.EpisodeDto
import com.baghdad.repository.model.GenreDto
import com.baghdad.repository.model.PagedResultDto
import com.baghdad.repository.model.ReviewDto
import com.baghdad.repository.model.TvShowDto
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.LocalDate
import org.junit.jupiter.api.Test
import java.util.Locale


class TvShowRepositoryImplTest {

    private val remoteGenreDataSource: RemoteGenreDataSource = mockk()
    private val tvShowRemoteDataSource: RemoteTvShowDataSource = mockk()
    private val authenticationRepository: AuthenticationRepository = mockk()
    private val tvShowRepositoryImpl: TvShowRepositoryImpl = TvShowRepositoryImpl(
        remoteGenreDataSource = remoteGenreDataSource,
        tvShowRemoteDataSource = tvShowRemoteDataSource,
        authenticationRepository = authenticationRepository
    )

    @Test
    fun `getGenres should return mapped genres when remote data source succeeds`() = runTest {
        val mockGenreDtos = listOf(
            createMockGenreDto(1L, "Action", GenreDto.GenreType.TV_SHOW),
            createMockGenreDto(2L, "Comedy", GenreDto.GenreType.TV_SHOW),
            createMockGenreDto(3L, "Drama", GenreDto.GenreType.TV_SHOW)
        )
        val expectedGenres = listOf(
            createMockGenre(1L, "Action"),
            createMockGenre(2L, "Comedy"),
            createMockGenre(3L, "Drama")
        )
        val currentLanguage = Locale.getDefault().language

        coEvery { remoteGenreDataSource.getTvShowGenre(language = currentLanguage) } returns mockGenreDtos

        val result = tvShowRepositoryImpl.getGenres()

        assertThat(expectedGenres.size == result.size).isTrue()
        assertThat(result).isEqualTo(expectedGenres)
        coVerify { remoteGenreDataSource.getTvShowGenre(language = currentLanguage) }
    }

    @Test
    fun `getGenres should return empty list when remote data source returns empty list`() =
        runTest {
            val currentLanguage = Locale.getDefault().language
            coEvery { remoteGenreDataSource.getTvShowGenre(language = currentLanguage) } returns emptyList()

            val result = tvShowRepositoryImpl.getGenres()

            assertThat(emptyList<Genre>() == result).isTrue()
            coVerify { remoteGenreDataSource.getTvShowGenre(language = currentLanguage) }
        }

    @Test
    fun `getGenres should use default locale language when calling remote data source`() = runTest {
        val mockGenreDtos = listOf(
            createMockGenreDto(1L, "Action", GenreDto.GenreType.TV_SHOW)
        )
        val currentLanguage = Locale.getDefault().language

        coEvery { remoteGenreDataSource.getTvShowGenre(language = currentLanguage) } returns mockGenreDtos

        tvShowRepositoryImpl.getGenres()

        coVerify { remoteGenreDataSource.getTvShowGenre(language = currentLanguage) }
    }

    @Test
    fun `getGenres should handle single genre correctly`() = runTest {
        val mockGenreDto = createMockGenreDto(1L, "Thriller", GenreDto.GenreType.TV_SHOW)
        val expectedGenre = createMockGenre(1L, "Thriller")
        val currentLanguage = Locale.getDefault().language

        coEvery { remoteGenreDataSource.getTvShowGenre(language = currentLanguage) } returns listOf(
            mockGenreDto
        )

        val result = tvShowRepositoryImpl.getGenres()

        assertThat(1 == result.size).isTrue()
        assertThat(expectedGenre.id == result[0].id).isTrue()
        assertThat(expectedGenre.name == result[0].name).isTrue()
        coVerify { remoteGenreDataSource.getTvShowGenre(language = currentLanguage) }
    }

    @Test
    fun `getGenres should handle multiple genres with different types correctly`() = runTest {
        val mockGenreDtos = listOf(
            createMockGenreDto(10L, "Horror", GenreDto.GenreType.TV_SHOW),
            createMockGenreDto(20L, "Romance", GenreDto.GenreType.TV_SHOW),
            createMockGenreDto(30L, "Sci-Fi", GenreDto.GenreType.TV_SHOW)
        )
        val expectedGenres = listOf(
            createMockGenre(10L, "Horror"),
            createMockGenre(20L, "Romance"),
            createMockGenre(30L, "Sci-Fi")
        )
        val currentLanguage = Locale.getDefault().language

        coEvery { remoteGenreDataSource.getTvShowGenre(language = currentLanguage) } returns mockGenreDtos

        val result = tvShowRepositoryImpl.getGenres()

        assertThat(expectedGenres.size == result.size).isTrue()
        assertThat(result).isEqualTo(expectedGenres)
        coVerify { remoteGenreDataSource.getTvShowGenre(language = currentLanguage) }
    }

    @Test
    fun `getTvShowDetails should return tv show with images and trailer when remote call succeeds`() =
        runTest {
            val tvId = 123L
            val mockTvShowDto = TV_SHOW_DTO
            val mockImages =
                listOf("/header1.jpg", "/header2.jpg")
            val mockTrailerUrl = "https://youtube.com/watch?v=trailer123"
            val expectedTvShow = TV_SHOW_DTO.copy(
                trailerURL = mockTrailerUrl
            ).toEntity()

            coEvery { tvShowRemoteDataSource.getTvShowDetails(tvId) } returns mockTvShowDto
            coEvery { tvShowRemoteDataSource.getTvShowImages(tvId) } returns mockImages
            coEvery { tvShowRemoteDataSource.getTvShowTrailer(tvId) } returns mockTrailerUrl

            val result = tvShowRepositoryImpl.getTvShowDetails(tvId)

            assertThat(result).isEqualTo(expectedTvShow)
            coVerify { tvShowRemoteDataSource.getTvShowDetails(tvId) }
            coVerify { tvShowRemoteDataSource.getTvShowImages(tvId) }
            coVerify { tvShowRemoteDataSource.getTvShowTrailer(tvId) }
        }

    @Test
    fun `getTvShowCastMembers should return list of cast members when remote call succeeds`() =
        runTest {
            val tvId = 123L
            val mockCastMemberDtos = listOf(createMockCastMemberDto())
            val expectedCastMembers = listOf(createMockCastMember())

            coEvery { tvShowRemoteDataSource.getTvShowCastMembers(tvId) } returns mockCastMemberDtos

            val result = tvShowRepositoryImpl.getTvShowCastMembers(tvId)

            assertThat(expectedCastMembers.size == result.size).isTrue()
            assertThat(result).isEqualTo(expectedCastMembers)
            coVerify { tvShowRemoteDataSource.getTvShowCastMembers(tvId) }
        }

    @Test
    fun `getTvShowCastMembers should return empty list when no cast members found`() = runTest {
        val tvId = 123L
        coEvery { tvShowRemoteDataSource.getTvShowCastMembers(tvId) } returns emptyList()

        val result = tvShowRepositoryImpl.getTvShowCastMembers(tvId)

        assertThat(emptyList<CastMember>() == result).isTrue()
        coVerify { tvShowRemoteDataSource.getTvShowCastMembers(tvId) }
    }

    @Test
    fun `getTvShowImages should return list of image urls when remote call succeeds`() = runTest {
        val tvId = 123L
        val mockImages = listOf("/image1.jpg", "/image2.jpg", "/image3.jpg")

        coEvery { tvShowRemoteDataSource.getTvShowImages(tvId) } returns mockImages

        val result = tvShowRepositoryImpl.getTvShowImages(tvId)

        assertThat(mockImages == result).isTrue()
        coVerify { tvShowRemoteDataSource.getTvShowImages(tvId) }
    }

    @Test
    fun `getTvShowImages should return empty list when no images found`() = runTest {
        val tvId = 123L
        coEvery { tvShowRemoteDataSource.getTvShowImages(tvId) } returns emptyList()

        val result = tvShowRepositoryImpl.getTvShowImages(tvId)

        assertThat(emptyList<String>() == result).isTrue()
        coVerify { tvShowRemoteDataSource.getTvShowImages(tvId) }
    }

    @Test
    fun `getTvShowsByGenre should return list of tv shows when remote call succeeds`() = runTest {
        val genreId = 18L
        val page = 1
        val expectedTvShows = createMockPagedTvShowDto()

        coEvery { tvShowRemoteDataSource.getTvShowsByGenre(genreId, page) } returns expectedTvShows

        val result = tvShowRepositoryImpl.getTvShowsByGenre(genreId, page, 20)

        assertThat(expectedTvShows.data.size == result.data.size).isTrue()
        assertThat(expectedTvShows.data[0].id == result.data[0].id).isTrue()
        assertThat(expectedTvShows.data[0].title == result.data[0].title).isTrue()
        coVerify { tvShowRemoteDataSource.getTvShowsByGenre(genreId, page) }
    }

    @Test
    fun `getTvShowsByGenre should return empty list when no tv shows found`() = runTest {
        val genreId = 18L
        val page = 1
        coEvery { tvShowRemoteDataSource.getTvShowsByGenre(genreId, page) } returns PagedResultDto(
            emptyList<TvShowDto>(),
            nextKey = null,
            prevKey = 10
        )

        val result = tvShowRepositoryImpl.getTvShowsByGenre(genreId, page, pageSize = 20)

        assertThat(emptyList<TvShowDto>() == result.data).isTrue()
        coVerify { tvShowRemoteDataSource.getTvShowsByGenre(genreId, page) }
    }


    @Test
    fun `getTvShowSeasonEpisodes should return list of episodes when remote call succeeds`() =
        runTest {
            val tvId = 123L
            val seasonNumber = 1
            val mockEpisodeDtos = listOf(createMockEpisodeDto())
            val expectedEpisodes = listOf(createMockEpisode())

            coEvery {
                tvShowRemoteDataSource.getTvShowEpisodes(
                    tvId,
                    seasonNumber
                )
            } returns mockEpisodeDtos

            val result = tvShowRepositoryImpl.getTvShowSeasonEpisodes(tvId, seasonNumber)

            assertThat(expectedEpisodes.size == result.size).isTrue()
            assertThat(result).isEqualTo(expectedEpisodes)
            coVerify { tvShowRemoteDataSource.getTvShowEpisodes(tvId, seasonNumber) }
        }

    @Test
    fun `getTvShowSeasonEpisodes should return empty list when no episodes found`() = runTest {
        val tvId = 123L
        val seasonNumber = 1
        coEvery { tvShowRemoteDataSource.getTvShowEpisodes(tvId, seasonNumber) } returns emptyList()

        val result = tvShowRepositoryImpl.getTvShowSeasonEpisodes(tvId, seasonNumber)

        assertThat(emptyList<Episode>() == result).isTrue()
        coVerify { tvShowRemoteDataSource.getTvShowEpisodes(tvId, seasonNumber) }
    }

    @Test
    fun `getTvShowReviews should return list of reviews when remote call succeeds`() = runTest {
        val tvId = 123L
        val mockReviewDtos = listOf(createMockReviewDto())
        val expectedReviews = listOf(createMockReview())

        coEvery { tvShowRemoteDataSource.getTvShowReviews(tvId) } returns mockReviewDtos

        val result = tvShowRepositoryImpl.getTvShowReviews(tvId)

        assertThat(expectedReviews.size == result.size).isTrue()
        assertThat(result).isEqualTo(expectedReviews)
        coVerify { tvShowRemoteDataSource.getTvShowReviews(tvId) }
    }

    @Test
    fun `getTvShowReviews should return empty list when no reviews found`() = runTest {
        val tvId = 123L
        coEvery { tvShowRemoteDataSource.getTvShowReviews(tvId) } returns emptyList()

        val result = tvShowRepositoryImpl.getTvShowReviews(tvId)

        assertThat(emptyList<Review>() == result).isTrue()
        coVerify { tvShowRemoteDataSource.getTvShowReviews(tvId) }
    }


    @Test
    fun `getTopRatedTvShows should return paged result when remote call succeeds`() = runTest {
        val page = 1
        val mockTvShowDtos = listOf(createMockTvShowDto())
        val mockPagedResult = PagedResultDto(mockTvShowDtos, nextKey = 2, prevKey = null)

        coEvery { tvShowRemoteDataSource.getTopRatedTvShows(page) } returns mockPagedResult

        val result = tvShowRepositoryImpl.getTopRatedTvShows(page)

        assertThat(1 == result.data.size).isTrue()
        assertThat(789L == result.data[0].id).isTrue()
        assertThat("Test TV Show" == result.data[0].title).isTrue()
        assertThat(2 == result.nextPage).isTrue()
        assertThat(null == result.prevPage).isTrue()
        coVerify { tvShowRemoteDataSource.getTopRatedTvShows(page) }
    }


    @Test
    fun `getPopularTvShows should return list of tv shows when remote call succeeds`() = runTest {
        val mockTvShowDtos = listOf(createMockTvShowDto())
        val expectedTvShows = listOf(createMockTvShow())

        coEvery { tvShowRemoteDataSource.getPopularTvShows() } returns mockTvShowDtos

        val result = tvShowRepositoryImpl.getPopularTvShows()

        assertThat(expectedTvShows.size == result.size).isTrue()
        assertThat(result).isEqualTo(expectedTvShows)
        coVerify { tvShowRemoteDataSource.getPopularTvShows() }
    }

    @Test
    fun `getPopularTvShows should return empty list when no popular tv shows found`() = runTest {
        coEvery { tvShowRemoteDataSource.getPopularTvShows() } returns emptyList()

        val result = tvShowRepositoryImpl.getPopularTvShows()

        assertThat(emptyList<TvShow>() == result).isTrue()
        coVerify { tvShowRemoteDataSource.getPopularTvShows() }
    }

    @Test
    fun `getTrendingTvShows should return paged result when remote call succeeds`() = runTest {
        val page = 1
        val mockTvShowDtos = listOf(createMockTvShowDto())
        val mockPagedResult = PagedResultDto(mockTvShowDtos, nextKey = 2, prevKey = null)

        coEvery { tvShowRemoteDataSource.getTrendingTvShows(page) } returns mockPagedResult

        val result = tvShowRepositoryImpl.getTrendingTvShows(page)

        assertThat(1 == result.data.size).isTrue()
        assertThat(789L == result.data[0].id).isTrue()
        assertThat("Test TV Show" == result.data[0].title).isTrue()
        assertThat(2 == result.nextPage).isTrue()
        assertThat(null == result.prevPage).isTrue()
        coVerify { tvShowRemoteDataSource.getTrendingTvShows(page) }
    }

    companion object {

        private fun createMockGenreDto(
            id: Long,
            name: String,
            genreType: GenreDto.GenreType
        ) = GenreDto(
            id = id,
            name = name,
            type = genreType
        )

        private fun createMockGenre(
            id: Long,
            name: String
        ) = Genre(
            id = id,
            name = name
        )

        private fun createMockPagedTvShowDto() = PagedResultDto(
            createMockTvShowsDto(),
            nextKey = 2,
            prevKey = null
        )


        private fun createMockTvShowDto() = TvShowDto(
            id = 789L,
            title = "Test TV Show",
            genres = listOf(GenreDto(18, "Drama", type = GenreDto.GenreType.TV_SHOW)),
            imdbRating = 7.9,
            userRating = 8,
            releaseDate = "2023-01-01",
            overview = "Test overview for TV Show",
            posterPictureURL = "/tv_poster.jpg",
            numberOfSeasons = 3,
            trailerURL = " ",
            headerImagesURLs = listOf("/header1.jpg", "/header2.jpg", "/header3.jpg")
        )

        private fun createMockTvShow() = TvShow(
            id = 789L,
            title = "Test TV Show",
            genres = listOf(Genre(18L, "Drama")),
            averageRating = 7.9,
            userRating = 8,
            releaseDate = LocalDate.parse("2023-01-01"),
            overview = "Test overview for TV Show",
            posterImageURL = "/tv_poster.jpg",
            numberOfSeasons = 3,
            trailerURL = " ",
            headerImagesURLs = listOf("/header1.jpg", "/header2.jpg", "/header3.jpg")
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
            trailerUrl = " ",
            userRating = 8,
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
            headerPictures = listOf("/header1.jpg", "/header2.jpg"),
            genres = emptyList(),
            trailerUrl = " ",
            userRating = 8,
        )

        private fun createMockReviewDto() = ReviewDto(
            id = "review123",
            contentTitle = "Great movie! Highly recommended.",
            authorName = "John Reviewer",
            rating = 9.0,
            authorAvatarUrl = "https://example.com/avatar.jpg",
            reviewText = "This movie was fantastic! The plot was engaging and the acting was top-notch.",
            postedDate = LocalDate.parse("2023-01-01").toString()
        )

        private fun createMockReview() = Review(
            id = "review123",
            contentTitle = "Great movie! Highly recommended.",
            authorName = "John Reviewer",
            rating = 9.0,
            authorAvatarUrl = "https://example.com/avatar.jpg",
            reviewText = "This movie was fantastic! The plot was engaging and the acting was top-notch.",
            postedDate = LocalDate.parse("2023-01-01")
        )

    }
}