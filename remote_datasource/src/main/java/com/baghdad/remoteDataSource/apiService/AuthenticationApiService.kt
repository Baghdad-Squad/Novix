package com.baghdad.remoteDataSource.apiService

import com.baghdad.remoteDataSource.interceptor.Authenticated
import com.baghdad.remoteDataSource.interceptor.RequiresSession
import com.baghdad.remoteDataSource.request.CredentialDataBody
import com.baghdad.remoteDataSource.request.RequestTokenBody
import com.baghdad.remoteDataSource.response.token.RequestTokenResponse
import com.baghdad.remoteDataSource.response.session.SessionResponse
import com.baghdad.remoteDataSource.response.user.UserResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST

interface AuthenticationApiService {
    @Authenticated
    @GET("authentication/token/new")
    suspend fun getRequestToken(): Response<RequestTokenResponse>

    @Authenticated
    @POST("authentication/token/validate_with_login")
    suspend fun validateCredential(
        @Body body: CredentialDataBody,
    ): Response<RequestTokenResponse>

    @Authenticated
    @POST("authentication/session/new")
    suspend fun createSession(
        @Body body: RequestTokenBody,
    ): Response<SessionResponse>

    @Authenticated
    @RequiresSession
    @GET("account")
    suspend fun getUserDetails(): Response<UserResponse>

    @Authenticated
    @RequiresSession
    @DELETE("authentication/session")
    suspend fun deleteSession(): Response<SessionResponse>
}