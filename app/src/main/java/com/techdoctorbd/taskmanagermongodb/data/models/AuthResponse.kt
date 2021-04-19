package com.techdoctorbd.taskmanagermongodb.data.models


import com.google.gson.annotations.SerializedName

data class AuthResponse(
    @SerializedName("token")
    val token: String,
    @SerializedName("user")
    val user: User
)