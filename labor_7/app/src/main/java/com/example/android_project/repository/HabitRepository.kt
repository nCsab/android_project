package com.example.android_project.repository

import android.content.Context
import com.example.android_project.model.Habit
import com.example.android_project.model.HabitCategory
import com.example.android_project.model.HabitResponse
import com.example.android_project.network.RetrofitClient

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class HabitRepository(context: Context) {
    private val api by lazy { RetrofitClient.getInstance(context) }

    suspend fun getHabitCategories(): List<HabitCategory> = withContext(Dispatchers.IO) {
        api.getHabitCategories()
    }

    suspend fun getHabits(): List<HabitResponse> = withContext(Dispatchers.IO) {
        api.getHabits()
    }

    suspend fun createHabit(name: String, description: String, categoryId: Long, goal: String): Habit? = withContext(Dispatchers.IO) {
        val request = com.example.android_project.model.CreateHabitRequest(
            name = name,
            description = description,
            categoryId = categoryId,
            goal = goal
        )
        val response = api.createHabit(request)
        if (response.isSuccessful) {
            response.body()
        } else {
            null
        }
    }
}
