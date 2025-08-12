package com.baghdad.remoteDataSource.apiService

import com.baghdad.remoteDataSource.interceptor.Authenticated
import com.baghdad.remoteDataSource.response.search.ActorSearchResponse
import com.baghdad.remoteDataSource.response.search.MovieSearchResponse
import com.baghdad.remoteDataSource.response.search.TvShowSearchResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface SearchApiService {

    @Authenticated
    @GET("search/movie")
    suspend fun searchMovies(
        @Query("query") query: String,
        @Query("page") page: Int

    ): Response<MovieSearchResponse>

    @Authenticated
    @GET("search/tv")
    suspend fun searchTvShows(
        @Query("query") query: String,
        @Query("page") page: Int
    ): Response<TvShowSearchResponse>

    @Authenticated
    @GET("search/person")
    suspend fun searchActors(
        @Query("query") query: String,
        @Query("page") page: Int
    ): Response<ActorSearchResponse>
}