package com.baghdad.repository.datasource.local

import com.baghdad.repository.model.PeopleDto

interface LocalPepularPeopleDataSource {
    suspend fun addPopularPeople(people: List<PeopleDto>)
    suspend fun getPopularPeople(page: Int, pageSize: Int = 20): List<PeopleDto>
    suspend fun getPersonById(id: Long): PeopleDto
    suspend fun deletePopularPeople()

}