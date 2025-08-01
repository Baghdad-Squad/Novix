package com.baghdad.remoteDataSource

import com.baghdad.remoteDataSource.apiService.TvShowApiService
import com.baghdad.remoteDataSource.response.CastMemberResponse
import com.baghdad.remoteDataSource.response.CastMembersResponse
import com.baghdad.remoteDataSource.response.MovieAuthorDetails
import com.baghdad.remoteDataSource.response.ReviewResponse
import com.baghdad.remoteDataSource.response.ReviewsResponse
import com.baghdad.remoteDataSource.response.actor.ImageResponse
import com.baghdad.remoteDataSource.response.tvShow.EpisodeResponse
import com.baghdad.remoteDataSource.response.tvShow.PopularTvShowsResponse
import com.baghdad.remoteDataSource.response.tvShow.SeasonDetailResponse
import com.baghdad.remoteDataSource.response.tvShow.TVShowDetailsResponse
import com.baghdad.remoteDataSource.response.tvShow.TVShowImagesResponse
import com.baghdad.remoteDataSource.response.tvShow.TVShowVideosResponse
import com.baghdad.remoteDataSource.response.tvShow.TopRatedTvShowSearchResponse
import com.baghdad.remoteDataSource.response.tvShow.TrendingTvShowsResponse
import com.baghdad.remoteDataSource.response.tvShow.TvShowResponse
import com.baghdad.repository.logger.Logger
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import retrofit2.Response

class RemoteTvShowDataSourceImplTest {

    private lateinit var dataSource: RemoteTvShowDataSourceImpl
    private lateinit var tvShowApiService: TvShowApiService
    private lateinit var logger: Logger

    @BeforeEach
    fun setUp() {
        tvShowApiService = mockk(relaxed = true)
        logger = mockk(relaxed = true)
        dataSource = RemoteTvShowDataSourceImpl(tvShowApiService, logger)
    }

    @Test
    fun `getTvShowCastMembers should return cast members`() = runTest {
        // Given
        val tvId = 1L
        val response = CastMembersResponse(
            cast = listOf(
                CastMemberResponse(
                    id = 1,
                    name = "Actor 1",
                    character = "Character 1",
                    profilePath = "/actor1.jpg"
                )
            )
        )
        coEvery { tvShowApiService.getTvShowCastMembers(tvId) } returns Response.success(response)

        // When
        val result = dataSource.getTvShowCastMembers(tvId)

        // Then
        assertThat(result).hasSize(1)
        assertThat(result[0].actor.id).isEqualTo(1L)
        assertThat(result[0].characterName).isEqualTo("Character 1")
    }

    @Test
    fun `getTvShowImages should return formatted image URLs`() = runTest {
        // Given
        val tvId = 1L
        val response = TVShowImagesResponse(
            backdrops = listOf(
                ImageResponse(filePath = "/backdrop1.jpg"),
                ImageResponse(filePath = "/backdrop2.jpg")
            )
        )
        coEvery { tvShowApiService.getTvShowImages(tvId) } returns Response.success(response)

        // When
        val result = dataSource.getTvShowImages(tvId)

        // Then
        assertThat(result).hasSize(2)
        assertThat(result[0]).isEqualTo("https://image.tmdb.org/t/p/w500/backdrop1.jpg")
    }

    @Test
    fun `getTvShowsByGenre should return TV shows list`() = runTest {
        // Given
        val genreId = 1L
        val page = 1
        val response = TvShowResponse(
            results = listOf(
                TVShowDetailsResponse(
                    id = 1,
                    name = "Show 1"
                )
            )
        )
        coEvery { tvShowApiService.getTvShowsByGenre(genreId, page) } returns Response.success(
            response
        )

        // When
        val result = dataSource.getTvShowsByGenre(genreId, page)

        // Then
        assertThat(result.data[0].id).isEqualTo(1L)
    }

    @Test
    fun `getTvShowEpisodes should return episodes list`() = runTest {
        // Given
        val tvId = 1L
        val seasonNumber = 1
        val response = SeasonDetailResponse(
            episodes = listOf(
                EpisodeResponse(
                    id = 1,
                    name = "Episode 1",
                    episodeNumber = 1
                )
            )
        )
        coEvery { tvShowApiService.getTvShowEpisodes(tvId, seasonNumber) } returns Response.success(
            response
        )

        // When
        val result = dataSource.getTvShowEpisodes(tvId, seasonNumber)

        // Then
        assertThat(result).hasSize(1)
        assertThat(result[0].id).isEqualTo(1L)
        assertThat(result[0].episodeNumber).isEqualTo(1)
    }

    @Test
    fun `getTrendingTvShows should return paged results`() = runTest {
        // Given
        val page = 1
        val response = TrendingTvShowsResponse(
            page = page,
            results = listOf(
                TrendingTvShowsResponse.TrendingTvShow(
                    id = 1,
                    name = "Trending Show"
                )
            ),
            totalPages = 10
        )
        coEvery { tvShowApiService.getTrendingTvShows(page) } returns Response.success(response)

        // When
        val result = dataSource.getTrendingTvShows(page)

        // Then
        assertThat(result.data).hasSize(1)
        assertThat(result.data[0].id).isEqualTo(1L)
    }

