package com.baghdad.repository.datasource.remote

import com.baghdad.repository.model.PagedResultDto
import com.baghdad.repository.model.PeopleDto

interface RemotePeopleDataSource {
    suspend fun getPopularPeople(page: Int): PagedResultDto<PeopleDto>
}