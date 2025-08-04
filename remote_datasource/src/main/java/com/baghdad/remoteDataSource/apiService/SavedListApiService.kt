package com.baghdad.remoteDataSource.apiService

import com.baghdad.remoteDataSource.interceptor.Authenticated
import com.baghdad.remoteDataSource.response.savedList.ListDetailsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface SavedListApiService {
    @Authenticated
    @GET(GET_LIST_DETAILS_ENDPOINT)
    suspend fun getListDetails(
        @Path("list_id") listId: Long,
    ): Response<ListDetailsResponse>

    companion object {
        private const val GET_LIST_DETAILS_ENDPOINT = "list/{list_id}"
    }
}
