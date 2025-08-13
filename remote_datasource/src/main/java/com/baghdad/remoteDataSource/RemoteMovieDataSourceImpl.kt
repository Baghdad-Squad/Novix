package com.baghdad.remoteDataSource

import com.baghdad.remoteDataSource.apiService.MovieApiService
import com.baghdad.remoteDataSource.mapper.castMembers.toCastMembers
import com.baghdad.remoteDataSource.mapper.mediaAccountStates.toDto
import com.baghdad.remoteDataSource.mapper.movie.mapToYoutubeURL
import com.baghdad.remoteDataSource.mapper.movie.toDto
import com.baghdad.remoteDataSource.mapper.movie.toImageUrl
import com.baghdad.remoteDataSource.mapper.movie.toMovieDtos
import com.baghdad.remoteDataSource.mapper.movie.toPagedMovieDtos
import com.baghdad.remoteDataSource.mapper.review.toReviewDto
import com.baghdad.remoteDataSource.request.RatingRequest
import com.baghdad.remoteDataSource.response.castMembers.CastMembersResponse
import com.baghdad.remoteDataSource.response.mediaAccount.MediaAccountStatesResponse
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
import com.baghdad.remoteDataSource.util.handleRequest
import com.baghdad.repository.datasource.remote.RemoteMovieDataSource
import com.baghdad.repository.logger.Logger
import com.baghdad.repository.model.CastMemberDto
import com.baghdad.repository.model.MediaAccountStateDto
import com.baghdad.repository.model.MovieDto
import com.baghdad.repository.model.PagedResultDto
import com.baghdad.repository.model.ReviewDto
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@Singleton
class RemoteMovieDataSourceImpl @Inject constructor(
    private val movieApiService: MovieApiService,
    private val logger: Logger
) : RemoteMovieDataSource {
    override suspend fun getSimilarMovies(movieId: Long): List<MovieDto> {
        return handleRequest<SimilarMovieResponse>(
            apiCall = { movieApiService.getSimilarMovies(movieId) },
            logger = logger,
        ).toMovieDtos()
    }

    override suspend fun getMovieDetails(movieId: Long): MovieDto {
        return handleRequest<MovieDetailsResponse>(
            apiCall = { movieApiService.getMovieDetails(movieId) },
            logger = logger,
        ).toDto()
    }

    override suspend fun getMovieCastMembers(movieId: Long): List<CastMemberDto> {
        return handleRequest<CastMembersResponse>(
            apiCall = { movieApiService.getMovieCastMembers(movieId) },
            logger = logger,
        ).toCastMembers()
    }

    override suspend fun getMoviesByGenre(genreId: Long, page: Int): PagedResultDto<MovieDto> {
        return handleRequest<DiscoverMovieResponse>(
            apiCall = {
                movieApiService.getMoviesByGenre(
                    genreId = genreId,
                    page = page
                )
            },
            logger = logger,
        ).toPagedMovieDtos()
    }

    override suspend fun getMovieReviews(movieId: Long): List<ReviewDto> {
        return handleRequest<ReviewsResponse>(
            apiCall = {
                movieApiService.getMovieReviews(
                    movieId = movieId
                )
            },
            logger = logger,
        ).toReviewDto()
    }

    override suspend fun getMovieImages(movieId: Long): List<String> {
        return handleRequest<MovieImageResponse>(
            apiCall = { movieApiService.getMovieImages(movieId) },
            logger = logger,
        ).toImageUrl()
    }

    override suspend fun getMovieTrailer(movieId: Long): String {
        return handleRequest<MovieVideosResponse>(
            apiCall = { movieApiService.getMovieTrailer(movieId) },
            logger = logger,
        ).mapToYoutubeURL()
    }

    override suspend fun getTopRatedMovies(
        page: Int,
    ): PagedResultDto<MovieDto> {
        return handleRequest<DiscoverMovieResponse>(
                apiCall = {
                movieApiService.getTopRatedMovies(
                    page = page,
                    sortBy = "vote_average.desc",
                    minVoteCount = 200,
                )
            },
            logger = logger,
        ).toPagedMovieDtos()
    }


    override suspend fun getTrendingMovies(page: Int): PagedResultDto<MovieDto> {
        return handleRequest<TrendingMovieResponse>(
            apiCall = { movieApiService.getTrendingMovies(page) },
            logger = logger,
        ).toPagedMovieDtos()
    }

    @OptIn(ExperimentalTime::class)
    override suspend fun getUpcomingMovies(genreId: Long?): List<MovieDto> {
        val today: LocalDate = Clock.System.now().toLocalDateTime(TimeZone.UTC).date
        val thirtyDaysLater: LocalDate = today.plus(
            value = 30,
            unit = DateTimeUnit.DAY,
        )

        val minimumReleaseDate = today.toString()
        val maximumReleaseDate = thirtyDaysLater.toString()
        return handleRequest<DiscoverMovieResponse>(
            apiCall = {
                movieApiService.getUpcomingMovies(
                    genres = genreId?.toString() ?: "",
                    releaseDateLte = maximumReleaseDate,
                    releaseDateGte = minimumReleaseDate
                )
            },
            logger = logger,
        ).toMovieDtos()
    }

    override suspend fun getPopularMovies(): List<MovieDto> {
        return handleRequest<PopularMoviesResponse>(
            apiCall = {
                movieApiService.getPopularMovies()
            },
            logger = logger,
        ).toMovieDtos()
    }

    override suspend fun addMovieRate(movieId: Long, rating: Int) {
        handleRequest<RatingResponse>(
            apiCall = {
                movieApiService.addMovieRate(
                    movieId = movieId,
                    rating = RatingRequest(rating),
                )
            },
            logger = logger,
        )
    }

    override suspend fun deleteMovieRate(movieId: Long) {
        handleRequest<RatingResponse>(
            apiCall = {
                movieApiService.deleteMovieRate(
                    movieId = movieId,
                )
            },
            logger = logger,
        )
    }

    override suspend fun getMovieAccountStates(
        movieId: Long,
    ): MediaAccountStateDto {
        return handleRequest<MediaAccountStatesResponse>(
            apiCall = { movieApiService.getMovieAccountStates(movieId) },
            logger = logger
        ).toDto()
    }

    override suspend fun getUserRatedMovies(
        accountId: Long,
        page: Int
    ): PagedResultDto<MovieDto> {
        return handleRequest<MyRatingMoviesResponse>(
            apiCall = { movieApiService.getUserRatedMovies(accountId, page) },
            logger = logger
        ).toPagedMovieDtos()
    }
}
