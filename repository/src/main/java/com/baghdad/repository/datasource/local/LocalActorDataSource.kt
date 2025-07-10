package com.baghdad.repository.datasource.local

import com.baghdad.repository.model.ActorDto

interface LocalActorDataSource {
    suspend fun addActor(name: String, imageUrl: String)
    suspend fun deleteActorById(id : Long)
    suspend fun getActorById(id : Long) : ActorDto
    suspend fun getAllActors() : List<ActorDto>
    suspend fun deleteAllActors()
    suspend fun updateActor(actor: ActorDto)
}