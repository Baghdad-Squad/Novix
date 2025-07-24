package com.baghdad.remoteDataSource.apiService

import com.baghdad.remoteDataSource.interceptor.Authenticated
import com.baghdad.remoteDataSource.response.actor.ActorDetailsResponse
import com.baghdad.remoteDataSource.response.actor.ActorImagesResponse
import com.baghdad.remoteDataSource.response.actor.ActorMoviesResponse
import com.baghdad.remoteDataSource.response.actor.ActorTvShowsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ActorApiService {

    @Authenticated
    @GET(PERSON_DETAILS_ENDPOINT)
    suspend fun getActorDetails(
        @Path("person_id") personId: Long
    ): Response<ActorDetailsResponse>

    @Authenticated
    @GET(PERSON_IMAGES_ENDPOINT)
    suspend fun getActorImages(
        @Path("person_id") personId: Long
    ): Response<ActorImagesResponse>

    @Authenticated
    @GET(PERSON_MOVIES_PICK_ENDPOINT)
    suspend fun getActorMovies(
        @Path("person_id") personId: Long
    ): Response<ActorMoviesResponse>

    @Authenticated
    @GET(PERSON_TV_SHOWS_PICK_ENDPOINT)
    suspend fun getActorTvShows(
        @Path("person_id") personId: Long,
    ): Response<ActorTvShowsResponse>

    companion object {
        private const val PERSON_MOVIES_PICK_ENDPOINT = "person/{person_id}/movie_credits"
        private const val PERSON_TV_SHOWS_PICK_ENDPOINT = "person/{person_id}/tv_credits"
        private const val PERSON_IMAGES_ENDPOINT = "person/{person_id}/images"
        private const val PERSON_DETAILS_ENDPOINT = "person/{person_id}"
    }
}