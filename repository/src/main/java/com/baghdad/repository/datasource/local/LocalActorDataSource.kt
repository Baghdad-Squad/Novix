package com.baghdad.repository.datasource.local

import com.baghdad.repository.model.actor.ActorDto
import kotlinx.coroutines.flow.Flow

interface LocalActorDataSource {
    suspend fun addActor(name: String, imageUrl: String)
    suspend fun addActors(actors: List<ActorDto>)
    suspend fun deleteActorById(id: Long)
    suspend fun getActorById(id: Long): ActorDto
    suspend fun getAllActors(): Flow<List<ActorDto>>
    suspend fun deleteAllActors()
    suspend fun updateActor(actor: ActorDto)
    suspend fun searchActorsByName(name: String, page: Int, pageSize: Int = 20): List<ActorDto>
    suspend fun getActorCountByName(name: String): Int
}