package com.baghdad.remoteDataSource.apiService

import com.baghdad.remoteDataSource.interceptor.Authenticated
import com.baghdad.remoteDataSource.request.CreateListRequest
import com.baghdad.remoteDataSource.response.savedList.CreateSavedListResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface SavedListApiService {

    @Authenticated
    @POST(CREATE_SAVED_LIST)
    suspend fun createSavedList(
        @Body body: CreateListRequest
    ): Response<CreateSavedListResponse>

    companion object {
        private const val CREATE_SAVED_LIST = "list"
    }

}
