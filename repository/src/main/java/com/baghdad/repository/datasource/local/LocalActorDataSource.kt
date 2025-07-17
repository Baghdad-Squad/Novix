package com.baghdad.repository.datasource.local

import com.baghdad.repository.model.ActorDto
import kotlinx.coroutines.flow.Flow

interface LocalActorDataSource {
    suspend fun addActor(name: String, imageUrl: String)
    suspend fun deleteActorById(id : Long)
    suspend fun getActorById(id : Long) : ActorDto
    suspend fun getAllActors(): Flow<List<ActorDto>>
    suspend fun deleteAllActors()
    suspend fun updateActor(actor: ActorDto)
    suspend fun searchActorsByName(name: String): List<ActorDto>
}