package com.baghdad.repository

import com.baghdad.domain.repository.ActorRepository
import com.baghdad.entity.media.Movie
import com.baghdad.entity.media.TvShow
import com.baghdad.entity.person.Actor
import com.baghdad.repository.datasource.local.LocalActorDataSource
import com.baghdad.repository.datasource.remote.RemoteActorDataSource
import com.baghdad.repository.mapper.toEntity

class ActorRepositoryImpl(
    private val remoteActorDataSource: RemoteActorDataSource,
    private val localActorDataSource: LocalActorDataSource
): ActorRepository {
    override suspend fun getActorInfo(actorId: Long): Actor {
        return remoteActorDataSource.getActorDetails(actorId).toEntity()
    }

    override suspend fun getActorMovies(actorId: Long): List<Movie> {
        return remoteActorDataSource.getActorMovies(actorId).map { it.toEntity() }
    }

    override suspend fun getActorTvShows(actorId: Long): List<TvShow> {
        return remoteActorDataSource.getActorTvShows(actorId).map { it.toEntity() }
    }

    override suspend fun getActorGallery(actorId: Long): List<String> {
        return remoteActorDataSource.getActorImages(actorId).images
    }


}