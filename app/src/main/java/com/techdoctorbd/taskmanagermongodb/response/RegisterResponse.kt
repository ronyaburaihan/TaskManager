package com.techdoctorbd.taskmanagermongodb.response


import com.google.gson.annotations.SerializedName
import com.techdoctorbd.taskmanagermongodb.models.User

data class RegisterResponse(
    @SerializedName("token")
    val token: String,
    @SerializedName("user")
    val user: User
)