package com.example.labor_1.network

import android.content.Context
import android.util.Log
import com.example.labor_1.utils.SessionManager
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(context: Context) : Interceptor {
    private val sessionManager = SessionManager(context.applicationContext)

    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        val builder = original.newBuilder()

        val token = sessionManager.fetchAuthToken()
        Log.d(
            "AuthInterceptor",
            "Request ${original.method()} ${original.url()} | hasToken=${token != null}"
        )
        token?.let { builder.addHeader("Authorization", "Bearer $it") }

        return chain.proceed(builder.build())
    }
}
