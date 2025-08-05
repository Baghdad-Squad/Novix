package com.baghdad.domain.usecase.savedList

import com.baghdad.domain.model.PagedResult
import com.baghdad.domain.model.savedList.SavedListDetails
import com.baghdad.domain.repository.SavedListRepository
import javax.inject.Inject

class GetSavedListDetailsUseCase @Inject constructor(
    private val savedListRepository: SavedListRepository,
) {
    suspend operator fun invoke(
        listId: Long,
        page: Int,
        pageSize: Int,
    ): PagedResult<SavedListDetails> {
       return savedListRepository.getSavedListDetails(listId, page, pageSize)
    }
}
