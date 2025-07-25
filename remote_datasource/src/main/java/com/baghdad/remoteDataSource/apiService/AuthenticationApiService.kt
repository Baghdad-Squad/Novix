package com.baghdad.remoteDataSource.apiService

import com.baghdad.remoteDataSource.interceptor.Authenticated
import com.baghdad.remoteDataSource.request.CredentialDataBody
import com.baghdad.remoteDataSource.request.RequestTokenBody
import com.baghdad.remoteDataSource.response.RequestTokenResponse
import com.baghdad.remoteDataSource.response.SessionResponse
import com.baghdad.remoteDataSource.response.user.UserResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface AuthenticationApiService {
    @Authenticated
    @GET("authentication/token/new")
    suspend fun getRequestToken(): Response<RequestTokenResponse>

    @Authenticated
    @POST("authentication/token/validate_with_login")
    suspend fun validateCredential(@Body body: CredentialDataBody): Response<RequestTokenResponse>

    @Authenticated
    @POST("authentication/session/new")
    suspend fun createSession(@Body body: RequestTokenBody): Response<SessionResponse>

    @Authenticated
    @GET("account")
    suspend fun getUserDetails(
        @Query("session_id") sessionId: String
    ): Response<UserResponse>

    @Authenticated
    @POST("authentication/session")
    suspend fun deleteSession(
        @Query("session_id") sessionId: String
    ): Response<SessionResponse>
}