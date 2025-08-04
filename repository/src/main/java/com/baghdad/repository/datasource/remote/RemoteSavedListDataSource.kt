package com.baghdad.repository.datasource.remote

import com.baghdad.repository.model.savedList.SavedListDetailsDto

interface RemoteSavedListDataSource {
    suspend fun getSavedListDetails(listId: Long): SavedListDetailsDto
}
