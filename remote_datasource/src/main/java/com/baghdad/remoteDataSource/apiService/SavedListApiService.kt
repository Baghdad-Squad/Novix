package com.baghdad.remoteDataSource.apiService

import com.baghdad.remoteDataSource.interceptor.Authenticated
import com.baghdad.remoteDataSource.response.savedList.DeleteSavedListResponse
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.Path

interface SavedListApiService {

    @Authenticated
    @DELETE(DELETE_SAVED_LIST_BY_TITLE_ENDPOINT)
    suspend fun deleteSavedListByTitle(
        @Path("list_id") listId: Long
    ): Response<DeleteSavedListResponse>


    companion object {
        private const val DELETE_SAVED_LIST_BY_TITLE_ENDPOINT = "list/{list_id}"
    }
}
