package com.baghdad.local_datasource

import com.baghdad.local_datasource.roomDB.dao.ActorDao
import com.baghdad.local_datasource.roomDB.entity.Actor
import com.baghdad.local_datasource.roomDB.entity.toDto
import com.baghdad.local_datasource.roomDB.errorHandler.executeWithErrorHandling
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
                department = "",
                characterName = ""
            )
            actorDao.upsertActor(actor)
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
            val actorEntity = actor.toDto()
            actorDao.upsertActor(actorEntity)
        }

    override suspend fun searchActorsByName(name: String) =
        executeWithErrorHandling {
            actorDao.searchActorsByName(name).map {
                it.toDto()
            }
        }
    }
