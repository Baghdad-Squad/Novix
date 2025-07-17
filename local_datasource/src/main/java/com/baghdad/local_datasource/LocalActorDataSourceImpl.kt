package com.baghdad.local_datasource

import com.baghdad.local_datasource.roomDB.dao.ActorDao
import com.baghdad.local_datasource.roomDB.entity.Actor
import com.baghdad.local_datasource.roomDB.entity.toDto
import com.baghdad.local_datasource.roomDB.entity.toEntity
import com.baghdad.local_datasource.roomDB.errorHandler.executeWithErrorHandling
import com.baghdad.local_datasource.util.calculatePageOffset
import com.baghdad.repository.datasource.local.LocalActorDataSource
import com.baghdad.repository.model.actor.ActorDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class LocalActorDataSourceImpl(
    private val actorDao: ActorDao
) : LocalActorDataSource {
    override suspend fun addActor(name: String, imageUrl: String) =
        executeWithErrorHandling {
            val actor = Actor( /*TODO replace with real data */
                name = name,
                profilePictureURL = imageUrl,
                birthDate = "",
                deathDate = null,
                biography = "",
                placeOfBirth = "",
                headerPictures = emptyList(),
                department = ""
            )
            actorDao.upsertActor(actor)
        }

    override suspend fun addActors(actors: List<ActorDto>) {
        executeWithErrorHandling {
            actorDao.upsertActors(actors.map(ActorDto::toEntity))
        }
    }

    override suspend fun deleteActorById(id: Long) =
        executeWithErrorHandling {
            actorDao.deleteActorById(id)
        }


    override suspend fun getActorById(id: Long): ActorDto =
        executeWithErrorHandling {
            actorDao.getActorById(id).toDto()
        }


    override suspend fun getAllActors(): Flow<List<ActorDto>> =
        executeWithErrorHandling {
            actorDao.getAllActors().map {
                it.map { it.toDto() }
            }
        }

    override suspend fun deleteAllActors() =
        executeWithErrorHandling {
            actorDao.deleteAllActors()
        }

    override suspend fun updateActor(actor: ActorDto) =
        executeWithErrorHandling {
            val actorEntity = actor.toEntity()
            actorDao.upsertActor(actorEntity)
        }

    override suspend fun searchActorsByName(name: String, page: Int, pageSize: Int) =
        executeWithErrorHandling {
            val pageOffset = calculatePageOffset(pageSize, page)
            actorDao.getActorsFromSearchQuery(name, pageSize, pageOffset).map(Actor::toDto)
        }

    override suspend fun getActorCountByName(name: String): Int {
        return executeWithErrorHandling {
            actorDao.getActorCountBySearchQuery(name)
        }
    }
}
