package com.baghdad.remoteDataSource.apiService

import com.baghdad.remoteDataSource.interceptor.Authenticated
import com.baghdad.remoteDataSource.response.actor.ActorDetailsResponse
import com.baghdad.remoteDataSource.response.actor.ActorImagesResponse
import com.baghdad.remoteDataSource.response.actor.ActorMoviesResponse
import com.baghdad.remoteDataSource.response.actor.ActorTvShowsResponse
import com.baghdad.remoteDataSource.response.actor.TrendingActorResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ActorApiService {

    @Authenticated
    @GET("person/{person_id}")
    suspend fun getActorDetails(
        @Path("person_id") personId: Long
    ): Response<ActorDetailsResponse>

    @Authenticated
    @GET("person/{person_id}/images")
    suspend fun getActorImages(
        @Path("person_id") personId: Long
    ): Response<ActorImagesResponse>

    @Authenticated
    @GET("person/{person_id}/movie_credits")
    suspend fun getActorMovies(
        @Path("person_id") personId: Long
    ): Response<ActorMoviesResponse>

    @Authenticated
    @GET("person/{person_id}/tv_credits")
    suspend fun getActorTvShows(
        @Path("person_id") personId: Long,
    ): Response<ActorTvShowsResponse>

    @Authenticated
    @GET("trending/person/day")
    suspend fun getTrendingActors(
        @Query("page") page: Int
    ): Response<TrendingActorResponse>
}