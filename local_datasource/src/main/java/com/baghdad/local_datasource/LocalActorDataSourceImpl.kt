package com.baghdad.local_datasource

import com.baghdad.local_datasource.roomDB.dao.ActorDao
import com.baghdad.local_datasource.roomDB.entity.Actor
import com.baghdad.local_datasource.roomDB.entity.toDto
import com.baghdad.local_datasource.roomDB.entity.toEntity
import com.baghdad.local_datasource.roomDB.errorHandler.executeFlowWithErrorHandling
import com.baghdad.local_datasource.roomDB.errorHandler.executeWithErrorHandling
import com.baghdad.local_datasource.util.calculatePageOffset
import com.baghdad.repository.datasource.local.LocalActorDataSource
import com.baghdad.repository.logger.Logger
import com.baghdad.repository.model.ActorDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class LocalActorDataSourceImpl @Inject constructor(
    private val actorDao: ActorDao,
    private val logger: Logger
) : LocalActorDataSource {
    override suspend fun addActor(actor: ActorDto) = executeWithErrorHandling(logger = logger) {
        actorDao.upsertActor(actor.toEntity())
    }

    override suspend fun addActors(actors: List<ActorDto>) {
        executeWithErrorHandling(logger = logger) {
            actorDao.upsertActors(actors.map(ActorDto::toEntity))
        }
    }

    override suspend fun deleteActorById(id: Long) = executeWithErrorHandling(logger = logger) {
        actorDao.deleteActorById(id)
    }


    override suspend fun getActorById(id: Long): ActorDto =
        executeWithErrorHandling(logger = logger) {
            actorDao.getActorById(id).toDto()
        }


    override suspend fun getAllActors(): Flow<List<ActorDto>> =
        executeFlowWithErrorHandling(logger = logger) {
            actorDao.getAllActors().map {
                it.map { it.toDto() }
            }
        }


    override suspend fun deleteAllActors() = executeWithErrorHandling(logger = logger) {
        actorDao.deleteAllActors()
    }

    override suspend fun updateActor(actor: ActorDto) = executeWithErrorHandling(logger = logger) {
        val actorEntity = actor.toEntity()
        actorDao.upsertActor(actorEntity)
    }

    override suspend fun searchActorsByName(name: String, page: Int, pageSize: Int) =
        executeWithErrorHandling(logger = logger) {
            val pageOffset = calculatePageOffset(pageSize, page)
            actorDao.getActorsFromSearchQuery(name, pageSize, pageOffset).map(Actor::toDto)
        }
}



