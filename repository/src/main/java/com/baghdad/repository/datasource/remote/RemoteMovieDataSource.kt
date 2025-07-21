package com.baghdad.repository.datasource.remote

import com.baghdad.repository.model.CastMemberDto
import com.baghdad.repository.model.MovieDto
import com.baghdad.repository.model.ReviewDto

interface RemoteMovieDataSource {
    suspend fun getSimilarMovies(movieId: Long): List<MovieDto>
    suspend fun getMovieDetails(movieId: Long): MovieDto
    suspend fun getMovieCastMembers(movieId: Long): List<CastMemberDto>
    suspend fun getMoviesByGenre(genreId: Long, page: Int): List<MovieDto>
    suspend fun getMovieReviews(movieId: Long): List<ReviewDto>
    suspend fun getMovieImages(movieId: Long): List<String>
    suspend fun getMovieTrailer(movieId: Long): String
    suspend fun getTopRatedMovies(page: Int): List<MovieDto>
}
