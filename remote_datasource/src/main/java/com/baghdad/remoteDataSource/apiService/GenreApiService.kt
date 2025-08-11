package com.baghdad.remoteDataSource.apiService

import com.baghdad.remoteDataSource.interceptor.Authenticated
import com.baghdad.remoteDataSource.response.genre.GenreListResponse
import retrofit2.Response
import retrofit2.http.GET

interface GenreApiService {

    @Authenticated
    @GET(MOVIE_GENRE_ENDPOINT)
    suspend fun getMovieGenre(): Response<GenreListResponse>

    @Authenticated
    @GET(TV_SHOW_GENRE_ENDPOINT)
    suspend fun getTvShowGenre(): Response<GenreListResponse>

    companion object {
        private const val MOVIE_GENRE_ENDPOINT = "genre/movie/list"
        private const val TV_SHOW_GENRE_ENDPOINT = "genre/tv/list"
    }
}