package com.techdoctorbd.taskmanagermongodb.api

import com.techdoctorbd.taskmanagermongodb.models.User
import com.techdoctorbd.taskmanagermongodb.response.RegisterResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST

interface TaskManagerApi {

    @POST("/users")
    suspend fun registerUser(@Body user: User): RegisterResponse
}