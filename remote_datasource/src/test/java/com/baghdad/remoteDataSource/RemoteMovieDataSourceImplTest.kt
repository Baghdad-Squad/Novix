package com.baghdad.remoteDataSource

import com.baghdad.remoteDataSource.apiService.MovieApiService
import com.baghdad.remoteDataSource.request.RatingRequest
import com.baghdad.remoteDataSource.response.castMembers.CastMembersResponse
import com.baghdad.remoteDataSource.response.movie.DiscoverMovieResponse
import com.baghdad.remoteDataSource.response.movie.MovieDetailsResponse
import com.baghdad.remoteDataSource.response.movie.MovieImageResponse
import com.baghdad.remoteDataSource.response.movie.MovieVideosResponse
import com.baghdad.remoteDataSource.response.movie.MyRatingMoviesResponse
import com.baghdad.remoteDataSource.response.movie.PopularMoviesResponse
import com.baghdad.remoteDataSource.response.movie.SimilarMovieResponse
import com.baghdad.remoteDataSource.response.movie.TrendingMovieResponse
import com.baghdad.remoteDataSource.response.rate.RatingResponse
import com.baghdad.remoteDataSource.response.reviews.ReviewsResponse
import com.baghdad.repository.logger.Logger
import com.baghdad.repository.model.ActorDto
import com.baghdad.repository.model.CastMemberDto
import com.baghdad.repository.model.GenreDto
import com.baghdad.repository.model.MovieDto
import com.baghdad.repository.model.ReviewDto
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import retrofit2.Response

class RemoteMovieDataSourceImplTest {
    private val movieApiService = mockk<MovieApiService>()
    private val logger = mockk<Logger>(relaxed = true)
    private val dataSource = RemoteMovieDataSourceImpl(movieApiService, logger)

    @Test
    fun `should return similar movies when getSimilarMovies is called with valid movie id`() = runTest {
        val successResponse = Response.success(similarMovieResponse)
        coEvery { movieApiService.getSimilarMovies(MOVIE_ID) } returns successResponse

        val result = dataSource.getSimilarMovies(MOVIE_ID)

        assertThat(result).hasSize(1)
        assertThat(result[0].id).isEqualTo(MOVIE_ID)
        assertThat(result[0].title).isEqualTo(MOVIE_TITLE)
        coVerify(exactly = 1) { movieApiService.getSimilarMovies(MOVIE_ID) }
    }

    @Test
    fun `should filter out movies with null id when getSimilarMovies is called`() = runTest {
        val successResponse = Response.success(similarMovieResponseWithNullId)
        coEvery { movieApiService.getSimilarMovies(MOVIE_ID) } returns successResponse

        val result = dataSource.getSimilarMovies(MOVIE_ID)

        assertThat(result).hasSize(1)
        assertThat(result[0].id).isEqualTo(MOVIE_ID)
    }

    @Test
    fun `should return movie details when getMovieDetails is called with valid movie id`() = runTest {
        val successResponse = Response.success(movieDetailsResponse)
        coEvery { movieApiService.getMovieDetails(MOVIE_ID) } returns successResponse

        val result = dataSource.getMovieDetails(MOVIE_ID)

        assertThat(result).isEqualTo(expectedMovieDto)
        coVerify(exactly = 1) { movieApiService.getMovieDetails(MOVIE_ID) }
    }

    @Test
    fun `should return cast members when getMovieCastMembers is called with valid movie id`() = runTest {
        val successResponse = Response.success(castMembersResponse)
        coEvery { movieApiService.getMovieCastMembers(MOVIE_ID) } returns successResponse

        val result = dataSource.getMovieCastMembers(MOVIE_ID)

        assertThat(result).hasSize(1)
        assertThat(result[0]).isEqualTo(expectedCastMemberDto)
        coVerify(exactly = 1) { movieApiService.getMovieCastMembers(MOVIE_ID) }
    }

    @Test
    fun `should filter out cast members with null id when getMovieCastMembers is called`() = runTest {
        val successResponse = Response.success(castMembersResponseWithNullId)
        coEvery { movieApiService.getMovieCastMembers(MOVIE_ID) } returns successResponse

        val result = dataSource.getMovieCastMembers(MOVIE_ID)

        assertThat(result).hasSize(1)
        assertThat(result[0].actor.id).isEqualTo(ACTOR_ID)
    }

