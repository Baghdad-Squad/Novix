package com.baghdad.remoteDataSource

import com.baghdad.remoteDataSource.mapper.actor.toDto
import com.baghdad.remoteDataSource.response.actor.ActorDetailsResponse
import com.baghdad.remoteDataSource.response.actor.ActorImagesResponse
import com.baghdad.remoteDataSource.response.actor.ActorMoviesResponse
import com.baghdad.remoteDataSource.response.actor.ActorTvShowsResponse
import com.baghdad.remoteDataSource.util.handleRequest
import com.baghdad.repository.datasource.remote.RemoteActorDataSource
import com.baghdad.repository.logger.Logger
import com.baghdad.repository.model.ActorDto
import com.baghdad.repository.model.MovieDto
import com.baghdad.repository.model.TvShowDto
import io.ktor.client.HttpClient

class RemoteActorDataSourceImpl(
    private val httpClient: HttpClient,
    private val logger: Logger,
    private val baseUrl: String
): RemoteActorDataSource {
    override suspend fun getActorDetails(personId: Long): ActorDto {
        return handleRequest<ActorDetailsResponse>(
            client = httpClient,
            logger = logger,
            url = "$baseUrl${PERSON_DETAILS_ENDPOINT.replace("{person_id}", personId.toString())}"
        ).toDto()
    }

    override suspend fun getActorImages(personId: Long): List<String> {
        return handleRequest<ActorImagesResponse>(
            client = httpClient,
            logger = logger,
            url = "$baseUrl${PERSON_IMAGES_ENDPOINT.replace("{person_id}", personId.toString())}"
        ).profiles.orEmpty().map { "https://image.tmdb.org/t/p/w500" + it.filePath }
    }

    override suspend fun getActorMovies(personId: Long): List<MovieDto> {
        return handleRequest<ActorMoviesResponse>(
            client = httpClient,
            logger = logger,
            url = "$baseUrl${
                PERSON_MOVIES_PICK_ENDPOINT.replace(
                    "{person_id}",
                    personId.toString()
                )
            }"
        ).cast?.map { it.toDto() } ?: emptyList()
    }

    override suspend fun getActorTvShows(personId: Long): List<TvShowDto> {
        return handleRequest<ActorTvShowsResponse>(
            client = httpClient,
            logger = logger,
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
