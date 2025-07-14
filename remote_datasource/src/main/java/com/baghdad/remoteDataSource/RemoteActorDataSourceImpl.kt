package com.baghdad.remoteDataSource

import com.baghdad.remoteDataSource.mapper.actor.toDto
import com.baghdad.remoteDataSource.response.actor.ActorDetailsResponse
import com.baghdad.remoteDataSource.response.actor.ActorImagesResponse
import com.baghdad.remoteDataSource.response.actor.ActorMoviesResponse
import com.baghdad.remoteDataSource.response.actor.ActorTvShowsResponse
import com.baghdad.remoteDataSource.util.handleRequest
import com.baghdad.repository.model.MovieDto
import com.baghdad.repository.model.TvShowDto
import com.baghdad.repository.model.actor.ActorDetailsDto
import com.baghdad.repository.model.actor.ActorImagesDto
import io.ktor.client.HttpClient

class RemoteActorDataSourceImpl(
    private val httpClient: HttpClient,
    private val baseUrl: String
) {
    suspend fun getActorDetails(personId: Int): ActorDetailsDto {
        return handleRequest<ActorDetailsResponse>(
            client = httpClient,
            url = "$baseUrl${PERSON_DETAILS_ENDPOINT.replace("{person_id}", personId.toString())}"
        ).toDto()
    }

    suspend fun getActorImages(personId: Int): ActorImagesDto {
        return handleRequest<ActorImagesResponse>(
            client = httpClient,
            url = "$baseUrl${PERSON_IMAGES_ENDPOINT.replace("{person_id}", personId.toString())}"
        ).toDto()
    }

    suspend fun getActorMovies(personId: Int): List<MovieDto> {
        return handleRequest<ActorMoviesResponse>(
            client = httpClient,
            url = "$baseUrl${
                PERSON_MOVIES_PICK_ENDPOINT.replace(
                    "{person_id}",
                    personId.toString()
                )
            }"
        ).cast?.map { it.toDto() } ?: emptyList()
    }

    suspend fun getActorTvShows(personId: Int): List<TvShowDto> {
        return handleRequest<ActorTvShowsResponse>(
            client = httpClient,
            url = "$baseUrl${
                PERSON_TV_SHOWS_PICK_ENDPOINT.replace(
                    "{person_id}",
                    personId.toString()
                )
            }"
        ).cast?.map { it.toDto() } ?: emptyList()
    }

    companion object {
        private const val PERSON_MOVIES_PICK_ENDPOINT = "/person/{person_id}/movie_credits"
        private const val PERSON_TV_SHOWS_PICK_ENDPOINT = "/person/{person_id}/tv_credits"
        private const val PERSON_IMAGES_ENDPOINT = "/person/{person_id}/images"
        private const val PERSON_DETAILS_ENDPOINT = "/person/{person_id}"
    }
}
