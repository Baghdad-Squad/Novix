package com.baghdad.remoteDataSource.apiService

import com.baghdad.remoteDataSource.interceptor.Authenticated
import com.baghdad.remoteDataSource.request.AddItemsRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface SavedListApiService {
    @Authenticated
    @POST(ADD_ITEM_TO_SAVED_LIST_ENDPOINT)
    suspend fun addItemToSavedList(
        @Path("list_id") listId: Long,
        @Body body: AddItemsRequest,
        @Query("session_id") sessionId: String
    ): Response<Unit>

    companion object {
        private const val ADD_ITEM_TO_SAVED_LIST_ENDPOINT = "list/{list_id}/items"
    }
}
