package com.techdoctorbd.taskmanagermongodb.data.api

import com.techdoctorbd.taskmanagermongodb.data.models.AuthResponse
import com.techdoctorbd.taskmanagermongodb.data.models.Task
import com.techdoctorbd.taskmanagermongodb.data.models.User
import retrofit2.Response
import retrofit2.http.*

interface TaskManagerApi {

    @POST("users")
    suspend fun registerUser(@Body user: User): Response<AuthResponse>

    @POST("users/login")
    suspend fun loginUser(@Body user: User): Response<AuthResponse>

    @POST("users/logout")
    suspend fun logoutUser(): Response<AuthResponse>

    @GET("users/me")
    suspend fun readProfile(): Response<User>

    @POST("tasks")
    suspend fun addTask(@Body task: Task): Response<Task>

    @GET("tasks")
    suspend fun getTasks(@QueryMap queries: Map<String, String>): Response<List<Task>>

    @DELETE("tasks/{id}")
    suspend fun deleteTask(@Path("id") taskId: String): Response<Task>

    @PATCH("tasks/{id}")
    suspend fun updateTask(@Path("id") taskId: String, @Body task: Task): Response<Task>
}