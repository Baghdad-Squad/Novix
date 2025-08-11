package com.baghdad.remoteDataSource.apiService

import com.baghdad.remoteDataSource.interceptor.Authenticated
import com.baghdad.remoteDataSource.interceptor.RequiresSession
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
    @RequiresSession
    @POST("list")
    suspend fun createSavedList(
        @Body body: CreateListRequest,
    ): Response<CreateSavedListResponse>

    @Authenticated
    @RequiresSession
    @POST("list/{list_id}/add_item")
    suspend fun addMovieToSavedList(
        @Path("list_id") listId: Long,
        @Body body: AddListItemRequest,
    ): Response<AddListItemResponse>

    @Authenticated
    @RequiresSession
    @POST("list/{list_id}/remove_item")
    suspend fun removeMovieFromSavedList(
        @Path("list_id") listId: Long,
        @Body body: RemoveListItemRequest,
    ): Response<RemoveListItemResponse>

    @Authenticated
    @RequiresSession
    @GET("account/{account_id}/lists")
    suspend fun getSavedLists(
        @Path("account_id") accountId: Long,
        @Query("page") page: Int,
    ): Response<UserListsResponse>

    @Authenticated
    @GET("list/{list_id}")
    suspend fun getListDetails(
        @Path("list_id") listId: Long,
        @Query("page") page: Int,
    ): Response<ListDetailsResponse>

    @Authenticated
    @RequiresSession
    @DELETE("list/{list_id}")
    suspend fun deleteSavedListById(
        @Path("list_id") listId: Long,
    ): Response<DeleteSavedListResponse>
}
