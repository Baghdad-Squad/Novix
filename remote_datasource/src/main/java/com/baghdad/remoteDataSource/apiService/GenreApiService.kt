package com.baghdad.remoteDataSource.apiService

import com.baghdad.remoteDataSource.interceptor.Authenticated
import com.baghdad.remoteDataSource.interceptor.Cacheable
import com.baghdad.remoteDataSource.response.GenreListResponse
import retrofit2.Response
import retrofit2.http.GET

interface GenreApiService {
    @Cacheable
    @Authenticated
    @GET("genre/movie/list")
    suspend fun getMovieGenre(): Response<GenreListResponse>

    @Cacheable
    @Authenticated
    @GET("genre/tv/list")
    suspend fun getTvShowGenre(): Response<GenreListResponse>

}