    @Test
    fun `should return paged movies when getMoviesByGenre is called`() = runTest {
        val successResponse = Response.success(discoverMovieResponse)
        coEvery { movieApiService.getMoviesByGenre(GENRE_ID, PAGE) } returns successResponse

        val result = dataSource.getMoviesByGenre(GENRE_ID, PAGE)

        assertThat(result.data).hasSize(1)
        assertThat(result.data[0].id).isEqualTo(MOVIE_ID)
        coVerify(exactly = 1) { movieApiService.getMoviesByGenre(GENRE_ID, PAGE) }
    }

    @Test
    fun `should filter out movies with null id when getMoviesByGenre is called`() = runTest {
        val successResponse = Response.success(discoverMovieResponseWithNullId)
        coEvery { movieApiService.getMoviesByGenre(GENRE_ID, PAGE) } returns successResponse

        val result = dataSource.getMoviesByGenre(GENRE_ID, PAGE)

        assertThat(result.data).hasSize(1)
        assertThat(result.data[0].id).isEqualTo(MOVIE_ID)
    }

    @Test
    fun `should return reviews when getMovieReviews is called with valid movie id`() = runTest {
        val successResponse = Response.success(reviewsResponse)
        coEvery { movieApiService.getMovieReviews(MOVIE_ID) } returns successResponse

        val result = dataSource.getMovieReviews(MOVIE_ID)

        assertThat(result).hasSize(1)
        assertThat(result[0]).isEqualTo(expectedReviewDto)
        coVerify(exactly = 1) { movieApiService.getMovieReviews(MOVIE_ID) }
    }

    @Test
    fun `should filter out reviews with null id when getMovieReviews is called`() = runTest {
        val successResponse = Response.success(reviewsResponseWithNullId)
        coEvery { movieApiService.getMovieReviews(MOVIE_ID) } returns successResponse

        val result = dataSource.getMovieReviews(MOVIE_ID)

        assertThat(result).hasSize(1)
        assertThat(result[0].id).isEqualTo(REVIEW_STRING_ID)
    }

    @Test
    fun `should return image urls when getMovieImages is called with valid movie id`() = runTest {
        val successResponse = Response.success(movieImageResponse)
        coEvery { movieApiService.getMovieImages(MOVIE_ID) } returns successResponse

        val result = dataSource.getMovieImages(MOVIE_ID)

        assertThat(result).containsExactly("https://image.tmdb.org/t/p/w500$IMAGE_FILE_PATH")
        coVerify(exactly = 1) { movieApiService.getMovieImages(MOVIE_ID) }
    }

    @Test
    fun `should return empty list when getMovieImages receives null backdrops`() = runTest {
        val successResponse = Response.success(movieImageResponseWithNullBackdrops)
        coEvery { movieApiService.getMovieImages(MOVIE_ID) } returns successResponse

        val result = dataSource.getMovieImages(MOVIE_ID)

        assertThat(result).isEmpty()
    }

    @Test
    fun `should return trailer url when getMovieTrailer is called with valid movie id`() = runTest {
        val successResponse = Response.success(movieVideosResponse)
        coEvery { movieApiService.getMovieTrailer(MOVIE_ID) } returns successResponse

        val result = dataSource.getMovieTrailer(MOVIE_ID)

        assertThat(result).isNotEmpty()
        coVerify(exactly = 1) { movieApiService.getMovieTrailer(MOVIE_ID) }
    }

    @Test
    fun `should return paged movies when getTopRatedMovies is called`() = runTest {
        val successResponse = Response.success(discoverMovieResponse)
        coEvery {
            movieApiService.getTopRatedMovies(PAGE, SORT_BY, MIN_VOTE_COUNT)
        } returns successResponse

        val result = dataSource.getTopRatedMovies(PAGE)

        assertThat(result.data).hasSize(1)
        assertThat(result.data[0].id).isEqualTo(MOVIE_ID)
        coVerify(exactly = 1) { movieApiService.getTopRatedMovies(PAGE, SORT_BY, MIN_VOTE_COUNT) }
    }

    @Test
    fun `should return paged movies when getTrendingMovies is called`() = runTest {
        val successResponse = Response.success(trendingMovieResponse)
        coEvery { movieApiService.getTrendingMovies(PAGE) } returns successResponse

        val result = dataSource.getTrendingMovies(PAGE)

        assertThat(result.data).hasSize(1)
        assertThat(result.data[0].id).isEqualTo(MOVIE_ID)
        coVerify(exactly = 1) { movieApiService.getTrendingMovies(PAGE) }
    }

