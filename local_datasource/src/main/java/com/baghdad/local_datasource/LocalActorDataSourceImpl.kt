package com.baghdad.local_datasource

import com.baghdad.local_datasource.roomDB.dao.ActorDao
import com.baghdad.local_datasource.roomDB.entity.Actor
import com.baghdad.local_datasource.roomDB.entity.toDto
import com.baghdad.local_datasource.roomDB.entity.toEntity
import com.baghdad.repository.datasource.local.LocalActorDataSource
import com.baghdad.repository.model.ActorDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class LocalActorDataSourceImpl(
    private val actorDao: ActorDao
) : LocalActorDataSource {
    override suspend fun addActor(name: String, imageUrl: String) {
        val actor = Actor(
            name = name,
            profilePictureURL = imageUrl
        )
        return actorDao.upsertActor(actor)
    }

    override suspend fun deleteActorById(id: Long) {
        return actorDao.deleteActorById(id)
    }

    override suspend fun getActorById(id: Long): ActorDto {
        return actorDao.getActorById(id).toDto()
    }

    override suspend fun getAllActors(): Flow<List<ActorDto>> {
        return actorDao.getAllActors().map {
            it.map { it.toDto() }
        }
    }

    override suspend fun deleteAllActors() {
        return actorDao.deleteAllActors()
    }

    override suspend fun updateActor(actor: ActorDto) {
        val actorEntity = actor.toEntity()
        return actorDao.upsertActor(actorEntity)
    }
}