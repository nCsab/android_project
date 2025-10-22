package com.example.labor_1.repository

import android.content.Context
import com.example.labor_1.model.AuthRequest
import com.example.labor_1.network.RetrofitClient

class AuthRepository(context: Context) {
    private val api = RetrofitClient.getInstance(context)

    suspend fun login(email: String, password: String) =
        api.login(AuthRequest(email = email, password = password))

    suspend fun register(email: String, password: String, name: String) =
        api.register(AuthRequest(email = email, password = password, name = name))
}
