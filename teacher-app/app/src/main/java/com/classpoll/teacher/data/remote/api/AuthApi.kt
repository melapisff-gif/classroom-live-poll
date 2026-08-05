package com.classpoll.teacher.data.remote.api

import com.classpoll.teacher.data.remote.dto.*
import retrofit2.Response
import retrofit2.http.*

interface AuthApi {
    @POST("auth/teacher/register")
    suspend fun register(@Body request: RegisterRequest): Response<AuthResponse>

    @POST("auth/teacher/login")
    suspend fun login(@Body request: LoginRequest): Response<AuthResponse>

    @POST("auth/refresh-token")
    suspend fun refreshToken(@Body request: RefreshTokenRequest): Response<TokenResponse>

    @POST("auth/logout")
    suspend fun logout(@Body request: LogoutRequest): Response<Unit>
}
