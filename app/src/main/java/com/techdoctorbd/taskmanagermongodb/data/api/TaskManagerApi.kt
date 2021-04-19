package com.techdoctorbd.taskmanagermongodb.data.api

import com.techdoctorbd.taskmanagermongodb.data.models.AuthResponse
import com.techdoctorbd.taskmanagermongodb.data.models.User
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface TaskManagerApi {

    @POST("users")
    suspend fun registerUser(@Body user: User): Response<AuthResponse>

    @POST("users/login")
    suspend fun loginUser(@Body user: User): Response<AuthResponse>

    @GET("users/me")
    suspend fun readProfile(): Response<User>
}