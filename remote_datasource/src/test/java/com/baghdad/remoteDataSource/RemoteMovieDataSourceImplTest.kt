package com.baghdad.remoteDataSource

import com.baghdad.remoteDataSource.apiService.MovieApiService
import com.baghdad.remoteDataSource.response.CastMemberResponse
import com.baghdad.remoteDataSource.response.CastMembersResponse
import com.baghdad.remoteDataSource.response.MovieAuthorDetails
import com.baghdad.remoteDataSource.response.MovieResult
import com.baghdad.remoteDataSource.response.ReviewResponse
import com.baghdad.remoteDataSource.response.ReviewsResponse
import com.baghdad.remoteDataSource.response.SimilarMovieResponse
import com.baghdad.remoteDataSource.response.actor.ImageResponse
import com.baghdad.remoteDataSource.response.movie.DiscoverMovieResponse
import com.baghdad.remoteDataSource.response.movie.Genre
import com.baghdad.remoteDataSource.response.movie.MovieDetailsResponse
import com.baghdad.remoteDataSource.response.movie.MovieImageResponse
import com.baghdad.remoteDataSource.response.movie.MovieVideosResponse
import com.baghdad.remoteDataSource.response.movie.PopularMoviesResponse
import com.baghdad.remoteDataSource.response.movie.TrendingMovieResponse
import com.baghdad.repository.logger.Logger
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import retrofit2.Response


class RemoteMovieDataSourceImplTest {

    private lateinit var dataSource: RemoteMovieDataSourceImpl
    private lateinit var movieApiService: MovieApiService
    private lateinit var logger: Logger

    @BeforeEach
    fun setUp() {
        movieApiService = mockk(relaxed = true)
        logger = mockk(relaxed = true)
        dataSource = RemoteMovieDataSourceImpl(movieApiService, logger)
    }

    @Test
    fun `getMovieDetails should map response to MovieDto`() = runTest {
        // Given
        val movieId = 1L
        val response = MovieDetailsResponse(
            id = movieId,
            title = "Test Movie",
            genres = listOf(Genre(id = 1, name = "Action")),
            voteAverage = 8.5,
            releaseDate = "2023-01-01",
            overview = "Test overview",
            posterPath = "/poster.jpg",
            runtime = 120
        )
        coEvery { movieApiService.getMovieDetails(movieId) } returns Response.success(response)

        // When
        val result = dataSource.getMovieDetails(movieId)

        // Then
        assertThat(result.id).isEqualTo(movieId)
        assertThat(result.title).isEqualTo("Test Movie")
        assertThat(result.genres).hasSize(1)
        assertThat(result.imdbRating).isEqualTo(8.5)
        assertThat(result.releaseDate).isEqualTo("2023-01-01")
        assertThat(result.runtimeMinutes).isEqualTo(120)
    }

    @Test
    fun `getSimilarMovies should map response to MovieDto list`() = runTest {
        // Given
        val movieId = 1L
        val response = SimilarMovieResponse(
            results = listOf(
                MovieResult(
                    id = 1,
                    title = "Similar 1",
                    posterPath = "/similar1.jpg"
                )
            )
        )
        coEvery { movieApiService.getSimilarMovies(movieId) } returns Response.success(response)

        // When
        val result = dataSource.getSimilarMovies(movieId)

        // Then
        assertThat(result).hasSize(1)
        assertThat(result[0].id).isEqualTo(1L)
        assertThat(result[0].title).isEqualTo("Similar 1")
    }

    @Test
    fun `getMovieImages should return backdrop paths`() = runTest {
        // Given
        val movieId = 1L
        val response = MovieImageResponse(
            backdrops = listOf(
                ImageResponse(filePath = "/backdrop1.jpg")
            )
        )
        coEvery { movieApiService.getMovieImages(movieId) } returns Response.success(response)

        // When
        val result = dataSource.getMovieImages(movieId)

        // Then
        assertThat(result).containsExactly("https://image.tmdb.org/t/p/w500/backdrop1.jpg")    }

    @Test
    fun `getTopRatedMovies should return paged results`() = runTest {
        // Given
        val page = 1
        val response = SimilarMovieResponse(
            page = page,
            results = listOf(
                MovieResult(id = 1, title = "Top Movie")
            ),
            totalPages = 10
        )
        coEvery { movieApiService.getTopRatedMovies(page) } returns Response.success(response)

        // When
        val result = dataSource.getTopRatedMovies(page)

        // Then
        assertThat(result.data).hasSize(1)
    }

    @Test
    fun `getMoviesByGenre should return paged results`() = runTest {
        // Given
        val genreId = 1L
        val page = 1
        val response = SimilarMovieResponse(
            page = page,
            results = listOf(
                MovieResult(id = 1, title = "Action Movie")
            ),
            totalPages = 5
        )
        coEvery { movieApiService.getMoviesByGenre(genreId, page) } returns Response.success(
            response
        )

        // When
        val result = dataSource.getMoviesByGenre(genreId, page)

        // Then
        assertThat(result.data).hasSize(1)
    }

