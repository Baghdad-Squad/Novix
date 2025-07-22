package com.baghdad.local_datasource

import com.baghdad.local_datasource.roomDB.dao.PeopleDao
import com.baghdad.local_datasource.roomDB.entity.toDto
import com.baghdad.local_datasource.roomDB.entity.toEntity
import com.baghdad.local_datasource.roomDB.errorHandler.executeWithErrorHandling
import com.baghdad.local_datasource.util.calculatePageOffset
import com.baghdad.repository.datasource.local.LocalPepularPeopleDataSource
import com.baghdad.repository.logger.Logger
import com.baghdad.repository.model.PeopleDto

class LocalPopularPeopleDataSourceImpl(
    private val peopleDao: PeopleDao,
    private val logger: Logger
): LocalPepularPeopleDataSource {
    override suspend fun addPopularPeople(people: List<PeopleDto>) =
       executeWithErrorHandling(logger = logger){
        peopleDao.upsertPopularPeople(people.map { it.toEntity() })
    }


    override suspend fun getPopularPeople(page: Int, pageSize: Int): List<PeopleDto> =
        executeWithErrorHandling(logger = logger) {
            val offset = calculatePageOffset(pageSize, page)
            val people = peopleDao.getAllPopularPeople(pageSize, offset)
            people.map { it.toDto() }
        }


    override suspend fun getPersonById(id: Long): PeopleDto =
         executeWithErrorHandling(logger = logger){
            peopleDao.getPersonId(id).toDto()
    }

    override suspend fun deletePopularPeople() =
         executeWithErrorHandling(logger = logger){
            peopleDao.deleteAllPopularPeople()
        }
    }

