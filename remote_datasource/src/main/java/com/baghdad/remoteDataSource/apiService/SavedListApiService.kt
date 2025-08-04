package com.baghdad.remoteDataSource.apiService

import com.baghdad.remoteDataSource.interceptor.Authenticated
import com.baghdad.remoteDataSource.request.AddItemRequest
import com.baghdad.remoteDataSource.request.CreateListRequest
import com.baghdad.remoteDataSource.response.AddItemToSavedResponse
import com.baghdad.remoteDataSource.response.UserListsResponse
import com.baghdad.remoteDataSource.response.savedList.CreateSavedListResponse
import com.baghdad.remoteDataSource.response.savedList.ListDetailsResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface SavedListApiService {
    @Authenticated
    @POST(CREATE_SAVED_LIST)
    suspend fun createSavedList(
        @Body body: CreateListRequest,
    ): Response<CreateSavedListResponse>

    @Authenticated
    @POST(ADD_ITEM_TO_SAVED_LIST_ENDPOINT)
    suspend fun addItemToSavedList(
        @Path("list_id") listId: Long,
        @Body body: AddItemRequest,
        @Query("session_id") sessionId: String,
    ): Response<AddItemToSavedResponse>

    @Authenticated
    @GET(LISTS_ENDPOINT)
    suspend fun getSavedLists(
        @Path("account_id") accountId: Long,
        @Query("session_id") sessionId: String,
        @Query("page") page: Int,
    ): Response<UserListsResponse>

    @Authenticated
    @GET(GET_LIST_DETAILS_ENDPOINT)
    suspend fun getListDetails(
        @Path("list_id") listId: Long,
        @Query("page") page: Int,
    ): Response<ListDetailsResponse>

    companion object {
        private const val LISTS_ENDPOINT = "account/{account_id}/lists"
        private const val ADD_ITEM_TO_SAVED_LIST_ENDPOINT = "/list/{list_id}/add_item"
        private const val CREATE_SAVED_LIST = "list"
        private const val GET_LIST_DETAILS_ENDPOINT = "list/{list_id}"
    }
}