    @Test
    fun `getMovieCastMembers should return cast list`() = runTest {
        // Given
        val movieId = 1L
        val response = CastMembersResponse(
            cast = listOf(
                CastMemberResponse(
                    id = 1,
                    name = "Actor",
                    character = "Hero"
                )
            )
        )
        coEvery { movieApiService.getMovieCastMembers(movieId) } returns Response.success(response)

        // When
        val result = dataSource.getMovieCastMembers(movieId)

        // Then
        assertThat(result).hasSize(1)
        assertThat(result[0].actor.name).isEqualTo("Actor")
        assertThat(result[0].characterName).isEqualTo("Hero")
    }

    @Test
    fun `getPopularMovies should return mapped list of MovieDto`() = runTest {
        // Given
        val response = PopularMoviesResponse(
            results = listOf(
                PopularMoviesResponse.Result(
                    id = 1L,
                    title = "Popular Movie",
                    genreIds = listOf(28L),
                    voteAverage = 7.2,
                    posterPath = "/popular.jpg",
                    releaseDate = "2024-01-01",
                    overview = "A popular movie"
                )
            )
        )

        coEvery { movieApiService.getPopularMovies() } returns Response.success(response)

        // When
        val result = dataSource.getPopularMovies()

        // Then
        assertThat(result).hasSize(1)
        val movie = result[0]
        assertThat(movie.id).isEqualTo(1L)
        assertThat(movie.title).isEqualTo("Popular Movie")
        assertThat(movie.genres).hasSize(1)
        assertThat(movie.imdbRating).isEqualTo(7.2)
        assertThat(movie.releaseDate).isEqualTo("2024-01-01")
        assertThat(movie.posterPictureURL).contains("/popular.jpg")
    }

    @Test
    fun `getMovieReviews should return mapped list of ReviewDto`() = runTest {
        // Given
        val response = ReviewsResponse(
            id = 1,
            page = 1,
            results = listOf(
                ReviewResponse(
                    id = "review_1",
                    author = "Critic",
                    authorDetails = MovieAuthorDetails(
                        name = "Critic",
                        username = "critic123",
                        avatarPath = "/avatar.png",
                        rating = 4.0
                    ),
                    content = "Great movie!",
                    createdAt = "2024-01-01T10:00:00Z"
                )
            ),
            totalPages = 1,
            totalResults = 1
        )

        coEvery { movieApiService.getMovieReviews(movieId = 1L) } returns Response.success(response)

        // When
        val result = dataSource.getMovieReviews(1L)

        // Then
        assertThat(result).hasSize(1)
        val review = result[0]
        assertThat(review.id).isEqualTo("review_1")
        assertThat(review.authorName).isEqualTo("Critic")
        assertThat(review.authorAvatarUrl).contains("/avatar.png")
        assertThat(review.rating).isEqualTo(4.0)
        assertThat(review.reviewText).isEqualTo("Great movie!")
        assertThat(review.postedDate).isEqualTo("2024-01-01T10:00:00Z")
    }


    @Test
    fun `getMovieTrailer should return youtube url`() = runTest {
        // Given
        val movieId = 1L
        val videoKey = "abc123"
        val response = MovieVideosResponse(
            results = listOf(
                MovieVideosResponse.Result(
                    key = videoKey,
                    site = "YouTube",
                    type = "Trailer"
                )
            )
        )
        coEvery { movieApiService.getMovieTrailer(movieId) } returns Response.success(response)

        // When
        val result = dataSource.getMovieTrailer(movieId)

        // Then
        assertThat(result).contains(videoKey)
    }

    @Test
    fun `getTrendingMovies should return paged result`() = runTest {
        // Given
        val page = 1
        val response = TrendingMovieResponse(
            results = listOf(
                TrendingMovieResponse.Result(
                    id = 1,
                    title = "Trending",
                    posterPath = "/trend.jpg"
                )
            )
        )
        coEvery { movieApiService.getTrendingMovies(page) } returns Response.success(response)

        // When
        val result = dataSource.getTrendingMovies(page)

        // Then
        assertThat(result.data).hasSize(1)
        assertThat(result.data[0].title).isEqualTo("Trending")
    }

    @Test
    fun `getUpcomingMovies should return paged result`() = runTest {
        // Given
        val genreId: Long = 28
        val response = DiscoverMovieResponse(
            results = listOf(
                DiscoverMovieResponse.Result(
                    id = 1,
                    title = "Upcoming Movie",
                    releaseDate = "2025-08-10",
                    genreIds = listOf(28)
                )
            )
        )
        coEvery {
            movieApiService.getUpcomingMovies(
                any(),
                any(),
                any(),
                any(),
                any(),
                any(),
                any(),
                any(),
            )
        } returns Response.success(response)

        // When
        val result = dataSource.getUpcomingMovies(genreId)

        // Then
        assertThat(result).hasSize(1)
        assertThat(result[0].id).isEqualTo(1L)

    }
}