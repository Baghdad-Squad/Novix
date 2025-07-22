package com.baghdad.domain.repository

import com.baghdad.domain.model.PagedResult
import com.baghdad.entity.person.People

interface PeopleRepository {
 suspend fun getPopularPeople(page: Int): PagedResult<People>
}
