package com.baghdad.remoteDataSource

import com.baghdad.remoteDataSource.apiService.ActorApiService
import com.baghdad.remoteDataSource.mapper.actor.toDto
import com.baghdad.remoteDataSource.mapper.actor.toPagedActorDtos
import com.baghdad.remoteDataSource.response.actor.ActorDetailsResponse
import com.baghdad.remoteDataSource.response.actor.ActorImagesResponse
import com.baghdad.remoteDataSource.response.actor.ActorMoviesResponse
import com.baghdad.remoteDataSource.response.actor.ActorTvShowsResponse
import com.baghdad.remoteDataSource.response.actor.TrendingActorResponse
import com.baghdad.remoteDataSource.util.handleRequest
import com.baghdad.repository.datasource.remote.RemoteActorDataSource
import com.baghdad.repository.logger.Logger
import com.baghdad.repository.model.ActorDto
import com.baghdad.repository.model.MovieDto
import com.baghdad.repository.model.PagedResultDto
import com.baghdad.repository.model.TvShowDto
import javax.inject.Inject

class RemoteActorDataSourceImpl @Inject constructor(
    private val actorApiService: ActorApiService,
    private val logger: Logger,
): RemoteActorDataSource {
    override suspend fun getActorDetails(personId: Long): ActorDto {
        return handleRequest<ActorDetailsResponse>(
            apiCall = { actorApiService.getActorDetails(personId) },
            logger = logger,
        ).toDto()
    }

    override suspend fun getActorImages(personId: Long): List<String> {
        return handleRequest<ActorImagesResponse>(
            apiCall = { actorApiService.getActorImages(personId = personId) },
            logger = logger,

        ).profiles.orEmpty().map { "https://image.tmdb.org/t/p/w500" + it.filePath }
    }

    override suspend fun getActorMovies(personId: Long): List<MovieDto> {
        return handleRequest<ActorMoviesResponse>(
            apiCall = { actorApiService.getActorMovies(personId) },
            logger = logger,
        ).cast?.map { it.toDto() } ?: emptyList()
    }

    override suspend fun getActorTvShows(personId: Long): List<TvShowDto> {
        return handleRequest<ActorTvShowsResponse>(
            apiCall = {actorApiService.getActorTvShows(personId)},
            logger = logger
        ).cast?.map { it.toDto() } ?: emptyList()
    }

    override suspend fun getTrendingActors(page: Int): PagedResultDto<ActorDto> {
        val result  = handleRequest<TrendingActorResponse>(
           apiCall = { actorApiService.getTrendingActors(page) },
            logger = logger
        )
        return result.toPagedActorDtos()
    }
}
