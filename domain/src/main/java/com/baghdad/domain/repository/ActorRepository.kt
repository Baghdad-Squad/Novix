package com.baghdad.domain.repository

import com.baghdad.domain.model.PagedResult
import com.baghdad.domain.model.savedList.SavableMovie
import com.baghdad.entity.media.TvShow
import com.baghdad.entity.person.Actor

interface ActorRepository {
    suspend fun getActorInfo(actorId: Long): Actor

    suspend fun getActorMovies(actorId: Long): List<SavableMovie>

    suspend fun getActorTvShows(actorId: Long): List<TvShow>
    suspend fun getActorGallery(actorId: Long): List<String>
    suspend fun getTrendingActors(page: Int): PagedResult<Actor>
}