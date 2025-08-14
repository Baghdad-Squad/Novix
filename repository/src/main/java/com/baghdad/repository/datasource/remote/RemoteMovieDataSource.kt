package com.baghdad.repository.datasource.remote

import com.baghdad.repository.model.CastMemberDto
import com.baghdad.repository.model.MediaAccountStateDto
import com.baghdad.repository.model.MovieDto
import com.baghdad.repository.model.PagedResultDto
import com.baghdad.repository.model.ReviewDto

interface RemoteMovieDataSource {

    suspend fun getSimilarMovies(movieId: Long): List<MovieDto>

    suspend fun getMovieDetails(movieId: Long): MovieDto

    suspend fun getMovieCastMembers(movieId: Long): List<CastMemberDto>

    suspend fun getMoviesByGenre(genreId: Long, page: Int): PagedResultDto<MovieDto>

    suspend fun getMovieReviews(movieId: Long): List<ReviewDto>

    suspend fun getMovieImages(movieId: Long): List<String>

    suspend fun getMovieTrailer(movieId: Long): String

    suspend fun getTrendingMovies(page: Int): PagedResultDto<MovieDto>

    suspend fun getTopRatedMovies(page: Int): PagedResultDto<MovieDto>

    suspend fun getUpcomingMovies(genreId: Long?): List<MovieDto>

    suspend fun getPopularMovies(): List<MovieDto>

    suspend fun addMovieRate(movieId: Long, rating: Int)

    suspend fun getMovieAccountStates(movieId: Long): MediaAccountStateDto

    suspend fun deleteMovieRate(movieId: Long)

    suspend fun getUserRatedMovies(
        accountId: Long,
        page: Int
    ): PagedResultDto<MovieDto>

}
