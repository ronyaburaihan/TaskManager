package com.techdoctorbd.taskmanagermongodb.api

import com.techdoctorbd.taskmanagermongodb.models.AuthResponse
import com.techdoctorbd.taskmanagermongodb.models.User
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface TaskManagerApi {

    @POST("users")
    suspend fun registerUser(@Body user: User): Response<AuthResponse>

    @POST("users/login")
    suspend fun loginUser(@Body user: User): Response<AuthResponse>
}