    @Test
    fun `should return upcoming movies when getUpcomingMovies is called with genre id`() = runTest {
        val successResponse = Response.success(discoverMovieResponse)

        coEvery {
            movieApiService.getUpcomingMovies(
                genres = GENRE_ID.toString(),
                includeAdult = false,
                includeVideo = false,
                sortBy = "popularity.desc",
                releaseType = "2|3",
                releaseDateGte = any(),
                releaseDateLte = any()
            )
        } returns successResponse

        val result = dataSource.getUpcomingMovies(GENRE_ID)

        assertThat(result).hasSize(1)
        assertThat(result[0].id).isEqualTo(MOVIE_ID)
        coVerify(exactly = 1) {
            movieApiService.getUpcomingMovies(
                genres = GENRE_ID.toString(),
                includeAdult = false,
                includeVideo = false,
                sortBy = "popularity.desc",
                releaseType = "2|3",
                releaseDateGte = any(),
                releaseDateLte = any()
            )
        }
    }

    @Test
    fun `should return popular movies when getPopularMovies is called`() = runTest {
        val successResponse = Response.success(popularMoviesResponse)
        coEvery { movieApiService.getPopularMovies() } returns successResponse

        val result = dataSource.getPopularMovies()

        assertThat(result).hasSize(1)
        assertThat(result[0].id).isEqualTo(MOVIE_ID)
        coVerify(exactly = 1) { movieApiService.getPopularMovies() }
    }

    @Test
    fun `should call api service when addMovieRate is called`() = runTest {
        val successResponse = Response.success(ratingResponse)
        coEvery { movieApiService.addMovieRate(MOVIE_ID, ratingRequest) } returns successResponse

        dataSource.addMovieRate(MOVIE_ID, RATING)

        coVerify(exactly = 1) { movieApiService.addMovieRate(MOVIE_ID, ratingRequest) }
    }


    @Test
    fun `should call api service when deleteMovieRate is called`() = runTest {
        val successResponse = Response.success(ratingResponse)
        coEvery { movieApiService.deleteMovieRate(MOVIE_ID) } returns successResponse

        dataSource.deleteMovieRate(MOVIE_ID)

        coVerify(exactly = 1) { movieApiService.deleteMovieRate(MOVIE_ID) }
    }

    @Test
    fun `should return user rated movies when getUserRatedMovies is called`() = runTest {
        val successResponse = Response.success(myRatingMoviesResponse)
        coEvery { movieApiService.getUserRatedMovies(ACCOUNT_ID, PAGE) } returns successResponse

        val result = dataSource.getUserRatedMovies(ACCOUNT_ID, PAGE)

        assertThat(result.data).hasSize(1)
        assertThat(result.data[0].id).isEqualTo(MOVIE_ID)
        coVerify(exactly = 1) { movieApiService.getUserRatedMovies(ACCOUNT_ID, PAGE) }
    }

