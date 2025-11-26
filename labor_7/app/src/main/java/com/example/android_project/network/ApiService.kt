package com.example.android_project.network

import com.example.android_project.model.AuthRequest
import com.example.android_project.model.AuthResponse
import com.example.android_project.model.CreateHabitRequest
import com.example.android_project.model.CreateScheduleRequest
import com.example.android_project.model.Habit
import com.example.android_project.model.HabitCategory
import com.example.android_project.model.HabitResponse
import com.example.android_project.model.ScheduleResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

import com.google.gson.JsonElement

interface ApiService {

    @POST("/auth/local/signin")
    suspend fun login(@Body request: AuthRequest): Response<AuthResponse>

    @POST("/auth/local/signup")
    suspend fun register(@Body request: AuthRequest): Response<AuthResponse>

    @GET("/schedule")
    suspend fun getScheduleByDay(): List<ScheduleResponse>

    @GET("/habit/categories")
    suspend fun getHabitCategories(): List<HabitCategory>

    @POST("/habit")
    suspend fun createHabit(@Body habit: CreateHabitRequest): Response<Habit>

    @GET("/habit")
    suspend fun getHabits(): List<HabitResponse>

    @POST("/schedule/custom")
    suspend fun createSchedule(@Body schedule: CreateScheduleRequest): Response<JsonElement>
}
