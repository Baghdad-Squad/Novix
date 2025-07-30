package com.baghdad.repository

import com.baghdad.entity.media.Episode
import com.baghdad.entity.media.Genre
import com.baghdad.entity.media.Review
import com.baghdad.entity.media.TvShow
import com.baghdad.entity.person.Actor
import com.baghdad.entity.person.CastMember
import com.baghdad.repository.datasource.remote.RemoteGenreDataSource
import com.baghdad.repository.datasource.remote.RemoteTvShowDataSource
import com.baghdad.repository.model.ActorDto
import com.baghdad.repository.model.CastMemberDto
import com.baghdad.repository.model.EpisodeDto
import com.baghdad.repository.model.GenreDto
import com.baghdad.repository.model.PagedResultDto
import com.baghdad.repository.model.ReviewDto
import com.baghdad.repository.model.TvShowDto
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.LocalDate
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.Locale


class TvShowRepositoryImplTest {

    private lateinit var remoteGenreDataSource: RemoteGenreDataSource
    private lateinit var tvShowRemoteDataSource: RemoteTvShowDataSource
    private lateinit var tvShowRepositoryImpl: TvShowRepositoryImpl


    @BeforeEach
    fun setUp() {
        remoteGenreDataSource = mockk()
        tvShowRemoteDataSource = mockk() 
        tvShowRepositoryImpl = TvShowRepositoryImpl(
            remoteGenreDataSource = remoteGenreDataSource,
            tvShowRemoteDataSource = tvShowRemoteDataSource,
        )
    }

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

