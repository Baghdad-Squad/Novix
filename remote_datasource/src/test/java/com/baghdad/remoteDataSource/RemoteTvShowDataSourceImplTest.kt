package com.baghdad.remoteDataSource

import com.baghdad.remoteDataSource.apiService.TvShowApiService
import com.baghdad.remoteDataSource.request.RatingRequest
import com.baghdad.remoteDataSource.response.castMembers.CastMembersResponse
import com.baghdad.remoteDataSource.response.mediaAccount.MediaAccountStatesResponse
import com.baghdad.remoteDataSource.response.rate.RatingResponse
import com.baghdad.remoteDataSource.response.reviews.ReviewsResponse
import com.baghdad.remoteDataSource.response.tvShow.MyRatingTvShowResponse
import com.baghdad.remoteDataSource.response.tvShow.PopularTvShowsResponse
import com.baghdad.remoteDataSource.response.tvShow.SeasonDetailResponse
import com.baghdad.remoteDataSource.response.tvShow.TVShowDetailsResponse
import com.baghdad.remoteDataSource.response.tvShow.TVShowImagesResponse
import com.baghdad.remoteDataSource.response.tvShow.TVShowVideosResponse
import com.baghdad.remoteDataSource.response.tvShow.TopRatedTvShowSearchResponse
import com.baghdad.remoteDataSource.response.tvShow.TrendingTvShowsResponse
import com.baghdad.remoteDataSource.response.tvShow.TvShowResponse
import com.baghdad.repository.logger.Logger
import com.baghdad.repository.model.ActorDto
import com.baghdad.repository.model.CastMemberDto
import com.baghdad.repository.model.EpisodeDto
import com.baghdad.repository.model.GenreDto
import com.baghdad.repository.model.MediaAccountStateDto
import com.baghdad.repository.model.ReviewDto
import com.baghdad.repository.model.TvShowDto
import com.google.common.truth.Truth.assertThat
import com.google.gson.JsonObject
import com.google.gson.JsonPrimitive
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import retrofit2.Response

class RemoteTvShowDataSourceImplTest {

    private val tvShowApiService = mockk<TvShowApiService>()
    private val logger = mockk<Logger>(relaxed = true)
    private val dataSource = RemoteTvShowDataSourceImpl(tvShowApiService, logger)

    @Test
    fun `should return TvShowDto when getTvShowDetails is called`() = runTest {
        val successResponse = Response.success(tvShowDetailsResponse)
        coEvery { tvShowApiService.getTvShowDetails(TV_SHOW_ID) } returns successResponse

        val result = dataSource.getTvShowDetails(TV_SHOW_ID)

        assertThat(result).isEqualTo(expectedTvShowDto)
        coVerify(exactly = 1) { tvShowApiService.getTvShowDetails(TV_SHOW_ID) }
    }

    @Test
    fun `should handle null values when getTvShowDetails is called`() = runTest {
        val successResponse = Response.success(tvShowDetailsResponseValidId)
        coEvery { tvShowApiService.getTvShowDetails(TV_SHOW_ID) } returns successResponse
        val result = dataSource.getTvShowDetails(TV_SHOW_ID)
        assertThat(result).isEqualTo(expectedTvShowDtoWithDefaults)
    }


    @Test
    fun `should return cast members when getTvShowCastMembers is called`() = runTest {
        val successResponse = Response.success(castMembersResponse)
        coEvery { tvShowApiService.getTvShowCastMembers(TV_SHOW_ID) } returns successResponse

        val result = dataSource.getTvShowCastMembers(TV_SHOW_ID)

        assertThat(result).hasSize(1)
        assertThat(result[0]).isEqualTo(expectedCastMemberDto)
        coVerify(exactly = 1) { tvShowApiService.getTvShowCastMembers(TV_SHOW_ID) }
    }

    @Test
    fun `should filter out cast members with null ids when getTvShowCastMembers is called`() =
        runTest {
            val successResponse = Response.success(castMembersResponseWithNulls)
            coEvery { tvShowApiService.getTvShowCastMembers(TV_SHOW_ID) } returns successResponse

            val result = dataSource.getTvShowCastMembers(TV_SHOW_ID)


            assertThat(result).hasSize(1)
            assertThat(result[0]).isEqualTo(expectedCastMemberDto)
        }

    @Test
    fun `should return empty list when getTvShowCastMembers receives empty cast`() = runTest {
        val successResponse = Response.success(castMembersResponseEmpty)
        coEvery { tvShowApiService.getTvShowCastMembers(TV_SHOW_ID) } returns successResponse

        val result = dataSource.getTvShowCastMembers(TV_SHOW_ID)

        assertThat(result).isEmpty()
    }

