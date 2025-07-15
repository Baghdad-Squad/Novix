package com.baghdad.local_datasource

import com.baghdad.local_datasource.database.dao.ActorDao
import com.baghdad.local_datasource.database.dto.LocalActorDto
import com.baghdad.local_datasource.database.dto.toDto
import com.baghdad.local_datasource.database.dto.toEntity
import com.baghdad.local_datasource.database.errorHandler.executeWithErrorHandling
import com.baghdad.repository.datasource.local.LocalActorDataSource
import com.baghdad.repository.model.actor.ActorDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class LocalActorDataSourceImpl(
    private val actorDao: ActorDao
) : LocalActorDataSource {
    override suspend fun addActor(name: String, imageUrl: String) {
        executeWithErrorHandling {
            val localActorDto = LocalActorDto(
                name = name,
                profilePictureURL = imageUrl,
                birthDate = "",
                deathDate = null,
                biography = "",
                placeOfBirth = "",
                headerPictures = emptyList(),
                department = ""
            )
            actorDao.upsertActor(localActorDto)
        }
    }

    override suspend fun deleteActorById(id: Long) {
        executeWithErrorHandling {
            actorDao.deleteActorById(id)
        }
    }


    override suspend fun getActorById(id: Long): ActorDto {
        return executeWithErrorHandling {
            actorDao.getActorById(id).toDto()
        }
    }


    override suspend fun getAllActors(): Flow<List<ActorDto>> {
        return executeWithErrorHandling {
            actorDao.getAllActors().map {
                it.map { it.toDto() }
            }
        }
    }

    override suspend fun deleteAllActors() {
        executeWithErrorHandling {
            actorDao.deleteAllActors()
        }
    }

    override suspend fun updateActor(actor: ActorDto) {
        executeWithErrorHandling {
            val actorEntity = actor.toEntity()
            actorDao.upsertActor(actorEntity)
        }
    }

    override suspend fun searchActorsByName(name: String): List<ActorDto> {
        return executeWithErrorHandling {
            actorDao.searchActorsByName(name).map {
                it.toDto()
            }
        }
    }
}
