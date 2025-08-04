package com.baghdad.domain.usecase.savedList

import com.baghdad.domain.model.PagedResult
import com.baghdad.domain.repository.SavedListRepository
import com.baghdad.entity.savedList.SavedList
import javax.inject.Inject

class GetSavedListsUseCase @Inject constructor(
    private val savedListRepository: SavedListRepository
) {
    suspend operator fun invoke(
        page: Int,
        pageSize: Int,
    ): PagedResult<SavedList> {
        return savedListRepository.getSavedLists(page,pageSize)
    }
}