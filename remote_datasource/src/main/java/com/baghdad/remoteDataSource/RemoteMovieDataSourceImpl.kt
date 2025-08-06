package com.baghdad.remoteDataSource

import android.util.Log
import com.baghdad.remoteDataSource.apiService.MovieApiService
import com.baghdad.remoteDataSource.mapper.actor.toDto
import com.baghdad.remoteDataSource.mapper.movie.mapToYoutubeURL
import com.baghdad.remoteDataSource.mapper.movie.toDto
import com.baghdad.remoteDataSource.mapper.movie.toMovieDtos
import com.baghdad.remoteDataSource.mapper.movie.toPagedMovieDtos
import com.baghdad.remoteDataSource.mapper.toDto
import com.baghdad.remoteDataSource.request.RatingRequest
import com.baghdad.remoteDataSource.response.CastMembersResponse
import com.baghdad.remoteDataSource.response.MediaAccountStatesResponse
import com.baghdad.remoteDataSource.response.RatingResponse
import com.baghdad.remoteDataSource.response.ReviewsResponse
import com.baghdad.remoteDataSource.response.SimilarMovieResponse
import com.baghdad.remoteDataSource.response.movie.DiscoverMovieResponse
import com.baghdad.remoteDataSource.response.movie.MovieDetailsResponse
import com.baghdad.remoteDataSource.response.movie.MovieImageResponse
import com.baghdad.remoteDataSource.response.movie.MovieVideosResponse
import com.baghdad.remoteDataSource.response.movie.MyRatingMoviesResponse
import com.baghdad.remoteDataSource.response.movie.PopularMoviesResponse
import com.baghdad.remoteDataSource.response.movie.TrendingMovieResponse
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
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

class RemoteMovieDataSourceImpl @Inject constructor(
    private val movieApiService: MovieApiService,
    private val logger: Logger
) : RemoteMovieDataSource {
    override suspend fun getSimilarMovies(movieId: Long): List<MovieDto> {
        return handleRequest<SimilarMovieResponse>(
            apiCall = { movieApiService.getSimilarMovies(movieId) },
            logger = logger,
        ).results?.mapNotNull { it.takeIf { it.id != null }?.toDto() } ?: emptyList()
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
        ).cast?.mapNotNull { it.takeIf { it.id != null }?.toDto() } ?: emptyList()
    }

    override suspend fun getMoviesByGenre(genreId: Long, page: Int): PagedResultDto<MovieDto> {
        return handleRequest<SimilarMovieResponse>(
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
        ).results.orEmpty().mapNotNull { it.takeIf { it.id != null }?.toDto() }
    }

    override suspend fun getMovieImages(movieId: Long): List<String> {
        return handleRequest<MovieImageResponse>(
            apiCall = { movieApiService.getMovieImages(movieId) },
            logger = logger,
        ).backdrops?.map { "https://image.tmdb.org/t/p/w500" + it.filePath.orEmpty() }.orEmpty()
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
        val response = handleRequest<SimilarMovieResponse>(
            apiCall = { movieApiService.getTopRatedMovies(page) },
            logger = logger,
        )
        return response.toPagedMovieDtos()
    }


    override suspend fun getTrendingMovies(page: Int): PagedResultDto<MovieDto> {
        return handleRequest<TrendingMovieResponse>(
            apiCall = { movieApiService.getTrendingMovies(page) },
            logger = logger
        ).toMovieDtos()
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
        val response = handleRequest<DiscoverMovieResponse>(
            apiCall = {
                movieApiService.getUpcomingMovies(
                    genres = genreId?.toString() ?: "",
                    releaseDateLte = maximumReleaseDate,
                    releaseDateGte = minimumReleaseDate
                )
            },
            logger = logger,
        )
        return response.toMovieDtos()
    }

    override suspend fun getPopularMovies(): List<MovieDto> {
        val response = handleRequest<PopularMoviesResponse>(
            apiCall = {
                movieApiService.getPopularMovies()
            },
            logger = logger,
        )
        return response.toMovieDtos()
    }

    override suspend fun addMovieRate(movieId: Long, rating: Int, sessionId: String) {
        handleRequest<RatingResponse>(
            apiCall = {
                movieApiService.addMovieRate(
                    movieId = movieId,
                    rating = RatingRequest(rating),
                    sessionId = sessionId
                )
            },
            logger = logger,
        )
    }

    override suspend fun deleteMovieRate(movieId: Long, sessionId: String) {
        handleRequest<RatingResponse>(
            apiCall = {
                movieApiService.deleteMovieRate(
                    movieId = movieId,
                    sessionId = sessionId
                )
            },
            logger = logger,
        )
    }

    override suspend fun getMovieAccountStates(
        movieId: Long,
        sessionId: String
    ): MediaAccountStateDto {
        return handleRequest<MediaAccountStatesResponse>(
            apiCall = { movieApiService.getMovieAccountStates(movieId, sessionId) },
            logger = logger
        ).toDto()
    }

    override suspend fun getUserRatedMovies(
        accountId: Long,
        sessionId: String,
        page: Int
    ): PagedResultDto<MovieDto> {
        return handleRequest<MyRatingMoviesResponse>(
            apiCall = { movieApiService.getUserRatedMovies(accountId, sessionId, page) },
            logger = logger
        ).toPagedMovieDtos()
    }
}