    @Test
    fun `should return image URLs when getTvShowImages is called`() = runTest {
        val successResponse = Response.success(tvShowImagesResponse)
        coEvery { tvShowApiService.getTvShowImages(TV_SHOW_ID) } returns successResponse

        val result = dataSource.getTvShowImages(TV_SHOW_ID)

        assertThat(result).hasSize(1)
        assertThat(result[0]).isEqualTo("https://image.tmdb.org/t/p/w500$BACKDROP_PATH")
        coVerify(exactly = 1) { tvShowApiService.getTvShowImages(TV_SHOW_ID) }
    }

    @Test
    fun `should return empty list when getTvShowImages receives empty backdrops`() = runTest {
        val successResponse = Response.success(tvShowImagesResponseEmpty)
        coEvery { tvShowApiService.getTvShowImages(TV_SHOW_ID) } returns successResponse

        val result = dataSource.getTvShowImages(TV_SHOW_ID)

        assertThat(result).isEmpty()
    }

    @Test
    fun `should handle null file paths when getTvShowImages is called`() = runTest {
        val successResponse = Response.success(tvShowImagesResponseWithNulls)
        coEvery { tvShowApiService.getTvShowImages(TV_SHOW_ID) } returns successResponse

        val result = dataSource.getTvShowImages(TV_SHOW_ID)


        assertThat(result).hasSize(2)
        assertThat(result[0]).isEqualTo("https://image.tmdb.org/t/p/w500$BACKDROP_PATH")
        assertThat(result[1]).isEqualTo("")
    }

    @Test
    fun `should return paged tv shows when getTvShowsByGenre is called`() = runTest {
        val successResponse = Response.success(tvShowResponse)
        coEvery { tvShowApiService.getTvShowsByGenre(GENRE_ID, PAGE) } returns successResponse

        val result = dataSource.getTvShowsByGenre(GENRE_ID, PAGE)

        assertThat(result.data).hasSize(1)
        assertThat(result.data[0]).isEqualTo(expectedTvShowDto)
        assertThat(result.nextKey).isEqualTo(2)
        assertThat(result.prevKey).isNull()
        coVerify(exactly = 1) { tvShowApiService.getTvShowsByGenre(GENRE_ID, PAGE) }
    }

    @Test
    fun `should filter out tv shows with null ids when getTvShowsByGenre is called`() = runTest {
        val successResponse = Response.success(tvShowResponseWithNulls)
        coEvery { tvShowApiService.getTvShowsByGenre(GENRE_ID, PAGE) } returns successResponse

        val result = dataSource.getTvShowsByGenre(GENRE_ID, PAGE)


        assertThat(result.data).hasSize(2)
        assertThat(result.data[0]).isEqualTo(expectedTvShowDto)
        assertThat(result.data[1]).isEqualTo(expectedTvShowDtoWithDefaults)
    }

    @Test
    fun `should return empty list when getTvShowsByGenre receives empty results`() = runTest {
        val successResponse = Response.success(tvShowResponseEmpty)
        coEvery { tvShowApiService.getTvShowsByGenre(GENRE_ID, PAGE) } returns successResponse

        val result = dataSource.getTvShowsByGenre(GENRE_ID, PAGE)

        assertThat(result.data).isEmpty()
        assertThat(result.nextKey).isNull()
        assertThat(result.prevKey).isNull()
    }

    @Test
    fun `should return episodes when getTvShowEpisodes is called`() = runTest {
        val successResponse = Response.success(seasonDetailResponse)
        coEvery {
            tvShowApiService.getTvShowEpisodes(
                TV_SHOW_ID,
                SEASON_NUMBER
            )
        } returns successResponse

        val result = dataSource.getTvShowEpisodes(TV_SHOW_ID, SEASON_NUMBER)

        assertThat(result).hasSize(1)
        assertThat(result[0]).isEqualTo(expectedEpisodeDto)
        coVerify(exactly = 1) { tvShowApiService.getTvShowEpisodes(TV_SHOW_ID, SEASON_NUMBER) }
    }

    @Test
    fun `should filter out episodes with null ids when getTvShowEpisodes is called`() = runTest {
        val successResponse = Response.success(seasonDetailResponseWithNulls)
        coEvery {
            tvShowApiService.getTvShowEpisodes(
                TV_SHOW_ID,
                SEASON_NUMBER
            )
        } returns successResponse

        val result = dataSource.getTvShowEpisodes(TV_SHOW_ID, SEASON_NUMBER)

        assertThat(result).hasSize(1)
        assertThat(result[0]).isEqualTo(expectedEpisodeDto)
    }

    @Test
    fun `should return empty list when getTvShowEpisodes receives empty episodes`() = runTest {
        val successResponse = Response.success(seasonDetailResponseEmpty)
        coEvery {
            tvShowApiService.getTvShowEpisodes(
                TV_SHOW_ID,
                SEASON_NUMBER
            )
        } returns successResponse

        val result = dataSource.getTvShowEpisodes(TV_SHOW_ID, SEASON_NUMBER)

        assertThat(result).isEmpty()
    }

