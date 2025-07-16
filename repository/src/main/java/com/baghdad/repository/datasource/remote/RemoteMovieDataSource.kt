package com.baghdad.repository.datasource.remote

import com.baghdad.repository.model.CastMemberDto
import com.baghdad.repository.model.MovieDto

interface RemoteMovieDataSource {
    suspend fun getSimilarMovies(movieId: Long): List<MovieDto>
    suspend fun getMovieDetails(movieId: Long): MovieDto
    suspend fun getMovieCastMembers(movieId: Long): List<CastMemberDto>

    suspend fun getMoviesByGenre(genreId: Long, page: Int): List<MovieDto>
}
