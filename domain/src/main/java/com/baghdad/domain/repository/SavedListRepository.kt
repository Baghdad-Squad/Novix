package com.baghdad.domain.repository

import com.baghdad.domain.model.savedList.SavedListDetails

interface SavedListRepository {
    suspend fun getSavedListDetails(listId: Long): SavedListDetails
}
