package com.baghdad.domain.repository

import com.baghdad.entity.media.Genre
import com.baghdad.entity.media.Movie
import com.baghdad.entity.media.Review
import com.baghdad.entity.person.CastMember

interface MovieRepository {
    suspend fun getGenres() : List<Genre>
    suspend fun getSimilarMovies(movieId: Long): List<Movie>
    suspend fun getMovieDetails(movieId: Long): Movie
    suspend fun getMovieCastMembers(movieId: Long): List<CastMember>
    suspend fun getMoviesByGenre(genreId: Long, page: Int): List<Movie>
    suspend fun getMovieReviews(movieId: Long): List<Review>
    suspend fun getMovieImages(movieId: Long): List<String>
    suspend fun getTrendingMovies(page: Int): List<Movie>
}