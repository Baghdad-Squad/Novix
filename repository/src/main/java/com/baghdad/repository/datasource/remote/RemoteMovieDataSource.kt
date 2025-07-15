package com.baghdad.repository.datasource.remote

import com.baghdad.repository.model.CastMemberDto
import com.baghdad.repository.model.MovieDto

interface RemoteMovieDataSource {
    suspend fun getSimilarMovies(movieId: Int): List<MovieDto>
    suspend fun getMovieDetails(movieId: Int): MovieDto
    suspend fun getMovieCredits(movieId: Int): List<CastMemberDto>
}
