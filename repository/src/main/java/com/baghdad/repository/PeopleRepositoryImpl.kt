package com.baghdad.repository

import com.baghdad.domain.model.PagedResult
import com.baghdad.domain.repository.PeopleRepository
import com.baghdad.entity.person.People
import com.baghdad.repository.datasource.local.LocalPepularPeopleDataSource
import com.baghdad.repository.datasource.remote.RemotePeopleDataSource
import com.baghdad.repository.mapper.toEntity
import com.baghdad.repository.model.PeopleDto
import com.baghdad.repository.util.getPagedSafely

class PeopleRepositoryImpl(
    private val remotePeopleDataSource: RemotePeopleDataSource,
    private val localPopularPeopleDataSource: LocalPepularPeopleDataSource
) : PeopleRepository {

    override suspend fun getPopularPeople(page: Int): PagedResult<People> {
        return getPagedSafely(
            page = page,
            mapToEntity = PeopleDto::toEntity,
            onStart = {},
            getCachedPage = { page, pageSize ->
                 localPopularPeopleDataSource.getPopularPeople(page, pageSize)

            },
            getRemoteData = { page, _ ->
             remotePeopleDataSource.getPopularPeople(page)

            },
            cacheData = {
                 localPopularPeopleDataSource.addPopularPeople(it)
            }
        )
    }

}
