package com.example.android_project.repository

import android.content.Context
import com.example.android_project.model.ScheduleResponse
import com.example.android_project.network.RetrofitClient

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ScheduleRepository(context: Context) {
    private val api by lazy { RetrofitClient.getInstance(context) }

    suspend fun getScheduleByDay(): List<ScheduleResponse> = withContext(Dispatchers.IO) {
        api.getScheduleByDay()
    }

    suspend fun createSchedule(
        habitId: Long,
        startTime: String,
        endTime: String,
        durationMinutes: Int,
        notes: String
    ): String? = withContext(Dispatchers.IO) {
        val request = com.example.android_project.model.CreateScheduleRequest(
            habitId = habitId,
            date = startTime,
            startTime = startTime,
            endTime = endTime,
            durationMinutes = durationMinutes,
            notes = notes
        )
        try {
            val response = api.createSchedule(request)
            if (response.isSuccessful) {
                null
            } else {
                "Error ${response.code()}: ${response.errorBody()?.string()}"
            }
        } catch (e: Exception) {
            "Exception: ${e.message}"
        }
    }
}
