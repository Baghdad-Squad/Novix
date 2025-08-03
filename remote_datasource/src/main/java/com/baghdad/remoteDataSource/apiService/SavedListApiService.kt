package com.baghdad.remoteDataSource.apiService

import com.baghdad.remoteDataSource.interceptor.Authenticated
import com.baghdad.remoteDataSource.response.savedList.CreateSavedListResponse
import retrofit2.Response
import retrofit2.http.POST

interface SavedListApiService {

    @Authenticated
    @POST("list")
    suspend fun createSavedList(title: String): Response<CreateSavedListResponse>
}
