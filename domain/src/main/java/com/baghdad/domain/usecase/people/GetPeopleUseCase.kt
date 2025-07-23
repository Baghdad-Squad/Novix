package com.baghdad.domain.usecase.people

import android.util.Log
import com.baghdad.domain.model.PagedResult
import com.baghdad.domain.repository.PeopleRepository
import com.baghdad.entity.person.People

class GetPopularPeopleUseCase(
    private val peopleRepository: PeopleRepository
) {
    suspend operator fun invoke(
        page: Int
    ): PagedResult<People> {
       return peopleRepository.getPopularPeople(page)

    }
}

