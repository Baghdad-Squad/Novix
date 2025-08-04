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

    @Authenticated
    @POST(ADD_ITEM_TO_SAVED_LIST_ENDPOINT)
    suspend fun addItemToSavedList(
        @Path("list_id") listId: Long,
        @Body body: AddItemRequest,
        @Query("session_id") sessionId: String
    ): Response<AddItemToSavedResponse>

    @Authenticated
    @GET(LISTS_ENDPOINT)
    suspend fun getSavedLists(
        @Path("account_id") accountId: Long,
        @Query("session_id") sessionId: String,
        @Query("page") page: Int,
    ): Response<UserListsResponse>

    companion object {
        private const val LISTS_ENDPOINT = "account/{account_id}/lists"
        private const val ADD_ITEM_TO_SAVED_LIST_ENDPOINT = "/list/{list_id}/add_item"
        private const val CREATE_SAVED_LIST = "list"
    }
}