    @Test
    fun `getTvShowCastMembers should return empty list when null cast`() = runTest {
        // Given
        val tvId = 1L
        val response = CastMembersResponse(cast = null)
        coEvery { tvShowApiService.getTvShowCastMembers(tvId) } returns Response.success(response)

        // When
        val result = dataSource.getTvShowCastMembers(tvId)

        // Then
        assertThat(result).isEmpty()
    }

    @Test
    fun `getTvShowImages should return empty list when null backdrops`() = runTest {
        // Given
        val tvId = 1L
        val response = TVShowImagesResponse(backdrops = null)
        coEvery { tvShowApiService.getTvShowImages(tvId) } returns Response.success(response)

        // When
        val result = dataSource.getTvShowImages(tvId)

        // Then
        assertThat(result).isEmpty()
    }

    @Test
    fun `getTvShowEpisodes should return empty list when null episodes`() = runTest {
        // Given
        val tvId = 1L
        val seasonNumber = 1
        val response = SeasonDetailResponse(episodes = null)
        coEvery { tvShowApiService.getTvShowEpisodes(tvId, seasonNumber) } returns Response.success(
            response
        )

        // When
        val result = dataSource.getTvShowEpisodes(tvId, seasonNumber)

        // Then
        assertThat(result).isEmpty()
    }

    @Test
    fun `getPopularTvShows should return list of TvShowDto`() = runTest {
        // Given
        val response = PopularTvShowsResponse(
            results = listOf(
                PopularTvShowsResponse.Result(
                    id = 1,
                    name = "Popular Show"
                )
            )
        )
        coEvery { tvShowApiService.getPopularTvShows() } returns Response.success(response)

        // When
        val result = dataSource.getPopularTvShows()

        // Then
        assertThat(result).hasSize(1)
        assertThat(result[0].id).isEqualTo(1L)
    }

    @Test
    fun `getTopRatedTvShows should return paged result of TvShowDto`() = runTest {
        // Given
        val page = 1
        val response = TopRatedTvShowSearchResponse(
            page = page,
            results = listOf(
                TopRatedTvShowSearchResponse.Result(
                    id = 1,
                    title = "Top Rated Show",
                    voteAverage = 8.5,
                    genreIds = listOf(1),
                    releaseDate = "2024-01-01",
                    overview = "Overview",
                    posterPath = "/poster.jpg"
                )
            ),
            totalPages = 10,
            totalResults = 1
        )
        coEvery { tvShowApiService.getTopRatedTvShows(page) } returns Response.success(response)

        // When
        val result = dataSource.getTopRatedTvShows(page)

        // Then
        assertThat(result.data).hasSize(1)
        assertThat(result.data[0].id).isEqualTo(1L)
    }

    @Test
    fun `getTvShowTrailer should return trailer URL`() = runTest {
        // Given
        val tvId = 1L
        val response = TVShowVideosResponse(
            results = listOf(
                TVShowVideosResponse.Result(
                    site = "YouTube",
                    key = "abcd1234",
                    type = "Trailer"
                )
            )
        )
        coEvery { tvShowApiService.getTvShowTrailer(tvId) } returns Response.success(response)

        // When
        val result = dataSource.getTvShowTrailer(tvId)

        // Then
        assertThat(result).contains("youtube.com")
        assertThat(result).contains("abcd1234")
    }

    @Test
    fun `getTvShowReviews should return list of ReviewDto`() = runTest {
        // Given
        val tvId = 1L
        val response = ReviewsResponse(
            results = listOf(
                ReviewResponse(
                    id = "rev1",
                    author = "Author",
                    content = "Great Show!",
                    createdAt = "2024-01-01",
                    authorDetails = MovieAuthorDetails(
                        name = "Author Name",
                        username = "author123",
                        avatarPath = "/avatar.jpg",
                        rating = 4.5
                    )
                )
            )
        )
        coEvery { tvShowApiService.getTvShowReviews(tvId) } returns Response.success(response)

        // When
        val result = dataSource.getTvShowReviews(tvId)

        // Then
        assertThat(result).hasSize(1)
        assertThat(result[0].id).isEqualTo("rev1")
        assertThat(result[0].rating).isEqualTo(4.5)
    }

    @Test
    fun `getTvShowDetails should return TvShowDto`() = runTest {
        // Given
        val tvId = 1L
        val response = TVShowDetailsResponse(
            id = 1,
            name = "TV Show",
            numberOfSeasons = 3,
            overview = "Description",
            posterPath = "/poster.jpg",
            voteAverage = 8.0,
            firstAirDate = "2022-01-01",
            genres = listOf()
        )
        coEvery { tvShowApiService.getTvShowDetails(tvId) } returns Response.success(response)

        // When
        val result = dataSource.getTvShowDetails(tvId)

        // Then
        assertThat(result.id).isEqualTo(1L)
        assertThat(result.title).isEqualTo("TV Show")
        assertThat(result.numberOfSeasons).isEqualTo(3)
    }
}