    companion object {
        const val MOVIE_ID = 123L
        const val GENRE_ID = 28L
        const val PAGE = 1
        const val RATING = 8
        const val ACCOUNT_ID = 456L
        const val MOVIE_TITLE = "Test Movie"
        const val OVERVIEW = "Movie overview"
        const val RELEASE_DATE = "2023-01-01"
        const val POSTER_PATH = "/poster.jpg"
        const val BACKDROP_PATH = "/backdrop.jpg"
        const val VOTE_AVERAGE = 7.5
        const val RUNTIME = 120
        const val POPULARITY = 8.9
        const val ACTOR_ID = 789L
        const val ACTOR_NAME = "Actor Name"
        const val CHARACTER_NAME = "Character Name"
        const val PROFILE_PATH = "/profile.jpg"
        const val KNOWN_FOR_DEPARTMENT = "Acting"
        const val REVIEW_ID = 101L

        const val REVIEW_STRING_ID = "101L"
        const val AUTHOR_NAME = "Reviewer"
        const val AUTHOR_USERNAME = "reviewer123"
        const val REVIEW_CONTENT = "Great movie!"
        const val CREATED_AT = "2023-01-15"
        const val AUTHOR_RATING = 9.0
        const val AVATAR_PATH = "/avatar.jpg"
        const val IMAGE_FILE_PATH = "/image.jpg"
        const val VIDEO_KEY = "abc123"
        const val TOTAL_PAGES = 5
        const val MIN_VOTE_COUNT = 200
        const val SORT_BY = "vote_average.desc"

        val movieGenre = MovieDetailsResponse.Genre(
            id = GENRE_ID,
            name = "Action"
        )

        val movieGenreWithNulls = MovieDetailsResponse.Genre(
            id = null,
            name = null
        )

        val movieGenreWithBlankName = MovieDetailsResponse.Genre(
            id = GENRE_ID,
            name = ""
        )

        val similarMovieResult = SimilarMovieResponse.MovieResult(
            id = MOVIE_ID,
            title = MOVIE_TITLE,
            genreIds = listOf(GENRE_ID),
            voteAverage = VOTE_AVERAGE,
            releaseDate = RELEASE_DATE,
            overview = OVERVIEW,
            posterPath = POSTER_PATH
        )

        val similarMovieResultWithNulls = SimilarMovieResponse.MovieResult(
            id = null,
            title = null,
            genreIds = null,
            voteAverage = null,
            releaseDate = null,
            overview = null,
            posterPath = null
        )

        val similarMovieResponse = SimilarMovieResponse(
            page = PAGE,
            results = listOf(similarMovieResult),
            totalPages = TOTAL_PAGES
        )

        val similarMovieResponseWithNullId = SimilarMovieResponse(
            page = PAGE,
            results = listOf(similarMovieResult, similarMovieResultWithNulls),
            totalPages = TOTAL_PAGES
        )

        val movieDetailsResponse = MovieDetailsResponse(
            id = MOVIE_ID,
            title = MOVIE_TITLE,
            genres = listOf(movieGenre),
            voteAverage = VOTE_AVERAGE,
            releaseDate = RELEASE_DATE,
            overview = OVERVIEW,
            posterPath = POSTER_PATH,
            runtime = RUNTIME
        )

        val castMemberResponse = CastMembersResponse.CastMemberResponse(
            id = ACTOR_ID,
            name = ACTOR_NAME,
            profilePath = PROFILE_PATH,
            character = CHARACTER_NAME,
            knownForDepartment = KNOWN_FOR_DEPARTMENT
        )

        val castMemberResponseWithNulls = CastMembersResponse.CastMemberResponse(
            id = null,
            name = null,
            profilePath = null,
            character = null,
            knownForDepartment = null
        )

        val castMembersResponse = CastMembersResponse(
            cast = listOf(castMemberResponse)
        )

        val castMembersResponseWithNullId = CastMembersResponse(
            cast = listOf(castMemberResponse, castMemberResponseWithNulls)
        )

        val discoverMovieResult = DiscoverMovieResponse.Result(
            id = MOVIE_ID,
            title = MOVIE_TITLE,
            genreIds = listOf(GENRE_ID),
            voteAverage = VOTE_AVERAGE,
            releaseDate = RELEASE_DATE,
            overview = OVERVIEW,
            posterPath = POSTER_PATH
        )

        val discoverMovieResultWithNulls = DiscoverMovieResponse.Result(
            id = null,
            title = null,
            genreIds = listOf(null),
            voteAverage = null,
            releaseDate = "",
            overview = null,
            posterPath = null
        )

        val discoverMovieResponse = DiscoverMovieResponse(
            page = PAGE,
            results = listOf(discoverMovieResult),
            totalPages = TOTAL_PAGES
        )

        val discoverMovieResponseWithNullId = DiscoverMovieResponse(
            page = PAGE,
            results = listOf(discoverMovieResult, discoverMovieResultWithNulls),
            totalPages = TOTAL_PAGES
        )

        val reviewAuthorDetails = ReviewsResponse.MovieAuthorDetails(
            name = AUTHOR_NAME,
            username = AUTHOR_USERNAME,
            avatarPath = AVATAR_PATH,
            rating = AUTHOR_RATING
        )

        val reviewResponse = ReviewsResponse.ReviewResponse(
            id = REVIEW_STRING_ID,
            authorDetails = reviewAuthorDetails,
            content = REVIEW_CONTENT,
            createdAt = CREATED_AT
        )

        val reviewResponseWithNulls = ReviewsResponse.ReviewResponse(
            id = null,
            authorDetails = null,
            content = null,
            createdAt = null
        )

        val reviewsResponse = ReviewsResponse(
            results = listOf(reviewResponse)
        )

        val reviewsResponseWithNullId = ReviewsResponse(
            results = listOf(reviewResponse, reviewResponseWithNulls)
        )

        val movieImageBackdrop = MovieImageResponse.ImageResponse(
            filePath = IMAGE_FILE_PATH
        )

        val movieImageResponse = MovieImageResponse(
            backdrops = listOf(movieImageBackdrop)
        )

        val movieImageResponseWithNullBackdrops = MovieImageResponse(
            backdrops = null
        )

        val movieVideoResult = MovieVideosResponse.Result(
            key = VIDEO_KEY,
            site = "YouTube",
            type = "Trailer"
        )

        val movieVideosResponse = MovieVideosResponse(
            results = listOf(movieVideoResult)
        )

        val trendingMovieResult = TrendingMovieResponse.Result(
            id = MOVIE_ID,
            title = MOVIE_TITLE,
            posterPath = POSTER_PATH,
            backdropPath = BACKDROP_PATH,
            overview = OVERVIEW,
            releaseDate = RELEASE_DATE,
            voteAverage = VOTE_AVERAGE,
            popularity = POPULARITY,
            genreIds = listOf(GENRE_ID.toLong())
        )

        val trendingMovieResponse = TrendingMovieResponse(
            page = PAGE,
            results = listOf(trendingMovieResult),
            totalPages = TOTAL_PAGES
        )
        val popularMovieResult = PopularMoviesResponse.Result(
            id = MOVIE_ID,
            title = MOVIE_TITLE,
            genreIds = listOf(GENRE_ID),
            voteAverage = VOTE_AVERAGE,
            releaseDate = RELEASE_DATE,
            overview = OVERVIEW,
            posterPath = POSTER_PATH,
            backdropPath = BACKDROP_PATH,
            popularity = POPULARITY,
            adult = false,
            originalLanguage = "en",
            originalTitle = MOVIE_TITLE,
            video = false,
            voteCount = 1000
        )

        val popularMoviesResponse = PopularMoviesResponse(
            page = PAGE,
            results = listOf(popularMovieResult),
            totalPages = TOTAL_PAGES,
            totalResults = 1000
        )

        val ratingRequest = RatingRequest(RATING)

        val ratingResponse = RatingResponse(
            isSuccess = true,
            statusCode = 200,
            statusMessage = "Success"
        )

        val myRatingMovieItem = MyRatingMoviesResponse.MovieItem(
            id = MOVIE_ID,
            title = MOVIE_TITLE,
            genreIds = listOf(GENRE_ID),
            voteAverage = VOTE_AVERAGE,
            releaseDate = RELEASE_DATE,
            overview = OVERVIEW,
            posterPath = POSTER_PATH,
            backdropPath = BACKDROP_PATH,
            popularity = POPULARITY,
            adult = false,
            originalLanguage = "en",
            originalTitle = MOVIE_TITLE,
            video = false,
            voteCount = 1000,
            rating = RATING
        )

        val myRatingMoviesResponse = MyRatingMoviesResponse(
            page = PAGE,
            results = listOf(myRatingMovieItem),
            totalPages = TOTAL_PAGES,
            totalResults = 1000
        )

        val expectedMovieDto = MovieDto(
            id = MOVIE_ID,
            title = MOVIE_TITLE,
            genres = listOf(GenreDto(GENRE_ID, "Action", GenreDto.GenreType.MOVIE)),
            imdbRating = VOTE_AVERAGE,
            userRating = null,
            releaseDate = RELEASE_DATE,
            overview = OVERVIEW,
            posterPictureURL = "https://image.tmdb.org/t/p/w500$POSTER_PATH",
            runtimeMinutes = RUNTIME,
            trailerURL = ""
        )

        val expectedCastMemberDto = CastMemberDto(
            actor = ActorDto(
                id = ACTOR_ID,
                name = ACTOR_NAME,
                imageUrl = "https://image.tmdb.org/t/p/w500$PROFILE_PATH",
                biography = "",
                birthdayDate = null,
                deathDate = null,
                placeOfBirth = "",
                headerPictures = emptyList(),
                department = KNOWN_FOR_DEPARTMENT
            ),
            characterName = CHARACTER_NAME
        )

        val expectedReviewDto = ReviewDto(
            id = REVIEW_STRING_ID,
            authorName = AUTHOR_NAME,
            authorAvatarUrl = "https://image.tmdb.org/t/p/w500$AVATAR_PATH",
            contentTitle = AUTHOR_USERNAME,
            rating = AUTHOR_RATING,
            reviewText = REVIEW_CONTENT,
            postedDate = CREATED_AT
        )
    }
}