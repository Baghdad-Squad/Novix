package com.baghdad.remoteDataSource.apiService

import com.baghdad.remoteDataSource.interceptor.Authenticated
import com.baghdad.remoteDataSource.request.AddListItemRequest
import com.baghdad.remoteDataSource.request.CreateListRequest
import com.baghdad.remoteDataSource.request.RemoveListItemRequest
import com.baghdad.remoteDataSource.response.UserListsResponse
import com.baghdad.remoteDataSource.response.savedList.AddListItemResponse
import com.baghdad.remoteDataSource.response.savedList.CreateSavedListResponse
import com.baghdad.remoteDataSource.response.savedList.DeleteSavedListResponse
import com.baghdad.remoteDataSource.response.savedList.ListDetailsResponse
import com.baghdad.remoteDataSource.response.savedList.RemoveListItemResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface SavedListApiService {

    @Authenticated
    @POST(CREATE_SAVED_LIST)
    suspend fun createSavedList(
        @Body body: CreateListRequest,
        @Query("session_id") sessionId: String,
    ): Response<CreateSavedListResponse>

    @Authenticated
    @POST(ADD_MOVIE_TO_SAVED_LIST_ENDPOINT)
    suspend fun addMovieToSavedList(
        @Path("list_id") listId: Long,
        @Body body: AddListItemRequest,
        @Query("session_id") sessionId: String
    ): Response<AddListItemResponse>

    @Authenticated
    @POST(REMOVE_MOVIE_TO_SAVED_LIST_ENDPOINT)
    suspend fun removeMovieFromSavedList(
        @Path("list_id") listId: Long,
        @Body body: RemoveListItemRequest,
        @Query("session_id") sessionId: String
    ): Response<RemoveListItemResponse>

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

    @Authenticated
    @DELETE(DELETE_SAVED_LIST_BY_ID_ENDPOINT)
    suspend fun deleteSavedListById(
        @Path("list_id") listId: Long,
    ): Response<DeleteSavedListResponse>

    companion object {
        private const val LISTS_ENDPOINT = "account/{account_id}/lists"
        private const val ADD_MOVIE_TO_SAVED_LIST_ENDPOINT = "list/{list_id}/add_item"
        private const val REMOVE_MOVIE_TO_SAVED_LIST_ENDPOINT = "list/{list_id}/remove_item"
        private const val CREATE_SAVED_LIST = "list"
        private const val GET_LIST_DETAILS_ENDPOINT = "list/{list_id}"
        private const val DELETE_SAVED_LIST_BY_ID_ENDPOINT = "list/{list_id}"
    }
}
