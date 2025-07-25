package com.baghdad.remoteDataSource

import com.baghdad.remoteDataSource.apiService.MovieApiService
import com.baghdad.remoteDataSource.mapper.actor.toDto
import com.baghdad.remoteDataSource.mapper.movie.mapToYoutubeURL
import com.baghdad.remoteDataSource.mapper.movie.toDto
import com.baghdad.remoteDataSource.mapper.movie.toPagedMovieDtos
import com.baghdad.remoteDataSource.mapper.movie.toMovieDtos
import com.baghdad.remoteDataSource.mapper.toDto
import com.baghdad.remoteDataSource.response.CastMembersResponse
import com.baghdad.remoteDataSource.response.ReviewsResponse
import com.baghdad.remoteDataSource.response.SimilarMovieResponse
import com.baghdad.remoteDataSource.response.movie.MovieDetailsResponse
import com.baghdad.remoteDataSource.response.movie.MovieImageResponse
import com.baghdad.remoteDataSource.response.movie.MovieVideosResponse
import com.baghdad.remoteDataSource.response.movie.TrendingMovieResponse
import com.baghdad.remoteDataSource.util.handleRequest
import com.baghdad.repository.datasource.remote.RemoteMovieDataSource
import com.baghdad.repository.logger.Logger
import com.baghdad.repository.model.CastMemberDto
import com.baghdad.repository.model.MovieDto
import com.baghdad.repository.model.PagedResultDto
import com.baghdad.repository.model.ReviewDto

class RemoteMovieDataSourceImpl(
    private val movieApiService: MovieApiService,
    private val logger: Logger
) : RemoteMovieDataSource {
    override suspend fun getSimilarMovies(movieId: Long): List<MovieDto> {
        return handleRequest<SimilarMovieResponse>(
            apiCall = { movieApiService.getSimilarMovies(movieId) },
            logger = logger,
        ).results?.map { it.toDto() } ?: emptyList()
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
        ).cast?.map { it.toDto() } ?: emptyList()
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
        ).results.orEmpty().map { it.toDto() }
    }

    override suspend fun getMovieImages(movieId: Long): List<String> {
        return handleRequest<MovieImageResponse>(
            apiCall = { movieApiService.getMovieImages(movieId) },
            logger = logger,
        ).backdrops?.map { it.filePath.orEmpty() }.orEmpty()
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
}
