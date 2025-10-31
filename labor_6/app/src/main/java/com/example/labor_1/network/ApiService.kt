package com.example.labor_1.network

import com.example.labor_1.model.AuthRequest
import com.example.labor_1.model.AuthResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("auth/local/signin")
    suspend fun login(@Body request: AuthRequest): Response<AuthResponse>

    @POST("auth/local/signup")
    suspend fun register(@Body request: AuthRequest): Response<AuthResponse>
}
