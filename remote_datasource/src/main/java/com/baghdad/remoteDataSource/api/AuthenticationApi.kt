package com.baghdad.remoteDataSource.api

import com.baghdad.remoteDataSource.interceptor.Authenticated
import com.baghdad.remoteDataSource.request.CredentialDataBody
import com.baghdad.remoteDataSource.request.RequestTokenBody
import com.baghdad.remoteDataSource.response.RequestTokenResponse
import com.baghdad.remoteDataSource.response.SessionResponse
import com.baghdad.remoteDataSource.response.user.UserResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface AuthenticationApi {
    @Authenticated
    @GET("authentication/token/new")
    suspend fun getRequestToken(): RequestTokenResponse

    @Authenticated
    @POST("authentication/token/validate_with_login")
    suspend fun validateCredential(@Body body: CredentialDataBody): RequestTokenResponse

    @Authenticated
    @POST("authentication/session/new")
    suspend fun createSession(@Body body: RequestTokenBody): SessionResponse

    @Authenticated
    @GET("account")
    suspend fun getUserDetails(
        @Query("session_id") sessionId: String
    ): UserResponse

    @Authenticated
    @POST("authentication/session")
    suspend fun deleteSession(
        @Query("session_id") sessionId: String
    ): SessionResponse
}