    @Test
    fun `should return reviews when getTvShowReviews is called`() = runTest {
        val successResponse = Response.success(reviewsResponse)
        coEvery { tvShowApiService.getTvShowReviews(TV_SHOW_ID) } returns successResponse

        val result = dataSource.getTvShowReviews(TV_SHOW_ID)

        assertThat(result).hasSize(1)
        assertThat(result[0]).isEqualTo(expectedReviewDto)
        coVerify(exactly = 1) { tvShowApiService.getTvShowReviews(TV_SHOW_ID) }
    }

    @Test
    fun `should filter out reviews with null ids when getTvShowReviews is called`() = runTest {
        val successResponse = Response.success(reviewsResponseWithNulls)
        coEvery { tvShowApiService.getTvShowReviews(TV_SHOW_ID) } returns successResponse

        val result = dataSource.getTvShowReviews(TV_SHOW_ID)

        assertThat(result).hasSize(1)
        assertThat(result[0]).isEqualTo(expectedReviewDto)
    }

    @Test
    fun `should return YouTube URL when getTvShowTrailer is called`() = runTest {
        val successResponse = Response.success(tvShowVideosResponse)
        coEvery { tvShowApiService.getTvShowTrailer(TV_SHOW_ID) } returns successResponse

        val result = dataSource.getTvShowTrailer(TV_SHOW_ID)

        assertThat(result).isEqualTo(YOUTUBE_URL)
        coVerify(exactly = 1) { tvShowApiService.getTvShowTrailer(TV_SHOW_ID) }
    }

    @Test
    fun `should return empty string when getTvShowTrailer finds no trailer`() = runTest {
        val successResponse = Response.success(tvShowVideosResponseNoTrailer)
        coEvery { tvShowApiService.getTvShowTrailer(TV_SHOW_ID) } returns successResponse

        val result = dataSource.getTvShowTrailer(TV_SHOW_ID)

        assertThat(result).isEmpty()
    }

    @Test
    fun `should return empty string when getTvShowTrailer receives empty results`() = runTest {
        val successResponse = Response.success(tvShowVideosResponseEmpty)
        coEvery { tvShowApiService.getTvShowTrailer(TV_SHOW_ID) } returns successResponse

        val result = dataSource.getTvShowTrailer(TV_SHOW_ID)

        assertThat(result).isEmpty()
    }

    @Test
    fun `should return paged tv shows when getTopRatedTvShows is called`() = runTest {
        val successResponse = Response.success(topRatedTvShowSearchResponse)
        coEvery {
            tvShowApiService.getTopRatedTvShows(
                PAGE,
                sortBy = "vote_average.desc",
                minVoteCount = 200
            )
        } returns successResponse

        val result = dataSource.getTopRatedTvShows(PAGE)

        assertThat(result.data).hasSize(1)
        assertThat(result.nextKey).isEqualTo(2)
        assertThat(result.prevKey).isNull()
        coVerify(exactly = 1) {
            tvShowApiService.getTopRatedTvShows(
                PAGE,
                sortBy = "vote_average.desc",
                minVoteCount = 200
            )
        }
    }

    @Test
    fun `should call getTopRatedTvShows with correct parameters`() = runTest {
        val differentPage = 3
        val successResponse = Response.success(topRatedTvShowSearchResponse)
        coEvery {
            tvShowApiService.getTopRatedTvShows(
                differentPage,
                sortBy = "vote_average.desc",
                minVoteCount = 200
            )
        } returns successResponse

        dataSource.getTopRatedTvShows(differentPage)

        coVerify(exactly = 1) {
            tvShowApiService.getTopRatedTvShows(
                differentPage,
                sortBy = "vote_average.desc",
                minVoteCount = 200
            )
        }
    }

    @Test
    fun `should return paged tv shows when getTrendingTvShows is called`() = runTest {
        val successResponse = Response.success(trendingTvShowsResponse)
        coEvery { tvShowApiService.getTrendingTvShows(PAGE) } returns successResponse

        val result = dataSource.getTrendingTvShows(PAGE)

        assertThat(result.data).hasSize(1)
        assertThat(result.nextKey).isEqualTo(2)
        assertThat(result.prevKey).isNull()
        coVerify(exactly = 1) { tvShowApiService.getTrendingTvShows(PAGE) }
    }

    @Test
    fun `should call getTrendingTvShows with different page`() = runTest {
        val differentPage = 2
        val successResponse = Response.success(trendingTvShowsResponse)
        coEvery { tvShowApiService.getTrendingTvShows(differentPage) } returns successResponse

        dataSource.getTrendingTvShows(differentPage)

        coVerify(exactly = 1) { tvShowApiService.getTrendingTvShows(differentPage) }
    }

