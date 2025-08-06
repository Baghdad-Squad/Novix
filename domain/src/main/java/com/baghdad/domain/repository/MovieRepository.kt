package com.baghdad.domain.repository

import com.baghdad.domain.model.MediaAccountStates
import com.baghdad.domain.model.PagedResult
import com.baghdad.domain.model.RatedMedia
import com.baghdad.entity.media.Genre
import com.baghdad.entity.media.Movie
import com.baghdad.entity.media.Review
import com.baghdad.entity.person.CastMember

interface MovieRepository {
    suspend fun getGenres(): List<Genre>
    suspend fun getSimilarMovies(movieId: Long): List<Movie>
    suspend fun getMovieDetails(movieId: Long): Movie
    suspend fun getMovieCastMembers(movieId: Long): List<CastMember>
    suspend fun getMoviesByGenre(
        genreId: Long,
        page: Int,
        pageSize: Int
    ): PagedResult<Movie>

    suspend fun getMovieReviews(movieId: Long): List<Review>
    suspend fun getMovieImages(movieId: Long): List<String>
    suspend fun getTopRatedMovies(page: Int): PagedResult<Movie>
    suspend fun getTrendingMovies(page: Int): PagedResult<Movie>
    suspend fun getPopularMovies(): List<Movie>
    suspend fun getUpcomingMovies(genreId: Long?): List<Movie>
    suspend fun addMovieRate(movieId: Long, rating: Int)
    suspend fun getMovieStates(movieId: Long): MediaAccountStates
    suspend fun getUserRatedMovies(page: Int, pageSize: Int): PagedResult<RatedMedia>
    suspend fun deleteMovieRate(movieId: Long)
}
