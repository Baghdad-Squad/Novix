package com.baghdad.domain.repository

import com.baghdad.entity.media.Movie
import com.baghdad.entity.media.TvShow
import com.baghdad.entity.person.Actor

interface ActorRepository {
    suspend fun getActorInfo(actorId: Long): Actor
    suspend fun getActorMovies(actorId: Long): List<Movie>
    suspend fun getActorTvShows(actorId: Long): List<TvShow>
    suspend fun getActorGallery(actorId: Long): List<String>
    suspend fun searchActorsByName(name: String): List<Actor>
}