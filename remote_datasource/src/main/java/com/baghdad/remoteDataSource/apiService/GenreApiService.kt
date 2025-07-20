package com.baghdad.remoteDataSource.apiService

import com.baghdad.remoteDataSource.response.GenreListResponse
import retrofit2.Response
import retrofit2.http.GET

interface GenreApiService {

    @GET(MOVIE_GENRE_ENDPOINT)
    suspend fun getMovieGenre(language: String): Response<GenreListResponse>

    @GET(TV_SHOW_GENRE_ENDPOINT)
    suspend fun getTvShowGenre(language: String): Response<GenreListResponse>

    companion object {
        private const val MOVIE_GENRE_ENDPOINT = "/genre/movie/list"
        private const val TV_SHOW_GENRE_ENDPOINT = "/genre/tv/list"
    }
}