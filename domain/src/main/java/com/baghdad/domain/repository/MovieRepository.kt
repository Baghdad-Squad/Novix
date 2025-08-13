package com.baghdad.domain.repository

import com.baghdad.domain.model.pagination.PagedResult
import com.baghdad.domain.model.userRating.RatedMedia
import com.baghdad.domain.model.savedList.SavedMovie
import com.baghdad.entity.media.Genre
import com.baghdad.entity.media.Review
import com.baghdad.entity.person.CastMember

interface MovieRepository {

    suspend fun getGenres(): List<Genre>

    suspend fun getSimilarMovies(movieId: Long): List<SavedMovie>

    suspend fun getMovieDetails(movieId: Long): SavedMovie

    suspend fun getMovieCastMembers(movieId: Long): List<CastMember>

    suspend fun getMoviesByGenre(
        genreId: Long,
        page: Int,
        pageSize: Int,
    ): PagedResult<SavedMovie>

    suspend fun getMovieReviews(movieId: Long): List<Review>

    suspend fun getMovieImages(movieId: Long): List<String>

    suspend fun getTopRatedMovies(page: Int): PagedResult<SavedMovie>

    suspend fun getTrendingMovies(page: Int): PagedResult<SavedMovie>

    suspend fun getPopularMovies(): List<SavedMovie>

    suspend fun getUpcomingMovies(genreId: Long?): List<SavedMovie>

    suspend fun addMovieRate(movieId: Long, rating: Int)

    suspend fun getMovieStates(movieId: Long): Boolean

    suspend fun getUserRatedMovies(page: Int, pageSize: Int): PagedResult<RatedMedia>

    suspend fun deleteMovieRate(movieId: Long)
}
