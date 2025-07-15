package com.baghdad.repository.datasource.remote

import com.baghdad.repository.model.MovieDto
import com.baghdad.repository.model.TvShowDto
import com.baghdad.repository.model.actor.ActorDto
import com.baghdad.repository.model.actor.ActorImagesDto

interface RemoteActorDataSource {
    suspend fun getActorDetails(personId: Long): ActorDto
    suspend fun getActorImages(personId: Long): ActorImagesDto
    suspend fun getActorMovies(personId: Long): List<MovieDto>
    suspend fun getActorTvShows(personId: Long): List<TvShowDto>
}