    @Test
    fun `should add tv show rating when addTvShowRate is called`() = runTest {
        val successResponse = Response.success(ratingResponse)
        coEvery {
            tvShowApiService.addTvShowRate(TV_SHOW_ID, RatingRequest(RATING))
        } returns successResponse

        dataSource.addTvShowRate(TV_SHOW_ID, RATING)

        coVerify(exactly = 1) {
            tvShowApiService.addTvShowRate(TV_SHOW_ID, RatingRequest(RATING))
        }
    }

    @Test
    fun `should add tv show rating with different parameters`() = runTest {
        val differentRating = 5
        val successResponse = Response.success(ratingResponse)
        coEvery {
            tvShowApiService.addTvShowRate(TV_SHOW_ID, RatingRequest(differentRating))
        } returns successResponse

        dataSource.addTvShowRate(TV_SHOW_ID, differentRating)

        coVerify(exactly = 1) {
            tvShowApiService.addTvShowRate(TV_SHOW_ID, RatingRequest(differentRating))
        }
    }


    @Test
    fun `should delete tv show rating when deleteTvShowRate is called`() = runTest {
        val successResponse = Response.success(ratingResponse)
        coEvery { tvShowApiService.deleteTvShowRate(TV_SHOW_ID) } returns successResponse

        dataSource.deleteTvShowRate(TV_SHOW_ID)

        coVerify(exactly = 1) { tvShowApiService.deleteTvShowRate(TV_SHOW_ID) }
    }

    @Test
    fun `should delete tv show rating with different parameters`() = runTest {
        val differentTvShowId = 999L
        val successResponse = Response.success(ratingResponse)
        coEvery { tvShowApiService.deleteTvShowRate(differentTvShowId) } returns successResponse

        dataSource.deleteTvShowRate(differentTvShowId)

        coVerify(exactly = 1) { tvShowApiService.deleteTvShowRate(differentTvShowId) }
    }

    @Test
    fun `should return media account state when getTvShowAccountStates is called`() = runTest {
        val successResponse = Response.success(mediaAccountStatesResponse)
        coEvery { tvShowApiService.getTvShowAccountStates(TV_SHOW_ID) } returns successResponse

        val result = dataSource.getTvShowAccountStates(TV_SHOW_ID)

        assertThat(result).isEqualTo(expectedMediaAccountStateDto)
        coVerify(exactly = 1) { tvShowApiService.getTvShowAccountStates(TV_SHOW_ID) }
    }

    @Test
    fun `should return media account state not rated when getTvShowAccountStates is called`() =
        runTest {
            val successResponse = Response.success(mediaAccountStatesResponseNotRated)
            coEvery { tvShowApiService.getTvShowAccountStates(TV_SHOW_ID) } returns successResponse

            val result = dataSource.getTvShowAccountStates(TV_SHOW_ID)

            assertThat(result).isEqualTo(expectedMediaAccountStateDtoNotRated)
        }

    @Test
    fun `should call getTvShowAccountStates with different parameters`() = runTest {
        val differentTvShowId = 456L
        val successResponse = Response.success(mediaAccountStatesResponse)
        coEvery { tvShowApiService.getTvShowAccountStates(differentTvShowId) } returns successResponse

        dataSource.getTvShowAccountStates(differentTvShowId)

        coVerify(exactly = 1) { tvShowApiService.getTvShowAccountStates(differentTvShowId) }
    }

    @Test
    fun `should return tv shows list when getPopularTvShows is called`() = runTest {
        val successResponse = Response.success(popularTvShowsResponse)
        coEvery { tvShowApiService.getPopularTvShows() } returns successResponse

        val result = dataSource.getPopularTvShows()

        assertThat(result).hasSize(1)
        coVerify(exactly = 1) { tvShowApiService.getPopularTvShows() }
    }

    @Test
    fun `should return empty list when getPopularTvShows receives empty results`() = runTest {
        val emptyPopularTvShowsResponse = PopularTvShowsResponse(
            page = PAGE,
            results = emptyList(),
            totalPages = 0,
            totalResults = 0
        )
        val successResponse = Response.success(emptyPopularTvShowsResponse)
        coEvery { tvShowApiService.getPopularTvShows() } returns successResponse

        val result = dataSource.getPopularTvShows()

        assertThat(result).isEmpty()
    }

    @Test
    fun `should return empty list when getPopularTvShows receives null results`() = runTest {
        val nullPopularTvShowsResponse = PopularTvShowsResponse(
            page = PAGE,
            results = null,
            totalPages = TOTAL_PAGES,
            totalResults = TOTAL_RESULTS
        )
        val successResponse = Response.success(nullPopularTvShowsResponse)
        coEvery { tvShowApiService.getPopularTvShows() } returns successResponse

        val result = dataSource.getPopularTvShows()

        assertThat(result).isEmpty()
    }

