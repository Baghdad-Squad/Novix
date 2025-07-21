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
    @GET(SEARCH_MOVIES_ENDPOINT)
    suspend fun searchMovies(
        @Query("query") query: String,
        @Query("page") page: Int

    ): Response<MovieSearchResponse>

    @Authenticated
    @GET(SEARCH_MOVIES_ENDPOINT)
    suspend fun getMoviesResultCount(
        @Query("title") title: String
    ): Response<MovieSearchResponse>

    @Authenticated
    @GET(SEARCH_TV_SHOWS_ENDPOINT)
    suspend fun searchTvShows(
        @Query("query") query: String,
        @Query("page") page: Int
    ): Response<TvShowSearchResponse>

    @Authenticated
    @GET(SEARCH_TV_SHOWS_ENDPOINT)
    suspend fun getTvShowsResultCount(
        @Query("title") title: String
    ): Response<TvShowSearchResponse>

    @Authenticated
    @GET(SEARCH_ACTORS_ENDPOINT)
    suspend fun searchActors(
        @Query("query") query: String,
        @Query("page") page: Int
    ): Response<ActorSearchResponse>

    @Authenticated
    @GET(SEARCH_ACTORS_ENDPOINT)
    suspend fun getActorsResultCount(
        @Query("name") name: String
    ): Response<ActorSearchResponse>


    companion object {
        private const val SEARCH_MOVIES_ENDPOINT = "search/movie"
        private const val SEARCH_TV_SHOWS_ENDPOINT = "search/tv"
        private const val SEARCH_ACTORS_ENDPOINT = "search/person"
    }
}