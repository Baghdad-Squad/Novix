package com.baghdad.remoteDataSource.apiService

import com.baghdad.remoteDataSource.interceptor.Authenticated
import com.baghdad.remoteDataSource.response.UserListsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface SavedListApiService {
    @Authenticated
    @GET(LISTS_ENDPOINT)
    suspend fun getSavedLists(
        @Path("session_id") sessionId: String,
        @Query("page") page: Int,
    ): Response<UserListsResponse>

    companion object {
        private const val LISTS_ENDPOINT = "account/{account_id}/lists"
    }
}