    @Test
    fun `should return paged tv shows when getUserRatedTvShows is called`() = runTest {
        val successResponse = Response.success(myRatingTvShowResponse)
        coEvery { tvShowApiService.getUserRatedTvShows(ACCOUNT_ID, PAGE) } returns successResponse

        val result = dataSource.getUserRatedTvShows(ACCOUNT_ID, PAGE)

        assertThat(result.data).hasSize(1)
        assertThat(result.nextKey).isEqualTo(2)
        assertThat(result.prevKey).isNull()
        coVerify(exactly = 1) { tvShowApiService.getUserRatedTvShows(ACCOUNT_ID, PAGE) }
    }

    @Test
    fun `should call getUserRatedTvShows with different parameters`() = runTest {
        val differentAccountId = 456L
        val differentPage = 3
        val successResponse = Response.success(myRatingTvShowResponse)
        coEvery {
            tvShowApiService.getUserRatedTvShows(
                differentAccountId,
                differentPage
            )
        } returns successResponse

        dataSource.getUserRatedTvShows(differentAccountId, differentPage)

        coVerify(exactly = 1) {
            tvShowApiService.getUserRatedTvShows(
                differentAccountId,
                differentPage
            )
        }
    }

    @Test
    fun `should return empty paged result when getUserRatedTvShows receives empty results`() =
        runTest {
            val emptyMyRatingTvShowResponse = MyRatingTvShowResponse(
                page = PAGE,
                results = emptyList(),
                totalPages = 0,
                totalResults = 0
            )
            val successResponse = Response.success(emptyMyRatingTvShowResponse)
            coEvery {
                tvShowApiService.getUserRatedTvShows(
                    ACCOUNT_ID,
                    PAGE
                )
            } returns successResponse

            val result = dataSource.getUserRatedTvShows(ACCOUNT_ID, PAGE)

            assertThat(result.data).isEmpty()
            assertThat(result.nextKey).isNull()
            assertThat(result.prevKey).isNull()
        }

    @Test
    fun `should filter out tv shows with null ids when getUserRatedTvShows is called`() = runTest {
        val myRatingTvShowItemWithNull = MyRatingTvShowResponse.TvShowItem(
            id = null,
            adult = false,
            backdropPath = BACKDROP_PATH,
            genreIds = listOf(18L, 80L),
            originCountry = listOf("US"),
            originalLanguage = "en",
            originalName = "Breaking Bad",
            overview = OVERVIEW,
            popularity = POPULARITY,
            posterPath = POSTER_PATH,
            firstAirDate = FIRST_AIR_DATE,
            name = TV_SHOW_NAME,
            voteAverage = VOTE_AVERAGE,
            voteCount = 1000,
            rating = RATING
        )

        val myRatingTvShowResponseWithNulls = MyRatingTvShowResponse(
            page = PAGE,
            results = listOf(myRatingTvShowItem, myRatingTvShowItemWithNull),
            totalPages = TOTAL_PAGES,
            totalResults = TOTAL_RESULTS
        )

        val successResponse = Response.success(myRatingTvShowResponseWithNulls)
        coEvery { tvShowApiService.getUserRatedTvShows(ACCOUNT_ID, PAGE) } returns successResponse

        val result = dataSource.getUserRatedTvShows(ACCOUNT_ID, PAGE)

        assertThat(result.data).hasSize(1)
    }