        assertEquals(expectedGenres.size, result.size)
        assertEquals(expectedGenres[0].id, result[0].id)
        assertEquals(expectedGenres[0].name, result[0].name)
        assertEquals(expectedGenres[1].id, result[1].id)
        assertEquals(expectedGenres[1].name, result[1].name)
        assertEquals(expectedGenres[2].id, result[2].id)
        assertEquals(expectedGenres[2].name, result[2].name)
        coVerify { remoteGenreDataSource.getTvShowGenre(language = currentLanguage) }
    }

    @Test
    fun `getGenres should return empty list when remote data source returns empty list`() = runTest {

        val currentLanguage = Locale.getDefault().language
        coEvery { remoteGenreDataSource.getTvShowGenre(language = currentLanguage) } returns emptyList()

        val result = tvShowRepositoryImpl.getGenres()

        assertEquals(emptyList<Genre>(), result)
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

        coEvery { remoteGenreDataSource.getTvShowGenre(language = currentLanguage) } returns listOf(mockGenreDto)

        val result = tvShowRepositoryImpl.getGenres()

        assertEquals(1, result.size)
        assertEquals(expectedGenre.id, result[0].id)
        assertEquals(expectedGenre.name, result[0].name)
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

        assertEquals(expectedGenres.size, result.size)
        for (i in expectedGenres.indices) {
            assertEquals(expectedGenres[i].id, result[i].id)
            assertEquals(expectedGenres[i].name, result[i].name)
        }
        coVerify { remoteGenreDataSource.getTvShowGenre(language = currentLanguage) }
    }

    @Test
    fun `getTvShowDetails should return tv show with images and trailer when remote call succeeds`() = runTest {
        // Given
        val tvId = 123L
        val mockTvShowDto = createMockTvShowDto()
        val mockImages = listOf("/image1.jpg", "/image2.jpg", "/image3.jpg", "/image4.jpg", "/image5.jpg")
        val mockTrailerUrl = "https://youtube.com/watch?v=trailer123"
        val expectedTvShow = createMockTvShow().copy(
            headerImagesURLs = mockImages.take(10),
            trailerURL = mockTrailerUrl
        )

        coEvery { tvShowRemoteDataSource.getTvShowDetails(tvId) } returns mockTvShowDto
        coEvery { tvShowRemoteDataSource.getTvShowImages(tvId) } returns mockImages
        coEvery { tvShowRemoteDataSource.getTvShowTrailer(tvId) } returns mockTrailerUrl

        val result = tvShowRepositoryImpl.getTvShowDetails(tvId)

        assertEquals(expectedTvShow.id, result.id)
        assertEquals(expectedTvShow.title, result.title)
        assertEquals(expectedTvShow.trailerURL, result.trailerURL)
        assertEquals(mockImages.take(10), result.headerImagesURLs)
        coVerify { tvShowRemoteDataSource.getTvShowDetails(tvId) }
        coVerify { tvShowRemoteDataSource.getTvShowImages(tvId) }
        coVerify { tvShowRemoteDataSource.getTvShowTrailer(tvId) }
    }

    @Test
    fun `getTvShowDetails should limit header images to 10 when more than 10 images available`() = runTest {
        // Given
        val tvId = 123L
        val mockTvShowDto = createMockTvShowDto()
        val mockImages = (1..15).map { "/image$it.jpg" }
        val mockTrailerUrl = "https://youtube.com/watch?v=trailer123"

        coEvery { tvShowRemoteDataSource.getTvShowDetails(tvId) } returns mockTvShowDto
        coEvery { tvShowRemoteDataSource.getTvShowImages(tvId) } returns mockImages
        coEvery { tvShowRemoteDataSource.getTvShowTrailer(tvId) } returns mockTrailerUrl

        val result = tvShowRepositoryImpl.getTvShowDetails(tvId)

        assertEquals(10, result.headerImagesURLs.size)
        assertEquals(mockImages.take(10), result.headerImagesURLs)
        coVerify { tvShowRemoteDataSource.getTvShowDetails(tvId) }
        coVerify { tvShowRemoteDataSource.getTvShowImages(tvId) }
        coVerify { tvShowRemoteDataSource.getTvShowTrailer(tvId) }
    }

    @Test
    fun `getTvShowCastMembers should return list of cast members when remote call succeeds`() = runTest {
        // Given
        val tvId = 123L
        val mockCastMemberDtos = listOf(createMockCastMemberDto())
        val expectedCastMembers = listOf(createMockCastMember())

        coEvery { tvShowRemoteDataSource.getTvShowCastMembers(tvId) } returns mockCastMemberDtos

        val result = tvShowRepositoryImpl.getTvShowCastMembers(tvId)

        assertEquals(expectedCastMembers.size, result.size)
        assertEquals(expectedCastMembers[0].actor.id, result[0].actor.id)
        assertEquals(expectedCastMembers[0].actor.name, result[0].actor.name)
        assertEquals(expectedCastMembers[0].characterName, result[0].characterName)
        coVerify { tvShowRemoteDataSource.getTvShowCastMembers(tvId) }
    }

    @Test
    fun `getTvShowCastMembers should return empty list when no cast members found`() = runTest {
        // Given
        val tvId = 123L
        coEvery { tvShowRemoteDataSource.getTvShowCastMembers(tvId) } returns emptyList()

        val result = tvShowRepositoryImpl.getTvShowCastMembers(tvId)

        assertEquals(emptyList<CastMember>(), result)
        coVerify { tvShowRemoteDataSource.getTvShowCastMembers(tvId) }
    }

    @Test
    fun `getTvShowImages should return list of image urls when remote call succeeds`() = runTest {
        // Given
        val tvId = 123L
        val mockImages = listOf("/image1.jpg", "/image2.jpg", "/image3.jpg")

        coEvery { tvShowRemoteDataSource.getTvShowImages(tvId) } returns mockImages

        val result = tvShowRepositoryImpl.getTvShowImages(tvId)

        assertEquals(mockImages, result)
        coVerify { tvShowRemoteDataSource.getTvShowImages(tvId) }
    }

    @Test
    fun `getTvShowImages should return empty list when no images found`() = runTest {
        // Given
        val tvId = 123L
        coEvery { tvShowRemoteDataSource.getTvShowImages(tvId) } returns emptyList()

        val result = tvShowRepositoryImpl.getTvShowImages(tvId)

        assertEquals(emptyList<String>(), result)
        coVerify { tvShowRemoteDataSource.getTvShowImages(tvId) }
    }

    @Test
    fun `getTvShowsByGenre should return list of tv shows when remote call succeeds`() = runTest {
        // Given
        val genreId = 18L
        val page = 1
        listOf(createMockTvShowDto())
        val expectedTvShows = createMockPagedTvShowDto()

        coEvery { tvShowRemoteDataSource.getTvShowsByGenre(genreId, page) } returns expectedTvShows

        val result = tvShowRepositoryImpl.getTvShowsByGenre(genreId, page, 20)

        assertEquals(expectedTvShows.data.size, result.data.size)
        assertEquals(expectedTvShows.data[0].id, result.data[0].id)
        assertEquals(expectedTvShows.data[0].title, result.data[0].title)
        coVerify { tvShowRemoteDataSource.getTvShowsByGenre(genreId, page) }
    }

    @Test
    fun `getTvShowsByGenre should return empty list when no tv shows found`() = runTest {
        // Given
        val genreId = 18L
        val page = 1
        coEvery { tvShowRemoteDataSource.getTvShowsByGenre(genreId, page) } returns PagedResultDto(
            emptyList<TvShowDto>(),
            nextKey = null,
            prevKey = 10
        )

        val result = tvShowRepositoryImpl.getTvShowsByGenre(genreId, page, pageSize = 20)

        assertEquals(emptyList<TvShowDto>(), result.data)
        coVerify { tvShowRemoteDataSource.getTvShowsByGenre(genreId, page) }
    }


    @Test
    fun `getTvShowSeasonEpisodes should return list of episodes when remote call succeeds`() = runTest {
        // Given
        val tvId = 123L
        val seasonNumber = 1
        val mockEpisodeDtos = listOf(createMockEpisodeDto())
        val expectedEpisodes = listOf(createMockEpisode())

        coEvery { tvShowRemoteDataSource.getTvShowEpisodes(tvId, seasonNumber) } returns mockEpisodeDtos

        val result = tvShowRepositoryImpl.getTvShowSeasonEpisodes(tvId, seasonNumber)

        assertEquals(expectedEpisodes.size, result.size)
        assertEquals(expectedEpisodes[0].id, result[0].id)
        assertEquals(expectedEpisodes[0].title, result[0].title)
        assertEquals(expectedEpisodes[0].episodeNumber, result[0].episodeNumber)
        coVerify { tvShowRemoteDataSource.getTvShowEpisodes(tvId, seasonNumber) }
    }

    @Test
    fun `getTvShowSeasonEpisodes should return empty list when no episodes found`() = runTest {
        // Given
        val tvId = 123L
        val seasonNumber = 1
        coEvery { tvShowRemoteDataSource.getTvShowEpisodes(tvId, seasonNumber) } returns emptyList()

        val result = tvShowRepositoryImpl.getTvShowSeasonEpisodes(tvId, seasonNumber)

        assertEquals(emptyList<Episode>(), result)
        coVerify { tvShowRemoteDataSource.getTvShowEpisodes(tvId, seasonNumber) }
    }

    @Test
    fun `getTvShowReviews should return list of reviews when remote call succeeds`() = runTest {
        // Given
        val tvId = 123L
        val mockReviewDtos = listOf(createMockReviewDto())
        val expectedReviews = listOf(createMockReview())

        coEvery { tvShowRemoteDataSource.getTvShowReviews(tvId) } returns mockReviewDtos

        val result = tvShowRepositoryImpl.getTvShowReviews(tvId)

        assertEquals(expectedReviews.size, result.size)
        assertEquals(expectedReviews[0].id, result[0].id)
        assertEquals(expectedReviews[0].contentTitle, result[0].contentTitle)
        assertEquals(expectedReviews[0].authorName, result[0].authorName)
        coVerify { tvShowRemoteDataSource.getTvShowReviews(tvId) }
    }

    @Test
    fun `getTvShowReviews should return empty list when no reviews found`() = runTest {
        // Given
        val tvId = 123L
        coEvery { tvShowRemoteDataSource.getTvShowReviews(tvId) } returns emptyList()

        val result = tvShowRepositoryImpl.getTvShowReviews(tvId)

        assertEquals(emptyList<Review>(), result)
        coVerify { tvShowRemoteDataSource.getTvShowReviews(tvId) }
    }


    @Test
    fun `getTopRatedTvShows should return paged result when remote call succeeds`() = runTest {
        // Given
        val page = 1
        val mockTvShowDtos = listOf(createMockTvShowDto())
        val mockPagedResult = PagedResultDto(mockTvShowDtos, nextKey = 2, prevKey = null)

        coEvery { tvShowRemoteDataSource.getTopRatedTvShows(page) } returns mockPagedResult

        val result = tvShowRepositoryImpl.getTopRatedTvShows(page)

        assertEquals(1, result.data.size)
        assertEquals(789L, result.data[0].id)
        assertEquals("Test TV Show", result.data[0].title)
        assertEquals(2, result.nextKey)
        assertEquals(null, result.prevKey)
        coVerify { tvShowRemoteDataSource.getTopRatedTvShows(page) }
    }


    @Test
    fun `getPopularTvShows should return list of tv shows when remote call succeeds`() = runTest {
        // Given
        val mockTvShowDtos = listOf(createMockTvShowDto())
        val expectedTvShows = listOf(createMockTvShow())

        coEvery { tvShowRemoteDataSource.getPopularTvShows() } returns mockTvShowDtos

        val result = tvShowRepositoryImpl.getPopularTvShows()

        assertEquals(expectedTvShows.size, result.size)
        assertEquals(expectedTvShows[0].id, result[0].id)
        assertEquals(expectedTvShows[0].title, result[0].title)
        coVerify { tvShowRemoteDataSource.getPopularTvShows() }
    }

    @Test
    fun `getPopularTvShows should return empty list when no popular tv shows found`() = runTest {
        // Given
        coEvery { tvShowRemoteDataSource.getPopularTvShows() } returns emptyList()

        val result = tvShowRepositoryImpl.getPopularTvShows()

        assertEquals(emptyList<TvShow>(), result)
        coVerify { tvShowRemoteDataSource.getPopularTvShows() }
    }

    @Test
    fun `getTrendingTvShows should return paged result when remote call succeeds`() = runTest {
        // Given
        val page = 1
        val mockTvShowDtos = listOf(createMockTvShowDto())
        val mockPagedResult = PagedResultDto(mockTvShowDtos, nextKey = 2, prevKey = null)

        coEvery { tvShowRemoteDataSource.getTrendingTvShows(page) } returns mockPagedResult

        val result = tvShowRepositoryImpl.getTrendingTvShows(page)

        assertEquals(1, result.data.size)
        assertEquals(789L, result.data[0].id)
        assertEquals("Test TV Show", result.data[0].title)
        assertEquals(2, result.nextKey)
        assertEquals(null, result.prevKey)
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
            listOf(createMockTvShowDto()),
            nextKey = 2,
            prevKey = null
        )


        private fun createMockTvShowDto() = TvShowDto(
            id = 789L,
            title = "Test TV Show",
            genres = listOf(GenreDto(18, "Drama", type = GenreDto.GenreType.TV_SHOW)),
            imdbRating = 7.9,
            userRating = 8.1,
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
            userRating = 8.1,
            releaseDate = LocalDate.parse("2023-01-01"),
            overview = "Test overview for TV Show",
            posterImageURL = "/tv_poster.jpg",
            numberOfSeasons = 3,
            trailerURL = " ",
            headerImagesURLs = listOf("/header1.jpg", "/header2.jpg", "/header3.jpg")
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
            id = 123L,
            name = "John Doe",
            imageUrl = "/profile.jpg",
            biography = "Famous actor biography",
            birthdayDate = "1980-01-01",
            deathDate = null,
            placeOfBirth = "New York, USA",
            headerPictures = listOf("/header1.jpg", "/header2.jpg"),
            department = "Acting"
        )

        private fun createMockActor() = Actor(
            id = 123L,
            name = "John Doe",
            profilePictureURL = "/profile.jpg",
            birthDate = LocalDate.parse("1980-01-01"),
            placeOfBirth = "New York, USA",
            deathDate = null,
            biography = "Famous actor biography",
            headerPictures = listOf("/header1.jpg", "/header2.jpg"),
            department = "Acting"
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
            headerPictures = listOf("/header1.jpg", "/header2.jpg"),
            genres = emptyList(),
            trailerUrl = " "
        )

        private fun createMockReviewDto() = ReviewDto(
            id = "review123",
            contentTitle = "Great movie! Highly recommended.",
            authorName = "John Reviewer",
            rating = 9.0f,
            authorAvatarUrl = "https://example.com/avatar.jpg",
            reviewText = "This movie was fantastic! The plot was engaging and the acting was top-notch.",
            postedDate = LocalDate.parse("2023-01-01").toString()
        )

        private fun createMockReview() = Review(
            id = "review123",
            contentTitle = "Great movie! Highly recommended.",
            authorName = "John Reviewer",
            rating = 9.0f,
            authorAvatarUrl = "https://example.com/avatar.jpg",
            reviewText = "This movie was fantastic! The plot was engaging and the acting was top-notch.",
            postedDate = LocalDate.parse("2023-01-01")
        )

    }
}