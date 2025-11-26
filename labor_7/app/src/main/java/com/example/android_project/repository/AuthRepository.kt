package com.example.android_project.repository

import android.content.Context
import com.example.android_project.model.AuthRequest
import com.example.android_project.network.RetrofitClient

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AuthRepository(context: Context) {
    private val api = RetrofitClient.getInstance(context)

    suspend fun login(email: String, password: String) = withContext(Dispatchers.IO) {
        api.login(AuthRequest(email = email, password = password))
    }

    suspend fun register(email: String, password: String, name: String) = withContext(Dispatchers.IO) {
        api.register(AuthRequest(email = email, password = password, username = name))
    }
}