    companion object {
        const val TV_SHOW_ID = 1L

        const val SEASON_NUMBER = 1
        const val ACCOUNT_ID = 123L
        const val GENRE_ID = 18L
        const val PAGE = 1
        const val RATING = 8
        const val TOTAL_PAGES = 5
        const val TOTAL_RESULTS = 100
        const val EPISODE_ID = 456L
        const val ACTOR_ID = 789L
        const val REVIEW_ID = 101L

        const val REVIEW_STRING_ID = "101L"

        const val TV_SHOW_NAME = "Breaking Bad"
        const val OVERVIEW =
            "A high school chemistry teacher diagnosed with inoperable lung cancer turns to manufacturing and selling methamphetamine."
        const val FIRST_AIR_DATE = "2008-01-20"
        const val POSTER_PATH = "/ggFHVNu6YYI5L9pCfOacjizRGt.jpg"
        const val BACKDROP_PATH = "/9faGSFi5jam6pDWGNd0p8JcJgXQ.jpg"
        const val PROFILE_PATH = "/73j0Oft-2DUAhwGz6XGaKREV3LH.jpg"
        const val VOTE_AVERAGE = 9.5
        const val NUMBER_OF_SEASONS = 5
        const val POPULARITY = 432.123
        const val RUNTIME = 47
        const val EPISODE_NUMBER = 1
        const val SEASON_NUMBER_EPISODE = 1

        const val ACTOR_NAME = "Bryan Cranston"
        const val CHARACTER_NAME = "Walter White"
        const val KNOWN_FOR_DEPARTMENT = "Acting"

        const val EPISODE_NAME = "Pilot"
        const val AIR_DATE = "2008-01-20"

        const val REVIEW_AUTHOR = "John Doe"
        const val REVIEW_USERNAME = "johndoe"
        const val REVIEW_CONTENT = "Amazing show!"
        const val REVIEW_CREATED_AT = "2023-01-01T12:00:00.000Z"
        const val REVIEW_RATING = 9.0
        const val AVATAR_PATH = "/avatar.jpg"

        const val YOUTUBE_KEY = "abc123"
        const val YOUTUBE_URL = "https://www.youtube.com/watch?v=$YOUTUBE_KEY"

        val genre1 = TVShowDetailsResponse.Genre(id = 18L, name = "Drama")
        val genre2 = TVShowDetailsResponse.Genre(id = 80L, name = "Crime")

        val tvShowDetailsResponse = TVShowDetailsResponse(
            id = TV_SHOW_ID,
            name = TV_SHOW_NAME,
            genres = listOf(genre1, genre2),
            numberOfSeasons = NUMBER_OF_SEASONS,
            overview = OVERVIEW,
            posterPath = POSTER_PATH,
            voteAverage = VOTE_AVERAGE,
            firstAirDate = FIRST_AIR_DATE
        )

        val tvShowDetailsResponseWithNulls = TVShowDetailsResponse(
            id = null,
            name = null,
            genres = null,
            numberOfSeasons = null,
            overview = null,
            posterPath = null,
            voteAverage = null,
            firstAirDate = null
        )

        val tvShowDetailsResponseValidId = TVShowDetailsResponse(
            id = TV_SHOW_ID,
            name = null,
            genres = null,
            numberOfSeasons = null,
            overview = null,
            posterPath = null,
            voteAverage = null,
            firstAirDate = null
        )

        val castMemberResponse = CastMembersResponse.CastMemberResponse(
            id = ACTOR_ID,
            name = ACTOR_NAME,
            profilePath = PROFILE_PATH,
            knownForDepartment = KNOWN_FOR_DEPARTMENT,
            character = CHARACTER_NAME
        )

        val castMemberResponseWithNulls = CastMembersResponse.CastMemberResponse(
            id = null,
            name = null,
            profilePath = null,
            knownForDepartment = null,
            character = null
        )

        val castMembersResponse = CastMembersResponse(
            id = TV_SHOW_ID,
            cast = listOf(castMemberResponse)
        )

        val castMembersResponseWithNulls = CastMembersResponse(
            id = TV_SHOW_ID,
            cast = listOf(castMemberResponse, castMemberResponseWithNulls)
        )

        val castMembersResponseEmpty = CastMembersResponse(
            id = TV_SHOW_ID,
            cast = emptyList()
        )

        val imageResponse = TVShowImagesResponse.ImageResponse(filePath = BACKDROP_PATH)
        val imageResponseWithNull = TVShowImagesResponse.ImageResponse(filePath = null)

        val tvShowImagesResponse = TVShowImagesResponse(
            id = TV_SHOW_ID,
            backdrops = listOf(imageResponse),
            logos = null,
            posters = null
        )

        val tvShowImagesResponseEmpty = TVShowImagesResponse(
            id = TV_SHOW_ID,
            backdrops = emptyList(),
            logos = null,
            posters = null
        )

        val tvShowImagesResponseWithNulls = TVShowImagesResponse(
            id = TV_SHOW_ID,
            backdrops = listOf(imageResponse, imageResponseWithNull),
            logos = null,
            posters = null
        )

        val tvShowResponse = TvShowResponse(
            page = PAGE,
            results = listOf(tvShowDetailsResponse),
            totalPages = TOTAL_PAGES,
            totalResults = TOTAL_RESULTS
        )

        val tvShowResponseWithNulls = TvShowResponse(
            page = PAGE,
            results = listOf(
                tvShowDetailsResponse,
                tvShowDetailsResponseWithNulls,
                tvShowDetailsResponseValidId
            ),
            totalPages = TOTAL_PAGES,
            totalResults = TOTAL_RESULTS
        )

        val tvShowResponseEmpty = TvShowResponse(
            page = PAGE,
            results = emptyList(),
            totalPages = 0,
            totalResults = 0
        )

        val episodeResponse = SeasonDetailResponse.EpisodeResponse(
            id = EPISODE_ID,
            airDate = AIR_DATE,
            episodeNumber = EPISODE_NUMBER,
            name = EPISODE_NAME,
            overview = OVERVIEW,
            runtime = RUNTIME,
            seasonNumber = SEASON_NUMBER_EPISODE,
            voteAverage = VOTE_AVERAGE
        )

        val episodeResponseWithNulls = SeasonDetailResponse.EpisodeResponse(
            id = null,
            airDate = null,
            episodeNumber = null,
            name = null,
            overview = null,
            runtime = null,
            seasonNumber = null,
            voteAverage = null
        )

        val seasonDetailResponse = SeasonDetailResponse(
            episodes = listOf(episodeResponse)
        )

        val seasonDetailResponseWithNulls = SeasonDetailResponse(
            episodes = listOf(episodeResponse, episodeResponseWithNulls)
        )

        val seasonDetailResponseEmpty = SeasonDetailResponse(episodes = emptyList())

        val authorDetails = ReviewsResponse.MovieAuthorDetails(
            name = REVIEW_AUTHOR,
            username = REVIEW_USERNAME,
            avatarPath = AVATAR_PATH,
            rating = REVIEW_RATING
        )

        val reviewResponse = ReviewsResponse.ReviewResponse(
            id = REVIEW_STRING_ID,
            author = REVIEW_AUTHOR,
            authorDetails = authorDetails,
            content = REVIEW_CONTENT,
            createdAt = REVIEW_CREATED_AT
        )

        val reviewResponseWithNulls = ReviewsResponse.ReviewResponse(
            id = null,
            author = null,
            authorDetails = null,
            content = null,
            createdAt = null
        )

        val reviewsResponse = ReviewsResponse(
            id = REVIEW_ID,
            page = PAGE,
            results = listOf(reviewResponse),
            totalPages = TOTAL_PAGES,
            totalResults = TOTAL_RESULTS
        )

        val reviewsResponseWithNulls = ReviewsResponse(
            id = REVIEW_ID,
            page = PAGE,
            results = listOf(reviewResponse, reviewResponseWithNulls),
            totalPages = TOTAL_PAGES,
            totalResults = TOTAL_RESULTS
        )

        val videoResult = TVShowVideosResponse.Result(
            id = "1L",
            key = YOUTUBE_KEY,
            name = "Official Trailer",
            official = true,
            publishedAt = "2023-01-01T12:00:00.000Z",
            site = "YouTube",
            size = 1080,
            type = "Trailer"
        )

        val videoResultNotTrailer = TVShowVideosResponse.Result(
            id = "2L",
            key = "xyz789",
            name = "Behind the Scenes",
            official = true,
            publishedAt = "2023-01-02T12:00:00.000Z",
            site = "YouTube",
            size = 1080,
            type = "Behind the Scenes"
        )

        val tvShowVideosResponse = TVShowVideosResponse(
            id = TV_SHOW_ID,
            results = listOf(videoResult)
        )

        val tvShowVideosResponseNoTrailer = TVShowVideosResponse(
            id = TV_SHOW_ID,
            results = listOf(videoResultNotTrailer)
        )

        val tvShowVideosResponseEmpty = TVShowVideosResponse(
            id = TV_SHOW_ID,
            results = emptyList()
        )

        val topRatedResult = TopRatedTvShowSearchResponse.Result(
            id = TV_SHOW_ID,
            genreIds = listOf(18L, 80L),
            adult = false,
            backdropPath = BACKDROP_PATH,
            originalLanguage = "en",
            originalTitle = "Breaking Bad",
            overview = OVERVIEW,
            popularity = POPULARITY,
            posterPath = POSTER_PATH,
            releaseDate = FIRST_AIR_DATE,
            title = TV_SHOW_NAME,
            video = false,
            voteAverage = VOTE_AVERAGE,
            voteCount = 1000
        )

        val topRatedTvShowSearchResponse = TopRatedTvShowSearchResponse(
            page = PAGE,
            results = listOf(topRatedResult),
            totalPages = TOTAL_PAGES,
            totalResults = TOTAL_RESULTS
        )

        val trendingTvShow = TrendingTvShowsResponse.TrendingTvShow(
            id = TV_SHOW_ID,
            genreIds = listOf(18L, 80L),
            adult = false,
            backdropPath = BACKDROP_PATH,
            name = TV_SHOW_NAME,
            originalName = "Breaking Bad",
            overview = OVERVIEW,
            posterPath = POSTER_PATH,
            mediaType = "tv",
            originalLanguage = "en",
            popularity = POPULARITY,
            firstAirDate = FIRST_AIR_DATE,
            voteAverage = VOTE_AVERAGE,
            voteCount = 1000,
            originCountry = listOf("US")
        )

        val trendingTvShowsResponse = TrendingTvShowsResponse(
            page = PAGE,
            results = listOf(trendingTvShow),
            totalPages = TOTAL_PAGES,
            totalResults = TOTAL_RESULTS
        )

        val ratingResponse = RatingResponse(
            isSuccess = true,
            statusCode = 1,
            statusMessage = "Success."
        )

        val ratedJsonObject = JsonObject().apply {
            add("value", JsonPrimitive(8))
        }

        val mediaAccountStatesResponse = MediaAccountStatesResponse(
            id = TV_SHOW_ID,
            rated = ratedJsonObject
        )

        val mediaAccountStatesResponseNotRated = MediaAccountStatesResponse(
            id = TV_SHOW_ID,
            rated = JsonPrimitive(false)
        )

        val popularTvShowResult = PopularTvShowsResponse.Result(
            id = TV_SHOW_ID,
            adult = false,
            backdropPath = BACKDROP_PATH,
            firstAirDate = FIRST_AIR_DATE,
            genreIds = listOf(18L, 80L),
            name = TV_SHOW_NAME,
            originCountry = listOf("US"),
            originalLanguage = "en",
            originalName = "Breaking Bad",
            overview = OVERVIEW,
            popularity = POPULARITY,
            posterPath = POSTER_PATH,
            voteAverage = VOTE_AVERAGE,
            voteCount = 1000
        )

        val popularTvShowsResponse = PopularTvShowsResponse(
            page = PAGE,
            results = listOf(popularTvShowResult),
            totalPages = TOTAL_PAGES,
            totalResults = TOTAL_RESULTS
        )

        val myRatingTvShowItem = MyRatingTvShowResponse.TvShowItem(
            id = TV_SHOW_ID,
            adult = false,
            backdropPath = BACKDROP_PATH,
            genreIds = listOf(18L, 80L),
            originCountry = listOf("US"),
            originalLanguage = "en",
            originalName = "Breaking Bad",
            overview = OVERVIEW,
            popularity = POPULARITY,
            posterPath = POSTER_PATH,
            firstAirDate = FIRST_AIR_DATE,
            name = TV_SHOW_NAME,
            voteAverage = VOTE_AVERAGE,
            voteCount = 1000,
            rating = RATING
        )

        val myRatingTvShowResponse = MyRatingTvShowResponse(
            page = PAGE,
            results = listOf(myRatingTvShowItem),
            totalPages = TOTAL_PAGES,
            totalResults = TOTAL_RESULTS
        )

        val expectedGenreDto1 = GenreDto(
            id = 18L,
            name = "Drama",
            type = GenreDto.GenreType.TV_SHOW
        )

        val expectedGenreDto2 = GenreDto(
            id = 80L,
            name = "Crime",
            type = GenreDto.GenreType.TV_SHOW
        )

        val expectedTvShowDto = TvShowDto(
            id = TV_SHOW_ID,
            title = TV_SHOW_NAME,
            genres = listOf(expectedGenreDto1, expectedGenreDto2),
            imdbRating = VOTE_AVERAGE,
            userRating = null,
            releaseDate = FIRST_AIR_DATE,
            overview = OVERVIEW,
            posterPictureURL = "https://image.tmdb.org/t/p/w500$POSTER_PATH",
            numberOfSeasons = NUMBER_OF_SEASONS,
            trailerURL = "",
            headerImagesURLs = emptyList()
        )

        val expectedTvShowDtoWithDefaults = TvShowDto(
            id = TV_SHOW_ID,
            title = "",
            genres = emptyList(),
            imdbRating = 0.0,
            userRating = null,
            releaseDate = "0001-01-01",
            overview = "",
            posterPictureURL = "",
            numberOfSeasons = 0,
            trailerURL = "",
            headerImagesURLs = emptyList()
        )

        val expectedActorDto = ActorDto(
            id = ACTOR_ID,
            name = ACTOR_NAME,
            imageUrl = "https://image.tmdb.org/t/p/w500$PROFILE_PATH",
            biography = "",
            birthdayDate = null,
            deathDate = null,
            placeOfBirth = "",
            headerPictures = emptyList(),
            department = KNOWN_FOR_DEPARTMENT
        )

        val expectedCastMemberDto = CastMemberDto(
            actor = expectedActorDto,
            characterName = CHARACTER_NAME
        )

        val expectedEpisodeDto = EpisodeDto(
            id = EPISODE_ID,
            title = EPISODE_NAME,
            episodeNumber = EPISODE_NUMBER,
            rating = VOTE_AVERAGE,
            duration = RUNTIME.toString(),
            releasedDate = AIR_DATE,
            currentSeason = SEASON_NUMBER_EPISODE,
            overview = OVERVIEW,
            headerPictures = emptyList(),
            trailerUrl = "",
            userRating = 0,
            genres = emptyList()
        )

        val expectedReviewDto = ReviewDto(
            id = REVIEW_STRING_ID,
            authorName = REVIEW_AUTHOR,
            authorAvatarUrl = "https://image.tmdb.org/t/p/w500$AVATAR_PATH",
            authorUsername = REVIEW_USERNAME,
            rating = REVIEW_RATING,
            reviewText = REVIEW_CONTENT,
            postedDate = REVIEW_CREATED_AT
        )

        val expectedMediaAccountStateDto = MediaAccountStateDto(
            id = TV_SHOW_ID,
            rated = 8
        )

        val expectedMediaAccountStateDtoNotRated = MediaAccountStateDto(
            id = TV_SHOW_ID,
            rated = null
        )
    }
}