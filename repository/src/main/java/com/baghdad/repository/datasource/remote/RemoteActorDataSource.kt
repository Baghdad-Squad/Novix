package com.baghdad.repository.datasource.remote

import com.baghdad.repository.model.MovieDto
import com.baghdad.repository.model.TvShowDto
import com.baghdad.repository.model.ActorDto
import com.baghdad.repository.model.PagedResultDto

interface RemoteActorDataSource {
    suspend fun getActorDetails(personId: Long): ActorDto
    suspend fun getActorImages(personId: Long): List<String>
    suspend fun getActorMovies(personId: Long): List<MovieDto>
    suspend fun getActorTvShows(personId: Long): List<TvShowDto>
    suspend fun getTrendingActors(page: Int): PagedResultDto<ActorDto>
}
