package com.baghdad.remoteDataSource.apiService

import com.baghdad.remoteDataSource.response.savedList.CreateSavedListResponse
import retrofit2.Response

interface SavedListApiService {
    suspend fun createSavedList(title: String): Response<CreateSavedListResponse>
}
